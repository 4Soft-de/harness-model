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

import com.foursoft.harness.vec.rdf.common.meta.VecClass;
import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VersionLookupModelProviderTest {

    @Test
    void should_return_correct_metadata() {
        final UmlModelProvider service = new VersionLookupModelProvider();

        final VecClass chsMetaData = VecClass.analyzeClass(VecConnectorHousingSpecification.class);
        final VecClass numValMetaData = VecClass.analyzeClass(VecNumericalValue.class);

        assertThat(chsMetaData).isNotNull();

        assertThat(chsMetaData.getType()).isEqualTo(VecConnectorHousingSpecification.class);

        assertThat(chsMetaData.getField("connectorPositionAssuranceType")).isNotNull().satisfies(cpa -> {
            assertThat(service.findField(cpa.getField())).isNotNull().satisfies(umlField -> {
                assertThat(umlField)
                        .returns(true, UmlField::isEnumField);
            });
        });

        assertThat(numValMetaData.getField("valueComponent")).isNotNull().satisfies(f -> {
            assertThat(f.getField().getName()).isEqualTo("valueComponent");
            assertThat(f.getValueType()).isEqualTo(double.class);
            assertThat(service.findField(f.getField())).isNotNull().satisfies(umlField -> {
                assertThat(umlField.type()).isNotNull();
                assertThat(umlField.isAssociation()).isFalse();
                assertThat(umlField.isOrdered()).isFalse();
                assertThat(umlField.isUnique()).isTrue();
                assertThat(umlField.type().isPrimitive()).isTrue();
                assertThat(umlField.isEnumField()).isFalse();

            });
        });
    }

}
