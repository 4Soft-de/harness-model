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

For detailed information over a project, have a look at its own ReadMe.

## Contributing

We appreciate if you like to contribute to our project! Please make sure to base your branch off of
our [develop branch](https://github.com/4Soft-de/harness-model/tree/develop) and create your PR into that
same branch. We will reject any PRs not following that or if this is already worked on.

Please read our
detailed [Contribution Guidelines](https://github.com/4Soft-de/harness-model/blob/develop/.github/CONTRIBUTING.md)
for more information, for example code style, formatter, etc.

## Moving to Jakarta namespace 

With release 5.x we updated to jaxb 4.0 and moved exclusively to the jakarta namespace with no further for the old javax.xml.bind namespace.
As an implementation we chose the org.glassfish namespace since it "seems" to be the one that should be used. Indications for that can be found here
[eclipse-ee4j/jaxb-ri](https://github.com/eclipse-ee4j/jaxb-ri/blob/master/jaxb-ri/boms/bom/pom.xml).

For the XJC part we moved from [jaxb2-basics](https://github.com/highsource/jaxb2-basics) to [hisrc-higherjaxb](https://github.com/patrodyne/hisrc-higherjaxb) since the original one only supports jaxb <= 2.3 

In general we had some troubles moving to jaxb > 2.3. The situation is quite frankly an absolute mess to be honest. We tried to clean up the poms as much as possible to not pollute the dependency tree.

## Error while releasing with github action
Error:  gpg: no default secret key: No secret key
Error:  gpg: signing failed: No secret key

This error message can also mean, that the key is there but expired. To check for this replace the mvn call with "gpg --list-keys".