package com.mine.listen.publisher;

import com.mine.listen.event.MyEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author CaoYang
 * @create 2023-02-19-1:04 PM
 * @description 建立事件与监听器的联系，在这里将发生的事件发布出去
 */
@Component
public class MyPublisher {

    @Autowired
    private ApplicationEventPublisher publisher;

    public void publishEvent() {
        MyEvent myEvent = new MyEvent("MySQL", "发生自MySQL数据库的事件");
        publisher.publishEvent(myEvent);
    }

}
