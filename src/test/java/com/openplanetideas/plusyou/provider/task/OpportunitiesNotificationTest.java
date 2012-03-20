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

package com.openplanetideas.plusyou.provider.task;

import com.google.common.collect.Lists;
import com.openplanetideas.plusyou.provider.csv.service.common.RowStatus;
import com.openplanetideas.plusyou.provider.task.common.AbstractOpportunitiesNotificationTest;
import com.openplanetideas.plusyou.provider.task.common.FailedFile;
import com.openplanetideas.plusyou.provider.task.common.ProcessStatus;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OpportunitiesNotificationTest extends AbstractOpportunitiesNotificationTest {

    @Test
    public void sendEmail_FAILED() throws MessagingException, IOException, SAXException {
        ProcessStatus processStatus = ProcessStatus.failed("unable to connect to server 'localhost':JSchException: UnknownHostKey: localhost.");
        sendEmail(new String[]{"receiver@there.com"}, processStatus);

        assertEquals("daily import opportunities", getMimeMessage().getSubject());
        assertEquals("receiver@there.com", getMimeMessage().getRecipients(Message.RecipientType.TO)[0].toString());
        assertHtml(getExpectedMessage("opportunitiesNotification_FAILED.html"), getMessage());
        assertEquals("opportunities.log", getAttachmentName());
    }

    @Test
    public void sendEmail_SUCCEEDED() throws MessagingException, IOException, SAXException {
        ProcessStatus processStatus = ProcessStatus.succeeded();
        sendEmail(new String[]{"receiver@there.com"}, processStatus);

        assertEquals("daily import opportunities", getMimeMessage().getSubject());
        assertEquals("receiver@there.com", getMimeMessage().getRecipients(Message.RecipientType.TO)[0].toString());
        assertHtml(getExpectedMessage("opportunitiesNotification_SUCCEEDED.html"), getMessage());
        assertEquals("opportunities.log", getAttachmentName());
    }
    
    @Test
    public void sendEmail_SUCCEEDED_WITH_FAILED_FILES() throws MessagingException, IOException, SAXException {
        List<RowStatus> failedRows = new ArrayList<RowStatus>();
        failedRows.add(RowStatus.failed(1L, "RequiredColumnException: column 'Title' must have a value"));
        failedRows.add(RowStatus.failed(5L, "MySQLIntegrityConstraintViolationException: Column 'interest_id' cannot be null"));
        
        List<FailedFile> failedFiles = Lists.newArrayList(new FailedFile("filename.csv", failedRows));
        ProcessStatus processStatus = ProcessStatus.succeededWithFailedFiles(failedFiles);
        sendEmail(new String[]{"receiver@there.com"}, processStatus);

        assertEquals("daily import opportunities", getMimeMessage().getSubject());
        assertEquals("receiver@there.com", getMimeMessage().getRecipients(Message.RecipientType.TO)[0].toString());
        assertHtml(getExpectedMessage("opportunitiesNotification_SUCCEEDED_WITH_FAILED_FILES.html"), getMessage());
        assertEquals("opportunities.log", getAttachmentName());
    }
}