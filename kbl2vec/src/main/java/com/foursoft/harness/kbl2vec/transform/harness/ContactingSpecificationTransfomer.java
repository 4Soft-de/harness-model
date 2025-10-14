package com.foursoft.harness.kbl2vec.transform.harness;

import com.foursoft.harness.kbl.v25.KblContactPoint;
import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecContactPoint;
import com.foursoft.harness.vec.v2x.VecContactingSpecification;

import java.util.List;

public class ContactingSpecificationTransfomer implements Transformer<KblHarness, VecContactingSpecification> {

    @Override
    public TransformationResult<VecContactingSpecification> transform(final TransformationContext context,
                                                                      final KblHarness source) {
        final VecContactingSpecification destination = new VecContactingSpecification();

        final List<KblContactPoint> contactPoints = source.getConnectorOccurrences().stream().flatMap(
                c -> c.getContactPoints().stream()).toList();
        return TransformationResult.from(destination)
                .withDownstream(KblContactPoint.class, VecContactPoint.class, Query.fromLists(contactPoints),
                                VecContactingSpecification::getContactPoints)
                .build();
    }
}
