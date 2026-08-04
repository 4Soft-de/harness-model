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

import com.foursoft.harness.vec.v12x.VecLanguageCode;
import com.foursoft.harness.vec.v12x.VecLocalizedString;
import com.foursoft.harness.vec.v12x.VecLocalizedStringProperty;
import com.foursoft.harness.vec.v12x.navigations.CustomPropertyNavs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LocalizedStringPropertiesTest {

    private final VecLocalizedStringProperty property = new VecLocalizedStringProperty();

    @BeforeEach
    void setUp() {
        final VecLocalizedString german = new VecLocalizedString();
        german.setLanguageCode(VecLanguageCode.DE);
        german.setValue("Kabelbaum");
        property.setValue(german);
    }

    @Test
    void navigatesToTheValueOfTheRequestedLanguage() {
        assertThat(LocalizedStringProperties.valueIn(VecLanguageCode.DE).from(property)).contains("Kabelbaum");
    }

    @Test
    void navigatesNowhereForAnotherLanguage() {
        assertThat(LocalizedStringProperties.valueIn(VecLanguageCode.EN).from(property)).isEmpty();
    }

    @Test
    void navigatesNowhereForAPropertyWithoutValue() {
        assertThat(LocalizedStringProperties.valueIn(VecLanguageCode.DE).from(new VecLocalizedStringProperty()))
                .isEmpty();
    }

    /**
     * Characterisation test for the deprecated {@link CustomPropertyNavs}, which delegates to
     * {@link LocalizedStringProperties}. Can be removed together with {@link CustomPropertyNavs}.
     */
    @Test
    @SuppressWarnings({"deprecation", "removal"})
    void deprecatedNavigationsBehaveLikeTheirReplacement() {
        assertThat(CustomPropertyNavs.convertLocalizedStringProperty(VecLanguageCode.DE).apply(property))
                .isEqualTo(LocalizedStringProperties.valueIn(VecLanguageCode.DE).from(property));
        assertThat(CustomPropertyNavs.convertLocalizedStringProperty(VecLanguageCode.EN).apply(property))
                .isEqualTo(LocalizedStringProperties.valueIn(VecLanguageCode.EN).from(property));
        assertThat(CustomPropertyNavs.convertLocalizedStringProperty(VecLanguageCode.DE).apply(null)).isEmpty();
    }

}
