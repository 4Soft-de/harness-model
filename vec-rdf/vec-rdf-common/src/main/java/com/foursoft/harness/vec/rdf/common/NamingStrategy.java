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

    public static String modelName(Class<?> clazz) {
        if (clazz.getName().endsWith(".VecContent")) {
            return clazz.getSimpleName();
        }
        return clazz.getSimpleName()
                .replaceFirst("Vec", "");
    }

    public static String modelName(Field field) {
        XmlElement annotation = field.getAnnotation(XmlElement.class);
        return StringUtils.uncapitalize(annotation.name());
    }

    public String rdfName(Class<?> clazz) {
        return modelName(clazz);
    }

    public String rdfName(Field field) {
        return StringUtils.uncapitalize(rdfName(field.getDeclaringClass())) + StringUtils.capitalize(modelName(field));
    }

    public String rdfName(Identifiable identifiable) {
        return rdfName(identifiable.getClass()) + "-" + identifiable.getXmlId();
    }

    public String rdfName(Enum<?> literalValue) {
        try {
            Class<?> enumClass = literalValue.getClass();
            String enumLiteralValue = (String) enumClass.getMethod("value")
                    .invoke(literalValue);
            return rdfName(enumClass) + "_" + URLEncoder.encode(enumLiteralValue, StandardCharsets.UTF_8);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new VecRdfException("Can not create URI for literal value: " + literalValue.name(), e);
        }
    }

    public String rdfLabel(Identifiable identifiable) {
        return modelName(identifiable.getClass()) + "[" + identifiable.getXmlId() + "]";
    }

    public String uriFor(Class<?> clazz) {
        return VEC.URI + rdfName(clazz);
    }

    public String uriFor(Field field) {
        return VEC.URI + rdfName(field);
    }

    public String uriFor(String namespace, Identifiable identifiable) {
        return namespace + rdfName(identifiable);
    }

    public String uriFor(Enum<?> literalValue) {
        return VEC.URI + rdfName(literalValue);
    }

    public String debugUri(String localName) {
        return VEC.DEBUG_NS + localName;
    }

    public String rdfNameForOpenEnumLiteral(UmlType enumType, String literalValue) {
        return enumType.name() + "_" + URLEncoder.encode(literalValue, StandardCharsets.UTF_8)
                .replace("+", "%20");
    }

    public String uriFor(UmlType modelType) {
        return VEC.URI + modelType.name();
    }

    public String uriForWrapper(UmlType modelType) {
        return VEC.URI + rdfNameForWrapper(modelType);
    }

    public String uriForWrapperItemProperty(UmlType modelType) {
        return VEC.URI + StringUtils.uncapitalize(rdfNameForWrapper(modelType)) + "Item";
    }

    private static String rdfNameForWrapper(UmlType modelType) {
        return modelType.name() + "Wrapper";
    }

    public String uriForOpenEnumLiteral(UmlType enumType, String enumLiteralValue, String instanceNamespace) {
        // We have an OpenEnumeration...
        String resourceName = rdfNameForOpenEnumLiteral(enumType, enumLiteralValue);
        if (hasDefinedLiteral(enumType, enumLiteralValue)) {
            return VEC.URI + resourceName;
        }
        return instanceNamespace + resourceName;
    }

    private boolean hasDefinedLiteral(UmlType enumType, String literal) {
        return Optional.ofNullable(enumType)
                .map(t -> Arrays.stream(t.literals())
                        .map(UmlLiteral::name)
                        .anyMatch(l -> StringUtils.capitalize(l)
                                .equals(literal)))
                .orElseThrow();
    }

}
