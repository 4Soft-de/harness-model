/*-
 * ========================LICENSE_START=================================
 * NavExt XJC Plugin
 * %%
 * Copyright (C) 2019 - 2026 4Soft GmbH
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
package com.foursoft.harness.navext.xjc.plugin.openenum;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Hand-maintained constant names for literals whose generated name would be unreadable or would
 * collide with the name of another literal of the same open enumeration.
 *
 * <p>
 * The file is looked up next to the literal schema and has the form:
 * </p>
 *
 * <pre>{@code
 * <open-enum-names>
 *     <type name="TerminalBoltNominalSize">
 *         <literal value="#1" name="NO_1"/>
 *     </type>
 * </open-enum-names>
 * }</pre>
 *
 * <p>
 * {@code type/@namespace} is optional; without it the type is matched by local name alone.
 * </p>
 * <p>
 * Every entry must be used by the generation, see {@link #unusedEntries()}. The file exists so that
 * a published constant name does not change by accident, and an entry that matches no literal - a
 * typo in the value, a wrong type name - would defeat that purpose silently: the default naming
 * would take over and the constant would change with nothing to notice it.
 * </p>
 */
final class ConstantNameOverrides {

    /** Keyed by type name, then by literal value. */
    private final Map<QName, Map<String, String>> overrides;

    /** The (type, value) pairs {@link #constantNameFor} has resolved so far. */
    private final Set<Map.Entry<QName, String>> used = new HashSet<>();

    private ConstantNameOverrides(final Map<QName, Map<String, String>> overrides) {
        this.overrides = overrides;
    }

    static ConstantNameOverrides none() {
        return new ConstantNameOverrides(Map.of());
    }

    /**
     * @param uri The location of the override file.
     * @return The parsed overrides.
     * @throws SAXException If the file cannot be read or is malformed.
     */
    static ConstantNameOverrides read(final String uri) throws SAXException {
        final Document document = parse(uri);

        final Map<QName, Map<String, String>> overrides = new LinkedHashMap<>();
        final NodeList types = document.getDocumentElement()
                .getElementsByTagName("type");
        for (int i = 0; i < types.getLength(); i++) {
            final Element type = (Element) types.item(i);
            final QName typeName = new QName(type.getAttribute("namespace"), type.getAttribute("name"));

            final Map<String, String> literals = overrides.computeIfAbsent(typeName, name -> new LinkedHashMap<>());
            final NodeList literalNodes = type.getElementsByTagName("literal");
            for (int j = 0; j < literalNodes.getLength(); j++) {
                final Element literal = (Element) literalNodes.item(j);
                literals.put(literal.getAttribute("value"), literal.getAttribute("name"));
            }
        }
        return new ConstantNameOverrides(overrides);
    }

    /**
     * @param typeName The type the literal belongs to.
     * @param value    The literal.
     * @return The overridden constant name, or {@code null} if there is none.
     */
    String constantNameFor(final QName typeName, final String value) {
        final QName localName = new QName(XMLConstants.NULL_NS_URI, typeName.getLocalPart());
        final Map<String, String> byLocalName = overrides.get(localName);
        if (byLocalName != null && byLocalName.containsKey(value)) {
            used.add(Map.entry(localName, value));
            return byLocalName.get(value);
        }
        final Map<String, String> byQualifiedName = overrides.get(typeName);
        if (byQualifiedName != null && byQualifiedName.containsKey(value)) {
            used.add(Map.entry(typeName, value));
            return byQualifiedName.get(value);
        }
        return null;
    }

    /**
     * @return The entries no call of {@link #constantNameFor} has resolved so far, as
     * {@code type/value} in file order, for reporting.
     */
    List<String> unusedEntries() {
        final List<String> unused = new ArrayList<>();
        for (final Map.Entry<QName, Map<String, String>> type : overrides.entrySet()) {
            for (final String value : type.getValue()
                    .keySet()) {
                if (!used.contains(Map.entry(type.getKey(), value))) {
                    unused.add(type.getKey() + "/" + value);
                }
            }
        }
        return unused;
    }

    private static Document parse(final String uri) throws SAXException {
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            return factory.newDocumentBuilder()
                    .parse(uri);
        } catch (final ParserConfigurationException | IOException e) {
            throw new SAXException("Could not read the open enumeration constant name overrides " + uri + ".", e);
        }
    }

}
