package com.mine.java.security;

import com.mine.java.internation.assist.GBC;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author CaoY
 * @date 2023-08-03 15:29
 * @description Java 的安全机制
 * 如启动下列 main 函数后，运行输入 com.mine.java.security.Caesar，会执行其中的 main 函数，
 * 只是默认情况下是不会传递给被执行的main函数什么参数的，故不会产生什么行为。所需要注意的是我们自定义的类加
 * 载器加载所得到的类。
 *
 * 扩展：
 *  1. 若对已经编译好的 class 文件进行修改，Java 的安全机制能检测到这种变化，并会阻止程序的运行。
 */
public class SecurityTest1 {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            ClassLoaderFrame frame = new ClassLoaderFrame();
            frame.setTitle("ClassLoaderTest");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

/**
 * 此框架包含两个文本字段，用于要加载的类的名称和解密密钥。
 */
class ClassLoaderFrame extends JFrame {
    private JTextField keyField = new JTextField("3", 4);
    private JTextField nameField = new JTextField("Calculator", 30);
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 200;

    public ClassLoaderFrame() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLayout(new GridBagLayout());
        add(new JLabel("Class"), new GBC(0, 0).setAnchor(GBC.EAST));
        add(nameField, new GBC(1, 0).setWeight(100, 0).setAnchor(GBC.WEST));
        add(new JLabel("Key"), new GBC(0, 1).setAnchor(GBC.EAST));
        add(keyField, new GBC(1, 1).setWeight(100, 0).setAnchor(GBC.WEST));
        JButton loadButton = new JButton("Load");
        add(loadButton, new GBC(0, 2, 2, 1));
        loadButton.addActionListener(event -> runClass(nameField.getText(), keyField.getText()));
        pack();
    }

    /**
     * 运行给定类的main方法。
     * @param name 类名
     * @param key 类文件的解密密钥
     */
    public void runClass(String name, String key) {
        try {
            CryptoClassLoader loader = new CryptoClassLoader(Integer.parseInt(key));
            Class<?> c = loader.loadClass(name);
            Method m = c.getMethod("main", String[].class);
            m.invoke(null, (Object) new String[] {});
        }
        catch (Throwable t) {
            JOptionPane.showMessageDialog(this, t);
        }
    }
}

/**
 * 这个类加载器加载加密的类文件。
 */
class CryptoClassLoader extends ClassLoader {
    private int key;

    /**
     * 构造加密类加载器。
     * @param k 解密密钥
     */
    public CryptoClassLoader(int k) {
        key = k;
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] classBytes = null;
            classBytes = loadClassBytes(name);
            Class<?> cl = defineClass(name, classBytes, 0, classBytes.length);
            if (cl == null) throw new ClassNotFoundException(name);
            return cl;
        }
        catch (IOException e) {
            throw new ClassNotFoundException(name);
        }
    }

    /**
     * 加载并解密类文件字节
     * @param name 类名
     * @return 具有类文件字节的数组
     */
    private byte[] loadClassBytes(String name) throws IOException {
        String cname = name.replace('.', '/') + ".caesar";
        byte[] bytes = Files.readAllBytes(Paths.get(cname));
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = (byte) (bytes[i] - key);
        return bytes;
    }
}
