package webpage.api;

import webpage.model.Notification;
import webpage.model.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static webpage.util.ServerInitializer.frontEndLink;

public class MailSender {

    private static Session session;


    public static void initMailSender(){
        // Sender's email ID needs to be mentioned
        String from = "gamestationnotifications@gmail.com";
        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";
        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "cjeokfkqsqsmshfp");
            }
        });
    }

    public static void sendMail(Notification notification, User user){
        if (user.getEmail() == null) return;
        String content = "You have a new notification.\n" +
                notification.getContent() +
                "\nClick here to see more. " + frontEndLink + notification.getPath();
        String header = notification.getContent();
        String to = user.getEmail();
        sendMail(content, header, to);
    }

    public static void sendMail(Notification notification, Set<User> users){
        List<String> emails = new ArrayList<>();
        for (User user : users) {
            if (user.getEmail() == null) continue;
            emails.add(user.getEmail());
        }
        String content = "You have a new notification.\n" +
                notification.getContent() +
                "\nClick here to see more. " + frontEndLink + notification.getPath();
        String header = notification.getContent();
        sendMail(content, header, emails);
    }

    private static void sendMail(String content, String header, String to) {
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress("gamestationnotifications@gmail.com"));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // Set Subject: header field
            message.setSubject(header);
            // Now set the actual message
            message.setText(content);
            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

    private static void sendMail(String content, String header, List<String> to) {
        if (to.isEmpty()) return;
        for (String s : to) {
            sendMail(content, header, s);
        }
    }
}
