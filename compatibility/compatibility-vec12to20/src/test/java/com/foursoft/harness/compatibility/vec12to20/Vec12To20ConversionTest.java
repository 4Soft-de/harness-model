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
import com.foursoft.harness.vec.v2x.validation.SchemaFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Upgrades a complete, real VEC 1.2 harness document to VEC 2.x and checks the result the way a consumer
 * sees it: written out and validated against the VEC 2.2.0 schema.
 * <p>
 * The sample document itself is not valid (it contains duplicate ids and references to missing ids), so the
 * conversion is checked not to add any violation of its own.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Vec12To20ConversionTest {

    private static final Pattern VIOLATION = Pattern.compile("(cvc-[\\w.-]+)[^']*('[^']*'|$)");

    private String sourceXml;
    private String convertedXml;

    @BeforeAll
    void convert() throws Exception {
        final com.foursoft.harness.vec.v12x.VecContent source;
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE_V12X)) {
            source = new com.foursoft.harness.vec.v12x.VecReader().read(inputStream);
        }
        // Written before the conversion: reading a JAXB list initializes it, which the writer then emits as an
        // empty element.
        sourceXml = new com.foursoft.harness.vec.v12x.VecWriter().writeToString(source);
        final com.foursoft.harness.vec.v2x.VecContent converted = Vec20XReader.createVec20x(source, "old beetle");
        InitializeFields.initializeFields(converted);
        convertedXml = new com.foursoft.harness.vec.v2x.VecWriter().writeToString(converted);
    }

    @Test
    void conversionAddsNoViolationOfTheVec2Schema() {
        final Set<String> sourceViolations = normalized(
                new SchemaViolations(com.foursoft.harness.vec.v12x.validation.SchemaFactory.getSchema())
                        .of(sourceXml));

        final Set<String> convertedViolations =
                normalized(new SchemaViolations(SchemaFactory.getSchema()).of(convertedXml));

        assertThat(convertedViolations).isSubsetOf(sourceViolations);
    }

    @Test
    void convertedDocumentKeepsAllElementsOfTheSource() {
        final Map<String, String> differences =
                ElementHistogram.of(sourceXml).differencesTo(ElementHistogram.of(convertedXml));

        assertThat(differences).containsOnly(
                // The one plain preferred use case became a localized string with a language and a value.
                Map.entry("PreferredUseCase/LanguageCode", "0 -> 1"),
                Map.entry("PreferredUseCase/Value", "0 -> 1"),
                // The connector housing roles reference their component connectors instead of the component
                // node. The other three component nodes belong to EE component roles and are kept.
                Map.entry("Role/ComponentConnector", "0 -> 6"),
                Map.entry("Role/ComponentNode", "9 -> 3"));
    }

    /**
     * Reduces a violation to its code and the first value it names (e.g. the duplicate id or the unexpected
     * element). The rest of the message lists what the respective schema expects instead, which differs
     * between the versions for the same problem.
     */
    private Set<String> normalized(final List<String> violations) {
        return violations.stream()
                .map(violation -> {
                    final Matcher matcher = VIOLATION.matcher(violation);
                    return matcher.find() ? matcher.group(1) + " " + matcher.group(2) : violation;
                })
                .collect(Collectors.toSet());
    }

}
