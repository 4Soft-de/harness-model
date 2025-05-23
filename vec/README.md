﻿# VEC model

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.foursoft.harness.vec/vec-parent/badge.svg)](https://mvnrepository.com/artifact/com.foursoft.harness.vec)

JAXB-Models of the VEC, based on the underlying UML model (not only the XSD).

## Introduction

VEC stands for **V**ehicle **E**lectric **C**ontainer and defines an information model, a data dictionary, and an XML
schema derived from and compliant to the model.

It can be used to define a vehicle with all parts, connections, constraints, etc.

This repository contains three VEC versions: Version 1.1.3, Version 1.2.X and version 2.X.X.
The version 1.2.X doesn't provide a backwards compatibility to version 1.1.3.
If you have trouble converting from VEC 1.1.3 to VEC 1.2.X, feel free to
contact our colleague [Johannes Becker](mailto:becker@4soft.de?subject=VEC%20Compatibility%20layer).

For an optimized performance, the XML is parsed by our
[enhanced xml navigation library](https://github.com/4Soft-de/harness-model/tree/develop/navext) which builds on top of JAXB.

More information about VEC can be found in the [ECAD wiki](https://ecad-wiki.prostep.org/specifications/vec).

## Key Features

VEC contains data of a multi-harness overall wiring system and includes its schematic information. Its key features are:

- Multiple 3D harness geometries (including variant configurations)
- Multiple 2D drawing information
- System schematics (including variant configurations)
- Harness topology and wire routing
- Electrical wiring and connectivity
- Module-based bill of material
- Multi-level composite part structures (e.g. for assembly parts)
- Predefined harness configurations
- Powerful property-based extensibility
- Comprehensive master data information
- Universal lifecycle and versioning information
- Defined mappings to external resources
- Elements can have back references to other elements (e.g. `VecUnit -> VecValueWithUnit`
  and `VecValueWithUnit -> VecUnit`)

## Download

Our builds are distributed to [Maven Central](https://mvnrepository.com/artifact/com.foursoft.harness.vec).

Latest
Version: [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.foursoft.harness.vec/vec-parent/badge.svg)](https://mvnrepository.com/artifact/com.foursoft.harness.vec)

**Make sure to replace the `VERSION` below with a real version as the one shown above!**

### VEC 1.1.3

#### Maven

```xml
<dependency>
    <groupId>com.foursoft.harness.vec</groupId>
    <artifactId>vec-v113</artifactId>
    <version>VERSION</version>
</dependency>
```

#### Gradle

```groovy
implementation group: 'com.foursoft.harness.vec', name: 'vec-v113', version: 'VERSION'
```

### VEC 1.2.X

#### Maven

```xml
<dependency>
    <groupId>com.foursoft.harness.vec</groupId>
    <artifactId>vec-v12x</artifactId>
    <version>VERSION</version>
</dependency>
```

#### Gradle

```groovy
implementation group: 'com.foursoft.harness.vec', name: 'vec-v12x', version: 'VERSION'
```
### VEC 2.X.X

#### Maven

```xml
<dependency>
    <groupId>com.foursoft.harness.vec</groupId>
    <artifactId>vec-v2x</artifactId>
    <version>VERSION</version>
</dependency>
```

#### Gradle

```groovy
implementation group: 'com.foursoft.harness.vec', name: 'vec-v2x', version: 'VERSION'
```

## Code examples

In the codebase, the root of a vec file is the `VecContent` class.

More examples can be found in the examples of each module:
- [VEC 1.1.3 examples](https://github.com/4Soft-de/harness-model/tree/develop/vec/vec-v113/src/examples/)
- [VEC 1.2.X examples](https://github.com/4Soft-de/harness-model/tree/develop/vec/vec-v12x/src/examples/)
- [VEC 2.X.X examples](https://github.com/4Soft-de/harness-model/tree/develop/vec/vec-v2x/src/examples/)

### Reading a VEC file

#### Example VEC file

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?cs checksum="2764192431"?>
<?xsd validated="true"?>
<vec:VecContent id="id_1000_0" xmlns:vec="http://www.prostep.org/ecad-if/2011/vec"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema">
    <CustomProperty id="id_1006_2" xsi:type="vec:SimpleValueProperty">
        <PropertyType>ExampleType</PropertyType>
        <Value>CAP</Value>
    </CustomProperty>
    <VecVersion>1.1.3</VecVersion>
    <Contract id="id_2107_0">
        <CompanyName>4Soft GmbH</CompanyName>
        <ContractRole>Oem</ContractRole>
    </Contract>
    <DocumentVersion id="id_1002_0">
        <CompanyName>4Soft GmbH</CompanyName>
        <Approval id="id_2014_0">
            <Status>Approved</Status>
            <Permission id="id_2185_0">
                <Permission>Released</Permission>
                <PermissionDate>2020-05-28T16:07:46.503+02:00</PermissionDate>
                <Permitter id="id_2186_0">
                    <LastName>/NULL</LastName>
                </Permitter>
            </Permission>
        </Approval>
    </DocumentVersion>
</vec:VecContent>
```

#### Java file

```java
public class MyVecReader {
    public void readVecFile(final String pathToFile) throws IOException {
        try (final InputStream is = MyVecReader.class.getResourceAsStream(pathToFile)) {
            final VecReader vecReader = new VecReader();
            final JaxbModel<VecContent, Identifiable> model = vecReader.readModel(is);

            final VecApproval approval = model.getIdLookup()
                    .findById(VecApproval.class, "id_2014_0")
                    .orElse(null);

            // get a specific permission
            final VecPermission vecPermission = model.getIdLookup()
                    .findById(VecPermission.class, "id_2185_0")
                    .orElse(null);

            // get all permissions of an approval (in oop style)
            final List<VecPermission> permissions = approval.getPermissions();
        }
    }
}
```

### Writing a VEC file

#### Java file

```java
public class MyVecWriter {
    public void writeExampleVecFile(final String target) throws IOException {
        final LocalDate exampleDate = LocalDate.of(2022, 3, 24);
        final LocalDateTime exampleDateTime = LocalDateTime.of(exampleDate, LocalTime.NOON);

        final VecContent root = new VecContent();
        root.setXmlId("id_1000_0");
        root.setVecVersion("1.1.3");
        root.setDateOfCreation(DateUtils.toXMLGregorianCalendar(exampleDate));

        final VecPermission permission = new VecPermission();
        permission.setXmlId("id_2185_0");
        permission.setPermission("Released");
        permission.setPermissionDate(DateUtils.toXMLGregorianCalendar(exampleDateTime));

        final VecApproval approval = new VecApproval();
        approval.setXmlId("id_2014_0");
        approval.setStatus("Approved");
        approval.getPermissions().add(permission);

        final VecDocumentVersion documentVersion = new VecDocumentVersion();
        documentVersion.setXmlId("id_1002_0");
        documentVersion.getApprovals().add(approval);
        documentVersion.setDocumentNumber("123_456_789");

        final VecSpecification specification = new VecConnectorHousingCapSpecification();
        specification.setXmlId("id_2000_0");
        specification.setIdentification("Ccs-123_456_789-1");

        documentVersion.getSpecifications().add(specification);

        final VecPartVersion partVersion = new VecPartVersion();
        partVersion.setXmlId("id_1001_0");
        partVersion.setPartNumber("123_456_789");

        root.getDocumentVersions().add(documentVersion);
        root.getPartVersions().add(partVersion);

        final VecWriter vecWriter = new VecWriter();

        try (final FileOutputStream outputStream = new FileOutputStream(target)) {
            vecWriter.write(root, outputStream);
        }
    }
}
```

#### Generated VEC file

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<vec:VecContent id="id_1000_0" xmlns:vec="http://www.prostep.org/ecad-if/2011/vec">
    <VecVersion>1.1.3</VecVersion>
    <DateOfCreation>2022-03-24T00:00:00</DateOfCreation>
    <DocumentVersion id="id_1002_0">
        <Approval id="id_2014_0">
            <Status>Approved</Status>
            <Permission id="id_2185_0">
                <Permission>Released</Permission>
                <PermissionDate>2022-03-24T12:00:00</PermissionDate>
            </Permission>
        </Approval>
        <DocumentNumber>123_456_789</DocumentNumber>
        <Specification xsi:type="vec:ConnectorHousingCapSpecification" id="id_2000_0"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
            <Identification>Ccs-123_456_789-1</Identification>
        </Specification>
    </DocumentVersion>
    <PartVersion id="id_1001_0">
        <PartNumber>123_456_789</PartNumber>
    </PartVersion>
</vec:VecContent>
```

## Contributing

See [parent ReadMe](https://github.com/4Soft-de/harness-model/blob/develop/README.md#contributing).