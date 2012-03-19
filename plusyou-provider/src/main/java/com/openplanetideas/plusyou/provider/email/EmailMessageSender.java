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

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

@Service
public class EmailMessageSender {
    
    @Autowired
    private Session session;
    
    @Autowired
    private EmailMessageSession emailMessageSession;
    
    @Autowired
    private VelocityEngine velocityEngine;
    
    public void sendEmail(EmailMessage emailMessage) throws MessagingException {
        MimeMessage mimeMessage = EmailMessageMimeCreator.create(session, emailMessage, velocityEngine);
        sendMimeMessage(mimeMessage);
    }
    
    private void sendMimeMessage(MimeMessage mimeMessage) throws MessagingException {
        Transport transport = null;
        try {
            transport = session.getTransport();
            
            String host = emailMessageSession.getHost();
            Integer port = emailMessageSession.getPort();
            String username = emailMessageSession.getUsername();
            String password = emailMessageSession.getPassword();
            transport.connect(host, port, username, password);
            
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        }
        finally {
            if (transport != null) {
                transport.close();
            }
        }
    }
}