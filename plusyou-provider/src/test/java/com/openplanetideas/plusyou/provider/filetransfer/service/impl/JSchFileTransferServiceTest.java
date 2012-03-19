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

package com.openplanetideas.plusyou.provider.filetransfer.service.impl;

import com.google.common.collect.Lists;
import com.openplanetideas.plusyou.provider.filetransfer.domain.FileTransfer;
import com.openplanetideas.plusyou.provider.filetransfer.exception.FileTransferServiceException;
import com.openplanetideas.plusyou.provider.filetransfer.service.FileTransferService;
import com.openplanetideas.plusyou.provider.filetransfer.service.TransferFile;
import com.openplanetideas.plusyou.provider.filetransfer.service.common.AbstractFileTransferServiceTest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.StringValueTransformer;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class JSchFileTransferServiceTest extends AbstractFileTransferServiceTest {
    
    private static final Logger LOGGER = Logger.getLogger(JSchFileTransferServiceTest.class.getName());

    @Test
    public void downloadFiles_FIRST_TIME() throws FileTransferServiceException {
        when(stringEncryptor.decrypt(anyString())).thenReturn(PASSWORD);
        FileTransfer fileTransfer = new FileTransfer.Builder()
                .withHost(HOST)
                .withPort(2200)
                .withUser(USERNAME)
                .withPassword(PASSWORD)
                .withDirectory(FILE_SERVER_ROOT_DIRECTORY)
                .build();
        FileTransferService fileTransferService = new JSchFileTransferService(fileTransfer, stringEncryptor, LOGGER);

        fileTransferService.connect();
        List<TransferFile> files = fileTransferService.downloadFiles();
        Collection expectedFileNames = Lists.newArrayList(DATA_3_CSV_FILE, DATA_1_CSV_FILE);
        Collection actualFileNames = CollectionUtils.collect(files, StringValueTransformer.getInstance());
        assertEquals(expectedFileNames, actualFileNames);
        fileTransferService.disconnect();
    }
    
    @Test
    public void downloadFiles_SECOND_TIME() throws FileTransferServiceException {
        when(stringEncryptor.decrypt(anyString())).thenReturn(PASSWORD);
        FileTransfer fileTransfer = new FileTransfer.Builder()
                .withHost(HOST)
                .withPort(2200)
                .withUser(USERNAME)
                .withPassword(PASSWORD)
                .withDirectory(FILE_SERVER_ROOT_DIRECTORY)
                .build();
        FileTransferService fileTransferService = new JSchFileTransferService(fileTransfer, stringEncryptor, LOGGER);

        fileTransferService.connect();
        fileTransferService.downloadFiles();
        fileTransferService.disconnect();

        fileTransferService.connect();
        List<TransferFile> files = fileTransferService.downloadFiles();
        assertEquals(0, files.size());
        fileTransferService.disconnect();
    }
}