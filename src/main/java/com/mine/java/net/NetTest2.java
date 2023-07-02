package com.mine.java.net;

import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author CaoY
 * @date 2023-07-02 9:36
 * @description 网络学习 - 实现服务器
 *
 * 下面对于服务器的模拟均可以用 telnet 进行验证。
 */
public class NetTest2 {

    // 一个简单的服务器，但是这个服务器一次只能服务一个客户端，而且输入 BYE 断开连接后也会关闭服务器
    @Test
    public void test1() throws IOException {
        // new ServerSocket(port) - 创建一个监听服务器端口的套接字
        try (ServerSocket s = new ServerSocket(8189)) {
            // 循环等待客户端连接
            try (Socket incoming = s.accept()) {
                InputStream inputStream = incoming.getInputStream();
                OutputStream outputStream = incoming.getOutputStream();

                try (Scanner in = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(outputStream,
                            StandardCharsets.UTF_8), true);

                    out.println("Hello, enter BYE to exit");
                    boolean end = false;
                    while (!end && in.hasNextLine()) {
                        String line = in.nextLine();
                        out.println("Input Content: " + line);
                        if ("BYE".equals(line.trim())) {
                            end = true;
                        }
                    }
                    out.close();
                }
            }
        }
    }

    /**
     * 多个客户端访问的简单服务器的搭建。
     *
     * 1. 首先，在 test2() 方法中创建了一个 ServerSocket 对象，它监听本地端口8189来接受客户端的连接请求。
     * 2. 然后进入一个无限循环 while (true) 中，等待客户端的连接请求。一旦有客户端连接请求到达，ServerSocket
     * 的 accept() 方法将返回一个新的 Socket 实例（称为 incoming），表示与该客户端的通信通道已建立。
     * 3. 在接受到客户端连接后，会打印出 "spawning " + i，其中 i 是一个累加的计数器，用于区分不同客户端的线程。
     * 4. 接下来，创建一个 ThreadEchoHandler 对象，传入前面接受到的 incoming Socket 实例，
     * 并将其作为参数传递给 Thread 的构造函数。 ThreadEchoHandler 类实现了 Runnable 接口，
     * 因此可以在独立线程中运行。
     * 5. 创建的线程 t 启动后，会执行 ThreadEchoHandler 类的 run() 方法。
     * 6. run() 方法负责处理客户端的输入输出。它通过 incoming 的输入流和输出流来进行通信。
     * 7. 在 run() 方法中，首先获取了 incoming 的输入流和输出流，并使用 Scanner 和 PrintWriter
     * 来对流进行读写操作。
     * 8. 首先向客户端发送一条欢迎消息，然后进入一个循环，直到遇到客户端发送的 "BYE" 指令或客户端断开连接
     */
    @Test
    public void test2() throws IOException {
        try (ServerSocket s = new ServerSocket(8189)) {
            int i = 1;
            while (true) {
                Socket incoming = s.accept();
                System.out.println("spawning " + i);
                Runnable r = new ThreadEchoHandler(incoming);
                Thread t = new Thread(r);
                t.start();
                i++;
            }
        }
    }

    /**
     * 用于多个客户端访问的简单的服务器的搭建的线程
     */
    private static class ThreadEchoHandler implements Runnable {

        private Socket incoming;

        public ThreadEchoHandler(Socket incoming) {
            this.incoming = incoming;
        }

        @Override
        public void run() {
            try (InputStream inStream = incoming.getInputStream();
            OutputStream outStream = incoming.getOutputStream();
            Scanner in = new Scanner(inStream, StandardCharsets.UTF_8.name());
            PrintWriter out = new PrintWriter(new OutputStreamWriter(outStream, StandardCharsets.UTF_8.name()), true)) {
                out.println("Hello, Enter BYE to exit.");
                boolean isEnd = false;
                while (!isEnd && in.hasNextLine()) {
                    String line = in.nextLine();
                    out.println("Echo: " + line);
                    if ("BYE".equals(line.trim())) {
                        isEnd = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
