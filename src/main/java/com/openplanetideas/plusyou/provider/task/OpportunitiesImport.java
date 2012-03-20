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

import com.openplanetideas.plusyou.provider.config.EnvironmentType;
import com.openplanetideas.plusyou.provider.csv.service.OpportunityImportService;
import com.openplanetideas.plusyou.provider.csv.service.common.RowStatus;
import com.openplanetideas.plusyou.provider.filetransfer.domain.FileTransfer;
import com.openplanetideas.plusyou.provider.filetransfer.exception.FileTransferServiceException;
import com.openplanetideas.plusyou.provider.filetransfer.repository.FileTransferRepository;
import com.openplanetideas.plusyou.provider.filetransfer.service.FileTransferService;
import com.openplanetideas.plusyou.provider.filetransfer.service.TransferFile;
import com.openplanetideas.plusyou.provider.filetransfer.service.impl.JSchFileTransferService;
import com.openplanetideas.plusyou.provider.task.common.FailedFile;
import com.openplanetideas.plusyou.provider.task.common.ProcessStatus;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Service
public class OpportunitiesImport {

    @Inject
    private Environment environment;

    @Inject
    private StringEncryptor stringEncryptor;

    @Inject
    private OpportunityImportService opportunityImportService;

    @Inject
    private FileTransferRepository fileTransferRepository;
    
    @Inject
    private OpportunitiesNotification opportunitiesNotification;
    
    private Logger logger;
    private String logFilename;

    public OpportunitiesImport() {
        logger = Logger.getLogger(OpportunitiesImport.class.getName());
        logFilename = System.getProperty("java.io.tmpdir") + File.separator + "opportunities.log";
    }

    @Scheduled(cron = "0 0 6 * * ?")
    public void process() {
        if (ArrayUtils.contains(environment.getActiveProfiles(), EnvironmentType.INTERNET)) {
            List<FileTransfer> fileTransfers = fileTransferRepository.findAll();
            for (FileTransfer fileTransfer : fileTransfers) {
                ProcessStatus processStatus = startProcess(fileTransfer);
                sendEmail(fileTransfer, processStatus);
            }
        }
        else {
            logger.warning("skipping OpportunitiesImport");
        }
    }

    private List<TransferFile> getFilesToProcess(FileTransfer fileTransfer) throws FileTransferServiceException {
        FileTransferService fileTransferService = new JSchFileTransferService(fileTransfer, stringEncryptor, logger);
        fileTransferService.connect();
        
        List<TransferFile> files = fileTransferService.downloadFiles();
        fileTransferService.disconnect();
        return files;
    }

    private List<RowStatus> processFile(TransferFile transferFile) throws FileNotFoundException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(transferFile.getFile());
            return opportunityImportService.processFile(in, transferFile.getFilename(), logger);
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException ignore) {
                }
            }
            
            if (transferFile.getFile().delete()) {
                logger.info(String.format("successfully deleted local file: '%s'", transferFile.getFile().getName()));
            }
        }
    }

    private ProcessStatus processFiles(FileTransfer fileTransfer) throws FileTransferServiceException, FileNotFoundException {
        List<TransferFile> filesToProcess = getFilesToProcess(fileTransfer);
        if (filesToProcess.isEmpty()) {
            String msg = "no files found to process";
            logger.severe(msg);
            return ProcessStatus.failed(msg);
        }
        
        List<FailedFile> failedFiles = new ArrayList<FailedFile>();
        for (TransferFile transferFile : filesToProcess) {
            List<RowStatus> failedRows = processFile(transferFile);
            if (!failedRows.isEmpty()) {
                failedFiles.add(new FailedFile(transferFile.getFilename(), failedRows));
            }
        }
        
        if (failedFiles.isEmpty()) {
            return ProcessStatus.succeeded();
        }
        return ProcessStatus.succeededWithFailedFiles(failedFiles);
    }

    private void sendEmail(FileTransfer fileTransfer, ProcessStatus processStatus) {
        try {
            String from = environment.getRequiredProperty("smtp.from");
            String[] to = fileTransfer.getContactPerson().split(",");
            File logFile = new File(logFilename);
            opportunitiesNotification.sendEmail(from, to, processStatus, logFile);
        }
        catch (Exception e) {
            logger.severe(ExceptionUtils.getStackTrace(e));
        }
    }

    private ProcessStatus startProcess(FileTransfer fileTransfer) {
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler(logFilename);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            logger.info(String.format("STARTING OpportunityImportService for %s", fileTransfer.getHost()));
            return processFiles(fileTransfer);
        }
        catch (Exception e) {
            logger.severe(ExceptionUtils.getStackTrace(e));
            return ProcessStatus.failed(ExceptionUtils.getMessage(e));
        }
        finally {
            if (logger != null) {
                logger.info(String.format("STOPPING OpportunityImportService for %s", fileTransfer.getHost()));
            }

            if (fileHandler != null) {
                fileHandler.close();
            }
        }
    }
}