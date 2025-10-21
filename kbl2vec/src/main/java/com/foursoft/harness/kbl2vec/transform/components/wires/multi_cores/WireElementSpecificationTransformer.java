package com.foursoft.harness.kbl2vec.transform.components.wires.multi_cores;

import com.foursoft.harness.kbl.v25.KblCore;
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

public class WireElementSpecificationTransformer implements Transformer<KblCore, VecWireElementSpecification> {

    @Override
    public TransformationResult<VecWireElementSpecification> transform(final TransformationContext context,
                                                                       final KblCore source) {
        final VecWireElementSpecification destination = new VecWireElementSpecification();
        destination.setIdentification(source.getId());

        final StringToWireTypeConverter wireTypeConverter =
                context.getConverterRegistry().getStringToWireTypeConverter();
        wireTypeConverter.convert(source.getWireType()).ifPresent(destination.getTypes()::add);

        return TransformationResult.from(destination)
                .withDownstream(KblNumericalValue.class, VecNumericalValue.class,
                                Query.of(source::getOutsideDiameter),
                                VecWireElementSpecification::setOutsideDiameter)
                .withDownstream(KblNumericalValue.class, VecNumericalValue.class,
                                Query.of(source::getBendRadius),
                                VecWireElementSpecification::setMinBendRadiusStatic)
                .withLinker(Query.of(source), VecInsulationSpecification.class,
                            VecWireElementSpecification::setInsulationSpecification)
                .withLinker(Query.of(source), VecCoreSpecification.class,
                            VecWireElementSpecification::setConductorSpecification)
                .build();
    }
}
