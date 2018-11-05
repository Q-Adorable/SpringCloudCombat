package com.example.userservice.controller;

import com.example.userservice.entity.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable Long id) {
        if (id == 1) {
            System.out.println(">>>>>>>>/users/{id}");
            User userCache = new User("dodo", 18);
            userCache.setId(id);
            return userCache;
        }
        User user = new User("didi", 20);
        user.setId(id);
        System.out.println(">>>>>>>>>>>>>>>>>>>");
        return user;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> findAll(@RequestParam String ids){
        System.out.println(">>>>>>>>请求合并:"+ids);
        ArrayList<User> users=new ArrayList<User>();
        users.add(new User(ids,11));
        users.add(new User(ids,12));
        users.add(new User(ids,13));
        users.add(new User(ids,14));
        return users;
    }
}
