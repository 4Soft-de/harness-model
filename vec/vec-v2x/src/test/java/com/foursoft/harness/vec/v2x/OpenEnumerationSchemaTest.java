/*-
 * ========================LICENSE_START=================================
 * VEC 2.X
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
package com.foursoft.harness.vec.v2x;

import com.foursoft.harness.vec.common.openenum.OpenEnumLiteral;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pins the generated open enumerations against the schema they were generated from.
 *
 * <p>
 * The literals are part of the published API, but they are not written by hand: they come from the
 * strict schema, which is replaced wholesale whenever the VEC version is updated. This test makes a
 * literal that upstream added, removed or renamed show up as a failure here rather than as a silent
 * change of the API.
 * </p>
 */
class OpenEnumerationSchemaTest {

    private static final String STRICT_SCHEMA = "/vec2/vec_2.2.0-strict.xsd";
    private static final String CODEGEN_SCHEMA = "/vec2/vec_2.2.0.xsd";

    @Test
    void everyOpenEnumerationOfTheSchemaIsGeneratedWithExactlyItsLiterals() {
        final Map<String, List<String>> strictLiterals = literalsOf(STRICT_SCHEMA);
        final Map<String, List<String>> codegenLiterals = literalsOf(CODEGEN_SCHEMA);

        final Map<String, List<String>> missing = new TreeMap<>();
        final Map<String, List<String>> mismatched = new TreeMap<>();

        for (final Map.Entry<String, List<String>> entry : strictLiterals.entrySet()) {
            final String typeName = entry.getKey();
            // An open enumeration declares its literals in the strict schema only. A type that
            // declares them in both is a closed one, which XJC generates by itself.
            if (entry.getValue()
                    .isEmpty() || !codegenLiterals.getOrDefault(typeName, List.of())
                    .isEmpty()) {
                continue;
            }

            final List<String> generated = generatedLiteralsOf(typeName);
            if (generated == null) {
                missing.put(typeName, entry.getValue());
            } else if (!generated.equals(entry.getValue())) {
                mismatched.put(typeName, generated);
            }
        }

        assertThat(missing)
                .as("Open enumerations of %s without a generated enum", STRICT_SCHEMA)
                .isEmpty();
        assertThat(mismatched)
                .as("Generated enums whose literals or their order differ from %s", STRICT_SCHEMA)
                .isEmpty();
    }

    @Test
    void theKnownNumberOfOpenEnumerationsIsGenerated() {
        final Map<String, List<String>> strictLiterals = literalsOf(STRICT_SCHEMA);
        final Map<String, List<String>> codegenLiterals = literalsOf(CODEGEN_SCHEMA);

        final long openEnumerations = strictLiterals.entrySet()
                .stream()
                .filter(entry -> !entry.getValue()
                        .isEmpty())
                .filter(entry -> codegenLiterals.getOrDefault(entry.getKey(), List.of())
                        .isEmpty())
                .count();

        assertThat(openEnumerations)
                .as("VEC 2.2.0 open enumerations")
                .isEqualTo(109);
    }

    /**
     * @return The literals of the generated enum for the given schema type, or {@code null} if there
     * is no such enum.
     */
    private static List<String> generatedLiteralsOf(final String typeName) {
        final Class<?> literalEnum;
        try {
            literalEnum = Class.forName(VecContent.class.getPackageName() + ".Vec" + typeName);
        } catch (final ClassNotFoundException e) {
            return null;
        }
        if (!literalEnum.isEnum()) {
            return null;
        }

        final List<String> literals = new ArrayList<>();
        for (final Object constant : literalEnum.getEnumConstants()) {
            literals.add(((OpenEnumLiteral) constant).value());
        }
        return literals;
    }

    /**
     * @return The enumeration facets of every global simple type of the given schema, in document
     * order, keyed by the type name.
     */
    private static Map<String, List<String>> literalsOf(final String schemaResource) {
        final Element schema = parse(schemaResource).getDocumentElement();

        final Map<String, List<String>> literals = new LinkedHashMap<>();
        final NodeList simpleTypes = schema.getElementsByTagNameNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, "simpleType");
        for (int i = 0; i < simpleTypes.getLength(); i++) {
            final Element simpleType = (Element) simpleTypes.item(i);
            if (!schema.equals(simpleType.getParentNode())) {
                continue;
            }
            literals.put(simpleType.getAttribute("name"), enumerationsOf(simpleType));
        }
        return literals;
    }

    private static List<String> enumerationsOf(final Element simpleType) {
        final List<String> values = new ArrayList<>();
        final NodeList enumerations =
                simpleType.getElementsByTagNameNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, "enumeration");
        for (int i = 0; i < enumerations.getLength(); i++) {
            values.add(((Element) enumerations.item(i)).getAttribute("value"));
        }
        return values;
    }

    private static Document parse(final String schemaResource) {
        try (final InputStream schema = OpenEnumerationSchemaTest.class.getResourceAsStream(schemaResource)) {
            assertThat(schema)
                    .as("Schema %s must be on the class path", schemaResource)
                    .isNotNull();

            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            return factory.newDocumentBuilder()
                    .parse(schema);
        } catch (final Exception e) {
            throw new AssertionError("Could not read " + schemaResource + ".", e);
        }
    }

}
