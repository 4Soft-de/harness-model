/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.transform.harness;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblModule;
import com.foursoft.harness.kbl.v25.KblModuleConfiguration;
import com.foursoft.harness.kbl.v25.KblModuleConfigurationType;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecVariantConfiguration;
import com.foursoft.harness.vec.v2x.VecVariantConfigurationSpecification;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.foursoft.harness.kbl2vec.utils.UndefinedValues.isUndefined;
import static com.foursoft.harness.vec.common.util.ModelClassNames.abbreviatedModelClassName;

public class VariantConfigurationSpecificationTransformer
        implements Transformer<KblHarness, VecVariantConfigurationSpecification> {
    @Override
    public TransformationResult<VecVariantConfigurationSpecification> transform(final TransformationContext context,
                                                                                final KblHarness source) {
        final VecVariantConfigurationSpecification element = new VecVariantConfigurationSpecification();
        element.setIdentification(
                abbreviatedModelClassName(element.getClass()) + "-" + source.getPartNumber());
        warnAboutDroppedProcessingInstructions(context, source);
        return TransformationResult.from(element)
                // One VariantConfiguration per distinct logistic control expression (see the VEC guideline
                // product-definition/harness), all configurations with the same expression reference it. The first one
                // is retained, so module configurations come first: their identification is derived from the module.
                .withDownstream(KblModuleConfiguration.class, VecVariantConfiguration.class,
                                moduleConfigurationQuery(source),
                                VecVariantConfigurationSpecification::getVariantConfigurations,
                                VariantConfigurationSpecificationTransformer::expressionKey, null)
                .build();
    }

    /**
     * The downstream transformations of a deduplicated configuration are not executed, so its processing instructions
     * are lost.
     */
    private void warnAboutDroppedProcessingInstructions(final TransformationContext context, final KblHarness source) {
        final Set<String> expressions = new HashSet<>();
        moduleConfigurationQuery(source).stream()
                .filter(c -> !isUndefined(c.getLogisticControlInformation()))
                .filter(c -> !expressions.add(c.getLogisticControlInformation()))
                .filter(c -> !c.getProcessingInstructions().isEmpty())
                .forEach(c -> context.getLogger().warn(
                        "'{}' has the same logistic control expression as another configuration, its processing " +
                                "instructions are dropped.", c));
    }

    /**
     * Configurations without an expression are never merged, each one is its own key.
     */
    private static Object expressionKey(final VecVariantConfiguration configuration) {
        final String expression = configuration.getLogisticControlExpression();
        return isUndefined(expression) ? configuration : expression;
    }

    Query<KblModuleConfiguration> moduleConfigurationQuery(final KblHarness source) {
        return () -> {
            final Stream<KblModuleConfiguration> moduleModuleConfigurations = source.getModules().stream().map(
                    KblModule::getModuleConfiguration);
            // Module lists are not logistic expressions, they are transformed into a VecModuleList instead.
            final Stream<KblModuleConfiguration> standAloneModuleConfigurations =
                    source.getModuleConfigurations().stream()
                            .filter(c -> c.getConfigurationType() != KblModuleConfigurationType.MODULE_LIST);

            return Stream.concat(moduleModuleConfigurations, standAloneModuleConfigurations)
                    .toList();
        };
    }

}
