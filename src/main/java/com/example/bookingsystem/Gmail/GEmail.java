package com.example.bookingsystem.Gmail;



import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class GEmail {

    public boolean sendEmail(String to, String subject, String text) {
        boolean flag = false;

        //logic
        //smtp properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // Vores email information
        String username = "noreplybookingsystemem@gmail.com";
        String password = "aeuopsogbjksdbai";

        //Ny session bliver startet
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // ny MimeMessage med Emailens indhold og sender den så ud.
        try {
            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress("noreplybookingsystemem@gmail.com"));
            message.setSubject(subject);
            message.setText(text);
            Transport.send(message);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    public boolean sendNotification(String to, String name, String bookingDato, String bookStart, String bookSlut) {
        boolean flag = false;

        //logic
        //smtp properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // Vores email information
        String username = "noreplybookingsystemem@gmail.com";
        String password = "aeuopsogbjksdbai";

        //Ny session bliver startet
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // ny MimeMessage med Emailens indhold og sender den så ud.
        try {
            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress("noreplybookingsystemem@gmail.com"));
            message.setSubject("Booking påmindelse");
            message.setText("Hej" + name + "\n "
                    + "Dette er en påmindelse om at du har en booking til den " + bookingDato + "i tidsrummet mellem "
                    + bookStart + " til " + bookSlut + ". Glæder os til at se jer.");
            Transport.send(message);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean sendBookingCode(String to, String s, String s2) {
        boolean flag = false;

        //logic
        //smtp properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // Vores email information
        String username = "noreplybookingsystemem@gmail.com";
        String password = "aeuopsogbjksdbai";

        //Ny session bliver startet
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // ny MimeMessage med Emailens indhold og sender den så ud.
        try {
            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress("noreplybookingsystemem@gmail.com"));
            message.setSubject("Booking kode");
            message.setText("Hej" + s + "\n" + "Her er din bookingkode som du kan bruge til at finde din booking i systemet: " + s2);
            Transport.send(message);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean ændringsMail(String to) {
        boolean flag = false;

        //logic
        //smtp properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // Vores email information
        String username = "noreplybookingsystemem@gmail.com";
        String password = "aeuopsogbjksdbai";

        //Ny session bliver startet
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // ny MimeMessage med Emailens indhold og sender den så ud.
        try {
            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress("noreplybookingsystemem@gmail.com"));
            message.setSubject("Ændring i booking");
            message.setText("Der er blevet foretaget en ændring i din booking. Gå venligst ind og tjek ændringerne");
            Transport.send(message);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean aflystMail(String to) {
        boolean flag = false;

        //logic
        //smtp properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // Vores email information
        String username = "noreplybookingsystemem@gmail.com";
        String password = "aeuopsogbjksdbai";

        //Ny session bliver startet
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // ny MimeMessage med Emailens indhold og sender den så ud.
        try {
            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress("noreplybookingsystemem@gmail.com"));
            message.setSubject("Aflyst booking");
            message.setText("Din booking er blevet aflyst. Kontakt Anne-Sofie hvis dette er en fejl.");
            Transport.send(message);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }




}