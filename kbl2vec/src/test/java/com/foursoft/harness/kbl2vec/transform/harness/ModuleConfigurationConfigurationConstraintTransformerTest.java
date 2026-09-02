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

import com.foursoft.harness.kbl.v25.KblModule;
import com.foursoft.harness.kbl.v25.KblModuleConfiguration;
import com.foursoft.harness.kbl.v25.KblModuleConfigurationType;
import com.foursoft.harness.kbl.v25.KblWireProtectionOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecConfigurationConstraint;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecVariantConfiguration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ModuleConfigurationConfigurationConstraintTransformerTest {

    private final ModuleConfigurationConfigurationConstraintTransformer transformer =
            new ModuleConfigurationConfigurationConstraintTransformer();

    @Test
    void should_constrainTheModule_forAModulesConfiguration() {
        // Given
        final KblModule module = new KblModule();
        module.setPartNumber("M-001");
        final KblModuleConfiguration source = new KblModuleConfiguration();
        source.setConfigurationType(KblModuleConfigurationType.OPTION_CODE);
        source.setParentModule(module);
        module.setModuleConfiguration(source);
        // Controlled components exist, but for a module's configuration the module is constrained.
        source.getControlledComponents().add(new KblWireProtectionOccurrence());

        final VecPartOccurrence moduleOccurrence = new VecPartOccurrence();
        final VecVariantConfiguration variantConfiguration = new VecVariantConfiguration();

        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(module, moduleOccurrence)
                .addMockMapping(source, variantConfiguration);

        // When
        final VecConfigurationConstraint result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("ConfConstraint_M-001", VecConfigurationConstraint::getIdentification)
                .returns(variantConfiguration, VecConfigurationConstraint::getConfigInfo);
        assertThat(result.getConstrainedElements()).containsExactly(moduleOccurrence);
    }

    @Test
    void should_constrainTheControlledComponents_forAStandaloneConfiguration() {
        // Given
        final KblWireProtectionOccurrence firstComponent = new KblWireProtectionOccurrence();
        final KblWireProtectionOccurrence secondComponent = new KblWireProtectionOccurrence();
        final KblModuleConfiguration source = new KblModuleConfiguration();
        source.setXmlId("id_353_136");
        source.setConfigurationType(KblModuleConfigurationType.OPTION_CODE);
        source.getControlledComponents().add(firstComponent);
        source.getControlledComponents().add(secondComponent);

        final VecPartOccurrence firstOccurrence = new VecPartOccurrence();
        final VecPartOccurrence secondOccurrence = new VecPartOccurrence();
        final VecVariantConfiguration variantConfiguration = new VecVariantConfiguration();

        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(firstComponent, firstOccurrence)
                .addMockMapping(secondComponent, secondOccurrence)
                .addMockMapping(source, variantConfiguration);

        // When
        final VecConfigurationConstraint result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(variantConfiguration, VecConfigurationConstraint::getConfigInfo);
        assertThat(result.getIdentification()).startsWith("GenericIdentifier-");
        assertThat(result.getConstrainedElements()).containsExactly(firstOccurrence, secondOccurrence);
    }

    @Test
    void should_notTransform_whenAStandaloneConfigurationControlsNoComponents() {
        // Given
        final KblModuleConfiguration source = new KblModuleConfiguration();
        source.setXmlId("id_353_137");
        source.setConfigurationType(KblModuleConfigurationType.OPTION_CODE);

        // When
        final VecConfigurationConstraint result = new TestConversionOrchestrator().transform(transformer, source);

        // Then
        assertThat(result).isNull();
    }
}
