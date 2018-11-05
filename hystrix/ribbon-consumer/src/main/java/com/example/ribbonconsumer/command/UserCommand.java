package com.example.ribbonconsumer.command;

import com.example.ribbonconsumer.entity.User;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import org.springframework.web.client.RestTemplate;

public class UserCommand extends HystrixCommand<User> {
    private static final HystrixCommandKey GETTER_KEY=HystrixCommandKey.Factory.asKey("UserCommandKey");
    private RestTemplate restTemplate;
    private Long id;

    public UserCommand(RestTemplate restTemplate, Long id) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("UserGroup"))
        .andCommandKey(GETTER_KEY)
        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("UserThreadPool")));
        this.restTemplate = restTemplate;
        this.id = id;
    }

    @Override
    protected User run() {
        return restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, id);
    }

    @Override
    protected User getFallback() {
        return new User();
    }

    @Override
    protected String getCacheKey(){
        return String.valueOf(id);
    }

    public static void flushCache(Long id){
        HystrixRequestCache.getInstance(GETTER_KEY,HystrixConcurrencyStrategyDefault.getInstance()).clear(String.valueOf(id));
    }
}
