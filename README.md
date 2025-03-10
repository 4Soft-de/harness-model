# Harness Model

![Java CI with Maven](https://github.com/4Soft-de/harness-model/workflows/Java%20CI%20with%20Maven/badge.svg?branch=develop)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=4Soft-de_harness-model&metric=alert_status)](https://sonarcloud.io/dashboard?id=4Soft-de_harness-model)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
![Java17](https://img.shields.io/badge/java-17-blue)

## Repository Contents

This repository consists of 4 libraries:
- [KBL](https://github.com/4Soft-de/harness-model/tree/develop/kbl)
- [VEC](https://github.com/4Soft-de/harness-model/tree/develop/vec)
- [Navigation Extender](https://github.com/4Soft-de/harness-model/tree/develop/navext)
- [Compatibility](https://github.com/4Soft-de/harness-model/tree/develop/compatibility)
- [VEC RDF Tooling](https://github.com/4Soft-de/harness-model/tree/develop/vec-rdf)
- [KBL to VEC Converter (Prototype)](https://github.com/4Soft-de/harness-model/tree/develop/kbl2vec)
- [VEC Scripting Api (Experimental)](https://github.com/4Soft-de/harness-model/tree/develop/vec/vec-scripting)

For detailed information over a project, have a look at its own ReadMe.

## Contributing

We appreciate if you like to contribute to our project! Please make sure to base your branch off of
our [develop branch](https://github.com/4Soft-de/harness-model/tree/develop) and create your PR into that
same branch. We will reject any PRs not following that or if this is already worked on.

Please read our
detailed [Contribution Guidelines](https://github.com/4Soft-de/harness-model/blob/develop/.github/CONTRIBUTING.md)
for more information, for example code style, formatter, etc.

## Moving to Jakarta namespace 

With the release of version 5.x, we upgraded to JAXB 4.0 and transitioned exclusively to the Jakarta namespace, discontinuing support for the old javax.xml.bind namespace. 
As our implementation, we opted for the org.glassfish namespace since it appears to be the recommended choice. Indications for this decision can be found here [eclipse-ee4j/jaxb-ri](https://github.com/eclipse-ee4j/jaxb-ri/blob/master/jaxb-ri/boms/bom/pom.xml).

For the XJC part, we migrated from [jaxb2-basics](https://github.com/highsource/jaxb2-basics) to [hisrc-higherjaxb](https://github.com/patrodyne/hisrc-higherjaxb) as the original one only supports JAXB versions up to 2.3.

In general, we encountered some challenges while transitioning to JAXB versions beyond 2.3. To be honest, the situation is quite messy. We made efforts to clean up the POMs as much as possible to avoid polluting the dependency tree.

## Error while releasing with github action
Error:  gpg: no default secret key: No secret key
Error:  gpg: signing failed: No secret key

This error message can also mean, that the key is there but expired. To check for this replace the mvn call with "gpg --list-keys".
