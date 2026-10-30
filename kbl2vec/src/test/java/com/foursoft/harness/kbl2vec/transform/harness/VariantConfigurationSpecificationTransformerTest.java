/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 - 2026 4Soft GmbH
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
import com.foursoft.harness.kbl2vec.core.ConversionOrchestrator;
import com.foursoft.harness.kbl2vec.core.ConversionProperties;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.kbl2vec.core.TransformerRegistry;
import com.foursoft.harness.vec.v2x.VecVariantConfiguration;
import com.foursoft.harness.vec.v2x.VecVariantConfigurationSpecification;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Runs on the real {@link ConversionOrchestrator}, because the deduplication of the variant configurations is
 * performed by the orchestration loop.
 */
class VariantConfigurationSpecificationTransformerTest {

    @Test
    void should_createOneVariantConfiguration_perDistinctExpression() {
        // Given
        final KblModuleConfiguration standalone = configuration("A+B");
        final KblModule module = module("M-001", configuration("A+B"));
        final KblHarness source = harness(List.of(module), standalone);

        // When
        final ConversionOrchestrator.Result<VecVariantConfigurationSpecification> result = transform(source);

        // Then
        final List<VecVariantConfiguration> variantConfigurations = result.resultValue().getVariantConfigurations();
        assertThat(variantConfigurations).singleElement()
                .returns("A+B", VecVariantConfiguration::getLogisticControlExpression);
        assertThat(result.entityMapping().get(standalone)).containsExactly(variantConfigurations.get(0));
        assertThat(result.entityMapping().get(module.getModuleConfiguration()))
                .containsExactly(variantConfigurations.get(0));
    }

    @Test
    void should_keepTheIdentificationOfTheModule_whenAStandaloneConfigurationHasTheSameExpression() {
        // Given
        final KblHarness source = harness(List.of(module("M-001", configuration("A+B"))), configuration("A+B"));

        // When
        final ConversionOrchestrator.Result<VecVariantConfigurationSpecification> result = transform(source);

        // Then
        assertThat(result.resultValue().getVariantConfigurations()).singleElement()
                .returns("VarConf_M-001", VecVariantConfiguration::getIdentification);
    }

    @Test
    void should_createSeparateVariantConfigurations_forDifferentExpressions() {
        // Given
        final KblHarness source = harness(List.of(module("M-001", configuration("A+B")),
                                                  module("M-002", configuration("A+C"))));

        // When
        final ConversionOrchestrator.Result<VecVariantConfigurationSpecification> result = transform(source);

        // Then
        assertThat(result.resultValue().getVariantConfigurations())
                .extracting(VecVariantConfiguration::getIdentification)
                .containsExactly("VarConf_M-001", "VarConf_M-002");
    }

    @Test
    void should_notMergeConfigurations_withoutExpression() {
        // Given
        final KblHarness source = harness(List.of(module("M-001", configuration("/NULL")),
                                                  module("M-002", configuration("/NULL"))));

        // When
        final ConversionOrchestrator.Result<VecVariantConfigurationSpecification> result = transform(source);

        // Then
        assertThat(result.resultValue().getVariantConfigurations()).hasSize(2);
    }

    private static ConversionOrchestrator.Result<VecVariantConfigurationSpecification> transform(
            final KblHarness source) {
        final TransformerRegistry registry = new TransformerRegistry() {
            @Override
            @SuppressWarnings("unchecked")
            public <S, D> Collection<Transformer<S, D>> getTransformer(final Class<S> sourceClass,
                                                                       final Class<D> destinationClass) {
                if (sourceClass == KblHarness.class) {
                    return List.of((Transformer<S, D>) new VariantConfigurationSpecificationTransformer());
                }
                if (sourceClass == KblModuleConfiguration.class) {
                    return List.of((Transformer<S, D>) new ModuleConfigurationVariantConfigurationTransformer());
                }
                return List.of();
            }
        };
        return new ConversionOrchestrator<>(KblHarness.class, VecVariantConfigurationSpecification.class, registry,
                                            new ConversionProperties())
                .orchestrateTransformation(source);
    }

    private static KblHarness harness(final List<KblModule> modules,
                                      final KblModuleConfiguration... standaloneConfigurations) {
        final KblHarness harness = new KblHarness();
        harness.setPartNumber("H-001");
        harness.getModules().addAll(modules);
        harness.getModuleConfigurations().addAll(List.of(standaloneConfigurations));
        return harness;
    }

    private static KblModule module(final String partNumber, final KblModuleConfiguration configuration) {
        final KblModule module = new KblModule();
        module.setPartNumber(partNumber);
        module.setModuleConfiguration(configuration);
        configuration.setParentModule(module);
        return module;
    }

    private static KblModuleConfiguration configuration(final String expression) {
        final KblModuleConfiguration configuration = new KblModuleConfiguration();
        configuration.setConfigurationType(KblModuleConfigurationType.OPTION_CODE);
        configuration.setLogisticControlInformation(expression);
        return configuration;
    }
}
