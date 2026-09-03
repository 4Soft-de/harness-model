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

import com.foursoft.harness.compatibility.core.exception.WrapperException;
import com.foursoft.harness.compatibility.core.wrapper.fixture.xmlfields.FailingGetterBean;
import com.foursoft.harness.compatibility.core.wrapper.fixture.xmlfields.XmlFieldsBean;
import com.foursoft.harness.compatibility.core.wrapper.fixture.xmlfields.XmlFieldsSubBean;
import com.foursoft.harness.compatibility.core.wrapper.fixture.xmlfields.XmlFieldsVisitor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * {@link XmlFieldInitializer} resolves the field / getter pairs of a class once and caches them. These tests
 * pin down what it writes back, so that the caching cannot silently change it.
 */
class XmlFieldInitializerTest {

    @Test
    void writesTheGetterValueIntoTheField() {
        final XmlFieldsBean bean = new XmlFieldsBean();
        bean.getItems().add("first");

        XmlFieldInitializer.initializeFields(bean);

        assertThat(bean.rawItems()).containsExactly("first");
    }

    @Test
    void resetsAnEmptyCollectionToNullSoItIsNotWrittenOut() {
        final XmlFieldsBean bean = new XmlFieldsBean();
        // The getter creates the list on access, which is exactly what has to be undone again.
        assertThat(bean.getItems()).isEmpty();
        assertThat(bean.rawItems()).isNotNull();

        XmlFieldInitializer.initializeFields(bean);

        assertThat(bean.rawItems()).isNull();
    }

    @Test
    void handlesIsGettersAndXmlIdFieldsAsWellAsPlainXmlElements() {
        final XmlFieldsBean bean = new XmlFieldsBean();
        bean.setIdentification("id-1");
        bean.setDescription("a description");
        bean.setFlag(Boolean.TRUE);

        XmlFieldInitializer.initializeFields(bean);

        assertThat(bean.getIdentification()).isEqualTo("id-1");
        assertThat(bean.getDescription()).isEqualTo("a description");
        assertThat(bean.isFlag()).isTrue();
    }

    @Test
    void leavesFieldsWithoutAJaxbAnnotationAlone() {
        final XmlFieldsBean bean = new XmlFieldsBean();
        bean.setNotAnnotated("untouched");

        XmlFieldInitializer.initializeFields(bean);

        assertThat(bean.rawNotAnnotated()).isEqualTo("untouched");
    }

    @Test
    void handlesInheritedFieldsAsWellAsDeclaredOnes() {
        final XmlFieldsSubBean bean = new XmlFieldsSubBean();
        bean.getExtras().add("extra");
        assertThat(bean.getItems()).isEmpty();

        XmlFieldInitializer.initializeFields(bean);

        assertThat(bean.rawExtras()).containsExactly("extra");
        assertThat(bean.rawItems()).isNull();
    }

    @Test
    void isIdempotentSoItCanBeCalledBeforeEveryWrite() {
        final XmlFieldsBean bean = new XmlFieldsBean();
        bean.getItems().add("first");
        bean.setDescription("a description");

        XmlFieldInitializer.initializeFields(bean);
        final List<String> afterFirst = bean.rawItems();
        XmlFieldInitializer.initializeFields(bean);

        assertThat(bean.rawItems()).isSameAs(afterFirst).containsExactly("first");
        assertThat(bean.getDescription()).isEqualTo("a description");
    }

    @Test
    void reportsTheFieldItCouldNotSetInsteadOfFailingOnTheAnnotationLookup() {
        // The field carries no XmlElement annotation, which the error message used to read unconditionally.
        assertThatThrownBy(() -> XmlFieldInitializer.initializeFields(new FailingGetterBean()))
                .isInstanceOf(WrapperException.class)
                .hasMessageContaining("identification")
                .hasMessageContaining(FailingGetterBean.class.getName());
    }

    @Test
    void visitorProxyInitializesTheVisitedBean() {
        final XmlFieldsVisitor visitor = XmlFieldInitializer.visitorProxy(XmlFieldsVisitor.class);
        final XmlFieldsBean bean = new XmlFieldsBean();
        assertThat(bean.getItems()).isEmpty();

        visitor.visitBean(bean);

        assertThat(bean.rawItems()).isNull();
    }

    @Test
    void visitorProxyIsSharedAndToleratesArgumentLessMethods() {
        final XmlFieldsVisitor first = XmlFieldInitializer.visitorProxy(XmlFieldsVisitor.class);
        final XmlFieldsVisitor second = XmlFieldInitializer.visitorProxy(XmlFieldsVisitor.class);

        assertThat(first).isSameAs(second);
        assertThat(first.getClass()).isSameAs(second.getClass());
        first.visitNothing();
    }

}
