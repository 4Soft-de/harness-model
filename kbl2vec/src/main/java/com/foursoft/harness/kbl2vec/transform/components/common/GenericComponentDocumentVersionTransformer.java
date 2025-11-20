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
package com.foursoft.harness.kbl2vec.transform.components.common;

import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;

import static com.foursoft.harness.kbl2vec.transform.components.common.Fragments.commonComponentInformation;

/**
 * Transformer for generic KBL parts to VEC document versions.
 * <p>
 * Filters out KBL part types that are handled by specific transformers
 * (such as {@link KblAssemblyPart}, {@link KblConnectorHousing}, and {@link KblGeneralWire}).
 * For unsupported types, a generic PartMaster Document without specific specifications will be created.
 */
public class GenericComponentDocumentVersionTransformer implements Transformer<KblPart, VecDocumentVersion> {

    @Override
    public TransformationResult<VecDocumentVersion> transform(final TransformationContext context,
                                                              final KblPart source) {
        if (source instanceof KblAssemblyPart || source instanceof KblConnectorHousing ||
                source instanceof KblGeneralWire || source instanceof KblWireProtection ||
                source instanceof KblCoPackPart || source instanceof KblCavityPlug || source instanceof KblCavitySeal ||
                source instanceof KblAccessory || source instanceof KblFixing || source instanceof KblGeneralTerminal ||
                source instanceof KblComponentBox) {
            return TransformationResult.noResult();
        }
        context.getLogger().warn(
                "The class of {} is not supported specifically by KBL2VEC, a generic PartMasterDocument without " +
                        "specific Specifications will be created.", source);
        final VecDocumentVersion documentVersion = new VecDocumentVersion();

        final TransformationResult.Builder<VecDocumentVersion> builder = TransformationResult.from(
                        documentVersion)
                .withFragment(commonComponentInformation(source, context));

        return builder
                .build();
    }
}
