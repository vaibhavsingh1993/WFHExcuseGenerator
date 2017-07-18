/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.meetyourmothers.sampleimapclient;

import com.meetyourmothers.wfhexcusegenerator.SMTPAuthenticator;
import com.meetyourmothers.wfhexcusegenerator.WFHExcuseGenerator;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author vavasing
 */
public class ImapClient {

    private static final Logger LOGGER = LogManager.getLogger(ImapClient.class);

    public static void main(String[] args) {

        try {
            InputStream is = WFHExcuseGenerator.class.getClassLoader().getResourceAsStream("strings.properties");
            Properties props = new Properties();
            props.load(is);
            String host = String.valueOf(props.get("hostname"));
            String user = String.valueOf(props.get("username"));
            String password = String.valueOf(props.get("password"));
            String imapport = String.valueOf(props.get("imapport"));
            String name = String.valueOf(props.get("yourname"));
            boolean isssl = Boolean.getBoolean(String.valueOf(props.get("issslon")));

            Authenticator auth = new SMTPAuthenticator(user, password);
            Session session = Session.getInstance(System.getProperties(), auth);
            session.setDebug(true);
            Store store = null;
            if (isssl) {
                store = session.getStore("imaps");
            } else {
                store = session.getStore("imap");
            }
            
            store.connect(host, user, password);

            // Get default folder
            Folder folder = store.getDefaultFolder();
            Message[] msg = folder.getMessages();
            
            Enumeration iter = msg[0].getAllHeaders();
            while(iter.hasMoreElements()) {
                LOGGER.info(iter.nextElement());
            }
            

            // Get any folder by name
            Folder[] folderList = folder.list();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }

    }
}
