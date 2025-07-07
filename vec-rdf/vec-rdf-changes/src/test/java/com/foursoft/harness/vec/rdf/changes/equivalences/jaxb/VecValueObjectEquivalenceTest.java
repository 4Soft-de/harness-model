/*-
 * ========================LICENSE_START=================================
 * VEC RDF Changesets (Experimental)
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
package com.foursoft.harness.vec.rdf.changes.equivalences.jaxb;

import com.foursoft.harness.vec.v2x.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VecValueObjectEquivalenceTest {

    private final VecValueObjectEquivalence equivalence = new VecValueObjectEquivalence();

    @Nested
    class Clazz {

        private Object a, b, c;

        @BeforeEach
        void setup() {
            a = new VecNumericalValue();
            b = new VecNumericalValueProperty();
            c = new VecNumericalValue();
        }

        @Test
        void should_return_false_on_different() {
            assertThat(equivalence.equivalent(a, b)).isFalse();
        }

        @Test
        void should_return_true_same() {
            assertThat(equivalence.equivalent(a, c)).isTrue();
        }
    }

    @Nested
    class Primitive {

        private VecNumericalValue a, b, c;

        @BeforeEach
        void setup() {
            a = new VecNumericalValue();
            a.setValueComponent(0.5);
            a.setXmlId("id_000037");
            b = new VecNumericalValue();
            b.setValueComponent(1.0);
            c = new VecNumericalValue();
            c.setValueComponent(0.5);
        }

        @Test
        void should_return_false_on_different() {
            assertThat(equivalence.equivalent(a, b)).isFalse();
        }

        @Test
        void should_return_true_same() {
            assertThat(equivalence.equivalent(a, c)).isTrue();
        }
    }

    @Nested
    class Reference {

        private VecNumericalValue a, b, c;
        private final VecUnit x = new VecSIUnit();
        private final VecUnit y = new VecSIUnit();

        @BeforeEach
        void setup() {
            x.setXmlId("x");
            y.setXmlId("y");
            a = new VecNumericalValue();
            a.setUnitComponent(x);
            a.setXmlId("id_000037");
            b = new VecNumericalValue();
            b.setUnitComponent(y);
            c = new VecNumericalValue();
            c.setUnitComponent(x);
        }

        @Test
        void should_return_false_on_different() {
            assertThat(equivalence.equivalent(a, b)).isFalse();
        }

        @Test
        void should_return_true_same() {
            assertThat(equivalence.equivalent(a, c)).isTrue();
        }
    }

    @Nested
    class Embedded {

        private VecNumericalValue a, b, c;
        private final VecTolerance x = new VecTolerance();
        private final VecTolerance y = new VecTolerance();
        private final VecTolerance z = new VecTolerance();

        @BeforeEach
        void setup() {
            a = new VecNumericalValue();
            a.setTolerance(x);
            a.setXmlId("id_000037");
            b = new VecNumericalValue();
            b.setTolerance(y);
            c = new VecNumericalValue();
            c.setTolerance(z);

            x.setLowerBoundary(-0.1);
            x.setUpperBoundary(0.1);
            y.setLowerBoundary(-0.2);
            y.setUpperBoundary(0.2);
            z.setLowerBoundary(-0.1);
            z.setUpperBoundary(0.1);
        }

        @Test
        void should_return_false_on_different() {
            assertThat(equivalence.equivalent(a, b)).isFalse();
        }

        @Test
        void should_return_true_same() {
            assertThat(equivalence.equivalent(a, c)).isTrue();
        }
    }

    @Nested
    class EmbeddedList {

        private VecMaterialComposition a, b, c;
        private final VecMassInformation w = new VecMassInformation();
        private final VecMassInformation x = new VecMassInformation();
        private final VecMassInformation y = new VecMassInformation();
        private final VecMassInformation z = new VecMassInformation();
        private final VecMassInformation v = new VecMassInformation();

        @BeforeEach
        void setup() {

            x.setXmlId("x");
            x.setDeterminationType(VecValueDetermination.CALCULATED);
            y.setXmlId("y");
            y.setDeterminationType(VecValueDetermination.ESTIMATED);
            z.setXmlId("z");
            z.setDeterminationType(VecValueDetermination.MEASURED);

            v.setXmlId("v");
            v.setDeterminationType(VecValueDetermination.CALCULATED);
            w.setXmlId("w");
            w.setDeterminationType(VecValueDetermination.ESTIMATED);

            a = new VecMaterialComposition();
            a.setXmlId("a");
            b = new VecMaterialComposition();
            b.setXmlId("b");
            c = new VecMaterialComposition();
            c.setXmlId("c");

            a.getMassInformations()
                    .add(x);
            a.getMassInformations()
                    .add(y);

            b.getMassInformations()
                    .add(x);
            b.getMassInformations()
                    .add(z);

            c.getMassInformations()
                    .add(w);
            c.getMassInformations()
                    .add(v);

        }

        @Test
        void should_return_false_on_different() {
            assertThat(equivalence.equivalent(a, b)).isFalse();
        }

        @Test
        void should_return_true_same() {
            assertThat(equivalence.equivalent(a, c)).isTrue();
        }
    }

}
