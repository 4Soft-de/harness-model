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

import com.foursoft.harness.vec.common.traversal.Navigations;
import com.foursoft.harness.vec.common.traversal.SingleNavigation;
import com.foursoft.harness.vec.v2x.VecLanguageCode;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecLocalizedStringProperty;
import com.foursoft.harness.vec.v2x.predicates.VecPredicates;

/**
 * Navigations starting at a {@link VecLocalizedStringProperty}.
 * <p>
 * A localized string property holds exactly one {@link VecLocalizedString}, so choosing between several
 * languages does not arise here; the language is a condition on the one value, not a selection among many.
 * Reducing several localized strings to one value is what {@link LocalizedStrings} is for.
 */
public final class LocalizedStringProperties {

    private LocalizedStringProperties() {
        // hide default constructor
    }

    /**
     * Navigates to the value of a localized string property, if it is in the given language.
     *
     * @param languageCode Language the value has to be in.
     * @return A navigation to the value in the given language.
     */
    public static SingleNavigation<VecLocalizedStringProperty, String> valueIn(final VecLanguageCode languageCode) {
        return Navigations.nullable(VecLocalizedStringProperty::getValue)
                .filter(VecPredicates.languageCode(languageCode))
                .then(Navigations.nullable(VecLocalizedString::getValue));
    }

}
