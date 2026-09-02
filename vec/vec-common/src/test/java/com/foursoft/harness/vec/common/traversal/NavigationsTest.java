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
package com.foursoft.harness.vec.common.traversal;

import com.foursoft.harness.vec.common.traversal.TestModel.Assembly;
import com.foursoft.harness.vec.common.traversal.TestModel.Grade;
import com.foursoft.harness.vec.common.traversal.TestModel.Kit;
import com.foursoft.harness.vec.common.traversal.TestModel.Part;
import com.foursoft.harness.vec.common.traversal.TestModel.Screw;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class NavigationsTest {

    private static final Grade GRADE_8_8 = new Grade("8.8");
    private static final Screw SCREW = new Screw("M6", GRADE_8_8);

    @Test
    void optionalAdaptsAFunctionReturningAnOptional() {
        final SingleNavigation<Screw, Grade> grade = Navigations.optional(screw -> Optional.ofNullable(screw.grade()));

        assertThat(grade.from(SCREW)).contains(GRADE_8_8);
        assertThat(grade.from(new Screw("M8", null))).isEmpty();
    }

    @Test
    void nullableWrapsANullResultIntoAnEmptyOptional() {
        final SingleNavigation<Screw, Grade> grade = Navigations.nullable(Screw::grade);

        assertThat(grade.from(SCREW)).contains(GRADE_8_8);
        assertThat(grade.from(new Screw("M8", null))).isEmpty();
    }

    @Test
    void streamAdaptsAFunctionReturningAStream() {
        final MultiNavigation<Assembly, Part> parts = Navigations.stream(assembly -> assembly.parts().stream());

        assertThat(parts.listFrom(new Assembly("assembly", null, List.of(SCREW)))).containsExactly(SCREW);
    }

    @Test
    void collectionAdaptsAFunctionReturningACollection() {
        final MultiNavigation<Assembly, Part> parts = Navigations.collection(Assembly::parts);

        assertThat(parts.listFrom(new Assembly("assembly", null, List.of(SCREW)))).containsExactly(SCREW);
        assertThat(parts.listFrom(new Assembly("assembly", null, Collections.emptyList()))).isEmpty();
    }

    /**
     * The result type of the adapted function only has to be a collection of a sub type of the navigated type,
     * so getters of specialized associations can be widened to a navigation of the general type.
     */
    @Test
    void collectionWidensTheElementType() {
        final MultiNavigation<Kit, Part> parts = Navigations.collection(Kit::screws);

        assertThat(parts.listFrom(new Kit(List.of(SCREW)))).containsExactly(SCREW);
    }

}
