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

import com.foursoft.harness.vec.common.HasDescription;
import com.foursoft.harness.vec.v2x.VecAbstractLocalizedString;
import com.foursoft.harness.vec.v2x.VecLanguageCode;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecLocalizedTypedString;
import com.foursoft.harness.vec.v2x.navigations.DescriptionNavs;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DescriptionsTest {

    private static final String LENGTH_TYPE = "Length";

    private static VecLocalizedString localizedString(final VecLanguageCode languageCode, final String value) {
        final VecLocalizedString localizedString = new VecLocalizedString();
        localizedString.setLanguageCode(languageCode);
        localizedString.setValue(value);
        return localizedString;
    }

    private static VecLocalizedTypedString typedString(final VecLanguageCode languageCode, final String type,
                                                       final String value) {
        final VecLocalizedTypedString typedString = new VecLocalizedTypedString();
        typedString.setLanguageCode(languageCode);
        typedString.setType(type);
        typedString.setValue(value);
        return typedString;
    }

    private static HasDescription<VecAbstractLocalizedString> holderOf(final VecAbstractLocalizedString... strings) {
        final List<VecAbstractLocalizedString> descriptions = List.of(strings);
        return () -> descriptions;
    }

    @Test
    void navigatesToTheDescriptionOfTheRequestedLanguage() {
        final HasDescription<VecAbstractLocalizedString> holder = holderOf(
                localizedString(VecLanguageCode.DE, "Leitung"),
                localizedString(VecLanguageCode.EN, "Wire"));

        assertThat(Descriptions.germanDescription().from(holder)).contains("Leitung");
        assertThat(Descriptions.englishDescription().from(holder)).contains("Wire");
        assertThat(Descriptions.descriptionIn(VecLanguageCode.DE).from(holder)).contains("Leitung");
    }

    @Test
    void navigatesToTheOnlyDescriptionRegardlessOfItsLanguage() {
        final HasDescription<VecAbstractLocalizedString> holder =
                holderOf(localizedString(VecLanguageCode.EN, "Wire"));

        assertThat(Descriptions.germanDescription().from(holder)).contains("Wire");
    }

    @Test
    void navigatesToNoDescriptionForAnEmptyHolder() {
        assertThat(Descriptions.germanDescription().from(holderOf())).isEmpty();
    }

    @Test
    void navigatesToNoDescriptionForASingleTypedString() {
        final HasDescription<VecAbstractLocalizedString> holder =
                holderOf(typedString(VecLanguageCode.DE, LENGTH_TYPE, "100"));

        assertThat(Descriptions.germanDescription().from(holder)).isEmpty();
    }

    @Test
    void navigatesToASingleTypedStringWithoutAType() {
        final HasDescription<VecAbstractLocalizedString> holder =
                holderOf(typedString(VecLanguageCode.DE, null, "Leitung"));

        assertThat(Descriptions.germanDescription().from(holder)).contains("Leitung");
    }

    @Test
    void ignoresTypedStringsWhenSeveralDescriptionsArePresent() {
        final HasDescription<VecAbstractLocalizedString> holder = holderOf(
                typedString(VecLanguageCode.DE, LENGTH_TYPE, "100"),
                localizedString(VecLanguageCode.DE, "Leitung"));

        assertThat(Descriptions.germanDescription().from(holder)).contains("Leitung");
    }

    @Test
    void navigatesToTheValueOfAListOfLocalizedStrings() {
        final List<VecAbstractLocalizedString> descriptions = List.of(
                localizedString(VecLanguageCode.DE, "Leitung"),
                localizedString(VecLanguageCode.EN, "Wire"));

        assertThat(Descriptions.germanString().from(descriptions)).contains("Leitung");
        assertThat(Descriptions.englishString().from(descriptions)).contains("Wire");
        assertThat(Descriptions.stringIn(VecLanguageCode.EN).from(descriptions)).contains("Wire");
        assertThat(Descriptions.germanString().from(Collections.emptyList())).isEmpty();
    }

    @Test
    void navigatesToTheTypedStringOfTheRequestedTypeAndLanguage() {
        final HasDescription<VecAbstractLocalizedString> holder = holderOf(
                typedString(VecLanguageCode.DE, LENGTH_TYPE, "100"),
                typedString(VecLanguageCode.EN, LENGTH_TYPE, "One hundred"),
                typedString(VecLanguageCode.DE, "Width", "5"));

        assertThat(Descriptions.germanTypedStringBy(LENGTH_TYPE).from(holder)).contains("100");
        assertThat(Descriptions.englishTypedStringBy(LENGTH_TYPE).from(holder)).contains("One hundred");
        assertThat(Descriptions.typedStringBy("Width", VecLanguageCode.DE).from(holder)).contains("5");
        assertThat(Descriptions.typedStringBy("Height", VecLanguageCode.DE).from(holder)).isEmpty();
    }

    @Test
    void navigatesToNoTypedStringForAnEmptyValue() {
        final HasDescription<VecAbstractLocalizedString> holder = holderOf(
                typedString(VecLanguageCode.DE, LENGTH_TYPE, ""),
                localizedString(VecLanguageCode.DE, "Leitung"));

        assertThat(Descriptions.germanTypedStringBy(LENGTH_TYPE).from(holder)).isEmpty();
    }

    /**
     * Characterisation test for the deprecated {@link DescriptionNavs}, which delegates to
     * {@link Descriptions}. Can be removed together with {@link DescriptionNavs}.
     */
    @Test
    @SuppressWarnings({"deprecation", "removal"})
    void deprecatedNavigationsBehaveLikeTheirReplacement() {
        final List<HasDescription<VecAbstractLocalizedString>> holders = List.of(
                holderOf(),
                holderOf(localizedString(VecLanguageCode.EN, "Wire")),
                holderOf(typedString(VecLanguageCode.DE, LENGTH_TYPE, "100")),
                holderOf(typedString(VecLanguageCode.DE, null, "Leitung")),
                holderOf(localizedString(VecLanguageCode.DE, "Leitung"),
                         localizedString(VecLanguageCode.EN, "Wire"),
                         typedString(VecLanguageCode.DE, LENGTH_TYPE, "100")));

        for (final HasDescription<VecAbstractLocalizedString> holder : holders) {
            assertThat(DescriptionNavs.germanDescription().apply(holder))
                    .isEqualTo(Descriptions.germanDescription().from(holder));
            assertThat(DescriptionNavs.englishDescription().apply(holder))
                    .isEqualTo(Descriptions.englishDescription().from(holder));
            assertThat(DescriptionNavs.descriptionIn(VecLanguageCode.DE).apply(holder))
                    .isEqualTo(Descriptions.descriptionIn(VecLanguageCode.DE).from(holder));
            assertThat(DescriptionNavs.germanString().apply(holder.getDescriptions()))
                    .isEqualTo(Descriptions.germanString().from(holder.getDescriptions()));
            assertThat(DescriptionNavs.englishString().apply(holder.getDescriptions()))
                    .isEqualTo(Descriptions.englishString().from(holder.getDescriptions()));
            assertThat(DescriptionNavs.stringIn(VecLanguageCode.EN).apply(holder.getDescriptions()))
                    .isEqualTo(Descriptions.stringIn(VecLanguageCode.EN).from(holder.getDescriptions()));
            assertThat(DescriptionNavs.germanTypedStringBy(LENGTH_TYPE).apply(holder))
                    .isEqualTo(Descriptions.germanTypedStringBy(LENGTH_TYPE).from(holder));
            assertThat(DescriptionNavs.englishTypedStringBy(LENGTH_TYPE).apply(holder))
                    .isEqualTo(Descriptions.englishTypedStringBy(LENGTH_TYPE).from(holder));
            assertThat(DescriptionNavs.typedStringBy(LENGTH_TYPE, VecLanguageCode.DE).apply(holder))
                    .isEqualTo(Descriptions.typedStringBy(LENGTH_TYPE, VecLanguageCode.DE).from(holder));
        }
    }

}
