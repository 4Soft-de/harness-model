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
package com.foursoft.harness.kbl2vec.transform.components.wires.wire;

import com.foursoft.harness.kbl.v25.KblCore;
import com.foursoft.harness.kbl.v25.KblGeneralWire;
import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.*;

import java.util.Optional;

import static com.foursoft.harness.kbl2vec.transform.components.common.Fragments.commonComponentInformation;

public class WireDocumentVersionTransformer implements Transformer<KblPart, VecDocumentVersion> {

    @Override
    public TransformationResult<VecDocumentVersion> transform(final TransformationContext context,
                                                              final KblPart source) {
        if (source instanceof final KblGeneralWire wire) {

            final VecDocumentVersion documentVersion = new VecDocumentVersion();

            final TransformationResult.Builder<VecDocumentVersion> builder =
                    TransformationResult.from(documentVersion)
                            .withFragment(commonComponentInformation(source, context))
                            .withDownstream(KblGeneralWire.class, VecWireElementSpecification.class, Query.of(wire),
                                            VecDocumentVersion::getSpecifications)
                            .withDownstream(KblGeneralWire.class, VecInsulationSpecification.class, Query.of(wire),
                                            VecDocumentVersion::getSpecifications)
                            .withDownstream(KblGeneralWire.class, VecWireSpecification.class, Query.of(wire),
                                            VecDocumentVersion::getSpecifications);

            final Optional<KblGeneralWire> singleCoreWire = Optional.of(wire).filter(w -> w.getCores().isEmpty());
            if (singleCoreWire.isPresent()) {
                return builder
                        .withDownstream(KblGeneralWire.class, VecCoreSpecification.class,
                                        () -> singleCoreWire.stream().toList(), VecDocumentVersion::getSpecifications)
                        .build();
            }

            // Multi-Core
            return builder
                    .withDownstream(KblCore.class, VecWireElementSpecification.class, wire::getCores,
                                    VecDocumentVersion::getSpecifications)
                    .withDownstream(KblCore.class, VecInsulationSpecification.class, wire::getCores,
                                    VecDocumentVersion::getSpecifications)
                    .withDownstream(KblCore.class, VecWireSpecification.class, wire::getCores,
                                    VecDocumentVersion::getSpecifications)
                    .withDownstream(KblCore.class, VecCoreSpecification.class, wire::getCores,
                                    VecDocumentVersion::getSpecifications)
                    .build();
        }
        return TransformationResult.noResult();
    }
}
