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

/**
 *
 * @author vavasing
 */
class SMTPAuthenticator extends Authenticator {

    String user = null;
    String pass = null;

    public SMTPAuthenticator(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.user, this.pass);
    }
}
