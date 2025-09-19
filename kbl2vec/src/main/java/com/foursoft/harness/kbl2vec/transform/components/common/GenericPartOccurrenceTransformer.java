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

import com.foursoft.harness.kbl.common.HasPart;
import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;

import static com.foursoft.harness.kbl2vec.transform.components.common.Fragments.commonOccurrenceInformation;

public class GenericPartOccurrenceTransformer implements Transformer<ConnectionOrOccurrence, VecPartOccurrence> {

    @Override
    public TransformationResult<VecPartOccurrence> transform(final TransformationContext context,
                                                             final ConnectionOrOccurrence source) {
        if (source instanceof KblConnection) {
            // KBL Connections do not have a direct representation in VEC
            return TransformationResult.noResult();
        }
        if (source instanceof KblGeneralWireOccurrence || source instanceof KblConnectorOccurrence ||
                source instanceof KblAssemblyPartOccurrence || source instanceof KblCavitySealOccurrence ||
                source instanceof KblCavityPlugOccurrence) {
            return TransformationResult.noResult();
        }
        context.getLogger().warn(
                "The class of {} is not supported specifically by KBL2VEC, a generic PartOccurrence without roles " +
                        "will be created.", source);
        if (source instanceof final HasPart<?> hasPart) {
            final VecPartOccurrence occurrence = new VecPartOccurrence();
            final TransformationResult.Builder<VecPartOccurrence> builder = TransformationResult.from(occurrence);

            return builder
                    .withFragment(commonOccurrenceInformation(hasPart, context))
                    .build();
        }
        return TransformationResult.noResult();
    }
}
