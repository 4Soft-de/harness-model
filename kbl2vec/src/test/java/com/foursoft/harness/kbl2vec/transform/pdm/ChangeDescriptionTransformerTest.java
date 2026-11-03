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

import com.foursoft.harness.kbl.v25.KblChange;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecAbstractLocalizedString;
import com.foursoft.harness.vec.v2x.VecChangeDescription;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecPerson;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChangeDescriptionTransformerTest {

    private final ChangeDescriptionTransformer transformer = new ChangeDescriptionTransformer();

    @Test
    void should_transformChangeDescription() {
        // Given
        final KblChange source = new KblChange();
        source.setId("C-1");
        source.setDescription("Initial release");
        source.setChangeRequest("CR-4711");
        source.setChangeDate("270115");
        source.setResponsibleDesigner("Kyriazis");
        source.setDesignerDepartment("K-SIPE-3");
        source.setApproverName("Mustermann");
        source.setApproverDepartment("/NULL");

        // When
        final VecChangeDescription result = new TestConversionOrchestrator().transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("C-1", VecChangeDescription::getIdentification)
                .returns("2015-01-27T00:00:00", v -> v.getChangeDate().toXMLFormat());
        assertThat(result.getDescriptions()).map(VecAbstractLocalizedString.class::cast)
                .extracting(d -> ((VecLocalizedString) d).getValue())
                .containsExactly("Initial release");
        assertThat(result.getRelatedChangeRequests()).containsExactly("CR-4711");
        assertThat(result.getResponsibleDesigner())
                .returns("Kyriazis", VecPerson::getLastName)
                .returns("K-SIPE-3", VecPerson::getDepartment);
        assertThat(result.getApprover())
                .returns("Mustermann", VecPerson::getLastName)
                .returns(null, VecPerson::getDepartment);
    }

    @Test
    void should_omitUndefinedValues() {
        // Given
        final KblChange source = new KblChange();
        source.setChangeDate("27.01.2015");
        source.setResponsibleDesigner("/NULL");
        source.setChangeRequest("/NULL");

        // When
        final VecChangeDescription result = new TestConversionOrchestrator().transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns(null, VecChangeDescription::getChangeDate)
                .returns(null, VecChangeDescription::getResponsibleDesigner)
                .returns(null, VecChangeDescription::getApprover);
        assertThat(result.getRelatedChangeRequests()).isEmpty();
    }
}
