package com.example.ribbonconsumer.controller;

import com.example.ribbonconsumer.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ConsumerController {
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/ribbon-consumer")
    public String helloConsumer() {
        return restTemplate.getForEntity("http://USER-SERVICE/hello", String.class).getBody();
    }

    @RequestMapping("/getDidi")
    public User getConsumerDidi() {
        return restTemplate.getForEntity("http://USER-SERVICE/user?name={1}", User.class,"didi").getBody();
    }

    @RequestMapping("/getDada")
    public User getConsumerDada() {
        Map<String,String> params = new HashMap<>();
        params.put("name","dada");
        ResponseEntity<User> responseEntity = restTemplate.getForEntity("http://USER-SERVICE/user?name={name}",User.class,params);
        return responseEntity.getBody();
    }

    @RequestMapping("/getDodo")
    public User getConsumerDodo() {
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString("http://USER-SERVICE/user?name={name}")
                .build()
                .expand("dodo")
                .encode();
        URI uri = uriComponents.toUri();
        ResponseEntity<User> responseEntity = restTemplate.getForEntity(uri,User.class);
        return responseEntity.getBody();
    }

    @RequestMapping("/addDidi")
    public User addDidi(){
        User user=new User("didi",30);
        ResponseEntity<User> responseEntity = restTemplate.postForEntity("http://USER-SERVICE/addUser",user,User.class);
        User body = responseEntity.getBody();
        return body;
    }

    @RequestMapping("/addDada")
    public User addDada(){
        User user=new User("dada",30);
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<User> responseEntity = restTemplate.postForEntity("http://USER-SERVICE/addUser",request,User.class);
        User body = responseEntity.getBody();
        return body;
    }

    @RequestMapping("/addDodo")
    public URI addDodo(){
        User user=new User("dada",30);
        URI responseURI = restTemplate.postForLocation("http://USER-SERVICE/addUser",user);
        return responseURI;
    }

    @RequestMapping("/putId")
    public void putId(){
        Long id = 1L;
        User user = new User("didi",50);
        restTemplate.put("http://USER-SERVICE/user/{1}",user,id);
    }

    @RequestMapping("/delete")
    public void deleteById(){
        Long id= 1L;
        restTemplate.delete("http://USER-SERVICE/users/{1}",id);
    }

}
