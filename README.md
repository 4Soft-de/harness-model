# KBL model
JAXB-Model of the KBL, based on the underlying UML-model (not only the XSD).

## Introduction
KBL is German and stands for **K**abel**b**aum**l**iste (engl. Harness Description List) and defines an information model, a data dictionary, and
an XML schema derived from and compliant to the model.

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
- Generated AssertJ assertions in additional jar files to write fluent assertions on KBL objects.


## Download
Our builds are distributed to [Maven Central](https://mvnrepository.com/artifact/com.foursoft.kblmodel).

Latest Version: [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.foursoft.kblmodel/kbl-parent/badge.svg)](https://mvnrepository.com/artifact/com.foursoft.kblmodel)

**Make sure to replace the `VERSION` below with a real version as the one shown above!**

### Maven
```xml
<dependency>
    <groupId>com.foursoft.kblmodel</groupId>
    <artifactId>kbl24</artifactId>
    <version>VERSION</version>
</dependency>
```

and for the assertion library:

```xml
<dependency>
    <groupId>com.foursoft.kblmodel</groupId>
    <artifactId>kbl24-assertions</artifactId>
    <version>VERSION</version>
    <scope>test</scope>
</dependency>
```

### Gradle
```groovy
implementation group: 'com.foursoft.kblmodel', name: 'kbl24', version: 'VERSION'
```

```groovy
testCompile group: 'com.foursoft.kblmodel', name: 'kbl24-assertions', version: 'VERSION'
```

## Code examples

In the codebase, the root of a kbl file is the `KBLContainer` class.

More examples can be found [in the examples](https://github.com/4Soft-de/kbl-model/tree/develop/kbl24/src/examples/).

### Reading a KBL file
#### Example KBL file
```xml
<?xml version="1.0" encoding="UTF-8"?>
<kbl:KBL_container xmlns:kbl="http://www.prostep.org/Car_electric_container/KBL2.3/KBLSchema"
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:schemaLocation="http://www.prostep.org/Car_electric_container/KBL2.3/KBLSchema file:/C:/Users/ganss.SOFT/Projects/GIT/com.foursoft/kbl-model/kbl24/src/main/resources/kbl24/KBL24_SR1.xsd" id="ID000" version_id="version_id0">
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
    public void readKblFile(final String pathToFile) throws JAXBException, IOException {
        try (final InputStream is = MyKblReader.class.getResourceAsStream(pathToFile)) {
            final ExtendedUnmarshaller<KBLContainer, Identifiable> unmarshaller =
                    new ExtendedUnmarshaller<KBLContainer, Identifiable>(
                        KBLContainer.class).withBackReferences()
                        .withIdMapper(Identifiable.class, Identifiable::getXmlId);

            final JaxbModel<KBLContainer, Identifiable> model = unmarshaller
                    .unmarshall(new BufferedInputStream(is));

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
    public void writeKblFile(final String target) throws JAXBException, TransformerFactoryConfigurationError, IOException {
        final JAXBContext jc = JAXBContext.newInstance(KBLContainer.class);
        final KBLContainer root = new KBLContainer();
        final KblHarness harness = new KblHarness();

        root.setHarness(harness);

        final KblConnectorOccurrence connectorOccurrence = new KblConnectorOccurrence();
        final KblTerminalOccurrence terminalOccurrence = new KblTerminalOccurrence();
        terminalOccurrence.setXmlId("id_4711");
        final KblTerminalOccurrence terminalOccurrence2 = new KblTerminalOccurrence();
        terminalOccurrence2.setXmlId("id_4711");

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

        final Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        final StringWriter stringWriter = new StringWriter();

        marshaller.marshal(root, stringWriter);

        final String result = stringWriter.toString();

        final Path outPath = Paths.get(target).toAbsolutePath();
        if (Files.notExists(outPath))  {
            final Path parentFolder = outPath.getParent();
            if (parentFolder != null && Files.notExists(parentFolder)) {
                Files.createDirectory(parentFolder);
            }
            Files.createFile(outPath);
        }

        Files.write(outPath, result.getBytes(StandardCharsets.UTF_8));
    }
}
```

#### Generated KBL file
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<kbl:KBL_container xmlns:kbl="http://www.prostep.org/Car_electric_container/KBL2.3/KBLSchema">
    <Harness>
        <Connector_occurrence>
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
        <Terminal_occurrence id="id_4711"/>
    </Harness>
</kbl:KBL_container>
```
### Assertions on KBL Files
For each KBL version we provide an additional jar file with generated AssertJ assertions to write fluent assertions on VEC elements. The assertions are generated with the [AssertJ assertions generator](https://joel-costigliola.github.io/assertj/assertj-assertions-generator-maven-plugin.html). 

Below is a short example for the usage of these assertions in combination with native AssertJ-Assertions. For detailed information please refer to the original [AssertJ Documentation](https://assertj.github.io/doc/).

Please not the static imports of the [assertions entry point](https://joel-costigliola.github.io/assertj/assertj-core-custom-assertions.html) and the order of `...Assertions.assertThat;`.

```java
package com.foursoft.kblmodel.test;

import static com.foursoft.kblmodel.kbl24.assertions.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;

import org.junit.jupiter.api.Test;

import com.foursoft.kblmodel.kbl24.KblGeneralWire;
import com.foursoft.kblmodel.kbl24.KblSpecialWireOccurrence;
import com.foursoft.kblmodel.kbl24.KblUnit;

public class KblSampleTest {

	@Test
	public void testWireOccurrenceCreation() {
		//Find the element to test, maybe with a traversing visitor...
		final KblUnit unitMM = ... ;
		final KblGeneralWire wire = ...;
		final KblSpecialWireOccurrence specialWireOccurrence = ...;

		assertThat(specialWireOccurrence).hasSpecialWireId("1111")
				.hasPart(wire)
				.satisfies(w -> {
					assertThat(w.getLengthInformations()).hasSize(1)
							.satisfies(l -> {
								assertThat(l).hasLengthType("Production")
										.satisfies(v -> {
											assertThat(v.getLengthValue()).hasUnitComponent(unitMM)
													.hasValueComponent(0.0d);
										});
							}, atIndex(0));
				});
	}
}
```

## Contributing
We appreciate if you like to contribute to our project! Please make sure to base your branch off of our [develop branch](https://github.com/4Soft-de/kbl-model/tree/develop) and create your PR into that same branch. We will reject any PRs not following that or if the feature is already worked on.

Please read our detailed [Contribution Guidelines](https://github.com/4Soft-de/kbl-model/blob/develop/.github/CONTRIBUTING.md) for more information, for example code style, formatter, etc.