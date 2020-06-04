/*-
 * ========================LICENSE_START=================================
 * vec113
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
package com.foursoft.vecmodel.vec113.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import java.util.function.Consumer;

public class EventConsumer implements Consumer<ValidationEvent> {

    private static final int INIT_CAPACITY = 100;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventConsumer.class);

    private static String getLocation(final ValidationEventLocator locator) {
        if (locator.getNode() == null) {
            return "";
        }
        final StringBuilder b = new StringBuilder(INIT_CAPACITY);
        b.append(locator.getLineNumber())
                .append(':')
                .append(locator.getColumnNumber());
        final Node n = locator.getNode();
        final Node id = n.getAttributes().getNamedItem("id");
        if (id != null) {
            b.append(" (").append(id).append(')');
        }
        return b.toString();
    }

    @Override
    public void accept(final ValidationEvent e) {
        if (e.getSeverity() == ValidationEvent.WARNING) {
            handleLog(e, true);
        } else {
            handleLog(e, false);
        }
    }

    private void handleLog(final ValidationEvent e, boolean warning)  {
        final String locationAndMessage = getLocationAndMessage(e);
        if (warning)  {
            LOGGER.warn(locationAndMessage, e.getLinkedException());
        }  else  {
            LOGGER.error(locationAndMessage, e.getLinkedException());
        }
    }

    private String getLocationAndMessage(final ValidationEvent e) {
        return String.format("%s: %s", getLocation(e.getLocator()), e.getMessage());
    }

}
