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
package com.foursoft.harness.kbl2vec.transform.connectivity;

import com.foursoft.harness.kbl.v25.KblCavitySealOccurrence;
import com.foursoft.harness.kbl.v25.KblContactPoint;
import com.foursoft.harness.kbl.v25.KblExtremity;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCavitySealRole;
import com.foursoft.harness.vec.v2x.VecWireEnd;
import com.foursoft.harness.vec.v2x.VecWireMounting;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WireMountingTransformerTest {

    @Test
    void should_transformWireMounting() {
        // Given
        final WireMountingTransformer transformer = new WireMountingTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblContactPoint source = new KblContactPoint();

        final KblCavitySealOccurrence cavitySeal = new KblCavitySealOccurrence();
        source.getAssociatedParts().add(cavitySeal);

        final VecCavitySealRole vecCavitySealRole = new VecCavitySealRole();
        orchestrator.addMockMapping(cavitySeal, vecCavitySealRole);

        final KblExtremity extremity = new KblExtremity();
        source.getRefExtremity().add(extremity);

        final VecWireEnd vecWireEnd = new VecWireEnd();
        orchestrator.addMockMapping(extremity, vecWireEnd);

        // When
        final VecWireMounting result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(vecCavitySealRole, VecWireMounting::getMountedCavitySeal)
                .satisfies(v -> assertThat(v.getReferencedWireEnd()).containsExactly(vecWireEnd));
    }
}
