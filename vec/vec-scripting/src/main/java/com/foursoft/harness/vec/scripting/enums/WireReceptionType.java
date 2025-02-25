package com.foursoft.harness.vec.scripting.enums;

public enum WireReceptionType {

    CRIMP("Crimp"), SOLDERING("Soldering"), PLASMA_SOLDERING("PlasmaSoldering");

    private final String value;

    WireReceptionType(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
