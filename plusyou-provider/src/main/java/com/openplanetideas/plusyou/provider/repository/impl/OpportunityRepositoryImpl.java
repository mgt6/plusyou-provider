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

import com.openplanetideas.plusyou.provider.domain.Interest;
import com.openplanetideas.plusyou.provider.domain.Opportunity;
import com.openplanetideas.plusyou.provider.domain.Opportunity_;
import com.openplanetideas.plusyou.provider.domain.Vendor;
import com.openplanetideas.plusyou.provider.repository.OpportunityRepository;
import com.openplanetideas.plusyou.provider.repository.common.AbstractRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.List;

@Repository
public class OpportunityRepositoryImpl extends AbstractRepository<Opportunity> implements OpportunityRepository {

    @Override
    public Opportunity findByExternalIdAndVendor(Long externalId, Vendor vendor) {
        Predicate correctExternalId = builder.equal(root.get(Opportunity_.externalId), externalId);
        Predicate correctVendor = builder.equal(root.get(Opportunity_.vendor), vendor);
        
        query.where(builder.and(correctExternalId, correctVendor));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public Opportunity findById(Long id) {
        return entityManager.find(Opportunity.class, id);
    }

    @Override
    public List<Opportunity> findByIds(List<Long> opportunityIds) {
        Predicate inIds = root.in(opportunityIds);
        query.where(inIds).distinct(true);

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Opportunity> findForDate(Date beginDate, Date endDate, Vendor vendor) {
        Predicate betweenDate = builder.between(root.get(Opportunity_.date), beginDate, endDate);
        Predicate correctVendor = builder.equal(root.get(Opportunity_.vendor), vendor);

        query.where(builder.and(betweenDate, correctVendor));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Opportunity> findForInterestAndDate(Interest interest, Date beginDate, Date endDate, Vendor vendor) {
        Predicate correctInterest = builder.isMember(interest, root.get(Opportunity_.interests));
        Predicate betweenDate = builder.between(root.get(Opportunity_.date), beginDate, endDate);
        Predicate correctVendor = builder.equal(root.get(Opportunity_.vendor), vendor);

        query.where(builder.and(correctInterest, betweenDate, correctVendor));
        return entityManager.createQuery(query).getResultList();
    }
}