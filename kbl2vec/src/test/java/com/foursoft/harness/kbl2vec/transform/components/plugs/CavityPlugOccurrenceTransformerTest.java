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
package com.foursoft.harness.kbl2vec.transform.components.plugs;

import com.foursoft.harness.kbl.v25.KblCavityPlug;
import com.foursoft.harness.kbl.v25.KblCavityPlugOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecCavityPlugRole;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CavityPlugOccurrenceTransformerTest {

    @Test
    void should_transformCavityPlugOccurrence() {
        // Given
        final CavityPlugOccurrenceTransformer transformer = new CavityPlugOccurrenceTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblCavityPlugOccurrence source = new KblCavityPlugOccurrence();
        source.setId("TestId");

        final KblCavityPlug part = new KblCavityPlug();
        source.setPart(part);

        final VecPartVersion vecPartVersion = new VecPartVersion();
        final VecCavityPlugRole vecCavityPlugRole = new VecCavityPlugRole();

        orchestrator.addMockMapping(part, vecPartVersion);
        orchestrator.addMockMapping(source, vecCavityPlugRole);

        // When
        final VecPartOccurrence result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result)
                .isNotNull()
                .returns("TestId", VecPartOccurrence::getIdentification)
                .returns(vecPartVersion, VecPartOccurrence::getPart)
                .satisfies(v -> assertThat(v.getRoles()).containsExactly(vecCavityPlugRole));
    }
}
