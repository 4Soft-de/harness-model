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
package com.foursoft.harness.kbl2vec.post;

import com.foursoft.harness.kbl2vec.core.Processor;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.v2x.VecContent;
import com.foursoft.harness.vec.v2x.VecSignal;
import com.foursoft.harness.vec.v2x.VecSignalSpecification;
import com.foursoft.harness.vec.v2x.VecWireElementReference;
import com.foursoft.harness.vec.v2x.visitor.DepthFirstTraverserImpl;
import com.foursoft.harness.vec.v2x.visitor.FunctionVisitor;
import com.foursoft.harness.vec.v2x.visitor.TraversingVisitor;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Collapses {@link VecSignal}s that share the same signal name into a single canonical signal.
 * <p>
 * The {@code SignalTransformer} deliberately emits one signal per {@link com.foursoft.harness.kbl.v25.KblConnection} so
 * that the {@code KblConnection -> VecSignal} entity mapping stays 1:1 and downstream signal linking resolves. Since
 * {@code Signal.identification} must be unique within a {@link VecSignalSpecification}, this post-processor removes the
 * resulting duplicates afterwards and repoints any references (e.g. {@link VecWireElementReference#getSignal()}) to the
 * retained canonical signal.
 * <p>
 * It must run before {@link XmlIdPostProcessor} so that removed duplicates never receive an XML id.
 */
public class SignalDeduplicationPostProcessor implements Processor<VecContent> {

    @Override
    public VecContent apply(final VecContent source, final TransformationContext context) {
        final VecSignalSpecification specification = findSignalSpecification(source);
        if (specification == null) {
            return source;
        }

        final Map<String, VecSignal> canonicalByName = new LinkedHashMap<>();
        final Map<VecSignal, VecSignal> duplicateToCanonical = new IdentityHashMap<>();
        for (final VecSignal signal : specification.getSignals()) {
            final VecSignal canonical = canonicalByName.putIfAbsent(signal.getSignalName(), signal);
            if (canonical != null) {
                duplicateToCanonical.put(signal, canonical);
            }
        }

        if (duplicateToCanonical.isEmpty()) {
            return source;
        }

        for (final VecWireElementReference wireElementReference : collectWireElementReferences(source)) {
            final VecSignal canonical = duplicateToCanonical.get(wireElementReference.getSignal());
            if (canonical != null) {
                wireElementReference.setSignal(canonical);
            }
        }

        specification.getSignals().removeIf(duplicateToCanonical::containsKey);

        return source;
    }

    private static VecSignalSpecification findSignalSpecification(final VecContent content) {
        return content.getDocumentVersions().stream()
                .filter(documentVersion -> "SignalList".equals(documentVersion.getDocumentType()))
                .flatMap(documentVersion -> documentVersion.getSpecifications().stream())
                .filter(VecSignalSpecification.class::isInstance)
                .map(VecSignalSpecification.class::cast)
                .findFirst()
                .orElse(null);
    }

    private static List<VecWireElementReference> collectWireElementReferences(final VecContent content) {
        final List<VecWireElementReference> references = new ArrayList<>();
        final FunctionVisitor<Identifiable, Void> visitor = new FunctionVisitor<>(element -> {
            if (element instanceof final VecWireElementReference wireElementReference) {
                references.add(wireElementReference);
            }
            return null;
        });
        content.accept(new TraversingVisitor<>(new DepthFirstTraverserImpl<>(), visitor));
        return references;
    }
}
