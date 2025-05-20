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
import org.junit.jupiter.api.Test;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class DateUtilsTest {

    static {
        // Ensure this is set before DateUtils is initialized in case the Clock Test is not the first one.
        System.setProperty("overwrite.clock", "2025-05-01T00:00:00Z");
    }

    @Test
    void testClock() {
        final LocalDate fixedNow = LocalDate.now(DateUtils.CLOCK);
        assertThat(fixedNow)
                .hasDayOfMonth(1)
                .hasMonthValue(5)
                .hasYear(2025);
    }

    @Test
    void testCurrentDate() {
        final XMLGregorianCalendar calenderOfCurrentDate = DateUtils.currentDate();

        final LocalDate now = LocalDate.now();
        final LocalDate localDateFromCalendar = DateUtils.toLocalDate(calenderOfCurrentDate);

        assertThat(now).isAfterOrEqualTo(localDateFromCalendar);
    }

    @Test
    void testToXMLGregorianCalendarWithString() {
        final String dateTimeString = "2022-03-22T16:05:35.421337Z";
        final XMLGregorianCalendar calenderOfDateTime = DateUtils.toXMLGregorianCalendar(dateTimeString);
        assertThat(calenderOfDateTime).hasToString(dateTimeString);

        final String dateString = "2022-03-22";
        final XMLGregorianCalendar calenderOfDate = DateUtils.toXMLGregorianCalendar(dateString);
        assertThat(calenderOfDate.toString()).startsWith(dateString);
    }

    @Test
    void testInvalidCalls() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> DateUtils.toXMLGregorianCalendar(""));
        assertThatExceptionOfType(VecException.class)
                .isThrownBy(() -> DateUtils.toXMLGregorianCalendar("test"))
                .withRootCauseExactlyInstanceOf(IllegalArgumentException.class);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> DateUtils.toLocalDate(null));
    }

}
