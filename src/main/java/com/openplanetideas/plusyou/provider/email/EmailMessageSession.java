/*
 * Copyright (c) 2012, Sony Corporation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of the Sony Corporation.
 */

package com.openplanetideas.plusyou.provider.email;

import javax.mail.Authenticator;
import javax.mail.Session;
import javax.net.ssl.SSLSocketFactory;
import java.util.Properties;

public class EmailMessageSession {

    private static final Integer TIMEOUT = 60000;

    private Properties properties = new Properties();
    private Authenticator authenticator;
    private String host;
    private Integer port = -1;
    private String username;
    private String password;

    public EmailMessageSession(String host, Integer port) {
        this.host = host;
        properties.setProperty("mail.smtp.host", host);

        this.port = port;
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.socketFactory.port", port);

        properties.setProperty("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.timeout", TIMEOUT);
        properties.put("mail.smtp.connectiontimeout", TIMEOUT);
    }

    public EmailMessageSession(String host, Integer port, String username, String password) {
        this(host, port);

        this.username = username;
        this.password = password;
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.socketFactory.class", SSLSocketFactory.class.getName());
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        authenticator = new EmailMessageAuthenticator(username, password);
    }
    
    public Session build() {
        return Session.getInstance(properties, authenticator);
    }

    public String getHost() {
        return host;
    }

    public String getPassword() {
        return password;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }
}