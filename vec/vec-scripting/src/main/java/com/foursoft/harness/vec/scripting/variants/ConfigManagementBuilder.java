/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2025 4Soft GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =========================LICENSE_END==================================
 */
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
