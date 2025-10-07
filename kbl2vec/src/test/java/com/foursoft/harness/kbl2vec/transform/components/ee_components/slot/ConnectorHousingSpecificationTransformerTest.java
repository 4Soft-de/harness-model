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

import com.foursoft.harness.kbl.v25.KblComponentCavity;
import com.foursoft.harness.kbl.v25.KblComponentSlot;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCavity;
import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import com.foursoft.harness.vec.v2x.VecSlot;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConnectorHousingSpecificationTransformerTest {

    @Test
    void should_transformHousingSpecificationFromComponentSlot() {
        // Given
        final ConnectorHousingSpecificationTransformer transformer = new ConnectorHousingSpecificationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblComponentSlot source = new KblComponentSlot();
        source.setId("TestId");

        final KblComponentCavity kblComponentCavity = new KblComponentCavity();
        source.getComponentCavities().add(kblComponentCavity);

        final VecCavity vecCavity = new VecCavity();
        orchestrator.addMockMapping(kblComponentCavity, vecCavity);

        final VecSlot vecSlot = new VecSlot();
        orchestrator.addMockMapping(source, vecSlot);

        // When
        final VecConnectorHousingSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestId", VecConnectorHousingSpecification::getIdentification)
                .satisfies(v -> assertThat(v.getSlots()).contains(vecSlot));
    }
}
