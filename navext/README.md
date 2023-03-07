# JAXB enhanced navigation

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.foursoft.harness.navext/navext-parent/badge.svg)](https://mvnrepository.com/artifact/com.foursoft.harness.navext)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
![Java11](https://img.shields.io/badge/java-11-blue)

A library for enhanced navigation methods for JAXB models.
It consists of two parts: An xjc-plugin (compile time) and a runtime component.

## Key Features are:

- Typesafe Idref-Relations (also Idrefs). The type can be defined as an abstract type if required.
- Typesafe Parent navigation (multiple parents are supported - at runtime of course only one can be set)
- Bidirectional typesafe navigation methods.
- Id-Lookup Map as gimmick when loading an XML-File with this lib. The XML-Id is used as key.

The last point was the main driver for this project and has helped us a lot. Without it the customer project would not
have been possible.

## Details

The get a better understanding of this lib please have a look at our blog post about it based on a real project.
https://www.4soft.de/en/blog/2018/navigation-on-large-xml-structures

### Extend files to be written with metadata

It is possible to extend the xml-file to write with comments or processing instructions.

```java
final Root root = new Root();
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
} catch (IOException ex) {
    ex.printStackTrace();
}
```

## Limitations

There is one major limitation of this project - it can only be used in read-only use-cases!
None of the data structure are updated (parent, back-navigation, id-lookup, etc).

## Download

Our builds are distributed to [Maven Central](https://mvnrepository.com/artifact/com.foursoft.harness.navext).

Latest
Version: [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.foursoft.harness.navext/navext-parent/badge.svg)](https://mvnrepository.com/artifact/com.foursoft.harness.navext)

**Make sure to replace the `VERSION` below with a real version as the one shown above!**

### Navigation Extender

#### Maven

```xml
<dependency>
    <groupId>com.foursoft.harness.navext</groupId>
    <artifactId>navext-xjc-plugin</artifactId>
    <version>VERSION</version>
</dependency>
```

#### Gradle

```groovy
implementation group: 'com.foursoft.harness.navext', name: 'navext-xjc-plugin', version: 'VERSION'
```

### Navigation Extender Runtime

#### Maven

```xml
<dependency>
    <groupId>com.foursoft.harness.navext</groupId>
    <artifactId>navext-runtime</artifactId>
    <version>VERSION</version>
</dependency>
```

#### Gradle

```groovy
implementation group: 'com.foursoft.harness.navext', name: 'navext-runtime', version: 'VERSION'
```

## Contributing

See [parent ReadMe](https://github.com/4Soft-de/harness-model/blob/develop/README.md#contributing).