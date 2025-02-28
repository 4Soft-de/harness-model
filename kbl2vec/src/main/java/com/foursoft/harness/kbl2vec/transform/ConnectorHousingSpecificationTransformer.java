package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.KblConnectorHousing;
import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import com.foursoft.harness.vec.v2x.VecPartVersion;

import static com.foursoft.harness.kbl2vec.transform.SpecificationUtils.withIdentification;

public class ConnectorHousingSpecificationTransformer
        implements Transformer<KblPart, VecConnectorHousingSpecification> {
    @Override
    public TransformationResult<VecConnectorHousingSpecification> transform(final TransformationContext context,
                                                                            final KblPart source) {
        if (source instanceof final KblConnectorHousing connector) {
            final VecConnectorHousingSpecification specification = new VecConnectorHousingSpecification();

            withIdentification(specification, source.getPartNumber());

            specification.setSpecialPartType(connector.getHousingType());

            return TransformationResult.from(specification)
                    .withLinker(Query.of(source), VecPartVersion.class, specification::getDescribedPart)
                    .build();
        }
        return TransformationResult.noResult();
    }
}
