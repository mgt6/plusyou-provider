package com.openplanetideas.plusyou.provider.external.domain;

import com.openplanetideas.plusyou.provider.domain.common.GeoLocation;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 *
 * This class represents an organisation which has come from outside out our system.
 *
 * TODO the internal opportunity should be extended to include all of these fields in the database.
 *
 * @author Mark Taylor <marktaycon@googlemail.com>
 * @version 1.6
 * @since 30/03/12
 */
public class ExternalOrganisation {

    /**
     * The contact name of the charity representative
     */
    private String seekerFistName;

    private String seekerLastName;

    private String orgName;

    private GeoLocation location;

    private String url;

    private String accreditation;

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeekerFistName() {
        return seekerFistName;
    }

    public void setSeekerFistName(String seekerFistName) {
        this.seekerFistName = seekerFistName;
    }

    public String getSeekerLastName() {
        return seekerLastName;
    }

    public void setSeekerLastName(String seekerLastName) {
        this.seekerLastName = seekerLastName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setorgName(String orgName) {
        this.orgName = orgName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccreditation() {
        return accreditation;
    }

    public void setAccreditation(String accreditation) {
        this.accreditation = accreditation;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public void setLocation(GeoLocation location) {
        this.location = location;
    }

    public void addUrl(String s) {
        if(this.url == null || this.url.equals("")){
            this.url = s;
        } else {
            this.url += " ," + url;
        }
    }

    /**
     * Equality is based on the org name
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ExternalOrganisation))
        {
            return false;
        }
        if (this == obj)
        {
            return true;
        }
        final ExternalOrganisation otherObject = (ExternalOrganisation) obj;

        return new EqualsBuilder()
                .append(this.orgName, otherObject.orgName)
                .isEquals();

    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.orgName)
                .toHashCode();
    }
}
