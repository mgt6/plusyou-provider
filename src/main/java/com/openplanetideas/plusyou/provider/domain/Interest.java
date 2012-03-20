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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType
@Entity(name = "interests")
public class Interest extends AbstractEntity {

    private static final long serialVersionUID = 6485845914160223017L;

    @XmlElement(required = true)
    @Column(nullable = false, unique = true)
    private String name;

    @XmlElement(required = true)
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private InterestCategory interestCategory;

    /**
     * InterstType is the counterpart of AwardType of Award. These two images correspond to each other.
     * This because we are lacking some kind of shared context due to timerestrictions.
     */
    @XmlType
    public enum InterestCategory {

        SPORT, ART, POLITICS, DISABILITY, ELDERLY, MUSIC, EDUCATION, COMMUNITY_SERVICES, ENVIRONMENT
    }

    protected Interest() {
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

        Interest interest = (Interest) obj;
        return new EqualsBuilder()
                .append(name, interest.getName())
                .append(interestCategory, interest.getInterestCategory())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(interestCategory)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(name)
                .append(interestCategory)
                .toString();
    }

    public InterestCategory getInterestCategory() {
        return interestCategory;
    }

    public String getName() {
        return name;
    }

    @XmlTransient
    public static class Builder implements org.apache.commons.lang3.builder.Builder<Interest> {

        private Interest interest = new Interest();

        public Builder withId(Long id) {
            interest.id = id;
            return this;
        }

        public Builder withName(String name) {
            interest.name = name;
            return this;
        }

        public Builder withType(InterestCategory category) {
            interest.interestCategory = category;
            return this;
        }

        public Interest build() {
            return interest;
        }
    }
}