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

import com.foursoft.harness.kbl.v25.KblConnection;
import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecSignal;
import com.foursoft.harness.vec.v2x.VecSignalSpecification;
import com.google.common.base.Strings;

/**
 * Creates the single {@link VecSignalSpecification} of the signal list document and populates it with one
 * {@link VecSignal} per {@link KblConnection} that carries a (non-blank) signal name.
 * <p>
 * The KBL carries one connection per signal occurrence, so several connections can share the same
 * {@code Signal_name}. Because {@code VecSignal.identification} must be unique within the specification, the
 * downstream transformation deduplicates the produced signals by {@link VecSignal#getSignalName()}: only the first
 * signal per name is kept, and every duplicate connection is re-pointed in the entity mapping to that retained
 * signal. This keeps a clean {@code KblConnection -> VecSignal} mapping so that downstream signal linking (e.g.
 * from {@code WireElementReference}) resolves to the retained signal via the entity mapping.
 */
public class SignalSpecificationTransformer implements Transformer<KblHarness, VecSignalSpecification> {

    @Override
    public TransformationResult<VecSignalSpecification> transform(final TransformationContext context,
                                                                  final KblHarness source) {
        final VecSignalSpecification specification = new VecSignalSpecification();
        specification.setIdentification("Signals");

        return TransformationResult.from(specification)
                .withDownstream(KblConnection.class, VecSignal.class,
                                () -> source.getConnections().stream()
                                        .filter(connection -> !Strings.isNullOrEmpty(connection.getSignalName()))
                                        .toList(),
                                VecSignalSpecification::getSignals,
                                VecSignal::getSignalName,
                                null)
                .build();
    }
}
