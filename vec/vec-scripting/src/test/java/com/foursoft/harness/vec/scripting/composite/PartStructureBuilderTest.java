/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
package com.foursoft.harness.vec.scripting.composite;

import com.foursoft.harness.vec.scripting.VecScriptingException;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.v2x.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PartStructureBuilderTest {

    @Test
    void references_the_contents_of_the_part() {
        final VecSession session = new VecSession();

        session.component("CON-1", "DRAW-CON-1", VecPrimaryPartType.CONNECTOR_HOUSING, comp -> comp
                .addConnectorHousing(connector -> connector.addCavity("A", "1")));

        session.component("ASSY-1", "DRAW-ASSY-1", VecPrimaryPartType.PART_STRUCTURE, comp -> comp
                .addPartOccurrence("CONNECTOR", "CON-1")
                .addPartOccurrence("HELPER", "CON-1")
                .addPartStructure(bom -> bom
                        .withContent(VecPartStructureContentType.ASSEMBLY)
                        .withContents("CONNECTOR")));

        final VecPartVersion assembly = session.findPartVersionByPartNumber("ASSY-1");
        final VecPartStructureSpecification bom = session.findDocument("DRAW-ASSY-1")
                .getSpecificationWithType(VecPartStructureSpecification.class)
                .orElseThrow();

        assertThat(bom.getIdentification()).isEqualTo("PSS-ASSY-1");
        assertThat(bom.getDescribedPart()).containsExactly(assembly);
        assertThat(bom.getContent()).isEqualTo("Assembly");
        assertThat(bom.getInBillOfMaterial()).extracting(VecOccurrenceOrUsage::getIdentification)
                .containsExactly("CONNECTOR");
    }

    @Test
    void fails_for_an_undefined_content() {
        final VecSession session = new VecSession();

        assertThatThrownBy(() -> session.component("ASSY-1", "DRAW-ASSY-1", VecPrimaryPartType.PART_STRUCTURE,
                                                   comp -> comp.addPartStructure(bom -> bom.withContents("MISSING"))))
                .isInstanceOf(VecScriptingException.class)
                .hasMessageContaining("MISSING");
    }

}
