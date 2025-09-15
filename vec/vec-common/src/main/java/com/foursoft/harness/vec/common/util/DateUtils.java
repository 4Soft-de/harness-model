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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    /**
     * {@link Clock} which either uses UTC time
     * or a fixed UTC time, controlled by defining {@code overwrite.clock}.
     */
    public static final Clock CLOCK;

    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);

    static {
        final String overwriteClock = System.getProperty("overwrite.clock", null);
        if (overwriteClock != null) {
            final Instant instant = Instant.parse(overwriteClock); // e.g. "2025-05-01T00:00:00Z"
            CLOCK = Clock.fixed(instant, ZoneOffset.UTC);
            LOGGER.warn("The Clock used for Instant#now was overridden with the value '{}'.", overwriteClock);
        } else {
            CLOCK = Clock.systemUTC();
        }
    }

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
        return toXMLGregorianCalendar(Instant.now(CLOCK));
    }

    /**
     * Converts the given {@link Instant} to an {@link XMLGregorianCalendar}.
     *
     * @param instant Instant to set.
     * @return A never-null {@link XMLGregorianCalendar} for the given date.
     * @throws VecException In case the conversion fails.
     */
    public static XMLGregorianCalendar toXMLGregorianCalendar(final Instant instant) {
        return toXMLGregorianCalendar(instant.toString());
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
        return toXMLGregorianCalendar(dateTime.atOffset(ZoneOffset.UTC).toInstant());
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
            final XMLGregorianCalendar xmlGregorianCalendar =
                    DatatypeFactory.newInstance().newXMLGregorianCalendar(dateTime);

            final int timezone = xmlGregorianCalendar.getTimezone();
            if (timezone == Integer.MIN_VALUE) {  // Missing "Z" in given String.
                xmlGregorianCalendar.setTimezone(0);
            } else if (timezone != 0) {
                // Instead of manually adjusting the day, hour and minute based on the offset, simply
                // convert the String to an Instant and create another calendar.
                // This performs the offset adjustment automatically and will return a calendar with no offset.
                final XMLGregorianCalendar subCalendar = toXMLGregorianCalendar(Instant.parse(dateTime));

                // When creating another calendar from the parsed Instant, it may not contain
                // the milliseconds anymore if they are zero even they were explicitly given in the original String.
                // With that, they will be restored but only in this special case.
                // This has the advantage that toString will return a more similar format to the original input.
                if (subCalendar.getFractionalSecond() == null) {
                    final BigDecimal fractional = BigDecimal.valueOf(0.0)
                            // Scale is actually important for the String format.
                            .setScale(3, RoundingMode.UNNECESSARY);
                    subCalendar.setFractionalSecond(fractional);
                }

                return subCalendar;
            }

            return xmlGregorianCalendar;
        } catch (final DatatypeConfigurationException | IllegalArgumentException e) {
            throw new VecException(String.format("Failed to create date calender for datetime %s.", dateTime), e);
        }
    }

    /**
     * Converts the passed {@link XMLGregorianCalendar} to an {@link Instant}.
     *
     * @param calendar The calendar to convert.
     * @return A never-null {@link Instant} for the given calendar.
     * @throws IllegalArgumentException If the given calendar is {@code null}.
     */
    public static Instant toInstant(final XMLGregorianCalendar calendar) {
        return toZonedDateTime(calendar).toInstant();
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
        if (fractionalSecond != null && fractionalSecond.doubleValue() != 0.0) {
            // Sometimes issues occur with the nanos of the XMLGregorianCalender.
            // In LocalTime, the nanos are stored as an int (e.g. 163857000).
            // The XMLGregorianCalender stores them as a double (e.g. 0.163857).
            // Sometimes, the scale is set to lower than 9 causing trailing zeros to be removed.
            // In such a case, the new nano value would be 163857 which is different from the original value.
            // Setting the scale to 9 will ensure the trailing zeros are present which will lead to correct nanos.
            // The rounding mode doesn't matter since this is only checked if the new scale is less than the old scale.
            final BigDecimal fixedScaleDecimal = fractionalSecond.setScale(9, RoundingMode.UNNECESSARY);

            // toString might return E notation (e.g. "0E-9"), thus using toPlainString.
            final String fractionAsString = fixedScaleDecimal.toPlainString();
            // Highest possible value: 0.999999999 -> Can always be converted to a String.
            final int nanos = Integer.parseInt(fractionAsString.substring(2));  // cut of the "0."
            zonedDateTime = zonedDateTime.withNano(nanos);
        }

        return zonedDateTime;
    }

}
