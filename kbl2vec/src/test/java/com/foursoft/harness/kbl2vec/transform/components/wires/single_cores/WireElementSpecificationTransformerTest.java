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
package com.foursoft.harness.kbl2vec.transform.components.wires.single_cores;

import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCoreSpecification;
import com.foursoft.harness.vec.v2x.VecInsulationSpecification;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import com.foursoft.harness.vec.v2x.VecWireElementSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WireElementSpecificationTransformerTest {

    @Test
    void should_transformWireElementSpecification() {
        // Given
        final WireElementSpecificationTransformer transformer = new WireElementSpecificationTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblGeneralWire source = new KblGeneralWire();
        source.setWireType("TestWireType");

        final KblNumericalValue outsideDiameter = new KblNumericalValue();
        source.setOutsideDiameter(outsideDiameter);

        final KblNumericalValue bendRadius = new KblNumericalValue();
        source.setBendRadius(bendRadius);

        final VecNumericalValue vecBendRadius = new VecNumericalValue();
        final VecNumericalValue vecOutsideDiameter = new VecNumericalValue();
        orchestrator.addMockMapping(outsideDiameter, vecOutsideDiameter);
        orchestrator.addMockMapping(bendRadius, vecBendRadius);

        final VecInsulationSpecification insulationSpecification = new VecInsulationSpecification();
        orchestrator.addMockMapping(source, insulationSpecification);

        final VecCoreSpecification coreSpecification = new VecCoreSpecification();
        orchestrator.addMockMapping(source, coreSpecification);

        // When
        final VecWireElementSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(vecOutsideDiameter, VecWireElementSpecification::getOutsideDiameter)
                .returns(vecBendRadius, VecWireElementSpecification::getMinBendRadiusStatic)
                .returns(coreSpecification, VecWireElementSpecification::getConductorSpecification)
                .returns(insulationSpecification, VecWireElementSpecification::getInsulationSpecification)
                .satisfies(v -> assertThat(v.getTypes().get(0).getType()).isEqualTo("TestWireType"));
    }
}
