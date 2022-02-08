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
package com.foursoft.xml.io.utils;

import jakarta.xml.bind.ValidationEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * a simple collector, which collects all events and logs them all by calling logEvents
 */
public class ValidationEventCollector implements Consumer<ValidationEvent> {
    private final List<ValidationEvent> events = new ArrayList<>();

    @Override
    public void accept(final ValidationEvent t) {
        events.add(t);
    }

    /**
     * logs all collected messages
     */
    public void logEvents() {
        if (events.isEmpty()) {
            return;
        }
        for (final ValidationEvent event : events) {
            LogEvent.log(event);
        }
    }

    public boolean hasEvents() {
        return !events.isEmpty();
    }
}
