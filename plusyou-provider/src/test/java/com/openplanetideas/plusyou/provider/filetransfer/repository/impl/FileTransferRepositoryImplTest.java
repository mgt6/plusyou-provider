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

package com.openplanetideas.plusyou.provider.filetransfer.repository.impl;

import com.google.common.collect.Lists;
import com.openplanetideas.plusyou.provider.filetransfer.domain.FileTransfer;
import com.openplanetideas.plusyou.provider.filetransfer.repository.impl.common.AbstractFileTransferRepositoryImplTest;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileTransferRepositoryImplTest extends AbstractFileTransferRepositoryImplTest {

    @Test
    public void findAll_OK() {
        FileTransfer expectedFileTransfer = new FileTransfer.Builder()
                .withHost("localhost1")
                .withPort(2200)
                .withUser("TestUser")
                .withPassword("the_password")
                .withDirectory("/var/plus_you/download")
                .withContactPerson("test01.plusyou@gmail.com")
                .withEnabled(true)
                .build();
        
        List<FileTransfer> expectedFileTransfers = Lists.newArrayList(expectedFileTransfer);
        List<FileTransfer> actualFileTransfers = fileTransferRepository.findAll();
        assertEquals(expectedFileTransfers, actualFileTransfers);

        String expectedPassword = expectedFileTransfer.getPassword();
        String actualPassword = actualFileTransfers.get(0).getPassword();
        assertFalse(expectedPassword.equals(actualPassword));
        assertTrue(expectedPassword.equals(stringEncryptor.decrypt(actualPassword)));
    }
}