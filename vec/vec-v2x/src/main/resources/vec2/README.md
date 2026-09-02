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
- Run the `scripts/generate-structured-primitive-xjb-from-model.xsl` on the UML XMI file (downloadable from the
  ECAD-WIKI) to generate `vec_2.x.x-structured-primitives.xjb`
- Adapt the `schemaLocation="vec_2.2.0.xsd"` attribute in the XJB files located in this directory.
- Also add the files (including model and ontologies) to `vec-rdf/vec-rdf-common/src/main/resources/vec`
- Fix the `com.foursoft.harness.vec.v2x.Version.VERSION` constant.
- Fix the schema file names in `OpenEnumerationSchemaTest`, which pins the generated open enumerations
  against the strict schema.
- Rebuild the complete project, fix compile errors (e.g. due to new type methods in visitor classes).

Note that the strict schema is a build input, not only a validation resource: the literals of the
[open enumerations](../../../../../../docs/open-enumerations.md) are generated from it. Two
consequences for an update:

- If two literals of one open enumeration resolve to the same Java constant name, the build fails
  with both values named. Add an entry for one of them to `vec_2.x.x-open-enum-names.xml`, which also
  holds the names that are merely unreadable by default (`#1`, `12V`, `1/2`, …).
- `OpenEnumerationSchemaTest` fails when upstream added, removed or renamed a literal. That is the
  intended signal: those constants are published API.