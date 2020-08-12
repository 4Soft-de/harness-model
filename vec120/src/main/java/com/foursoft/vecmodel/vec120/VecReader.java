/*-
 * ========================LICENSE_START=================================
 * vec120
 * %%
 * Copyright (C) 2020 4Soft GmbH
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
package com.foursoft.vecmodel.vec120;

import com.foursoft.xml.io.read.XMLReader;
import com.foursoft.xml.model.Identifiable;

/**
 * a default implementation for a thread local stored VEC 120 reader. Validation events are logged to slf4j.
 * Please implement an own implementation of XMLReader, if a custom event consumer is needed (see @{@link XMLReader}).
 */
public final class VecReader extends XMLReader<VecContent, Identifiable> {

    private static final ThreadLocal<VecReader> localReader = ThreadLocal.withInitial(
            VecReader::new);

    private VecReader() {
        super(VecContent.class, Identifiable.class, Identifiable::getXmlId);
    }

    /**
     * @return a thread local VecReader object
     */
    public static VecReader getLocalReader() {
        return localReader.get();
    }
}