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

import com.openplanetideas.plusyou.provider.domain.Country;
import com.openplanetideas.plusyou.provider.domain.Interest;
import com.openplanetideas.plusyou.provider.domain.Opportunity;
import com.openplanetideas.plusyou.provider.domain.Vendor;
import com.openplanetideas.plusyou.provider.domain.common.Address;
import com.openplanetideas.plusyou.provider.domain.common.GeoLocation;

import java.util.ArrayList;
import java.util.List;

public class OpportunityTestData {

    private OpportunityTestData() {
    }

    public static Opportunity getInsertData() {
        Address address = new Address.Builder()
                .withStreet("street opp")
                .withPostcode("postcode opp")
                .withCity("city opp")
                .withCountry(new Country.Builder().withName("United Kingdom").build())
                .build();

        List<Interest> interests = new ArrayList<Interest>();
        interests.add(new Interest.Builder().withName("Art and Culture").withType(Interest.InterestCategory.ART).build());
        interests.add(new Interest.Builder().withName("Disability").withType(Interest.InterestCategory.DISABILITY).build());
        interests.add(new Interest.Builder().withName("Music").withType(Interest.InterestCategory.MUSIC).build());
        interests.add(new Interest.Builder().withName("Education and Literacy").withType(Interest.InterestCategory.EDUCATION).build());

        return new Opportunity.Builder()
                .withExternalId(5000L)
                .withTitle("title opp")
                .withDescription("description opp")
                .withDate("2012-02-07")
                .withBeginTime("13:00")
                .withEndTime("14:00")
                .withVendor(new Vendor.Builder().withName("Do-it").build())
                .withGeoLocation(new GeoLocation.Builder().withLatitude(50.885167).withLongitude(4.465771).build())
                .withAddress(address)
                .withInterests(interests)
                .withOrganization(OrganizationTestData.getInsertData())
                .build();
    }
    
    public static Opportunity getUpdateData() {
        Address address = new Address.Builder()
                .withStreet("new street opp")
                .withPostcode("new postcode opp")
                .withCity("new city opp")
                .withCountry(new Country.Builder().withName("United Kingdom").build())
                .build();

        List<Interest> interests = new ArrayList<Interest>();
        interests.add(new Interest.Builder().withName("Sport and Outdoor Activities").withType(Interest.InterestCategory.SPORT).build());
        interests.add(new Interest.Builder().withName("Politics").withType(Interest.InterestCategory.POLITICS).build());
        interests.add(new Interest.Builder().withName("Elderly").withType(Interest.InterestCategory.ELDERLY).build());
        interests.add(new Interest.Builder().withName("Music").withType(Interest.InterestCategory.MUSIC).build());

        return new Opportunity.Builder()
                .withExternalId(5000L)
                .withTitle("new title opp")
                .withDescription("new description opp")
                .withDate("2013-02-07")
                .withBeginTime("14:00")
                .withEndTime("15:00")
                .withVendor(new Vendor.Builder().withName("Do-it").build())
                .withGeoLocation(new GeoLocation.Builder().withLatitude(60.885167).withLongitude(5.465771).build())
                .withAddress(address)
                .withInterests(interests)
                .withOrganization(OrganizationTestData.getInsertData())
                .build();
    }
}