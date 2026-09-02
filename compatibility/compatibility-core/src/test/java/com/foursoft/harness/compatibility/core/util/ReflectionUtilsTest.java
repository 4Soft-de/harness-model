/*-
 * ========================LICENSE_START=================================
 * Compatibility Core
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
package com.foursoft.harness.compatibility.core.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionUtilsTest {

    @Test
    void setsAPrivateField() throws Exception {
        final Bean bean = new Bean();

        ReflectionUtils.setFieldValue(bean, field("value"), "written");

        assertThat(bean.getValue()).isEqualTo("written");
    }

    @Test
    void setsTheSameFieldRepeatedly() throws Exception {
        // The field is opened up once and stays writable; a second write must not fail on the
        // accessibility check either.
        final Bean bean = new Bean();
        final Field field = field("value");

        ReflectionUtils.setFieldValue(bean, field, "first");
        ReflectionUtils.setFieldValue(bean, field, "second");

        assertThat(bean.getValue()).isEqualTo("second");
    }

    @Test
    void setsAFieldToNull() throws Exception {
        final Bean bean = new Bean();
        ReflectionUtils.setFieldValue(bean, field("value"), "written");

        ReflectionUtils.setFieldValue(bean, field("value"), null);

        assertThat(bean.getValue()).isNull();
    }

    @Test
    void setsTheParentRelationshipByFieldName() {
        final Bean bean = new Bean();
        final Bean parent = new Bean();

        ReflectionUtils.setParentRelationship(bean, "parent", parent);

        assertThat(bean.getParent()).isSameAs(parent);
    }

    private Field field(final String name) throws NoSuchFieldException {
        return Bean.class.getDeclaredField(name);
    }

    private static class Bean {

        private String value;
        private Bean parent;

        String getValue() {
            return value;
        }

        Bean getParent() {
            return parent;
        }

    }

}
