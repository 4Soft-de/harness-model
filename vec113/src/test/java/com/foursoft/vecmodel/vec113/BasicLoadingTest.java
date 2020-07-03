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
package com.foursoft.vecmodel.vec113;

import com.foursoft.xml.model.Identifiable;
import com.foursoft.vecmodel.vec113.common.EventConsumer;
import com.foursoft.xml.ExtendedUnmarshaller;
import com.foursoft.xml.JaxbModel;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicLoadingTest {

    @Test
    public void testLoadModel() throws IOException, JAXBException {
        final ExtendedUnmarshaller<VecContent, Identifiable> unmarshaller =
            new ExtendedUnmarshaller<VecContent, Identifiable>(VecContent.class)
                .withBackReferences()
                .withEventLogging(new EventConsumer())
                .withIdMapper(Identifiable.class, Identifiable::getXmlId);

        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.SAMPLE_VEC)) {
            final JaxbModel<VecContent, Identifiable> model = unmarshaller
                    .unmarshall(new BufferedInputStream(inputStream));
            assertThat(model).isNotNull();
        }
    }

    @Test
    public void testSelectorInheritance() throws JAXBException, IOException {
        try (final InputStream is = TestFiles.getInputStream(TestFiles.SAMPLE_VEC)) {
            final ExtendedUnmarshaller<VecContent, Identifiable> unmarshaller =
                new ExtendedUnmarshaller<VecContent, Identifiable>(VecContent.class)
                    .withBackReferences()
                    .withIdMapper(Identifiable.class, Identifiable::getXmlId);

            final JaxbModel<VecContent, Identifiable> model = unmarshaller
                    .unmarshall(new BufferedInputStream(is));

            assertThat(model).isNotNull();

            final VecApproval approval = model.getIdLookup()
                    .findById(VecApproval.class, "id_2014_0")
                    .orElse(null);

            assertThat(approval).isNotNull();

            final VecPermission vecPermission = model.getIdLookup()
                    .findById(VecPermission.class, "id_2185_0")
                    .orElse(null);

            assertThat(vecPermission).isNotNull();

            final List<VecPermission> permissions = approval.getPermissions();

            assertThat(permissions)
                    .isNotEmpty()
                    .hasSize(1)
                    .containsExactly(vecPermission);
        }
    }

    @Test
    public void validationTest() throws Exception {
        try (final InputStream is = TestFiles.getInputStream(TestFiles.SAMPLE_VEC)) {
            final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            final Source source = new StreamSource(getClass().getResourceAsStream("/vec113/vec_1.1.3.xsd"));

            final Schema schema = schemaFactory.newSchema(source);

            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            dbf.setNamespaceAware(true);
            dbf.setSchema(schema);

            final DocumentBuilder db = dbf.newDocumentBuilder();
            final Document d = db.parse(new BufferedInputStream(is));
            assertThat(d).isNotNull();
        }
    }

    @Test
    public void testBackReferences() throws IOException, JAXBException {
        try (final InputStream is = TestFiles.getInputStream(TestFiles.SAMPLE_VEC)) {
            final ExtendedUnmarshaller<VecContent, Identifiable> unmarshaller =
                    new ExtendedUnmarshaller<VecContent, Identifiable>(VecContent.class)
                            .withBackReferences()
                            .withIdMapper(Identifiable.class, Identifiable::getXmlId);

            final JaxbModel<VecContent, Identifiable> model = unmarshaller
                    .unmarshall(new BufferedInputStream(is));

            final VecContent content = model.getRootElement();

            // VecUnit -> VecValueWithUnit
            final List<VecUnit> units = content.getUnits();
            assertThat(units).isNotEmpty();
            final VecUnit vecUnit = units.get(0);
            final Set<VecValueWithUnit> refValueWithUnit = vecUnit.getRefValueWithUnit();
            final VecValueWithUnit vecValueWithUnit = refValueWithUnit.stream().findFirst().orElse(null);
            assertThat(vecValueWithUnit).isNotNull();
            final String xmlId = vecValueWithUnit.getXmlId();

            // VecValueWithUnit -> VecUnit
            final VecValueWithUnit unitWithValue = model.getIdLookup()
                    .findById(VecValueWithUnit.class, xmlId)
                    .orElse(null);
            assertThat(unitWithValue).isEqualTo(vecValueWithUnit);
            final VecUnit unitComponent = unitWithValue.getUnitComponent();
            assertThat(unitComponent).isEqualTo(vecUnit);
        }
    }

    @Test
    public void testWithLogging() throws IOException {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.SAMPLE_VEC)) {
            final VecContent content = VecReader.read(inputStream);
            assertThat(content).isNotNull();
        }
    }

}
