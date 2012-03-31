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

import com.openplanetideas.plusyou.provider.domain.Organization;
import com.openplanetideas.plusyou.provider.domain.Organization_;
import com.openplanetideas.plusyou.provider.repository.OrganizationRepository;
import com.openplanetideas.plusyou.provider.repository.common.AbstractRepository;
import org.hibernate.ejb.EntityManagerImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;

@Repository
public class OrganizationRepositoryImpl extends AbstractRepository<Organization> implements OrganizationRepository {

    @Override
    public Organization findByName(String name) {
        Predicate correctOrganization = builder.equal(builder.lower(root.get(Organization_.name)), name.toLowerCase());

        query.where(correctOrganization);
        return entityManager.createQuery(query).getSingleResult();
    }

    public void save(Organization organization){
        entityManager.getTransaction().begin();
        entityManager.persist(organization);
        entityManager.getTransaction().commit();
    }
}