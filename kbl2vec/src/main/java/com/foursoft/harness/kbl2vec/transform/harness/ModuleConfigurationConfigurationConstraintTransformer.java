package com.foursoft.harness.kbl2vec.transform.harness;

import com.foursoft.harness.kbl.v25.KblModuleConfiguration;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecConfigurationConstraint;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecVariantConfiguration;

public class ModuleConfigurationConfigurationConstraintTransformer
        implements Transformer<KblModuleConfiguration, VecConfigurationConstraint> {

    private int idCounter = 0;

    @Override public TransformationResult<VecConfigurationConstraint> transform(final TransformationContext context,
                                                                                final KblModuleConfiguration source) {

        final VecConfigurationConstraint configurationConstraint = new VecConfigurationConstraint();

        final TransformationResult.Builder<VecConfigurationConstraint> builder = TransformationResult.from(
                configurationConstraint);

        if (source.getParentModule() != null) {
            builder.withFragment((v, b) -> {
                v.setIdentification("ConfConstraint_" + source.getParentModule().getPartNumber());
            });
        } else {
            builder.withComment("This occurence has no \"Id\" in the KBL data.");
            builder.withFragment((v, b) -> {
                v.setIdentification("GenericIdentifier-" + idCounter++);
            });
        }

        return builder
                //TODO: only working for "non-standalone" module configurations
                .withLinker(Query.of(source), VecVariantConfiguration.class, VecConfigurationConstraint::setConfigInfo)
                .withLinker(Query.of(source.getParentModule()), VecPartOccurrence.class,
                            VecConfigurationConstraint::getConstrainedElements)
                .build();
    }
}
