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
import com.foursoft.harness.vec.common.traversal.TestModel.Nut;
import com.foursoft.harness.vec.common.traversal.TestModel.Part;
import com.foursoft.harness.vec.common.traversal.TestModel.Plant;
import com.foursoft.harness.vec.common.traversal.TestModel.Screw;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MultiNavigationTest {

    private static final Grade GRADE_8_8 = new Grade("8.8");
    private static final Grade GRADE_10_9 = new Grade("10.9");
    private static final Screw SCREW_M6 = new Screw("M6", GRADE_8_8);
    private static final Screw SCREW_M8 = new Screw("M8", GRADE_10_9);
    private static final Screw SCREW_WITHOUT_GRADE = new Screw("M10", null);
    private static final Nut NUT = new Nut("M6");

    private static final MultiNavigation<Assembly, Part> PARTS =
            Navigations.collection(Assembly::parts);

    private static Assembly assemblyWith(final Part... parts) {
        return new Assembly("assembly", null, List.of(parts));
    }

    @Test
    void fromReturnsAllElements() {
        assertThat(PARTS.from(assemblyWith(SCREW_M6, NUT))).containsExactly(SCREW_M6, NUT);
    }

    @Test
    void fromReturnsAFreshStreamOnEveryApplication() {
        final Assembly assembly = assemblyWith(SCREW_M6, NUT);

        assertThat(PARTS.from(assembly)).hasSize(2);
        assertThat(PARTS.from(assembly)).hasSize(2);
    }

    @Test
    void thenDropsElementsWithoutATarget() {
        final MultiNavigation<Assembly, Grade> grades = PARTS
                .ofType(Screw.class)
                .then(Navigations.nullable(Screw::grade));

        assertThat(grades.listFrom(assemblyWith(SCREW_M6, NUT, SCREW_WITHOUT_GRADE, SCREW_M8)))
                .containsExactly(GRADE_8_8, GRADE_10_9);
    }

    @Test
    void thenEachFlattensNestedNavigations() {
        final MultiNavigation<Plant, Part> allParts =
                Navigations.<Plant, Assembly>collection(Plant::assemblies)
                        .thenEach(PARTS);

        final Plant plant = new Plant(List.of(assemblyWith(SCREW_M6), assemblyWith(NUT, SCREW_M8)));

        assertThat(allParts.listFrom(plant)).containsExactly(SCREW_M6, NUT, SCREW_M8);
    }

    @Test
    void filterKeepsOnlyMatchingElements() {
        assertThat(PARTS.filter(part -> "M6".equals(part.name())).listFrom(assemblyWith(SCREW_M6, NUT, SCREW_M8)))
                .containsExactly(SCREW_M6, NUT);
    }

    @Test
    void ofTypeNarrowsTheNavigation() {
        assertThat(PARTS.ofType(Screw.class).listFrom(assemblyWith(SCREW_M6, NUT, SCREW_M8)))
                .containsExactly(SCREW_M6, SCREW_M8);
    }

    @Test
    void atMostOneReturnsEmptyForNoElement() {
        assertThat(PARTS.atMostOne().from(assemblyWith())).isEmpty();
    }

    @Test
    void atMostOneReturnsTheOnlyElement() {
        assertThat(PARTS.atMostOne().from(assemblyWith(SCREW_M6))).contains(SCREW_M6);
    }

    @Test
    void atMostOneReturnsTheFirstOfSeveralElements() {
        assertThat(PARTS.atMostOne().from(assemblyWith(SCREW_M6, NUT))).contains(SCREW_M6);
    }

    @Test
    void listFromAndAsListCollectTheElements() {
        final Assembly assembly = assemblyWith(SCREW_M6, NUT);

        assertThat(PARTS.listFrom(assembly)).containsExactly(SCREW_M6, NUT);
        assertThat(PARTS.asList().from(assembly)).containsExactly(SCREW_M6, NUT);
        assertThat(PARTS.listFrom(assemblyWith())).isEmpty();
    }

    @Test
    void isUsableWhereverAFunctionIsExpected() {
        final Plant plant = new Plant(List.of(assemblyWith(SCREW_M6), assemblyWith(NUT)));

        assertThat(plant.assemblies().stream().flatMap(PARTS)).containsExactly(SCREW_M6, NUT);
    }

    @Test
    void navigatesAnEmptySourceWithoutFailing() {
        assertThat(Navigations.<Plant, Assembly>collection(Plant::assemblies)
                           .thenEach(PARTS)
                           .listFrom(new Plant(Collections.emptyList()))).isEmpty();
    }

}
