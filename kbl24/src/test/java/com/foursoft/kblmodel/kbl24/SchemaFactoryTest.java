/*-
 * ========================LICENSE_START=================================
 * kbl24
 * %%
 * Copyright (C) 2020 - 2021 4Soft GmbH
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
package com.foursoft.kblmodel.kbl24;

import  com.foursoft.jaxb.navext.runtime.io.validation.LogValidator;
import  com.foursoft.jaxb.navext.runtime.io.validation.XMLValidation;
import org.junit.jupiter.api.Test;

import javax.xml.validation.Schema;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

class SchemaFactoryTest {

    @Test
    void testSchema() {
        final KBLContainer root = new KBLContainer();
        root.setXmlId("id_1000_0");
        root.setVersionId("id");
        final KblHarness harness = new KblHarness();
        harness.setXmlId("id_1001_0");
        harness.setPartNumber("harnessId");
        harness.setCompanyName("Company");
        harness.setVersion("1");
        harness.setAbbreviation("Abbreviation");
        harness.setDescription("Description");
        harness.setModelYear("Year");
        harness.setCarClassificationLevel2("Level");
        harness.setContent(KblHarnessContent.HARNESS_COMPLETE_SET);
        root.setHarness(harness);

        final KblWriter kblWriter = new KblWriter();
        final String result = kblWriter.writeToString(root);

        final Schema schema = SchemaFactory.getSchema();
        final Collection<LogValidator.ErrorLocation> errorLocations =
                new XMLValidation(schema).validateXML(result, StandardCharsets.UTF_8);

        assertThat(errorLocations).isEmpty();
    }

    @Test
    void testInvalidSchema() {
        final KBLContainer root = new KBLContainer();
        root.setXmlId("id_1000_0");
        final KblHarness harness = new KblHarness();
        harness.setXmlId("id_1001_0");
        root.setHarness(harness);

        final KblWriter kblWriter = new KblWriter();
        final String result = kblWriter.writeToString(root);

        final Schema schema = SchemaFactory.getSchema();
        final Collection<LogValidator.ErrorLocation> errorLocations =
                new XMLValidation(schema).validateXML(result, StandardCharsets.UTF_8);

        assertThat(errorLocations).isNotEmpty();
    }
}
