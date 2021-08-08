/*-
 * ========================LICENSE_START=================================
 * navigation-extender-runtime
 * %%
 * Copyright (C) 2019 - 2020 4Soft GmbH
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
package com.foursoft.xml.io;

import com.foursoft.test.model.AbstractBase;
import com.foursoft.test.model.Root;
import com.foursoft.xml.io.read.XMLReader;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class TestData {
    public static final String BASIC_TEST_XML = "basic-test.xml";
    public static final String ERROR_TEST_XML = "error-test.xml";
    public static final Path BASIC_BASE_PATH = Paths.get("src", "test", "resources", "basic");
    public static final Path VALIDATE_BASE_PATH_SRC = Paths.get("src", "test", "resources", "validate");
    public static final Path VALIDATE_BASE_PATH = Paths.get("validate");


    private TestData() {
    }

    public static Root readBasicTest() {
        final XMLReader<Root, AbstractBase> reader = new XMLReader<>(Root.class,
                AbstractBase.class,
                AbstractBase::getXmlId);
        final InputStream inputStream = TestData.class.getResourceAsStream("/basic/" + BASIC_TEST_XML);
        return reader.read(inputStream);
    }
}

