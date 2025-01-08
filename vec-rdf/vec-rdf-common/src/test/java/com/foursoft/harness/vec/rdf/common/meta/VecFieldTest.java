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

import com.foursoft.harness.vec.v2x.VecCustomProperty;
import com.foursoft.harness.vec.v2x.VecExtendableElement;
import com.foursoft.harness.vec.v2x.VecPartOrUsageRelatedSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VecFieldTest {

    @Test
    void should_return_true_on_xmlid() throws NoSuchFieldException {
        final VecField vecField = new VecField(VecExtendableElement.class.getDeclaredField("xmlId"));

        assertThat(vecField.isXmlId()).isTrue();
    }

    @Test
    void should_return_false_on_xmlid() throws NoSuchFieldException {
        final VecField vecField = new VecField(VecExtendableElement.class.getDeclaredField("customProperties"));

        assertThat(vecField.isXmlId()).isFalse();
    }

    @Test
    void should_return_true_on_transient() throws NoSuchFieldException {
        final VecField vecField = new VecField(VecExtendableElement.class.getDeclaredField("refExternalMapping"));

        assertThat(vecField.isTransient()).isTrue();
    }

    @Test
    void should_return_false_on_transient() throws NoSuchFieldException {
        final VecField vecField = new VecField(VecExtendableElement.class.getDeclaredField("customProperties"));

        assertThat(vecField.isTransient()).isFalse();
    }

    @Test
    void should_return_true_on_reference() throws NoSuchFieldException {
        final VecField vecField = new VecField(
                VecPartOrUsageRelatedSpecification.class.getDeclaredField("describedPart"));

        assertThat(vecField.isReference()).isTrue();
    }

    @Test
    void should_return_false_on_reference() throws NoSuchFieldException {
        final VecField vecField = new VecField(VecExtendableElement.class.getDeclaredField("customProperties"));

        assertThat(vecField.isReference()).isFalse();
    }

    @Test
    void should_return_true_on_collection() throws NoSuchFieldException {
        final VecField vecField = new VecField(
                VecPartOrUsageRelatedSpecification.class.getDeclaredField("describedPart"));

        assertThat(vecField.isCollection()).isTrue();
    }

    @Test
    void should_return_false_on_collection() throws NoSuchFieldException {
        final VecField vecField = new VecField(
                VecPartOrUsageRelatedSpecification.class.getDeclaredField("specialPartType"));

        assertThat(vecField.isCollection()).isFalse();
    }

    @Test
    void should_return_collection_items_type() throws NoSuchFieldException {
        final VecField vecField = new VecField(
                VecExtendableElement.class.getDeclaredField("customProperties"));

        assertThat(vecField.getValueType()).isEqualTo(VecCustomProperty.class);
    }

    @Test
    void should_return_field_type_for_none_collections() throws NoSuchFieldException {
        final VecField vecField = new VecField(
                VecPartOrUsageRelatedSpecification.class.getDeclaredField("specialPartType"));

        assertThat(vecField.getValueType()).isEqualTo(String.class);
    }

}
