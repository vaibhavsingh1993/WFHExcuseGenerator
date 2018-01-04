/*
 * An email generator to send emails on your behalf, using a list of predefined 
 * excuses.
 */
package com.meetyourmothers.wfhexcusegenerator;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import javax.mail.*;
import javax.mail.internet.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author vavasing
 */
public class WFHExcuseGenerator {

    private static final Logger LOGGER = LogManager.getLogger(WFHExcuseGenerator.class);
    public static void main(String[] args) {

        InputStream is = WFHExcuseGenerator.class.getClassLoader().getResourceAsStream("strings.properties");
        Properties props = new Properties();
        try {
            props.load(is);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }

        
        
        String host = String.valueOf(props.get("hostname"));
        final String user = String.valueOf(props.get("username"));
        final String password = String.valueOf(props.get("password"));
        final String port = String.valueOf(props.get("port"));
        final String name = String.valueOf(props.get("yourname"));

        String to = String.valueOf(props.get("receiver"));

        //Get the session object  
        props = new Properties();
        props.put("mail.smtp.user", user);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        try {
            Authenticator auth = new SMTPAuthenticator(user, password);
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            
            // Check Sender header
            message.setSender(new InternetAddress("someone@oracle.com"));
            
            
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("WFH today, " + new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime()));
            message.setText("Working from home today as " + fetchExcuse() + addSignature(name));

            //send the message  
            Transport transport = session.getTransport("smtps");
            transport.connect(host, Integer.parseInt(port), user, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            System.out.println("message sent successfully...");

        } catch (MessagingException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private static String fetchExcuse() {
        List<String> excuses = new ArrayList();
        excuses.add("I have fever.");
        //excuses.add("my cat died.");
        excuses.add("I have some personal work.");
        excuses.add("I have some personal emergency.");
        //excuses.add("I broke my leg.");
        //excuses.add("I need to go to my cat's gynecologist.");

        return excuses.get(ThreadLocalRandom.current().nextInt(0, excuses.size() + 1));
    }
    
    private static String addSignature(String user) {
        StringBuffer buf = new StringBuffer();
        buf.append("\n\n");
        buf.append("Thanks,\n");
        buf.append(user);
        return buf.toString();
    }    
}
