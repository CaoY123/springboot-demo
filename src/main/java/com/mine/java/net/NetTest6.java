package com.mine.java.net;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

/**
 * @author CaoY
 * @date 2023-07-03 14:27
 * @description 网络学习 - 发送 E-mail
 *
 * 没有运行起来，但是作为示例，可以参考一下基本的写法。
 */
public class NetTest6 {

    public static void main(String[] args) throws IOException, MessagingException {
        Properties props = new Properties();
        Path propsPath = Paths.get("src", "main", "resources", "net", "mail.properties");
        try (InputStream in = Files.newInputStream(propsPath)) {
            props.load(in);
        }
        List<String> lines = Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8);

        String from = lines.get(0);
        String to = lines.get(1);
        String subject = lines.get(2);

        StringBuilder builder = new StringBuilder();

        for (int i = 3; i < lines.size(); i++) {
            builder.append(lines.get(i));
            builder.append("\n");
        }

        Console console = System.console();
        String password = new String(console.readPassword("Password: "));

        Session mailSession = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(from);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(builder.toString());
        Transport tr = mailSession.getTransport();
        try {
            tr.connect(null, password);
            tr.sendMessage(message, message.getAllRecipients());
        } finally {
            tr.close();
        }
    }
}
