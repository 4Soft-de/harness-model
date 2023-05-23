/*-
 * ========================LICENSE_START=================================
 * Compatibility Core
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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
package com.foursoft.harness.compatibility.core.common;

import jakarta.xml.bind.ValidationEvent;
import jakarta.xml.bind.ValidationEventLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import java.util.function.Consumer;

/**
 * Simple {@link Consumer} for {@link ValidationEvent} which logs warnings / errors with a {@link Logger}.
 */
public class EventConsumer implements Consumer<ValidationEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventConsumer.class);

    @Override
    public void accept(final ValidationEvent t) {
        if (t.getSeverity() == ValidationEvent.WARNING) {
            handleWarning(t);
        } else {
            handleError(t);
        }
    }

    private static String getLocation(final ValidationEventLocator locator) {
        final StringBuilder b = new StringBuilder(100);
        final int lineNumber = locator.getLineNumber();
        if (lineNumber > 0) {
            final int columnNumber = locator.getColumnNumber();
            b.append(locator)
                    .append(':')
                    .append(columnNumber > -1 ? columnNumber : "?");
        }

        final Node n = locator.getNode();
        if (n != null) {
            final Node id = n.getAttributes().getNamedItem("id");
            if (id != null) {
                b.append(" (").append(id).append(')');
            }
        }

        if (b.length() > 0) {
            b.append(": ");
        }
        return b.toString();
    }

    private static void handleWarning(final ValidationEvent e) {
        LOGGER.warn("{}{}", getLocation(e.getLocator()), e.getMessage(), e.getLinkedException());
    }

    private static void handleError(final ValidationEvent e) {
        LOGGER.error("{}{}", getLocation(e.getLocator()), e.getMessage(), e.getLinkedException());
    }

}
