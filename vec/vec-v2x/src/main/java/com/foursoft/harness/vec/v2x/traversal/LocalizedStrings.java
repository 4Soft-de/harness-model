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
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.common.util.StringUtils;
import com.foursoft.harness.vec.v2x.VecAbstractLocalizedString;
import com.foursoft.harness.vec.v2x.VecLanguageCode;
import com.foursoft.harness.vec.v2x.VecLocalizedTypedString;
import com.foursoft.harness.vec.v2x.predicates.VecPredicates;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collector;

/**
 * Reductions from several {@link VecAbstractLocalizedString}s to the one value that applies.
 * <p>
 * These are <em>not</em> navigations: choosing the right value needs rules over the localized strings as a
 * whole, which no sequence of steps expresses. They are therefore used with
 * {@link MultiNavigation#collect(Collector)} on a navigation leading to the localized strings, such as
 * {@link Descriptions#descriptions()}:
 * <pre>
 * {@code
 * Descriptions.descriptions().collect(LocalizedStrings.valueIn(VecLanguageCode.DE));
 * }
 * </pre>
 */
public final class LocalizedStrings {

    private LocalizedStrings() {
        // hide default constructor
    }

    /**
     * Reduces localized strings to the value in the given language.
     * <p>
     * A single {@link VecLocalizedTypedString} with a type is not considered a description and therefore
     * yields no value. A single untyped string is used regardless of its language, as there is nothing to
     * choose from. Otherwise the typed strings are ignored and the value of the given language is used.
     *
     * @param languageCode Language of the value to reduce to.
     * @return A reduction to the value in the given language.
     */
    public static Collector<VecAbstractLocalizedString, ?, Optional<String>> valueIn(
            final VecLanguageCode languageCode) {
        return StreamUtils.reducing(localizedStrings -> {
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
        });
    }

    /**
     * Reduces localized strings to the value of the {@link VecLocalizedTypedString} with the given type and
     * language.
     *
     * @param descriptionType Type of the localized string to reduce to.
     * @param languageCode    Language of the localized string to reduce to.
     * @return A reduction to the value of the given type and language.
     */
    public static Collector<VecAbstractLocalizedString, ?, Optional<String>> typedValueBy(
            final String descriptionType, final VecLanguageCode languageCode) {
        return StreamUtils.reducing(localizedStrings -> reduceToTypedValue(
                localizedStrings, descriptionType, languageCode));
    }

    private static Optional<String> reduceToTypedValue(final List<VecAbstractLocalizedString> localizedStrings,
                                                       final String descriptionType,
                                                       final VecLanguageCode languageCode) {
        return localizedStrings.stream()
                .filter(VecPredicates.languageCode(languageCode))
                .flatMap(StreamUtils.ofClass(VecLocalizedTypedString.class))
                .filter(typedString -> descriptionType.equals(typedString.getType()))
                .collect(StreamUtils.findOneOrNone())
                .map(VecLocalizedTypedString::getValue)
                .filter(StringUtils::isNotEmpty);
    }

}
