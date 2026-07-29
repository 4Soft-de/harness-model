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
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SingleNavigationTest {

    private static final Grade GRADE_8_8 = new Grade("8.8");
    private static final Screw SCREW = new Screw("M6", GRADE_8_8);
    private static final Nut NUT = new Nut("M6");

    private static final SingleNavigation<Assembly, Part> MAIN_PART =
            Navigations.nullable(Assembly::mainPart);

    private static Assembly assemblyWith(final Part mainPart) {
        return new Assembly("assembly", mainPart, Collections.emptyList());
    }

    @Test
    void fromReturnsThePresentElement() {
        assertThat(MAIN_PART.from(assemblyWith(SCREW))).contains(SCREW);
    }

    @Test
    void fromReturnsEmptyForAnAbsentElement() {
        assertThat(MAIN_PART.from(assemblyWith(null))).isEmpty();
    }

    @Test
    void thenComposesTwoSingleNavigations() {
        final SingleNavigation<Assembly, Grade> grade = MAIN_PART
                .ofType(Screw.class)
                .then(Navigations.nullable(Screw::grade));

        assertThat(grade.from(assemblyWith(SCREW))).contains(GRADE_8_8);
    }

    @Test
    void thenPropagatesAnAbsentIntermediateElement() {
        final SingleNavigation<Assembly, Grade> grade = MAIN_PART
                .ofType(Screw.class)
                .then(Navigations.nullable(Screw::grade));

        assertThat(grade.from(assemblyWith(null))).isEmpty();
        assertThat(grade.from(assemblyWith(NUT))).isEmpty();
        assertThat(grade.from(assemblyWith(new Screw("M8", null)))).isEmpty();
    }

    @Test
    void thenEachContinuesWithAMultiNavigation() {
        final MultiNavigation<Plant, Part> partsOfTheOnlyAssembly =
                Navigations.<Plant, Assembly>collection(Plant::assemblies)
                        .atMostOne()
                        .thenEach(Navigations.collection(Assembly::parts));

        final Assembly assembly = new Assembly("assembly", null, List.of(SCREW, NUT));

        assertThat(partsOfTheOnlyAssembly.listFrom(new Plant(List.of(assembly)))).containsExactly(SCREW, NUT);
        assertThat(partsOfTheOnlyAssembly.listFrom(new Plant(Collections.emptyList()))).isEmpty();
    }

    @Test
    void filterKeepsOnlyMatchingElements() {
        final SingleNavigation<Assembly, Part> m6 = MAIN_PART.filter(part -> "M6".equals(part.name()));

        assertThat(m6.from(assemblyWith(SCREW))).contains(SCREW);
        assertThat(m6.from(assemblyWith(new Nut("M8")))).isEmpty();
    }

    @Test
    void ofTypeNarrowsTheNavigation() {
        final SingleNavigation<Assembly, Screw> screw = MAIN_PART.ofType(Screw.class);

        assertThat(screw.from(assemblyWith(SCREW))).contains(SCREW);
        assertThat(screw.from(assemblyWith(NUT))).isEmpty();
    }

    @Test
    void asMultiYieldsZeroOrOneElement() {
        assertThat(MAIN_PART.asMulti().listFrom(assemblyWith(SCREW))).containsExactly(SCREW);
        assertThat(MAIN_PART.asMulti().listFrom(assemblyWith(null))).isEmpty();
    }

    @Test
    void orElseNullAndOrElseUnwrapTheResult() {
        assertThat(MAIN_PART.orElseNull(assemblyWith(SCREW))).isEqualTo(SCREW);
        assertThat(MAIN_PART.orElseNull(assemblyWith(null))).isNull();
        assertThat(MAIN_PART.orElse(assemblyWith(null), NUT)).isEqualTo(NUT);
    }

    @Test
    void streamFromYieldsZeroOrOneElement() {
        assertThat(MAIN_PART.streamFrom(assemblyWith(SCREW))).containsExactly(SCREW);
        assertThat(MAIN_PART.streamFrom(assemblyWith(null))).isEmpty();
    }

    @Test
    void isUsableWhereverAFunctionIsExpected() {
        assertThat(Stream.of(assemblyWith(SCREW)).map(MAIN_PART)).containsExactly(Optional.of(SCREW));
    }

}
