package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecGeneralTechnicalPartSpecification;
import com.foursoft.harness.vec.v2x.VecMassInformation;
import com.foursoft.harness.vec.v2x.VecMaterial;
import com.foursoft.harness.vec.v2x.VecPartVersion;

public class GeneralTechnicalPartSpecificationTransformer
        implements Transformer<KblPart, VecGeneralTechnicalPartSpecification> {
    @Override
    public TransformationResult<VecGeneralTechnicalPartSpecification> transform(final TransformationContext context,
                                                                                final KblPart source) {
        final VecGeneralTechnicalPartSpecification specification = new VecGeneralTechnicalPartSpecification();
        specification.setIdentification("GenTechPart_" + source.getPartNumber());
        //TODO: Mass / Material
        final VecMassInformation mi = new VecMassInformation();

        specification.getMassInformations()
                .add(mi);
        specification.getMaterialInformations()
                .add(new VecMaterial());
        //        source.get
        return TransformationResult.from(specification)
                .withLinker(Query.of(source), VecPartVersion.class, specification::getDescribedPart)
                .build();
    }
}
