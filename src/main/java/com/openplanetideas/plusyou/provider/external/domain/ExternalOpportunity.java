package com.openplanetideas.plusyou.provider.external.domain;

import com.openplanetideas.plusyou.provider.domain.common.GeoLocation;

/**
 * This class represents an opportunity which has come from outside out our system.
 *
 * TODO the internal opportunity should be extended to include all of these fields in the database.
 *
 * @author Mark Taylor <marktaycon@googlemail.com>
 * @version 1.6
 * @since 30/03/12
 */
public class ExternalOpportunity {

    private String title;

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    private String URL;

    private String description;

    private String cause;

    private String Skill;

    private String deadline;

    private String numberOfAnswers;

    private String numberOfSolvers;

    private GeoLocation geoLocation;

    private ExternalOrganisation externalOrganisation;

    public ExternalOrganisation getExternalOrganisation() {
        return externalOrganisation;
    }

    public void setExternalOrganisation(ExternalOrganisation externalOrganisation) {
        this.externalOrganisation = externalOrganisation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void addUrl(String URL){
        if(this.URL == null || this.URL.equals("")){
            this.URL = URL;
        } else {
            this.URL += " ," + URL;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getSkill() {
        return Skill;
    }

    public void setSkill(String skill) {
        Skill = skill;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getNumberOfAnswers() {
        return numberOfAnswers;
    }

    public void setNumberOfAnswers(String numberOfAnswers) {
        this.numberOfAnswers = numberOfAnswers;
    }

    public String getNumberOfSolvers() {
        return numberOfSolvers;
    }

    public void setNumberOfSolvers(String numberOfSolvers) {
        this.numberOfSolvers = numberOfSolvers;
    }

    public void addCategory(String cause) {
        if(cause == null || cause.equals("")){
            this.cause = cause;
        } else {
            this.cause += " ," + cause;
        }
    }
}
