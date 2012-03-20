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

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.openplanetideas.plusyou.provider.filetransfer.domain.FileTransfer;
import com.openplanetideas.plusyou.provider.filetransfer.exception.FileTransferServiceException;
import com.openplanetideas.plusyou.provider.filetransfer.service.FileTransferService;
import com.openplanetideas.plusyou.provider.filetransfer.service.TransferFile;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class JSchFileTransferService implements FileTransferService {
    
    private static final String CSV_EXTENSION = "csv";
    private static final String OK_EXTENSION = "ok";
    private static final String SEPARATOR_EXTENSION = ".";
    
    private FileTransfer fileTransfer;
    private StringEncryptor stringEncryptor;
    private Logger logger;

    private Session session;
    private ChannelSftp channel;

    public JSchFileTransferService(FileTransfer fileTransfer, StringEncryptor stringEncryptor, Logger logger) {
        this.fileTransfer = fileTransfer;
        this.stringEncryptor = stringEncryptor;
        this.logger = logger;
    }

    @Override
    public void connect() throws FileTransferServiceException {
        try {
            JSch client = new JSch();
            session = client.getSession(fileTransfer.getUser(), fileTransfer.getHost(), fileTransfer.getPort());
            session.setPassword(stringEncryptor.decrypt(fileTransfer.getPassword()));
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();
            logger.info(String.format("connected to server '%s':%d", fileTransfer.getHost(), fileTransfer.getPort()));
        }
        catch (Exception e) {
            String msg = String.format("unable to connect to server '%s':%d", fileTransfer.getHost(), fileTransfer.getPort());
            throw new FileTransferServiceException(msg, e);
        }
    }

    @Override
    public void disconnect() {
        if (isConnected()) {
            channel.disconnect();
            session.disconnect();
            logger.info(String.format("disconnected from server '%s':%d", fileTransfer.getHost(), fileTransfer.getPort()));
        }
    }

    @Override
    public List<TransferFile> downloadFiles() throws FileTransferServiceException {
        try {
            List<TransferFile> files = new ArrayList<TransferFile>();
            if (isConnected()) {
                changeToDownloadDirectory();

                for (Object obj : channel.ls(channel.pwd())) {
                    ChannelSftp.LsEntry ftpFile = (ChannelSftp.LsEntry) obj;
                    if (isCSVFile(ftpFile)) {
                        logger.info(String.format("found file '%s' to download", ftpFile.getFilename()));
                        File file = createTempFile(ftpFile);
                        downloadFile(file, ftpFile);
                        markFileAsDownloaded(ftpFile);
                        files.add(new TransferFile(ftpFile.getAttrs().getMTime(), ftpFile.getFilename(), file));
                    }
                }
            }
            
            Collections.sort(files);
            return files;
        }
        catch (Exception e) {
            String msg = "unable to download files";
            throw new FileTransferServiceException(msg, e);
        }
    }

    private void changeToDownloadDirectory() throws SftpException {
        channel.cd(channel.getHome());
        channel.cd(fileTransfer.getDirectory());
        logger.info(String.format("changed to directory '%s'", channel.pwd()));
    }

    private File createTempFile(ChannelSftp.LsEntry ftpFile) throws IOException {
        String prefix = StringUtils.stripFilenameExtension(ftpFile.getFilename());
        String suffix = SEPARATOR_EXTENSION + StringUtils.getFilenameExtension(ftpFile.getFilename());

        File file = File.createTempFile(prefix, suffix);
        logger.info(String.format("created temp file '%s'", file.getName()));
        return file;
    }

    private void downloadFile(File file, ChannelSftp.LsEntry ftpFile) throws FileNotFoundException, SftpException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);

            logger.info(String.format("begin download of file '%s'", ftpFile.getFilename()));
            channel.get(ftpFile.getFilename(), out);
            logger.info(String.format("end download of file '%s'", ftpFile.getFilename()));
        }
        finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                }
                catch (Exception ignore) {
                }
            }
        }
    }
    
    private boolean isCSVFile(ChannelSftp.LsEntry ftpFile) {
        if (ftpFile.getAttrs().isDir() || ftpFile.getAttrs().isLink()) {
            return false;
        }
        
        String path = ftpFile.getFilename();
        String extension = StringUtils.getFilenameExtension(path);
        return CSV_EXTENSION.equalsIgnoreCase(extension);
    }
    
    private boolean isConnected() {
        return session != null && session.isConnected() && channel != null && channel.isConnected();
    }
    
    private void markFileAsDownloaded(ChannelSftp.LsEntry ftpFile) throws SftpException {
        String filename = ftpFile.getFilename();
        String newFilename = filename + SEPARATOR_EXTENSION + OK_EXTENSION;

        channel.rename(filename, newFilename);
        logger.info(String.format("marked file '%s' as downloaded", filename));
    }
}