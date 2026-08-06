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
| `then` | continue with the next step, overloaded on its kind |
| `filter` | keep only elements matching a predicate |
| `ofType` | narrow the navigation to a sub type |
| `collect` | reduce many results to at most one, with given rules |
| `atMostOne` | the most common reduction: expect a single result |
| `asMulti` / `asList` | convert between result shapes |

`then` is **overloaded on the kind of the following step** rather than split into `then`/`thenEach`
variants: the argument's type already says whether the next step is single- or multi-valued, so a
suffix would only restate it. The result stays single-valued exactly when both steps are:

| | `.then(single)` | `.then(multi)` |
| --- | --- | --- |
| `single` | single | multi |
| `multi` | multi | multi |

`Navigations` is the factory that lifts ordinary model getters into navigations (`optional`,
`nullable`, `stream`, `collection`). It is the intended entry point of a chain: a plain getter
becomes a navigation, and operators take it from there.

One consequence of the overload is worth knowing: since both kinds are functional interfaces, an
*implicitly* typed lambda (`x -> …`) is ambiguous as an argument to `then`. Lift it with `Navigations`
— the intended form anyway — or use an exact method reference or an explicitly typed lambda, all of
which resolve. `SingleNavigationTest` pins these forms.

### Reductions

Going from many elements to one is not always "expect a single result". Which of several localized
strings is *the* description, for instance, follows rules over the strings as a whole — a lone untyped
string counts regardless of its language, but among several the typed ones are ignored. No sequence of
filters and mappings expresses that.

`collect` is therefore the general `Multi → Single` operator, taking a reduction as a
`java.util.stream.Collector` that yields an `Optional`. `atMostOne()` is simply the most common one,
defined as `collect(StreamUtils.findOneOrNone())` rather than as a mechanism of its own. Domain
reductions are authored with `StreamUtils.reducing(…)`, which builds such a collector from a function
over the accumulated elements, and are grouped into their own catalogs — `LocalizedStrings` holds
reductions, not navigations:

```java
Descriptions.toDescriptions().collect(LocalizedStrings.valueIn(VecLanguageCode.DE))
```

Recognising a reduction matters for modelling, not just for tidiness. Before `collect` existed, the
only place for these rules was a pretend navigation *from the collection* — which forced `List<…>` to
be a source type, forced an identity lift to get back to the elements, and forced the step leading to
the descriptions to be a private list-valued navigation instead of a public `MultiNavigation`. All
three disappeared once the reduction was named as such. **If a navigation's source is a collection,
that is the signal: it is probably a reduction wearing a navigation's clothes.**

### Catalogs

Concrete navigations are grouped into **catalogs**: final classes with a private constructor and
static factory methods returning the navigation interfaces. A catalog contributes the *steps*; the
caller composes them into a path with the operators above. Reductions are grouped the same way, in
catalogs of their own.

### Naming the factories

**A navigation that is a pure structural step is named `toX()`; one that selects a particular result
keeps a noun phrase.** The dividing line is whether the result depends on anything beyond the model's
own structure:

| | Named | Because |
| --- | --- | --- |
| `Placements.toLocations()` | `to…` | follows an association |
| `PlaceableElementRoles.toOnWayPlacements()` | `to…` | association plus a narrowing along the type hierarchy |
| `ViewItems.toPlaceableElementRole()` | `to…` | association, reduced to the expected single result |
| `Descriptions.descriptionIn(DE)` | noun | a domain value picks the result |
| `Descriptions.germanDescription()` | noun | a preset of a selector, so it inherits its naming |
| `LocalizedStrings.valueIn(DE)` | noun | a reduction, not a navigation at all |

The `to` earns its place at the chain's start, where no operator precedes it, and keeps the whole chain
reading as one sentence — *"view items **to** placeable element role, then **to** on-way placements,
then **to** locations"*:

```java
ViewItems.toPlaceableElementRole()
        .then(PlaceableElementRoles.toOnWayPlacements())
        .then(Placements.toLocations())
```

Note that the sibling `predicates` catalog goes the other way: commit `07a8bd1a` deliberately stripped
the `is` prefix from every `VecPredicates` method, because `filter(…)` already supplies the verb. The
difference is that `is` was purely redundant with `Predicate`, whereas `to` states a direction that a
bare noun leaves the reader to infer. Selectors do not state a direction — they name a thing — which is
why they keep the predicates style.

### Catalog organisation

**A navigation belongs to the catalog of its source type `S`.** The catalog is named after that type
in plural with the `Vec` prefix dropped, and method names state only the *target*, never the source —
see [Naming the factories](#naming-the-factories) above. A `Has*` mixin drops the `Has` instead, which is
where `Descriptions` and `CustomProperties` come from; where that would collide with the catalog of the
element itself, the mixin catalog says whose it is — `HasSpecifications` becomes `SpecificationOwners`,
because `Specifications` is the catalog of `VecSpecification`.

Granularity is the **nearest model supertype** that has navigations, so a type shares a catalog with
its subtypes, and `Has*` mixin interfaces are legitimate source types with their own catalogs. A
catalog is split only once it grows unwieldy. This bounds the number of catalogs — the model has
several hundred classes — without making the lookup ambiguous.

That gives a mechanical answer to *"I am holding an `x`, where can I go from here?"*: the navigation
is in the catalog named after `x`'s type or one of its supertypes or mixins, and autocomplete on that
catalog enumerates everything reachable. Three consequences of the rule are worth stating explicitly:

- **The source never appears in a method name.** A catalog with a single source type does not need to
  disambiguate, so `parentDocumentVersion()` stays as it is instead of becoming
  `parentDocumentVersionOfOccurrence()`. Since the factories take no arguments, they cannot be
  overloaded, which means a catalog mixing source types is *forced* to encode the source in its method
  names — the legacy `PartOccurrenceOrUsageNavs` shows the result.
- **Where a family shares a target, the navigation belongs at the family level.** `Placements.toLocations()`
  starts at `VecPlacement` and resolves the subtype internally, rather than offering separate
  `onPointLocations()`/`onWayLocations()` entries that would reintroduce source-encoded names.
  `OccurrenceOrUsages` is the larger case: `toParentDocumentVersion()`, `toPrimaryPartType()` and
  `toPartOrUsageRelatedSpecifications()` each start at `VecOccurrenceOrUsage` and switch on the subtype,
  which is what retires the `OfOccurrence`/`OfUsage` suffixes of the legacy catalog. Such a switch covers
  every subtype the model has, so its `default` arm throws a `VecException` rather than returning an empty
  result: reaching it means a subtype was missed when the navigation was written, and an empty result would
  hide that as a legitimately empty path.
- **A mixin catalog is generic in its source.** `SpecificationOwners.toSpecifications()` is declared
  `<S extends HasSpecifications<VecSpecification>>`, so a chain starting at a `VecDocumentVersion` keeps
  that type — `SpecificationOwners.<VecDocumentVersion>toSpecifications().ofType(…)` — instead of widening
  to the mixin and forcing every catalog downstream to widen too. Without the type parameter, `DocumentVersions`
  could not build on the mixin's steps and would have to duplicate them.

The rule deliberately optimises the "from" direction. *"Which navigations lead to a `VecLocation`?"* is
not answerable from the class layout, because a target is reachable from many sources while a
navigation has exactly one source; that direction is served by documentation and by search, not by
where the code lives.

### Catalogs hold steps, callers compose paths

**A catalog factory must not take a parameter that it merely forwards to one of the interface's own
operators.** If `Catalog.x(arg)` would be nothing but `Catalog.y().someOperator(arg)`, it does not
belong in the catalog: it adds API surface and an argument without adding meaning, and it forces the
reader inside-out. Composition belongs at the call site, where the chain reads left to right in the
order the model is traversed:

```java
// as a catalog method taking a navigation — nests, reads inside-out
ViewItems.locations(PlaceableElementRoles.toOnWayPlacements().then(Placements.toLocations()))

// composed by the caller — reads in traversal order, no extra API
ViewItems.toPlaceableElementRole()
        .then(PlaceableElementRoles.toOnWayPlacements())
        .then(Placements.toLocations())
```

Two kinds of parameter remain legitimate, because neither is expressible by chaining:

- **Domain values that feed the traversal logic**, such as the language in
  `Descriptions.descriptionIn(VecLanguageCode)` or the property type in
  `LocalizedStrings.typedValueBy(String, VecLanguageCode)`.
- **Nothing at all**: zero-argument named presets are vocabulary, not indirection, and are encouraged.
  `PlaceableElementRoles.toOnPointPlacements()` and `Descriptions.germanDescription()` are just
  `toPlacements().ofType(VecOnPointPlacement.class)` and `descriptionIn(DE)`, but they name a concept the
  domain actually has.

A navigation-typed parameter is only justified when the catalog wraps logic *around* the passed
navigation that the caller could not otherwise express — not when the body is pure composition.

The same applies **inside** a catalog. Where a navigation is just a sequence of getter steps and type
narrowings, it is composed from the operators rather than written as a stream pipeline, so that the
implementation reads like the path it describes — `ViewItems.toPlaceableElementRole()` is the reference
example. Raw stream code and hand-written lambdas stay appropriate for *leaf* steps, where there is
genuine logic and nothing to compose: `Placements.toLocations()` dispatches on the placement subtype, and
the body of a reduction such as `LocalizedStrings.valueIn(…)` weighs the elements against each other.
Forcing those through the operators would make them longer, not clearer.

### Deprecated model associations

The VEC standard evolves too and deprecates associations of its own. Where it supersedes one path
through the model with another, the navigation **follows the recommended path and falls back to the
deprecated one only where the recommended path leads nowhere**. Both generations of files therefore
navigate through the same catalog method, and no call site has to know which generation it is holding.

`ConfigurableElements.toVariantConfiguration()` is the reference case. VEC 2.X configures an element
through the `VecConfigurationConstraint`s referencing it and deprecates the element's own `ConfigInfo`
association for removal, so the navigation goes back to the constraints and forward to their
configuration first, and only an empty result sends it to the old association. Three properties of that
shape generalise:

- **The fallback keys on the result, not on a version flag.** Nothing tells a navigation which
  generation a file was written against, and mixed files exist. An empty result is the only signal
  available, and it is the right one: it means the recommended path genuinely holds no answer here.
- **A path against the reference direction needs back references.** The recommended path is usually the
  reverse of the association the deprecated one followed forwards, so it depends on the `navext`
  back-reference accessors and carries `@RequiresBackReferences` — along with everything built on it.
  Read without back references, such a navigation finds nothing and silently takes the fallback:
  correct, but degraded, which is what the annotation warns about.
- **The suppression stays in the catalog.** Using a deprecated getter needs
  `@SuppressWarnings({"deprecation", "removal"})`, and the catalog is where it belongs. It is then the
  single place in the repository touching the association, and consumers never need the suppression at
  all.

The direction is the mirror image of the repository's own deprecations, where the old API delegates to
the new one (see [API Evolution](api-evolution.md)). Here the new path falls back to the old, because
the deprecation is in the data rather than in this code: a consumer can migrate its calls, but nobody
can migrate the files that already exist.

### The legacy API (`navigations`)

The older generation uses catalogs too — final classes named `<Concept>Navs` with static factory
methods — but every factory returns a raw `java.util.function.Function`, and the catalogs follow no
single grouping rule. All of them are now deprecated for removal and delegate to the typed catalogs, so
what follows describes the surface that is being retired, not behaviour that still lives anywhere.
Consequences:

- A navigation has no type identity and cannot be distinguished from any other function.
- Result shapes are inconsistent across the catalogs: bare values, `Optional`, `List` and `Stream`
  all occur, and some navigations return `null` for an absent target.
- Composition is limited to `Function#andThen`, and a navigation taken as a parameter has to be
  typed as a concrete raw `Function`. This is not merely inelegant: it made
  `PlacementNavs.locationsOf` document a usage its own parameter type rejected.
- **Three competing grouping criteria coexist**, so there is no way to predict which catalog holds a
  given navigation. Most catalogs group by source type (`DocumentVersionNavs`, `ContentNavs`,
  `SegmentNavs`, `ConfigurableElementNavs`); some group by *target* concept and therefore mix unrelated
  source types (`PlacementNavs`, `DescriptionNavs`, `CustomPropertyNavs`); `SpecificationNavs` groups by
  types whose *name* ends in `Specification`; and `VecNavs` is a catch-all. The visible symptoms are
  `parentDocumentNumber()` existing in three catalogs, `parentDocumentVersion()` in three,
  `geometryNode2dBy`/`geometryNode3dBy` in two, and the `OfUsage`/`OfOccurrence` method-name suffixes
  in `PartOccurrenceOrUsageNavs`.

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
- **Catalogs are keyed on the source type, not the target.** A navigation has exactly one source but a
  target is reachable from many sources, so only the source yields a total, unambiguous assignment. It
  also matches the question developers actually ask at the call site, and it is what frees method names
  from having to name the source.
- **A distinct package name, not a singular/plural pair.** `traversal` was chosen over
  `navigation` precisely because the old package is called `navigations`; while both exist, a
  singular/plural pair would create permanent import ambiguity.
- **A deprecated model association is a fallback, not a second navigation.** Offering the recommended
  and the superseded path as two catalog methods would push the choice, and the knowledge of which VEC
  generation a file follows, to every call site — which is exactly what a navigation exists to absorb.
  One method with a fallback keeps the model's deprecation invisible to consumers until the model
  removes the association, at which point only the fallback is deleted.
- **Catalogs expose steps, not composed paths.** Composition happens at the call site, so a catalog
  factory never takes a parameter it would only forward to one of the interface's operators. The chained
  form reads in traversal order and needs no API surface; a wrapper method reads inside-out and hides
  nothing. The composition operators on the interface are what make this possible — without them, the
  wrapper method would be the only option, which is the position the legacy API is in.

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
├── traversal/                    ← typed catalogs, the same set as v2x
└── navigations/                  ← legacy catalogs (deprecated for removal)
```

Both packages are exported in each module's `module-info.java` and published to Maven Central, so
they are public API.

## Migration Status

| Module | Typed catalogs | Legacy catalogs |
| --- | --- | --- |
| `vec-common` | interfaces and factory | — |
| `vec-v2x` | 15 catalogs, see below | 10 `*Navs`, all deprecated and delegating |
| `vec-v12x` | the same 15 catalogs | 10 `*Navs`, all deprecated and delegating |
| `vec-v113` | — | none |

Every legacy navigation has a replacement, so both `navigations` packages are deprecated in full and
carry no logic of their own. The catalogs and their source types, identical in both modules:

| Catalog | Source type `S` |
| --- | --- |
| `ConfigurableElements` | `VecConfigurableElement` |
| `Contents` | `VecContent` |
| `CustomProperties` | `HasCustomProperties<VecCustomProperty>` |
| `Descriptions` | `HasDescription<? extends VecAbstractLocalizedString>` |
| `DocumentVersions` | `VecDocumentVersion` |
| `ExtendableElements` | `VecExtendableElement` |
| `LocalizedStringProperties` | `VecLocalizedStringProperty` |
| `OccurrenceOrUsages` | `VecOccurrenceOrUsage` (and its occurrence/usage subtypes) |
| `PlaceableElementRoles` | `VecPlaceableElementRole` |
| `Placements` | `VecPlacement` (and its on-point/on-way subtypes) |
| `Roles` | `VecRole` |
| `Specifications` | `VecSpecification` (and its subtypes) |
| `SpecificationOwners` | `HasSpecifications<VecSpecification>` |
| `TopologySegments` | `VecTopologySegment` |
| `ViewItems` | `HasOccurrenceOrUsages` |

Plus one reduction catalog: `LocalizedStrings`, over `VecAbstractLocalizedString`.

The mapping from old catalog to new is not one-to-one. Catalogs which grouped by target concept split
across several source types — `DescriptionNavs` became `Descriptions` plus the `LocalizedStrings`
reductions, `PlacementNavs` became `Placements`, `PlaceableElementRoles` and `ViewItems`, and
`CustomPropertyNavs` became `CustomProperties` plus `LocalizedStringProperties` — while the catch-all
`VecNavs` split into `ExtendableElements` and `Roles`. In the other direction, the `OfOccurrence` and
`OfUsage` pairs of `PartOccurrenceOrUsageNavs` collapsed into one family-level navigation each.

A few legacy signatures had no navigation shape at all and are deprecated without a one-to-one
replacement:

- `PartOccurrenceOrUsageNavs.occurrence()` / `usage()` are `ofType` on whatever navigation leads to the
  occurrences.
- `PartOccurrenceOrUsageNavs.findNodeOfComponent()` took two sources; it became
  `DocumentVersions.topologyNodeOf(VecOccurrenceOrUsage)`, a navigation from the document version with the
  component as a domain value.
- `CustomPropertyNavs.customPropertyOfType(…)` (v12x only) starts at a collection of properties, the
  signal of a navigation in the wrong shape; it is a filter and a narrowing at the call site.

`vec-v113` has no navigations of either generation.
