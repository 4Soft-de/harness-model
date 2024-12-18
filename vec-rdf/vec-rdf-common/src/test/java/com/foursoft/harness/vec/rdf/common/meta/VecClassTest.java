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

import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import org.junit.jupiter.api.Test;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import static org.assertj.core.api.Assertions.assertThat;

class VecClassTest {

    @Test
    void should_return_all_fields() {
        final VecField[] connectorHousingFields = VecClass.analyzeClass(VecConnectorHousingSpecification.class)
                .getFields();

        assertThat(connectorHousingFields).hasSize(17);
    }

    @Test
    void should_return_a_field() throws IntrospectionException {
        final PropertyDescriptor propertyDescriptor = new PropertyDescriptor("identification",
                                                                             VecConnectorHousingSpecification.class);

        assertThat(
                VecClass.analyzeClass(VecConnectorHousingSpecification.class).getField(propertyDescriptor)).isNotNull();
    }

    @Test
    void should_return_correct_metadata() {
        final VecClass numValMetaData = VecClass.analyzeClass(VecNumericalValue.class);

        assertThat(numValMetaData).isNotNull();

        assertThat(numValMetaData.getType()).isEqualTo(VecNumericalValue.class);
        assertThat(numValMetaData.getFields()).hasSize(4);

        assertThat(numValMetaData.getFields()[0]).satisfies(f -> {
            assertThat(f.getField().getName()).isEqualTo("valueComponent");
            assertThat(f.getValueType()).isEqualTo(double.class);
            assertThat(f.isCollection()).isFalse();
        });
    }

}
