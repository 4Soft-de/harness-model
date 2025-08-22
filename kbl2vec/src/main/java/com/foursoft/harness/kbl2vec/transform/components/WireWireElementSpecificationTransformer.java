package com.foursoft.harness.kbl2vec.transform.components;

import com.foursoft.harness.kbl.v25.KblGeneralWire;
import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl2vec.convert.StringToWireTypeConverter;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCoreSpecification;
import com.foursoft.harness.vec.v2x.VecInsulationSpecification;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import com.foursoft.harness.vec.v2x.VecWireElementSpecification;

public class WireWireElementSpecificationTransformer
        implements Transformer<KblGeneralWire, VecWireElementSpecification> {

    @Override public TransformationResult<VecWireElementSpecification> transform(final TransformationContext context,
                                                                                 final KblGeneralWire source) {
        final VecWireElementSpecification dest = new VecWireElementSpecification();
        dest.setIdentification("WIRE");
        final StringToWireTypeConverter wireTypeConverter =
                context.getConverterRegistry().getStringToWireTypeConverter();
        wireTypeConverter.convert(source.getWireType()).ifPresent(dest.getTypes()::add);

        final TransformationResult.Builder<VecWireElementSpecification> builder =
                TransformationResult.from(dest)
                        .withDownstream(KblNumericalValue.class, VecNumericalValue.class,
                                        Query.of(source::getOutsideDiameter),
                                        VecWireElementSpecification::setOutsideDiameter)
                        .withDownstream(KblNumericalValue.class, VecNumericalValue.class,
                                        Query.of(source::getBendRadius),
                                        VecWireElementSpecification::setMinBendRadiusStatic)
                        .withLinker(Query.of(source), VecInsulationSpecification.class,
                                    VecWireElementSpecification::setInsulationSpecification);
        // Single Core Wire
        if (source.getCores().isEmpty()) {
            builder.withLinker(Query.of(source), VecCoreSpecification.class,
                               VecWireElementSpecification::setConductorSpecification);
        }

        return builder
                .build();
    }
}
