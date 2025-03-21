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
package com.foursoft.harness.vec.aas;

import org.eclipse.digitaltwin.aas4j.v3.dataformat.core.SerializationException;
import org.eclipse.digitaltwin.aas4j.v3.dataformat.json.JsonSerializer;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class Vec2AasConverterTest {
    private final JsonSerializer serializer = new JsonSerializer();

    @Test
    void should_convert_a_xml_file() throws SerializationException, FileNotFoundException {
        final Vec2AasConverter converter = new Vec2AasConverter();

        final InputStream inputFile = this.getClass().getResourceAsStream("/kostal-32124733993.vec");

        final Submodel model = converter.convert(inputFile, "https://www.kostal.com/kostal-32124733993#");

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        serializer.write(new FileOutputStream("kostal-example.json"), StandardCharsets.UTF_8, model);
//        serializer.write(baos, StandardCharsets.UTF_8, model);
//
//        System.out.println(baos.toString(StandardCharsets.UTF_8));
    }

    @Test
    void should_convert_coroflex_xml_file() throws SerializationException, FileNotFoundException {
        final Vec2AasConverter converter = new Vec2AasConverter();

        final InputStream inputFile = this.getClass().getResourceAsStream("/coroflex-ti-9-2611-35.vec");

        final Submodel model = converter.convert(inputFile, "https://www.coroflex.com/kostal-9-2611-353#");

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        serializer.write(new FileOutputStream("coroflex-example.json"), StandardCharsets.UTF_8, model);
//        serializer.write(baos, StandardCharsets.UTF_8, model);
//
//        System.out.println(baos.toString(StandardCharsets.UTF_8));
    }

}
