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
package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponent;
import com.foursoft.harness.kbl.v25.KblComponentOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecConnectorHousingRole;
import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentHousingRoleTransformerTest {

    @Test
    void should_transformConnectorHousingRole() {
        // Given
        final KblComponentOccurrence source = new KblComponentOccurrence();
        source.setId("F1");
        final KblComponent part = new KblComponent();
        source.setPart(part);

        final VecConnectorHousingSpecification housingSpecification = new VecConnectorHousingSpecification();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(part, housingSpecification);

        // When
        final VecConnectorHousingRole result = orchestrator.transform(new ComponentHousingRoleTransformer(), source);

        // Then
        assertThat(result).isNotNull()
                .returns("F1", VecConnectorHousingRole::getIdentification)
                .returns(housingSpecification, VecConnectorHousingRole::getConnectorHousingSpecification);
    }
}
