package com.volkswagenag.daem.converter.vec.c2v.vecwrite;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.transform.Source;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LogValidator {
    private final Validator validator;
    private final List<Pair<Integer, String>> errorLines;
    private boolean isValid;

    public LogValidator(final Validator validator) {
        errorLines = new ArrayList<>();
        this.validator = validator;
        isValid = true;
        validator.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(final SAXParseException exception) {
                errorLines.add(ImmutablePair.of(exception.getLineNumber(), exception.getMessage()));
                isValid = false;
            }

            @Override
            public void fatalError(final SAXParseException exception) {
                errorLines.add(ImmutablePair.of(exception.getLineNumber(), exception.getMessage()));
                isValid = false;
            }

            @Override
            public void error(final SAXParseException exception) {
                errorLines.add(ImmutablePair.of(exception.getLineNumber(), exception.getMessage()));
                isValid = false;
            }
        });
    }

    public Collection<Pair<Integer, String>> getErrorLines() {
        return errorLines;
    }

    public boolean validate(final Source source) throws SAXException, IOException {
        assert isValid;
        validator.validate(source);
        return isValid;
    }
}