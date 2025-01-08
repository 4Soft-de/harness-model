/*-
 * ========================LICENSE_START=================================
 * VEC RDF Common
 * %%
 * Copyright (C) 2024 4Soft GmbH
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
package com.foursoft.harness.vec.rdf.common.meta.xmi;

import net.sf.saxon.xpath.XPathFactoryImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class XmiModelProviderBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            XmiModelProviderBuilder.class);
    private static final String UML_TYPES_SEARCH_XPATH =
            "xmi:XMI/uml:Model/packagedElement[@name='VEC']//packagedElement[(@xmi:type='uml:Class' or " +
                    "@xmi:type='uml:Enumeration' or @xmi:type='uml:PrimitiveType')and not" +
                    "(@xmi:id=/xmi:XMI/MagicDraw_Profile:Legend/@base_Class)]";
    public static final String XP_OWNED_ATTRIBUTE = "ownedAttribute";
    public static final String XP_NAME = "@name";
    public static final String XP_XMI_TYPE = "@xmi:type";
    public static final String UML_CLASS = "uml:Class";
    public static final String UML_ENUMERATION = "uml:Enumeration";
    public static final String UML_PRIMITIVE = "uml:PrimitiveType";
    public static final String XP_XMI_ID = "@xmi:id";
    public static final String XP_TYPE = "@type";

    public static final String XP_IS_ORDERED = "@isOrdered";

    public static final String XP_IS_UNIQUE = "@isUnique";

    public static final String XP_IS_ASSOCIATION = "exists(@association) and not(@aggregation='composite')";

    public static final String XP_IS_ATTRIBUTE = "not(exists(@association))";

    public static final String XP_OWNED_LITERAL = "ownedLiteral";
    public static final String XP_TRUE = "true";
    public static final String XP_FALSE = "false";
    private final Document vecUmlModelDocument;
    private XPath xPath;
    private Map<String, UmlType> typeMap;

    public XmiModelProviderBuilder(final InputStream vecUmlModelInputStream) {
        this.vecUmlModelDocument = loadDocument(vecUmlModelInputStream);
    }

    /**
     * Internal delegate to make memory management more easy (memory consuming initialization is in this Builder
     * which will be collected after use. This makes the factory more "lightweight.
     */

    public UmlModelProvider build() {
        final XPathFactory xPathFactory = new XPathFactoryImpl();
        xPath = xPathFactory.newXPath();
        xPath.setNamespaceContext(new NamespaceResolver(vecUmlModelDocument));

        final NodeList classNodes = xpathList(UML_TYPES_SEARCH_XPATH, vecUmlModelDocument);

        final List<Element> classes = new ArrayList<>();

        typeMap = toStream(classNodes).map(x -> {
                    final UmlType typeInfo = this.toUmlType(x);
                    if (typeInfo.isClass()) {
                        classes.add(x);
                    }
                    return typeInfo;
                })
                .collect(Collectors.toMap(UmlType::xmiId, x -> x));

        final List<UmlField> fields = classes.stream()
                .flatMap(this::toUmlFields)
                .filter(Objects::nonNull)
                .toList();

        LOGGER.info("Found {} classes in UML model", classNodes.getLength());
        return new XmiUmlModelProvider(fields);
    }

    private Document loadDocument(final InputStream modelStream) {
        LOGGER.info("Loading VEC UML model for additional meta information");

        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setXIncludeAware(false);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            final DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(modelStream);
        } catch (final ParserConfigurationException | SAXException | IOException e) {
            throw new XmiException("Loading of XML / XMI document to extract additional meta data failed.", e);
        }
    }

    private UmlType toUmlType(final Element element) {
        final String name = xpath(XP_NAME, element);
        final String xmiType = xpath(XP_XMI_TYPE, element);
        final String xmiId = xpath(XP_XMI_ID, element);

        final UmlLiteral[] literals = toStream(xpathList(XP_OWNED_LITERAL, element)).map(this::toUmlLiteral)
                .toArray(UmlLiteral[]::new);

        return new UmlType(xmiId, name, xmiType, literals);
    }

    private Stream<UmlField> toUmlFields(final Element classElement) {
        final String xmiId = xpath(XP_XMI_ID, classElement);
        final UmlType classType = typeMap.get(xmiId);
        if (classType == null) {
            throw new XmiException("FATAL: Did not find class in second pass!");
        }

        return toStream(xpathList(XP_OWNED_ATTRIBUTE, classElement)).map(f -> toUmlField(f, classType));
    }

    private UmlLiteral toUmlLiteral(final Element literalElement) {
        return new UmlLiteral(xpath(XP_NAME, literalElement));
    }

    private UmlField toUmlField(final Element fieldElement, final UmlType classType) {
        final String fieldName = xpath(XP_NAME, fieldElement);
        if (StringUtils.isBlank(fieldName)) {
            return null;
        }
        final String type = xpath(XP_TYPE, fieldElement);
        final boolean isOrdered = xpath(XP_IS_ORDERED, fieldElement).equals(XP_TRUE);
        final boolean isUnique = !xpath(XP_IS_UNIQUE, fieldElement).equals(XP_FALSE);
        final boolean isAssociation = xpath(XP_IS_ASSOCIATION, fieldElement).equals(XP_TRUE);
        final boolean isAttribute = xpath(XP_IS_ATTRIBUTE, fieldElement).equals(XP_TRUE);

        final UmlType fieldType = typeMap.get(type);
        if (fieldType == null) {
            throw new XmiException(
                    "FATAL: Did not find type '" + type + "' in second pass! (" + fieldName + "+@id=" + xpath(
                            XP_XMI_ID, fieldElement) + ")");
        }

        return new UmlField(classType, fieldName, fieldType, isOrdered, isUnique, isAssociation, isAttribute);
    }

    private Stream<Element> toStream(final NodeList nodeList) {
        return IntStream.range(0, nodeList.getLength())
                .mapToObj(nodeList::item)
                .map(Element.class::cast);
    }

    private NodeList xpathList(final String expression, final Object context) {
        try {
            return (NodeList) xPath.evaluate(expression, context, XPathConstants.NODESET);
        } catch (final XPathExpressionException e) {
            throw new XmiException("Wrong XPATH.", e);
        }
    }

    private String xpath(final String expression, final Object context) {
        try {
            return xPath.evaluate(expression, context);
        } catch (final XPathExpressionException e) {
            throw new XmiException("Wrong XPATH.", e);
        }
    }
}


