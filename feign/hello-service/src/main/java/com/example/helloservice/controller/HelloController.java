package com.example.helloservice.controller;

import com.example.helloservice.entity.User;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class HelloController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String index() throws Exception {
        int sleepTime = new Random().nextInt(3000);
        System.out.println("sleepTime:"+sleepTime);
        Thread.sleep(sleepTime);
        System.out.println("/hello");
        return "Hello World";
    }

    @RequestMapping(value = "/hello1", method = RequestMethod.GET)
    public String hello(@RequestParam String name) {
        return "Hello " + name;
    }

    @RequestMapping(value = "/hello2", method = RequestMethod.GET)
    public User hello(@RequestHeader String name, @RequestHeader Integer age) {
        return new User(name, age);
    }

    @RequestMapping(value = "/hello3", method = RequestMethod.POST)
    public String hello(@RequestBody User user) {
        return "Hello " + user.getName() + ", " + user.getAge();
    }

    @RequestMapping(value = "hello4", method = RequestMethod.GET)
    public String hello2(@RequestParam String name) {
        return "Hello " + name;
    }

    @RequestMapping(value = "hello5", method = RequestMethod.GET)
    public User hello2(@RequestHeader String name, @RequestHeader Integer age) {
        return new User(name, age);
    }

    @RequestMapping(value = "hello6", method = RequestMethod.POST)
    public String hello2(@RequestBody User user) {
        return "Hello " + user.getName() + ", " + user.getAge();
    }
}
