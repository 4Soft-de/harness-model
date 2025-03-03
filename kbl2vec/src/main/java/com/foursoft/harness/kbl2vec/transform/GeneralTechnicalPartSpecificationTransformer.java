package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.KblConnectorHousing;
import com.foursoft.harness.kbl.v25.KblMaterial;
import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl2vec.convert.StringToColorConverter;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecGeneralTechnicalPartSpecification;
import com.foursoft.harness.vec.v2x.VecMassInformation;
import com.foursoft.harness.vec.v2x.VecMaterial;

import static com.foursoft.harness.kbl2vec.transform.SpecificationUtils.commonSpecificationAttributes;

public class GeneralTechnicalPartSpecificationTransformer
        implements Transformer<KblPart, VecGeneralTechnicalPartSpecification> {
    @Override
    public TransformationResult<VecGeneralTechnicalPartSpecification> transform(final TransformationContext context,
                                                                                final KblPart source) {
        final VecGeneralTechnicalPartSpecification specification = new VecGeneralTechnicalPartSpecification();

        if (source instanceof final KblConnectorHousing connectorHousing) {
            final StringToColorConverter colorConverter =
                    context.getConverterRegistry().getStringToColorConverter();

            colorConverter.convert(connectorHousing.getHousingColour()).ifPresent(
                    specification.getColorInformations()::add);
        }

        return TransformationResult.from(specification)
                .withFragment(commonSpecificationAttributes(source))
                .downstreamTransformation(KblNumericalValue.class, VecMassInformation.class,
                                          Query.of(source.getMassInformation()),
                                          specification::getMassInformations)
                .downstreamTransformation(KblMaterial.class, VecMaterial.class,
                                          Query.of(source.getMaterialInformation()),
                                          specification::getMaterialInformations)
                .build();
    }
}
