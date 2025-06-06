/*-
 * ========================LICENSE_START=================================
 * VEC Common
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
package com.foursoft.harness.vec.common.util;

import com.foursoft.harness.vec.common.exception.VecException;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;

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
     * Converts the passed {@link LocalDate} to an {@link XMLGregorianCalendar}.
     * <b>To work with the VEC, midnight will be set as the time.</b>
     *
     * @param date The LocalDate which should be converted.
     * @return A never-null {@link XMLGregorianCalendar} for the given date.
     * @throws VecException In case the conversion fails.
     */
    public static XMLGregorianCalendar toXMLGregorianCalendar(final LocalDate date) {
        return toXMLGregorianCalendar(LocalDateTime.of(date, LocalTime.MIDNIGHT));
    }

    /**
     * Converts the passed {@link LocalDateTime} to an {@link XMLGregorianCalendar}.
     *
     * @param dateTime The LocalDateTime which should be converted.
     * @return A never-null {@link XMLGregorianCalendar} for the given date.
     * @throws VecException In case the conversion fails.
     */
    public static XMLGregorianCalendar toXMLGregorianCalendar(final LocalDateTime dateTime) {
        String validDateTimeString = dateTime.toString();

        // If seconds and nanos are both 0, the String cannot be parsed because the seconds are missing in it.
        final int nanos = dateTime.getNano();
        final int seconds = dateTime.getSecond();
        if (seconds == 0 && nanos == 0) {
            validDateTimeString += ":00";
        }

        return toXMLGregorianCalendar(validDateTimeString);
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
     * @throws IllegalArgumentException If the given calendar is {@code null}.
     */
    public static LocalDate toLocalDate(final XMLGregorianCalendar calendar) {
        return toZonedDateTime(calendar).toLocalDate();
    }

    /**
     * Converts the passed {@link XMLGregorianCalendar} to a {@link LocalDateTime}.
     *
     * @param calendar The calendar to convert.
     * @return A never-null {@link LocalDateTime} for the given calendar.
     * @throws IllegalArgumentException If the given calendar is {@code null}.
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
            // Sometimes issues occur with the nanos of the XMLGregorianCalender.
            // In LocalTime, the nanos are stored as an int (e.g. 163857000).
            // The XMLGregorianCalender stores them as a double (e.g. 0.163857).
            // Sometimes, the scale is set to lower than 9 causing trailing zeros to be removed.
            // In such a case, the new nano value would be 163857 which is different from the original value.
            // Setting the scale to 9 will ensure the trailing zeros are present which will lead to correct nanos.
            // The rounding mode doesn't matter since this is only checked if the new scale is less than the old scale.
            final BigDecimal fixedScaleDecimal = fractionalSecond.setScale(9, RoundingMode.UNNECESSARY);

            final String fractionAsString = fixedScaleDecimal.toString();
            // Highest possible value: 0.999999999 -> Can always be converted to a String.
            final int nanos = Integer.parseInt(fractionAsString.substring(2));  // cut of the "0."
            zonedDateTime = zonedDateTime.withNano(nanos);
        }

        return zonedDateTime;
    }

}
