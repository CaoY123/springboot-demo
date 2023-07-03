package com.mine.java.net;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author CaoY
 * @date 2023-07-02 16:38
 * @description 网络学习 - 获取 Web 数据
 *
 * 1. 在 Java 中，URI（Uniform Resource Identifier，统一资源标识符）是个纯粹的语法结构，包含用来指定 Web
 * 资源的字符串的各种组成部分。URL（Uniform Resource Locator，统一资源定位符） 是 URI 的一个特例，它包含了
 * 用于定位 Web 资源的足够信息。其他 URI，比如：mailto:cay@horstmann.com 则不属于定位符，因为根据该标识符
 * 无法定位到任何数据，像这样的 URI 我们称之为 URN（Uniform Resource Name，统一资源名）。
 *
 * 下面练习为给定任意的一个 URL 获取其中的内容和相关信息。
 * 注：没有演示有个人信息和密码的情况，其实也就是需要加密一下密码并且拼出响应的字符串。
 */
public class NetTest4 {

    public static void main(String[] args) {
        try {
             String urlName = "http://horstmann.com";
            URL url = new URL(urlName);
            URLConnection connection = url.openConnection();

            // 建立连接
            connection.connect();

            // 获取响应头的值的情况
            Map<String, List<String>> headers = connection.getHeaderFields();
            for (Map.Entry<String, List<String> > entry : headers.entrySet()) {
                String key = entry.getKey();
                for (String value : entry.getValue()) {
                    System.out.println(key + " -> " + value);
                }
            }

            // 获取响应体中关心的内容
            System.out.println("----------------------");
            System.out.println("Content Type: " + connection.getContentType());
            System.out.println("Content Length: " + connection.getContentLength());
            System.out.println("Content Encoding: " + connection.getContentEncoding());
            System.out.println("Date: " + connection.getDate());
            System.out.println("Expiration: " + connection.getExpiration());
            System.out.println("LastModified: " + connection.getLastModified());
            System.out.println("----------------------");

            // 设置字符编码格式，并读取部分响应体的内容
            String encoding = connection.getContentEncoding();
            if (encoding == null) {
                encoding = "UTF-8";
            }
            try (Scanner in = new Scanner(connection.getInputStream(), encoding)) {
                for (int n = 1; in.hasNextLine() && n <= 5; n++) {
                    System.out.println(in.nextLine());
                }
                if (in.hasNextLine()) {
                    System.out.println("...");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
