package com.example.ribbonconsumer.service;

import com.example.ribbonconsumer.entity.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class UserCollapseByExtendsServiceImpl implements UserCollapseByExtendsService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public User find(Long id) {
        return restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, id);
    }

    @Override
    public List<User> findAll(List<Long> ids) {
        System.out.println("findAll---------"+ids+"Thread.currentThread().getName():" + Thread.currentThread().getName());
        User[] users=restTemplate.getForObject("http://USER-SERVICE/users?ids={1}", User[].class, StringUtils.join(ids, ","));
        return Arrays.asList(users);
    }
}
