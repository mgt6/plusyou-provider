package com.openplanetideas.plusyou.provider.external.impl;

import com.openplanetideas.plusyou.provider.domain.Organization;
import com.openplanetideas.plusyou.provider.external.ExternalMapper;
import com.openplanetideas.plusyou.provider.external.domain.ExternalOrganisation;

/**
 * @author Mark Taylor <marktaycon@googlemail.com>
 * @version 1.6
 * @since 30/03/12
 */
public class ExternalOrganisationMapper implements ExternalMapper<ExternalOrganisation, Organization> {

    @Override
    public Organization fromExternal(ExternalOrganisation externalOrganization) {
        Organization.Builder builder = new Organization.Builder();
        builder.withName(externalOrganization.getOrgName());
        builder.withWebsite(externalOrganization.getUrl());
        return builder.build();
    }
}
