package com.cjh.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MailComponent {

    @Autowired
    private JavaMailSender mailSender;

    public void send(){
        System.out.println("发送邮件");
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("主题");
        mailMessage.setText("正文");
        mailMessage.setSentDate(new Date());
        mailMessage.setTo("cing_self0731@qq.com");
        mailMessage.setFrom("cing_self0731@qq.com");

        mailSender.send(mailMessage);
    }
}
