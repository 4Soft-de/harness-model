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
package com.foursoft.harness.kbl2vec.transform.pdm;

import com.foursoft.harness.kbl.v25.KblApproval;
import com.foursoft.harness.kbl.v25.KblChange;
import com.foursoft.harness.kbl.v25.KblCreation;
import com.foursoft.harness.kbl.v25.KblModule;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecApproval;
import com.foursoft.harness.vec.v2x.VecChangeDescription;
import com.foursoft.harness.vec.v2x.VecCopyrightInformation;
import com.foursoft.harness.vec.v2x.VecCreation;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PdmFragmentsTest {

    private final Transformer<KblModule, VecPartVersion> transformer =
            (context, source) -> TransformationResult.from(new VecPartVersion())
                    .withFragment(PdmFragments.copyright(source))
                    .withFragment(PdmFragments.history(source, context))
                    .build();

    @Test
    void should_addThePdmInformation() {
        // Given
        final KblModule source = new KblModule();
        source.setCopyrightNote("All rights reserved.");
        final KblChange change = new KblChange();
        source.getChanges().add(change);
        final KblApproval approval = new KblApproval();
        source.getRefApproval().add(approval);
        final KblCreation creation = new KblCreation();
        source.getRefCreation().add(creation);

        final VecCopyrightInformation copyright = new VecCopyrightInformation();
        final VecChangeDescription changeDescription = new VecChangeDescription();
        final VecApproval vecApproval = new VecApproval();
        final VecCreation vecCreation = new VecCreation();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(source, copyright)
                .addMockMapping(change, changeDescription)
                .addMockMapping(approval, vecApproval)
                .addMockMapping(creation, vecCreation);

        // When
        final VecPartVersion result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(copyright, VecPartVersion::getCopyrightInformation)
                .returns(vecCreation, VecPartVersion::getCreation);
        assertThat(result.getChangeDescriptions()).containsExactly(changeDescription);
        assertThat(result.getApprovals()).containsExactly(vecApproval);
    }

    @Test
    void should_transformOnlyTheFirstCreation() {
        // Given
        final KblModule source = new KblModule();
        final KblCreation first = new KblCreation();
        first.setXmlId("C1");
        final KblCreation second = new KblCreation();
        second.setXmlId("C2");
        source.getRefCreation().add(second);
        source.getRefCreation().add(first);

        final VecCreation vecCreation = new VecCreation();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(first, vecCreation);

        // When
        final VecPartVersion result = orchestrator.transform(transformer, source);

        // Then
        assertThat(result.getCreation()).isSameAs(vecCreation);
        assertThat(result.getCopyrightInformation()).isNull();
    }
}
