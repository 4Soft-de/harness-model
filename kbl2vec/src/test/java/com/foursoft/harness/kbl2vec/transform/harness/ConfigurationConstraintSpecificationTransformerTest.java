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
import com.foursoft.harness.kbl.v25.KblWireProtectionOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecConfigurationConstraint;
import com.foursoft.harness.vec.v2x.VecConfigurationConstraintSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigurationConstraintSpecificationTransformerTest {

    private final ConfigurationConstraintSpecificationTransformer transformer =
            new ConfigurationConstraintSpecificationTransformer();

    @Test
    void should_transformConfigurationConstraintSpecification_forAModule() {
        // Given
        final KblModule module = new KblModule();
        final KblModuleConfiguration configuration = new KblModuleConfiguration();
        configuration.setConfigurationType(KblModuleConfigurationType.OPTION_CODE);
        configuration.setParentModule(module);
        module.setModuleConfiguration(configuration);
        final KblHarness source = new KblHarness();
        source.setPartNumber("H-001");
        source.getModules().add(module);

        final VecConfigurationConstraint mappedConstraint = new VecConfigurationConstraint();

        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(configuration, mappedConstraint);

        // When
        final VecConfigurationConstraintSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("CCS-H-001", VecConfigurationConstraintSpecification::getIdentification);
        assertThat(result.getConfigurationConstraints()).containsExactly(mappedConstraint);
    }

    @Test
    void should_transformConfigurationConstraintSpecification_forAStandaloneConfigurationWithComponents() {
        // Given
        final KblModuleConfiguration configuration = standaloneConfiguration();
        configuration.getControlledComponents().add(new KblWireProtectionOccurrence());
        final KblHarness source = new KblHarness();
        source.getModuleConfigurations().add(configuration);

        final VecConfigurationConstraint mappedConstraint = new VecConfigurationConstraint();

        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(configuration, mappedConstraint);

        // When
        final VecConfigurationConstraintSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getConfigurationConstraints()).containsExactly(mappedConstraint);
    }

    @Test
    void should_notTransform_whenNoConfigurationsArePresent() {
        // Given
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        // When
        final VecConfigurationConstraintSpecification result = orchestrator.transform(transformer, new KblHarness());

        // Then
        assertThat(result).isNull();
    }

    @Test
    void should_notTransform_whenNoConfigurationConstrainsAnything() {
        // Given
        final KblHarness source = new KblHarness();
        // Skipped by the ConfigurationConstraint transformer, because it controls no components.
        source.getModuleConfigurations().add(standaloneConfiguration());
        // Transformed into a ModuleList instead.
        final KblModuleConfiguration moduleList = standaloneConfiguration();
        moduleList.setConfigurationType(KblModuleConfigurationType.MODULE_LIST);
        moduleList.getControlledComponents().add(new KblWireProtectionOccurrence());
        source.getModuleConfigurations().add(moduleList);

        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        // When
        final VecConfigurationConstraintSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNull();
    }

    private static KblModuleConfiguration standaloneConfiguration() {
        final KblModuleConfiguration configuration = new KblModuleConfiguration();
        configuration.setConfigurationType(KblModuleConfigurationType.OPTION_CODE);
        return configuration;
    }

}
