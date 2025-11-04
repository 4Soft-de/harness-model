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
package com.foursoft.harness.kbl2vec.transform.components.ee_components.connector;

import com.foursoft.harness.kbl.v25.KblComponentBoxConnector;
import com.foursoft.harness.kbl.v25.KblComponentBoxConnectorOccurrence;
import com.foursoft.harness.kbl.v25.KblSlotOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecConnectorHousingRole;
import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import com.foursoft.harness.vec.v2x.VecSlotReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentBoxHousingRoleTransformerTest {

    @Test
    void should_transformConnectorHousingRole() {
        // Given
        final ComponentBoxHousingRoleTransformer transformer = new ComponentBoxHousingRoleTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblComponentBoxConnectorOccurrence source = new KblComponentBoxConnectorOccurrence();

        final KblComponentBoxConnector part = new KblComponentBoxConnector();
        source.setPart(part);

        final VecConnectorHousingSpecification specification = new VecConnectorHousingSpecification();
        orchestrator.addMockMapping(part, specification);

        final KblSlotOccurrence slotOccurrence = new KblSlotOccurrence();
        source.getSlots().add(slotOccurrence);

        final VecSlotReference vecSlotReference = new VecSlotReference();
        orchestrator.addMockMapping(slotOccurrence, vecSlotReference);

        // When
        final VecConnectorHousingRole result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(specification, VecConnectorHousingRole::getConnectorHousingSpecification)
                .satisfies(v -> assertThat(v.getSlotReferences()).containsExactly(vecSlotReference));
    }
}
