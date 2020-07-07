# VEC model
JAXB-Models of the VEC, based on the underlying UML model (not only the XSD).

## Introduction
VEC stands for **V**ehicle **E**lectric **C**ontainer and defines an information model, a data dictionary, and
an XML schema derived from and compliant to the model.

It can be used to define a vehicle with all parts, connections, constraints, etc. 

This repository contains two VEC versions, version 1.1.3 and version 1.2.0.
The version 1.2.0 doesn't provide a backwards compatibility to version 1.1.3.
If you have trouble converting from VEC 1.1.3 to VEC 1.2.0, feel free to
contact our colleague [Johannes Becker](mailto:becker@4soft.de?subject=VEC%20Compatibility%20layer).

For an optimized performance, the XML is parsed by our
[enhanced xml navigation library](https://github.com/4Soft-de/jaxb-enhanced-navigation) which builds on top of JAXB.

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
- Elements can have back references to other elements (e.g. `VecUnit -> VecValueWithUnit` and `VecValueWithUnit -> VecUnit`)

## Download
Our builds are distributed to [Maven Central](https://mvnrepository.com/artifact/com.foursoft.vecmodel).

Latest Version: [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.foursoft.vecmodel/vec-parent/badge.svg)](https://mvnrepository.com/artifact/com.foursoft.vecmodel)

**Make sure to replace the `VERSION` below with a real version as the one shown above!**

### VEC 1.1.3
#### Maven
```xml
<dependency>
    <groupId>com.foursoft.vecmodel</groupId>
    <artifactId>vec113</artifactId>
    <version>VERSION</version>
</dependency>
```

#### Gradle
```groovy
implementation group: 'com.foursoft.vecmodel', name: 'vec113', version: 'VERSION'
```

### VEC 1.2.0
#### Maven
```xml
<dependency>
    <groupId>com.foursoft.vecmodel</groupId>
    <artifactId>vec120</artifactId>
    <version>VERSION</version>
</dependency>
```

#### Gradle
```groovy
implementation group: 'com.foursoft.vecmodel', name: 'vec120', version: 'VERSION'
```

## Code examples

In the codebase, the root of a vec file is the `VecContent` class.

More examples can be found in the examples of each module:
- [VEC 1.1.3 examples](https://github.com/4Soft-de/vec-model/tree/develop/vec113/src/examples/)
- [VEC 1.2.0 examples](https://github.com/4Soft-de/vec-model/tree/develop/vec120/src/examples/)

### Reading a VEC file
#### Example VEC file
```xml
<?xml version="1.0" encoding="UTF-8"?>
<?cs checksum="2764192431"?>
<?xsd validated="true"?>
<vec:VecContent id="id_1000_0" xmlns:vec="http://www.prostep.org/ecad-if/2011/vec" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema">
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
    public void readVecFile(final String pathToFile) throws JAXBException, IOException  {
        try (final InputStream is = MyVecReader.class.getResourceAsStream(pathToFile)) {
            final ExtendedUnmarshaller<VecContent, Identifiable> unmarshaller =
                new ExtendedUnmarshaller<VecContent, Identifiable>(VecContent.class)
                    .withBackReferences()
                    .withIdMapper(Identifiable.class, Identifiable::getXmlId);
    
            final JaxbModel<VecContent, Identifiable> model = unmarshaller
                    .unmarshall(new BufferedInputStream(is));
    
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
    public void writeVecFile(final String target) throws JAXBException, TransformerFactoryConfigurationError, IOException {
        final JAXBContext jc = JAXBContext.newInstance(VecContent.class);

        final VecContent root = new VecContent();
        root.setXmlId("id_1000_0");
        root.setVecVersion("1.1.3");

        final VecPermission permission = new VecPermission();
        permission.setPermission("Released");

        final VecApproval approval = new VecApproval();
        approval.setStatus("Approved");
        approval.getPermissions().add(permission);

        final VecDocumentVersion documentVersion = new VecDocumentVersion();
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

        final Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        final StringWriter stringWriter = new StringWriter();
        marshaller.marshal(root, stringWriter);
        final String result = stringWriter.toString();

        final Path outPath = Paths.get(target).toAbsolutePath();
        if (Files.notExists(outPath))  {
            final Path parentFolder = outPath.getParent();
            if (parentFolder != null) {
                Files.createDirectory(parentFolder);
            }
            Files.createFile(outPath);
        }

        Files.write(outPath, result.getBytes(StandardCharsets.UTF_8));
    }
}
```

#### Generated VEC file
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ns2:VecContent id="id_1000_0" xmlns:ns2="http://www.prostep.org/ecad-if/2011/vec">
    <VecVersion>1.1.3</VecVersion>
    <DocumentVersion>
        <Approval>
            <Status>Approved</Status>
            <Permission>
                <Permission>Released</Permission>
            </Permission>
        </Approval>
        <DocumentNumber>123_456_789</DocumentNumber>
        <Specification xsi:type="ns2:ConnectorHousingCapSpecification" id="id_2000_0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
            <Identification>Ccs-123_456_789-1</Identification>
        </Specification>
    </DocumentVersion>
    <PartVersion id="id_1001_0">
        <PartNumber>123_456_789</PartNumber>
    </PartVersion>
</ns2:VecContent>
```

## Contributing
We appreciate if you like to contribute to our project! Please make sure to base your branch off of our [develop branch](https://github.com/4Soft-de/vec-model/tree/develop) and create your PR into that same branch. We will reject any PRs not following that or if the feature is already worked on.

Please read our detailed [Contribution Guidelines](https://github.com/4Soft-de/vec-model/blob/develop/.github/CONTRIBUTING.md) for more information, for example code style, formatter, etc.