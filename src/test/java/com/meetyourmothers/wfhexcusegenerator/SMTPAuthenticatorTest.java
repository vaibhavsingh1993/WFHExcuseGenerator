/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meetyourmothers.wfhexcusegenerator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author vavasing
 */
public class SMTPAuthenticatorTest {
    private static final Logger LOGGER = LogManager.getLogger(SMTPAuthenticatorTest.class);
        public static void main(String[] args) {
        try {
            test();
        } catch (MessagingException ex) {
            LOGGER.error(ex.getMessage());
        }

    }

    private static void test() throws MessagingException {

        InputStream is = SMTPAuthenticator.class.getClassLoader().getResourceAsStream("strings.properties");
        Properties props = new Properties();
        try {
            props.load(is);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String d_email = String.valueOf(props.get("username"));
        String d_uname = String.valueOf(props.get("username"));
        String d_password = String.valueOf(props.get("password"));
        String d_host = String.valueOf(props.get("hostname"));
        String d_port = String.valueOf(props.get("port"));; //465,587
        String m_to = String.valueOf(props.get("receiver"));
        String m_subject = "Testing";
        String m_text = "Hey, this is the testing email.";

        props.put("mail.smtp.user", d_email);
        props.put("mail.smtp.host", d_host);
        props.put("mail.smtp.port", d_port);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", d_port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        Authenticator auth = new SMTPAuthenticator(d_uname, d_password);
        Session session = Session.getInstance(props, auth);
        session.setDebug(true);

        MimeMessage msg = new MimeMessage(session);
        msg.setText(m_text);
        msg.setSubject(m_subject);
        msg.setFrom(new InternetAddress(d_email));
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(m_to));

        Transport transport = session.getTransport("smtps");
        transport.connect(d_host, 465, d_uname, d_password);
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
    }
}
