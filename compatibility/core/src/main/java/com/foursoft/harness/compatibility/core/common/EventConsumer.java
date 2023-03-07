package com.foursoft.harness.compatibility.core.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
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
