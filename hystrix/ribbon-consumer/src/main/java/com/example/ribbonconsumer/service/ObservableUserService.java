package com.example.ribbonconsumer.service;

import com.example.ribbonconsumer.entity.User;
import com.example.ribbonconsumer.exception.BadRequestException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.ObservableExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Subscriber;

import java.util.ArrayList;
import java.util.List;

@Service
public class ObservableUserService {
    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(observableExecutionMode = ObservableExecutionMode.EAGER, fallbackMethod = "observableFailed")
    public Observable<User> getUserById(Long id) {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        User user = restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, id);
                        observer.onNext(user);
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        });
    }

    @HystrixCommand(fallbackMethod = "defaultUserSec")
    public User observableFailed(Long id) {
        //此处可能是另外一个网络请求来获取，所以也可能失败
        return new User();
    }

    public User defaultUserSec(Long id) {
        return new User();
    }

    @HystrixCommand(observableExecutionMode = ObservableExecutionMode.EAGER)
    public Observable<User> getUsers(Long id) {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> observer) {
                List<Long> ids = new ArrayList<Long>();
                List<User> users = new ArrayList<User>();
                ids.add(id);
                ids.add(id);
                try {
                    if (!observer.isUnsubscribed()) {
                        for (Long id : ids) {
                            User user = restTemplate.getForObject("http://USER-SERVICE/users/{1}", User.class, id);
                            users.add(user);
                            observer.onNext(user);
                        }
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        });
    }
}
