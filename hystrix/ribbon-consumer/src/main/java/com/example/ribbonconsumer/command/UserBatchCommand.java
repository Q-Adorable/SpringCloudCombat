package com.example.ribbonconsumer.command;

import com.example.ribbonconsumer.entity.User;
import com.example.ribbonconsumer.service.UserCollapseByExtendsService;
import com.netflix.hystrix.HystrixCommand;

import java.util.ArrayList;
import java.util.List;

import static com.netflix.hystrix.HystrixCommandGroupKey.Factory.asKey;


public class UserBatchCommand extends HystrixCommand<List<User>> {

    private UserCollapseByExtendsService userCollapseByExtendsService;
    private List<Long> userIds;

    public UserBatchCommand(UserCollapseByExtendsService userCollapseByExtendsService, List<Long> userIds) {
        super(Setter.withGroupKey(asKey("userBatchCommand")));
        this.userCollapseByExtendsService = userCollapseByExtendsService;
        this.userIds = userIds;
    }

    @Override
    protected List<User> run() throws Exception {
        return userCollapseByExtendsService.findAll(userIds);
    }

    @Override
    protected List<User> getFallback(){
        List<User> users = new ArrayList<User>();
        users.add(new User("失败",99));
        return new ArrayList<User>(users);
    }
}
