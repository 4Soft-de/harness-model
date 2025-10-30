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
package com.foursoft.harness.kbl2vec.transform.components.accessory;

import com.foursoft.harness.kbl.v25.KblAccessory;
import com.foursoft.harness.kbl.v25.KblAccessoryOccurrence;
import com.foursoft.harness.kbl.v25.KblAliasIdentification;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccessoryOccurrenceTransformerTest {

    @Test
    void should_transformAccessoryOccurrence() {
        // Given
        final AccessoryOccurrenceTransformer transformer = new AccessoryOccurrenceTransformer();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator();

        final KblAccessoryOccurrence source = new KblAccessoryOccurrence();
        source.setId("TestId");

        final KblAliasIdentification aliasId = new KblAliasIdentification();
        aliasId.setAliasId("TestAliasId");
        source.getAliasIds().add(aliasId);

        final VecAliasIdentification vecAliasId = new VecAliasIdentification();
        orchestrator.addMockMapping(aliasId, vecAliasId);

        final VecSpecificRole vecRole = new VecSpecificRole();
        orchestrator.addMockMapping(source, vecRole);

        final KblAccessory part = new KblAccessory();
        source.setPart(part);

        final VecPartVersion vecPartVersion = new VecPartVersion();
        orchestrator.addMockMapping(part, vecPartVersion);

        final VecPlaceableElementRole vecPlaceableElementRole = new VecPlaceableElementRole();
        orchestrator.addMockMapping(source, vecPlaceableElementRole);

        // When
        final VecPartOccurrence result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("TestId", VecPartOccurrence::getIdentification)
                .satisfies(v -> assertThat(v.getAliasIds()).containsExactly(vecAliasId))
                .satisfies(v -> assertThat(v.getRoles()).containsExactly(vecRole, vecPlaceableElementRole))
                .satisfies(v -> assertThat(v.getPart()).isEqualTo(vecPartVersion));
    }
}
