/*-
 * ========================LICENSE_START=================================
 * vec-v2x
 * %%
 * Copyright (C) 2020 - 2022 4Soft GmbH
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
package com.foursoft.harness.vec.v2x.navigations;

import com.foursoft.harness.vec.common.HasDescription;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.common.util.StringUtils;
import com.foursoft.harness.vec.v2x.VecAbstractLocalizedString;
import com.foursoft.harness.vec.v2x.VecLanguageCode;
import com.foursoft.harness.vec.v2x.VecLocalizedTypedString;
import com.foursoft.harness.vec.v2x.predicates.VecPredicates;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Navigation methods for {@link HasDescription} with {@link VecAbstractLocalizedString}s.
 */
public final class DescriptionNavs {

    private DescriptionNavs() {
        // hide default constructor
    }

    public static Function<HasDescription<? extends VecAbstractLocalizedString>, Optional<String>> germanDescription() {
        return descriptionIn(VecLanguageCode.DE);
    }

    public static Function<HasDescription<? extends VecAbstractLocalizedString>, Optional<String>> englishDescription() {
        return descriptionIn(VecLanguageCode.EN);
    }

    public static Function<HasDescription<? extends VecAbstractLocalizedString>, Optional<String>> descriptionIn(
            final VecLanguageCode vecLanguageCode) {
        return descHolder -> {
            final List<? extends VecAbstractLocalizedString> localizedStrings = descHolder.getDescriptions();
            return stringIn(vecLanguageCode).apply(localizedStrings);
        };
    }

    public static Function<List<? extends VecAbstractLocalizedString>, Optional<String>> germanString() {
        return stringIn(VecLanguageCode.DE);
    }

    public static Function<List<? extends VecAbstractLocalizedString>, Optional<String>> englishString() {
        return stringIn(VecLanguageCode.EN);
    }

    public static Function<List<? extends VecAbstractLocalizedString>, Optional<String>> stringIn(
            final VecLanguageCode vecLanguageCode) {
        return localizedStrings -> {
            if (localizedStrings.isEmpty()) {
                return Optional.empty();
            }
            if (localizedStrings.size() == 1) {
                final VecAbstractLocalizedString localizedString = localizedStrings.get(0);
                if (localizedString instanceof VecLocalizedTypedString &&
                        !StringUtils.isEmpty(((VecLocalizedTypedString) localizedString).getType())) {
                    return Optional.empty();
                }
                return Optional.ofNullable(localizedString).map(VecAbstractLocalizedString::getValue);
            }

            return localizedStrings.stream()
                    .filter(Objects::nonNull)
                    .filter(d -> !(d instanceof VecLocalizedTypedString))
                    .filter(VecPredicates.isLanguageCode(vecLanguageCode))
                    .map(VecAbstractLocalizedString::getValue)
                    .filter(Objects::nonNull)
                    .collect(StreamUtils.findOneOrNone());
        };
    }

    public static Function<HasDescription<? extends VecAbstractLocalizedString>, Optional<String>> germanTypedStringBy(
            final String descriptionType) {
        return typedStringBy(descriptionType, VecLanguageCode.DE);
    }

    public static Function<HasDescription<? extends VecAbstractLocalizedString>, Optional<String>> englishTypedStringBy(
            final String descriptionType) {
        return typedStringBy(descriptionType, VecLanguageCode.EN);
    }

    public static Function<HasDescription<? extends VecAbstractLocalizedString>, Optional<String>> typedStringBy(
            final String descriptionType, final VecLanguageCode vecLanguageCode) {
        return hasDescription -> hasDescription.getDescriptions().stream()
                .filter(VecPredicates.isLanguageCode(vecLanguageCode))
                .flatMap(StreamUtils.ofClass(VecLocalizedTypedString.class))
                .filter(typedString -> descriptionType.equals(typedString.getType()))
                .collect(StreamUtils.findOneOrNone())
                .map(VecLocalizedTypedString::getValue)
                .filter(StringUtils::isNotEmpty);
    }

}
