package com.example.hello_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class HelloController {
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String index() {
        return "Hello World";
    }

    @RequestMapping(value = "/sleep", method = RequestMethod.GET)
    public String hello() throws Exception {
        int sleepTime = new Random().nextInt(3000);
        System.out.println("sleepTime : "+sleepTime);
        Thread.sleep(sleepTime);
        return "Hello Word"+sleepTime;
    }
}
