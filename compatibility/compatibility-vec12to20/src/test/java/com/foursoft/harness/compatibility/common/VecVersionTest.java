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
package com.foursoft.harness.compatibility.common;

import com.foursoft.harness.compatibility.vec.common.VecVersion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class VecVersionTest {

    private static final String XS_NAMESPACE = XMLConstants.W3C_XML_SCHEMA_NS_URI;

    @Test
    void knowsEveryReleaseTheSchemaDefines() throws Exception {
        // The VecVersion enumeration of the newest schema lists every release of the VEC.
        final List<String> releases = vecVersionLiterals("/vec2/vec_2.2.0.xsd");

        final Map<String, VecVersion> entryOfRelease = releases.stream()
                .collect(Collectors.toMap(release -> release,
                                          release -> VecVersion.findVersionByNumber(release).orElseThrow()));
        assertThat(entryOfRelease)
                .containsEntry("1.1.1", VecVersion.VEC11X)
                .containsEntry("1.1.3", VecVersion.VEC11X)
                .containsEntry("1.2.0", VecVersion.VEC12X)
                .containsEntry("1.2.1", VecVersion.VEC12X)
                .containsEntry("1.2.2", VecVersion.VEC12X)
                .containsEntry("2.0.0", VecVersion.VEC2XX)
                .containsEntry("2.2.0", VecVersion.VEC2XX);

        final List<String> knownVersions = Arrays.stream(VecVersion.values())
                .flatMap(vecVersion -> vecVersion.getKnownVersions().stream())
                .toList();
        assertThat(knownVersions).isEqualTo(releases);
    }

    @Test
    void writesTheSchemaVersionOfTheModel() {
        // A converted document claims the version of the schema its model is generated from.
        assertThat(VecVersion.VEC12X.getCurrentVersion())
                .isEqualTo(com.foursoft.harness.vec.v12x.Version.VERSION)
                .isEqualTo("1.2.2");
        assertThat(VecVersion.VEC2XX.getCurrentVersion())
                .isEqualTo(com.foursoft.harness.vec.v2x.Version.VERSION)
                .isEqualTo("2.2.0");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2.0.3", "2.3.0", "2.10.1", " 2.2.0 "})
    void acceptsEveryVec2Release(final String versionNumber) {
        // Since 2.0.0 the VEC follows semantic versioning, so a later 2.x release is still readable.
        assertThat(VecVersion.findVersionByNumber(versionNumber)).contains(VecVersion.VEC2XX);
        assertThat(VecVersion.findVersion("<VecVersion>" + versionNumber + "</VecVersion>"))
                .contains(VecVersion.VEC2XX);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.0.0", "1.1.0", "1.1.4", "1.2.3", "3.0.0", "2.2", "2.2.0-SNAPSHOT", "", "VEC 1.2.0"})
    void rejectsVersionsThatAreNoRelease(final String versionNumber) {
        assertThat(VecVersion.findVersionByNumber(versionNumber)).isEmpty();
        assertThat(VecVersion.findVersion("<VecVersion>" + versionNumber + "</VecVersion>")).isEmpty();
    }

    @Test
    void findsNoVersionWithoutVersionNumber() {
        assertThat(VecVersion.findVersionByNumber(null)).isEmpty();
        assertThat(VecVersion.findVersion(new com.foursoft.harness.vec.v2x.VecContent())).isEmpty();
        assertThat(VecVersion.findVersion("2.2.0")).isEmpty();
    }

    @Test
    void findsTheVersionOfAContent() {
        final com.foursoft.harness.vec.v2x.VecContent content = new com.foursoft.harness.vec.v2x.VecContent();
        content.setVecVersion("2.1.0");

        assertThat(VecVersion.findVersion(content)).contains(VecVersion.VEC2XX);
    }

    @Test
    void readsTheVersionFromTheBeginningOfADocument() {
        final String beginning = """
                <?xml version="1.0" encoding="UTF-8"?>
                <vec:VecContent xmlns:vec="http://www.prostep.org/ecad-if/2011/vec" id="id_1">
                  <VecVersion>2.0.1</VecVersion>
                  <GeneratingSystemName>test</GeneratingSystemName>
                """;

        assertThat(VecVersion.findVersion(beginning)).contains(VecVersion.VEC2XX);
    }

    @Test
    void canBeConvertedToTheNeighbouringVersionsOnly() {
        assertThat(VecVersion.VEC12X.canBeConvertedTo(VecVersion.VEC2XX)).isTrue();
        assertThat(VecVersion.VEC2XX.canBeConvertedTo(VecVersion.VEC12X)).isTrue();
        assertThat(VecVersion.VEC11X.canBeConvertedTo(VecVersion.VEC2XX)).isFalse();
    }

    private List<String> vecVersionLiterals(final String schemaResource) throws Exception {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        final Document schema;
        try (final InputStream inputStream = VecVersionTest.class.getResourceAsStream(schemaResource)) {
            assertThat(inputStream).as("schema %s on the classpath", schemaResource).isNotNull();
            schema = factory.newDocumentBuilder().parse(inputStream);
        }

        final NodeList simpleTypes = schema.getElementsByTagNameNS(XS_NAMESPACE, "simpleType");
        for (int i = 0; i < simpleTypes.getLength(); i++) {
            final Element simpleType = (Element) simpleTypes.item(i);
            if ("VecVersion".equals(simpleType.getAttribute("name"))) {
                final NodeList enumerations = simpleType.getElementsByTagNameNS(XS_NAMESPACE, "enumeration");
                final List<String> literals = new ArrayList<>();
                for (int j = 0; j < enumerations.getLength(); j++) {
                    literals.add(((Element) enumerations.item(j)).getAttribute("value"));
                }
                return literals;
            }
        }
        throw new AssertionError("No simple type VecVersion in " + schemaResource);
    }

}
