/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
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
package com.foursoft.harness.kbl2vec.core;

import com.foursoft.harness.vec.v2x.VecLanguageCode;
import com.foursoft.harness.vec.v2x.VecValueDetermination;

public class ConversionProperties {
    private VecLanguageCode defaultLanguageCode = VecLanguageCode.DE;
    private String defaultMassInformationSource = "Series";
    private VecValueDetermination defaultValueDetermination = VecValueDetermination.MEASURED;
    private String defaultColorReferenceSystem = "ACME Inc.";

    public VecLanguageCode getDefaultLanguageCode() {
        return defaultLanguageCode;
    }

    public void setDefaultLanguageCode(final VecLanguageCode defaultLanguageCode) {
        this.defaultLanguageCode = defaultLanguageCode;
    }

    public String getDefaultMassInformationSource() {
        return defaultMassInformationSource;
    }

    public void setDefaultMassInformationSource(final String defaultMassInformationSource) {
        this.defaultMassInformationSource = defaultMassInformationSource;
    }

    public VecValueDetermination getDefaultValueDetermination() {
        return defaultValueDetermination;
    }

    public void setDefaultValueDetermination(final VecValueDetermination defaultValueDetermination) {
        this.defaultValueDetermination = defaultValueDetermination;
    }

    public String getDefaultColorReferenceSystem() {
        return defaultColorReferenceSystem;
    }

    public void setDefaultColorReferenceSystem(final String defaultColorReferenceSystem) {
        this.defaultColorReferenceSystem = defaultColorReferenceSystem;
    }
}
