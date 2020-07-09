package com.foursoft.xml.io.write;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.util.ValidationEventCollector;

public class JAXB2ValidationEventCollector extends ValidationEventCollector {
    private static final Logger LOGGER = LoggerFactory.getLogger(JAXB2ValidationEventCollector.class);

    @Override
    public boolean handleEvent(final ValidationEvent event) {
        super.handleEvent(event);
        return true;
    }

    public void logEvents() {
        if (hasEvents()) {
            for (final ValidationEvent event : getEvents()) {
                LOGGER.warn(event.getMessage(), event.getLinkedException());
            }
        }
    }
}