/*-
 * ========================LICENSE_START=================================
 * VEC to AAS Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
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
package com.foursoft.harness.vec.aas.wrapper;

import com.foursoft.harness.vec.aas.AasConversionException;
import org.eclipse.digitaltwin.aas4j.v3.model.LangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangStringTextType;

import java.util.Set;

public class LocalizedStringWrapper extends GenericVecObjectWrapper {
    private static final Set<String> LOCALIZED_TYPES = Set.of("VecAbstractLocalizedString", "VecLocalizedString",
                                                              "VecLocalizedTypedString");

    private LocalizedStringWrapper(final Object contextObject) {
        super(contextObject);
        if (!isLocalizedType(contextObject.getClass())) {
            throw new AasConversionException("Cannot convert context object to a LocalizedString: " + contextObject);
        }
    }

    public String getLanguage() {
        return getFieldValue("languageCode").toString().toLowerCase();
    }

    public String getValue() {
        return getFieldValue("value").toString();
    }

    public String getType() {
        if (getFields().containsKey("type")) {
            return getFieldValue("type").toString();
        }
        return "";
    }

    public LangStringTextType toLangStringTextType() {
        return new DefaultLangStringTextType.Builder()
                .language(getLanguage())
                .text(getValue())
                .build();
    }

    public static LocalizedStringWrapper wrap(final Object contextObject) {
        return new LocalizedStringWrapper(contextObject);
    }

    public static boolean isLocalizedType(final Class<?> clazz) {
        return LOCALIZED_TYPES.contains(clazz.getSimpleName());
    }

}
