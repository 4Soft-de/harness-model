/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
package com.foursoft.harness.vec.scripting.enums;

import com.foursoft.harness.vec.common.openenum.OpenEnumLiteral;
import com.foursoft.harness.vec.v2x.VecDocumentType;
import com.foursoft.harness.vec.v2x.VecDocumentTypeLiteral;
import com.foursoft.harness.vec.v2x.VecLengthClassification;
import com.foursoft.harness.vec.v2x.VecSignalInformationType;
import com.foursoft.harness.vec.v2x.VecSignalSubType;
import com.foursoft.harness.vec.v2x.VecSignalType;
import com.foursoft.harness.vec.v2x.VecTemperatureType;
import com.foursoft.harness.vec.v2x.VecTemperatureTypeLiteral;
import com.foursoft.harness.vec.v2x.VecWireReceptionType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The hand-maintained enums of this API are superseded by the ones generated from the VEC schema.
 * These tests pin that both write the same literals, and are deleted together with the deprecated
 * enums.
 */
@SuppressWarnings({"deprecation", "removal"})
class DeprecatedEnumsTest {

    @Test
    void theDeprecatedEnumsWriteTheSameLiteralsAsTheirReplacements() {
        final Map<OpenEnumLiteral, OpenEnumLiteral> replacements = Map.ofEntries(
                Map.entry(DocumentType.HARNESS_DESCRIPTION, VecDocumentType.HARNESS_DESCRIPTION),
                Map.entry(DocumentType.NETWORK_ARCHITECTURE, VecDocumentType.NETWORK_ARCHITECTURE),
                Map.entry(DocumentType.PART_MASTER, VecDocumentType.PART_MASTER),
                Map.entry(DocumentType.PROCESSING_INSTRUCTION, VecDocumentType.PROCESSING_INSTRUCTION),
                Map.entry(DocumentType.REQUIREMENTS_DESCRIPTION, VecDocumentType.REQUIREMENTS_DESCRIPTION),
                Map.entry(DocumentType.SYSTEM_SCHEMATIC, VecDocumentType.SYSTEM_SCHEMATIC),
                Map.entry(SignalType.INFORMATION, VecSignalType.INFORMATION),
                Map.entry(SignalType.ENERGY, VecSignalType.ENERGY),
                Map.entry(SignalType.GROUND, VecSignalType.GROUND),
                Map.entry(SignalSubType.CAN, VecSignalSubType.CAN),
                Map.entry(SignalSubType.LIN, VecSignalSubType.LIN),
                Map.entry(SignalInformationType.ANALOG, VecSignalInformationType.ANALOG),
                Map.entry(SignalInformationType.DIGITAL, VecSignalInformationType.DIGITAL),
                Map.entry(LengthClassification.DESIGNED, VecLengthClassification.DESIGNED),
                Map.entry(LengthClassification.ADAPTED, VecLengthClassification.ADAPTED),
                Map.entry(TemperatureType.OPERATING_TEMPERATURE, VecTemperatureType.OPERATING_TEMPERATURE),
                Map.entry(TemperatureType.AMBIENT_TEMPERATURE, VecTemperatureType.AMBIENT_TEMPERATURE),
                Map.entry(TemperatureType.SHORT_TERM_AGING_TEMPERATURE,
                          AdditionalTemperatureType.SHORT_TERM_AGING_TEMPERATURE),
                Map.entry(WireReceptionType.CRIMP, VecWireReceptionType.CRIMP),
                Map.entry(WireReceptionType.SOLDERING, VecWireReceptionType.SOLDERING),
                Map.entry(WireReceptionType.PLASMA_SOLDERING, VecWireReceptionType.PLASMA_SOLDERING));

        assertThat(replacements)
                .allSatisfy((deprecated, replacement) -> assertThat(deprecated.value())
                        .isEqualTo(replacement.value()));
    }

    @Test
    void everyConstantOfADeprecatedEnumIsCovered() {
        // Guards the map above against a constant being added to a deprecated enum without a
        // replacement being decided for it.
        assertThat(DocumentType.values()).hasSize(6);
        assertThat(SignalType.values()).hasSize(3);
        assertThat(SignalSubType.values()).hasSize(2);
        assertThat(SignalInformationType.values()).hasSize(2);
        assertThat(LengthClassification.values()).hasSize(2);
        assertThat(TemperatureType.values()).hasSize(3);
        assertThat(WireReceptionType.values()).hasSize(3);
    }

    @Test
    void theDeprecatedEnumsAreAcceptedWhereALiteralIsExpected() {
        // The deprecated enums implement the generated interfaces, so callers that have not migrated
        // yet keep compiling against the widened builder signatures.
        final VecDocumentTypeLiteral documentType = DocumentType.PART_MASTER;
        final VecTemperatureTypeLiteral temperatureType = TemperatureType.SHORT_TERM_AGING_TEMPERATURE;

        assertThat(documentType.value()).isEqualTo("PartMaster");
        assertThat(temperatureType.value()).isEqualTo("ShortTermAgingTemperature");
    }

    @Test
    void aLiteralThisApiAddsResolvesToItsOwnConstant() {
        // ScriptingOpenEnumLiterals registers it, so reading it back is typed rather than anonymous.
        assertThat(VecTemperatureTypeLiteral.of("ShortTermAgingTemperature"))
                .isSameAs(AdditionalTemperatureType.SHORT_TERM_AGING_TEMPERATURE);
        assertThat(AdditionalTemperatureType.SHORT_TERM_AGING_TEMPERATURE.isCustom()).isFalse();
    }

}
