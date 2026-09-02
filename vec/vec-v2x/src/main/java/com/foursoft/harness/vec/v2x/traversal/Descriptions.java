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
import com.foursoft.harness.vec.common.traversal.MultiNavigation;
import com.foursoft.harness.vec.common.traversal.Navigations;
import com.foursoft.harness.vec.common.traversal.SingleNavigation;
import com.foursoft.harness.vec.v2x.VecAbstractLocalizedString;
import com.foursoft.harness.vec.v2x.VecLanguageCode;
import com.foursoft.harness.vec.v2x.VecLocalizedTypedString;

/**
 * Navigations starting at a {@link HasDescription} holding {@link VecAbstractLocalizedString}s.
 * <p>
 * Which of several descriptions applies is decided by a reduction from {@link LocalizedStrings}, not by the
 * navigation itself.
 */
public final class Descriptions {

    private Descriptions() {
        // hide default constructor
    }

    /**
     * Navigates to the localized strings describing an element.
     *
     * @return A navigation to the descriptions of an element.
     */
    public static MultiNavigation<HasDescription<? extends VecAbstractLocalizedString>,
            VecAbstractLocalizedString> toDescriptions() {
        return Navigations.collection(HasDescription::getDescriptions);
    }

    /**
     * Navigates to the german description of an element.
     *
     * @return A navigation to the german description.
     * @see #descriptionIn(VecLanguageCode)
     */
    public static SingleNavigation<HasDescription<? extends VecAbstractLocalizedString>, String> germanDescription() {
        return descriptionIn(VecLanguageCode.DE);
    }

    /**
     * Navigates to the english description of an element.
     *
     * @return A navigation to the english description.
     * @see #descriptionIn(VecLanguageCode)
     */
    public static SingleNavigation<HasDescription<? extends VecAbstractLocalizedString>, String> englishDescription() {
        return descriptionIn(VecLanguageCode.EN);
    }

    /**
     * Navigates to the description of an element in the given language.
     *
     * @param languageCode Language of the description to navigate to.
     * @return A navigation to the description in the given language.
     * @see LocalizedStrings#valueIn(VecLanguageCode)
     */
    public static SingleNavigation<HasDescription<? extends VecAbstractLocalizedString>, String> descriptionIn(
            final VecLanguageCode languageCode) {
        return toDescriptions().collect(LocalizedStrings.valueIn(languageCode));
    }

    /**
     * Navigates to the german {@link VecLocalizedTypedString} of the given type.
     *
     * @param descriptionType Type of the localized string to navigate to.
     * @return A navigation to the german value of the given type.
     * @see #typedStringBy(String, VecLanguageCode)
     */
    public static SingleNavigation<HasDescription<? extends VecAbstractLocalizedString>, String> germanTypedStringBy(
            final String descriptionType) {
        return typedStringBy(descriptionType, VecLanguageCode.DE);
    }

    /**
     * Navigates to the english {@link VecLocalizedTypedString} of the given type.
     *
     * @param descriptionType Type of the localized string to navigate to.
     * @return A navigation to the english value of the given type.
     * @see #typedStringBy(String, VecLanguageCode)
     */
    public static SingleNavigation<HasDescription<? extends VecAbstractLocalizedString>, String> englishTypedStringBy(
            final String descriptionType) {
        return typedStringBy(descriptionType, VecLanguageCode.EN);
    }

    /**
     * Navigates to the {@link VecLocalizedTypedString} of the given type in the given language.
     *
     * @param descriptionType Type of the localized string to navigate to.
     * @param languageCode    Language of the localized string to navigate to.
     * @return A navigation to the value of the given type and language.
     * @see LocalizedStrings#typedValueBy(String, VecLanguageCode)
     */
    public static SingleNavigation<HasDescription<? extends VecAbstractLocalizedString>, String> typedStringBy(
            final String descriptionType, final VecLanguageCode languageCode) {
        return toDescriptions().collect(LocalizedStrings.typedValueBy(descriptionType, languageCode));
    }

}
