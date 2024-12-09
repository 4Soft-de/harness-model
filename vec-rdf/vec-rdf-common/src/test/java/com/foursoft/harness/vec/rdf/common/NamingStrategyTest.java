/*-
 * ========================LICENSE_START=================================
 * VEC RDF Common
 * %%
 * Copyright (C) 2024 4Soft GmbH
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
package com.foursoft.harness.vec.rdf.common;

import com.foursoft.harness.vec.v113.VecConnectorHousingSpecification;
import com.foursoft.harness.vec.v113.VecContent;
import com.foursoft.harness.vec.v113.VecDocumentVersion;
import com.foursoft.harness.vec.v113.VecPrimaryPartType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class NamingStrategyTest {

    private final NamingStrategy strategy = new NamingStrategy();

    @Test
    void should_return_model_name_for_classes() {
        assertThat(NamingStrategy.modelName(VecContent.class)).isEqualTo("VecContent");
        assertThat(NamingStrategy.modelName(VecConnectorHousingSpecification.class)).isEqualTo(
                "ConnectorHousingSpecification");
    }

    @Test
    void should_return_model_name_for_fields() throws NoSuchFieldException {
        Field f = VecDocumentVersion.class.getDeclaredField("specifications");

        assertThat(NamingStrategy.modelName(f)).isEqualTo("specification");
    }

    @Test
    void should_return_rdf_name_for_classes() {
        assertThat(strategy.rdfName(VecContent.class)).isEqualTo("VecContent");
        assertThat(strategy.rdfName(VecConnectorHousingSpecification.class)).isEqualTo("ConnectorHousingSpecification");
    }

    @Test
    void should_return_rdf_name_for_fields() throws NoSuchFieldException {
        Field f = VecDocumentVersion.class.getDeclaredField("specifications");

        assertThat(strategy.rdfName(f)).isEqualTo("documentVersionSpecification");
    }

    @Test
    void should_return_rdf_name_for_enum_literals() throws NoSuchFieldException {
        assertThat(strategy.rdfName(VecPrimaryPartType.CABLE_DUCT)).isEqualTo("PrimaryPartType_CableDuct");
    }

    @Test
    void should_return_rdf_name_for_identifiables() {
        VecDocumentVersion dv = new VecDocumentVersion();
        dv.setXmlId("id-1234");
        assertThat(strategy.rdfName(dv)).isEqualTo("DocumentVersion-id-1234");
    }

    @Test
    void should_return_uri_for_classes() {
        assertThat(strategy.uriFor(VecContent.class)).isEqualTo(
                "http://www.prostep.org/ontologies/ecad/2024/03/vec#VecContent");
        assertThat(strategy.uriFor(VecConnectorHousingSpecification.class)).isEqualTo(
                "http://www.prostep.org/ontologies/ecad/2024/03/vec#ConnectorHousingSpecification");
    }

    @Test
    void should_return_uri_for_fields() throws NoSuchFieldException {
        Field f = VecDocumentVersion.class.getDeclaredField("specifications");

        assertThat(strategy.uriFor(f)).isEqualTo(
                "http://www.prostep.org/ontologies/ecad/2024/03/vec#documentVersionSpecification");
    }

    @Test
    void should_return_uri_for_enum_literals() throws NoSuchFieldException {
        assertThat(strategy.uriFor(VecPrimaryPartType.CABLE_DUCT)).isEqualTo(
                "http://www.prostep.org/ontologies/ecad/2024/03/vec#PrimaryPartType_CableDuct");
    }

    @Test
    void should_return_uri_for_identifiables() {
        VecDocumentVersion dv = new VecDocumentVersion();
        dv.setXmlId("id-1234");
        assertThat(strategy.uriFor("http://www.example.com/test#", dv)).isEqualTo(
                "http://www.example.com/test#DocumentVersion-id-1234");
    }

}
