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
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.vec.v2x.VecApproval;
import com.foursoft.harness.vec.v2x.VecPermission;
import com.foursoft.harness.vec.v2x.VecPerson;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApprovalTransformerTest {

    private final ApprovalTransformer transformer = new ApprovalTransformer();

    @Test
    void should_transformApproval_asApprovedWithTheTypeOfApprovalAsPermission() {
        // Given
        final KblApproval source = new KblApproval();
        source.setTypeOfApproval("released for production");
        source.setName("Mustermann");
        source.setDepartment("K-SIPE-3");
        source.setDate("270115");

        // When
        final VecApproval result = new TestConversionOrchestrator().transform(transformer, source);

        // Then
        assertThat(result).isNotNull()
                .returns("Approved", VecApproval::getStatus);
        assertThat(result.getPermissions()).singleElement()
                .returns("released for production", VecPermission::getPermission)
                .returns("2015-01-27T00:00:00", v -> v.getPermissionDate().toXMLFormat())
                .extracting(VecPermission::getPermitter)
                .returns("Mustermann", VecPerson::getLastName)
                .returns("K-SIPE-3", VecPerson::getDepartment);
    }

    @Test
    void should_keepThePlaceholder_ofTheMandatoryPermission() {
        // Given
        final KblApproval source = new KblApproval();
        source.setTypeOfApproval("/NULL");

        // When
        final VecApproval result = new TestConversionOrchestrator().transform(transformer, source);

        // Then
        assertThat(result.getPermissions()).singleElement()
                .returns("/NULL", VecPermission::getPermission)
                .returns(null, VecPermission::getPermissionDate)
                .returns(null, VecPermission::getPermitter);
    }
}
