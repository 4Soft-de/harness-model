package com.foursoft.vecmodel.vec113;

import javax.xml.validation.Schema;

public class SchemaFactory {

    private static final String SCHEMA_PATH = "vec113/vec_1.1.3.xsd";
    private static final String STRICT_SCHEMA_PATH = "vec113/vec_1.1.3.xsd";

    private SchemaFactory() {
        // Hide constructor
    }

    public static Schema getStrictSchema() {
        return com.foursoft.xml.io.validation.SchemaFactory.getSchema(STRICT_SCHEMA_PATH);
    }

    public static Schema getSchema() {
        return com.foursoft.xml.io.validation.SchemaFactory.getSchema(SCHEMA_PATH);
    }
}
