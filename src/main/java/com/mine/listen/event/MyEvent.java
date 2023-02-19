package com.mine.listen.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author CaoYang
 * @create 2023-02-19-11:08 AM
 * @description 个人定义的机制
 */
public class MyEvent extends ApplicationEvent {

    private String message;

    public MyEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
