/*-
 * ========================LICENSE_START=================================
 * VEC Common
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
package com.foursoft.harness.vec.common.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ModelClassNamesTest {

    // Stand-ins for classes of the generated Java API, only their simple names matter.
    private static class VecPartStructureSpecification {
    }

    private static class VecCartesianVector3D {
    }

    private static class VecContent {
    }

    private static class Unprefixed {
    }

    @Test
    void modelClassNameRemovesTheJavaApiPrefix() {
        assertThat(ModelClassNames.modelClassName(VecPartStructureSpecification.class))
                .isEqualTo("PartStructureSpecification");
    }

    @Test
    void modelClassNameKeepsVecInsideTheName() {
        assertThat(ModelClassNames.modelClassName(VecCartesianVector3D.class)).isEqualTo("CartesianVector3D");
    }

    @Test
    void modelClassNameOfVecContentKeepsThePrefix() {
        assertThat(ModelClassNames.modelClassName(VecContent.class)).isEqualTo("VecContent");
    }

    @Test
    void modelClassNameKeepsAClassWithoutThePrefix() {
        assertThat(ModelClassNames.modelClassName(Unprefixed.class)).isEqualTo("Unprefixed");
    }

    @Test
    void abbreviatedModelClassNameConsistsOfTheCapitalLetters() {
        assertThat(ModelClassNames.abbreviatedModelClassName(VecPartStructureSpecification.class)).isEqualTo("PSS");
        assertThat(ModelClassNames.abbreviatedModelClassName(VecCartesianVector3D.class)).isEqualTo("CVD");
    }

}
