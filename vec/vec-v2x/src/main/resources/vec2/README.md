## Updating VEC Schema Files

Since Version 2.X.X the VEC follows semantic versioning. Therefore, it should be okay to integrate
minor updates of the schema files without breaking the `com.foursoft.harness.vec.v2x` API. To update the Schema,
follow these steps:

- Download the latest VEC 2.X.X schema files from the ECAD-WIKI.
- Replace the existing schema files in `src/main/resources/schema/vec/v2x/` with the newly downloaded ones.
- Fix the file name references in the SchemaFactory class located
  `src/main/java/com/foursoft/harness/vec/v2x/validation/SchemaFactory.java`.
- Run the `scripts/relocate-doc-in-xsd.xsl` on the XSDs, to make the embedded docs XJC compatible.
- Run the `scripts/generate-xjb-from-schema.xsl` on the XSDs to generate the `vec_2.x.x.xjb` file.
- Adapt the `schemaLocation="vec_2.2.0.xsd"` attribute in the XJB files located in this directory.
- Also add the files (including model and ontologies) to `vec-rdf/vec-rdf-common/src/main/resources/vec`
- Fix the `com.foursoft.harness.vec.v2x.Version.VERSION` constant.
- Rebuild the complete project, fix compile errors (e.g. due to new type methods in visitor classes).