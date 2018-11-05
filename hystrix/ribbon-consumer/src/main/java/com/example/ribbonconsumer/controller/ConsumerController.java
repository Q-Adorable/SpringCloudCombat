package com.example.ribbonconsumer.controller;

import com.example.ribbonconsumer.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ConsumerController {
    @Autowired
    HelloService helloService;

    @RequestMapping(value = "ribbon-consumer")
    public String helloConsumer() {
        return helloService.hello();
    }

    @RequestMapping(value = "sleep")
    public String sleep() {
        return helloService.sleep();
    }
}
