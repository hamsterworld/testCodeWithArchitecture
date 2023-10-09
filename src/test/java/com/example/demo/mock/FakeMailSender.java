package com.example.demo.mock;

import com.example.demo.user.service.port.MailSender;

public class FakeMailSender implements MailSender {

    public String email;

    public String title;
    public String conetent;

    @Override
    public void send(String email, String title, String content) {
        this.email = email;
        this.title = title;
        this.conetent = content;
    }
}
