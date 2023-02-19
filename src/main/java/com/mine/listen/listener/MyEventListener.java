package com.mine.listen.listener;

import com.mine.listen.event.MyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author CaoYang
 * @create 2023-02-19-11:13 AM
 * @description 自定义的事件监听器
 */
@Component
public class MyEventListener implements ApplicationListener<MyEvent> {
    @Override
    public void onApplicationEvent(MyEvent event) {
        if (Objects.isNull(event)) {
            throw new RuntimeException("监听的事件为空！");
        }
        // 这里写事件处理逻辑
        System.out.println(getClass().getName() + "监听器正在监听事件" + event.getClass().getName() +  "，消息为：" + (Objects.nonNull(event) ? event.getMessage() : "消息为空" ));
    }
}
