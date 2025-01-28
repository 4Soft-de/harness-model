package com.foursoft.harness.vec.scripting.enums;

public enum TemperatureType {

    OPERATING_TEMPERATURE("OperatingTemperature"), SHORT_TERM_AGING_TEMPERATURE("ShortTermAgingTemperature");

    private final String value;

    TemperatureType(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
