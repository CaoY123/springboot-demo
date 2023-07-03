package com.mine.java.net;

import cn.hutool.core.text.StrBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author CaoY
 * @date 2023-07-03 1:56
 * @description 网络学习 - 获取 Web 数据
 *
 * 下面代码实现了 HTTP POST 请求向指定的 URL 发送数据，并获取响应结果的功能。
 */
public class NetTest5 {

    public static void main(String[] args) throws IOException {

        String propsFileName = "src" + File.separator +  "main" + File.separator +
                "resources" + File.separator +  "net" + File.separator +  "post.properties";

        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get(propsFileName))) {
            props.load(in);
        }
        String urlString = props.remove("url").toString();
        Object userAgent = props.remove("User-Agent");
        Object redirects = props.remove("redirects");
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        String result = doPost(new URL(urlString), props,
                userAgent == null ? null : userAgent.toString(),
                redirects == null ? -1 : Integer.parseInt(redirects.toString()));
        System.out.println(result);
    }

    /**
     * doPost 方法接受一个 URL 对象、一个键值对的 Map、一个用户代理字符串和一个重定向次数作为参数。
     * 它使用 HttpURLConnection 建立与指定 URL 的连接，设置请求头部信息，发送 POST 请求，并获取响应数据。
     * 如果存在重定向，则根据重定向地址重新执行 doPost 方法，直到达到重定向次数或不再发生重定向。
     * 最后，将响应结果转化为字符串并返回。
     * @param url
     * @param nameValuePairs    属性键值对
     * @param userAgent         用户代理字符串
     * @param redirects         重定向次数
     * @return
     * @throws IOException
     */
    public static String doPost(URL url, Map<Object, Object> nameValuePairs, String userAgent,
                                int redirects) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        if (userAgent != null) {
            connection.setRequestProperty("User-Agent", userAgent);
        }

        if (redirects >= 0) {
            connection.setInstanceFollowRedirects(false);
        }

        connection.setDoOutput(true);

        try (PrintWriter out = new PrintWriter(connection.getOutputStream())) {
            boolean first = true;
            for (Map.Entry<Object, Object> pair : nameValuePairs.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    out.print('&');
                }
                String name = pair.getKey().toString();
                String value = pair.getValue().toString();
                out.print(name);
                out.print('=');
                out.print(URLEncoder.encode(value, StandardCharsets.UTF_8.name()));
            }
        }

        String encoding = connection.getContentEncoding();
        if (encoding == null) {
            encoding = "UTF-8";
        }

        if (redirects > 0) {
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_MOVED_PERM
            || responseCode == HttpURLConnection.HTTP_MOVED_TEMP
            || responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
                String location = connection.getHeaderField("Location");
                if (location != null) {
                    URL base = connection.getURL();
                    connection.disconnect();
                    return doPost(new URL(base, location), nameValuePairs, userAgent,
                            redirects - 1);
                }
            }
        } else if (redirects == 0) {
            throw new IOException("Too many redirects");
        }

        StrBuilder response = new StrBuilder();
        try (Scanner in = new Scanner(connection.getInputStream(), encoding)) {
            while (in.hasNextLine()) {
                response.append(in.nextLine());
                response.append("\n");
            }
        } catch (IOException e) {
            InputStream err = connection.getErrorStream();
            if (err == null) {
                throw e;
            }
            try (Scanner in = new Scanner(err)) {
                response.append(in.nextLine());
                response.append("\n");
            }
        }

        return response.toString();
    }
}
