/*
   Copyright 2021 Sebastian Knabe
 */
package tech.cscheer.impfen.selenium;

import static tech.cscheer.impfen.selenium.Environment.EMAIL_ENABLE_SMTP_AUTH;
import static tech.cscheer.impfen.selenium.Environment.EMAIL_ENABLE_STARTTLS;
import static tech.cscheer.impfen.selenium.Environment.EMAIL_PASSWORD;
import static tech.cscheer.impfen.selenium.Environment.EMAIL_RECIPIENTS;
import static tech.cscheer.impfen.selenium.Environment.EMAIL_SMTP_HOST;
import static tech.cscheer.impfen.selenium.Environment.EMAIL_SMTP_PORT;
import static tech.cscheer.impfen.selenium.Environment.EMAIL_USERNAME;
import static tech.cscheer.impfen.selenium.Environment.VNC_LINK;

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

import org.apache.commons.lang3.StringUtils;

public class Mailer {
    private static final Properties PROPERTIES = new Properties() {{
        put("mail.smtp.auth", EMAIL_ENABLE_SMTP_AUTH);
        put("mail.smtp.starttls.enable", EMAIL_ENABLE_STARTTLS);
        put("mail.smtp.host", EMAIL_SMTP_HOST);
        put("mail.smtp.port", EMAIL_SMTP_PORT);
    }};

    public static void sendMail() {
        try {
            Session session = Session.getInstance(PROPERTIES, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_RECIPIENTS));
            message.setSubject("CORONI: DEIN TERMIN IS DA");

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(getText(), "UTF-8", "html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getText() {
        String text = "Auf zum Computer!";
        if (StringUtils.isNotBlank(VNC_LINK)) {
            text += String.format("<br><a href='%s'>%s</a>", VNC_LINK, VNC_LINK);
        }
        return text;
    }
}
