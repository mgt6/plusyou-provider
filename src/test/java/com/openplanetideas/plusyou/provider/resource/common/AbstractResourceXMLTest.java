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

package com.openplanetideas.plusyou.provider.resource.common;

import com.openplanetideas.plusyou.provider.config.AppConfig;
import com.openplanetideas.plusyou.provider.config.EnvironmentTestType;
import com.openplanetideas.plusyou.provider.util.XmlUtils;
import com.sun.grizzly.http.SelectorThread;
import com.sun.grizzly.http.servlet.ServletAdapter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.container.grizzly.GrizzlyServerFactory;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.xml.sax.SAXException;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public abstract class AbstractResourceXMLTest {

    private static final String SERVER_URL = "http://localhost/";
    private static final Integer SERVER_PORT = 9999;

    private URI baseUri;
    private SelectorThread threadSelector;

    @Before
    public void setUp() throws IOException {
        ServletAdapter adapter = new ServletAdapter();
        adapter.addContextParameter(ContextLoaderListener.CONTEXT_CLASS_PARAM, AnnotationConfigWebApplicationContext.class.getName());
        adapter.addContextParameter(ContextLoaderListener.CONFIG_LOCATION_PARAM, AppConfig.class.getName());
        adapter.addContextParameter(StandardEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, EnvironmentTestType.TEST);
        adapter.addServletListener(ContextLoaderListener.class.getName());
        adapter.addServletListener(RequestContextListener.class.getName());
        adapter.setServletInstance(new SpringServlet());

        baseUri = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
        threadSelector = GrizzlyServerFactory.create(baseUri, adapter);
        XMLUnit.setIgnoreWhitespace(true);
    }

    @After
    public void tearDown() {
        threadSelector.stopEndpoint();
    }

    protected void assertXML(String expectedXml, String actualXml) throws IOException, SAXException {
        DetailedDiff diff = new DetailedDiff(new Diff(expectedXml, actualXml));
        XMLAssert.assertXMLIdentical(diff, Boolean.TRUE);
    }

    protected String executeGet(String path) {
        return executeGet(path, new String[]{});
    }

    protected String executeGet(String path, String[] elementsToIgnore) {
        Client client = Client.create();
        WebResource resource = client.resource(baseUri + path);

        String xml = resource.get(String.class);
        xml = XmlUtils.removeElements(xml, elementsToIgnore);
        return xml;
    }

    protected String getExpectedXml(String filename) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(filename);
        byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
        return new String(bytes);
    }
}