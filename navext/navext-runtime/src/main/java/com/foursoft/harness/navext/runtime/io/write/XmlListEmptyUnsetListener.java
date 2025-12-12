/*-
 * ========================LICENSE_START=================================
 * NavExt Runtime
 * %%
 * Copyright (C) 2019 - 2025 4Soft GmbH
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
package com.foursoft.harness.navext.runtime.io.write;

import com.foursoft.harness.navext.runtime.JaxbModelException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlList;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class XmlListEmptyUnsetListener extends Marshaller.Listener {

    private static final Map<Class<?>, List<Field>> XML_LIST_FIELDS_CACHE = new ConcurrentHashMap<>();

    @Override
    public void beforeMarshal(final Object source) {
        if (source == null) {
            return;
        }
        for (final Field field : xmlListFields(source.getClass())) {
            try {
                final Object value = field.get(source);
                if (value instanceof final List<?> list && list.isEmpty()) {
                    // Set to null so the element is omitted rather than marshalled as <X/>
                    field.set(source, null);
                }
            } catch (final IllegalAccessException e) {
                throw new JaxbModelException(
                        "Cannot access field " + field + " on " + source.getClass() + " to unset empty collections", e);
            }
        }
    }

    private static List<Field> xmlListFields(final Class<?> type) {
        return XML_LIST_FIELDS_CACHE.computeIfAbsent(type, XmlListEmptyUnsetListener::scanXmlListFields);
    }

    private static List<Field> scanXmlListFields(final Class<?> type) {
        return classHierarchy(type)
                .flatMap(c -> Arrays.stream(c.getDeclaredFields()))
                .filter(XmlListEmptyUnsetListener::isListType)
                .filter(XmlListEmptyUnsetListener::isAnnotatedWithXmlList)
                .map(XmlListEmptyUnsetListener::makeAccessible)
                .toList();

    }

    private static Stream<Class<?>> classHierarchy(final Class<?> type) {
        return Stream.iterate(
                type,
                c -> c != null && c != Object.class,
                Class::getSuperclass
        );
    }

    private static boolean isAnnotatedWithXmlList(final Field f) {
        return f.getAnnotation(XmlList.class) != null;
    }

    private static boolean isListType(final Field f) {
        return List.class.isAssignableFrom(f.getType());
    }

    private static Field makeAccessible(final Field f) {
        f.setAccessible(true);
        return f;
    }
}


