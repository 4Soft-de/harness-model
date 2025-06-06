# VEC RDF

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.foursoft.harness.vec.rdf/vec-rdf-parent/badge.svg)](https://central.sonatype.com/search?q=com.foursoft.harness.vec.rdf)

Utilities to handle VEC data in RDF / Knowledge Graphs.

## Introduction

VEC stands for **V**ehicle **E**lectric **C**ontainer and defines an information model for engineering data in the
wiring harness development process. The _Harness Model_ GitHub repository
contains [libraries to read VEC XML data](../vec).

In 2024 the propstep ivip VES-WF published the VEC models as OWL2 ontology in addition to the XML Schemas.
More Information about VEC Ontologies can be found in
the [ECAD wiki](https://ecad-wiki.prostep.org/specifications/vec/guidelines/general/rdf-owl/).

## VEC RDF Features

The key feature of this library at the moment is the conversion of VEC XML data into an RDF representation. In addition
to that it contains some useful utilities and some experimental features.

### Conversion

> [!IMPORTANT]
>
> **Limitations:** In its current implementation state, the XML to RDF conversion is intended as first step into the
> direction
> of linked data and to offer a possibility to use VEC XML data in a semantic web stack.
>
> However, to use this data on a larger scale in a production environment some major adaptions are necessary. Foremost
> is the `IRI` generation strategy to mention. In the absence of anything better and as it is the easiest and most
> straight forward approach:
>
> **IRIs created by the converter are derived from the internal XML IDs**
>
> That means, IRIs generated by the converter are not stable over time and are only useful within a single dataset.
>
> If you are in need of better IRIs contact our colleague [Johannes Becker](mailto:becker@4soft.de?subject=VEC%20IRIs).

The main entry point into conversion is the `com.foursoft.harness.vec.rdf.convert.Vec2RdfConverter` with its `convert`
-Method.

```java
/**
 * Converts a VEC file (provided as {@link InputStream}) into an Apache Jena {@link Model}. The VEC Model version
 * is autodetected and handled accordingly. The content data is created in the given targetNamespace.
 * <p>
 * The result can then be serialized with the Apache Jena serialization functions (see
 * <a href="https://jena.apache.org/tutorials/rdf_api.html#ch-Writing-RDF">Apache Jena / Writing-RDF</a>).
 *
 * @param vecXmlFile      VEC XML data as {@link InputStream}
 * @param targetNamespace The namespace in which the data from the XML file is created.
 * @return The content data in an Apache Jena Model.
 */
 */
public Model convert(final InputStream vecXmlFile, final String targetNamespace) {...}
```

The actual usage is straight forward. This is a usage example from the test cases:

```java
Vec2RdfConverter converter = new Vec2RdfConverter();

InputStream inputFile = this.getClass().getResourceAsStream("/fixtures/oldbeetle/oldbeetle_vec113.vec");

Model model = converter.convert(inputFile, "https://www.acme.inc/oldbeetle");
```

### Common

The `vec-rdf-common` contains some useful utilities and meta data tools.

Noteworthy is the class `com.foursoft.harness.vec.rdf.common.VEC`.
It contains generated constants for all elements of the VEC ontology to use with the Apache Jena API, like that:

```java
public static final Property zoneCoverageSecondLocation = M_MODEL.createProperty(
        "http://www.prostep.org/ontologies/ecad/2024/03/vec#zoneCoverageSecondLocation");

/** <p>Abstract super-class for Localized text values.</p> */
public static final Resource AbstractLocalizedString = M_MODEL.createResource(
        "http://www.prostep.org/ontologies/ecad/2024/03/vec#AbstractLocalizedString");
```

### Changesets

The `vec-rdf-changes` contains an experimental implementation for change detection and exchange of delta / patch
information of VEC data, using the capabilities and features of RDF. This was part of a PoC conducted within the prostep
ivip groups VES-WF & ECAD-IF.

## Download

Our builds are distributed to Maven Central.

Latest
Version: [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.foursoft.harness.vec.rdf/vec-rdf-parent/badge.svg)](https://central.sonatype.com/search?q=com.foursoft.harness.vec.rdf)

Make sure to replace the VERSION below with a real version as the one shown above!

### VEC `XML` to `RDF` Converter

#### Maven

```xml

<dependency>
    <groupId>com.foursoft.harness.vec.rdf</groupId>
    <artifactId>vec-rdf-converter</artifactId>
    <version>${project.version}</version>
</dependency>
```

#### Gradle

```groovy
implementation group: 'com.foursoft.harness.vec.rdf', name: 'vec-rdf-converter', version: 'VERSION'
```