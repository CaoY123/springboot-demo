package com.mine.java.compile.processor;

import com.mine.java.compile.anno.ActionListenerFor;

import java.awt.event.*;
import java.lang.reflect.*;

/**
 * ActionListenerFor 注解的处理类，使 ActionListenerFor 注解实际发挥作用的类
 */
public class ActionListenerInstaller {
   /**
    * 处理给定对象中的所有ActionListenerFor注释
    * @param obj 一个对象，它的方法可能有ActionListenerFor注释
    */
   public static void processAnnotations(Object obj) {
      try {
         Class<?> cl = obj.getClass();
         for (Method m : cl.getDeclaredMethods()) {
            ActionListenerFor a = m.getAnnotation(ActionListenerFor.class);
            if (a != null) {
               Field f = cl.getDeclaredField(a.source());
               f.setAccessible(true);
               addListener(f.get(obj), obj, m);
            }
         }
      }
      catch (ReflectiveOperationException e) {
         e.printStackTrace();
      }
   }

   /**
    * 添加调用给定方法的操作监听器
    * @param source 向其添加操作监听器的事件源
    * @param param 监听器调用的方法的隐式参数
    * @param m 监听器调用的方法
    */
   public static void addListener(Object source, final Object param, final Method m)
         throws ReflectiveOperationException {
      InvocationHandler handler = new InvocationHandler() {
         public Object invoke(Object proxy, Method mm, Object[] args) throws Throwable {
            return m.invoke(param);
         }
      };

      Object listener = Proxy.newProxyInstance(null,
         new Class[] { java.awt.event.ActionListener.class }, handler);
      Method adder = source.getClass().getMethod("addActionListener", ActionListener.class);
      adder.invoke(source, listener);
   }
}