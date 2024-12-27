package com.foursoft.harness.vec.scripting.variants;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.v2x.VecConfigurationConstraint;
import com.foursoft.harness.vec.v2x.VecVariantConfiguration;

public class ConfigVariantBuilder implements Builder<ConfigVariantBuilder.Result> {

    private final VecConfigurationConstraint constraint;
    private VecVariantConfiguration variantConfiguration;

    public ConfigVariantBuilder(String identifier) {
        constraint = new VecConfigurationConstraint();
        constraint.setIdentification(identifier);
    }

    public ConfigVariantBuilder withLogisticControlExpression(String logisticControlExpression) {
        ensureVariantConfiguration();
        variantConfiguration.setLogisticControlExpression(logisticControlExpression);

        return this;
    }

    @Override public Result build() {
        return new Result(constraint, variantConfiguration);
    }

    private void ensureVariantConfiguration() {
        if (variantConfiguration == null) {
            variantConfiguration = new VecVariantConfiguration();
            variantConfiguration.setIdentification(constraint.getIdentification());
            constraint.setConfigInfo(variantConfiguration);
        }
    }

    public record Result(VecConfigurationConstraint constraint, VecVariantConfiguration variantConfiguration) {
    }

}
