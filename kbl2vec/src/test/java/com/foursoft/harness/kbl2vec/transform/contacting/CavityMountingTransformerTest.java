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
package com.foursoft.harness.kbl2vec.transform.contacting;

import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCavityMounting;
import com.foursoft.harness.vec.v2x.VecCavityPlugRole;
import com.foursoft.harness.vec.v2x.VecCavityReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CavityMountingTransformerTest {

    @Test
    void should_transformCavityMounting() {
        // Given
        final CavityMountingTransformer transformer = new CavityMountingTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblContactPoint source = new KblContactPoint();

        final KblCavityPlugOccurrence cavityPlugOccurrence = new KblCavityPlugOccurrence();
        final KblPartSubstitution partSubstitution = new KblPartSubstitution();
        final KblTerminalOccurrence hasReplacing = new KblTerminalOccurrence();
        partSubstitution.setReplaced(cavityPlugOccurrence);
        hasReplacing.getReplacings().add(partSubstitution);
        source.getAssociatedParts().add(hasReplacing);

        final VecCavityPlugRole vecCavityPlugRole = new VecCavityPlugRole();
        orchestrator.addMockMapping(cavityPlugOccurrence, vecCavityPlugRole);

        final KblCavityOccurrence cavityOccurrence = new KblCavityOccurrence();
        source.getContactedCavity().add(cavityOccurrence);

        final VecCavityReference vecCavityReference = new VecCavityReference();
        orchestrator.addMockMapping(cavityOccurrence, vecCavityReference);

        // When
        final VecCavityMounting result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .satisfies(v -> assertThat(v.getReplacedPlug()).containsExactly(vecCavityPlugRole))
                .satisfies(v -> assertThat(v.getEquippedCavityRef()).containsExactly(vecCavityReference));
    }
}
