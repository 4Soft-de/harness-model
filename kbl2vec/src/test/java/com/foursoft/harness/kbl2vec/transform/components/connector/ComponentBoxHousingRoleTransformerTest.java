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
package com.foursoft.harness.kbl2vec.transform.components.connector;

import com.foursoft.harness.kbl.v25.KblConnectorHousing;
import com.foursoft.harness.kbl.v25.KblConnectorOccurrence;
import com.foursoft.harness.kbl.v25.KblSlotOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecConnectorHousingRole;
import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import com.foursoft.harness.vec.v2x.VecSlotReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConnectorHousingRoleTransformerTest {

    @Test
    void should_transformConnectorHousingRole() {
        // Given
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();
        final ConnectorHousingRoleTransformer transformer = new ConnectorHousingRoleTransformer();

        final KblConnectorOccurrence source = new KblConnectorOccurrence();
        source.setId("TestConnectorOccurrenceId");

        final KblConnectorHousing part = new KblConnectorHousing();
        source.setPart(part);

        final VecConnectorHousingSpecification connectorHousingSpecification = new VecConnectorHousingSpecification();

        final KblSlotOccurrence kblSlotOccurrence = new KblSlotOccurrence();
        final VecSlotReference vecSlotReference = new VecSlotReference();

        source.getSlots().add(kblSlotOccurrence);

        orchestrator.addMockMapping(kblSlotOccurrence, vecSlotReference);
        orchestrator.addMockMapping(part, connectorHousingSpecification);

        // When
        final VecConnectorHousingRole result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestConnectorOccurrenceId", VecConnectorHousingRole::getIdentification)
                .returns(connectorHousingSpecification, VecConnectorHousingRole::getConnectorHousingSpecification)
                .satisfies(
                        c -> assertThat(c.getSlotReferences()).contains(vecSlotReference)
                );
    }
}
