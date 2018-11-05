package com.example.ribbonconsumer.service;

import com.example.ribbonconsumer.entity.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class UserCollapseService {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCollapser(batchMethod = "findAll", collapserProperties ={
            @HystrixProperty(name="timerDelayInMilliseconds", value = "100")
    })
    public Future<User> find(Long id){
        return null;
    }

    @HystrixCommand
    public List<User> findAll(List<Long> ids){
        System.out.println("findAll---------"+ids+"Thread.currentThread().getName():" + Thread.currentThread().getName());
        User[] users=restTemplate.getForObject("http://USER-SERVICE/users?ids={1}", User[].class, StringUtils.join(ids, ","));
        return Arrays.asList(users);
    }
}
