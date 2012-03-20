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

package com.openplanetideas.plusyou.provider.domain;

import com.openplanetideas.plusyou.provider.domain.common.AbstractEntity;
import com.openplanetideas.plusyou.provider.domain.common.Address;
import com.openplanetideas.plusyou.provider.domain.common.GeoLocation;
import com.openplanetideas.plusyou.provider.domain.jaxb.DateTimeXmlAdapter;
import com.openplanetideas.plusyou.provider.util.DateUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

@XmlRootElement
@XmlType
@Entity(name = "opportunities")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"externalId", "vendor_id"}))
public class Opportunity extends AbstractEntity implements Comparable<Opportunity> {

    private static final long serialVersionUID = 6415256933907583143L;

    @XmlElement(required = true)
    @Column(nullable = false)
    private Long externalId;

    @XmlElement(required = true)
    @Column(nullable = false)
    private String title;

    @XmlElement(required = true)
    @Column(nullable = false)
    private String description;

    @XmlElementWrapper(required = true)
    @XmlElement(name = "interest", required = true) //TODO rename to interestTypes
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "x_opportunities_interests",
            joinColumns = @JoinColumn(name = "opportunity_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"opportunity_id", "interest_id"})
    )
    private List<Interest> interests;

    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(DateTimeXmlAdapter.class)
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;

    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(DateTimeXmlAdapter.class)
    @Column(nullable = false)
    @Temporal(TemporalType.TIME)
    private Date beginTime;

    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(DateTimeXmlAdapter.class)
    @Column(nullable = false)
    @Temporal(TemporalType.TIME)
    private Date endTime;

    @XmlElement(required = true)
    @ManyToOne(optional = false)
    private Vendor vendor;

    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(DateTimeXmlAdapter.class)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    @XmlJavaTypeAdapter(DateTimeXmlAdapter.class)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @XmlElement(required = true)
    @Embedded
    private GeoLocation geoLocation;

    @XmlElement(required = true)
    @Embedded
    private Address address;

    @ManyToOne(optional = false)
    private Organization organization;

    @XmlElement(required = true)
    @Transient
    private Double distance;

    protected Opportunity() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (obj.getClass() != getClass()) {
            return false;
        }

        Opportunity opportunity = (Opportunity) obj;
        return new EqualsBuilder()
                .append(externalId, opportunity.getExternalId())
                .append(title, opportunity.getTitle())
                .append(description, opportunity.getDescription())
                .append(interests, opportunity.getInterests())
                .append(date, opportunity.getDate())
                .append(beginTime, opportunity.getBeginTime())
                .append(endTime, opportunity.getEndTime())
                .append(vendor, opportunity.getVendor())
                .append(geoLocation, opportunity.getGeoLocation())
                .append(address, opportunity.getAddress())
                .append(organization, opportunity.getOrganization())
                .append(distance, opportunity.getDistance())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(externalId)
                .append(title)
                .append(description)
                .append(interests)
                .append(date)
                .append(beginTime)
                .append(endTime)
                .append(vendor)
                .append(geoLocation)
                .append(address)
                .append(organization)
                .append(distance)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(externalId)
                .append(title)
                .append(description)
                .append(interests)
                .append(date)
                .append(beginTime)
                .append(endTime)
                .append(vendor)
                .append(geoLocation)
                .append(address)
                .append(organization)
                .append(distance)
                .toString();
    }

    @Override
    public int compareTo(Opportunity opportunity) {
        return distance.compareTo(opportunity.getDistance());
    }

    public Address getAddress() {
        return address;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public Date getCreated() {
        return created;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Long getExternalId() {
        return externalId;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public Organization getOrganization() {
        return organization;
    }

    public String getTitle() {
        return title;
    }

    public Date getUpdated() {
        return updated;
    }

    public Vendor getVendor() {
        return vendor;
    }

    @XmlTransient
    public static class Builder implements org.apache.commons.lang3.builder.Builder<Opportunity> {

        private Opportunity opportunity = new Opportunity();

        public Builder withId(Long id) {
            opportunity.id = id;
            return this;
        }

        public Builder withExternalId(Long externalId) {
            opportunity.externalId = externalId;
            return this;
        }

        public Builder withTitle(String title) {
            opportunity.title = title;
            return this;
        }

        public Builder withDescription(String description) {
            opportunity.description = description;
            return this;
        }

        public Builder withInterests(List<Interest> interests) {
            opportunity.interests = interests;
            return this;
        }

        public Builder withDate(String date) {
            opportunity.date = DateUtils.parseDate(date);
            return this;
        }

        public Builder withBeginTime(String beginTime) {
            opportunity.beginTime = DateUtils.parseTime(beginTime);
            return this;
        }

        public Builder withEndTime(String endTime) {
            opportunity.endTime = DateUtils.parseTime(endTime);
            return this;
        }

        public Builder withVendor(Vendor vendor) {
            opportunity.vendor = vendor;
            return this;
        }

        public Builder withGeoLocation(GeoLocation geoLocation) {
            opportunity.geoLocation = geoLocation;
            return this;
        }

        public Builder withAddress(Address address) {
            opportunity.address = address;
            return this;
        }

        public Builder withOrganization(Organization organization) {
            opportunity.organization = organization;
            return this;
        }

        public Builder withDistance(Double distance) {
            opportunity.distance = distance;
            return this;
        }

        public Opportunity build() {
            return opportunity;
        }
    }
}