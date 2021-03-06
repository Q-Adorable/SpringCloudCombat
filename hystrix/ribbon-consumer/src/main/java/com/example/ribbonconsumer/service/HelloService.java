package com.example.ribbonconsumer.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloService {
    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "helloFallback")
    public String hello() {
        return restTemplate.getForEntity("http://HELLO-SERVICE/hello", String.class).getBody();
    }


    @HystrixCommand(fallbackMethod = "sleepFallback")
    public String sleep() {
        return restTemplate.getForEntity("http://HELLO-SERVICE/sleep", String.class).getBody();
    }

    public String helloFallback() {
        return "error";
    }

    public String sleepFallback(){
        return "timeOut";
    }
}
