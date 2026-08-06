/*-
 * ========================LICENSE_START=================================
 * VEC 1.2.X
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
package com.foursoft.harness.vec.v12x.navigations;

import com.foursoft.harness.vec.common.HasCustomProperties;
import com.foursoft.harness.vec.common.traversal.SingleNavigation;
import com.foursoft.harness.vec.v12x.VecCustomProperty;
import com.foursoft.harness.vec.v12x.VecExtendableElement;
import com.foursoft.harness.vec.v12x.VecLanguageCode;
import com.foursoft.harness.vec.v12x.VecLocalizedStringProperty;
import com.foursoft.harness.vec.v12x.VecValueRange;
import com.foursoft.harness.vec.v12x.traversal.CustomProperties;
import com.foursoft.harness.vec.v12x.traversal.LocalizedStringProperties;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Navigation methods for custom properties of a {@link VecExtendableElement}.
 *
 * @deprecated Use {@link CustomProperties} instead, and {@link LocalizedStringProperties} for the navigations
 * starting at a {@link VecLocalizedStringProperty}.
 */
@Deprecated(forRemoval = true)
public final class CustomPropertyNavs {

    private CustomPropertyNavs() {
        // hide default constructor
    }

    /**
     * @deprecated Use {@link CustomProperties#stringValueOf(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<HasCustomProperties<VecCustomProperty>, Optional<String>> customPropertyValueStringOf(
            final String customProperty) {
        return CustomProperties.stringValueOf(customProperty);
    }

    /**
     * @deprecated Use {@link CustomProperties#integerValueOf(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<HasCustomProperties<VecCustomProperty>, Optional<BigInteger>> customPropertyValueIntegerOf(
            final String customProperty) {
        return CustomProperties.integerValueOf(customProperty);
    }

    /**
     * @deprecated Use {@link CustomProperties#stringValuesOf(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<HasCustomProperties<VecCustomProperty>, List<String>> customPropertyValueStringsOf(
            final String customProperty) {
        return CustomProperties.stringValuesOf(customProperty)::listFrom;
    }

    /**
     * @deprecated Use {@link CustomProperties#doubleValueOf(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<HasCustomProperties<VecCustomProperty>, Optional<Double>> customPropertyValueDoubleOf(
            final String customProperty) {
        return CustomProperties.doubleValueOf(customProperty);
    }

    /**
     * @deprecated Use {@link CustomProperties#valueRangeOf(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<HasCustomProperties<VecCustomProperty>, Optional<VecValueRange>> customPropertyValueRangeOf(
            final String customProperty) {
        return CustomProperties.valueRangeOf(customProperty);
    }

    /**
     * @deprecated Use {@link CustomProperties#booleanValueOf(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<HasCustomProperties<VecCustomProperty>, Optional<Boolean>> customPropertyValueBooleanOf(
            final String customProperty) {
        return CustomProperties.booleanValueOf(customProperty);
    }

    /**
     * @deprecated Use {@link CustomProperties#nestedPropertiesOf(String)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<HasCustomProperties<VecCustomProperty>, List<VecCustomProperty>> customPropertyValuesOf(
            final String customProperty) {
        return CustomProperties.nestedPropertiesOf(customProperty)::listFrom;
    }

    /**
     * @deprecated Use {@link CustomProperties#localizedValueOf(String, VecLanguageCode)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<HasCustomProperties<VecCustomProperty>, Optional<String>> customPropertyValueLocalizedString(
            final String customProperty, final VecLanguageCode languageCode) {
        return CustomProperties.localizedValueOf(customProperty, languageCode);
    }

    /**
     * @deprecated Use {@link LocalizedStringProperties#valueIn(VecLanguageCode)} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecLocalizedStringProperty, Optional<String>> convertLocalizedStringProperty(
            final VecLanguageCode languageCode) {
        return localizedString -> Optional.ofNullable(localizedString)
                .flatMap(LocalizedStringProperties.valueIn(languageCode));
    }

    /**
     * @deprecated Narrow a navigation leading to the custom properties at the call site instead, for example
     * {@code CustomProperties.toCustomProperties().filter(…).ofType(targetClass).atMostOne()}. This one takes
     * the properties themselves rather than the element holding them, so there is nothing to navigate from.
     */
    @Deprecated(forRemoval = true)
    public static <T extends VecCustomProperty> Function<Collection<VecCustomProperty>, Optional<T>> customPropertyOfType(
            final String propertyType, final Class<T> targetClass) {
        final SingleNavigation<HasCustomProperties<VecCustomProperty>, T> property =
                CustomProperties.toCustomProperties()
                        .filter(customProperty -> customProperty.getPropertyType().equalsIgnoreCase(propertyType))
                        .ofType(targetClass)
                        .atMostOne();
        return customProperties -> property.from(() -> new ArrayList<>(customProperties));
    }

}
