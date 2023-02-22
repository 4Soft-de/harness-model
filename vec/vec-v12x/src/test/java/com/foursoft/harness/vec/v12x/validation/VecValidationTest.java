/*-
 * ========================LICENSE_START=================================
 * vec-v12x
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
package com.foursoft.harness.vec.v12x.validation;

import com.foursoft.harness.vec.common.exception.VecException;
import com.foursoft.harness.vec.v12x.VecContent;
import com.foursoft.harness.vec.v12x.VecPartVersion;
import com.foursoft.harness.vec.v12x.VecWriter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VecValidationTest {

    @Test
    void testStrictSchema() {
        final VecContent root = new VecContent();
        root.setXmlId("id_1000_0");
        root.setVecVersion("1.2.0");


        final VecWriter vecWriter = new VecWriter();
        final String result = vecWriter.writeToString(root);

        final Collection<String> errors = new ArrayList<>();
        VecValidation.validateXML(result, errors::add, true);

        assertThat(errors).isEmpty();

    }

    @Test
    void testInvalidSchema() {
        final VecContent root = new VecContent();
        root.setXmlId("id_1000_0");
        root.setVecVersion("1.2.0");

        final VecPartVersion partVersion = new VecPartVersion();
        partVersion.setXmlId("id_1001_0");
        partVersion.setPartNumber("123_456_789");
        root.getPartVersions().add(partVersion);

        final VecWriter vecWriter = new VecWriter();
        final String result = vecWriter.writeToString(root);

        final Collection<String> errors = new ArrayList<>();
        assertThatThrownBy(() -> VecValidation.validateXML(result, errors::add, true))
                .isInstanceOf(VecException.class)
                .hasMessageContaining("Schema validation failed! Use detailedLog for more information");
        assertThat(errors).isNotEmpty();
    }
}
