/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 - 2026 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.convert;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Optional;

import static com.foursoft.harness.kbl2vec.utils.UndefinedValues.isUndefined;

/**
 * Converts a KBL date string into an {@code xs:dateTime}, using the
 * {@linkplain com.foursoft.harness.kbl2vec.core.ConversionProperties#getDateFormats() configured date formats}.
 */
public class StringToDateTimeConverter implements Converter<String, Optional<XMLGregorianCalendar>> {

    private final List<DateTimeFormatter> formatters;
    private final DatatypeFactory datatypeFactory;

    public StringToDateTimeConverter(final List<String> dateFormats) {
        this.formatters = dateFormats.stream().map(DateTimeFormatter::ofPattern).toList();
        try {
            this.datatypeFactory = DatatypeFactory.newInstance();
        } catch (final DatatypeConfigurationException e) {
            throw new IllegalStateException("No XML datatype factory available.", e);
        }
    }

    /**
     * @param source A KBL date string.
     * @return The date, empty if the source is undefined or matches none of the date formats.
     */
    @Override
    public Optional<XMLGregorianCalendar> convert(final String source) {
        if (isUndefined(source)) {
            return Optional.empty();
        }
        for (final DateTimeFormatter formatter : formatters) {
            try {
                final TemporalAccessor parsed =
                        formatter.parseBest(source.trim(), LocalDateTime::from, LocalDate::from);
                final LocalDateTime dateTime = parsed instanceof final LocalDate date
                        ? date.atStartOfDay()
                        : (LocalDateTime) parsed;
                return Optional.of(toXmlDateTime(dateTime));
            } catch (final DateTimeParseException e) {
                // Try the next format.
            }
        }
        return Optional.empty();
    }

    private XMLGregorianCalendar toXmlDateTime(final LocalDateTime dateTime) {
        return datatypeFactory.newXMLGregorianCalendar(dateTime.getYear(), dateTime.getMonthValue(),
                                                       dateTime.getDayOfMonth(), dateTime.getHour(),
                                                       dateTime.getMinute(), dateTime.getSecond(),
                                                       DatatypeConstants.FIELD_UNDEFINED,
                                                       DatatypeConstants.FIELD_UNDEFINED);
    }
}
