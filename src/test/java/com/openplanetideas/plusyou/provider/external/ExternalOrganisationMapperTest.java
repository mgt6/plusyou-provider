package com.openplanetideas.plusyou.provider.external;

import com.openplanetideas.plusyou.provider.domain.Organization;
import com.openplanetideas.plusyou.provider.external.domain.ExternalOrganisation;
import com.openplanetideas.plusyou.provider.external.impl.ExternalOrganisationMapper;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author Mark Taylor <marktaycon@googlemail.com>
 * @version 1.6
 * @since 30/03/12
 */
public class ExternalOrganisationMapperTest {

    private ExternalOrganisationMapper externalOrganisationMapper;

    @Before
    public void setUp(){
        externalOrganisationMapper = new ExternalOrganisationMapper();

    }

    @Test
    public void testFromExternal(){
        ExternalOrganisation externalOrganisation = new ExternalOrganisation();

        externalOrganisation.setorgName("Test Org");
        externalOrganisation.setUrl("www.testorg.org");

        Organization organization = externalOrganisationMapper.fromExternal(externalOrganisation);

        assertEquals(externalOrganisation.getOrgName(), organization.getName());
        assertEquals(externalOrganisation.getUrl(), organization.getWebsite());

    }
}
