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
package com.foursoft.harness.kbl2vec.transform.components.terminals;

import com.foursoft.harness.kbl.v25.KblTerminalOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecTerminalReceptionReference;
import com.foursoft.harness.vec.v2x.VecTerminalRole;
import com.foursoft.harness.vec.v2x.VecTerminalSpecification;
import com.foursoft.harness.vec.v2x.VecWireReceptionReference;

public class TerminalRoleTransformer implements Transformer<KblTerminalOccurrence, VecTerminalRole> {

    @Override
    public TransformationResult<VecTerminalRole> transform(final TransformationContext context,
                                                           final KblTerminalOccurrence source) {
        final VecTerminalRole destination = new VecTerminalRole();
        destination.setIdentification(source.getId());

        return TransformationResult.from(destination)
                .withDownstream(KblTerminalOccurrence.class, VecTerminalReceptionReference.class, Query.of(source),
                                VecTerminalRole::getTerminalReceptionReferences)
                .withDownstream(KblTerminalOccurrence.class, VecWireReceptionReference.class, Query.of(source),
                                VecTerminalRole::getWireReceptionReferences)
                .withLinker(Query.of(source::getPart), VecTerminalSpecification.class,
                            VecTerminalRole::setTerminalSpecification)
                .build();
    }
}
