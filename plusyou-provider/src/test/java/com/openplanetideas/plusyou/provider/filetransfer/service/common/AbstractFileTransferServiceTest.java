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

package com.openplanetideas.plusyou.provider.filetransfer.service.common;

import com.google.common.collect.Lists;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.sftp.SftpSubsystem;
import org.jasypt.encryption.StringEncryptor;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractFileTransferServiceTest {
    
    protected static final String USERNAME = "sshd";
    protected static final String PASSWORD = "sshd";
    protected static final String HOST = "localhost";
    private static final int PORT = 2200;
    
    private static final String HOST_KEY_PEM_FILE = "hostkey.pem";
    protected static final String DATA_1_CSV_FILE = "data_1.csv";
    private static final String DATA_2_TXT_FILE = "data_2.txt";
    protected static final String DATA_3_CSV_FILE = "data_3.csv";
    protected static final String FILE_SERVER_ROOT_DIRECTORY = "target/file-server/";

    @Mock
    protected StringEncryptor stringEncryptor;

    private SshServer server;
    private Session session;
    private ChannelSftp channel;
    private File rootDirectory;

    @Before
    public void setUp() throws IOException, JSchException, SftpException, InterruptedException {
        server = SshServer.setUpDefaultServer();
        server.setPort(PORT);
        server.setPasswordAuthenticator(new BogusPasswordAuthenticator());
        server.setKeyPairProvider(new FileKeyPairProvider(new String[]{getClass().getResource(HOST_KEY_PEM_FILE).getPath()}));
        server.setSubsystemFactories(Lists.<NamedFactory<Command>>newArrayList(new SftpSubsystem.Factory()));
        server.start();

        JSch client = new JSch();
        session = client.getSession(USERNAME, HOST, PORT);
        session.setPassword(PASSWORD);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        
        channel = (ChannelSftp) session.openChannel("sftp");
        channel.connect();

        rootDirectory = new File(FILE_SERVER_ROOT_DIRECTORY);
        if (rootDirectory.mkdir()) {
            sendFile(DATA_3_CSV_FILE);
            Thread.sleep(1000);
            sendFile(DATA_2_TXT_FILE);
            Thread.sleep(1000);
            sendFile(DATA_1_CSV_FILE);
        }
    }

    @After
    public void tearDown() throws InterruptedException {
        FileSystemUtils.deleteRecursively(rootDirectory);
        channel.disconnect();
        session.disconnect();
        server.stop();
    }

    private void sendFile(String file) throws SftpException, IOException {
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream(file);
            channel.put(in, FILE_SERVER_ROOT_DIRECTORY + file);
        }
        finally {
            if (in != null) {
                in.close();
            }
        }
    }
}