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

import com.foursoft.harness.vec.common.HasCustomProperties;
import com.foursoft.harness.vec.v2x.VecBooleanValueProperty;
import com.foursoft.harness.vec.v2x.VecComplexProperty;
import com.foursoft.harness.vec.v2x.VecCustomProperty;
import com.foursoft.harness.vec.v2x.VecDoubleValueProperty;
import com.foursoft.harness.vec.v2x.VecIntegerValueProperty;
import com.foursoft.harness.vec.v2x.VecLanguageCode;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecLocalizedStringProperty;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecSimpleValueProperty;
import com.foursoft.harness.vec.v2x.VecValueRange;
import com.foursoft.harness.vec.v2x.VecValueRangeProperty;
import com.foursoft.harness.vec.v2x.navigations.CustomPropertyNavs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

class CustomPropertiesTest {

    private static final String COLOUR = "Colour";
    private static final String COUNT = "Count";
    private static final String LENGTH = "Length";
    private static final String RELEASED = "Released";
    private static final String TOLERANCE = "Tolerance";
    private static final String DIMENSIONS = "Dimensions";
    private static final String LABEL = "Label";

    private final VecPartOccurrence element = new VecPartOccurrence();

    private VecValueRange tolerance;
    private VecCustomProperty nested;

    private static <T extends VecCustomProperty> T property(final T property, final String propertyType) {
        property.setPropertyType(propertyType);
        return property;
    }

    private void add(final VecCustomProperty property) {
        element.getCustomProperties().add(property);
    }

    @BeforeEach
    void setUp() {
        final VecSimpleValueProperty colour = property(new VecSimpleValueProperty(), COLOUR);
        colour.setValue("black");
        final VecSimpleValueProperty secondColour = property(new VecSimpleValueProperty(), COLOUR);
        secondColour.setValue("white");
        final VecIntegerValueProperty count = property(new VecIntegerValueProperty(), COUNT);
        count.setValue(BigInteger.valueOf(3));
        final VecDoubleValueProperty length = property(new VecDoubleValueProperty(), LENGTH);
        length.setValue(12.5);
        final VecBooleanValueProperty released = property(new VecBooleanValueProperty(), RELEASED);
        released.setValue(true);

        tolerance = new VecValueRange();
        final VecValueRangeProperty toleranceProperty = property(new VecValueRangeProperty(), TOLERANCE);
        toleranceProperty.setValue(tolerance);

        nested = property(new VecSimpleValueProperty(), "Width");
        final VecComplexProperty dimensions = property(new VecComplexProperty(), DIMENSIONS);
        dimensions.getCustomProperties().add(nested);

        final VecLocalizedString german = new VecLocalizedString();
        german.setLanguageCode(VecLanguageCode.DE);
        german.setValue("Kabelbaum");
        final VecLocalizedStringProperty label = property(new VecLocalizedStringProperty(), LABEL);
        label.setValue(german);

        add(colour);
        add(secondColour);
        add(count);
        add(length);
        add(released);
        add(toleranceProperty);
        add(dimensions);
        add(label);
    }

    @Test
    void navigatesToTheCustomPropertiesOfAnElement() {
        assertThat(CustomProperties.toCustomProperties().listFrom(element)).hasSize(8);
        assertThat(CustomProperties.toCustomProperties().listFrom(new VecPartOccurrence())).isEmpty();
    }

    @Test
    void navigatesToTheValueOfTheRequestedProperty() {
        assertThat(CustomProperties.stringValueOf(COLOUR).from(element)).contains("black");
        assertThat(CustomProperties.integerValueOf(COUNT).from(element)).contains(BigInteger.valueOf(3));
        assertThat(CustomProperties.doubleValueOf(LENGTH).from(element)).contains(12.5);
        assertThat(CustomProperties.booleanValueOf(RELEASED).from(element)).contains(true);
        assertThat(CustomProperties.valueRangeOf(TOLERANCE).from(element)).contains(tolerance);
    }

    @Test
    void navigatesToAllValuesOfTheRequestedProperty() {
        assertThat(CustomProperties.stringValuesOf(COLOUR).listFrom(element)).containsExactly("black", "white");
    }

    @Test
    void navigatesToThePropertiesNestedInTheRequestedComplexProperty() {
        assertThat(CustomProperties.nestedPropertiesOf(DIMENSIONS).listFrom(element)).containsExactly(nested);
    }

    @Test
    void navigatesToTheLocalizedValueOfTheRequestedProperty() {
        assertThat(CustomProperties.localizedValueOf(LABEL, VecLanguageCode.DE).from(element))
                .contains("Kabelbaum");
        assertThat(CustomProperties.localizedValueOf(LABEL, VecLanguageCode.EN).from(element)).isEmpty();
    }

    @Test
    void navigatesNowhereForAnUnknownProperty() {
        assertThat(CustomProperties.stringValueOf("Unknown").from(element)).isEmpty();
        assertThat(CustomProperties.stringValuesOf("Unknown").listFrom(element)).isEmpty();
        assertThat(CustomProperties.nestedPropertiesOf("Unknown").listFrom(element)).isEmpty();
        assertThat(CustomProperties.localizedValueOf("Unknown", VecLanguageCode.DE).from(element)).isEmpty();
    }

    @Test
    void navigatesNowhereForAPropertyOfAnotherKind() {
        assertThat(CustomProperties.integerValueOf(COLOUR).from(element)).isEmpty();
        assertThat(CustomProperties.stringValueOf(COUNT).from(element)).isEmpty();
    }

    /**
     * Characterisation test for the deprecated {@link CustomPropertyNavs}, which delegates to
     * {@link CustomProperties}. Can be removed together with {@link CustomPropertyNavs}.
     */
    @Test
    @SuppressWarnings({"deprecation", "removal"})
    void deprecatedNavigationsBehaveLikeTheirReplacement() {
        final HasCustomProperties<VecCustomProperty> holder = element;

        assertThat(CustomPropertyNavs.customPropertyValueStringOf(COLOUR).apply(holder))
                .isEqualTo(CustomProperties.stringValueOf(COLOUR).from(element));
        assertThat(CustomPropertyNavs.customPropertyValueStringsOf(COLOUR).apply(holder))
                .isEqualTo(CustomProperties.stringValuesOf(COLOUR).listFrom(element));
        assertThat(CustomPropertyNavs.customPropertyValueIntegerOf(COUNT).apply(holder))
                .isEqualTo(CustomProperties.integerValueOf(COUNT).from(element));
        assertThat(CustomPropertyNavs.customPropertyValueDoubleOf(LENGTH).apply(holder))
                .isEqualTo(CustomProperties.doubleValueOf(LENGTH).from(element));
        assertThat(CustomPropertyNavs.customPropertyValueBooleanOf(RELEASED).apply(holder))
                .isEqualTo(CustomProperties.booleanValueOf(RELEASED).from(element));
        assertThat(CustomPropertyNavs.customPropertyValueRangeOf(TOLERANCE).apply(holder))
                .isEqualTo(CustomProperties.valueRangeOf(TOLERANCE).from(element));
        assertThat(CustomPropertyNavs.customPropertyValuesOf(DIMENSIONS).apply(holder))
                .isEqualTo(CustomProperties.nestedPropertiesOf(DIMENSIONS).listFrom(element));
        assertThat(CustomPropertyNavs.customPropertyValueLocalizedString(LABEL, VecLanguageCode.DE).apply(holder))
                .isEqualTo(CustomProperties.localizedValueOf(LABEL, VecLanguageCode.DE).from(element));
    }

}
