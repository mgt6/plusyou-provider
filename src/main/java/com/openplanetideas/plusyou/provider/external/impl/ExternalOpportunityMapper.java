package com.openplanetideas.plusyou.provider.external.impl;

import com.openplanetideas.plusyou.provider.domain.Opportunity;
import com.openplanetideas.plusyou.provider.domain.Organization;
import com.openplanetideas.plusyou.provider.domain.common.GeoLocation;
import com.openplanetideas.plusyou.provider.domain.common.GeoLocation_;
import com.openplanetideas.plusyou.provider.external.ExternalMapper;
import com.openplanetideas.plusyou.provider.external.domain.ExternalOpportunity;
import com.openplanetideas.plusyou.provider.repository.OrganizationRepository;
import com.openplanetideas.plusyou.provider.repository.impl.OrganizationRepositoryImpl;

/**
 * @author Mark Taylor <marktaycon@googlemail.com>
 * @version 1.6
 * @since 30/03/12
 */
public class ExternalOpportunityMapper implements ExternalMapper<ExternalOpportunity, Opportunity> {

    private OrganizationRepository organizationRepository = new OrganizationRepositoryImpl();

    @Override
    public Opportunity fromExternal(ExternalOpportunity externalOpportunity) {
        Organization organization = organizationRepository.findByName(externalOpportunity.getExternalOrganisation().getOrgName());

        Opportunity.Builder builder = new Opportunity.Builder();
        builder.withDate(externalOpportunity.getDeadline());
        builder.withGeoLocation(new GeoLocation.Builder().withLatitude(22d).withLongitude(22d).build());
        builder.withOrganization(organization);
        builder.withTitle(externalOpportunity.getTitle());
        return builder.build();
    }
}
