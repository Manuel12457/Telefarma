package com.example.telefarma.servlets;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailServlet {

    public static void sendMail(String recipient, String subject, String mssg) {
        System.out.println("Preparando mail...");

        String emailAddress = "telefarma.app@gmail.com";
        String password = "Telefarma2021";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); //TLS
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAddress, password);
            }
        });

        try {
            Message message = prepareMessage(session, emailAddress, recipient, subject, mssg);
            Transport.send(message);
            System.out.println("Mail enviado :)");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static Message prepareMessage(Session session, String emailAddress, String recipient, String subject, String mssg)
            throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailAddress));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);
        message.setText(mssg);
        return message;
    }

}
