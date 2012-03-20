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

package com.openplanetideas.plusyou.provider.repository.impl;

import com.openplanetideas.plusyou.provider.domain.Country;
import com.openplanetideas.plusyou.provider.domain.Organization;
import com.openplanetideas.plusyou.provider.domain.common.Address;
import com.openplanetideas.plusyou.provider.repository.common.AbstractRepositoryTest;
import org.junit.Test;

import javax.persistence.NoResultException;

import static org.junit.Assert.assertEquals;

public class OrganizationRepositoryImplTest extends AbstractRepositoryTest {

    @Test
    public void findByName_FOUND_LOWERCASE() {
        Address address = new Address.Builder()
                .withStreet("31 West Street")
                .withPostcode("SM1 1SJ")
                .withCity("Sutton")
                .withCountry(new Country.Builder().withName("United Kingdom").build())
                .build();

        Organization expectedOrganization = new Organization.Builder()
                .withName("Volunteer Centre Sutton")
                .withAddress(address)
                .withTelephone("0208 661 5900")
                .withEmail("vcsutton@vcsutton.org.uk")
                .build();
        Organization actualOrganization = organizationRepository.findByName("volunteer centre sutton");
        
        assertEquals(expectedOrganization, actualOrganization);
    }

    @Test
    public void findByName_FOUND_UPPERCASE() {
        Address address = new Address.Builder()
                .withStreet("31 West Street")
                .withPostcode("SM1 1SJ")
                .withCity("Sutton")
                .withCountry(new Country.Builder().withName("United Kingdom").build())
                .build();

        Organization expectedOrganization = new Organization.Builder()
                .withName("Volunteer Centre Sutton")
                .withAddress(address)
                .withTelephone("0208 661 5900")
                .withEmail("vcsutton@vcsutton.org.uk")
                .build();
        Organization actualOrganization = organizationRepository.findByName("VOLUNTEER CENTRE SUTTON");

        assertEquals(expectedOrganization, actualOrganization);
    }

    @Test(expected = NoResultException.class)
    public void findByName_NOT_FOUND() {
        organizationRepository.findByName("Volunteer Centre Sutton 2");
    }
}