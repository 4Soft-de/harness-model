# Compatibility Library

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.foursoft.harness.compatibility/compatibility-parent/badge.svg)](https://mvnrepository.com/artifact/com.foursoft.harness.compatibility)

A library for converting different versions of e.g. a VEC.

## Download

Our builds are distributed to [Maven Central](https://mvnrepository.com/artifact/com.foursoft.harness.compatibility).

Latest
Version: [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.foursoft.harness.compatibility/compatibility-parent/badge.svg)](https://mvnrepository.com/artifact/com.foursoft.harness.compatibility)

**Make sure to replace the `VERSION` below with a real version as the one shown above!**

### Compatibility Core

#### Maven

```xml

<dependency>
    <groupId>com.foursoft.harness.compatibility</groupId>
    <artifactId>compatibility-core</artifactId>
    <version>VERSION</version>
</dependency>
```

#### Gradle

```groovy
implementation group: 'com.foursoft.harness.compatibility', name: 'compatibility-core', version: 'VERSION'
```

### Compatibility VEC 1.1.X to VEC 1.2.X

#### Maven

```xml

<dependency>
    <groupId>com.foursoft.harness.compatibility</groupId>
    <artifactId>compatibility-vec11to12</artifactId>
    <version>VERSION</version>
</dependency>
```

#### Gradle

```groovy
implementation group: 'com.foursoft.harness.compatibility', name: 'compatibility-vec11to12', version: 'VERSION'
```

### Compatibility VEC 1.2.X to VEC 2.0.X

#### Maven

```xml

<dependency>
    <groupId>com.foursoft.harness.compatibility</groupId>
    <artifactId>compatibility-vec12to20</artifactId>
    <version>VERSION</version>
</dependency>
```

#### Gradle

```groovy
implementation group: 'com.foursoft.harness.compatibility', name: 'compatibility-vec12to20', version: 'VERSION'
```

## Coverage of the VEC versions

[docs/vec-release-notes-coverage.md](docs/vec-release-notes-coverage.md) matches every issue of the VEC
release notes from 1.2.0 to 2.2.0 against its handling in this library, and lists what a conversion cannot
carry over and the known open gaps.

## Contributing

See [parent ReadMe](https://github.com/4Soft-de/harness-model/blob/develop/README.md#contributing).

```