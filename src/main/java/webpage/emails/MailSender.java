package webpage.emails;

import webpage.model.Notification;
import webpage.model.User;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import static webpage.util.ServerInitializer.frontEndLink;

public class MailSender {

    public static void sendMail(Notification notification, User user){
        if (user.getEmail() == null) return;
        String content = "You have a new notification.\n" +
                notification.getContent() +
                "\nClick here to see more." + frontEndLink + notification.getPath();
        String header = "Hey! You have a notification!";
        String to = user.getEmail();
        sendMail(content, header, to);
    }

    public static void sendMail(String content, String header, String to) {


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
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("gamestationnotifications@gmail.com", "cjeokfkqsqsmshfp");

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

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

}
