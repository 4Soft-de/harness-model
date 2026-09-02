# Architecture Documentation

Abstract documentation of the architectural concepts, patterns and decisions in this repository.
These documents describe *what* the parts do and *why* they are built that way; the module `README.md`
files cover usage and the code covers the details.

## VEC

- [VEC Navigation API](vec-navigation-api.md) — reusable, composable ways through the VEC model: the
  typed `Navigation` interface hierarchy, the per-version catalogs, and the legacy `*Navs` generation.
- [Open Enumerations](open-enumerations.md) — compile-safe literals for the enumerations the VEC
  leaves open: the generated interface and enum per type, how a literal the standard does not define
  is read and written, and how consumers contribute literals of their own.

## Cross-cutting

- [API Evolution](api-evolution.md) — how public API is replaced without breaking downstream
  consumers: version-suffixed modules, the deprecate-and-delegate pattern for superseded concepts, and
  how deprecations in the models themselves differ from it.

## Module-level documents

Some modules keep their own in-depth architecture document next to the code:

- [`kbl2vec/ARCHITECTURE.md`](../kbl2vec/ARCHITECTURE.md) — the two-phase KBL-to-VEC conversion.
