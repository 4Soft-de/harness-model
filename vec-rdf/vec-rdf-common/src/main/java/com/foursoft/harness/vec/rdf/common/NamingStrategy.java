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
package com.foursoft.harness.vec.rdf.common;

import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.rdf.common.exception.VecRdfException;
import com.foursoft.harness.vec.rdf.common.meta.xmi.UmlLiteral;
import com.foursoft.harness.vec.rdf.common.meta.xmi.UmlType;
import jakarta.xml.bind.annotation.XmlElement;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

public class NamingStrategy {

    public static String modelName(final Class<?> clazz) {
        if (clazz.getName().endsWith(".VecContent")) {
            return clazz.getSimpleName();
        }
        return clazz.getSimpleName()
                .replaceFirst("Vec", "");
    }

    public static String modelName(final Field field) {
        final XmlElement annotation = field.getAnnotation(XmlElement.class);
        return StringUtils.uncapitalize(annotation.name());
    }

    public String rdfName(final Class<?> clazz) {
        return modelName(clazz);
    }

    public String rdfName(final Field field) {
        return StringUtils.uncapitalize(rdfName(field.getDeclaringClass())) + StringUtils.capitalize(modelName(field));
    }

    public String rdfName(final Identifiable identifiable) {
        return rdfName(identifiable.getClass()) + "-" + identifiable.getXmlId();
    }

    public String rdfName(final Enum<?> literalValue) {
        try {
            final Class<?> enumClass = literalValue.getClass();
            final String enumLiteralValue = (String) enumClass.getMethod("value")
                    .invoke(literalValue);
            return rdfName(enumClass) + "_" + URLEncoder.encode(enumLiteralValue, StandardCharsets.UTF_8);
        } catch (final IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new VecRdfException("Can not create URI for literal value: " + literalValue.name(), e);
        }
    }

    public String rdfLabel(final Identifiable identifiable) {
        return modelName(identifiable.getClass()) + "[" + identifiable.getXmlId() + "]";
    }

    public String uriFor(final Class<?> clazz) {
        return VEC.NS + rdfName(clazz);
    }

    public String uriFor(final Field field) {
        return VEC.NS + rdfName(field);
    }

    public String uriFor(final String namespace, final Identifiable identifiable) {
        return namespace + rdfName(identifiable);
    }

    public String uriFor(final Enum<?> literalValue) {
        return VEC.NS + rdfName(literalValue);
    }

    public String debugUri(final String localName) {
        return VecNsUtilities.DEBUG_NS + localName;
    }

    public String rdfNameForOpenEnumLiteral(final UmlType enumType, final String literalValue) {
        return enumType.name() + "_" + URLEncoder.encode(literalValue, StandardCharsets.UTF_8)
                .replace("+", "%20");
    }

    public String uriFor(final UmlType modelType) {
        return VEC.NS + modelType.name();
    }

    public String uriForWrapper(final UmlType modelType) {
        return VEC.NS + rdfNameForWrapper(modelType);
    }

    public String uriForWrapperItemProperty(final UmlType modelType) {
        return VEC.NS + StringUtils.uncapitalize(rdfNameForWrapper(modelType)) + "Item";
    }

    private static String rdfNameForWrapper(final UmlType modelType) {
        return modelType.name() + "Wrapper";
    }

    public String uriForOpenEnumLiteral(final UmlType enumType, final String enumLiteralValue,
                                        final String instanceNamespace) {
        // We have an OpenEnumeration...
        final String resourceName = rdfNameForOpenEnumLiteral(enumType, enumLiteralValue);
        if (hasDefinedLiteral(enumType, enumLiteralValue)) {
            return VEC.NS + resourceName;
        }
        return instanceNamespace + resourceName;
    }

    private boolean hasDefinedLiteral(final UmlType enumType, final String literal) {
        return Optional.ofNullable(enumType)
                .map(t -> Arrays.stream(t.literals())
                        .map(UmlLiteral::name)
                        .anyMatch(l -> StringUtils.capitalize(l)
                                .equals(literal)))
                .orElseThrow();
    }

}
