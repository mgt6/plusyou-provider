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

import com.openplanetideas.plusyou.provider.domain.Interest;
import com.openplanetideas.plusyou.provider.domain.Opportunity;
import com.openplanetideas.plusyou.provider.domain.Vendor;
import com.openplanetideas.plusyou.provider.resource.common.AbstractResource;
import com.openplanetideas.plusyou.provider.resource.param.DateParam;
import com.openplanetideas.plusyou.provider.resource.param.common.RequiredParam;
import com.openplanetideas.plusyou.provider.util.OpportunityDistancePredicate;
import com.openplanetideas.plusyou.provider.util.OpportunityUtils;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.util.UidGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.SocketException;
import java.util.*;

@Controller
@Scope(WebApplicationContext.SCOPE_REQUEST)
@Path("opportunities")
public class OpportunityResource extends AbstractResource {

    private static final Integer MAX_RESULTS = 30;

    @GET
    @Path("/ical/{ical}")
    public Response getIcal(@PathParam("ical") Long idParma){
        Opportunity opportunity = opportunityRepository.findById(idParma);
        if (opportunity == null) {
            return Response.noContent().build();
        }

        Date date = opportunity.getDate();
        Date start = opportunity.getBeginTime();
        Date end = opportunity.getEndTime();

        VEvent event;

        if(start != null && end != null){
          event   = new VEvent(new net.fortuna.ical4j.model.Date(start),new net.fortuna.ical4j.model.Date(end), opportunity.getTitle());
        }       else {
            event = new VEvent(new net.fortuna.ical4j.model.Date(date), opportunity.getTitle());
        }
        // initialise as an all-day event..


        // Generate a UID for the event..
        UidGenerator ug = null;
        try {
            ug = new UidGenerator(Integer.toString(opportunity.hashCode()));
        } catch (SocketException e) {


        }
        event.getProperties().add(ug.generateUid());

        net.fortuna.ical4j.model.Calendar cal = new net.fortuna.ical4j.model.Calendar();
        cal.getComponents().add(event);

        return Response.ok(cal.toString(), MediaType.TEXT_PLAIN).build();


    }

    @GET
    @Path("{id}")
    public Response findById(@PathParam("id") Long idParam, @QueryParam("asJson") Boolean asJson) {
        Opportunity opportunity = opportunityRepository.findById(idParam);
        if (opportunity == null) {
            return Response.noContent().build();
        }
        else {
            if(asJson == null || !asJson){
                return Response.ok(opportunity).build();
            }else {
                return Response.ok(opportunity, MediaType.APPLICATION_JSON).build();
            }
        }
    }

    @GET
    @Path("ids")
    public Response findByIds(@QueryParam("id") List<Long> opportunityIds, @QueryParam("asJson") Boolean asJson) {
        List<Opportunity> opportunities = opportunityRepository.findByIds(opportunityIds);

        if (opportunities.isEmpty()) {
            return Response.noContent().build();
        } else {
            GenericEntity<List<Opportunity>> entity = new GenericEntity<List<Opportunity>>(opportunities){};
            if(asJson == null || !asJson){
                return Response.ok(entity).build();
            }else {
                return Response.ok(entity, MediaType.APPLICATION_JSON).build();
            }
        }
    }

    @GET
    public Response findForCriteria(
                                    @QueryParam("asJson") Boolean asJson,
                                    @MatrixParam("interest") Long interestParam,
                                    @RequiredParam @MatrixParam("beginDate") DateParam beginDateParam,
                                    @RequiredParam @MatrixParam("endDate") DateParam endDateParam,
                                    @RequiredParam @MatrixParam("distance") Integer distanceParam,
                                    @RequiredParam @MatrixParam("latitude") Double latitudeParam,
                                    @RequiredParam @MatrixParam("longitude") Double longitudeParam,
                                    @RequiredParam @MatrixParam("vendor") Long vendorParam){
        Date beginDate = beginDateParam.getValue();
        Date endDate = endDateParam.getValue();
        Vendor vendor = new Vendor.Builder().withId(vendorParam).build();

        List<Opportunity> opportunities;
        if (interestParam == null) {
            opportunities = opportunityRepository.findForDate(beginDate, endDate, vendor);
        }
        else {
            Interest interest = new Interest.Builder().withId(interestParam).build();
            opportunities = opportunityRepository.findForInterestAndDate(interest, beginDate, endDate, vendor);
        }

        if (opportunities.isEmpty()) {
            return Response.noContent().build();
        }
        else {
            OpportunityUtils.calculateAndSetDistance(latitudeParam, longitudeParam, opportunities);
            CollectionUtils.filter(opportunities, new OpportunityDistancePredicate(distanceParam));
            Collections.sort(opportunities);

            int end = opportunities.size() >= MAX_RESULTS ? MAX_RESULTS : opportunities.size();
            GenericEntity<List<Opportunity>> entities = new GenericEntity<List<Opportunity>>(opportunities.subList(0, end)){};
            if (entities.getEntity().isEmpty()) {
                return Response.noContent().build();
            }
            else {
                if(asJson == null || !asJson){
                    return Response.ok(entities).build();
                }else {
                    return Response.ok(entities, MediaType.APPLICATION_JSON).build();
                }
            }
        }
    }
}