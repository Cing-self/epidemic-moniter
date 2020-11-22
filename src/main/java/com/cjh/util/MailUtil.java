package com.cjh.util;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class MailUtil {

    private static String account = "cing_self0731@qq.com";
    private static String password = "gtcymduepuxeccif";
    private static String emailHost = "smtp.qq.com";

    public static void main(String[] args) throws MessagingException, UnsupportedEncodingException {
        //创建参数
        Properties prop = new Properties();
        prop.put("mail.transport.protocol", "smtp");
        prop.put("mail.smtp.host", emailHost);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.fallback", "false");

        //创建会话
        Session session = Session.getInstance(prop);
        session.setDebug(true);

        //创建邮件
        MimeMessage message = new MimeMessage(session);
        //发件人
        message.setFrom(new InternetAddress(account));
        //收件人
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(account));
        message.setSubject("标题");
        message.setText("内容");
        message.setSentDate(new Date());
        message.saveChanges();

        //创建传输对象
        Transport transport = session.getTransport();
        transport.connect(account, password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }


}
