# VEC Navigation API

## Overview

The VEC model is a large, generated JAXB object graph. Getting from one element to a related one
often takes several steps — filtering by type, following a collection, picking the single expected
element. The navigation API packages those recurring ways through the model as named, reusable,
composable objects instead of leaving them spelled out at every call site.

There are currently **two generations** of this API. The typed `traversal` API is the target state;
the older `navigations` packages remain in place for existing consumers. See
[API Evolution](api-evolution.md) for how the two coexist.

> **Naming caution:** "navigation" means two unrelated things in this repository. The `navext` XJC
> plugin generates *back-reference and parent accessors* into the model (`@XmlBackReference`,
> `@XmlParent`). The navigation API described here is a hand-written layer of *navigation functions*
> on top of the generated model. They are unrelated concepts that happen to share a word.

## How It Works

### The typed API (`traversal`)

The abstraction lives once in `vec-common`; the concrete navigations live per VEC version module.

A navigation is a function from a source element to a result, expressed through a small interface
hierarchy:

- `Navigation<S, T>` — the base type, extending `java.util.function.Function<S, T>`. `apply` remains
  the single abstract method, so lambdas work and a navigation can be handed to any API expecting a
  `Function` (`Stream#map`, `Stream#flatMap`, …). It mainly serves as a base type and as an escape
  hatch for result shapes the sub types do not cover.
- `SingleNavigation<S, T>` — a navigation to *at most one* element, so the result is an `Optional`.
  An absent target is part of the contract rather than a `null` return value.
- `MultiNavigation<S, T>` — a navigation to *any number* of elements, so the result is a `Stream`.
  A fresh stream is produced on every application, which keeps the navigation lazy and reusable.

Both sub types carry composition operators as default methods, so navigations are built by combining
smaller ones rather than by writing new traversal code:

| Operator | Meaning |
| --- | --- |
| `then` | continue with a single-valued navigation |
| `thenEach` | continue with a multi-valued navigation |
| `filter` | keep only elements matching a predicate |
| `ofType` | narrow the navigation to a sub type |
| `atMostOne` | reduce many results to at most one |
| `asMulti` / `asList` | convert between result shapes |

`Navigations` is the factory that lifts ordinary model getters into navigations (`optional`,
`nullable`, `stream`, `collection`). It is the intended entry point of a chain: a plain getter
becomes a navigation, and operators take it from there.

Concrete navigations are grouped into **catalogs** — one final class per subject area, named after
the plural of the domain concept (`Descriptions`, `Placements`), with a private constructor and
static factory methods returning the navigation interfaces. Because the factories return the
interface type and not a raw `Function`, a navigation can be *passed to* another navigation as a
typed parameter. That is what makes higher-order navigations expressible.

### The legacy API (`navigations`)

The older generation follows the same catalog idea — one final class per subject area, named
`<Concept>Navs`, with static factory methods — but every factory returns a raw
`java.util.function.Function`. Consequences:

- A navigation has no type identity and cannot be distinguished from any other function.
- Result shapes are inconsistent across the catalogs: bare values, `Optional`, `List` and `Stream`
  all occur, and some navigations return `null` for an absent target.
- Composition is limited to `Function#andThen`, and a navigation taken as a parameter has to be
  typed as a concrete raw `Function`. This is not merely inelegant: it made
  `PlacementNavs.locationsOf` document a usage its own parameter type rejected.

## Key Design Decisions

- **A dedicated interface instead of a raw `Function`.** The interface is what makes navigations a
  first-class concept: it carries the composition operators, gives the type a name, and lets a
  navigation be a typed parameter of another navigation. Extending `Function` keeps full
  interoperability with the streams and APIs that already exist.
- **`apply` stays the single abstract method.** A domain-named method such as `from` is added as a
  default rather than as a second abstract method, so the interfaces remain functional interfaces
  and lambda-implementable.
- **Result shape is part of the type.** `SingleNavigation` always yields `Optional`,
  `MultiNavigation` always yields `Stream`. Callers no longer need to know per navigation whether
  absence is signalled by `null`, an empty `Optional` or an empty collection.
- **Abstraction in `vec-common`, catalogs per version module.** The interfaces are
  version-independent and therefore shared, matching how the `Has*` mixin interfaces are already
  organised. The catalogs reference version-specific model classes and must stay per module.
- **A distinct package name, not a singular/plural pair.** `traversal` was chosen over
  `navigation` precisely because the old package is called `navigations`; while both exist, a
  singular/plural pair would create permanent import ambiguity.
- **Higher-order navigations take a navigation, not a placement.** Where a navigation needs a
  sub-path supplied by the caller, the parameter is a `MultiNavigation`/`SingleNavigation` describing
  the whole remaining way. This is more general than accepting an intermediate element type and it is
  what allows one navigation to serve several kinds of placement.

## Relationships

- **[API Evolution](api-evolution.md)** — how the legacy `navigations` catalogs are deprecated and
  delegated to their replacements while both generations coexist.
- **`Has*` mixin interfaces** (`vec-common`) — `HasDescription`, `HasCustomProperties`, `HasRoles`,
  `HasSpecifications`, … are the version-independent source types that navigations accept, which is
  what lets one navigation apply across many model classes.
- **`StreamUtils`** (`vec-common`) — supplies the traversal primitives the navigations and operators
  build on, notably the "expect at most one" collector used by `atMostOne`.
- **`predicates`** (per version module) — reusable `Predicate` factories, the sibling concept to
  navigations; used as arguments to `filter`.
- **`navext`** — generates the back-reference and parent accessors that navigations against the
  parent direction depend on. Navigations requiring them are annotated with `@RequiresBackReferences`,
  since they only work on a model read with back references enabled.
- **`visitor`** (per version module) — the alternative traversal mechanism, for walking the whole
  model rather than following a specific known path.

## File Locations

```
vec/vec-common/src/main/java/com/foursoft/harness/vec/common/
├── traversal/                    ← the abstraction (version-independent)
│   ├── Navigation.java
│   ├── SingleNavigation.java
│   ├── MultiNavigation.java
│   └── Navigations.java
├── util/StreamUtils.java         ← traversal primitives
├── annotations/RequiresBackReferences.java
└── Has*.java                     ← mixin source types

vec/vec-v2x/src/main/java/com/foursoft/harness/vec/v2x/
├── traversal/                    ← typed catalogs
└── navigations/                  ← legacy catalogs (deprecated for removal)

vec/vec-v12x/src/main/java/com/foursoft/harness/vec/v12x/
└── navigations/                  ← legacy catalogs, not yet ported
```

Both packages are exported in each module's `module-info.java` and published to Maven Central, so
they are public API.

## Migration Status

| Module | Typed catalogs | Legacy catalogs |
| --- | --- | --- |
| `vec-common` | interfaces and factory | — |
| `vec-v2x` | `Descriptions`, `Placements` | 10 `*Navs`, two of them deprecated and delegating |
| `vec-v12x` | — | 10 `*Navs` |
| `vec-v113` | — | none |

The remaining `vec-v2x` catalogs are ported one at a time; `vec-v12x` follows afterwards.
