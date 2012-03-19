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

import org.apache.commons.lang3.CharEncoding;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.util.StringUtils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailMessageMimeCreator {
    
    private static final String HTML = "html";
    
    private static MimeBodyPart getAttachment(EmailMessageAttachment attachment) throws MessagingException {
        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        attachmentBodyPart.setFileName(attachment.getFilename());
        attachmentBodyPart.setDataHandler(new DataHandler(new FileDataSource(attachment.getContent())));
        return attachmentBodyPart;
    }
    
    private static String getMessage(EmailMessageText text, VelocityEngine velocityEngine) {
        return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, text.getTemplate(), text.getModel().asMap());
    }
    
    public static MimeMessage create(Session session, EmailMessage emailMessage, VelocityEngine velocityEngine) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(emailMessage.getFrom()));
        mimeMessage.setSubject(emailMessage.getSubject());
        mimeMessage.setRecipients(Message.RecipientType.TO, StringUtils.arrayToCommaDelimitedString(emailMessage.getTo()));
        
        String text = getMessage(emailMessage.getText(), velocityEngine);
        if (!emailMessage.hasAttachment()) {
            mimeMessage.setText(text, CharEncoding.UTF_8, HTML);
        }
        else {
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(text, CharEncoding.UTF_8, HTML);
            
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(getAttachment(emailMessage.getAttachment()));
            
            mimeMessage.setContent(multipart);
        }
        
        return mimeMessage;
    }
}