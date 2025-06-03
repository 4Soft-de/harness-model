package com.foursoft.harness.kbl2vec.transform.harness;

import com.foursoft.harness.kbl.v25.KblModuleConfiguration;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecVariantConfiguration;

public class ModuleConfigurationVariantConfigurationTransformer
        implements Transformer<KblModuleConfiguration, VecVariantConfiguration> {

    private int idCounter = 0;

    @Override public TransformationResult<VecVariantConfiguration> transform(final TransformationContext context,
                                                                             final KblModuleConfiguration source) {

        final VecVariantConfiguration variantConfiguration = new VecVariantConfiguration();
        variantConfiguration.setConfigurationType("Logistic");
        variantConfiguration.setLogisticControlString(source.getLogisticControlInformation());

        final TransformationResult.Builder<VecVariantConfiguration> builder = TransformationResult.from(
                variantConfiguration);

        if (source.getParentModule() != null) {
            builder.withFragment((v, b) -> {
                v.setIdentification("VarConf_" + source.getParentModule().getPartNumber());
            });
        } else {
            builder.withComment("This occurence has no \"Id\" in the KBL data.");
            builder.withFragment((v, b) -> {
                v.setIdentification("GenericIdentifier-" + idCounter++);
            });
        }

        return builder.build();
    }
}
