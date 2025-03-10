package com.foursoft.harness.kbl2vec.transform.topology;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblNode;
import com.foursoft.harness.kbl.v25.KblSegment;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecTopologyNode;
import com.foursoft.harness.vec.v2x.VecTopologySegment;
import com.foursoft.harness.vec.v2x.VecTopologySpecification;

import static com.foursoft.harness.kbl2vec.transform.Fragments.abbreviatedClassName;

public class TopologySpecificationTransformer implements Transformer<KblHarness, VecTopologySpecification> {
    @Override
    public TransformationResult<VecTopologySpecification> transform(final TransformationContext context,
                                                                    final KblHarness source) {
        final VecTopologySpecification topologySpecification = new VecTopologySpecification();
        topologySpecification.setIdentification(
                abbreviatedClassName(topologySpecification.getClass()) + "-" + source.getPartNumber());

        return TransformationResult.from(topologySpecification)
                .downstreamTransformation(KblNode.class, VecTopologyNode.class,
                                          () -> source.getParentKBLContainer().getNodes(),
                                          topologySpecification::getTopologyNodes)
                .downstreamTransformation(KblSegment.class, VecTopologySegment.class,
                                          () -> source.getParentKBLContainer().getSegments(),
                                          topologySpecification::getTopologySegments)
                .build();
    }
}
