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
package com.foursoft.harness.kbl2vec.transform.signal;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecLanguageCode;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecSignalSpecification;

/**
 * Creates a dedicated, standalone {@link VecDocumentVersion} (the "Signal list" document) that holds a single
 * {@link VecSignalSpecification} with the signals derived from the harness' connections.
 * <p>
 * This transformer shares the {@code KblHarness -> VecDocumentVersion} registration with
 * {@code HarnessDocumentVersionTransformer}; both are picked up by the reflection based registry and emitted by the
 * existing downstream declaration in the root {@code VecContentTransformer}.
 */
public class SignalListDocumentVersionTransformer implements Transformer<KblHarness, VecDocumentVersion> {

    @Override
    public TransformationResult<VecDocumentVersion> transform(final TransformationContext context,
                                                              final KblHarness source) {
        final VecDocumentVersion documentVersion = new VecDocumentVersion();

        documentVersion.setDocumentType("SignalList");
        documentVersion.setDocumentNumber("Signals");
        documentVersion.setDocumentVersion("1");
        documentVersion.setCompanyName(context.getConversionProperties().getDefaultSignalListCompanyName());
        documentVersion.getDescriptions().add(localizedString(VecLanguageCode.DE, "Signalliste"));
        documentVersion.getDescriptions().add(localizedString(VecLanguageCode.EN, "Signal list"));

        return TransformationResult.from(documentVersion)
                .withDownstream(KblHarness.class, VecSignalSpecification.class, Query.of(source),
                                VecDocumentVersion::getSpecifications)
                .build();
    }

    private static VecLocalizedString localizedString(final VecLanguageCode languageCode, final String value) {
        final VecLocalizedString localizedString = new VecLocalizedString();
        localizedString.setLanguageCode(languageCode);
        localizedString.setValue(value);
        return localizedString;
    }
}
