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
package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.KBLContainer;
import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl.v25.KblUnit;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecContent;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecUnit;

import static com.foursoft.harness.kbl2vec.transform.Queries.allParts;

public class VecContentTransformer implements Transformer<KBLContainer, VecContent> {
    @Override
    public TransformationResult<VecContent> transform(final TransformationContext context, final KBLContainer source) {
        final VecContent resultElement = new VecContent();

        resultElement.setVecVersion("2.1.0");
        resultElement.setGeneratingSystemName("4Soft KBL2VEC Converter");
        resultElement.setGeneratingSystemVersion("0.0.1");

        return TransformationResult.from(resultElement)
                .withComment(
                        """
                                WARNING:
                                This file was created with KBL2VEC PoC Converter (https://github.com/4Soft-de/harness-model).
                                This is just a showcase implementation for KBL mapping in the VEC.
                                This is not intended for productive use and is not complete!
                                """)
//                .downstreamTransformation(KblPart.class, VecCopyrightInformation.class, allParts(source),
//                                          resultElement::getCopyrightInformations)
                .withDownstream(KblUnit.class, VecUnit.class, source::getUnits, VecContent::getUnits)
                .withDownstream(KblPart.class, VecPartVersion.class, allParts(source),
                                VecContent::getPartVersions)
                .withDownstream(KblPart.class, VecDocumentVersion.class, source::getParts,
                                VecContent::getDocumentVersions)
                .withDownstream(KblHarness.class, VecDocumentVersion.class, Query.of(source::getHarness),
                                VecContent::getDocumentVersions)
                .build();
    }

}
