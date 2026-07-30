/*-
 * ========================LICENSE_START=================================
 * VEC 2.X
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
package com.foursoft.harness.vec.v2x.traversal;

import com.foursoft.harness.vec.v2x.VecAbstractLocalizedString;
import com.foursoft.harness.vec.v2x.VecLanguageCode;
import com.foursoft.harness.vec.v2x.navigations.DescriptionNavs;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import static com.foursoft.harness.vec.v2x.traversal.LocalizedStringFixtures.LENGTH_TYPE;
import static com.foursoft.harness.vec.v2x.traversal.LocalizedStringFixtures.localizedString;
import static com.foursoft.harness.vec.v2x.traversal.LocalizedStringFixtures.typedString;
import static org.assertj.core.api.Assertions.assertThat;

class LocalizedStringsTest {

    private static Optional<String> reduce(final List<VecAbstractLocalizedString> localizedStrings,
                                           final Collector<VecAbstractLocalizedString, ?, Optional<String>>
                                                   reduction) {
        return localizedStrings.stream().collect(reduction);
    }

    @Test
    void reducesToTheValueOfTheRequestedLanguage() {
        final List<VecAbstractLocalizedString> localizedStrings = List.of(
                localizedString(VecLanguageCode.DE, "Leitung"),
                localizedString(VecLanguageCode.EN, "Wire"));

        assertThat(reduce(localizedStrings, LocalizedStrings.valueIn(VecLanguageCode.DE))).contains("Leitung");
        assertThat(reduce(localizedStrings, LocalizedStrings.valueIn(VecLanguageCode.EN))).contains("Wire");
    }

    @Test
    void reducesToTheOnlyValueRegardlessOfItsLanguage() {
        final List<VecAbstractLocalizedString> localizedStrings =
                List.of(localizedString(VecLanguageCode.EN, "Wire"));

        assertThat(reduce(localizedStrings, LocalizedStrings.valueIn(VecLanguageCode.DE))).contains("Wire");
    }

    @Test
    void reducesToNoValueForNoLocalizedString() {
        assertThat(reduce(Collections.emptyList(), LocalizedStrings.valueIn(VecLanguageCode.DE))).isEmpty();
    }

    @Test
    void reducesToNoValueForASingleTypedString() {
        final List<VecAbstractLocalizedString> localizedStrings =
                List.of(typedString(VecLanguageCode.DE, LENGTH_TYPE, "100"));

        assertThat(reduce(localizedStrings, LocalizedStrings.valueIn(VecLanguageCode.DE))).isEmpty();
    }

    @Test
    void reducesToASingleTypedStringWithoutAType() {
        final List<VecAbstractLocalizedString> localizedStrings =
                List.of(typedString(VecLanguageCode.DE, null, "Leitung"));

        assertThat(reduce(localizedStrings, LocalizedStrings.valueIn(VecLanguageCode.DE))).contains("Leitung");
    }

    @Test
    void ignoresTypedStringsWhenSeveralValuesArePresent() {
        final List<VecAbstractLocalizedString> localizedStrings = List.of(
                typedString(VecLanguageCode.DE, LENGTH_TYPE, "100"),
                localizedString(VecLanguageCode.DE, "Leitung"));

        assertThat(reduce(localizedStrings, LocalizedStrings.valueIn(VecLanguageCode.DE))).contains("Leitung");
    }

    @Test
    void reducesToTheTypedValueOfTheRequestedTypeAndLanguage() {
        final List<VecAbstractLocalizedString> localizedStrings = List.of(
                typedString(VecLanguageCode.DE, LENGTH_TYPE, "100"),
                typedString(VecLanguageCode.EN, LENGTH_TYPE, "One hundred"),
                typedString(VecLanguageCode.DE, "Width", "5"));

        assertThat(reduce(localizedStrings, LocalizedStrings.typedValueBy(LENGTH_TYPE, VecLanguageCode.DE)))
                .contains("100");
        assertThat(reduce(localizedStrings, LocalizedStrings.typedValueBy(LENGTH_TYPE, VecLanguageCode.EN)))
                .contains("One hundred");
        assertThat(reduce(localizedStrings, LocalizedStrings.typedValueBy("Height", VecLanguageCode.DE)))
                .isEmpty();
    }

    @Test
    void reducesToNoTypedValueForAnEmptyValue() {
        final List<VecAbstractLocalizedString> localizedStrings = List.of(
                typedString(VecLanguageCode.DE, LENGTH_TYPE, ""),
                localizedString(VecLanguageCode.DE, "Leitung"));

        assertThat(reduce(localizedStrings, LocalizedStrings.typedValueBy(LENGTH_TYPE, VecLanguageCode.DE)))
                .isEmpty();
    }

    /**
     * Characterisation test for the list based navigations of the deprecated {@link DescriptionNavs}, which
     * now apply these reductions. Can be removed together with {@link DescriptionNavs}.
     */
    @Test
    @SuppressWarnings({"deprecation", "removal"})
    void deprecatedNavigationsBehaveLikeTheirReplacement() {
        for (final List<VecAbstractLocalizedString> localizedStrings : LocalizedStringFixtures.allVariants()) {
            assertThat(DescriptionNavs.germanString().apply(localizedStrings))
                    .isEqualTo(reduce(localizedStrings, LocalizedStrings.valueIn(VecLanguageCode.DE)));
            assertThat(DescriptionNavs.englishString().apply(localizedStrings))
                    .isEqualTo(reduce(localizedStrings, LocalizedStrings.valueIn(VecLanguageCode.EN)));
            assertThat(DescriptionNavs.stringIn(VecLanguageCode.EN).apply(localizedStrings))
                    .isEqualTo(reduce(localizedStrings, LocalizedStrings.valueIn(VecLanguageCode.EN)));
        }
    }

}
