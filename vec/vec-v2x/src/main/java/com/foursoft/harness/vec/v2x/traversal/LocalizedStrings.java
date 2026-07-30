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

import com.foursoft.harness.vec.common.traversal.MultiNavigation;
import com.foursoft.harness.vec.common.traversal.Navigations;
import com.foursoft.harness.vec.common.traversal.SingleNavigation;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.common.util.StringUtils;
import com.foursoft.harness.vec.v2x.VecAbstractLocalizedString;
import com.foursoft.harness.vec.v2x.VecLanguageCode;
import com.foursoft.harness.vec.v2x.VecLocalizedTypedString;
import com.foursoft.harness.vec.v2x.predicates.VecPredicates;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Navigations starting at a list of {@link VecAbstractLocalizedString}s.
 * <p>
 * Use {@link Descriptions} to navigate from an element holding such a list.
 */
public final class LocalizedStrings {

    private LocalizedStrings() {
        // hide default constructor
    }

    /**
     * Navigates to the german value.
     *
     * @return A navigation to the german value.
     * @see #stringIn(VecLanguageCode)
     */
    public static SingleNavigation<List<? extends VecAbstractLocalizedString>, String> germanString() {
        return stringIn(VecLanguageCode.DE);
    }

    /**
     * Navigates to the english value.
     *
     * @return A navigation to the english value.
     * @see #stringIn(VecLanguageCode)
     */
    public static SingleNavigation<List<? extends VecAbstractLocalizedString>, String> englishString() {
        return stringIn(VecLanguageCode.EN);
    }

    /**
     * Navigates to the value in the given language.
     * <p>
     * A single {@link VecLocalizedTypedString} with a type is not considered a description and therefore leads
     * to no value. A single untyped string is used regardless of its language, as there is nothing to choose
     * from. Otherwise the typed strings are ignored and the value of the given language is used.
     *
     * @param languageCode Language of the value to navigate to.
     * @return A navigation to the value in the given language.
     */
    public static SingleNavigation<List<? extends VecAbstractLocalizedString>, String> stringIn(
            final VecLanguageCode languageCode) {
        return localizedStrings -> {
            if (localizedStrings.isEmpty()) {
                return Optional.empty();
            }
            if (localizedStrings.size() == 1) {
                final VecAbstractLocalizedString localizedString = localizedStrings.get(0);
                if (localizedString instanceof final VecLocalizedTypedString typedString
                        && StringUtils.isNotEmpty(typedString.getType())) {
                    return Optional.empty();
                }
                return Optional.ofNullable(localizedString)
                        .map(VecAbstractLocalizedString::getValue);
            }

            return localizedStrings.stream()
                    .filter(Objects::nonNull)
                    .filter(localizedString -> !(localizedString instanceof VecLocalizedTypedString))
                    .filter(VecPredicates.languageCode(languageCode))
                    .map(VecAbstractLocalizedString::getValue)
                    .filter(Objects::nonNull)
                    .collect(StreamUtils.findOneOrNone());
        };
    }

    /**
     * Navigates to the value of the {@link VecLocalizedTypedString} with the given type and language.
     *
     * @param descriptionType Type of the localized string to navigate to.
     * @param languageCode    Language of the localized string to navigate to.
     * @return A navigation to the value of the given type and language.
     */
    public static SingleNavigation<List<? extends VecAbstractLocalizedString>, String> typedStringBy(
            final String descriptionType, final VecLanguageCode languageCode) {
        return elements()
                .filter(VecPredicates.languageCode(languageCode))
                .ofType(VecLocalizedTypedString.class)
                .filter(typedString -> descriptionType.equals(typedString.getType()))
                .atMostOne()
                .then(Navigations.nullable(VecLocalizedTypedString::getValue))
                .filter(StringUtils::isNotEmpty);
    }

    /**
     * Navigates from the list to the single localized strings, so that navigations over them can be composed
     * from the operators. Needed because the source of this catalog is the list itself.
     */
    private static MultiNavigation<List<? extends VecAbstractLocalizedString>, VecAbstractLocalizedString>
    elements() {
        return Navigations.collection(localizedStrings -> localizedStrings);
    }

}
