package com.example.ribbonconsumer.command;

import com.example.ribbonconsumer.entity.User;
import com.netflix.hystrix.HystrixObservableCommand;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

import static com.netflix.hystrix.HystrixCommandGroupKey.Factory.asKey;

public class UserObservableCommand extends HystrixObservableCommand<User> {
    private RestTemplate restTemplate;
    private Long id;

    public UserObservableCommand(RestTemplate restTemplate, Long id) {
        super(Setter.withGroupKey(asKey("")));
        this.restTemplate = restTemplate;
        this.id = id;
    }

    @Override
    protected Observable<User> construct() {
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

    @Override
    protected Observable<User> resumeWithFallback() {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        User user = new User();
                        observer.onNext(user);
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }
}
