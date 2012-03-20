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

package com.openplanetideas.plusyou.provider.repository.impl;

import com.google.common.collect.Lists;
import com.openplanetideas.plusyou.provider.domain.Country;
import com.openplanetideas.plusyou.provider.domain.Interest;
import com.openplanetideas.plusyou.provider.domain.Opportunity;
import com.openplanetideas.plusyou.provider.domain.Organization;
import com.openplanetideas.plusyou.provider.domain.Vendor;
import com.openplanetideas.plusyou.provider.domain.common.Address;
import com.openplanetideas.plusyou.provider.domain.common.GeoLocation;
import com.openplanetideas.plusyou.provider.repository.common.AbstractRepositoryTest;
import com.openplanetideas.plusyou.provider.util.DateUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OpportunityRepositoryImplTest extends AbstractRepositoryTest {
    
    @Test
    public void findByExternalIdAndVendor() {
        Opportunity expectedOpportunity = new Opportunity.Builder().withTitle("Cheering Squad").build();
        Opportunity actualOpportunity = opportunityRepository.findByExternalIdAndVendor(-1L, vendorRepository.findByName("Do-it"));
        assertEquals(expectedOpportunity.getTitle(), actualOpportunity.getTitle());
    }

    @Test
    public void findById() {
        Country country = new Country.Builder().withName("United Kingdom").build();
        Address address = new Address.Builder().withStreet("Duke St").withPostcode("SM1 4").withCity("Sutton").withCountry(country).build();
        GeoLocation geoLocation = new GeoLocation.Builder().withLatitude(51.368324).withLongitude(-0.183892).build();
        Vendor vendor = new Vendor.Builder().withName("Do-it").build();

        Address organizationAddress = new Address.Builder().withStreet("31 West Street").withPostcode("SM1 1SJ").withCity("Sutton").withCountry(country).build();
        Organization organization = new Organization.Builder()
                .withName("Volunteer Centre Sutton")
                .withAddress(organizationAddress)
                .withTelephone("0208 661 5900")
                .withEmail("vcsutton@vcsutton.org.uk")
                .build();

        Opportunity actualOpportunity = opportunityRepository.findById(2L);

        List<Interest> interests = new ArrayList<Interest>();
        interests.add(new Interest.Builder().withName("Art and Culture").withType(Interest.InterestCategory.ART).build());
        interests.add(new Interest.Builder().withName("Politics").withType(Interest.InterestCategory.POLITICS).build());
        Opportunity expectedOpportunity = new Opportunity.Builder()
                .withAddress(address)
                .withGeoLocation(geoLocation)
                .withDate("2011-10-07")
                .withBeginTime("14:30")
                .withEndTime("16:30")
                .withExternalId(-2L)
                .withTitle("Free Summer Film Training")
                .withDescription("Learn how to make TV with something to say Free summer film training for 16-25 year olds")
                .withInterests(interests)
                .withVendor(vendor)
                .withOrganization(organization)
                .build();

        assertEquals(expectedOpportunity, actualOpportunity);
    }

    @Test
    public void findForDate() {
        Date beginDate = DateUtils.parseDate("2011-10-06");
        Date endDate = DateUtils.parseDate("2011-10-22");
        Vendor vendor = new Vendor.Builder().withId(1L).build();

        List<Opportunity> actualOpportunities = opportunityRepository.findForDate(beginDate, endDate, vendor);
        List<Long> actualOpportunityIds = createListOfIds(actualOpportunities);
        List<Long> expectedOpportunityIds = Arrays.asList(2L, 3L, 4L, 5L);

        assertEquals(expectedOpportunityIds, actualOpportunityIds);
    }

    @Test
    public void findForInterestAndDate() {
        Interest interest = new Interest.Builder().withId(4L).build();
        Date beginDate = DateUtils.parseDate("2011-10-06");
        Date endDate = DateUtils.parseDate("2011-10-22");
        Vendor vendor = new Vendor.Builder().withId(1L).build();

        List<Opportunity> actualOpportunities = opportunityRepository.findForInterestAndDate(interest, beginDate, endDate, vendor);
        List<Long> actualOpportunityIds = createListOfIds(actualOpportunities);
        List<Long> expectedOpportunityIds = Arrays.asList(3L, 4L, 5L);

        assertEquals(expectedOpportunityIds, actualOpportunityIds);
    }

    @Test
    public void findOpportunitiesInId() {
        List<Long> opportunityIds = Lists.newArrayList(1L, 2L, 4L, 5L, 6L);
        List<Opportunity> actualOpportunities = opportunityRepository.findByIds(opportunityIds);

        assertEquals(opportunityIds.size(), actualOpportunities.size());
    }
}