package com.example.ribbonconsumer.controller;

import com.example.ribbonconsumer.command.UserCollapseCommand;
import com.example.ribbonconsumer.command.UserCommand;
import com.example.ribbonconsumer.command.UserObservableCommand;
import com.example.ribbonconsumer.entity.User;
import com.example.ribbonconsumer.service.ObservableUserService;
import com.example.ribbonconsumer.service.UserCollapseByExtendsService;
import com.example.ribbonconsumer.service.UserCollapseService;
import com.example.ribbonconsumer.service.UserService;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class UserController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserService userService;

    @Autowired
    ObservableUserService observableUserService;

    @Autowired
    UserCollapseByExtendsService userCollapseByExtendsService;

    @Autowired
    UserCollapseService userCollapseService;

    @RequestMapping(value = "sync-get")
    public User getBySync() {
        HystrixRequestContext.initializeContext();
        UserCommand userCommand = new UserCommand(restTemplate, 10L);
        User user = userCommand.execute();
        return user;
    }

    @RequestMapping(value = "async-get")
    public User getByAsync() throws ExecutionException, InterruptedException {
        HystrixRequestContext.initializeContext();
        UserCommand userCommand = new UserCommand(restTemplate, 11L);
        Future<User> futureUser = userCommand.queue();
        return futureUser.get();
    }

    @RequestMapping(value = "by-observe")
    public Observable<User> getUserByObserve() {
        HystrixRequestContext.initializeContext();
        UserCommand userCommand = new UserCommand(restTemplate, 12L);
        Observable<User> ho = userCommand.observe();
        return ho;
    }

    @RequestMapping(value = "by-to-observable")
    public Observable<User> getUserByToObservable() {
        HystrixRequestContext.initializeContext();
        UserCommand userCommand = new UserCommand(restTemplate, 13L);
        Observable<User> co = userCommand.toObservable();
        return co;
    }

    @RequestMapping(value = "sync-get-anno-cache")
    public User getUserByIdAndSync() {
        HystrixRequestContext.initializeContext();
        User user1 = userService.getUserById(1L);
        User user2 = userService.getUserById(1L);
        User user3 = userService.getUserById(1L);
        System.out.println("user1:" + user1);
        System.out.println("user2:" + user2);
        System.out.println("user3:" + user3);
        return user1;
    }

    @RequestMapping(value = "async-get-anno")
    public User getUserByIdAndAsync() throws ExecutionException, InterruptedException {
        return userService.getUserByIdAsync(15L).get();
    }

    @RequestMapping(value = "observe")
    public Observable<User> getUserByExtends1() {
        Observable<User> observable = new UserObservableCommand(restTemplate, 16L).observe();
        return observable;
    }

    @RequestMapping(value = "observable")
    public Observable<User> getUserByExtends2() {
        Observable<User> observable = new UserObservableCommand(restTemplate, 17L).toObservable();
        return observable;
    }

    @RequestMapping(value = "observable-anno")
    public Observable<User> getUserById() {
        return observableUserService.getUserById(18L);
    }

    @RequestMapping(value = "observable-anno-list")
    public Observable<User> getUsers() {
        return observableUserService.getUsers(19L);
    }

    @RequestMapping(value = "test-cache")
    public User testCahce() {
        HystrixRequestContext.initializeContext();
        User user1 = new UserCommand(restTemplate, 1L).execute();
        User user2 = new UserCommand(restTemplate, 1L).execute();
        User user3 = new UserCommand(restTemplate, 1L).execute();
        System.out.println("user1:" + user1);
        System.out.println("user2:" + user2);
        System.out.println("user3:" + user3);
        return user1;
    }

    @RequestMapping(value = "test-no-cache")
    public Observable<User> testNoCache() {
        Observable<User> observable1 = new UserObservableCommand(restTemplate, 1L).observe();
        Observable<User> observable2 = new UserObservableCommand(restTemplate, 1L).observe();
        Observable<User> observable3 = new UserObservableCommand(restTemplate, 1L).observe();
        System.out.println("observable1:" + observable1);
        System.out.println("observable2:" + observable2);
        System.out.println("observable3:" + observable3);
        return observable1;
    }

    @RequestMapping(value = "test-cache-clear")
    public User testCacheClear() {
        HystrixRequestContext.initializeContext();
        User user1 = new UserCommand(restTemplate, 1L).execute();
        UserCommand.flushCache(1L);
        User user2 = new UserCommand(restTemplate, 1L).execute();
        User user3 = new UserCommand(restTemplate, 1L).execute();
        System.out.println("user1:" + user1);
        System.out.println("user2:" + user2);
        System.out.println("user3:" + user3);
        return user1;
    }

    @RequestMapping(value = "test-request-merger")
    public void testMerger() throws ExecutionException, InterruptedException {
//        HystrixRequestContext context = HystrixRequestContext.initializeContext();
//        Future<User> f1 = new UserCollapseCommand(userCollapseByExtendsService, 1L).queue();
//        Future<User> f2 = new UserCollapseCommand(userCollapseByExtendsService, 2L).queue();
//        Future<User> f3 = new UserCollapseCommand(userCollapseByExtendsService, 3L).queue();
//        Thread.sleep(3000);
//        Future<User> f4 = new UserCollapseCommand(userCollapseByExtendsService, 4L).queue();
//        User user1 = f1.get();
//        User user2 = f2.get();
//        User user3 = f3.get();
//        User user4 = f4.get();
//        System.out.println("book1>>>" + user1);
//        System.out.println("book2>>>" + user2);
//        System.out.println("book3>>>" + user3);
//        System.out.println("book4>>>" + user4);
//        context.close();


        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        UserCollapseCommand uc1 = new UserCollapseCommand(userCollapseByExtendsService, 1L);
        UserCollapseCommand uc2 = new UserCollapseCommand(userCollapseByExtendsService, 2L);
        UserCollapseCommand uc3 = new UserCollapseCommand(userCollapseByExtendsService, 3L);
        Future<User> f1=uc1.queue();
        Future<User> f2=uc2.queue();
        User user1 = f1.get();
        User user2 = f2.get();
        User user3 = uc3.queue().get();
        Thread.sleep(3000);
        UserCollapseCommand uc4 = new UserCollapseCommand(userCollapseByExtendsService, 4L);
        User user4 = uc4.queue().get();
        System.out.println("book1>>>" + user1);
        System.out.println("book2>>>" + user2);
        System.out.println("book3>>>" + user3);
        System.out.println("book4>>>" + user4);
        context.close();
    }

    @RequestMapping(value = "test-request-merger2")
    public void testMerger2() throws ExecutionException, InterruptedException {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        Future<User> f1=userCollapseService.find(1L);
        Future<User> f2=userCollapseService.find(2L);
        Future<User> f3=userCollapseService.find(3L);
        User user1 = f1.get();
        User user2 = f2.get();
        User user3 = f3.get();
        Thread.sleep(3000);
        User user4=userCollapseService.find(4L).get();
        System.out.println("book1>>>" + user1);
        System.out.println("book2>>>" + user2);
        System.out.println("book3>>>" + user3);
        System.out.println("book4>>>" + user4);
        context.close();
    }
}
