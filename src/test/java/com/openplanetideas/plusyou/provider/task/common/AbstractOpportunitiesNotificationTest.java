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

package com.openplanetideas.plusyou.provider.task.common;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.openplanetideas.plusyou.provider.config.AppConfig;
import com.openplanetideas.plusyou.provider.config.EnvironmentTestType;
import com.openplanetideas.plusyou.provider.task.OpportunitiesNotification;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.FileCopyUtils;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AppConfig.class)
@ActiveProfiles(EnvironmentTestType.TEST)
public abstract class AbstractOpportunitiesNotificationTest {

    private File file;
    private GreenMail server;

    @Inject
    private OpportunitiesNotification opportunitiesNotification;

    @Before
    public void setUp() throws IOException {
        file = new ClassPathResource("opportunities.log", getClass()).getFile();
        server = new GreenMail(new ServerSetup(3025, "localhost", ServerSetup.PROTOCOL_SMTP));
        server.start();
        XMLUnit.setIgnoreWhitespace(true);
    }

    @After
    public void tearDown() {
        server.stop();
    }

    protected void assertHtml(String expectedHtml, String actualHtml) throws IOException, SAXException {
        DetailedDiff diff = new DetailedDiff(new Diff(expectedHtml, actualHtml));
        XMLAssert.assertXMLIdentical(diff, Boolean.TRUE);
    }
    
    protected String getAttachmentName() throws IOException, MessagingException {
        return getParentBodyPart(1).getFileName();
    }
    
    protected String getExpectedMessage(String filename) throws IOException {
        byte[] bytes = FileCopyUtils.copyToByteArray(getClass().getResourceAsStream(filename));
        return new String(bytes);
    }
    
    protected String getMessage() throws IOException, MessagingException {
        return String.valueOf(getParentBodyPart(0).getContent());
    }

    protected MimeMessage getMimeMessage() {
        return server.getReceivedMessages()[0];
    }
    
    protected void sendEmail(String[] to, ProcessStatus processStatus) throws MessagingException {
        opportunitiesNotification.sendEmail("sender@there.com", to, processStatus, file);
    }
    
    private MimeBodyPart getParentBodyPart(int index) throws IOException, MessagingException {
        MimeMultipart multipart = (MimeMultipart) getMimeMessage().getContent();
        return (MimeBodyPart) multipart.getBodyPart(index);
    }
}