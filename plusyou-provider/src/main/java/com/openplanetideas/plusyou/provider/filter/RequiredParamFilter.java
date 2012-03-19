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

package com.openplanetideas.plusyou.provider.filter;

import com.openplanetideas.plusyou.provider.exception.MissingParamValueException;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import java.util.ArrayList;
import java.util.List;

public class RequiredParamFilter implements ResourceFilter, ContainerRequestFilter {

    private static final String MISSING_PARAM_SEPARATOR = ", ";

    private List<String> requiredParams;

    public RequiredParamFilter(List<String> requiredParams) {
        this.requiredParams = requiredParams;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        if (requiredParams.size() > 0) {
            List<String> missingMatrixParams = getMissingMatrixParams(request);

            if (missingMatrixParams.size() > 0) {
                String missingParamValue = StringUtils.join(missingMatrixParams.toArray(), MISSING_PARAM_SEPARATOR);
                throw new MissingParamValueException(missingParamValue);
            }
        }
        return request;
    }

    @Override
    public ContainerRequestFilter getRequestFilter() {
        return this;
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return null;
    }

    private List<String> getMissingMatrixParams(ContainerRequest request) {
        List<PathSegment> paths = request.getPathSegments();
        PathSegment path = paths.get(paths.size() - 1);
        MultivaluedMap<String, String> matrixParams = path.getMatrixParameters();

        List<String> missingMatrixParams = new ArrayList<String>();
        for (String requiredParam : requiredParams) {
            String matrixParam = matrixParams.getFirst(requiredParam);
            if (StringUtils.isBlank(matrixParam)) {
                missingMatrixParams.add(requiredParam);
            }
        }
        return missingMatrixParams;
    }
}