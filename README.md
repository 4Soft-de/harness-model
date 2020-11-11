# JAXB enhanced navigation
![Java CI with Maven](https://github.com/4Soft-de/jaxb-enhanced-navigation/workflows/Java%20CI%20with%20Maven/badge.svg?branch=develop) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=4Soft-de_jaxb-enhanced-navigation&metric=alert_status)](https://sonarcloud.io/dashboard?id=4Soft-de_jaxb-enhanced-navigation) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.foursoft.jaxb2/navigation-extender-runtime/badge.svg)](https://mvnrepository.com/artifact/com.foursoft.jaxb2)

A library for enhanced navigations for JAXB models. It consists of two parts: An xjc-plugin (compile time) and a runtime component.

## Key Features are:

- Typesafe Idref-Relations (also Idrefs). The type can be defined as an abstract type if required
- Typesafe Parent navigation (multiple parents are supported - at runtime of course only one can be set)
- Bi-directional typesafe navigations
- Id-Lookup Map as gimmick when loading an XML-File with this lib. The XML-Id is used as key.

The last point was the main driver for this project and has helped us a lot. Without it the customer project would not have been possible. 


## Details
The get a better understanding of this lib please have a look at our blog post about it based on a real project.
https://www.4soft.de/blog/2018/navigation-on-large-xml-structures/

### Extend files to be written with metadata
It is possible to extend the xml-file to write with comments or processing instructions
```java
final XMLWriter<Root> xmlWriter = new XMLWriter<>(Root.class);
final XMLMeta meta = new XMLMeta();

final Comments comments = new Comments();
meta.setComments(comments);
comments.put(root, "Comment");


final List<ProcessingInstruction> pis = new ArrayList<>();
pis.add(new ProcessingInstruction("pc", "checksum=\"12345\""));

final ProcessingInstructions processingInstructions = new ProcessingInstructions();
processingInstructions.put(root, pis);
meta.setProcessingInstructions(processingInstructions);

try (final ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream()) {
    xmlWriter.write(root, meta, byteOutputStream);
}
```
## Limitations
There is one major limitation of this project - it can only be used in read-only usecases! Non of the data structure are updated (parent, back-navigation, id-lookup, etc). 

## Download
Our builds are distributed to [Maven Central](https://mvnrepository.com/artifact/com.foursoft.jaxb2).

Latest Version: [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.foursoft.jaxb2/navigation-extender-runtime/badge.svg)](https://mvnrepository.com/artifact/com.foursoft.jaxb2)

**Make sure to replace the `VERSION` below with a real version as the one shown above!**

### Navigation Extender

#### Maven
```xml
<dependency>
    <groupId>com.foursoft.jaxb2</groupId>
    <artifactId>navigation-extender</artifactId>
    <version>VERSION</version>
</dependency>
```

#### Gradle
```groovy
compile group: 'com.foursoft.jaxb2', name: 'navigation-extender', version: 'VERSION'
```

### Navigation Extender Runtime

#### Maven
```xml
<dependency>
    <groupId>com.foursoft.jaxb2</groupId>
    <artifactId>navigation-extender-runtime</artifactId>
    <version>VERSION</version>
</dependency>
```

#### Gradle
```groovy
compile group: 'com.foursoft.jaxb2', name: 'navigation-extender-runtime', version: 'VERSION'
```

## Contributing
We appreciate if you like to contribute to our project! Please make sure to base your branch off of our [develop branch](https://github.com/4Soft-de/jaxb-enhanced-navigation/tree/develop) and create your PR into that same branch. We will reject any PRs not following that or if this is already worked on.

Please read our detailed [Contribution Guidelines](https://github.com/4Soft-de/jaxb-enhanced-navigation/blob/develop/.github/CONTRIBUTING.md) for more information, for example code style, formatter, etc.