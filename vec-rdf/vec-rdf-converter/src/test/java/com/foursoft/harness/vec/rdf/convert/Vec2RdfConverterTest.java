/*-
 * ========================LICENSE_START=================================
 * VEC RDF Converter
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
package com.foursoft.harness.vec.rdf.convert;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class Vec2RdfConverterTest {

    @Test
    void should_convert_a_xml_file() {
        Vec2RdfConverter converter = new Vec2RdfConverter();

        InputStream inputFile = this.getClass().getResourceAsStream("/fixtures/oldbeetle/oldbeetle_vec113.vec");

        Model model = converter.convert(inputFile, "https://www.acme.inc/oldbeetle");

        Model expectedModel = ModelFactory.createDefaultModel();
        expectedModel.read("fixtures/oldbeetle/oldbeetle.ttl", "TURTLE");

        assertThat(model).satisfies(new Condition<>(m -> m.isIsomorphicWith(expectedModel),
                                                    "model is isomorphic with expected model"));

    }

}
