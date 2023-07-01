package com.mine.java.net;

import org.junit.Test;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author CaoY
 * @date 2023-07-01 17:29
 * @description 网路学习 - 连接到服务器
 * 下面程序练习了使用 telnet 连接到某个服务器并获取信息，只不过这种连接比较简单，而且没考虑什么复杂的因素。
 * 此外还练习了关于因特网地址的一些操作。
 */
public class NetTest1 {

    /**
     * 使用 telnet 工具连接到某个端口并打印出它所找到的信息。
     * 这里它所找到的信息为当前铯原子钟的计量时间
     * @throws IOException
     */
    @Test
    public void test1() throws IOException {

        try (Socket s = new Socket("time-a.nist.gov", 13);
             Scanner in = new Scanner(s.getInputStream(), StandardCharsets.UTF_8.name())) {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                System.out.println(line);
            }
        }
    }

    // 因特网地址基础操作
    @Test
    public void test2() throws UnknownHostException {
        // 为给定的主机创建一个 InetAddress 对象，包含了它的 IPv4地址和 IPv6地址（如果有的话）
        InetAddress hostAddress = InetAddress.getByName("time-a.nist.gov");
        System.out.println("hostAddress: " + hostAddress);

        byte[] bytes = hostAddress.getAddress();
        System.out.println("IPv4地址字节数组表示：" + Arrays.toString(bytes));
        System.out.println("======================================");

        String host = hostAddress.getHostName();
        System.out.println("host: " + host);
        String address = hostAddress.getHostAddress();
        System.out.println("address: " + address);
        System.out.println("======================================");

        // 一些访问量较大的主机名会对应多个因特网地址，以实现负载均衡，如下：使用 getAllByName 来获取所有主机
        InetAddress[] inetAddresses = InetAddress.getAllByName("google.com");
        System.out.println(Arrays.toString(inetAddresses));
        System.out.println("======================================");

        // 获取本地主机的地址，可供其他主机访问，不是 127.0.0.1
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println("localHost: " + localHost);
    }

    // 考虑套接字超时，但是实际上运行下面代码一般不会看到超时的效果
    @Test
    public void test3() throws IOException {
        String host = "example.com";
        int port = 80;
        int timeout = 5000; // 设置超时时间为5秒

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout);
            // 执行套接字操作
            System.out.println("执行操作中...");
            System.out.println("执行操作结束...");
        } catch (SocketTimeoutException e) {
            System.out.println("连接超时！");
            e.printStackTrace();
        }
    }
}
