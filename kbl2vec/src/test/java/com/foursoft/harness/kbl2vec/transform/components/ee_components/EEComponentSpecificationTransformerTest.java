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
package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponentBox;
import com.foursoft.harness.kbl.v25.KblComponentBoxConnection;
import com.foursoft.harness.kbl.v25.KblComponentBoxConnector;
import com.foursoft.harness.kbl.v25.KblComponentSlot;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EEComponentSpecificationTransformerTest {

    @Test
    void should_transformEEComponentSpecification() {
        // Given
        final EEComponentSpecificationTransformer transformer = new EEComponentSpecificationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblComponentBox source = new KblComponentBox();

        final VecPartVersion vecPartVersion = new VecPartVersion();
        final VecPartOrUsageRelatedSpecification vecPartOrUsageRelatedSpecification =
                new VecPartOrUsageRelatedSpecification();
        orchestrator.addMockMapping(source, vecPartVersion);
        orchestrator.addMockMapping(source, vecPartOrUsageRelatedSpecification);

        final KblComponentSlot slot = new KblComponentSlot();
        source.getComponentSlots().add(slot);

        final VecHousingComponent vecHousingComponentFromComponentSlot = new VecHousingComponent();
        orchestrator.addMockMapping(slot, vecHousingComponentFromComponentSlot);

        final KblComponentBoxConnection kblComponentBoxConnection = new KblComponentBoxConnection();
        source.getConnections().add(kblComponentBoxConnection);

        final VecInternalComponentConnection vecInternalComponentConnection = new VecInternalComponentConnection();
        orchestrator.addMockMapping(kblComponentBoxConnection, vecInternalComponentConnection);

        final KblComponentBoxConnector kblComponentBoxConnector = new KblComponentBoxConnector();
        source.getComponentBoxConnectors().add(kblComponentBoxConnector);

        final VecHousingComponent vecHousingComponentFromComponentBoxConnector = new VecHousingComponent();
        orchestrator.addMockMapping(kblComponentBoxConnector, vecHousingComponentFromComponentBoxConnector);

        // When
        final VecEEComponentSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .satisfies(v -> assertThat(v.getHousingComponents()).containsExactlyInAnyOrder(
                        vecHousingComponentFromComponentSlot,
                        vecHousingComponentFromComponentBoxConnector))
                .satisfies(v -> assertThat(v.getConnections()).containsExactly(vecInternalComponentConnection));
    }
}
