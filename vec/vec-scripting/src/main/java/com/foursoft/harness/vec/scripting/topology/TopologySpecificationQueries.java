/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2025 4Soft GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =========================LICENSE_END==================================
 */
package com.foursoft.harness.vec.scripting.topology;

import com.foursoft.harness.vec.scripting.DefaultValues;
import com.foursoft.harness.vec.scripting.VecScriptingException;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.v2x.VecTopologyNode;
import com.foursoft.harness.vec.v2x.VecTopologySegment;
import com.foursoft.harness.vec.v2x.VecTopologySpecification;

import static com.foursoft.harness.vec.common.util.StreamUtils.findOne;

public class TopologySpecificationQueries {

    private final VecTopologySpecification topologySpecification;

    public TopologySpecificationQueries() {
        this.topologySpecification = null;
    }

    public TopologySpecificationQueries(final VecTopologySpecification topologySpecification) {
        this.topologySpecification = topologySpecification;
    }

    /**
     * Queries for the {@link VecTopologySpecification} of the given document, e.g. to instantiate the topology of a
     * composite part in a using context.
     */
    public static TopologySpecificationQueries forDocument(final VecSession session, final String documentNumber) {
        return new TopologySpecificationQueries(session.findDocument(documentNumber)
                                                        .getSpecificationWith(VecTopologySpecification.class,
                                                                              DefaultValues.TOPO_SPEC_IDENTIFICATION)
                                                        .orElseThrow(() -> new VecScriptingException(
                                                                "No TopologySpecification found in document '" +
                                                                        documentNumber + "'.")));
    }

    private VecTopologySpecification topo() {
        if (topologySpecification == null) {
            throw new VecScriptingException("No TopologySpecification set for context.");
        }
        return topologySpecification;
    }

    public VecTopologyNode findTopologyNode(final String identification) {
        return topo()
                .getTopologyNodes()
                .stream()
                .filter(n -> identification.equals(n.getIdentification()))
                .collect(findOne());
    }

    public VecTopologySegment findTopologySegment(final String identification) {
        return topo()
                .getTopologySegments()
                .stream()
                .filter(n -> identification.equals(n.getIdentification()))
                .collect(findOne());
    }
}
