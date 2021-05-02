/*
   Copyright 2021 Sebastian Knabe
 */
package tech.cscheer.impfen.selenium;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mailer {
    private static final Properties PROPERTIES = new Properties() {{
        put("mail.smtp.auth", true);
        put("mail.smtp.starttls.enable", "true");
        put("mail.smtp.host", "posteo.de");
        put("mail.smtp.port", "587");
    }};
    private static final String EMAIL = System.getenv("EMAIL");
    private static final String EMAIL_PASSWORD = System.getenv("EMAIL_PASSWORD");

    public static void sendMail(String subject, String text) {
        try {
            Session session = Session.getInstance(PROPERTIES, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL, EMAIL_PASSWORD);
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL));
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(text, "UTF-8", "html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
