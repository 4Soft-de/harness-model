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
package com.foursoft.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.Map;

/**
 * Caches created JAXBContext instances, because creating JAXBContext is an
 * expensive Operation an shall only be done once.
 *
 * @author becker
 * @see <a href="https://javaee.github.io/jaxb-v2/doc/user-guide/ch03.html#other-miscellaneous-topics-performance-and-thread-safety">JAXB V2 User Guide</a>
 */
public final class JaxbContextFactory {

    private static final Map<String, JAXBContext> jaxbContextCache = new HashMap<>();

    private JaxbContextFactory() {
        throw new UnsupportedOperationException("JAXBContextFactory shall not be instantiated (static class");
    }

    public static synchronized JAXBContext initializeContext(final String packageName, final ClassLoader classLoader) throws JAXBException {
        JAXBContext context = jaxbContextCache.get(packageName);

        // not implemented with computeIfAbsent because .newInstance throws JAXBException
        if (context == null) {
            context = JAXBContext.newInstance(packageName, classLoader);
            jaxbContextCache.put(packageName, context);
        }

        return context;
    }

}
