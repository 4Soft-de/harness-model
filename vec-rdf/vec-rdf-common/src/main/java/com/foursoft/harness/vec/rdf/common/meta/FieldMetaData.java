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
package com.foursoft.harness.vec.rdf.common.meta;

import com.foursoft.harness.vec.rdf.common.meta.xmi.UmlField;
import com.foursoft.harness.vec.rdf.common.meta.xmi.UmlType;
import com.foursoft.harness.vec.rdf.common.meta.xmi.VecModelProvider;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Optional;

public class FieldMetaData {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldMetaData.class);

    private final Field field;
    private final Optional<UmlField> modelField;

    FieldMetaData(Field field, VecModelProvider fieldDataLocator) {
        this.field = field;
        this.modelField = fieldDataLocator.findField(field);
        if (this.modelField.isEmpty()) {
            LOGGER.warn(
                    "Encountered field without corresponding UML model element. Maybe model & VEC Api versions are " +
                            "not matching [{}.{}].",
                    field.getDeclaringClass()
                            .getSimpleName(), field.getName());
        }
    }

    public Field getField() {
        return field;
    }

    public Class<?> getType() {
        return field.getType();
    }

    public boolean isEnumField() {
        return modelField.map(UmlField::type)
                .map(UmlType::isEnum)
                .orElse(false);
    }

    public boolean isOrdered() {
        return modelField.map(UmlField::isOrdered)
                .orElse(false);
    }

    public boolean isUnique() {
        return modelField.map(UmlField::isUnique)
                .orElse(false);
    }

    public boolean isAssociation() {
        return modelField.map(UmlField::isAssociation)
                .orElse(false);
    }

    public UmlType getModelType() {
        return modelField.map(UmlField::type)
                .orElseThrow();
    }

    public Object readValue(Object target) {
        try {
            return FieldUtils.readField(field, target, true);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to read field value.", e);
        }
    }
}
