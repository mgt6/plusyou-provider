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

package com.openplanetideas.plusyou.provider.csv.service.common;

import com.csvreader.CsvReader;
import com.openplanetideas.plusyou.provider.csv.config.common.Column;
import com.openplanetideas.plusyou.provider.csv.config.common.Row;
import com.openplanetideas.plusyou.provider.csv.config.common.SqlPlaceholder;
import com.openplanetideas.plusyou.provider.csv.exception.InvalidHeaderException;
import com.openplanetideas.plusyou.provider.csv.exception.RowMapperException;
import com.openplanetideas.plusyou.provider.csv.rowmapper.common.RowMapper;
import com.openplanetideas.plusyou.provider.csv.rowmapper.common.RowMapperSplitter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public abstract class ImportService {

    private static final char DEFAULT_DELIMITER = '#';
    private static final String DEFAULT_CHAR_ENCODING = CharEncoding.ISO_8859_1;
    
    private CsvReader reader;
    private Logger logger;
    private NamedParameterJdbcTemplate jdbcTemplate;
    private PlatformTransactionManager transactionManager;
    
    protected abstract Row getRow();

    protected abstract List<RowMapper> getRowMappers();

    protected ImportService(DataSource dataSource, PlatformTransactionManager transactionManager) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.transactionManager = transactionManager;
    }

    public List<RowStatus> processFile(InputStream in, String filename, Logger logger) {
        List<RowStatus> failedRows = new ArrayList<RowStatus>();
        try {
            this.logger = logger;
            logger.info(String.format("begin process of file '%s'", filename));
            reader = new CsvReader(in, getDelimiter(), Charset.forName(getCharEncoding()));

            checkHeader();
            while (reader.readRecord()) {
                RowStatus rowStatus = processRow();
                if (rowStatus.isFailed()) {
                    failedRows.add(rowStatus);
                }
            }
            return failedRows;
        }
        catch (Exception e) {
            String msg = ExceptionUtils.getRootCauseMessage(e);
            logger.severe(msg);
            
            failedRows.add(RowStatus.failed(getCurrentRecord(), msg));
            return failedRows;
        }
        finally {
            if (reader != null) {
                reader.close();
            }

            logger.info(String.format("end process of file '%s'", filename));
        }
    }

    protected String getCharEncoding() {
        return DEFAULT_CHAR_ENCODING;
    }

    protected char getDelimiter() {
        return DEFAULT_DELIMITER;
    }

    private MapSqlParameterSource[] addCsvValues(RowMapper rowMapper, Row row, MapSqlParameterSource[] parameters, MapSqlParameterSource parameter) {
        for (SqlPlaceholder sqlPlaceholder : rowMapper.getSqlPlaceholders()) {
            String value = rowMapper.getCsvValue(row, sqlPlaceholder);
            parameter.addValue(sqlPlaceholder.getSqlParam(), value);
        }
        
        return ArrayUtils.add(parameters, parameter);
    }

    private void checkHeader() throws IOException, InvalidHeaderException {
        reader.readHeaders();
        Set<String> expectedHeaders = getRow().getColumnNames();
        Collection<String> actualHeaders = Arrays.asList(reader.getHeaders());

        expectedHeaders.removeAll(actualHeaders);
        if (!expectedHeaders.isEmpty()) {
            throw new InvalidHeaderException(expectedHeaders);
        }
    }
    
    private long getCurrentRecord() {
        return reader.getCurrentRecord() + 1;
    }
    
    private RowStatus processRow() {
        try {
            Row row = getRow();
            for (Column column : row.getColumns()) {
                String value = reader.get(column.getName());
                column.setValue(value);
            }
            logger.info(String.format("successfully processed row %d", getCurrentRecord()));
            updateDatabase(row);
            
            return RowStatus.success(getCurrentRecord());
        }
        catch (Exception e) {
            String msg = ExceptionUtils.getRootCauseMessage(e);
            logger.info(String.format("unable to process row %d, %s", getCurrentRecord(), msg));
            return RowStatus.failed(getCurrentRecord(), msg);
        }
    }

    private void updateDatabase(Row row) throws RowMapperException {
        TransactionStatus transactionStatus = null;
        try {
            logger.info(String.format("begin update database of row %d", getCurrentRecord()));
            transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
            for (RowMapper rowMapper : getRowMappers()) {
                MapSqlParameterSource[] parameters = new MapSqlParameterSource[]{};

                if (rowMapper instanceof RowMapperSplitter) {
                    RowMapperSplitter splitter = (RowMapperSplitter) rowMapper;
                    for (String value : splitter.getCsvValues(row)) {
                        MapSqlParameterSource parameter = new MapSqlParameterSource();
                        parameter.addValue(splitter.getSqlPlaceholderSplitter().getSqlParam(), value);
                        parameters = addCsvValues(rowMapper, row, parameters, parameter);
                    }
                }
                else {
                    MapSqlParameterSource parameter = new MapSqlParameterSource();
                    parameters = addCsvValues(rowMapper, row, parameters, parameter);
                }

                logger.info(String.format("begin execute of sql '%s'", rowMapper.getSqlFilename()));
                jdbcTemplate.batchUpdate(rowMapper.getSql(), parameters);
                logger.info(String.format("end execute of sql '%s'", rowMapper.getSqlFilename()));
            }
            transactionManager.commit(transactionStatus);
        }
        catch (Exception e) {
            if (transactionStatus != null) {
                transactionManager.rollback(transactionStatus);
            }

            throw new RowMapperException("unable to update database", e);
        }
        finally {
            logger.info(String.format("end update database of row %d", getCurrentRecord()));
        }
    }
}