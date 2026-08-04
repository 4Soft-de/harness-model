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
package com.foursoft.harness.vec.v12x.traversal;

import com.foursoft.harness.vec.common.HasCustomProperties;
import com.foursoft.harness.vec.common.traversal.MultiNavigation;
import com.foursoft.harness.vec.common.traversal.Navigations;
import com.foursoft.harness.vec.common.traversal.SingleNavigation;
import com.foursoft.harness.vec.v12x.VecBooleanValueProperty;
import com.foursoft.harness.vec.v12x.VecComplexProperty;
import com.foursoft.harness.vec.v12x.VecCustomProperty;
import com.foursoft.harness.vec.v12x.VecDoubleValueProperty;
import com.foursoft.harness.vec.v12x.VecIntegerValueProperty;
import com.foursoft.harness.vec.v12x.VecLanguageCode;
import com.foursoft.harness.vec.v12x.VecLocalizedStringProperty;
import com.foursoft.harness.vec.v12x.VecSimpleValueProperty;
import com.foursoft.harness.vec.v12x.VecValueRange;
import com.foursoft.harness.vec.v12x.VecValueRangeProperty;

import java.math.BigInteger;
import java.util.function.Predicate;

/**
 * Navigations starting at a {@link HasCustomProperties} holding {@link VecCustomProperty}s.
 * <p>
 * The value navigations are named after the value they lead to, since the property type given as their
 * argument is what picks it. To reach the properties themselves, start at {@link #toCustomProperties()} and
 * narrow it at the call site, for example
 * <pre>
 * {@code
 * CustomProperties.toCustomProperties().ofType(VecSimpleValueProperty.class);
 * }
 * </pre>
 */
public final class CustomProperties {

    private CustomProperties() {
        // hide default constructor
    }

    /**
     * Navigates to the custom properties of an element.
     *
     * @return A navigation to the custom properties of an element.
     */
    public static MultiNavigation<HasCustomProperties<VecCustomProperty>, VecCustomProperty> toCustomProperties() {
        return Navigations.collection(HasCustomProperties::getCustomProperties);
    }

    /**
     * Navigates to the string value of the {@link VecSimpleValueProperty} with the given property type.
     *
     * @param propertyType Type of the property to navigate to.
     * @return A navigation to the string value of the given property.
     */
    public static SingleNavigation<HasCustomProperties<VecCustomProperty>, String> stringValueOf(
            final String propertyType) {
        return propertiesOf(propertyType, VecSimpleValueProperty.class).atMostOne()
                .then(Navigations.nullable(VecSimpleValueProperty::getValue));
    }

    /**
     * Navigates to the string values of all {@link VecSimpleValueProperty}s with the given property type.
     *
     * @param propertyType Type of the properties to navigate to.
     * @return A navigation to the string values of the given properties.
     */
    public static MultiNavigation<HasCustomProperties<VecCustomProperty>, String> stringValuesOf(
            final String propertyType) {
        return propertiesOf(propertyType, VecSimpleValueProperty.class)
                .then(Navigations.nullable(VecSimpleValueProperty::getValue));
    }

    /**
     * Navigates to the integer value of the {@link VecIntegerValueProperty} with the given property type.
     *
     * @param propertyType Type of the property to navigate to.
     * @return A navigation to the integer value of the given property.
     */
    public static SingleNavigation<HasCustomProperties<VecCustomProperty>, BigInteger> integerValueOf(
            final String propertyType) {
        return propertiesOf(propertyType, VecIntegerValueProperty.class).atMostOne()
                .then(Navigations.nullable(VecIntegerValueProperty::getValue));
    }

    /**
     * Navigates to the double value of the {@link VecDoubleValueProperty} with the given property type.
     *
     * @param propertyType Type of the property to navigate to.
     * @return A navigation to the double value of the given property.
     */
    public static SingleNavigation<HasCustomProperties<VecCustomProperty>, Double> doubleValueOf(
            final String propertyType) {
        return propertiesOf(propertyType, VecDoubleValueProperty.class).atMostOne()
                .then(Navigations.nullable(VecDoubleValueProperty::getValue));
    }

    /**
     * Navigates to the boolean value of the {@link VecBooleanValueProperty} with the given property type.
     *
     * @param propertyType Type of the property to navigate to.
     * @return A navigation to the boolean value of the given property.
     */
    public static SingleNavigation<HasCustomProperties<VecCustomProperty>, Boolean> booleanValueOf(
            final String propertyType) {
        return propertiesOf(propertyType, VecBooleanValueProperty.class).atMostOne()
                .then(Navigations.nullable(VecBooleanValueProperty::isValue));
    }

    /**
     * Navigates to the {@link VecValueRange} of the {@link VecValueRangeProperty} with the given property type.
     *
     * @param propertyType Type of the property to navigate to.
     * @return A navigation to the value range of the given property.
     */
    public static SingleNavigation<HasCustomProperties<VecCustomProperty>, VecValueRange> valueRangeOf(
            final String propertyType) {
        return propertiesOf(propertyType, VecValueRangeProperty.class).atMostOne()
                .then(Navigations.nullable(VecValueRangeProperty::getValue));
    }

    /**
     * Navigates to the custom properties nested in the {@link VecComplexProperty} with the given property type.
     *
     * @param propertyType Type of the complex property to navigate into.
     * @return A navigation to the custom properties of the given complex property.
     */
    public static MultiNavigation<HasCustomProperties<VecCustomProperty>, VecCustomProperty> nestedPropertiesOf(
            final String propertyType) {
        return propertiesOf(propertyType, VecComplexProperty.class).atMostOne()
                .then(Navigations.collection(VecComplexProperty::getCustomProperties));
    }

    /**
     * Navigates to the value of the {@link VecLocalizedStringProperty} with the given property type, if that
     * value is in the given language.
     *
     * @param propertyType Type of the property to navigate to.
     * @param languageCode Language the value has to be in.
     * @return A navigation to the localized value of the given property.
     * @see LocalizedStringProperties#valueIn(VecLanguageCode)
     */
    public static SingleNavigation<HasCustomProperties<VecCustomProperty>, String> localizedValueOf(
            final String propertyType, final VecLanguageCode languageCode) {
        return propertiesOf(propertyType, VecLocalizedStringProperty.class).atMostOne()
                .then(LocalizedStringProperties.valueIn(languageCode));
    }

    private static <T extends VecCustomProperty> MultiNavigation<HasCustomProperties<VecCustomProperty>, T> propertiesOf(
            final String propertyType, final Class<T> type) {
        return toCustomProperties()
                .ofType(type)
                .filter(ofPropertyType(propertyType));
    }

    private static Predicate<VecCustomProperty> ofPropertyType(final String propertyType) {
        return customProperty -> propertyType.equals(customProperty.getPropertyType());
    }

}
