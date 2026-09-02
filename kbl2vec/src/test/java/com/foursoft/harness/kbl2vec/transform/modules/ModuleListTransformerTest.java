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
package com.foursoft.harness.kbl2vec.transform.modules;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblModule;
import com.foursoft.harness.kbl.v25.KblModuleConfiguration;
import com.foursoft.harness.kbl.v25.KblModuleConfigurationType;
import com.foursoft.harness.kbl.v25.KblWireProtectionOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecModuleList;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPartWithSubComponentsRole;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ModuleListTransformerTest {

    private final ModuleListTransformer transformer = new ModuleListTransformer();

    private final KblHarness harness = new KblHarness();

    @Test
    void should_transformModuleList() {
        // Given
        final KblModuleConfiguration firstModule = module("id_331_0");
        final KblModuleConfiguration secondModule = module("id_331_17");
        module("id_331_5"); // not referenced by the list

        final KblWireProtectionOccurrence component = new KblWireProtectionOccurrence();
        final KblModuleConfiguration source = moduleList("ML-1", "id_331_0 id_331_17", component);

        final VecPartWithSubComponentsRole firstRole = new VecPartWithSubComponentsRole();
        final VecPartWithSubComponentsRole secondRole = new VecPartWithSubComponentsRole();
        final VecPartOccurrence componentOccurrence = new VecPartOccurrence();

        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(firstModule, firstRole)
                .addMockMapping(secondModule, secondRole)
                .addMockMapping(component, componentOccurrence);

        // When
        final VecModuleList result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("ModuleList-ML-1", VecModuleList::getIdentification);
        assertThat(result.getModuleInList()).containsExactly(firstRole, secondRole);
        assertThat(result.getCompletionComponents()).containsExactly(componentOccurrence);
    }

    @Test
    void should_ignoreUnresolvableModuleReferences() {
        // Given
        final KblModuleConfiguration knownModule = module("id_331_0");
        final KblWireProtectionOccurrence component = new KblWireProtectionOccurrence();
        final KblModuleConfiguration source = moduleList("ML-2", "id_331_0 id_does_not_exist", component);

        final VecPartWithSubComponentsRole role = new VecPartWithSubComponentsRole();

        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(knownModule, role)
                .addMockMapping(component, new VecPartOccurrence());

        // When
        final VecModuleList result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getModuleInList()).containsExactly(role);
    }

    @Test
    void should_notTransform_whenConfigurationIsNoModuleList() {
        // Given
        final KblModuleConfiguration source = moduleList("ML-3", "id_331_0", new KblWireProtectionOccurrence());
        source.setConfigurationType(KblModuleConfigurationType.OPTION_CODE);

        // When
        final VecModuleList result = new TestConversionOrchestrator().transform(transformer, source);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void should_notTransform_whenNoModuleIsReferenced() {
        // Given
        final KblModuleConfiguration source = moduleList("ML-4", null, new KblWireProtectionOccurrence());

        // When
        final VecModuleList result = new TestConversionOrchestrator().transform(transformer, source);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void should_notTransform_whenNoComponentIsControlled() {
        // Given
        module("id_331_0");
        final KblModuleConfiguration source = moduleList("ML-5", "id_331_0");

        // When
        final VecModuleList result = new TestConversionOrchestrator().transform(transformer, source);

        // Then
        assertThat(result).isNull();
    }

    /**
     * Creates a module below the harness and returns its configuration, which is the source element the
     * {@link VecPartWithSubComponentsRole} of a module is created from.
     */
    private KblModuleConfiguration module(final String xmlId) {
        final KblModuleConfiguration configuration = new KblModuleConfiguration();
        final KblModule module = new KblModule();
        module.setXmlId(xmlId);
        module.setModuleConfiguration(configuration);
        configuration.setParentModule(module);
        harness.getModules().add(module);
        return configuration;
    }

    private KblModuleConfiguration moduleList(final String xmlId, final String logisticControlInformation,
                                              final KblWireProtectionOccurrence... controlledComponents) {
        final KblModuleConfiguration configuration = new KblModuleConfiguration();
        configuration.setXmlId(xmlId);
        configuration.setConfigurationType(KblModuleConfigurationType.MODULE_LIST);
        configuration.setLogisticControlInformation(logisticControlInformation);
        configuration.getControlledComponents().addAll(List.of(controlledComponents));
        configuration.setParentHarness(harness);
        harness.getModuleConfigurations().add(configuration);
        return configuration;
    }
}
