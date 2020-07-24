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
package com.foursoft.xml.io.write;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Comments allows adding XML-comments to the output file. The comments are linked to JAXB elements
 * and added directly before the xml-element.
 * e.g. if a Root-class exists which is serialized to &lt;Root&gt;&lt;/Root&gt;
 * the following code:
 * Root root = new Root();
 * Comments comments = new Comments();
 * comments.put(root, "TestComment");
 * VecWriter::write(root, comments);
 * would result in:
 * &lt;!-- TestComment --&gt;
 * &lt;Root&gt;&lt;/Root&gt;
 */
public class Comments {
    private final Map<Object, String> map = new HashMap<>();

    public boolean containsKey(final Object key) {
        return map.containsKey(key);
    }

    public Optional<String> get(final Object key) {
        return Optional.ofNullable(map.get(key));
    }

    public void put(final Object key, final String comment) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(comment);
        map.put(key, comment);
    }
}
