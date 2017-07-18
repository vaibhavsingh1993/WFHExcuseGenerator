/*
 * An email generator to send emails on your behalf, using a list of predefined 
 * excuses.
 */
package com.meetyourmothers.wfhexcusegenerator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author vavasing
 */
class SMTPAuthenticator extends Authenticator {
    private static final Logger LOGGER = LogManager.getLogger(SMTPAuthenticator.class);
    private String user = null;
    private String pass = null;

    public SMTPAuthenticator(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        LOGGER.debug("SMTPAuthenticator::Inside getPasswordAuthentication");
        return new PasswordAuthentication(this.user, this.pass);
    }
}
