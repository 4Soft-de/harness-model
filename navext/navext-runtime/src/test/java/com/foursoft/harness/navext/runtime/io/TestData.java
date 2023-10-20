/*-
 * ========================LICENSE_START=================================
 * NavExt Runtime
 * %%
 * Copyright (C) 2019 - 2023 4Soft GmbH
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
package com.foursoft.harness.navext.runtime.io;

import com.foursoft.harness.navext.runtime.io.read.XMLReader;
import com.foursoft.harness.navext.runtime.io.validation.SchemaFactory;
import com.foursoft.harness.navext.runtime.model.AbstractBase;
import com.foursoft.harness.navext.runtime.model.Root;
import jakarta.xml.bind.ValidationEvent;

import javax.xml.validation.Schema;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public final class TestData {

    private static final String BASIC_TEST_XML = "basic-test.xml";
    private static final String BASIC_TEST_XSD = "basic-test.xsd";
    private static final String ERROR_TEST_XML = "error-test.xml";

    private static final String BASIC_BASE_PATH = "basic";
    private static final String VALIDATE_BASE_PATH = "validate";

    public static String BASIC_BASIC_TEST_XML = BASIC_BASE_PATH + "/" + BASIC_TEST_XML;
    public static String BASIC_ERROR_TEST_XML = BASIC_BASE_PATH + "/" + ERROR_TEST_XML;

    public static String VALIDATE_BASIC_TEST_XML = VALIDATE_BASE_PATH + "/" + BASIC_TEST_XML;
    public static String VALIDATE_BASIC_TEST_XSD = VALIDATE_BASE_PATH + "/" + BASIC_TEST_XSD;
    public static String VALIDATE_ERROR_TEST_XML = VALIDATE_BASE_PATH + "/" + ERROR_TEST_XML;

    private TestData() {
    }

    public static Root readBasicXml() {
        final TestXMLReader reader = new TestXMLReader();
        final InputStream inputStream = getInputStream(BASIC_BASIC_TEST_XML);
        return reader.read(inputStream);
    }

    public static Schema getBasicSchema() {
        final InputStream inputStream = getInputStream(VALIDATE_BASIC_TEST_XSD);
        return SchemaFactory.getSchema(inputStream);
    }

    public static InputStream getInputStream(final String testPath) {
        // Leading slash is important.
        final String path = "/" + testPath;
        return TestData.class.getResourceAsStream(path);
    }

    public static Path getPath(final String testPath) {
        return Paths.get("src/test/resources/" + testPath);
    }

    public static class TestXMLReader extends XMLReader<Root, AbstractBase> {
        public TestXMLReader() {
            super(Root.class, AbstractBase.class, AbstractBase::getXmlId);
        }

        public TestXMLReader(final Consumer<ValidationEvent> consumer) {
            super(Root.class, AbstractBase.class, AbstractBase::getXmlId, consumer);
        }
    }

}

