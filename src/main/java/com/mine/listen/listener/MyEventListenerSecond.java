package com.mine.listen.listener;

import com.mine.listen.event.MyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author CaoYang
 * @create 2023-02-19-11:20 AM
 * @description 第二个自定义的事件监听器
 */
@Component
public class MyEventListenerSecond {

    @EventListener
    public void handleEvent(MyEvent event) {
        if (Objects.isNull(event)) {
            throw new RuntimeException("监听的事件为空！");
        }
        // 这里写的是事件的处理逻辑
        System.out.println(getClass().getName() + "处理事件：" + event.getClass().getName() + "，消息为 ==> " + event.getMessage());
    }

}
