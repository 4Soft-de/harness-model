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
package com.foursoft.xml.io.write.xmlmeta;

import javax.xml.bind.Marshaller.Listener;
import java.util.*;

public class MarshallerListener extends Listener {

    private final List<Listener> listeners;
    private final Set<Object> visitObjects = new HashSet<>();

    public MarshallerListener() {
        listeners = new ArrayList<>();
    }

    public MarshallerListener(final Listener... listeners) {
        this.listeners = Arrays.asList(listeners);
    }

    public void addListener(final Listener listener) {
        listeners.add(listener);
    }

    public void clear() {
        visitObjects.clear();
        listeners.clear();
    }

    @Override
    public void beforeMarshal(final Object source) {
        if (visitObjects.contains(source)) {
            return;
        }
        listeners.forEach(c -> c.beforeMarshal(source));
        visitObjects.add(source);
    }
}
