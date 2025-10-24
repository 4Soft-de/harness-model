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
package com.foursoft.harness.kbl2vec.transform.contacting;

import com.foursoft.harness.kbl.v25.KblContactPoint;
import com.foursoft.harness.kbl.v25.KblProcessingInstruction;
import com.foursoft.harness.kbl.v25.KblTerminalOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v2x.*;

import java.util.List;

public class ContactPointTransformer implements Transformer<KblContactPoint, VecContactPoint> {

    @Override
    public TransformationResult<VecContactPoint> transform(final TransformationContext context,
                                                           final KblContactPoint source) {
        final VecContactPoint destination = new VecContactPoint();
        destination.setIdentification(source.getId());

        return TransformationResult.from(destination)
                .withDownstream(KblContactPoint.class, VecCavityMounting.class, Query.of(source),
                                VecContactPoint::getCavityMountings)
                .withDownstream(KblProcessingInstruction.class, VecCustomProperty.class,
                                source::getProcessingInformations, VecContactPoint::getCustomProperties)
                .withDownstream(KblContactPoint.class, VecWireMounting.class, Query.of(source),
                                VecContactPoint::getWireMountings)
                .withLinker(Query.fromLists(getTerminalOccurrence(source, context)), VecTerminalRole.class,
                            VecContactPoint::setMountedTerminal)
                .build();
    }

    private List<KblTerminalOccurrence> getTerminalOccurrence(final KblContactPoint contactPoint,
                                                              final TransformationContext context) {
        final List<KblTerminalOccurrence> terminalOccurrences = contactPoint.getAssociatedParts().stream()
                .flatMap(StreamUtils.ofClass(KblTerminalOccurrence.class))
                .toList();

        if (terminalOccurrences.size() > 1) {
            context.getLogger().warn("Multiple terminal occurrences found for '{}-{}'.",
                                     contactPoint.getClass().getSimpleName(), contactPoint.getId());
            return List.of(terminalOccurrences.get(0));
        }
        return terminalOccurrences;
    }
}
