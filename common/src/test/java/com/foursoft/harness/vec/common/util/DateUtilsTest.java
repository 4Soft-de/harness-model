package com.foursoft.harness.vec.common.util;

import com.foursoft.harness.vec.common.exception.VecException;
import org.junit.jupiter.api.Test;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class DateUtilsTest {

    @Test
    void testCurrentDate() {
        final XMLGregorianCalendar calenderOfCurrentDate = DateUtils.currentDate();

        final LocalDate now = LocalDate.now();
        final LocalDate localDateFromCalendar = DateUtils.toLocalDate(calenderOfCurrentDate);

        assertThat(now).isAfterOrEqualTo(localDateFromCalendar);
    }

    @Test
    void testToXMLGregorianCalendarWithDate() {
        final LocalDate now = LocalDate.now();
        final XMLGregorianCalendar calenderOfDate = DateUtils.toXMLGregorianCalendar(now);

        assertThat(now).hasToString(calenderOfDate.toString());

        final LocalDate localDateFromCalendar = DateUtils.toLocalDate(calenderOfDate);
        assertThat(now).isEqualTo(localDateFromCalendar);
    }

    @Test
    void testToXMLGregorianCalendarWithDateTime() {
        final LocalDateTime now = LocalDateTime.now();
        final XMLGregorianCalendar calenderOfDate = DateUtils.toXMLGregorianCalendar(now);

        assertThat(now).hasToString(calenderOfDate.toString());

        final LocalDateTime localDateTimeFromCalendar = DateUtils.toLocalDateTime(calenderOfDate);
        assertThat(now).isEqualTo(localDateTimeFromCalendar);
    }

    @Test
    void testToXMLGregorianCalendarWithString() {
        final String dateTimeString = "2022-03-22T16:05:35.421337Z";
        final XMLGregorianCalendar calenderOfDateTime = DateUtils.toXMLGregorianCalendar(dateTimeString);
        assertThat(calenderOfDateTime).hasToString(dateTimeString);

        final String dateString = "2022-03-22";
        final XMLGregorianCalendar calenderOfDate = DateUtils.toXMLGregorianCalendar(dateString);
        assertThat(calenderOfDate).hasToString(dateString);
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
