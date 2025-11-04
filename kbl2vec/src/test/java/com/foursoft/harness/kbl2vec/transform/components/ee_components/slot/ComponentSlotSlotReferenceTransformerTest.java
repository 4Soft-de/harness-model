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
package com.foursoft.harness.kbl2vec.transform.components.ee_components.slot;

import com.foursoft.harness.kbl.v25.KblComponentCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentSlot;
import com.foursoft.harness.kbl.v25.KblComponentSlotOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCavityReference;
import com.foursoft.harness.vec.v2x.VecSlot;
import com.foursoft.harness.vec.v2x.VecSlotReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentSlotSlotReferenceTransformerTest {

    @Test
    void should_transformSlotReference() {
        // Given
        final ComponentSlotSlotReferenceTransformer transformer = new ComponentSlotSlotReferenceTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblComponentSlotOccurrence source = new KblComponentSlotOccurrence();

        final KblComponentSlot part = new KblComponentSlot();
        source.setPart(part);

        final KblComponentCavityOccurrence kblComponentCavityOccurrence = new KblComponentCavityOccurrence();
        source.getComponentCavities().add(kblComponentCavityOccurrence);

        final VecCavityReference vecCavityReference = new VecCavityReference();
        orchestrator.addMockMapping(kblComponentCavityOccurrence, vecCavityReference);

        final VecSlot vecSlot = new VecSlot();
        orchestrator.addMockMapping(part, vecSlot);

        // When
        final VecSlotReference result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(vecSlot, VecSlotReference::getReferencedSlot)
                .satisfies(v -> assertThat(v.getCavityReferences()).containsExactly(vecCavityReference));
    }
}
