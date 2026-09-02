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
import com.foursoft.harness.kbl.v25.KblModuleConfiguration;
import com.foursoft.harness.kbl.v25.KblModuleConfigurationType;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecModuleList;
import com.foursoft.harness.vec.v2x.VecModuleListSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ModuleListSpecificationTransformerTest {

    private final ModuleListSpecificationTransformer transformer = new ModuleListSpecificationTransformer();

    @Test
    void should_transformModuleListSpecification() {
        // Given
        final KblHarness source = new KblHarness();
        final KblModuleConfiguration moduleList = configuration(KblModuleConfigurationType.MODULE_LIST);
        final KblModuleConfiguration optionCode = configuration(KblModuleConfigurationType.OPTION_CODE);
        source.getModuleConfigurations().add(optionCode);
        source.getModuleConfigurations().add(moduleList);

        final VecModuleList mappedModuleList = new VecModuleList();

        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(moduleList, mappedModuleList);

        // When
        final VecModuleListSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("ModuleLists", VecModuleListSpecification::getIdentification);
        assertThat(result.getModuleListConfigurations()).containsExactly(mappedModuleList);
    }

    @Test
    void should_notTransform_whenOnlyOtherModuleConfigurationsArePresent() {
        // Given
        final KblHarness source = new KblHarness();
        source.getModuleConfigurations().add(configuration(KblModuleConfigurationType.OPTION_CODE));
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        // When
        final VecModuleListSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNull();
    }

    private static KblModuleConfiguration configuration(final KblModuleConfigurationType type) {
        final KblModuleConfiguration configuration = new KblModuleConfiguration();
        configuration.setConfigurationType(type);
        return configuration;
    }
}
