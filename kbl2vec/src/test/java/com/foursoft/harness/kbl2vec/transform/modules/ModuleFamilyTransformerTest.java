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
package com.foursoft.harness.kbl2vec.transform.modules;

import com.foursoft.harness.kbl.v25.KblLanguageCode;
import com.foursoft.harness.kbl.v25.KblLocalizedString;
import com.foursoft.harness.kbl.v25.KblModule;
import com.foursoft.harness.kbl.v25.KblModuleConfiguration;
import com.foursoft.harness.kbl.v25.KblModuleFamily;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecAbstractLocalizedString;
import com.foursoft.harness.vec.v2x.VecLanguageCode;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecModuleFamily;
import com.foursoft.harness.vec.v2x.VecPartWithSubComponentsRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class ModuleFamilyTransformerTest {

    @Test
    void should_transformModuleFamily() {
        // Given
        final ModuleFamilyTransformer transformer = new ModuleFamilyTransformer();

        final KblModuleFamily source = new KblModuleFamily();
        source.setId("AUDIO");
        source.setDescription("Audio equipment");
        source.getLocalizedDescriptions().add(localizedString(KblLanguageCode.EN, "Audio equipment"));
        source.getLocalizedDescriptions().add(localizedString(KblLanguageCode.DE, "Audioausstattung"));

        final KblModuleConfiguration premiumConfiguration = createModule(source, "M-002");
        final KblModuleConfiguration basicConfiguration = createModule(source, "M-001");

        final VecPartWithSubComponentsRole basicRole = new VecPartWithSubComponentsRole();
        final VecPartWithSubComponentsRole premiumRole = new VecPartWithSubComponentsRole();

        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(basicConfiguration, basicRole)
                .addMockMapping(premiumConfiguration, premiumRole);

        // When
        final VecModuleFamily result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("AUDIO", VecModuleFamily::getIdentification);
        assertThat(result.getDescriptions())
                .extracting(d -> ((VecLocalizedString) d).getLanguageCode(),
                            VecAbstractLocalizedString::getValue)
                .containsExactly(tuple(VecLanguageCode.DE, "Audio equipment"),
                                 tuple(VecLanguageCode.EN, "Audio equipment"),
                                 tuple(VecLanguageCode.DE, "Audioausstattung"));
        assertThat(result.getModuleInFamily()).containsExactly(basicRole, premiumRole);
    }

    private static KblLocalizedString localizedString(final KblLanguageCode languageCode, final String value) {
        final KblLocalizedString localizedString = new KblLocalizedString();
        localizedString.setLanguageCode(languageCode);
        localizedString.setValue(value);
        return localizedString;
    }

    private static KblModuleConfiguration createModule(final KblModuleFamily family, final String partNumber) {
        final KblModuleConfiguration configuration = new KblModuleConfiguration();
        final KblModule module = new KblModule();
        module.setPartNumber(partNumber);
        module.setModuleConfiguration(configuration);
        module.setOfFamily(family);
        family.getRefModule().add(module);
        return configuration;
    }
}
