/*-
 * ========================LICENSE_START=================================
 * vec113
 * %%
 * Copyright (C) 2020 4Soft GmbH
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
package com.foursoft.vecmodel.vec120;

import org.testng.annotations.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.TransformerFactoryConfigurationError;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicWritingTest {

    @Test
    public void testWriteModel() throws JAXBException, TransformerFactoryConfigurationError {
        final JAXBContext jc = JAXBContext.newInstance(VecContent.class);

        final VecContent root = new VecContent();
        root.setXmlId("id_1000_0");
        root.setVecVersion("1.2.0");

        final VecPermission permission = new VecPermission();
        permission.setPermission("Released");

        final VecApproval approval = new VecApproval();
        approval.setStatus("Approved");
        approval.getPermissions().add(permission);

        final VecDocumentVersion documentVersion = new VecDocumentVersion();
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

        final Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        final StringWriter stringWriter = new StringWriter();

        marshaller.marshal(root, stringWriter);

        final String result = stringWriter.toString();
        assertThat(result)
                .isEqualToIgnoringWhitespace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                        "<ns2:VecContent id=\"id_1000_0\" xmlns:ns2=\"http://www.prostep.org/ecad-if/2011/vec\">\n" +
                        "    <VecVersion>1.2.0</VecVersion>\n" +
                        "    <DocumentVersion>\n" +
                        "        <Approval>\n" +
                        "            <Status>Approved</Status>\n" +
                        "            <Permission>\n" +
                        "                <Permission>Released</Permission>\n" +
                        "            </Permission>\n" +
                        "        </Approval>\n" +
                        "        <DocumentNumber>123_456_789</DocumentNumber>\n" +
                        "        <Specification xsi:type=\"ns2:ConnectorHousingCapSpecification\" id=\"id_2000_0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                        "            <Identification>Ccs-123_456_789-1</Identification>\n" +
                        "        </Specification>\n" +
                        "    </DocumentVersion>\n" +
                        "    <PartVersion id=\"id_1001_0\">\n" +
                        "        <PartNumber>123_456_789</PartNumber>\n" +
                        "    </PartVersion>\n" +
                        "</ns2:VecContent>");
    }

}
