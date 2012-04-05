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

    private static String url = "http://volunteerfeed.tnkd.net/api/feed"; //The URL


   /**
       * Imports data in JSON format from the URL, This data is then converted into External objects which are mapped to internal domain object
       * which can be saved into the database and used by the app.
       */
    public void importResource(){
        Set<ExternalOpportunity> externalOpportunitySet = new HashSet<ExternalOpportunity>();
        Set<ExternalOrganisation> externalOrganisations = new HashSet<ExternalOrganisation>();
        String data = getData();
        createExternalObjects(externalOpportunitySet, externalOrganisations, data);
        mapExternalObjects(externalOpportunitySet, externalOrganisations);
    }

  /**
     * Gets the data from the URL.
     *
     * @return The response from the URL as a string.
     */
    private String getData(){
        Client client = Client.create();
        WebResource resource = client.resource(url);
        String object = resource.get(String.class);
        return object;
    }

  /**
     * Maps external Objects to internal ones and attempts to save them.
     *
     * @param externalOpportunitySet  The Set of ExternalOpportunity objects to save.
     * @param externalOrganisations The Set of ExternalOrganisation Objects to save.
     */
    private void mapExternalObjects(Set<ExternalOpportunity> externalOpportunitySet, Set<ExternalOrganisation> externalOrganisations) {
        mapOrganisations(externalOrganisations);
        mapOpportunities(externalOpportunitySet);
    }

  /**
     *  Loops the data and creates an External Domain object for every object in the JSON then adds them to a collection.
     *
     * @param externalOpportunitySet A collection of Opportunities to add the external opportunity to.
     * @param externalOrganisations A collection of Organisation to add the external organisation to.
     * @param data the data to get the objects from.
     */
    private void createExternalObjects(Set<ExternalOpportunity> externalOpportunitySet, Set<ExternalOrganisation> externalOrganisations, String data) {
        Gson gson = new Gson();
        ArrayList<LinkedHashMap<String, Object>> obj = (ArrayList<LinkedHashMap<String, Object>>) gson.fromJson(data, Object.class);

        for (LinkedHashMap<String, Object> entry : obj) {
            ExternalOpportunity externalOpportunity = mapExternalOpportunity(entry);
            ExternalOrganisation externalOrganisation = mapExternalOrganisation((LinkedHashMap<String, Object>) entry.get("organisation"));
            externalOrganisations.add(externalOrganisation);
            externalOpportunity.setExternalOrganisation(externalOrganisation);
            externalOpportunitySet.add(externalOpportunity);
        }
    }

  /**
     * Maps A {@link LinkedHashMap} of String to objects, as produced by the GSON library to an {@link ExternalOpportunity} object.
     *
     * @param entry The entry from GSON
     */
    private ExternalOpportunity mapExternalOpportunity(LinkedHashMap<String, Object> entry) {
        ExternalOpportunity externalOpportunity = new ExternalOpportunity();
        addUrlTpOpportunity(entry, externalOpportunity);
        addTitleToOpportunity(entry, externalOpportunity);
        addCategoryToOpportunity(entry, externalOpportunity);
        addDescriptionToOpportunity(entry, externalOpportunity);
        addAvailabilityToOpportunity(entry, externalOpportunity);
        addGeoLocationToOpportunity(entry, externalOpportunity);
        addSkillsNeededToOpportunity(entry, externalOpportunity);
        return externalOpportunity;
    }

  /**
     * Adds a skill to the opportunity.
     *
     * @param entry The data containing the skill information..
     * @param externalOpportunity the opportunity to add the skill to.
     */
    private void addSkillsNeededToOpportunity(LinkedHashMap<String, Object> entry, ExternalOpportunity externalOpportunity) {
        LinkedHashMap<String, String> skills = (LinkedHashMap<String, String>) entry.get("skillsNeeded");
        externalOpportunity.setSkill(skills.get("plain"));
    }

  /**
     * Adds a {@link GeoLocation} to the opportunity
     *
     * @param entry The data containing the GeoLocation information.
     * @param externalOpportunity the opportunity to add the information to.
     */
    private void addGeoLocationToOpportunity(LinkedHashMap<String, Object> entry, ExternalOpportunity externalOpportunity) {
        LinkedHashMap<String, Object> location = (LinkedHashMap<String, Object>) entry.get("location");
        Double lat = (Double) location.get("lat");
        Double lon = (Double) location.get("lon");
        externalOpportunity.setGeoLocation(new GeoLocation.Builder().withLatitude(lat).withLongitude(lon).build());
    }

  /**
     * Adds the availability of the opportunity.
     *
     * @param entry The data containing the opportunity information.
     * @param externalOpportunity The opportunity information to.
     */
    private void addAvailabilityToOpportunity(LinkedHashMap<String, Object> entry, ExternalOpportunity externalOpportunity) {
        LinkedHashMap<String, String> deadline = (LinkedHashMap<String, String>) entry.get("availability");
        externalOpportunity.setDeadline(deadline.get("start"));
    }

  /**
     * Adds a description to the opportunity.
     *
     * @param entry  The data containing the description information.
     * @param externalOpportunity The opportunity to add the information to.
     */
    private void addDescriptionToOpportunity(LinkedHashMap<String, Object> entry, ExternalOpportunity externalOpportunity) {
        LinkedHashMap<String, String> description = (LinkedHashMap<String, String>) entry.get("description");
        externalOpportunity.setDescription(description.get("plain"));
    }

  /**
     * Adds a category to the opportunity.
     *
     * @param entry  The data containing the category information.
     * @param externalOpportunity The opportunity to add the information to.
     */
    private void addCategoryToOpportunity(LinkedHashMap<String, Object> entry, ExternalOpportunity externalOpportunity) {
        ArrayList<String> categories = (ArrayList<String>) entry.get("categories");
        if (categories != null) {
            for (String s : categories) {
                externalOpportunity.addCategory(s);
            }
        }
    }

  /**
     * Adds a title to the opportunity.
     *
     * @param entry  The data containing the title information.
     * @param externalOpportunity The opportunity to add the information to.
     */
    private void addTitleToOpportunity(LinkedHashMap<String, Object> entry, ExternalOpportunity externalOpportunity) {
        externalOpportunity.setTitle((String) entry.get("title"));
    }

  /**
     * Adds a url to the opportunity.
     *
     * @param entry  The data containing the url information.
     * @param externalOpportunity The opportunity to add the information to.
     */
    private void addUrlTpOpportunity(LinkedHashMap<String, Object> entry, ExternalOpportunity externalOpportunity) {
        LinkedHashMap<String, String> urls = (LinkedHashMap<String, String>) entry.get("urls");
        for (String s : urls.values()) {
            if (s != null) {
                externalOpportunity.addUrl(s);
            }
        }
    }

  /**
     * Maps each of the External Opportunities we have found to {@link Opportunity} objects then attempts to save them locally
     *
     * @param externalOpportunitySet Set of ExternalOpportunity objects from the JSON
     */
    private void mapOpportunities(Set<ExternalOpportunity> externalOpportunitySet) {
        ExternalOpportunityMapper mapper = new ExternalOpportunityMapper();
        OpportunityRepositoryImpl repository = new OpportunityRepositoryImpl();
        for (ExternalOpportunity externalOpportunity : externalOpportunitySet) {
            Opportunity opportunity = mapper.fromExternal(externalOpportunity);
            /*repository.save(opportunity);
                    System.out.println(opportunity.getTitle() + " Saved");*/
        }
    }

  /**
     * Maps each of the External Organisation we have found to {@link Organization} objects then attempts to save them locally
     *
     * @param externalOrganisations Set of ExternalOrganisation objects from the JSON
     */
    private void mapOrganisations(Set<ExternalOrganisation> externalOrganisations) {
        ExternalOrganisationMapper mapper = new ExternalOrganisationMapper();
        OrganizationRepositoryImpl repository = new OrganizationRepositoryImpl();
        for (ExternalOrganisation externalOrganisation : externalOrganisations) {
            Organization organization = mapper.fromExternal(externalOrganisation);
            /*        repository.save(organization);
                    System.out.println(organization.getName() + " Saved");*/
        }
        System.out.println();
    }

    /**
     * Maps A {@link LinkedHashMap} of String to objects, as produced by the GSON library to an {@link ExternalOrganisation} object.
     *
     * @param objectMap The entry from GSON
     */
    private ExternalOrganisation mapExternalOrganisation(LinkedHashMap<String, Object> objectMap) {
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
            if (s != null) {
                externalOrganisation.addUrl(s);
            }
        }
        return externalOrganisation;
    }
}
