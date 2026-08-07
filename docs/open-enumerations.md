# Open Enumerations

## Overview

The VEC distinguishes two kinds of enumeration. A **closed** enumeration has a fixed set of literals.
An **open** enumeration defines literals that are recommended but not exhaustive: a sender may
legitimately use a literal of its own, and a receiver has to cope with it.

The schema expresses that distinction across the two files each VEC version ships. A closed
enumeration declares its `xs:enumeration` facets in both of them. An open enumeration declares them
only in `*-strict.xsd`, the schema used for validation; in the schema used for code generation the
same simple type is a bare `xs:string` restriction.

XJC therefore maps closed enumerations to Java enums and open enumerations to plain `String`, which
loses every literal the standard does define — no compile safety, no autocompletion, no `switch`, and
no way to tell a literal of the standard from a typo.

This document describes how that gap is closed.

## How It Works

For each open enumeration the build generates two types into the model package, next to the enums XJC
generates for the closed ones:

```java
public interface VecDocumentTypeLiteral extends OpenEnumLiteral {
    static VecDocumentTypeLiteral of(String value) { … }
    final class Custom implements VecDocumentTypeLiteral, CustomOpenEnumLiteral {
        public Custom(String value) { … }
        public String value() { … }   // plus equals, hashCode and toString
    }
}

public enum VecDocumentType implements VecDocumentTypeLiteral {
    BASELINE_DEFINITION("BaselineDefinition"), PART_MASTER("PartMaster"), … ;

    public String value();
    /** Returns null rather than throwing for a literal the standard does not define. */
    public static VecDocumentType fromValue(String value);
}
```

The interface is the type used in signatures; the enum holds the literals of the standard. The
mapped `String` property is left exactly as it was, and the typed accessors are added next to it:

```java
public String getDocumentType();                                   // unchanged
public void setDocumentType(String value);                         // unchanged
public VecDocumentTypeLiteral getDocumentTypeLiteral();            // added
public void setDocumentTypeLiteral(VecDocumentTypeLiteral value);  // added
```

Repeating properties get `getXLiterals()`, `addXLiteral(...)` and `setXLiterals(...)` instead, named
after the XSD element rather than the JAXB property.

### Reading

`null` means the property is unset, and nothing else. A literal that is not defined by the standard
comes back as a `Custom`, so all four cases are distinguishable in one `switch`:

```java
switch (documentVersion.getDocumentTypeLiteral()) {
    case VecDocumentType.PART_MASTER     -> master();
    case AcmeDocumentType.ACME_SPEC      -> acme();            // see below
    case VecDocumentTypeLiteral.Custom c -> unrecognized(c.value());
    case null                            -> unset();
    default                              -> other();
}
```

Unmarshalling cannot fail on an unrecognized literal, because nothing in the read path consults the
enum: the mapped property is still a string, and the literal is derived on access.

### Writing

```java
documentVersion.setDocumentTypeLiteral(VecDocumentType.PART_MASTER);
documentVersion.setDocumentTypeLiteral(new VecDocumentTypeLiteral.Custom("AcmeInternalType"));
documentVersion.setDocumentType("AcmeInternalType");        // still available
```

### Contributing literals of your own

The literal interfaces are not sealed, which is what makes an open enumeration open. Implement one
with an enum of your own, and register the constants so that reading a document resolves them too:

```java
public enum AcmeDocumentType implements VecDocumentTypeLiteral {
    ACME_SPEC("AcmeSpec");
    public String value() { … }
}

public class AcmeLiterals implements OpenEnumLiteralProvider {
    public Collection<? extends OpenEnumLiteral> literals() { return List.of(AcmeDocumentType.values()); }
}
// module-info.java:  provides OpenEnumLiteralProvider with AcmeLiterals;
// or class path:     META-INF/services/com.foursoft.harness.vec.common.openenum.OpenEnumLiteralProvider
```

`vec-scripting` does this for `ShortTermAgingTemperature`, a temperature type its wire data sheet
samples need and VEC 2.2.0 does not define — see `AdditionalTemperatureType` and
`ScriptingOpenEnumLiterals`.

Literals of the standard always win, so a provider cannot shadow them.

## Key Design Decisions

- **The mapped property stays a `String`, and the typed accessors are additive.** All 141 published
  properties of `vec-v2x` keep their signature, no in-repo consumer of the string form needs
  changing, and read robustness is structural rather than something error handling has to achieve.
- **`Literal`-suffixed accessors instead of overloads.** An overloaded `setDocumentType(literal)`
  next to `setDocumentType(String)` would make `setDocumentType(null)` an ambiguous method
  invocation and break callers that compile today.
- **An interface per open enumeration, not one shared marker.** A single marker would let a
  `SignalType` literal be passed where a `DocumentType` is expected. That is exactly the compile
  safety this feature exists for.
- **Not sealed, so `switch` needs a `default`.** Sealing would buy exhaustiveness at the price of
  consumer extension, which is the more valuable half for a standard that expects custom literals.
- **`fromValue` returns `null` instead of throwing.** The signature stays the one XJC generates for
  closed enumerations, so habits transfer; only the failure mode differs, in the safe direction.
- **The literal types carry no JAXB annotations.** They must not enter the JAXB context, which builds
  from the package's `ObjectFactory`; annotating them would only be misleading.
- **Constant name collisions fail the build.** `TerminalBoltNominalSize` mixes ISO metric with
  Unified Thread Standard sizes, where `#1` and `1` mangle to the same name. Names are resolved by
  the same algorithm XJC uses for closed enumerations, and a genuine collision is settled by a
  hand-maintained override file rather than an automatic suffix — a suffix would silently renumber
  published constants whenever a literal is inserted upstream.

## Trade-offs and Limits

- **Custom literals and strict validation are mutually exclusive.** A document using a literal beyond
  the standard does not validate against `*-strict.xsd`. That is what the strict schema is for, and a
  property of the VEC rather than of this API. `CustomLiteralValidationTest` pins both halves.
- **`getXLiterals()` is a snapshot, not the live list.** Unlike the JAXB list getters it cannot be
  mutated in place; use `addXLiteral` / `setXLiterals`. The proxy-coverage tests of `compatibility/*`
  exclude these getters from the live-list invariant for that reason.
- **Which properties are open enumerations differs between VEC versions.** A property may be a plain
  string in 1.1.3 and an open enumeration in 1.2.2, or may not exist at all. `WrapperProxyFactory`
  therefore leaves the typed accessors unintercepted, so their own implementation runs and derives
  the literals from the plain accessor, which is proxied as usual.

## Generation

The generation lives in the `-Xopen-enums` plugin in `navext/navext-xjc-plugin`, enabled last in the
`<args>` of the three VEC modules. It reads the literals from the strict schema, which is a build
input now and not only a validation resource. See `OpenEnumOptions` for the options, and
`vec_2.2.0-open-enum-names.xml` for the constant name overrides.

The plugin itself is model-agnostic: the runtime types it generates against are named by the
`-Xopen-enums-runtime` option, and live in `vec-common` for the VEC modules.

## Relationships

- **[API Evolution](api-evolution.md)** — the deprecate-and-delegate rules the `vec-scripting`
  migration follows. The seven hand-maintained enums there now implement the generated interfaces, so
  callers that have not migrated still compile.
- **[VEC Navigation API](vec-navigation-api.md)** — the other catalog of typed access on top of the
  generated model.
