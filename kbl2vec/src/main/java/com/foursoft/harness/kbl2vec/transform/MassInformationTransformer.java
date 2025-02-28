package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecMassInformation;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MassInformationTransformer implements Transformer<KblNumericalValue, VecMassInformation> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MassInformationTransformer.class);

    @Override
    public TransformationResult<VecMassInformation> transform(final TransformationContext context,
                                                              final KblNumericalValue source) {
        LOGGER.debug("Transforming {} to VecMassinformation.", source);

        final VecMassInformation massInformation = new VecMassInformation();

        return TransformationResult.from(massInformation).downstreamTransformation(KblNumericalValue.class,
                                                                                   VecNumericalValue.class,
                                                                                   Query.of(source),
                                                                                   massInformation::setValue)
                .build();

    }
}
