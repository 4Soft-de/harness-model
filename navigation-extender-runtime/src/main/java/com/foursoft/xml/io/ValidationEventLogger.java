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
package com.foursoft.xml.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import java.util.function.Consumer;

/**
 * A simple logger for JAXB validation events to slf4j
 */
public class ValidationEventLogger implements Consumer<ValidationEvent> {
    private static final int INIT_CAPACITY = 128;
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationEventLogger.class);

    @Override
    public void accept(final ValidationEvent t) {
        handleLog(t, t.getSeverity() == ValidationEvent.WARNING);
    }

    private static String getLocation(final ValidationEventLocator locator) {
        final Node node = locator.getNode();
        if (node == null) {
            return "";
        }
        final Node id = node.getAttributes().getNamedItem("id");

        final StringBuilder b = new StringBuilder(INIT_CAPACITY);
        b.append(locator.getLineNumber())
                .append(':')
                .append(locator.getColumnNumber());
        if (id != null) {
            b.append(" (").append(id).append(')');
        }
        return b.toString();
    }

    private static void handleLog(final ValidationEvent e, final boolean warning) {
        final String locationAndMessage = getLocationAndMessage(e);
        if (warning) {
            LOGGER.warn(locationAndMessage, e.getLinkedException());
        } else {
            LOGGER.error(locationAndMessage, e.getLinkedException());
        }
    }

    private static String getLocationAndMessage(final ValidationEvent e) {
        return getLocation(e.getLocator()) + ": " + e.getMessage();
    }

}
