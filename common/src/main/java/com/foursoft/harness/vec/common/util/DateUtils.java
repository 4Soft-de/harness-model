/*-
 * ========================LICENSE_START=================================
 * vec-common
 * %%
 * Copyright (C) 2020 - 2022 4Soft GmbH
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
package com.foursoft.harness.vec.common.util;

import com.foursoft.harness.vec.common.exception.VecException;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * Utility class for date related operations.
 * <p>
 * Amongst others, it provides methods to create {@link XMLGregorianCalendar}s which is used
 * for date fields within the VEC.
 */
public final class DateUtils {

    private DateUtils() {
        // hide default constructor
    }

    /**
     * Creates a {@link XMLGregorianCalendar} for the current date.
     *
     * @return A never-null {@link XMLGregorianCalendar} for the current date.
     * @throws VecException In case the conversion fails.
     */
    public static XMLGregorianCalendar currentDate() {
        final Instant date = Instant.now();
        return toXMLGregorianCalendar(date.toString());
    }

    /**
     * Converts the passed {@link LocalDateTime} to an {@link XMLGregorianCalendar}.
     *
     * @param date The LocalDate which should be converted.
     * @return A never-null {@link XMLGregorianCalendar} for the given date.
     * @throws VecException In case the conversion fails.
     */
    public static XMLGregorianCalendar toXMLGregorianCalendar(final LocalDate date) {
        return toXMLGregorianCalendar(date.toString());
    }

    /**
     * Converts the passed {@link LocalDateTime} to an {@link XMLGregorianCalendar}.
     *
     * @param dateTime The LocalDateTime which should be converted.
     * @return A never-null {@link XMLGregorianCalendar} for the given date.
     * @throws VecException In case the conversion fails.
     */
    public static XMLGregorianCalendar toXMLGregorianCalendar(final LocalDateTime dateTime) {
        return toXMLGregorianCalendar(dateTime.toString());
    }

    /**
     * Converts the passed date as string to an {@link XMLGregorianCalendar}.
     *
     * @param dateTime The date as string in form {@code yyyyMMdd HH:mm:ss.SSSSSS Z}.
     * @return A never-null {@link XMLGregorianCalendar} for the given date.
     * @throws IllegalArgumentException In case the given input is null or an empty String.
     * @throws VecException             In case the conversion fails.
     */
    public static XMLGregorianCalendar toXMLGregorianCalendar(final String dateTime) {
        if (StringUtils.isEmpty(dateTime)) {
            throw new IllegalArgumentException("Given String may not be empty or null.");
        }

        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(dateTime);
        } catch (final DatatypeConfigurationException | IllegalArgumentException e) {
            throw new VecException(String.format("Failed to create date calender for datetime %s.", dateTime), e);
        }
    }

    /**
     * Converts the passed {@link XMLGregorianCalendar} to a {@link LocalDate}.
     *
     * @param calendar The calendar to convert.
     * @return A never-null {@link LocalDate} for the given calendar.
     */
    public static LocalDate toLocalDate(final XMLGregorianCalendar calendar) {
        return toZonedDateTime(calendar).toLocalDate();
    }

    /**
     * Converts the passed {@link XMLGregorianCalendar} to a {@link LocalDateTime}.
     *
     * @param calendar The calendar to convert.
     * @return A never-null {@link LocalDateTime} for the given calendar.
     */
    public static LocalDateTime toLocalDateTime(final XMLGregorianCalendar calendar) {
        return toZonedDateTime(calendar).toLocalDateTime();
    }

    private static ZonedDateTime toZonedDateTime(final XMLGregorianCalendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException("Given Calender may not be null.");
        }

        ZonedDateTime zonedDateTime = calendar.toGregorianCalendar().toZonedDateTime();

        // The nanos are not respected while converting to the GregorianCalendar.
        final BigDecimal fractionalSecond = calendar.getFractionalSecond();
        if (fractionalSecond != null) {
            final String fractionAsString = fractionalSecond.toString();
            // Highest possible value: 0.999999999 -> Can always be converted to a String.
            final int nanos = Integer.parseInt(fractionAsString.substring(2));  // cut of the "0."
            zonedDateTime = zonedDateTime.withNano(nanos);
        }

        return zonedDateTime;
    }

}
