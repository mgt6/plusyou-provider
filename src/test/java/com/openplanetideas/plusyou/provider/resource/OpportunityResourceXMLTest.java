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

package com.openplanetideas.plusyou.provider.resource;

import com.openplanetideas.plusyou.provider.resource.common.AbstractResourceXMLTest;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;


//TODO Test with JSON
public class OpportunityResourceXMLTest extends AbstractResourceXMLTest {

    @Test
    public void opportunitiesByIds() throws IOException, SAXException {
        String path = "opportunities/ids?id=1&id=2&id=3&";
        String expectedXml = getExpectedXml("opportunitiesByIds.xml");
        String actualXml = executeGet(path, new String[]{"created", "updated"});
        assertXML(expectedXml, actualXml);
    }

    @Test
    public void opportunitiesWithInterestCriteria() throws IOException, SAXException {
        String path = "opportunities;interest=2;beginDate=20111001;endDate=20111020;distance=3;latitude=51.37312;longitude=-0.144796;vendor=1";
        String expectedXml = getExpectedXml("opportunitiesWithInterestCriteria.xml");
        String actualXml = executeGet(path, new String[]{"created", "updated"});

        assertXML(expectedXml, actualXml);
    }

    @Test
    public void opportunitiesWithoutInterestCriteria() throws IOException, SAXException {
        String path = "opportunities;beginDate=20111001;endDate=20111020;distance=3;latitude=51.37312;longitude=-0.144796;vendor=1";
        String expectedXml = getExpectedXml("opportunitiesWithoutInterestCriteria.xml");
        String actualXml = executeGet(path, new String[]{"created", "updated"});

        assertXML(expectedXml, actualXml);
    }

    @Test
    public void opportunity() throws IOException, SAXException {
        String path = "opportunities/2";
        String expectedXml = getExpectedXml("opportunity.xml");
        String actualXml = executeGet(path, new String[]{"created", "updated"});

        assertXML(expectedXml, actualXml);
    }
}