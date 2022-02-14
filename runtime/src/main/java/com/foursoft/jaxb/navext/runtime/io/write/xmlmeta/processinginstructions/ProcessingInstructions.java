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
package com.foursoft.jaxb.navext.runtime.io.write.xmlmeta.processinginstructions;

import java.util.*;

/**
 * ProcessingInstructions allows adding XML-ProcessingInstructions to the output file. The ProcessingInstructions are linked to JAXB elements
 * and added directly before the xml-element.
 */
public class ProcessingInstructions {

    private final Map<Object, List<ProcessingInstruction>> map = new HashMap<>();

    public boolean containsKey(final Object key) {
        return map.containsKey(key);
    }

    public List<ProcessingInstruction> get(final Object key) {
        return map.getOrDefault(key, Collections.emptyList());
    }

    public void put(final Object key, final List<ProcessingInstruction> processingInstruction) {
        Objects.requireNonNull(key);
        map.put(key, processingInstruction);
    }

    public void put(final Object key, final ProcessingInstruction... processingInstruction) {
        Objects.requireNonNull(key);
        map.put(key, Arrays.asList(processingInstruction));
    }

}
