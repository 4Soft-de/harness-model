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
import com.foursoft.harness.vec.v2x.VecWireElementReference;
import com.foursoft.harness.vec.v2x.VecWireRole;
import com.foursoft.harness.vec.v2x.VecWireSpecification;

public class WireRoleTransformer implements Transformer<KblGeneralWireOccurrence, VecWireRole> {
    @Override
    public TransformationResult<VecWireRole> transform(final TransformationContext context,
                                                       final KblGeneralWireOccurrence sourceWireOccurrence) {

        final VecWireRole dest = new VecWireRole();
        final TransformationResult.Builder<VecWireRole> builder = TransformationResult.from(dest);

        if (sourceWireOccurrence instanceof final KblWireOccurrence wireOccurrence) {
            dest.setIdentification(wireOccurrence.getWireNumber());
        } else if (sourceWireOccurrence instanceof final KblSpecialWireOccurrence specialWireOccurrence) {
            dest.setIdentification(specialWireOccurrence.getSpecialWireId());
            builder.withDownstream(KblCoreOccurrence.class, VecWireElementReference.class,
                                   specialWireOccurrence::getCoreOccurrences, VecWireRole::getWireElementReferences);
        } else {
            context.getLogger().warn("'{}' has a unsupported wire class type", sourceWireOccurrence);
        }

        return builder
                .withDownstream(KblGeneralWireOccurrence.class, VecWireElementReference.class,
                                Query.of(sourceWireOccurrence), VecWireRole::getWireElementReferences)
                .withLinker(Query.of(sourceWireOccurrence::getPart), VecWireSpecification.class,
                            VecWireRole::setWireSpecification)
                .build();
    }
}
