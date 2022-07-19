/*-
 * ========================LICENSE_START=================================
 * kbl-v25
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
package com.foursoft.harness.kbl.v25.validation;

import com.foursoft.harness.kbl.common.exception.KblException;
import com.foursoft.harness.kbl.v25.KBLContainer;
import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblHarnessContent;
import com.foursoft.harness.kbl.v25.KblWriter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class KblValidationTest {
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

        final Collection<String> errors = new ArrayList<>();
        KblValidation.validateXML(result, errors::add, true);

        assertThat(errors).isEmpty();
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

        final Collection<String> errors = new ArrayList<>();
        assertThatThrownBy(() -> KblValidation.validateXML(result, errors::add, true))
                .isInstanceOf(KblException.class)
                .hasMessageContaining("Schema validation failed! Use detailedLog for more information");
        assertThat(errors).isNotEmpty();
    }
}
