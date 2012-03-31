package com.openplanetideas.plusyou.provider.resource;

import com.openplanetideas.plusyou.provider.domain.Opportunity;
import com.openplanetideas.plusyou.provider.domain.Organization;
import com.openplanetideas.plusyou.provider.domain.common.GeoLocation;
import com.openplanetideas.plusyou.provider.external.domain.ExternalOpportunity;
import com.openplanetideas.plusyou.provider.external.domain.ExternalOrganisation;
import com.openplanetideas.plusyou.provider.external.impl.ExternalOpportunityMapper;
import com.openplanetideas.plusyou.provider.external.impl.ExternalOrganisationMapper;
import com.openplanetideas.plusyou.provider.repository.impl.OpportunityRepositoryImpl;
import com.openplanetideas.plusyou.provider.repository.impl.OrganizationRepositoryImpl;
import com.openplanetideas.plusyou.provider.util.OpportunityUtils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.google.gson.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * @author Mark Taylor <marktaycon@googlemail.com>
 * @version 1.6
 * @since 31/03/12
 */

public class ImportResource {

    String url = "http://volunteerfeed.tnkd.net/api/feed";

    @Test
    public void getData(){
        Client client = Client.create();
        WebResource resource = client.resource(url);
        String object = resource.get(String.class);
        //Remove the first and last char
        String jsonString = object.substring(1, (object.length() -1));
        Gson gson = new Gson();

        Set<ExternalOpportunity> externalOpportunitySet = new HashSet<ExternalOpportunity>();
        Set<ExternalOrganisation> externalOrganisations = new HashSet<ExternalOrganisation>();

        ArrayList<LinkedHashMap<String, Object>> obj = (ArrayList<LinkedHashMap<String, Object>>) gson.fromJson(object, Object.class);
        for (LinkedHashMap<String, Object> entry : obj) {
            ExternalOpportunity externalOpportunity = new ExternalOpportunity();


            LinkedHashMap<String, String> urls = (LinkedHashMap<String, String>) entry.get("urls");

            for (String s : urls.values()) {
                if(s != null){
                    externalOpportunity.addUrl(s);
                }
            }

            externalOpportunity.setTitle((String) entry.get("title"));
            ArrayList<String> categories = (ArrayList<String>) entry.get("categories");
            if(categories != null){
                for(String s: categories){
                    externalOpportunity.addCategory(s);
                }
            }


            LinkedHashMap<String, String> description = (LinkedHashMap<String, String>) entry.get("description");
            externalOpportunity.setDescription(description.get("plain"));
            LinkedHashMap<String, String> deadline = (LinkedHashMap<String, String>) entry.get("availability");
            externalOpportunity.setDeadline(deadline.get("start"));

            LinkedHashMap<String, Object> location = (LinkedHashMap<String, Object>) entry.get("location");
            Double lat = (Double) location.get("lat");
            Double lon = (Double) location.get("lon");

            externalOpportunity.setGeoLocation(new GeoLocation.Builder().withLatitude(lat).withLongitude(lon).build());

            LinkedHashMap<String, String> skills = (LinkedHashMap<String, String>) entry.get("skillsNeeded");

            externalOpportunity.setSkill(skills.get("plain"));
            ExternalOrganisation externalOrganisation = createExternalOrganisation((LinkedHashMap<String, Object>) entry.get("organisation"));
            externalOrganisations.add(externalOrganisation);
            externalOpportunity.setExternalOrganisation(externalOrganisation);
            externalOpportunitySet.add(externalOpportunity);
        }


        mapOrganisations(externalOrganisations);
        mapOpportunities(externalOpportunitySet);

    }

    private void mapOpportunities(Set<ExternalOpportunity> externalOpportunitySet) {
        ExternalOpportunityMapper mapper = new ExternalOpportunityMapper();
        OpportunityRepositoryImpl repository = new OpportunityRepositoryImpl();
        for (ExternalOpportunity externalOpportunity : externalOpportunitySet) {
            Opportunity opportunity = mapper.fromExternal(externalOpportunity);
            repository.save(opportunity);
            System.out.println(opportunity.getTitle() + " Saved");
        }
    }

    private void mapOrganisations(Set<ExternalOrganisation> externalOrganisations) {
        ExternalOrganisationMapper mapper = new ExternalOrganisationMapper();
        OrganizationRepositoryImpl repository = new OrganizationRepositoryImpl();
        for (ExternalOrganisation externalOrganisation : externalOrganisations) {
            Organization organization = mapper.fromExternal(externalOrganisation);
//            repository.save(organization);
 //           System.out.println(organization.getName() + " Saved");
        }
        System.out.println();
    }

    private ExternalOrganisation createExternalOrganisation(LinkedHashMap<String, Object> objectMap){
        ExternalOrganisation externalOrganisation = new ExternalOrganisation();

        externalOrganisation.setorgName((String) objectMap.get("name"));
        externalOrganisation.setSeekerFistName((String) objectMap.get("firstname"));
        externalOrganisation.setSeekerLastName((String) objectMap.get("lastname"));
        externalOrganisation.setAccreditation((String) objectMap.get("accreditation"));

        LinkedHashMap<String, String> description = (LinkedHashMap<String, String>) objectMap.get("description");

        externalOrganisation.setDescription(description.get("plain"));

        LinkedHashMap<String, Object> location = (LinkedHashMap<String, Object>) objectMap.get("location");

        Double lat = (Double) location.get("lat");
        Double lon = (Double) location.get("lon");

        externalOrganisation.setLocation(new GeoLocation.Builder().withLatitude(lat).withLongitude(lon).build());

        LinkedHashMap<String, String> urls = (LinkedHashMap<String, String>) objectMap.get("urls");

        for (String s : urls.values()) {
            if(s != null){
                externalOrganisation.addUrl(s);
            }
        }


        return externalOrganisation;
    }

}
