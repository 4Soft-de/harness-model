/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2025 4Soft GmbH
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
package com.foursoft.harness.vec.files.guidelines;

import com.foursoft.harness.vec.files.TestUtils;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.v2x.VecContent;
import com.foursoft.harness.vec.v2x.VecPrimaryPartType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class WireTest {

    @Test
    void create_single_core() {
        final VecSession session = new VecSession();

        session.component("WIRE", "DRAW-WIRE", VecPrimaryPartType.WIRE, comp -> comp
                .addGeneralTechnicalPart(builder -> builder
                        .withMassInformation(4.6, session.gramPerMeter()))
                .addSingleCore(wire -> wire
                        .withDin76722WireType("FLRY-A")
                        .withCSA(0.35)
                        .withOutsideDiameter(1.3, -0.1, 0.0)
                        .withInsulationThickness(0.20)
                        .withNumberOfStrands(7)
                        .withStrandDiameter(0.27)
                )
        );

        assertThatCode(() -> {
            session.writeToStream(TestUtils.createTestFileStream("ecad-wiki-guideline-single-core"));
        }).doesNotThrowAnyException();

        final VecContent content = session.toVecContent();

        assertThat(content).isNotNull()
                .as("check VecContent").satisfies(vecContent -> {
                    assertThat(vecContent.getDocumentVersions()).as("check documents")
                            .hasSize(1);
                    assertThat(vecContent.getPartVersions()).as("check parts")
                            .hasSize(1);
                })
        ;

    }

}
