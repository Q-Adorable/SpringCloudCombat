package com.example.ribbonconsumer.service;

import com.example.ribbonconsumer.entity.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class UserService {
    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(commandKey = "getUserById", groupKey = "UserGroup", threadPoolKey = "getUserByIdThread", fallbackMethod = "getError")
    @CacheResult(cacheKeyMethod = "getCacheKey")
    public User getUserById(Long id) {
        return restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, id);
    }

    @HystrixCommand
    public Future<User> getUserByIdAsync(final Long id) {
        return new AsyncResult<User>() {
            @Override
            public User invoke() {
                return restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, id);
            }
        };
    }

    public User getError(Long id) {
        return new User();
    }

    public String getCacheKey(Long id) {
        return String.valueOf(id);
    }
}
