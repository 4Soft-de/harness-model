package com.foursoft.harness.vec.scripting.variants;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.Customizer;
import com.foursoft.harness.vec.scripting.DefaultValues;
import com.foursoft.harness.vec.v2x.VecConfigurationConstraintSpecification;
import com.foursoft.harness.vec.v2x.VecSpecification;
import com.foursoft.harness.vec.v2x.VecVariantConfigurationSpecification;

import java.util.List;

public class ConfigManagementBuilder implements Builder<List<VecSpecification>> {

    private final VecVariantConfigurationSpecification variantConfigurationSpecification =
            new VecVariantConfigurationSpecification();
    private final VecConfigurationConstraintSpecification constraintSpecification =
            new VecConfigurationConstraintSpecification();

    public ConfigManagementBuilder() {
        variantConfigurationSpecification.setIdentification(DefaultValues.VARIANT_CONFIGURATION_SPEC_IDENTIFICATION);
        constraintSpecification.setIdentification(DefaultValues.CONFIG_CONSTRAINTS_SPEC_IDENTIFICATION);
    }

    public ConfigManagementBuilder addConfigVariant(String identifier, String logisticControlExpression) {
        return addConfigVariant(identifier, variant -> {
            variant.withLogisticControlExpression(logisticControlExpression);
        });
    }

    public ConfigManagementBuilder addConfigVariant(String identifier, Customizer<ConfigVariantBuilder> customizer) {
        final ConfigVariantBuilder builder = new ConfigVariantBuilder(identifier);

        customizer.customize(builder);

        final ConfigVariantBuilder.Result build = builder.build();
        if (build.variantConfiguration() != null) {
            variantConfigurationSpecification.getVariantConfigurations().add(build.variantConfiguration());
        }
        if (build.constraint() != null) {
            constraintSpecification.getConfigurationConstraints().add(build.constraint());
        }
        return this;
    }

    @Override
    public List<VecSpecification> build() {
        return List.of(variantConfigurationSpecification, constraintSpecification);
    }
}
