/*-
 * ========================LICENSE_START=================================
 * VEC 1.2.X
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
package com.foursoft.harness.vec.v12x;

import jakarta.xml.bind.annotation.XmlTransient;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The back-reference sets are the most numerous objects in a large VEC document - a complete vehicle export holds
 * millions of them, and the vast majority stay empty. They are therefore created on first access instead of in the
 * constructor.
 */
class BackReferenceInitializationTest {

    @Test
    void backReferenceSetsAreNotAllocatedByTheConstructor() {
        final VecPartVersion partVersion = new VecPartVersion();

        assertThat(backReferenceFields(VecPartVersion.class))
                .isNotEmpty()
                .allSatisfy(field -> assertThat(readField(field, partVersion))
                        .as("back-reference set '%s' has to be created on first access, not in the constructor",
                            field.getName())
                        .isNull());
    }

    @Test
    void theGetterCreatesAStableSet() {
        final VecPartVersion partVersion = new VecPartVersion();

        final Set<VecDocumentVersion> refDocumentVersion = partVersion.getRefDocumentVersion();

        assertThat(refDocumentVersion)
                // The unmarshalling adds to the set the getter returned, so a fresh set per call would drop
                // back-references.
                .isSameAs(partVersion.getRefDocumentVersion())
                .isEmpty();
    }

    private List<Field> backReferenceFields(final Class<?> type) {
        return Stream.<Class<?>>iterate(type, c -> c != null && c != Object.class, Class::getSuperclass)
                .flatMap(c -> Arrays.stream(c.getDeclaredFields()))
                .filter(field -> Set.class.isAssignableFrom(field.getType()))
                .filter(field -> field.isAnnotationPresent(XmlTransient.class))
                .toList();
    }

    private Object readField(final Field field, final Object target) {
        try {
            field.setAccessible(true);
            return field.get(target);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException("Cannot read " + field, e);
        }
    }

}
