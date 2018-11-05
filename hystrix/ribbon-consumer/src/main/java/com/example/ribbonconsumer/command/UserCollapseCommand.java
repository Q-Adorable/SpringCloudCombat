package com.example.ribbonconsumer.command;

import com.example.ribbonconsumer.entity.User;
import com.example.ribbonconsumer.service.UserCollapseByExtendsService;
import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserCollapseCommand extends HystrixCollapser<List<User>, User, Long> {

    private UserCollapseByExtendsService userCollapseByExtendsService;
    private Long userId;

    public UserCollapseCommand(UserCollapseByExtendsService userCollapseByExtendsService, Long userId) {
        super(Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("userCollapseCommand")).andCollapserPropertiesDefaults(
                HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(100)));
        this.userCollapseByExtendsService = userCollapseByExtendsService;
        this.userId = userId;
    }

    /**
     *获取请求参数
     */
    @Override
    public Long getRequestArgument() {
        return userId;
    }

    /**
     *合并请求产生批量命令的具体实现
     */
    @Override
    protected HystrixCommand<List<User>> createCommand(Collection<CollapsedRequest<User, Long>> collapsedRequests) {
        List<Long> userIds = new ArrayList<>(collapsedRequests.size());
        userIds.addAll(collapsedRequests
                .stream()
                .map(CollapsedRequest::getArgument)
                .collect(Collectors.toList()));
        return new UserBatchCommand(userCollapseByExtendsService, userIds);
    }

    /**
     *批量命令结果返回后的处理，需要实现将批量结果拆分并传递给合并前的各原子请求命令的逻辑中
     */
    @Override
    protected void mapResponseToRequests(List<User> batchResponse, Collection<CollapsedRequest<User, Long>> collapsedRequests) {
        System.out.println("mapResponseToRequests");
        int count = 0;
        for (CollapsedRequest<User, Long> collapsedRequest : collapsedRequests) {
            User user = batchResponse.get(count++);
            collapsedRequest.setResponse(user);
        }
    }
}
