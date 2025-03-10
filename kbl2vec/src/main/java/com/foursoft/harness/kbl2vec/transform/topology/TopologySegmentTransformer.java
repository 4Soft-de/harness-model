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
