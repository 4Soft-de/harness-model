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

import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSComponent;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;

/**
 * Extracts the {@code xs:documentation} of a schema component so that it can be used as Javadoc.
 * <p>
 * Requires the schema to have been parsed with a
 * {@link com.sun.xml.xsom.util.DomAnnotationParserFactory}; without it XSOM discards annotations.
 */
final class SchemaDocumentation {

    private SchemaDocumentation() {
        throw new AssertionError("SchemaDocumentation must not be instantiated.");
    }

    /**
     * @param component The component to read the documentation of.
     * @return The documentation, or {@code null} if the component has none.
     */
    static String of(final XSComponent component) {
        final XSAnnotation annotation = component.getAnnotation();
        if (annotation == null || !(annotation.getAnnotation() instanceof final Element element)) {
            return null;
        }

        final StringBuilder documentation = new StringBuilder();
        final NodeList nodes = element.getElementsByTagNameNS(XMLConstants.W3C_XML_SCHEMA_NS_URI, "documentation");
        for (int i = 0; i < nodes.getLength(); i++) {
            appendText(nodes.item(i), documentation);
        }

        final String result = documentation.toString()
                .strip();
        // The VEC documentation is HTML, which Javadoc renders as-is. Only a comment terminator
        // inside it would break the generated source.
        return result.isEmpty() ? null : result.replace("*/", "*&#47;");
    }

    private static void appendText(final Node node, final StringBuilder target) {
        final String text = node.getTextContent();
        if (text == null || text.isBlank()) {
            return;
        }
        if (!target.isEmpty()) {
            target.append(System.lineSeparator());
        }
        target.append(text.strip());
    }

}
