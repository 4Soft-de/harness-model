/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 - 2026 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.transform.coupling;

import com.foursoft.harness.kbl.v25.KblComponentBoxOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentSlotOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecConnectorHousingRole;
import com.foursoft.harness.vec.v2x.VecCouplingPoint;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CouplingPointTransformerTest {

    @Test
    void should_coupleTheComponentWithTheSlotItIsMountedOn() {
        // Given
        final KblComponentSlotOccurrence slot = new KblComponentSlotOccurrence();
        final KblComponentOccurrence source = new KblComponentOccurrence();
        source.setId("F1");
        source.getMounting().add(new KblComponentBoxOccurrence());
        source.getMounting().add(slot);
        source.getMounting().add(new KblComponentCavityOccurrence());

        final VecConnectorHousingRole slotRole = new VecConnectorHousingRole();
        final VecConnectorHousingRole componentRole = new VecConnectorHousingRole();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(slot, slotRole)
                .addMockMapping(source, componentRole);

        // When
        final VecCouplingPoint result = orchestrator.transform(new CouplingPointTransformer(), source);

        // Then
        assertThat(result).isNotNull()
                .returns("F1", VecCouplingPoint::getIdentification)
                .returns(slotRole, VecCouplingPoint::getFirstConnector)
                .returns(componentRole, VecCouplingPoint::getSecondConnector);
    }

    @Test
    void should_notTransform_whenNotMountedOnASlot() {
        // Given
        final KblComponentOccurrence source = new KblComponentOccurrence();
        source.getMounting().add(new KblComponentBoxOccurrence());

        // When
        final VecCouplingPoint result = new TestConversionOrchestrator().transform(new CouplingPointTransformer(),
                                                                                   source);

        // Then
        assertThat(result).isNull();
    }
}
