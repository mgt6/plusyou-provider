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

package com.openplanetideas.plusyou.provider.csv.config;

import com.openplanetideas.plusyou.provider.csv.config.common.Column;
import com.openplanetideas.plusyou.provider.csv.config.common.Row;

public class OpportunityRow extends Row {
    
    @Override
    protected void defineColumns() {
        addColumn(new Column("Id"));
        addColumn(new Column("Title"));
        addColumn(new Column("Description"));
        addColumn(new Column("Date"));
        addColumn(new Column("BeginTime"));
        addColumn(new Column("EndTime"));
        addColumn(new Column("Vendor"));
        addColumn(new Column("Latitude"));
        addColumn(new Column("Longitude"));
        addColumn(new Column("Street", false));
        addColumn(new Column("Postcode"));
        addColumn(new Column("City", false));
        addColumn(new Column("Country"));
        addColumn(new Column("Interests"));
        addColumn(new Column("OrganizationName"));
        addColumn(new Column("OrganizationStreet", false));
        addColumn(new Column("OrganizationPostcode"));
        addColumn(new Column("OrganizationCity", false));
        addColumn(new Column("OrganizationCountry"));
        addColumn(new Column("OrganizationTelephone", false));
        addColumn(new Column("OrganizationEmail", false));
        addColumn(new Column("OrganizationWebSite", false));
    }
}