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

package com.openplanetideas.plusyou.provider.resource;

import com.openplanetideas.plusyou.provider.config.TestConnectionsConfig;
import com.openplanetideas.plusyou.provider.email.EmailMessage;
import com.openplanetideas.plusyou.provider.email.EmailMessageSender;
import com.openplanetideas.plusyou.provider.email.EmailMessageText;
import com.openplanetideas.plusyou.provider.filetransfer.domain.FileTransfer;
import com.openplanetideas.plusyou.provider.filetransfer.service.FileTransferService;
import com.openplanetideas.plusyou.provider.filetransfer.service.impl.JSchFileTransferService;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.WebApplicationContext;

import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Controller
@Scope(WebApplicationContext.SCOPE_REQUEST)
@Path("abracadabra")
public class TestConnections {

    private static final Logger LOGGER = Logger.getLogger(TestConnections.class.getName());
    
    @Autowired
    private TestConnectionsConfig config;
    
    @Autowired
    private StringEncryptor stringEncryptor;
    
    @Autowired
    private EmailMessageSender emailMessageSender;

    @OPTIONS
    @Path("fileServer")
    public Response testFileServerConnection() {
        try {
            FileTransfer fileTransfer = new FileTransfer.Builder()
                    .withHost(config.getSFTPHost())
                    .withPort(config.getSFTPPort())
                    .withUser(config.getSFTPUser())
                    .withPassword(config.getSFTPPassword())
                    .build();

            FileTransferService fileTransferService = new JSchFileTransferService(fileTransfer, stringEncryptor, LOGGER);
            fileTransferService.connect();
            fileTransferService.disconnect();
            return Response.ok().build();
        }
        catch (Exception e) {
            return Response.serverError().entity(ExceptionUtils.getFullStackTrace(e)).build();
        }
    }
    
    @OPTIONS
    @Path("mailServer")
    public Response testMailServerConnection() {
        try {
            EmailMessageText text = new EmailMessageText(config.getSMTPTextTemplate(), new ExtendedModelMap());
            EmailMessage emailMessage = new EmailMessage(config.getSMTPFrom(), config.getSMTPTo(), config.getSMTPSubject(), text);
            emailMessageSender.sendEmail(emailMessage);
            return Response.ok().build();
        }
        catch (Exception e) {
            return Response.serverError().entity(ExceptionUtils.getFullStackTrace(e)).build();
        }
    }
}