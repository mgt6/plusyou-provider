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

package com.openplanetideas.plusyou.provider.csv.rowmapper;

import com.openplanetideas.plusyou.provider.csv.config.common.SqlPlaceholder;
import com.openplanetideas.plusyou.provider.csv.config.common.SqlPlaceholderSplitter;
import com.openplanetideas.plusyou.provider.csv.rowmapper.common.RowMapperSplitter;
import org.springframework.stereotype.Component;

@Component
public class InsertOpportunitiesInterestsRowMapper extends RowMapperSplitter {

    @Override
    public String getSqlFilename() {
        return "insert_opportunities_interests.sql";
    }

    @Override
    public SqlPlaceholderSplitter getSqlPlaceholderSplitter() {
        return new SqlPlaceholderSplitter("interest_type", "Interests", "\\$");
    }

    @Override
    protected void defineSqlPlaceholders() {
        addSqlPlaceholder(new SqlPlaceholder("externalId", "Id"));
        addSqlPlaceholder(new SqlPlaceholder("vendor_name", "Vendor"));
    }
}