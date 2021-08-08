/*-
 * ========================LICENSE_START=================================
 * navigation-extender-runtime
 * %%
 * Copyright (C) 2019 - 2021 4Soft GmbH
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
package com.foursoft.xml.io.validation;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import java.io.InputStream;
import java.util.Objects;

public class SchemaFactory {

    private SchemaFactory() {
        // Hide constructor
    }

    public static Schema getSchema(final InputStream inputStream) {
        try (final InputStream xsdFile = inputStream) {
            final javax.xml.validation.SchemaFactory factory =
                    javax.xml.validation.SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            return factory.newSchema(new StreamSource(xsdFile));
        } catch (final Exception e) {
            throw new SchemaFactoryException("Cannot initialize schema!", e);
        }
    }

    public static Schema getSchema(final String fileName) {
        final InputStream inputStream = getInputStream(fileName);
        return getSchema(inputStream);
    }

    private static InputStream getInputStream(final String fileName) {
        final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        final InputStream is = classloader.getResourceAsStream(fileName);
        return Objects.requireNonNull(is, "Cannot open file: " + fileName);
    }
}
