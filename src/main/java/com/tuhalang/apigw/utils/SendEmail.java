package com.tuhalang.apigw.utils;

import com.tuhalang.apigw.common.MyRunnable;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class SendEmail extends MyRunnable {

    private static final Logger LOGGER = Logger.getLogger(SendEmail.class);
    private static final Long TIMEOUT = 60000L;

    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    private String emailTo;
    private String content;
    private String subject;

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public static void send(String emailTo, String content, String subject) {
        SendEmail sendEmail = new SendEmail();
        sendEmail.setEmailTo(emailTo);
        sendEmail.setSubject(subject);
        sendEmail.setContent(content);
        Thread thread = new Thread(sendEmail);
        thread.start();

        Long startTimeMillis = System.currentTimeMillis();
        Long endTimeMillis = startTimeMillis + TIMEOUT;

        while (thread.isAlive()) {
            if (System.currentTimeMillis() > endTimeMillis) {
                try {
                    thread.stop();
                } catch (Exception e) {
                    LOGGER.error(e);
                }
                LOGGER.error("Send mail unsuccessful");
            } else if (sendEmail.getFinish()) {
                break;
            }

            try {
                Thread.sleep(100);
                Thread.yield();
            } catch (Exception e) {
                LOGGER.error(e);
            }

        }
    }

    @Override
    protected void execute() {
        try {
            Properties mailServerProperties;
            Session getMailSession;
            MimeMessage mailMessage;

            // Step1: setup Mail Server
            mailServerProperties = System.getProperties();
            mailServerProperties.put("mail.smtp.port", "587");
            mailServerProperties.put("mail.smtp.auth", "true");
            mailServerProperties.put("mail.smtp.starttls.enable", "true");

            // Step2: get Mail Session
            getMailSession = Session.getDefaultInstance(mailServerProperties, null);
            mailMessage = new MimeMessage(getMailSession);
            mailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(this.emailTo));
            mailMessage.setSubject(this.subject);
            mailMessage.setText(this.content);

            // Step3: Send mail
            Transport transport = getMailSession.getTransport("smtp");

            transport.connect("smtp.gmail.com", USERNAME, PASSWORD);
            transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
