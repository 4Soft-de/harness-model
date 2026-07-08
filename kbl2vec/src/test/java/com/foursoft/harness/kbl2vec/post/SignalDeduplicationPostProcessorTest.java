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

import com.foursoft.harness.vec.v2x.VecCompositionSpecification;
import com.foursoft.harness.vec.v2x.VecContent;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecSignal;
import com.foursoft.harness.vec.v2x.VecSignalSpecification;
import com.foursoft.harness.vec.v2x.VecWireElementReference;
import com.foursoft.harness.vec.v2x.VecWireRole;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SignalDeduplicationPostProcessorTest {

    @Test
    void should_collapseDuplicateSignalsAndRepointReferences() {
        // Given
        final SignalDeduplicationPostProcessor processor = new SignalDeduplicationPostProcessor();

        final VecSignal firstA = signal("A");
        final VecSignal secondA = signal("A");
        final VecSignal onlyB = signal("B");
        final VecSignalSpecification specification = new VecSignalSpecification();
        specification.getSignals().addAll(List.of(firstA, secondA, onlyB));

        final VecDocumentVersion signalListDocument = new VecDocumentVersion();
        signalListDocument.setDocumentType("SignalList");
        signalListDocument.getSpecifications().add(specification);

        // A wire element reference pointing at the duplicate signal, reachable from the content graph.
        final VecWireElementReference wireElementReference = new VecWireElementReference();
        wireElementReference.setSignal(secondA);
        final VecWireRole wireRole = new VecWireRole();
        wireRole.getWireElementReferences().add(wireElementReference);
        final VecPartOccurrence occurrence = new VecPartOccurrence();
        occurrence.getRoles().add(wireRole);
        final VecCompositionSpecification compositionSpecification = new VecCompositionSpecification();
        compositionSpecification.getComponents().add(occurrence);
        final VecDocumentVersion harnessDocument = new VecDocumentVersion();
        harnessDocument.setDocumentType("HarnessDescription");
        harnessDocument.getSpecifications().add(compositionSpecification);

        final VecContent content = new VecContent();
        content.getDocumentVersions().addAll(List.of(signalListDocument, harnessDocument));

        // When
        final VecContent result = processor.apply(content, null);

        // Then
        assertThat(result).isSameAs(content);
        assertThat(specification.getSignals()).containsExactly(firstA, onlyB);
        assertThat(wireElementReference.getSignal()).isSameAs(firstA);
    }

    private static VecSignal signal(final String signalName) {
        final VecSignal signal = new VecSignal();
        signal.setIdentification(signalName);
        signal.setSignalName(signalName);
        return signal;
    }
}
