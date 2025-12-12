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

import com.foursoft.harness.vec.scripting.Customizer;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.components.ComponentMasterDataBuilder;
import com.foursoft.harness.vec.scripting.components.protection.CorrugatedPipeSpecificationBuilder;
import com.foursoft.harness.vec.v2x.VecPrimaryPartType;
import org.junit.jupiter.api.Test;

import static com.foursoft.harness.vec.files.TestUtils.storeVecAndValidate;
import static org.assertj.core.api.Assertions.assertThatCode;

class PartSubstitutionTest {

    @Test
    void should_generate_substitution_table() {
        final VecSession session = new VecSession();

        session.component("T-0001", "DOC-T-0001", VecPrimaryPartType.CORRUGATED_PIPE, comp -> {
            createTemplate(comp, spec -> {
                spec.withNominalSize("6/10")
                        .withInnerDiameter(6.3)
                        .withOuterDiameter(10.0);
            });
        });

        session.component("T-0002", "DOC-T-0002", VecPrimaryPartType.CORRUGATED_PIPE, comp -> {
            createTemplate(comp, spec -> {
                spec.withNominalSize("9/13")
                        .withInnerDiameter(9)
                        .withOuterDiameter(13.6);
            });
        });

        session.component("T-A100", "DOC-T-A100", VecPrimaryPartType.CORRUGATED_PIPE, comp -> {
            createTemplate(comp, spec -> {
            });
            comp.addPartSubstitution(substitution -> {
                substitution.addAlternativePartVersion("T-0001")
                        .addAlternativePartVersion("T-0002");
            });
        });

        session.harness("HARNESS", "A", harness -> {
            harness.addPartOccurrence("TUBE-OCC", "T-A100");
            harness.addPartUsage("TUBE-PU", "DOC-T-A100", pu -> {
                pu.addCorrugatedPipeSpecification("CPS-T-A100", role -> {
                }).addPartSubstitution("PSS-T-A100");
            });
        });

        assertThatCode(
                storeVecAndValidate("ecad-wiki-guideline-part-substitution", session)).doesNotThrowAnyException();

    }

    private void createTemplate(final ComponentMasterDataBuilder comp,
                                final Customizer<CorrugatedPipeSpecificationBuilder> customizer) {
        comp.addCorrugatedPipe(prot -> {
            prot.withCorrugationHeight(3)
                    .withCorrugationWidth(5);
            customizer.customize(prot);
        });

    }
}
