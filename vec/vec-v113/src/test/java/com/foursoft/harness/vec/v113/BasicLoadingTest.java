/*-
 * ========================LICENSE_START=================================
 * vec-v113
 * %%
 * Copyright (C) 2020 - 2022 4Soft GmbH
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
package com.foursoft.harness.vec.v113;

import com.foursoft.harness.vec.v113.validation.SchemaFactory;
import com.foursoft.jaxb.navext.runtime.ExtendedUnmarshaller;
import com.foursoft.jaxb.navext.runtime.JaxbModel;
import com.foursoft.jaxb.navext.runtime.io.utils.ValidationEventLogger;
import com.foursoft.jaxb.navext.runtime.model.Identifiable;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BasicLoadingTest {

    @Test
    void testLoadModel() {
        final VecContent vecContent = new VecReader().read(TestFiles.getInputStream(TestFiles.SAMPLE_VEC));
        assertThat(vecContent).isNotNull();
    }

    @Test
    void testLoadModelWithEventLogger() {
        final ValidationEventLogger validationEventLogger = new ValidationEventLogger();
        final VecContent vecContent = new VecReader(validationEventLogger)
                .read(TestFiles.getInputStream(TestFiles.SAMPLE_VEC));
        assertThat(vecContent).isNotNull();
    }

    @Test
    void testSelectorInheritance() throws IOException, JAXBException {
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
    void validationTest() throws Exception {
        try (final InputStream is = TestFiles.getInputStream(TestFiles.SAMPLE_VEC)) {
            final Schema schema = SchemaFactory.getSchema();

            final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            dbf.setNamespaceAware(true);
            dbf.setSchema(schema);

            final DocumentBuilder db = dbf.newDocumentBuilder();
            final Document d = db.parse(new BufferedInputStream(is));
            assertThat(d).isNotNull();
        }
    }

    @Test
    void testBackReferences() throws Exception {
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

}
