/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.transform.topology;

import com.foursoft.harness.kbl.v25.KblAliasIdentification;
import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl.v25.KblSegmentForm;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.*;

import java.util.function.Consumer;

public class TopologySegmentTransformer implements Transformer<KblSegment, VecTopologySegment> {
    @Override
    public TransformationResult<VecTopologySegment> transform(final TransformationContext context,
                                                              final KblSegment source) {
        final VecTopologySegment topologySegment = new VecTopologySegment();
        topologySegment.setIdentification(source.getId());
        if (source.getForm() != null) {
            topologySegment.setForm(source.getForm() == KblSegmentForm.CIRCULAR ? "Circular" : "NonCircular");
        }

        return TransformationResult.from(topologySegment)
                .downstreamTransformation(KblAliasIdentification.class, VecAliasIdentification.class,
                                          source::getAliasIds, topologySegment::getAliasIds)
                .downstreamTransformation(KblNumericalValue.class, VecNumericalValue.class,
                                          Query.of(source.getVirtualLength()),
                                          appendLengthInformation(topologySegment, "Designed"))
                .downstreamTransformation(KblNumericalValue.class, VecNumericalValue.class,
                                          Query.of(source.getPhysicalLength()),
                                          appendLengthInformation(topologySegment, "Adapted"))
                .withLinker(Query.of(source::getStartNode), VecTopologyNode.class, topologySegment::setStartNode)
                .withLinker(Query.of(source::getEndNode), VecTopologyNode.class, topologySegment::setEndNode)
                .build();
    }

    private Consumer<VecNumericalValue> appendLengthInformation(final VecTopologySegment topologySegment,
                                                                final String classification) {
        return (final VecNumericalValue numericalValue) -> {
            final VecSegmentLength segmentLength = new VecSegmentLength();
            segmentLength.setClassification(classification);
            segmentLength.setLength(numericalValue);
            topologySegment.getLengthInformations().add(segmentLength);
        };
    }

}
