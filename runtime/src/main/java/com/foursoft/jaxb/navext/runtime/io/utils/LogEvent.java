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
package com.foursoft.jaxb.navext.runtime.io.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;

/**
 * Helper class to log  ValidationEvent
 */
public final class LogEvent {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogEvent.class);

    private LogEvent() {
    }

    /**
     * @param event the event to parse
     * @return the location and the message of the event
     */
    public static String getLocationAndMessage(final ValidationEvent event) {
        if (event == null) {
            return "";
        }
        return getLocation(event.getLocator()) + ": " + event.getMessage();
    }

    /**
     * @param locator a locator
     * @return the location stored in the locator as string
     */
    public static String getLocation(final ValidationEventLocator locator) {
        if (locator == null) {
            return "";
        }

        return String.valueOf(locator.getLineNumber()) +
                ':' +
                locator.getColumnNumber() +
                " (" +
                getNodeId(locator) +
                ')';
    }

    /**
     * logs the event to the slf4j
     *
     * @param event the validation event to log
     */
    public static void log(final ValidationEvent event) {
        if (event == null) {
            return;
        }
        final String locationAndMessage = getLocationAndMessage(event);
        final boolean warning = event.getSeverity() == ValidationEvent.WARNING;
        if (warning) {
            LOGGER.warn(locationAndMessage, event.getLinkedException());
        } else {
            LOGGER.error(locationAndMessage, event.getLinkedException());
        }
    }

    private static String getNodeId(final ValidationEventLocator locator) {
        final Node node = locator.getNode();
        if (node == null) {
            return "";
        }
        final NamedNodeMap attributes = node.getAttributes();
        if (attributes == null) {
            return "";
        }
        final Node id = attributes.getNamedItem("id");
        if (id == null) {
            return "";
        }
        return id.toString();
    }
}
