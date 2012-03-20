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

package com.openplanetideas.plusyou.provider.csv.service;

import com.google.common.collect.Lists;
import com.openplanetideas.plusyou.provider.config.AppConfig;
import com.openplanetideas.plusyou.provider.config.EnvironmentTestType;
import com.openplanetideas.plusyou.provider.csv.service.common.OpportunityTestData;
import com.openplanetideas.plusyou.provider.csv.service.common.OrganizationTestData;
import com.openplanetideas.plusyou.provider.csv.service.common.RowStatus;
import com.openplanetideas.plusyou.provider.domain.Opportunity;
import com.openplanetideas.plusyou.provider.domain.Organization;
import com.openplanetideas.plusyou.provider.repository.OpportunityRepository;
import com.openplanetideas.plusyou.provider.repository.OrganizationRepository;
import com.openplanetideas.plusyou.provider.repository.VendorRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AppConfig.class)
@ActiveProfiles(EnvironmentTestType.INTEGRATION_TEST)
@Transactional
public class OpportunityImportServiceIntegrationTest {

    private static final Logger LOGGER = Logger.getLogger(OpportunityImportServiceIntegrationTest.class.getName());

    @Autowired
    private OpportunityImportService opportunityImportService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Test
    public void checkOpportunityData() throws Exception {
        String filename = "file_with_opp_id_5000.csv";
        opportunityImportService.processFile(getFile(filename), filename, LOGGER);
        Opportunity actualOpportunity = opportunityRepository.findByExternalIdAndVendor(5000L, vendorRepository.findByName("Do-it"));
        Opportunity expectedOpportunity = OpportunityTestData.getInsertData();
        assertEquals(expectedOpportunity, actualOpportunity);

        filename = "file_with_opp_id_5000_with_updated_opp_data.csv";
        opportunityImportService.processFile(getFile(filename), filename, LOGGER);
        entityManager.refresh(actualOpportunity);
        expectedOpportunity = OpportunityTestData.getUpdateData();
        assertEquals(expectedOpportunity, actualOpportunity);
    }

    @Test
    public void checkOrganizationData() throws Exception {
        String filename = "file_with_opp_id_5000.csv";
        opportunityImportService.processFile(getFile(filename), filename, LOGGER);
        Organization actualOrganization = organizationRepository.findByName("name org");
        Organization expectedOrganization = OrganizationTestData.getInsertData();
        assertEquals(expectedOrganization, actualOrganization);
        
        filename = "file_with_opp_id_5000_with_updated_org_data.csv";
        opportunityImportService.processFile(getFile(filename), filename, LOGGER);
        entityManager.refresh(actualOrganization);
        expectedOrganization = OrganizationTestData.getUpdateData();
        assertEquals(expectedOrganization, actualOrganization);
    }

    @Test
    public void fileWithMissingTitleHeader() throws Exception {
        String filename = "file_with_missing_title_header.csv";
        List<RowStatus> actualFailedRows = opportunityImportService.processFile(getFile(filename), filename, LOGGER);
        List<RowStatus> expectedFailedRows = Lists.newArrayList(RowStatus.failed(0L, "InvalidHeaderException: missing headers [Title]"));
        
        assertEquals(expectedFailedRows, actualFailedRows);
    }
    
    @Test
    public void rowWithBlankTitle() throws Exception {
        String filename = "row_with_blank_title.csv";
        List<RowStatus> actualFailedRows = opportunityImportService.processFile(getFile(filename), filename, LOGGER);
        List<RowStatus> expectedFailedRows = Lists.newArrayList(RowStatus.failed(1L, "RequiredColumnException: column 'Title' must have a value"));

        assertEquals(expectedFailedRows, actualFailedRows);
    }

    @Test
    public void rowWithInvalidInterestType() throws Exception {
        String filename = "row_with_invalid_interest_type.csv";
        List<RowStatus> actualFailedRows = opportunityImportService.processFile(getFile(filename), filename, LOGGER);
        List<RowStatus> expectedFailedRows = Lists.newArrayList(RowStatus.failed(1L, "MySQLIntegrityConstraintViolationException: Column 'interest_id' cannot be null"));

        assertEquals(expectedFailedRows, actualFailedRows);
    }
    
    @Test
    public void rowWithNoFailures() throws Exception {
        String filename = "row_with_no_failures.csv";
        List<RowStatus> actualFailedRows = opportunityImportService.processFile(getFile(filename), filename, LOGGER);
        List<RowStatus> expectedFailedRows = Lists.newArrayList();
        
        assertEquals(expectedFailedRows, actualFailedRows);
    }
    
    private InputStream getFile(String filename) {
        return getClass().getResourceAsStream(filename);
    }
}