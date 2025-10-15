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
package com.foursoft.harness.kbl2vec.transform.components.wires;

import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecWireElement;
import com.foursoft.harness.vec.v2x.VecWireElementReference;
import com.foursoft.harness.vec.v2x.VecWireEnd;
import com.foursoft.harness.vec.v2x.VecWireLength;

import java.util.ArrayList;
import java.util.List;

public class WireWireElementReferenceTransformer
        implements Transformer<KblGeneralWireOccurrence, VecWireElementReference> {

    @Override
    public TransformationResult<VecWireElementReference> transform(final TransformationContext context,
                                                                   final KblGeneralWireOccurrence source) {
        final VecWireElementReference dest = new VecWireElementReference();

        if (source instanceof final KblWireOccurrence wireOccurrence) {
            dest.setIdentification(wireOccurrence.getWireNumber());
        } else if (source instanceof final KblSpecialWireOccurrence specialWireOccurrence) {
            dest.setIdentification(specialWireOccurrence.getSpecialWireId());
        } else {
            context.getLogger().warn("'{}' has a unsupported wire class type", source);
        }

        return TransformationResult.from(dest)
                .withDownstream(KblWireLength.class, VecWireLength.class, source::getLengthInformations,
                                VecWireElementReference::getWireLengths)
                .withDownstream(KblExtremity.class, VecWireEnd.class, () -> getExtremities(source),
                                VecWireElementReference::getWireEnds)
                .withLinker(Query.of(source::getPart), VecWireElement.class,
                            VecWireElementReference::setReferencedWireElement)
                .build();
    }

    private List<KblExtremity> getExtremities(final KblGeneralWireOccurrence source) {
        if (source instanceof final KblWireOccurrence wireOccurrence) {
            return wireOccurrence.getRefConnection().stream()
                    .flatMap(v -> v.getExtremities().stream())
                    .toList();
        }
        return new ArrayList<>();
    }
}
