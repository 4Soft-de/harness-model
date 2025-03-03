package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.KblAliasIdentification;
import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl.v25.KblSegmentForm;
import com.foursoft.harness.kbl2vec.core.*;
import com.foursoft.harness.vec.v2x.*;

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
                .withFragment(segmentLength(source))
                .downstreamTransformation(KblAliasIdentification.class, VecAliasIdentification.class,
                                          source::getAliasIds, topologySegment::getAliasIds)
                .withLinker(Query.of(source::getStartNode), VecTopologyNode.class, topologySegment::setStartNode)
                .withLinker(Query.of(source::getEndNode), VecTopologyNode.class, topologySegment::setEndNode)
                .build();
    }

    private TransformationFragment<VecTopologySegment> segmentLength(final KblSegment source) {
        return (topologySegment, builder) -> {
            final VecSegmentLength virtualLength = createLength(source.getVirtualLength(), "Designed");
            final VecSegmentLength physicalLength = createLength(source.getPhysicalLength(), "Adapted");

            if (virtualLength != null) {
                topologySegment.getLengthInformations().add(virtualLength);
                builder.downstreamTransformation(KblNumericalValue.class, VecNumericalValue.class,
                                                 Query.of(source.getVirtualLength()), virtualLength::setLength);
            }
            if (physicalLength != null) {
                topologySegment.getLengthInformations().add(physicalLength);
                builder.downstreamTransformation(KblNumericalValue.class, VecNumericalValue.class,
                                                 Query.of(source.getPhysicalLength()), physicalLength::setLength);
            }
        };
    }

    private VecSegmentLength createLength(final KblNumericalValue numericalValue, final String classification) {
        if (numericalValue == null) {
            return null;
        }
        final VecSegmentLength length = new VecSegmentLength();
        length.setClassification(classification);
        return length;
    }
}
