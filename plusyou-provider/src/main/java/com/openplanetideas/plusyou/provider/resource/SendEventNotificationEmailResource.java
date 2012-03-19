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

import com.openplanetideas.plusyou.provider.config.EnvironmentType;
import com.openplanetideas.plusyou.provider.domain.Opportunity;
import com.openplanetideas.plusyou.provider.email.EmailMessage;
import com.openplanetideas.plusyou.provider.email.EmailMessageSender;
import com.openplanetideas.plusyou.provider.email.EmailMessageText;
import com.openplanetideas.plusyou.provider.repository.OpportunityRepository;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.tools.generic.DateTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import javax.mail.MessagingException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Controller
@Scope(WebApplicationContext.SCOPE_REQUEST)
@Path("sendEventNotificationEmail")
public class SendEventNotificationEmailResource {

    private static final Logger LOG = Logger.getLogger(SendEventNotificationEmailResource.class.getName());

    @Autowired
    private Environment environment;
    
    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private EmailMessageSender emailMessageSender;
    
    @POST
    @Path("join/{user}/{opportunityId}")
    public Response sendJoinEventNotificationEmail(@PathParam("user") String user, @PathParam("opportunityId") Long opportunityId) throws MessagingException {
        if (internetProfile()) {
            String template = getTemplate("eventNotificationEmail.vm");
            sendEmail("join", template, user, opportunityId);
        }
        else {
            LOG.warning("skipping sendJoinEventNotificationEmail");
        }
        return Response.ok().build();
    }

    @POST
    @Path("withdraw/{user}/{opportunityId}")
    public Response sendWithdrawEventNotificationEmail(@PathParam("user") String user, @PathParam("opportunityId") Long opportunityId) throws MessagingException {
        if (internetProfile()) {
            String template = getTemplate("eventNotificationEmail.vm");
            sendEmail("withdraw", template, user, opportunityId);
        }
        else {
            LOG.warning("skipping sendWithdrawEventNotificationEmail");
        }
        return Response.ok().build();
    }

    private String getSubject(Opportunity opportunity, String type, String user) {
        return String.format("%s - %s -%s", opportunity.getTitle(), type, user);
    }
    
    private String getTemplate(String name) {
        String pck = getClass().getPackage().getName();
        pck = pck.replace(".", "/");
        
        return String.format("%s/%s", pck, name);
    }

    private EmailMessageText getText(String template, String type, String user, Opportunity opportunity) {
        Model model = new ExtendedModelMap();
        model.addAttribute("dateTool", new DateTool());
        model.addAttribute("type", type);
        model.addAttribute("user", user);
        model.addAttribute("opportunity", opportunity);
        
        return new EmailMessageText(template, model);
    }
    
    private boolean internetProfile() {
        return ArrayUtils.contains(environment.getActiveProfiles(), EnvironmentType.INTERNET);
    }

    private void sendEmail(String type, String template, String user, Long opportunityId) throws MessagingException {
        Opportunity opportunity = opportunityRepository.findById(opportunityId);
        String email = opportunity.getOrganization().getEmail();

        if (StringUtils.isNotBlank(email)) {
            String from = environment.getRequiredProperty("email.from");
            String subject = getSubject(opportunity, type, user);
            EmailMessageText text = getText(template, type, user, opportunity);

            EmailMessage emailMessage = new EmailMessage(from, new String[]{email}, subject, text);
            emailMessageSender.sendEmail(emailMessage);
        }
    }
}