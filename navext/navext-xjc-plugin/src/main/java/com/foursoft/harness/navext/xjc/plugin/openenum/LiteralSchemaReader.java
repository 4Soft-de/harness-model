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

import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSRestrictionSimpleType;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.parser.XSOMParser;
import com.sun.xml.xsom.util.DomAnnotationParserFactory;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Reads the enumeration literals of a <i>literal schema</i>.
 *
 * <p>
 * Open enumerations declare their literals only in the schema used for strict validation; the schema
 * used for code generation declares the same simple types without any {@code xs:enumeration} facet,
 * which is why XJC maps them to {@link String}. This reader parses the literal schema so that the
 * literals can be generated as constants.
 * </p>
 */
final class LiteralSchemaReader {

    private LiteralSchemaReader() {
        throw new AssertionError("LiteralSchemaReader must not be instantiated.");
    }

    /**
     * @param schemaUri The system id of the literal schema.
     * @return All global simple types of that schema that declare at least one enumeration facet,
     * keyed by their qualified name.
     * @throws SAXException If the schema cannot be parsed.
     */
    static Map<QName, OpenEnumDefinition> read(final String schemaUri) throws SAXException {
        final XSSchemaSet schemaSet = parse(schemaUri);

        final Map<QName, OpenEnumDefinition> definitions = new HashMap<>();
        final Iterator<XSSimpleType> simpleTypes = schemaSet.iterateSimpleTypes();
        while (simpleTypes.hasNext()) {
            final XSSimpleType simpleType = simpleTypes.next();
            final List<OpenEnumDefinition.Literal> literals =
                    simpleType.isGlobal() ? literalsOf(simpleType) : List.of();
            if (!literals.isEmpty()) {
                final QName typeName = new QName(simpleType.getTargetNamespace(), simpleType.getName());
                definitions.put(typeName,
                                new OpenEnumDefinition(typeName, SchemaDocumentation.of(simpleType), literals));
            }
        }
        return definitions;
    }

    private static XSSchemaSet parse(final String schemaUri) throws SAXException {
        final SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);

        final XSOMParser parser = new XSOMParser(parserFactory);
        // Without a DOM annotation parser XSOM drops all annotations, and with them the
        // documentation that becomes the Javadoc of the generated constants.
        parser.setAnnotationParser(new DomAnnotationParserFactory());
        parser.parse(schemaUri);

        final XSSchemaSet schemaSet = parser.getResult();
        if (schemaSet == null) {
            throw new SAXException("Could not parse the open enumeration literal schema " + schemaUri + ".");
        }
        return schemaSet;
    }

    private static List<OpenEnumDefinition.Literal> literalsOf(final XSSimpleType simpleType) {
        final XSRestrictionSimpleType restriction = simpleType.asRestriction();
        if (restriction == null) {
            return List.of();
        }

        // XSOM does not preserve the order the facets are declared in, but that order decides the
        // ordinals of the generated constants, so it is restored from the source locations.
        final List<XSFacet> facets = new ArrayList<>(restriction.getDeclaredFacets(XSFacet.FACET_ENUMERATION));
        facets.sort(Comparator.comparingInt(LiteralSchemaReader::lineOf)
                            .thenComparingInt(LiteralSchemaReader::columnOf));

        final List<OpenEnumDefinition.Literal> literals = new ArrayList<>();
        for (final XSFacet facet : facets) {
            literals.add(new OpenEnumDefinition.Literal(facet.getValue().value, SchemaDocumentation.of(facet)));
        }
        return literals;
    }

    private static int lineOf(final XSFacet facet) {
        return facet.getLocator() == null ? 0 : facet.getLocator()
                .getLineNumber();
    }

    private static int columnOf(final XSFacet facet) {
        return facet.getLocator() == null ? 0 : facet.getLocator()
                .getColumnNumber();
    }

}
