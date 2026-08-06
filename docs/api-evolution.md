# API Evolution

## Overview

Every module in this repository is published to Maven Central and exports its packages via JPMS, so
essentially all of it is public API. At the same time, in-repo usage of that API is thin: several
public packages have no in-repo callers and no test coverage at all. A signature change can therefore
pass the whole build here and still break every downstream consumer.

This document records the patterns used to change API anyway.

## How It Works

### Version-suffixed modules

The primary coexistence mechanism operates at the artifact level, not the class level. Incompatible
generations of a model live side by side as separate modules with separate root packages and separate
JPMS module names — `vec-v113` / `vec-v12x` / `vec-v2x`, `kbl-v24` / `kbl-v25`. Old versions are kept
indefinitely rather than deprecated, because they model a different version of an external standard,
not an older idea of the same thing.

Bridging between generations is done by dedicated modules (`compatibility/*`) using dynamic-proxy
wrappers, and by dedicated converter modules (`kbl2vec`), never by compatibility shims inside the
model modules.

### New package, deprecate, delegate

When a *concept* rather than a model version is replaced, the replacement is introduced as a **new
package inside the same module**, and the old one is deprecated in place. The steps:

1. **Build the replacement under a clearly distinct package name.** Distinct means a different word,
   not a singular/plural or numeric variant of the old name, so that both can be imported without
   ambiguity for as long as they coexist.
2. **Mark the superseded classes and methods `@Deprecated(forRemoval = true)`**, each with a javadoc
   `@deprecated` line naming its concrete replacement. This is the established form in this
   repository; `since` is conventionally omitted.
3. **Rewrite the deprecated bodies to delegate to the replacement.** No logic is left behind, so
   there is a single source of truth and the two generations cannot drift apart while both exist.
   Deprecated signatures are preserved exactly, adapting shapes at the boundary where the new API
   uses a different result type.
4. **Add characterisation tests asserting old and new agree**, marked
   `@SuppressWarnings({"deprecation", "removal"})`. These are the safety net for the delegation
   rewrite and are deleted together with the deprecated classes.
5. **Port incrementally.** One catalog, class or subject area per commit rather than a big-bang
   rewrite, so each step is reviewable and the build stays green throughout.
6. **Remove in a future major release.**

The delegation direction is deliberately old → new. Making the deprecated class the implementation
and the new one a wrapper would keep the old semantics as the source of truth and prevent the new API
from fixing anything.

### Deprecations in the model are a separate axis

The VEC and KBL standards deprecate parts of their own models, which is unrelated to this repository
deprecating its API — a generated model class can be deprecated for removal by the standard while the
hand-written code using it is current. The two are handled in opposite directions. An API deprecation
delegates old to new, because the caller is the one who migrates. A model deprecation is absorbed by
preferring the recommended path and falling back to the deprecated one, because the files already
written cannot be migrated by anyone here. See
[Deprecated model associations](vec-navigation-api.md#deprecated-model-associations).

## Key Design Decisions

- **Tests accompany the new API, not the old one.** Because the legacy API is typically untested,
  writing tests for the replacement plus old-versus-new characterisation tests is what makes the
  delegation rewrite verifiable at all. This is the point at which coverage gets introduced.
- **Deprecation is scoped to what has actually been replaced.** Classes are deprecated as they are
  ported, not upfront, so consumers never get a deprecation warning without a replacement to move to.
- **Deprecation warnings are kept at zero outside the suppressed tests.** Any in-repo caller of a
  newly deprecated API is migrated in the same change, so the warnings that remain are meaningful.
- **`forRemoval = true` needs `@SuppressWarnings("removal")`.** `removal` is a separate javac lint
  category from `deprecation`; suppressing only the latter leaves the warnings in place. Both are
  needed on intentional uses of a deprecated-for-removal API.
- **Behaviour-preserving beats correct, in the deprecated path.** Where the replacement fixes a flaw,
  the deprecated method keeps its original observable behaviour. Consumers get the fix by migrating,
  not by upgrading.

## Relationships

- **[VEC Navigation API](vec-navigation-api.md)** — the first concept replaced with this pattern; the
  legacy `navigations` catalogs delegate to the typed `traversal` catalogs.
- **`compatibility/*` modules** — the cross-version counterpart of this pattern, bridging different
  model generations rather than different generations of a hand-written API.

## Notes

There is no formally documented deprecation policy or semantic-versioning statement in the
repository. Observed practice is that breaking changes land in major releases — version 5.x was the
Jakarta-namespace break — and that `.github/CONTRIBUTING.md` governs commit and code conventions but
says nothing about API compatibility.
