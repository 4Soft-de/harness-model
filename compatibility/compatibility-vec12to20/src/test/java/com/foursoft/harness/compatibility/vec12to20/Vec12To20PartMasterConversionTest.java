/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.2.X To VEC 2.0.X
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
package com.foursoft.harness.compatibility.vec12to20;

import com.foursoft.harness.compatibility.vec12to20.util.InitializeFields;
import com.foursoft.harness.compatibility.vec12to20.util.Vec20XReader;
import com.foursoft.harness.vec.v2x.VecBoltTerminalSpecification;
import com.foursoft.harness.vec.v2x.VecContent;
import com.foursoft.harness.vec.v2x.VecDefaultDimensionSpecification;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecLanguageCode;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecRequirementsConformanceSpecification;
import com.foursoft.harness.vec.v2x.VecRingTerminalSpecification;
import com.foursoft.harness.vec.v2x.VecWireReceptionSpecification;
import com.foursoft.harness.vec.v2x.validation.SchemaFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Upgrades synthetic VEC 1.2.2 part master data, which consists of exactly the constructs VEC 2.x models
 * differently, and checks that every value arrives in VEC 2.x and that the result is a valid VEC 2.2.0.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Vec12To20PartMasterConversionTest {

    private String sourceXml;
    private VecContent converted;
    private String convertedXml;

    @BeforeAll
    void convert() throws Exception {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.PART_MASTER_V122)) {
            sourceXml = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
        converted = Vec20XReader.createVec20x(readSource(), "part master");
        InitializeFields.initializeFields(converted);
        convertedXml = new com.foursoft.harness.vec.v2x.VecWriter().writeToString(converted);
    }

    @Test
    void fixtureIsAValidVec122() {
        assertThat(new SchemaViolations(com.foursoft.harness.vec.v12x.validation.SchemaFactory.getSchema())
                           .of(sourceXml))
                .isEmpty();
    }

    @Test
    void convertedDocumentIsValidAgainstTheVec2Schema() {
        assertThat(new SchemaViolations(SchemaFactory.getSchema()).of(convertedXml)).isEmpty();
    }

    @Test
    void onlyTheRemodelledElementsDifferFromTheSource() {
        final String sourceWritten = new com.foursoft.harness.vec.v12x.VecWriter()
                .writeToString(readSource());

        final Map<String, String> differences =
                ElementHistogram.of(sourceWritten).differencesTo(ElementHistogram.of(convertedXml));

        // The plain string became a localized string with a language and a value.
        assertThat(differences).containsOnly(Map.entry("PreferredUseCase/LanguageCode", "0 -> 1"),
                                             Map.entry("PreferredUseCase/Value", "0 -> 1"));
    }

    @Test
    void preferredUseCaseBecomesAGermanLocalizedString() {
        final VecPartVersion ringTerminal = partVersion("RING-1");

        assertThat(ringTerminal.getPreferredUseCases())
                .singleElement()
                .satisfies(useCase -> assertThat(useCase.getLanguageCode()).isEqualTo(VecLanguageCode.DE))
                .satisfies(useCase -> assertThat(useCase.getValue()).isEqualTo("Battery connection"))
                // An xs:ID has to be an NCName and unique, so it must not reuse the id of the part version.
                .satisfies(useCase -> assertThat(useCase.getXmlId()).matches("[A-Za-z_][\\w.-]*")
                        .isNotEqualTo(ringTerminal.getXmlId()));
    }

    @Test
    void numberOfSheetsBecomesAnInteger() {
        assertThat(documentVersion("RING-1").getNumberOfSheets()).isEqualTo(BigInteger.valueOf(3));
    }

    @Test
    void untypedBoltNominalSizeBecomesAString() {
        assertThat(documentVersion("RING-1").getSpecificationWithType(VecRingTerminalSpecification.class))
                .hasValueSatisfying(spec -> assertThat(spec.getBoltNominalSize()).isEqualTo("M8"));
        assertThat(documentVersion("BOLT-1").getSpecificationWithType(VecBoltTerminalSpecification.class))
                .hasValueSatisfying(spec -> assertThat(spec.getBoltNominalSize()).isEqualTo("M6"));
    }

    @Test
    void untypedSatisfiesBecomesABoolean() {
        assertThat(documentVersion("RING-1").getSpecificationWithType(VecRequirementsConformanceSpecification.class))
                .hasValueSatisfying(spec -> assertThat(spec.getConformanceStatements())
                        .singleElement()
                        .satisfies(statement -> assertThat(statement.isSatisfies()).isTrue()));
    }

    @Test
    void singleValuesBecomeListsWithThatValue() {
        final VecDocumentVersion documentVersion = documentVersion("RING-1");

        assertThat(documentVersion.getSpecificationWithType(VecDefaultDimensionSpecification.class))
                .hasValueSatisfying(spec -> assertThat(spec.getDefaultDimensions())
                        .singleElement()
                        .satisfies(dimension -> assertThat(dimension.getDimensionType()).isEqualTo("Length")));
        assertThat(documentVersion.getSpecificationWithType(VecWireReceptionSpecification.class))
                .hasValueSatisfying(spec -> assertThat(spec.getWireReceptionTypes()).containsExactly("Crimp"));
    }

    @Test
    void settingTheVersionOfTheConvertedContentLeavesTheSourceUntouched() {
        final com.foursoft.harness.vec.v12x.VecContent source = readSource();
        final VecContent upgraded = Vec20XReader.createVec20x(source, "part master");

        upgraded.setVecVersion("2.1.0");

        assertThat(upgraded.getVecVersion()).isEqualTo("2.1.0");
        assertThat(source.getVecVersion()).isEqualTo("1.2.2");
    }

    private com.foursoft.harness.vec.v12x.VecContent readSource() {
        return new com.foursoft.harness.vec.v12x.VecReader()
                .read(new ByteArrayInputStream(sourceXml.getBytes(StandardCharsets.UTF_8)));
    }

    private VecDocumentVersion documentVersion(final String documentNumber) {
        return converted.getDocumentVersions().stream()
                .filter(documentVersion -> documentNumber.equals(documentVersion.getDocumentNumber()))
                .findFirst()
                .orElseThrow();
    }

    private VecPartVersion partVersion(final String partNumber) {
        return converted.getPartVersions().stream()
                .filter(partVersion -> partNumber.equals(partVersion.getPartNumber()))
                .findFirst()
                .orElseThrow();
    }

}
