/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.1.X To VEC 1.2.X
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
package com.foursoft.harness.compatibility.vec11to12;

import com.foursoft.harness.compatibility.vec11to12.util.InitializeFields;
import com.foursoft.harness.compatibility.vec11to12.util.Vec1XXReader;
import com.foursoft.harness.vec.v113.VecContent;
import com.foursoft.harness.vec.v113.VecDocumentVersion;
import com.foursoft.harness.vec.v113.VecPartVersion;
import com.foursoft.harness.vec.v113.VecPrimaryPartType;
import com.foursoft.harness.vec.v113.VecRingTerminalSpecification;
import com.foursoft.harness.vec.v113.VecSpecification;
import com.foursoft.harness.vec.v113.validation.SchemaFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Downgrades synthetic VEC 1.2.2 part master data, which consists of exactly the constructs VEC 1.1.3 models
 * differently or not at all, and checks that the result is a valid VEC 1.1.3 that keeps everything VEC 1.1.3
 * can express.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Vec12To11PartMasterConversionTest {

    private String sourceXml;
    private VecContent converted;
    private String convertedXml;

    @BeforeAll
    void convert() throws Exception {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.PART_MASTER_V122)) {
            sourceXml = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
        converted = Vec1XXReader.createVec11x(readSource(), "part master");
        InitializeFields.initializeFields(converted);
        convertedXml = new com.foursoft.harness.vec.v113.VecWriter().writeToString(converted);
    }

    @Test
    void fixtureIsAValidVec122() {
        assertThat(new SchemaViolations(com.foursoft.harness.vec.v12x.validation.SchemaFactory.getSchema())
                           .of(sourceXml))
                .isEmpty();
    }

    @Test
    void convertedDocumentIsValidAgainstTheVec113Schema() {
        assertThat(new SchemaViolations(SchemaFactory.getSchema()).of(convertedXml)).isEmpty();
    }

    @Test
    void onlyWhatVec113CannotExpressIsLeftOut() {
        final String sourceWritten = new com.foursoft.harness.vec.v12x.VecWriter().writeToString(readSource());

        final Map<String, String> differences =
                ElementHistogram.of(sourceWritten).differencesTo(ElementHistogram.of(convertedXml));

        assertThat(differences).containsOnly(
                // RequirementsConformanceSpecification, TerminalPairingSpecification and
                // BoltTerminalSpecification do not exist in VEC 1.1.3.
                Map.entry("DocumentVersion/Specification", "7 -> 4"),
                Map.entry("Specification/Identification", "7 -> 4"),
                Map.entry("Specification/DescribedPart", "4 -> 2"),
                Map.entry("Specification/ConformanceStatement", "1 -> 0"),
                Map.entry("ConformanceStatement/Satisfies", "1 -> 0"),
                Map.entry("ConformanceStatement/DocumentVersion", "1 -> 0"),
                Map.entry("Specification/MaxTerminalCount", "1 -> 0"),
                // Properties added by VEC 1.2.
                Map.entry("Specification/BoltNominalSize", "2 -> 0"),
                Map.entry("Specification/FitRate", "1 -> 0"));
    }

    @Test
    void specificationsWithoutCounterpartAreLeftOut() {
        assertThat(documentVersion("RING-1").getSpecifications())
                .extracting(VecSpecification::getIdentification)
                .containsExactly("GTPS_RING", "RTS_RING", "WRS_RING", "DDS_RING");
        assertThat(documentVersion("BOLT-1").getSpecifications()).isEmpty();
    }

    @Test
    void valuesVec113KnowsArrive() {
        final VecDocumentVersion documentVersion = documentVersion("RING-1");

        assertThat(documentVersion.getNumberOfSheets()).isEqualTo("3");
        assertThat(documentVersion.getSpecificationWithType(VecRingTerminalSpecification.class))
                .hasValueSatisfying(spec -> assertThat(spec.getBoltType()).isEqualTo("Stud"));
        assertThat(partVersion("RING-1").getPreferredUseCase()).isEqualTo("Battery connection");
    }

    @Test
    void primaryPartTypeUnknownToVec113BecomesTheGeneralOne() {
        assertThat(partVersion("RING-1").getPrimaryPartType()).isEqualTo(VecPrimaryPartType.RING_TERMINAL);
        assertThat(partVersion("BOLT-1").getPrimaryPartType()).isEqualTo(VecPrimaryPartType.TERMINAL);
    }

    @Test
    void settingTheVersionOfTheConvertedContentLeavesTheSourceUntouched() {
        final com.foursoft.harness.vec.v12x.VecContent source = readSource();
        final VecContent downgraded = Vec1XXReader.createVec11x(source, "part master");

        downgraded.setVecVersion("1.1.2");

        assertThat(downgraded.getVecVersion()).isEqualTo("1.1.2");
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
