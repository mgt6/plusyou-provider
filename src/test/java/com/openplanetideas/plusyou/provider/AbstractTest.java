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

package com.openplanetideas.plusyou.provider;

import com.openplanetideas.plusyou.provider.domain.Vendor;
import com.openplanetideas.plusyou.provider.domain.common.AbstractEntity;
import com.openplanetideas.plusyou.provider.resource.param.DateParam;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTest {

    private static final String DATE = "20110101";

    protected DateParam createDateParam() {
        return new DateParam(DATE);
    }

    protected List<Long> createListOfIds(List<? extends AbstractEntity> entities) {
        List<Long> listOfIds = new ArrayList<Long>();
        for (AbstractEntity entity : entities) {
            listOfIds.add(entity.getId());
        }
        return listOfIds;
    }

    protected List<Vendor> createVendors(String... names) {
        List<Vendor> vendors = new ArrayList<Vendor>();
        for (String name : names) {
            vendors.add(new Vendor.Builder().withName(name).build());
        }
        return vendors;
    }
}