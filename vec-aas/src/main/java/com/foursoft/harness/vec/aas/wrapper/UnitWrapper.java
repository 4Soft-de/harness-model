package com.foursoft.harness.vec.aas.wrapper;

public class UnitWrapper extends GenericVecObjectWrapper {

    private UnitWrapper(final Object contextObject) {
        super(contextObject);
    }

    public String getUnEceCode() {
        return (String) getFieldValue("unEceCode");
    }

    public static UnitWrapper wrap(final Object contextObject) {
        return new UnitWrapper(contextObject);
    }

}
