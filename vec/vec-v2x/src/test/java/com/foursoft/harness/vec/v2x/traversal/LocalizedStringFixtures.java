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

import com.foursoft.harness.vec.v2x.VecAbstractLocalizedString;
import com.foursoft.harness.vec.v2x.VecLanguageCode;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecLocalizedTypedString;

import java.util.Collections;
import java.util.List;

/**
 * Localized string fixtures shared by {@link LocalizedStringsTest} and {@link DescriptionsTest}.
 */
final class LocalizedStringFixtures {

    static final String LENGTH_TYPE = "Length";

    private LocalizedStringFixtures() {
        // hide default constructor
    }

    static VecLocalizedString localizedString(final VecLanguageCode languageCode, final String value) {
        final VecLocalizedString localizedString = new VecLocalizedString();
        localizedString.setLanguageCode(languageCode);
        localizedString.setValue(value);
        return localizedString;
    }

    static VecLocalizedTypedString typedString(final VecLanguageCode languageCode, final String type,
                                               final String value) {
        final VecLocalizedTypedString typedString = new VecLocalizedTypedString();
        typedString.setLanguageCode(languageCode);
        typedString.setType(type);
        typedString.setValue(value);
        return typedString;
    }

    /**
     * The description list shapes the navigations treat differently, for exhaustive comparisons of the
     * deprecated navigations against their replacements.
     */
    static List<List<VecAbstractLocalizedString>> allVariants() {
        return List.of(
                Collections.emptyList(),
                List.of(localizedString(VecLanguageCode.EN, "Wire")),
                List.of(typedString(VecLanguageCode.DE, LENGTH_TYPE, "100")),
                List.of(typedString(VecLanguageCode.DE, null, "Leitung")),
                List.of(typedString(VecLanguageCode.DE, LENGTH_TYPE, ""),
                        localizedString(VecLanguageCode.DE, "Leitung")),
                List.of(localizedString(VecLanguageCode.DE, "Leitung"),
                        localizedString(VecLanguageCode.EN, "Wire"),
                        typedString(VecLanguageCode.DE, LENGTH_TYPE, "100")));
    }

}
