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
package com.foursoft.harness.kbl2vec.transform.components.fixing;

import com.foursoft.harness.kbl.v25.KblFixing;
import com.foursoft.harness.kbl.v25.KblFixingOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecFixingRole;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FixingOccurrenceTransformerTest {

    @Test
    void should_transformFixingOccurrence() {
        // Given
        final FixingOccurrenceTransformer transformer = new FixingOccurrenceTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblFixingOccurrence source = new KblFixingOccurrence();
        source.setId("TestId");

        final KblFixing part = new KblFixing();
        source.setPart(part);

        final VecPartVersion vecPartVersion = new VecPartVersion();
        final VecFixingRole vecFixingRole = new VecFixingRole();
        orchestrator.addMockMapping(source, vecFixingRole);
        orchestrator.addMockMapping(part, vecPartVersion);

        final VecPlaceableElementRole vecPlaceableElementRole = new VecPlaceableElementRole();
        orchestrator.addMockMapping(source, vecPlaceableElementRole);

        // When
        final VecPartOccurrence result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestId", VecPartOccurrence::getIdentification)
                .returns(vecPartVersion, VecPartOccurrence::getPart)
                .satisfies(v -> assertThat(v.getRoles()).containsExactly(vecFixingRole, vecPlaceableElementRole));
    }
}
