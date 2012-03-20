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

package com.openplanetideas.plusyou.provider.resource;

import com.google.common.collect.Lists;
import com.openplanetideas.plusyou.provider.domain.Interest;
import com.openplanetideas.plusyou.provider.domain.Opportunity;
import com.openplanetideas.plusyou.provider.domain.Vendor;
import com.openplanetideas.plusyou.provider.domain.common.GeoLocation;
import com.openplanetideas.plusyou.provider.resource.common.AbstractResourceTest;
import org.junit.Test;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OpportunityResourceTest extends AbstractResourceTest {

    @Test
    public void findByIdWhenOpportunityIsFound() {
        Opportunity expectedOpportunity = new Opportunity.Builder().withTitle("Free Summer Film Training").build();
        when(opportunityRepository.findById(anyLong())).thenReturn(expectedOpportunity);
        Response response = opportunityResource.findById(0L);
        verify(opportunityRepository).findById(anyLong());
        Opportunity actualOpportunity = (Opportunity) response.getEntity();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedOpportunity, actualOpportunity);
    }

    @Test
    public void findByIdWhenOpportunityIsNotFound() {
        when(opportunityRepository.findById(anyLong())).thenReturn(null);
        Response response = opportunityResource.findById(0L);
        verify(opportunityRepository).findById(anyLong());

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void findForCriteriaWhenNoOpportunitiesAreFound() {
        Interest interest = any(Interest.class);
        Date date = any(Date.class);
        Vendor vendor = any(Vendor.class);
        when(opportunityRepository.findForInterestAndDate(interest, date, eq(date), vendor)).thenReturn(Collections.<Opportunity>emptyList());
        Response response = opportunityResource.findForCriteria(0L, createDateParam(), createDateParam(), 0, 0D, 0D, 0L);
        verify(opportunityRepository, never()).findForDate(any(Date.class), any(Date.class), any(Vendor.class));
        verify(opportunityRepository).findForInterestAndDate(any(Interest.class), any(Date.class), any(Date.class), any(Vendor.class));

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void findForCriteriaWhenNoOpportunitiesAreFoundAfterCalculatingDistance() {
        List<Opportunity> opportunities = new ArrayList<Opportunity>();
        opportunities.add(new Opportunity.Builder().withId(1L).withGeoLocation(new GeoLocation.Builder().withLatitude(51.369664).withLongitude(-0.186338).build()).build());
        opportunities.add(new Opportunity.Builder().withId(2L).withGeoLocation(new GeoLocation.Builder().withLatitude(51.368324).withLongitude(-0.183892).build()).build());
        opportunities.add(new Opportunity.Builder().withId(3L).withGeoLocation(new GeoLocation.Builder().withLatitude(51.362463).withLongitude(-0.19572).build()).build());
        opportunities.add(new Opportunity.Builder().withId(4L).withGeoLocation(new GeoLocation.Builder().withLatitude(51.360929).withLongitude(-0.160761).build()).build());
        when(opportunityRepository.findForInterestAndDate(any(Interest.class), any(Date.class), any(Date.class), any(Vendor.class))).thenReturn(opportunities);
        Response response = opportunityResource.findForCriteria(0L, createDateParam(), createDateParam(), 0, 0D, 0D, 0L);
        verify(opportunityRepository, never()).findForDate(any(Date.class), any(Date.class), any(Vendor.class));
        verify(opportunityRepository).findForInterestAndDate(any(Interest.class), any(Date.class), any(Date.class), any(Vendor.class));

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void findForCriteriaWhenOpportunitiesAreFoundAfterCalculatingDistance() {
        List<Opportunity> opportunities = new ArrayList<Opportunity>();
        opportunities.add(new Opportunity.Builder().withId(1L).withGeoLocation(new GeoLocation.Builder().withLatitude(51.369664).withLongitude(-0.186338).build()).build());
        opportunities.add(new Opportunity.Builder().withId(2L).withGeoLocation(new GeoLocation.Builder().withLatitude(51.368324).withLongitude(-0.183892).build()).build());
        opportunities.add(new Opportunity.Builder().withId(3L).withGeoLocation(new GeoLocation.Builder().withLatitude(51.362463).withLongitude(-0.19572).build()).build());
        opportunities.add(new Opportunity.Builder().withId(4L).withGeoLocation(new GeoLocation.Builder().withLatitude(51.360929).withLongitude(-0.160761).build()).build());
        when(opportunityRepository.findForInterestAndDate(any(Interest.class), any(Date.class), any(Date.class), any(Vendor.class))).thenReturn(opportunities);
        Response response = opportunityResource.findForCriteria(0L, createDateParam(), createDateParam(), 1, 51.369088, -0.179579, 0L);
        verify(opportunityRepository, never()).findForDate(any(Date.class), any(Date.class), any(Vendor.class));
        verify(opportunityRepository).findForInterestAndDate(any(Interest.class), any(Date.class), any(Date.class), any(Vendor.class));

        List<Opportunity> expectedOpportunities = new ArrayList<Opportunity>();
        expectedOpportunities.add(new Opportunity.Builder().withId(2L).withDistance(0.31).withGeoLocation(new GeoLocation.Builder().withLatitude(51.368324).withLongitude(-0.183892).build()).build());
        expectedOpportunities.add(new Opportunity.Builder().withId(1L).withDistance(0.47).withGeoLocation(new GeoLocation.Builder().withLatitude(51.369664).withLongitude(-0.186338).build()).build());
        List<Opportunity> actualOpportunities = ((GenericEntity<List<Opportunity>>) response.getEntity()).getEntity();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedOpportunities, actualOpportunities);
    }

    @Test
    public void findForCriteriaWhenOpportunitiesHasNoInterestSpecified() {
        List<Opportunity> opportunities = new ArrayList<Opportunity>();
        opportunities.add(new Opportunity.Builder().withId(1L).withGeoLocation(new GeoLocation.Builder().withLatitude(51.369664).withLongitude(-0.186338).build()).build());
        opportunities.add(new Opportunity.Builder().withId(2L).withGeoLocation(new GeoLocation.Builder().withLatitude(51.368324).withLongitude(-0.183892).build()).build());
        opportunities.add(new Opportunity.Builder().withId(3L).withGeoLocation(new GeoLocation.Builder().withLatitude(51.362463).withLongitude(-0.19572).build()).build());
        opportunities.add(new Opportunity.Builder().withId(4L).withGeoLocation(new GeoLocation.Builder().withLatitude(51.360929).withLongitude(-0.160761).build()).build());
        when(opportunityRepository.findForDate(any(Date.class), any(Date.class), any(Vendor.class))).thenReturn(opportunities);
        Response response = opportunityResource.findForCriteria(null, createDateParam(), createDateParam(), 1, 51.369088, -0.179579, 0L);
        verify(opportunityRepository).findForDate(any(Date.class), any(Date.class), any(Vendor.class));
        verify(opportunityRepository, never()).findForInterestAndDate(any(Interest.class), any(Date.class), any(Date.class), any(Vendor.class));

        List<Opportunity> expectedOpportunities = new ArrayList<Opportunity>();
        expectedOpportunities.add(new Opportunity.Builder().withId(2L).withDistance(0.31).withGeoLocation(new GeoLocation.Builder().withLatitude(51.368324).withLongitude(-0.183892).build()).build());
        expectedOpportunities.add(new Opportunity.Builder().withId(1L).withDistance(0.47).withGeoLocation(new GeoLocation.Builder().withLatitude(51.369664).withLongitude(-0.186338).build()).build());
        List<Opportunity> actualOpportunities = ((GenericEntity<List<Opportunity>>) response.getEntity()).getEntity();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedOpportunities, actualOpportunities);
    }

    @Test
    public void findOpportunitiesByIdsWithNoResults() {
        when(opportunityRepository.findByIds(Lists.<Long>newArrayList())).thenReturn(Lists.<Opportunity>newArrayList());
        Response response = opportunityResource.findByIds(Lists.<Long>newArrayList());
        verify(opportunityRepository).findByIds(Lists.<Long>newArrayList());

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void findOpportunitiesByIdsWithResults() {
        List<Long> opportunityIdList = Lists.newArrayList(1L, 2L, 8L);

        List<Opportunity> opportunities = Lists.newArrayList();
        opportunities.add(new Opportunity.Builder().withId(1L).build());
        opportunities.add(new Opportunity.Builder().withId(2L).build());
        opportunities.add(new Opportunity.Builder().withId(8L).build());

        when(opportunityRepository.findByIds(opportunityIdList)).thenReturn(opportunities);
        Response response = opportunityResource.findByIds(opportunityIdList);
        verify(opportunityRepository).findByIds(opportunityIdList);

        List<Opportunity> actualOpportunities = ((GenericEntity<List<Opportunity>>) response.getEntity()).getEntity();
        List<Opportunity> expectedOpportunities = Lists.newArrayList();
        expectedOpportunities.add(new Opportunity.Builder().withId(1L).build());
        expectedOpportunities.add(new Opportunity.Builder().withId(2L).build());
        expectedOpportunities.add(new Opportunity.Builder().withId(8L).build());

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(actualOpportunities, expectedOpportunities);
    }
}