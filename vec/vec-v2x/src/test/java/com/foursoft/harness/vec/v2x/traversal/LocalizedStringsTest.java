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

import static com.foursoft.harness.vec.v2x.traversal.LocalizedStringFixtures.LENGTH_TYPE;
import static com.foursoft.harness.vec.v2x.traversal.LocalizedStringFixtures.localizedString;
import static com.foursoft.harness.vec.v2x.traversal.LocalizedStringFixtures.typedString;
import static org.assertj.core.api.Assertions.assertThat;

class LocalizedStringsTest {

    @Test
    void navigatesToTheValueOfTheRequestedLanguage() {
        final List<VecAbstractLocalizedString> localizedStrings = List.of(
                localizedString(VecLanguageCode.DE, "Leitung"),
                localizedString(VecLanguageCode.EN, "Wire"));

        assertThat(LocalizedStrings.germanString().from(localizedStrings)).contains("Leitung");
        assertThat(LocalizedStrings.englishString().from(localizedStrings)).contains("Wire");
        assertThat(LocalizedStrings.stringIn(VecLanguageCode.EN).from(localizedStrings)).contains("Wire");
    }

    @Test
    void navigatesToTheOnlyValueRegardlessOfItsLanguage() {
        assertThat(LocalizedStrings.germanString().from(List.of(localizedString(VecLanguageCode.EN, "Wire"))))
                .contains("Wire");
    }

    @Test
    void navigatesToNoValueForAnEmptyList() {
        assertThat(LocalizedStrings.germanString().from(Collections.emptyList())).isEmpty();
    }

    @Test
    void navigatesToNoValueForASingleTypedString() {
        assertThat(LocalizedStrings.germanString()
                           .from(List.of(typedString(VecLanguageCode.DE, LENGTH_TYPE, "100")))).isEmpty();
    }

    @Test
    void navigatesToASingleTypedStringWithoutAType() {
        assertThat(LocalizedStrings.germanString()
                           .from(List.of(typedString(VecLanguageCode.DE, null, "Leitung")))).contains("Leitung");
    }

    @Test
    void ignoresTypedStringsWhenSeveralValuesArePresent() {
        final List<VecAbstractLocalizedString> localizedStrings = List.of(
                typedString(VecLanguageCode.DE, LENGTH_TYPE, "100"),
                localizedString(VecLanguageCode.DE, "Leitung"));

        assertThat(LocalizedStrings.germanString().from(localizedStrings)).contains("Leitung");
    }

    @Test
    void navigatesToTheTypedStringOfTheRequestedTypeAndLanguage() {
        final List<VecAbstractLocalizedString> localizedStrings = List.of(
                typedString(VecLanguageCode.DE, LENGTH_TYPE, "100"),
                typedString(VecLanguageCode.EN, LENGTH_TYPE, "One hundred"),
                typedString(VecLanguageCode.DE, "Width", "5"));

        assertThat(LocalizedStrings.typedStringBy(LENGTH_TYPE, VecLanguageCode.DE).from(localizedStrings))
                .contains("100");
        assertThat(LocalizedStrings.typedStringBy(LENGTH_TYPE, VecLanguageCode.EN).from(localizedStrings))
                .contains("One hundred");
        assertThat(LocalizedStrings.typedStringBy("Height", VecLanguageCode.DE).from(localizedStrings))
                .isEmpty();
    }

    @Test
    void navigatesToNoTypedStringForAnEmptyValue() {
        final List<VecAbstractLocalizedString> localizedStrings = List.of(
                typedString(VecLanguageCode.DE, LENGTH_TYPE, ""),
                localizedString(VecLanguageCode.DE, "Leitung"));

        assertThat(LocalizedStrings.typedStringBy(LENGTH_TYPE, VecLanguageCode.DE).from(localizedStrings))
                .isEmpty();
    }

    /**
     * Characterisation test for the deprecated {@link DescriptionNavs}, whose list based navigations delegate
     * to {@link LocalizedStrings}. Can be removed together with {@link DescriptionNavs}.
     */
    @Test
    @SuppressWarnings({"deprecation", "removal"})
    void deprecatedNavigationsBehaveLikeTheirReplacement() {
        for (final List<VecAbstractLocalizedString> localizedStrings : LocalizedStringFixtures.allVariants()) {
            assertThat(DescriptionNavs.germanString().apply(localizedStrings))
                    .isEqualTo(LocalizedStrings.germanString().from(localizedStrings));
            assertThat(DescriptionNavs.englishString().apply(localizedStrings))
                    .isEqualTo(LocalizedStrings.englishString().from(localizedStrings));
            assertThat(DescriptionNavs.stringIn(VecLanguageCode.EN).apply(localizedStrings))
                    .isEqualTo(LocalizedStrings.stringIn(VecLanguageCode.EN).from(localizedStrings));
        }
    }

}
