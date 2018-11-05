package com.example.ribbonconsumer.service;

import com.example.ribbonconsumer.entity.User;

import java.util.List;

public interface UserCollapseByExtendsService {
    public User find(Long id);

    public List<User> findAll(List<Long> ids);
}
