package com.thoughtworks.tw_mall_project.controller;

import com.thoughtworks.tw_mall_project.entity.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String index() {
        return "Hello World";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public User getUser(@RequestParam String name) {
        User user = new User(name, 20);
        return user;
    }

    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public User addUser(@RequestBody User user){
        return user;
    }

    @RequestMapping(value = "/addUser/{name}",method = RequestMethod.POST)
    public User addUser(@PathVariable String name){
        User user = new User(name,40);
        return user;
    }

    @RequestMapping(value = "user/{id}",method = RequestMethod.PUT)
    public User putUserById(@PathVariable Long id,@RequestBody User user){
        System.out.println(user.getName()+":"+user.getAge());
        return user;
    }

    @RequestMapping(value = "users/{id}",method = RequestMethod.DELETE)
    public Long deleteUserById(@PathVariable Long id){
        System.out.println(id);
        return id;
    }

}
