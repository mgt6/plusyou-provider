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

import com.openplanetideas.plusyou.provider.csv.config.OpportunityRow;
import com.openplanetideas.plusyou.provider.csv.config.common.Row;
import com.openplanetideas.plusyou.provider.csv.rowmapper.DeleteOpportunitiesInterestsRowMapper;
import com.openplanetideas.plusyou.provider.csv.rowmapper.InsertOpportunitiesInterestsRowMapper;
import com.openplanetideas.plusyou.provider.csv.rowmapper.MergeOpportunitiesRowMapper;
import com.openplanetideas.plusyou.provider.csv.rowmapper.MergeOrganizationsRowMapper;
import com.openplanetideas.plusyou.provider.csv.rowmapper.common.RowMapper;
import com.openplanetideas.plusyou.provider.csv.service.common.ImportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpportunityImportService extends ImportService {
    
    @Inject
    private MergeOrganizationsRowMapper mergeOrganizations;

    @Inject
    private MergeOpportunitiesRowMapper mergeOpportunities;
    
    @Inject
    private DeleteOpportunitiesInterestsRowMapper deleteOpportunitiesInterests;
    
    @Inject
    private InsertOpportunitiesInterestsRowMapper insertOpportunitiesInterests;

    @Inject
    public OpportunityImportService(DataSource dataSource, PlatformTransactionManager transactionManager) {
        super(dataSource, transactionManager);
    }

    @Override
    protected Row getRow() {
        return new OpportunityRow();
    }

    @Override
    protected List<RowMapper> getRowMappers() {
        List<RowMapper> rowMappers = new ArrayList<RowMapper>();
        rowMappers.add(mergeOrganizations);
        rowMappers.add(mergeOpportunities);
        rowMappers.add(deleteOpportunitiesInterests);
        rowMappers.add(insertOpportunitiesInterests);
        return rowMappers;
    }
}