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

import com.foursoft.harness.kbl.v25.KblFuse;
import com.foursoft.harness.kbl.v25.KblFuseType;
import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecFuseSpecification;
import com.foursoft.harness.vec.v2x.VecHousingComponent;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FuseSpecificationTransformerTest {

    private final FuseSpecificationTransformer transformer = new FuseSpecificationTransformer();

    @Test
    void should_transformFuseSpecification() {
        // Given
        final KblFuse source = new KblFuse();
        source.setPartNumber("F-001");
        final KblFuseType type = new KblFuseType();
        type.setKey("Type_SF30");
        type.setReferenceSystem("VOBES");
        source.setType(type);
        final KblNumericalValue nominalCurrent = new KblNumericalValue();
        source.setNominalCurrent(nominalCurrent);

        final VecPartVersion partVersion = new VecPartVersion();
        final VecNumericalValue iMax = new VecNumericalValue();
        final VecHousingComponent housingComponent = new VecHousingComponent();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(source, partVersion)
                .addMockMapping(source, housingComponent)
                .addMockMapping(nominalCurrent, iMax);

        // When
        final VecFuseSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("FS-F-001", VecFuseSpecification::getIdentification)
                .returns("Type_SF30", VecFuseSpecification::getFuseType)
                .returns(iMax, VecFuseSpecification::getIMax)
                .satisfies(v -> assertThat(v.getDescribedPart()).containsExactly(partVersion))
                .satisfies(v -> assertThat(v.getHousingComponents()).containsExactly(housingComponent));
    }

    @Test
    void should_transformFuseSpecification_withoutTypeAndNominalCurrent() {
        // Given
        final KblFuse source = new KblFuse();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(source, new VecPartVersion())
                .addMockMapping(source, new VecHousingComponent());

        // When
        final VecFuseSpecification result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(null, VecFuseSpecification::getFuseType)
                .returns(null, VecFuseSpecification::getIMax);
    }
}
