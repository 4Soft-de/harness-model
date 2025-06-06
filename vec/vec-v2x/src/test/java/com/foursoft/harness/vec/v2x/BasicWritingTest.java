/*-
 * ========================LICENSE_START=================================
 * VEC 2.X
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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
package com.foursoft.harness.vec.v2x;

import com.foursoft.harness.vec.common.util.DateUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class BasicWritingTest {

    @Test
    void testWriteModel() {
        final LocalDate exampleDate = LocalDate.of(2022, 3, 24);
        final LocalDateTime exampleDateTime = LocalDateTime.of(exampleDate, LocalTime.NOON);

        final VecContent root = new VecContent();
        root.setXmlId("id_1000_0");
        root.setVecVersion("1.2.0");
        root.setDateOfCreation(DateUtils.toXMLGregorianCalendar(exampleDate));

        final VecPermission permission = new VecPermission();
        permission.setXmlId("id_2185_0");
        permission.setPermission("Released");
        permission.setPermissionDate(DateUtils.toXMLGregorianCalendar(exampleDateTime));

        final VecApproval approval = new VecApproval();
        approval.setXmlId("id_2014_0");
        approval.setStatus("Approved");
        approval.getPermissions().add(permission);

        final VecDocumentVersion documentVersion = new VecDocumentVersion();
        documentVersion.setXmlId("id_1002_0");
        documentVersion.getApprovals().add(approval);
        documentVersion.setDocumentNumber("123_456_789");

        final VecSpecification specification = new VecConnectorHousingCapSpecification();
        specification.setXmlId("id_2000_0");
        specification.setIdentification("Ccs-123_456_789-1");

        documentVersion.getSpecifications().add(specification);

        final VecPartVersion partVersion = new VecPartVersion();
        partVersion.setXmlId("id_1001_0");
        partVersion.setPartNumber("123_456_789");

        root.getDocumentVersions().add(documentVersion);
        root.getPartVersions().add(partVersion);

        final VecWriter vecWriter = new VecWriter();
        final String result = vecWriter.writeToString(root);

        assertThat(result)
                .isEqualToIgnoringWhitespace(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                                "<vec:VecContent id=\"id_1000_0\" " +
                                "xmlns:vec=\"http://www.prostep.org/ecad-if/2011/vec\">\n" +
                                "    <VecVersion>1.2.0</VecVersion>\n" +
                                "    <DateOfCreation>2022-03-24T00:00:00</DateOfCreation>\n" +
                                "    <DocumentVersion id=\"id_1002_0\">\n" +
                                "        <Approval id=\"id_2014_0\">\n" +
                                "            <Status>Approved</Status>\n" +
                                "            <Permission id=\"id_2185_0\">\n" +
                                "                <Permission>Released</Permission>\n" +
                                "                <PermissionDate>2022-03-24T12:00:00</PermissionDate>\n" +
                                "            </Permission>\n" +
                                "        </Approval>\n" +
                                "        <DocumentNumber>123_456_789</DocumentNumber>\n" +
                                "        <Specification " +
                                "xsi:type=\"vec:ConnectorHousingCapSpecification\" " +
                                "id=\"id_2000_0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                                "            <Identification>Ccs-123_456_789-1</Identification>\n" +
                                "        </Specification>\n" +
                                "    </DocumentVersion>\n" +
                                "    <PartVersion id=\"id_1001_0\">\n" +
                                "        <PartNumber>123_456_789</PartNumber>\n" +
                                "    </PartVersion>\n" +
                                "</vec:VecContent>");
    }

}
