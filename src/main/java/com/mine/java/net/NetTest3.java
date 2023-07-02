package com.mine.java.net;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author CaoY
 * @date 2023-07-02 14:38
 * @description 网络学习 - 实现服务器
 *
 * 1. 可中断的套接字和阻塞套接字是不同的网络通信机制，它们在处理网络连接时具有不同的特点和实际意义。
 *  1）可中断的套接字（Interruptible Socket）：用 SocketChannel 实现
 *      可中断的套接字使用非阻塞模式进行通信，它允许在等待数据的过程中中断或取消操作。
 *      这种方式适用于需要同时处理多个网络连接的情况，通过允许中断操作，可以更好地控制和管理网络连接。
 *      例如，在GUI应用程序中，可中断的套接字可以使用户能够取消一个正在进行的网络操作，而不会阻塞整个应用程序。
 *
 *  2）阻塞套接字（Blocking Socket）：用 Socket 实现
 *      阻塞套接字使用阻塞模式进行通信，它在读取和写入数据时会一直等待，直到操作完成或发生错误。
 *      这种方式适用于单线程的网络通信，因为它可以简化编程逻辑，让程序等待数据返回。
 *      阻塞套接字在等待数据时会阻塞当前线程，可能会导致整个应用程序在等待网络响应时无法执行其他任务。
 *
 * 下面的代码对比了可中断套接字和阻塞套接字，第一个线程（Interruptible按钮）使用可中断套接字，第二个线程（Blocking按钮）
 * 使用阻塞套接字。服务器连续发送数字，并在每发送十个数字后停滞一下。使用cancel按钮可在发送过程中中断发送的过程。
 */
public class NetTest3 {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            InterruptibleSocketFrame frame = new InterruptibleSocketFrame();
            frame.setTitle("NetTest3");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    private static class InterruptibleSocketFrame extends JFrame {

        private Scanner in;
        private JButton interruptibleButton;
        private JButton blockingButton;
        private JButton cancelButton;
        private JTextArea messages;
        private TestServer server;
        private Thread connectThread;

        public InterruptibleSocketFrame() {
            JPanel northPanel = new JPanel();
            add(northPanel, BorderLayout.NORTH);

            final int TEXT_ROWS = 20;
            final int TEXT_COLUMNS = 60;
            messages = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
            add(new JScrollPane(messages));

            interruptibleButton = new JButton("Interruptible");
            blockingButton = new JButton("Blocking");

            northPanel.add(interruptibleButton);
            northPanel.add(blockingButton);

            interruptibleButton.addActionListener(event -> {
                interruptibleButton.setEnabled(false);
                blockingButton.setEnabled(false);
                cancelButton.setEnabled(true);
                connectThread = new Thread(() -> {
                    try {
                        connectInterruptibly();
                    } catch (IOException e) {
                        messages.append("\nNetTest3.connectInterruptibly: " + e);
                    }
                });
                connectThread.start();
            });

            blockingButton.addActionListener(event -> {
                interruptibleButton.setEnabled(false);
                blockingButton.setEnabled(false);
                cancelButton.setEnabled(true);
                connectThread = new Thread(() -> {
                    try {
                        connectBlocking();
                    } catch (IOException e) {
                        messages.append("\nNetTest3.connectBlocking: " + e);
                    }
                });
                connectThread.start();
            });

            cancelButton = new JButton("cancel");
            cancelButton.setEnabled(false);
            northPanel.add(cancelButton);
            cancelButton.addActionListener(event -> {
                connectThread.interrupt();
                cancelButton.setEnabled(false);
            });

            server = new TestServer();
            new Thread(server).start();
            pack();
        }

        /**
         * 使用可中断的I/O连接到测试服务器
         * @throws IOException
         */
        public void connectInterruptibly() throws IOException {
            messages.append("Interrunptible:\n");
            try (SocketChannel channel =
                         SocketChannel.open(new InetSocketAddress("localhost", 8189))) {
                in = new Scanner(channel, StandardCharsets.UTF_8.name());
                while (!Thread.currentThread().isInterrupted()) {
                    messages.append("Reading ");
                    if (in.hasNextLine()) {
                        String line = in.nextLine();
                        messages.append(line);
                        messages.append("\n");
                    }
                }
            } finally {
                EventQueue.invokeLater(() -> {
                    messages.append("Channel closed\n");
                    interruptibleButton.setEnabled(true);
                    blockingButton.setEnabled(true);
                });
            }
        }

        /**
         * 使用可阻塞的I/O理解到测试服务器
         * @throws IOException
         */
        public void connectBlocking() throws IOException {
            messages.append("Blocking:\n");
            try (Socket sock = new Socket("localhost", 8189)) {
                in = new Scanner(sock.getInputStream(), StandardCharsets.UTF_8.name());
                while (!Thread.currentThread().isInterrupted()) {
                    messages.append("Reading ");
                    if (in.hasNextLine()) {
                        String line = in.nextLine();
                        messages.append(line);
                        messages.append("\n");
                    }
                }
            } finally {
                EventQueue.invokeLater(() -> {
                    messages.append("Socket closed\n");
                    interruptibleButton.setEnabled(true);
                    blockingButton.setEnabled(true);
                });
            }
        }

        /**
         * 一个多线程服务器，监听8189端口并向客户端发送数字，模拟10个数字后挂起的服务器。
         */
        class TestServer implements Runnable {

            @Override
            public void run() {
                try (ServerSocket s = new ServerSocket(8189)) {
                    while (true) {
                        Socket incoming = s.accept();
                        Runnable r = new TestServerHandler(incoming);
                        new Thread(r).start();
                    }
                } catch (IOException e) {
                    messages.append("\nTestServer.run: " + e);
                }
            }
        }

        /**
         * 该类处理一个服务器套接字连接的客户端输入。
         */
        class TestServerHandler implements Runnable {

            private Socket incoming;
            private int counter;

            public TestServerHandler(Socket incoming) {
                this.incoming = incoming;
            }

            @Override
            public void run() {
                try {
                    OutputStream outStream = null;
                    PrintWriter out = null;
                    try {
                        outStream = incoming.getOutputStream();
                        out = new PrintWriter(new OutputStreamWriter(outStream, StandardCharsets.UTF_8),
                                true);
                        while (counter < 100) {
                            counter++;
                            if (counter <= 10) {
                                out.println(counter);
                            }
                            Thread.sleep(100);
                        }
                    } finally {
                        incoming.close();
                        messages.append("Closing Server\n");
                    }
                } catch (Exception e) {
                    messages.append("\nTestServerHandler.run: " + e);
                }
            }
        }

    }
}
