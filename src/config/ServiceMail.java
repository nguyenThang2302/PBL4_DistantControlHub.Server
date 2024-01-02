package config;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import common.bean.ModelMessage;

public class ServiceMail {

    public ModelMessage sendMain(String toEmail, String code) {
        ModelMessage ms = new ModelMessage(false, "");
        String from = "distantcontrolhub@gmail.com";
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        String username = "distantcontrolhub@gmail.com";
        String password = "rvfjdqfylwcccbzo";   
        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Mã xác nhận đăng kí tài khoản DistantControlHub");
            message.setText("Mã xác nhận của bạn là: " + code);
            Transport.send(message);
            ms.setSuccess(true);
        } catch (MessagingException e) {
        	System.out.print(e);
            if (e.getMessage().equals("Invalid Addresses")) {
                ms.setMessage("Invalid email");
            } else {
                ms.setMessage("Error");
            }
        }
        return ms;
    }
    
    public ModelMessage sendForgotPassword(String toEmail, String code) {
        ModelMessage ms = new ModelMessage(false, "");
        String from = "distantcontrolhub@gmail.com";
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        String username = "distantcontrolhub@gmail.com";
        String password = "rvfjdqfylwcccbzo"; 
        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Mã xác nhận đặt lại tài khoản DistantControlHub");
            message.setText("Mã xác nhận của bạn là: " + code);
            Transport.send(message);
            ms.setSuccess(true);
        } catch (MessagingException e) {
        	System.out.print(e);
            if (e.getMessage().equals("Invalid Addresses")) {
                ms.setMessage("Invalid email");
            } else {
                ms.setMessage("Error");
            }
        }
        return ms;
    }
    
    public ModelMessage sendNewPassword(String toEmail, String newPassword) {
        ModelMessage ms = new ModelMessage(false, "");
        String from = "distantcontrolhub@gmail.com";
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        String username = "distantcontrolhub@gmail.com";
        String password = "rvfjdqfylwcccbzo"; 
        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Mật khẩu đặt lại tài khoản DistantControlHub");
            message.setText("Mật khẩu mới của bạn là: " + newPassword);
            Transport.send(message);
            ms.setSuccess(true);
        } catch (MessagingException e) {
        	System.out.print(e);
            if (e.getMessage().equals("Invalid Addresses")) {
                ms.setMessage("Invalid email");
            } else {
                ms.setMessage("Error");
            }
        }
        return ms;
    }
}
