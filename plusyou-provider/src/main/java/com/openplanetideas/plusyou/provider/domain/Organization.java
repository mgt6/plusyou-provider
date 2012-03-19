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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType
@Entity(name = "organizations")
public class Organization extends AbstractEntity {

    private static final long serialVersionUID = -7165956383429523210L;

    @XmlElement(required = true)
    @Column(nullable = false, unique = true)
    private String name;

    @XmlElement(required = true)
    @Embedded
    private Address address;

    private String telephone;
    private String email;
    private String website;

    protected Organization() {
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

        Organization organization = (Organization) obj;
        return new EqualsBuilder()
                .append(name, organization.getName())
                .append(address, organization.getAddress())
                .append(telephone, organization.getTelephone())
                .append(email, organization.getEmail())
                .append(website, organization.getWebsite())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(address)
                .append(telephone)
                .append(email)
                .append(website)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(name)
                .append(address)
                .append(telephone)
                .append(email)
                .append(website)
                .toString();
    }

    public Address getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getWebsite() {
        return website;
    }

    @XmlTransient
    public static class Builder implements org.apache.commons.lang3.builder.Builder<Organization> {

        private Organization organization = new Organization();

        public Builder withName(String name) {
            organization.name = name;
            return this;
        }

        public Builder withAddress(Address address) {
            organization.address = address;
            return this;
        }

        public Builder withTelephone(String telephone) {
            organization.telephone = telephone;
            return this;
        }

        public Builder withEmail(String email) {
            organization.email = email;
            return this;
        }

        public Builder withWebsite(String website) {
            organization.website = website;
            return this;
        }

        public Organization build() {
            return organization;
        }
    }
}