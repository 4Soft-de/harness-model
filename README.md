# KBL model

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Java11](https://img.shields.io/badge/java-11-blue)](https://img.shields.io/badge/java-11-blue)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.foursoft.harness.kbl/kbl-parent/badge.svg)](https://mvnrepository.com/artifact/com.foursoft.harness.kbl)

JAXB-Model of the KBL, based on the underlying UML-model (not only the XSD).

## Introduction

KBL is German and stands for **K**abel**b**aum**l**iste (engl. Harness Description List) and defines an information
model, a data dictionary, and an XML schema derived from and compliant to the model.

It can be used to define a single harness with all parts, connections, constraints, etc.

For an optimized performance, the XML is parsed by our
[enhanced xml navigation library](https://github.com/4Soft-de/jaxb-enhanced-navigation) which builds on top of JAXB.

More information about KBL can be found in the [ECAD wiki](https://ecad-wiki.prostep.org/specifications/kbl).

## Key Features

KBL contains data of a single harness and includes the physical bordnet only. Its key features are:

- 3D harness geometry (XOR 2D single sheet drawing information)
- Harness topology and wire routing
- Electrical wiring and connectivity
- Module-based bill of material, including assembly parts
- Predefined harness configurations
- Simple installation and processing instructions
- Rudimentary master data information
- Simple change and approval meta-data
- Simple external references

## Download

Our builds are distributed to [Maven Central](https://mvnrepository.com/artifact/com.foursoft.harness.kbl).

Latest
Version: [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.foursoft.harness.kbl/kbl-parent/badge.svg)](https://mvnrepository.com/artifact/com.foursoft.harness.kbl)

**Make sure to replace the `VERSION` below with a real version as the one shown above!**

### Maven

```xml

<dependency>
    <groupId>com.foursoft.harness.kbl</groupId>
    <artifactId>kbl-v24</artifactId>
    <version>VERSION</version>
</dependency>
```

### Gradle

```groovy
implementation group: 'com.foursoft.harness.kbl', name: 'kbl-v24', version: 'VERSION'
```

## Code examples

In the codebase, the root of a kbl file is the `KBLContainer` class.

More examples can be found [in the examples](https://github.com/4Soft-de/kbl-model/tree/develop/v24/src/examples/).

### Reading a KBL file

#### Example KBL file

```xml
<?xml version="1.0" encoding="UTF-8"?>
<kbl:KBL_container xmlns:kbl="http://www.prostep.org/Car_electric_container/KBL2.3/KBLSchema" id="ID000"
                   version_id="version_id0">
    <Connector_housing id="ch_part">
        <Part_number>Part_number14</Part_number>
        <Company_name>Company_name14</Company_name>
        <Version>Version14</Version>
        <Abbreviation>Abbreviation14</Abbreviation>
        <Description>Description186</Description>
    </Connector_housing>
    <Harness id="I1397">
        <Part_number>Part_number22</Part_number>
        <Company_name>Company_name22</Company_name>
        <Version>Version22</Version>
        <Abbreviation>Abbreviation22</Abbreviation>
        <Description>Description226</Description>
        <Copyright_note>Copyright_note22</Copyright_note>
        <Project_number>Project_number0</Project_number>
        <Car_classification_level_2>asdf</Car_classification_level_2>
        <Model_year>Model_year0</Model_year>
        <Content>harness complete set</Content>
        <Connector_occurrence id="I1616">
            <Id>Id184</Id>
            <Description>Description265</Description>
            <Part>ch_part</Part>
            <Installation_information id="I1632">
                <Instruction_type>Instruction_type390</Instruction_type>
                <Instruction_value>Instruction_value390</Instruction_value>
            </Installation_information>
        </Connector_occurrence>
    </Harness>
</kbl:KBL_container>
```

#### Java file

```java
public class MyKblReader {
    public void readKblFile(final String pathToFile) throws IOException {
        try (final InputStream is = MyKblReader.class.getResourceAsStream(pathToFile)) {
            final KblReader kblReader = new KblReader();
            final JaxbModel<KBLContainer, Identifiable> model = kblReader.readModel(is);

            final KblConnectorOccurrence occurrence = model.getIdLookup()
                    .findById(KblConnectorOccurrence.class, "I1616")
                    .orElse(null);

            final KblConnectorHousing part = model.getIdLookup()
                    .findById(KblConnectorHousing.class, "ch_part")
                    .orElse(null);

            final boolean equalsPart = occurrence.getPart().equals(part);

            final KblHarness parentHarness = occurrence.getParentHarness();
            final KblHarness harnessOfRootElement = model.getRootElement().getHarness();

            final boolean equalsHarness = harnessOfRootElement.equals(parentHarness);
            System.out.println("Harness of root element identical to parent harness of occurrence? " + (equalsHarness));

            final Set<KblConnectorOccurrence> singleton = Collections.singleton(occurrence);
            final Set<KblConnectorOccurrence> refConnectorOccurrence = part.getRefConnectorOccurrence();

            final boolean equalsOccurrence = refConnectorOccurrence.equals(singleton);
            System.out.println("Occurrence being equals to the referenced connector of the part? " + (equalsOccurrence));
        }
    }
}
```

### Writing a KBL file

#### Java file

```java
public class MyKblWriter {
    public void writeExampleKblFile(final String target) throws IOException {
        final KBLContainer root = new KBLContainer();
        root.setXmlId("ID000");
        root.setVersionId("version_id0");

        final KblHarness harness = new KblHarness();
        harness.setXmlId("I1397");

        root.setHarness(harness);

        final KblConnectorOccurrence connectorOccurrence = new KblConnectorOccurrence();
        connectorOccurrence.setXmlId("I1616");
        final KblTerminalOccurrence terminalOccurrence = new KblTerminalOccurrence();
        terminalOccurrence.setXmlId("id_4711");
        final KblTerminalOccurrence terminalOccurrence2 = new KblTerminalOccurrence();
        terminalOccurrence2.setXmlId("id_4712");

        harness.getConnectorOccurrences()
                .add(connectorOccurrence);
        harness.getTerminalOccurrences()
                .add(terminalOccurrence);
        harness.getTerminalOccurrences()
                .add(terminalOccurrence2);

        final KblContactPoint contactPoint = new KblContactPoint();
        connectorOccurrence.getContactPoints()
                .add(contactPoint);

        contactPoint.setId("SCHNUPSI");
        contactPoint.setXmlId("id_1234");
        contactPoint.getAssociatedParts()
                .add(terminalOccurrence);

        final KblContactPoint contactPoint2 = new KblContactPoint();
        connectorOccurrence.getContactPoints()
                .add(contactPoint2);

        contactPoint2.setId("SCHNUPSI");
        contactPoint2.setXmlId("id_1235");

        // Access to getter = Lazy Init to of List = EmptyList =
        // <AssociatedParts></AssociatedParts>
        contactPoint2.getAssociatedParts();

        final KblContactPoint contactPoint3 = new KblContactPoint();
        connectorOccurrence.getContactPoints()
                .add(contactPoint3);

        contactPoint3.setId("SCHNUPSI");
        contactPoint3.setXmlId("id_1236");

        final KblWriter kblWriter = new KblWriter();

        try (final FileOutputStream outputStream = new FileOutputStream(target)) {
            kblWriter.write(root, outputStream);
        }
    }
}
```

#### Generated KBL file

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kbl:KBL_container id="ID000" version_id="version_id0"
                   xmlns:kbl="http://www.prostep.org/Car_electric_container/KBL2.3/KBLSchema">
    <Harness id="I1397">
        <Connector_occurrence id="I1616">
            <Contact_points id="id_1234">
                <Id>SCHNUPSI</Id>
                <Associated_parts>id_4711</Associated_parts>
            </Contact_points>
            <Contact_points id="id_1235">
                <Id>SCHNUPSI</Id>
            </Contact_points>
            <Contact_points id="id_1236">
                <Id>SCHNUPSI</Id>
            </Contact_points>
        </Connector_occurrence>
        <Terminal_occurrence id="id_4711"/>
        <Terminal_occurrence id="id_4712"/>
    </Harness>
</kbl:KBL_container>
```

## Contributing

We appreciate if you like to contribute to our project! Please make sure to base your branch off of
our [develop branch](https://github.com/4Soft-de/kbl-model/tree/develop) and create your PR into that same branch. We
will reject any PRs not following that or if this is already worked on.

Please read our
detailed [Contribution Guidelines](https://github.com/4Soft-de/kbl-model/blob/develop/.github/CONTRIBUTING.md) for more
information, for example code style, formatter, etc.