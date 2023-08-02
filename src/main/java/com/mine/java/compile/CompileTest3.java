package com.mine.java.compile;

import com.mine.java.compile.anno.ActionListenerFor;
import com.mine.java.compile.processor.ActionListenerInstaller;
import javax.swing.*;
import java.awt.*;

/**
 * @author CaoY
 * @date 2023-08-02 12:45
 * @description 脚本、编译与注解处理 - 注解的学习
 * 自定义添加动作监听器的注解，定义其解析器，并让注解发挥作用（本质上需要调用）
 */
public class CompileTest3 {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            ButtonFrame frame = new ButtonFrame();
            frame.setTitle("ButtonTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    public static class ButtonFrame extends JFrame {
        private static final int DEFAULT_WIDTH = 300;
        private static final int DEFAULT_HEIGHT = 200;

        private JPanel panel;
        private JButton yellowButton;
        private JButton blueButton;
        private JButton redButton;

        public ButtonFrame() {
            setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

            panel = new JPanel();
            add(panel);

            yellowButton = new JButton("Yellow");
            blueButton = new JButton("Blue");
            redButton = new JButton("Red");

            panel.add(yellowButton);
            panel.add(blueButton);
            panel.add(redButton);

            ActionListenerInstaller.processAnnotations(this);
        }

        @ActionListenerFor(source = "yellowButton")
        public void yellowBackground() {
            panel.setBackground(Color.YELLOW);
        }

        @ActionListenerFor(source = "blueButton")
        public void blueBackground() {
            panel.setBackground(Color.BLUE);
        }

        @ActionListenerFor(source = "redButton")
        public void redBackground() {
            panel.setBackground(Color.RED);
        }
    }
}
