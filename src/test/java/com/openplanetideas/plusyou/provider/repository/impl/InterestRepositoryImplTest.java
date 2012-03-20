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
import com.openplanetideas.plusyou.provider.repository.common.AbstractRepositoryTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class InterestRepositoryImplTest extends AbstractRepositoryTest {

    @Test
    public void findAllSortedByName() {
        List<Interest> actualInterests = interestRepository.findAllSortedByName();
        List<Interest> expectedInterests = new ArrayList<Interest>();
        expectedInterests.add(new Interest.Builder().withName("Art and Culture").withType(Interest.InterestCategory.ART).build());
        expectedInterests.add(new Interest.Builder().withName("Community Services").withType(Interest.InterestCategory.COMMUNITY_SERVICES).build());
        expectedInterests.add(new Interest.Builder().withName("Disability").withType(Interest.InterestCategory.DISABILITY).build());
        expectedInterests.add(new Interest.Builder().withName("Education and Literacy").withType(Interest.InterestCategory.EDUCATION).build());
        expectedInterests.add(new Interest.Builder().withName("Elderly").withType(Interest.InterestCategory.ELDERLY).build());
        expectedInterests.add(new Interest.Builder().withName("Environment").withType(Interest.InterestCategory.ENVIRONMENT).build());
        expectedInterests.add(new Interest.Builder().withName("Music").withType(Interest.InterestCategory.MUSIC).build());
        expectedInterests.add(new Interest.Builder().withName("Politics").withType(Interest.InterestCategory.POLITICS).build());
        expectedInterests.add(new Interest.Builder().withName("Sport and Outdoor Activities").withType(Interest.InterestCategory.SPORT).build());

        assertEquals(expectedInterests, actualInterests);
    }
}