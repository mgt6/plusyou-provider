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

import com.openplanetideas.plusyou.provider.domain.Interest;
import com.openplanetideas.plusyou.provider.resource.common.AbstractResourceTest;
import org.junit.Test;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InterestResourceTest extends AbstractResourceTest {

    @Test
    public void findAllSortedByNameWhenInterestsAreFound() {
        List<Interest> expectedInterests = new ArrayList<Interest>(2);
        expectedInterests.add(new Interest.Builder().withName("Art and Culture").withType(Interest.InterestCategory.ART).build());
        expectedInterests.add(new Interest.Builder().withName("Disability").withType(Interest.InterestCategory.DISABILITY).build());
        when(interestRepository.findAllSortedByName()).thenReturn(expectedInterests);
        Response response = interestResource.findAllSortedByName();
        verify(interestRepository).findAllSortedByName();
        List<Interest> actualInterests = ((GenericEntity<List<Interest>>)response.getEntity()).getEntity();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(expectedInterests, actualInterests);
    }

    @Test
    public void findAllSortedByNameWhenNoInterestsAreFound() {
        when(interestRepository.findAllSortedByName()).thenReturn(Collections.<Interest>emptyList());
        Response response = interestResource.findAllSortedByName();
        verify(interestRepository).findAllSortedByName();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }
}