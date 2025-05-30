/*-
 * ========================LICENSE_START=================================
 * VEC 2.X
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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

import com.foursoft.harness.vec.common.HasCustomProperties;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v2x.*;
import com.foursoft.harness.vec.v2x.predicates.VecPredicates;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Navigation methods for custom properties of a {@link VecExtendableElement}.
 */
public final class CustomPropertyNavs {

    private CustomPropertyNavs() {
        // hide default constructor
    }

    public static Function<HasCustomProperties<VecCustomProperty>, Optional<String>> customPropertyValueStringOf(
            final String customProperty) {
        return element -> element.getCustomProperty(VecSimpleValueProperty.class, customProperty)
                .map(VecSimpleValueProperty::getValue);
    }

    public static Function<HasCustomProperties<VecCustomProperty>, Optional<BigInteger>> customPropertyValueIntegerOf(
            final String customProperty) {
        return element -> element.getCustomProperty(VecIntegerValueProperty.class, customProperty)
                .map(VecIntegerValueProperty::getValue);
    }

    public static Function<HasCustomProperties<VecCustomProperty>, List<String>> customPropertyValueStringsOf(
            final String customProperty) {
        return element -> element.getCustomPropertiesWithType(VecSimpleValueProperty.class).stream()
                .filter(c -> c.getPropertyType().equals(customProperty))
                .map(VecSimpleValueProperty::getValue)
                .toList();
    }

    public static Function<HasCustomProperties<VecCustomProperty>, Optional<Double>> customPropertyValueDoubleOf(
            final String customProperty) {
        return element -> element.getCustomProperty(VecDoubleValueProperty.class, customProperty)
                .map(VecDoubleValueProperty::getValue);
    }

    public static Function<HasCustomProperties<VecCustomProperty>, Optional<VecValueRange>> customPropertyValueRangeOf(
            final String customProperty) {
        return element -> element.getCustomProperty(VecValueRangeProperty.class, customProperty)
                .map(VecValueRangeProperty::getValue);
    }

    public static Function<HasCustomProperties<VecCustomProperty>, Optional<Boolean>> customPropertyValueBooleanOf(
            final String customProperty) {
        return element -> element.getCustomProperty(VecBooleanValueProperty.class, customProperty)
                .map(VecBooleanValueProperty::isValue);
    }

    public static Function<HasCustomProperties<VecCustomProperty>, List<VecCustomProperty>> customPropertyValuesOf(
            final String customProperty) {
        return element -> element.getCustomProperty(VecComplexProperty.class, customProperty)
                .map(VecComplexProperty::getCustomProperties)
                .orElseGet(Collections::emptyList);
    }

    public static Function<HasCustomProperties<VecCustomProperty>, Optional<String>> customPropertyValueLocalizedString(
            final String customProperty, final VecLanguageCode languageCode) {
        return element -> element.getCustomProperty(VecLocalizedStringProperty.class, customProperty)
                .stream()
                .map(convertLocalizedStringProperty(languageCode))
                .flatMap(StreamUtils.unwrapOptional())
                .collect(StreamUtils.findOneOrNone());
    }

    public static Function<VecLocalizedStringProperty, Optional<String>> convertLocalizedStringProperty(
            final VecLanguageCode languageCode) {
        return localizedString -> Optional.ofNullable(localizedString)
                .map(VecLocalizedStringProperty::getValue)
                .filter(VecPredicates.languageCode(languageCode))
                .map(VecLocalizedString::getValue);
    }

}
