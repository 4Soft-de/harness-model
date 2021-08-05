package com.foursoft.vecmodel.vec120;

import javax.xml.validation.Schema;

public class SchemaFactory {

    private static final String SCHEMA_PATH = "vec120/vec_1.2.1.RC-SNAPSHOT.xsd";
    private static final String STRICT_SCHEMA_PATH = "vec120/vec_1.2.0-strict.xsd";

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
