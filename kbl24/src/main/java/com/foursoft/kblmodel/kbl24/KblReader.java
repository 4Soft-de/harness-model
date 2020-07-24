/*-
 * ========================LICENSE_START=================================
 * kbl24
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
package com.foursoft.kblmodel.kbl24;

import com.foursoft.xml.io.read.XMLReader;
import com.foursoft.xml.model.Identifiable;

/**
 * a default implementation for a thread local stored KBL reader. Validation events are logged to slf4j.
 */
public final class KblReader extends XMLReader<KBLContainer, Identifiable> {

    private static final ThreadLocal<KblReader> localReader = ThreadLocal.withInitial(KblReader::new);

    private KblReader() {
        super(KBLContainer.class, Identifiable.class, Identifiable::getXmlId);
    }

    /**
     * @return a thread local KblReader object
     */
    public static KblReader getLocalReader() {
        return localReader.get();
    }
}
