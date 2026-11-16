# VEC release notes 1.2.0 - 2.2.0: coverage by the compatibility library

Every issue of the official VEC release notes (https://ecad-wiki.prostep.org/specifications/vec/) from
1.2.0 to 2.2.0, matched against the model change in the schemas (`vec_1.1.3.xsd`, `vec_1.2.2.xsd`,
`vec_2.2.0.xsd`) and against the code of the compatibility library that handles it. State of 2026-11-16.

## Issue counts

| Version | Issues in vec-release-notes.md | Rows written below |
|---|---|---|
| 1.2.0 | 106 | 106 |
| 1.2.2 (incl. 1.2.1: KBLFRM-1128) | 2 | 2 |
| 2.0.0 | 66 | 66 |
| 2.0.1 | 9 | 9 |
| 2.0.2 | 5 | 5 |
| 2.1.0 | 41 | 41 |
| 2.2.0 | 38 | 38 |
| **Total** | **267** | **267** |

KBLFRM-1128 appears twice (1.2.2 page and 2.0.0 page), and KBLFRM-1104 is listed in 2.0.0 but delivered in
2.2.0 as #969. Each is counted once per page, as in the input.

## How to read this

**Directions.** "Down" = DOWNGRADE 1.2.2 -> 1.1.3 (VEC 1.2 objects read through the VEC 1.1.3 API), the
productive direction for 1.2.x issues. "Up" = UPGRADE 1.2.2 -> 2.2.0 (VEC 1.2 objects read through the VEC
2.x API), the important direction for 2.x issues. The other direction is mentioned only where it needs
something.

**Rules** of the core, applied to every property: R1 pass-through, R2 cardinality adaptation, R3 scalar conversion, R4
class without counterpart left out, R5 PropertyAddition, R6 property only in the source dropped, R7 closed
enum mapped by constant name (missing constant -> null).

**Status.** "covered" = handling exists and is correct (for doc-only issues: nothing to handle).
"covered, lossy" = handled, but the target cannot hold the data. "covered by test" = a test asserts the
conversion. "GAP" = missing or wrong handling, with the runtime effect.

**Path abbreviations** (all under `compatibility/`):

| Abbreviation | Path |
|---|---|
| M11 | `compatibility-vec11to12/src/main/java/com/foursoft/harness/compatibility/vec11to12/Vec11XTo12XClassMapper.java` |
| M20 | `compatibility-vec12to20/src/main/java/com/foursoft/harness/compatibility/vec12to20/Vec12XTo20XClassMapper.java` |
| w12to11/ | `compatibility-vec11to12/src/main/java/com/foursoft/harness/compatibility/vec11to12/wrapper/vec12to11/` (downgrade wrappers) |
| w11to12/ | `compatibility-vec11to12/src/main/java/com/foursoft/harness/compatibility/vec11to12/wrapper/vec11to12/` |
| w12to20/ | `compatibility-vec12to20/src/main/java/com/foursoft/harness/compatibility/vec12to20/wrapper/vec12to20/` (upgrade wrappers) |
| w20to12/ | `compatibility-vec12to20/src/main/java/com/foursoft/harness/compatibility/vec12to20/wrapper/vec20to12/` |
| core/ | `compatibility-core/src/main/java/com/foursoft/harness/compatibility/core/` |
| T11/ | `compatibility-vec11to12/src/test/java/com/foursoft/harness/compatibility/vec11to12/` |
| T20/ | `compatibility-vec12to20/src/test/java/com/foursoft/harness/compatibility/vec12to20/` |

M11 and M20 entries refer to the `.register(...)` calls of the respective mapper.

**How the claims were verified.** The model changes were checked against structural diffs of the three
schemas and against the generated JAXB classes (closed vs. open enumerations, Java property types). The
handling was checked against the mappers and wrappers, and the generic part by the proxy coverage tests,
which call every getter and setter of every model class in both directions.

---

## VEC 1.2.0 (106 issues). Relevant direction: Down 1.2.2 -> 1.1.3

| Issue | Title (short) | Model change (verified) | Relevant direction | Handling | Where | Status |
|---|---|---|---|---|---|---|
| KBLFRM-967 | No official Schematron support | doc only (decision) | - | none needed | - | covered |
| KBLFRM-810 | ChangeDescription.ResponsibleDesigner optional | ChangeDescription.ResponsibleDesigner [1..1] -> [0..1] | Down | R1 pass-through. Nothing supplies the person that 1.1.3 requires. | no handling | **GAP**: if a 1.2 ChangeDescription has no ResponsibleDesigner, the 1.1.3 getter returns null and the required element is missing, so the output is schema-invalid. |
| KBLFRM-842 | PartWithSubComponentsRole.SubComponent optional | SubComponent IDREFS [1..1] -> [0..1] | Down | R1 pass-through | no handling | **GAP**: a role without sub components yields an empty list for the required 1.1.3 IDREFS, so the output is schema-invalid. |
| KBLFRM-843 | Multiplicity of InternalComponentConnection.pins | doc only (no XSD change; IDREFS in both) | - | none needed | - | covered |
| KBLFRM-875 | ContactPoint3D removed from description | doc only | - | none needed | - | covered |
| KBLFRM-877 | False mandatory attributes in TapeSpecification | Width/Thickness/CoilCoreDiameter [0..*] -> [0..1] | Down | Wrapper TapeSpecificationWrapper returns the single 1.2 value as a one-element list for getWidths/getThicknesses/getCoilCoreDiameters. Setters are kept in the proxy. | w12to11/specification/wireprotection/TapeSpecificationWrapper.java | covered (Up 1.1.3 -> 1.2: w11to12 TapeSpecificationWrapper takes the first value) |
| KBLFRM-915 | Path wrongly inherited from ConfigurableElement | Path base ConfigurableElement -> ExtendableElement; ConfigInfo removed from Path | Down | R5 value `configInfo` on v12x VecPath (returns null) | M11 | covered (1.2 cannot hold the value) |
| KBLFRM-947 | Docs of WireElement/WireGroupSpecification | doc only | - | none needed | - | covered |
| KBLFRM-472 | Topology elements <-> TopologyZones | Zone -> TopologyZone (+Type, +Assignment, -RequiredSealingClass); zones move from TopologySpecification.Zone to the new TopologyZoneSpecification; new ZoneAssignment/ZoneCoverage; BuildingBlockSpecification3D.Zone -> .TopologyZone | Down | Class map TopologyZone -> Zone (M11). TopologyZoneSpecification R4. TopologySpecification.getZones = empty list addition (M11). Zone parent/RequiredSealingClass additions (M11). BuildingBlockSpecification3DWrapper reads getTopologyZone for getZone. | w12to11/specification/BuildingBlockSpecification3DWrapper.java; M11, M11 | **GAP**: every zone is lost, because its only 1.1.3 container (TopologySpecification.Zone) is always empty. BuildingBlockSpecification3D.Zone still references the zone, which leaves a dangling IDREF and makes the 1.1.3 output schema-invalid. Zone type, assignments and sub-zone placement are lost. |
| KBLFRM-722 | Local geometry model of a component | new LocalGeometrySpecification, LocalPosition, MeasurePointPosition, PlacementPointPosition | Down | R4 (left out) | core/WrapperProxyFactory.java | covered, lossy (local geometry lost) |
| KBLFRM-818 | Diodes | new DiodeSpecification; PrimaryPartType (closed) +Diode. In 1.2 a diode is instantiated with the generic EEComponentRole. | Down | PartVersion: PartVersionWrapper maps Diode -> EEComponent; PartUsage.PrimaryPartUsageType gets the same replacement (PrimaryPartTypeMapping). DiodeSpecification R4. | w12to11/PartVersionWrapper.java | **GAP**: (a) EEComponentRole.EEComponentSpecification [1..1] pointing to a DiodeSpecification becomes null, so the output is schema-invalid. The diode's EEComponentSpecification content is lost, although it could be mapped to the superclass. PartVersion: covered, lossy. |
| KBLFRM-840 | Bolt terminals | new BoltTerminalSpecification/Role; PrimaryPartType +BoltTerminal; RingTerminalSpecification +BoltNominalSize (untyped). The inferred attribution is confirmed by the diff. | Down | PartVersionWrapper maps BoltTerminal -> Terminal. Spec and role R4. BoltNominalSize R6. | w12to11/PartVersionWrapper.java; T11/Vec12To11PartMasterConversionTest (primaryPartTypeUnknownToVec113BecomesTheGeneralOne, specificationsWithoutCounterpartAreLeftOut, onlyWhatVec113CannotExpressIsLeftOut) | Part master data: covered by test (lossy: BoltTerminalSpecification and BoltNominalSize dropped). **GAP** for harness data: (a) the BoltTerminalRole is dropped, so MatingPoint.First/SecondTerminalRole [1..1] becomes null when a bolt terminal is mated, which is schema-invalid. PartUsage.PrimaryPartUsageType: covered (Vec12To11RemodelledPropertiesTest). |
| KBLFRM-847 | Baselines | new BaselineSpecification (BaselineState, BaselineContent); DocumentType (open) +BaselineDefinition | Down | R4; literal R1 (1.1.3 DocumentType is a plain xs:string) | - | covered, lossy |
| KBLFRM-852 | Bending restrictions | new TopologyBendingRestriction(+Specification) | Down | R4 | - | covered, lossy |
| KBLFRM-863 | Supplementary parts in locations (Slots) | Slot +SupplementaryParts (IDREFS -> PartRelation); confirmed by the diff. Part of the refactored concept: ModularSlot/ExtensionSlot.AllowedInserts now reference PartRelations instead of PartVersions (same IDREFS in the XSD, see "XSD changes without a release-note issue"). | Down | Slot.SupplementaryParts R6. AllowedInsertsWrapper returns the accessory parts of the relations. | w12to11/AllowedInsertsWrapper.java | covered, lossy (slot supplementary parts and relation types lost) |
| KBLFRM-865 | Mapping wires <-> connected E/E components | new PinWireMappingSpecification/Point | Down | R4 | - | covered, lossy |
| KBLFRM-874 | Hierarchical VariantGroups | new VariantStructureSpecification/Node; VariantCode/VariantGroup +AliasId, +Abbreviation; VariantGroup.GroupType string -> open enum | Down | R4 / R6; GroupType R1 (String in both bindings) | - | covered, lossy |
| KBLFRM-880 | Grommet sealing with additional parts | new CableLeadThroughSpecification, CableLeadThroughReference; GrommetRole +CableLeadThroughReference; CableLeadThrough +CableLeadThroughSpecification | Down | R4 / R6 | - | covered, lossy |
| KBLFRM-883 | NURBS instead of BSplineCurve | BSplineCurve removed; Curve3D/NURBSCurve/NURBSControlPoint (Weight, CartesianPoint3D) added; GeometrySegment3D.BSplineCurve -> Curve | Down | Class map NURBSCurve -> BSplineCurve (M11). GeometrySegment3DWrapper builds getBSplineCurves from getCurves. BSplineCurveWrapper builds the control points from the CartesianPoint3D of each NURBSControlPoint. Degree R1. | w12to11/GeometrySegment3DWrapper.java; w12to11/BSplineCurveWrapper.java | covered, lossy (knots and weights dropped, so a rational or non-uniform NURBS becomes a B-spline over the same control polygon). No downgrade test; the upgrade has GeometrySegment3DWrapperTest. |
| KBLFRM-884 | Nested topologies | new TopologyMappingSpecification, NodeMapping, SegmentMapping | Down | R4 | - | covered, lossy |
| KBLFRM-894 | Advanced multi-cores in their usage | WireElement +SubWireElement; WireSpecification.WireElement [1..*] -> [1..1]; WireGrouping: RelatedWireRole -> RelatedWireElementReference, +ContainedWireGroupings, +ConnectionGroup, WireGroupSpecification [1..1] -> [0..1] (inferred attribution confirmed by the diff) | Down | WireSpecificationWrapper flattens the WireElement and all levels of SubWireElements into getWireElements. WireGrouping uses renamed(relatedWireRole, relatedWireElementReference). This rename is consistent: the library's 1.1.3 binding types RelatedWireRole as WireElementReference (vec_1.1.3.xjb). | w12to11/specification/WireSpecificationWrapper.java; M11 | Nesting: covered by test (Vec12To11RemodelledPropertiesTest). **GAP**: a WireGrouping without WireGroupSpecification misses the element 1.1.3 requires, so the output is schema-invalid. Lossy: nested ContainedWireGroupings disappear; ConnectionGroup is dropped. |
| KBLFRM-899 | Application constraints for other elements | new ApplicationConstraint(+Specification); ConfigurableElement +ApplicationConstraint | Down | R4 / R6 | - | covered, lossy |
| KBLFRM-918 | Conformance to requirements | new RequirementsConformanceSpecification/Statement | Down | R4 | T11/Vec12To11PartMasterConversionTest#onlyWhatVec113CannotExpressIsLeftOut, #specificationsWithoutCounterpartAreLeftOut | covered by test (lossy) |
| KBLFRM-925 | ComplexProperty | new ComplexProperty extends CustomProperty | Down | R4: left out of every CustomProperty list by the element filter | core/wrapper/ReturnValueAdapter.java | covered, lossy |
| KBLFRM-928 | Instantiation of topologies | TopologyNode +InstantiatedNode, TopologySegment +InstantiatedSegment, PartUsage +InstanciatedUsage (inferred attribution confirmed by the diff) | Down | R6 | - | covered, lossy |
| KBLFRM-930 | FIT rates | GeneralTechnicalPartSpecification +FitRate | Down | R6 | T11/Vec12To11PartMasterConversionTest#onlyWhatVec113CannotExpressIsLeftOut ("FitRate 1 -> 0") | covered by test (lossy) |
| KBLFRM-954 | XML schema pattern restrictions | technique (first use: CodingName, KBLFRM-942) | Down | R1 (pattern-restricted strings remain strings) | - | covered |
| KBLFRM-964 | Complex PartRelations with custom expression | PartRelation +CustomRelationExpression; PartRelationType (closed enum) +Custom | Down | Expression R6; RelationType R7 | core/wrapper/ReflectionBasedWrapper.java | **GAP**: RelationType=Custom has no 1.1.3 constant and becomes null (error logged). PartRelation.RelationType is [1..1] in 1.1.3, so the output is schema-invalid. The expression is lost. |
| KBLFRM-973 | Orthogonal grouping (AssignmentGroups) | new AssignmentGroup(+Specification), FunctionalAssignmentGroup, FunctionalRequirement, DocumentRelatedAssignmentGroup; ConfigurableElement +AssociatedAssignmentGroups | Down | R4 / R6 | - | covered, lossy |
| KBLFRM-974 | Ferrite | new FerriteSpecification/FerriteRole; PrimaryPartType +Ferrite | Down | PartVersionWrapper maps Ferrite -> Other. Spec and role R4. | w12to11/PartVersionWrapper.java | covered, lossy (PartUsage.PrimaryPartUsageType gets the same replacement (PrimaryPartTypeMapping); specification and role dropped). |
| KBLFRM-365 | Deprecation of CavityDesign | no structural change | Down | R1 | - | covered |
| KBLFRM-630 | OpenEnd node type and OpenWireEndTerminal | ComponentNodeType/NetworkNodeType (open) +OpenEnd; new OpenWireEndTerminalSpecification/Role; PrimaryPartType +OpenWireEnd | Down | Literals R1. PartVersionWrapper maps OpenWireEnd -> Terminal. Spec and role R4. | w12to11/PartVersionWrapper.java | covered, lossy (PartUsage.PrimaryPartUsageType gets the same replacement (PrimaryPartTypeMapping); specification and role dropped). |
| KBLFRM-643 | PartUsages in the BoM | no XSD change. PartStructureSpecification.InBillOfMaterial widens its target from PartOccurrence to OccurrenceOrUsage (binding). | Down | R2 element filter: PartUsage proxies are no VecPartOccurrence and are left out | core/wrapper/ReturnValueAdapter.java | covered, lossy (PartUsages disappear from the BoM) |
| KBLFRM-697 | Guideline partitioning/sizing | doc only | - | none needed | - | covered |
| KBLFRM-726 | Instantiation of integrated terminals | CavityReference +IntegratedTerminalRole | Down | R6 | - | covered, lossy |
| KBLFRM-732 | Docs of WireReception.angle | doc only | - | none needed | - | covered |
| KBLFRM-764 | Zero-length TopologySegments invalid | doc only (constraint) | - | none needed | - | covered |
| KBLFRM-785 | DocumentType HarnessCoupling | open literal | Down | R1 | - | covered |
| KBLFRM-788 | Open enums for RobustnessProperties | Class/ClassReferenceSystem string -> open enum (String in both bindings) | Down | R1 | - | covered |
| KBLFRM-793 | CompatibilityStatement/Specification removed | classes removed | Down | backRef `refCompatibilityStatement` on v12x AbstractSlot, Cavity, CavityPartSpecification, CavitySpecification, ConnectorHousingSpecification, TerminalSpecification, WireSpecification | M11 | covered (nothing to convert) |
| KBLFRM-794 | Open enum FuseType | string -> open enum | Down | R1 | - | covered |
| KBLFRM-796 | Docs of MultiContact | doc only | - | none needed | - | covered |
| KBLFRM-799 | "Power" split in PinComponentType | open literals +PowerSupply, +PowerDistribution, +NotConnected; -Power, -"Not connected" | Down | R1 string pass-through, no literal mapping | - | covered, lossy (valid, because 1.1.3 PinComponentType is a plain string, but "PowerSupply"/"PowerDistribution"/"NotConnected" are not mapped back to "Power"/"Not connected", which 1.1.3 consumers know) |
| KBLFRM-800 | DocumentTypes for symbols | open literals +SchematicSymbol, +NetworkSymbol | Down | R1 | - | covered |
| KBLFRM-804 | Plugging compatibility of HousingComponent | HousingComponent +CompatibleTypes | Down | R6 | - | covered, lossy |
| KBLFRM-811 | SealingClass/AbrasionResistanceClass -> RobustnessProperties | SealingClass and AbrasionResistanceClass removed from CavityPart-, ConnectorHousing-, Grommet-, TerminalSpecification and WireProtectionSpecification | Down | The robustness data itself is kept (GTPS.RobustnessProperties exists in 1.1.3, R1). Wrappers rebuild SealingClass and AbrasionResistanceClass from the RobustnessProperties of the GTPS in the same document. | w12to11/specification/AbstractSpecificationReflectionBasedWrapper.java; w12to11/specification/wireprotection/WireProtectionSpecificationWrapper.java | **GAP (wrong handling)**: entries are selected by `ClassKey` equalsIgnoreCase "SealingClass"/"AbrasionResistanceClass". In VEC 1.2 ClassKey is the level (A, B, C) and the influence is in `Class` (e.g. LiquidIngressProtection), so spec-conformant data yields empty lists. Where the convention does match, 1.1.3 AbrasionResistanceClass.ClassKey gets the literal "AbrasionResistanceClass" instead of a level. The first GTPS of a multi-part document is used for every specification. Zone.RequiredSealingClass is always empty (M11). Up 1.1.3 -> 1.2 does not convert SealingClass/AbrasionResistanceClass at all (lost). |
| KBLFRM-812 | Redefined NetGroup/ConnectionGroup | ConnectionGroup -NetGroup; ConnectionGroupType string -> open enum | Down | value `netGroup` on v12x ConnectionGroup (null); backRef `refConnectionGroup` on v12x NetGroup; type R1 | M11, M11 | covered |
| KBLFRM-816 | Typing of cavity add-ons | CavityAddOn +Type (WireAddOnType) [1..1]; new WireReceptionAddOn | Down | R6 / R4 | - | covered, lossy. Up 1.1.3 -> 1.2 needs something: value `type` on v113 CavityAddOn (M11) returns null for the mandatory 1.2 CavityAddOn.Type, so upgraded output is schema-invalid. |
| KBLFRM-817 | Taping with WireProtectionRole | same change set as KBLFRM-878 | Down | see KBLFRM-878 | see KBLFRM-878 | covered, lossy |
| KBLFRM-820 | Concise taping direction | TapingDirection: 1.1.3 open enum (Left, Right = rotation) -> 1.2 **closed** enum (FromStart, FromEnd = direction along the segment). Correction to the input: the type also changed from open to closed. | Down | TapeRole.TapingDirection is shadowed by value `tapingDirection` on v12x WireProtectionRole and returns null (deliberate: different semantics) | M11; rationale in w12to11/TapeRoleWrapper.java | covered, lossy (taping direction lost) |
| KBLFRM-824 | External references for all ExtendableElements | ExtendableElement +ReferencedExternalDocuments | Down | R6 | - | covered, lossy |
| KBLFRM-825 | StripeSpecification thickness | StripeSpecification +Thickness | Down | StripeSpecificationWrapper appends a NumericalValueProperty "Thickness" to CustomProperties | w12to11/specification/wireprotection/StripeSpecificationWrapper.java; T11/wrapper/vec12to11/specification/wireprotection/StripeSpecificationWrapperTest | covered by test |
| KBLFRM-826 | SubComponent vs. inBillOfMaterial | doc only | - | none needed | - | covered |
| KBLFRM-830 | Open enum WireLength.lengthType | string -> open enum | Down | R1 | - | covered |
| KBLFRM-832 | Partial specifications | doc only | - | none needed | - | covered |
| KBLFRM-834 | Open enum Color.ReferenceSystem | string -> open enum | Down | R1 | - | covered |
| KBLFRM-837 | Modification tracking of digital content | DocumentVersion +DigitalRepresentationIndex | Down | R6 | - | covered, lossy |
| KBLFRM-839 | DocumentType ComponentDrawing | open literal | Down | R1 | - | covered |
| KBLFRM-848 | Unused cores in multi-cores | WireElementReference +Unconnected | Down | R6 | - | covered, lossy |
| KBLFRM-856 | FileBasedInstruction fileName/location | FileBasedInstruction -Location (inferred, confirmed by the diff) | Down | value `location` on v12x FileBasedInstruction (null) | M11 | covered |
| KBLFRM-857 | DocumentType DeviationTable | open literal | Down | R1 | - | covered |
| KBLFRM-858 | Docs of segment length | doc only | - | none needed | - | covered |
| KBLFRM-859 | Geometry vs. topology | doc only | - | none needed | - | covered |
| KBLFRM-860 | Implicitly positioned elements | doc only | - | none needed | - | covered |
| KBLFRM-867 | Instantiation of model structures | doc only | - | none needed | - | covered |
| KBLFRM-878 | TapeRole introduced | new TapeRole (TapeOverlap as NumericalValue, TapeOverlapRelative, TapingDirection, Gradient, WindingType, WindingFirmness); WireProtectionRole -TapeOverlap (string), -TapingDirection, -Gradient | Down | Class map TapeRole -> WireProtectionRole (M11). TapeRoleWrapper passes Gradient. Values `tapeOverlap`/`tapingDirection` on v12x WireProtectionRole return null. The remaining TapeRole properties R6. | M11, M11; w12to11/TapeRoleWrapper.java | covered, lossy (TapeOverlap, TapingDirection, TapeOverlapRelative, WindingType, WindingFirmness lost). No downgrade test. |
| KBLFRM-888 | Semantics of PartRelation | doc only | - | none needed | - | covered |
| KBLFRM-889 | Data rate of Signal | Signal +DataRate | Down | R6 | - | covered, lossy |
| KBLFRM-890 | CurrentType of Signal | Signal +CurrentType | Down | R6 | - | covered, lossy |
| KBLFRM-891 | HV classes in NominalVoltage | open literals +HV1..HV5 | Down | R1 (open in 1.1.3) | - | covered |
| KBLFRM-892 | PinCurrentType ClippingCurrent | open literal | Down | R1 (open in 1.1.3) | - | covered |
| KBLFRM-896 | Traceability of directly mated E/E components | MatingPoint/MatingDetail +Connection | Down | R6 | - | covered, lossy |
| KBLFRM-897 | Bridge terminals | new BridgeTerminalSpecification/Role (no PrimaryPartType literal in 1.2) | Down | R4 | - | covered, lossy. A BridgeTerminalRole used in a MatingPoint makes First/SecondTerminalRole null (same GAP as KBLFRM-840). |
| KBLFRM-898 | Base inclusion of VariantConfigurations | VariantConfiguration +BaseInclusion | Down | R6 | - | covered, lossy |
| KBLFRM-900 | referenceElement moved to OccurrenceOrUsage | PartOccurrence -ReferenceElement, OccurrenceOrUsage +ReferenceElement | Down | PartOccurrence: R1, and the element filter drops PartUsage targets (the 1.1.3 list is typed PartOccurrence). PartUsage: R6. | core/wrapper/ReturnValueAdapter.java | covered, lossy |
| KBLFRM-901 | WireReceptionType literal | open +UltraSonicCompactation | Down | R1 | - | covered |
| KBLFRM-907 | WiringGroup <-> ConnectionGroup | WireGrouping/WireElementReference +ConnectionGroup | Down | R6 | - | covered, lossy |
| KBLFRM-908 | ContactPoints free of variance | doc only | - | none needed | - | covered |
| KBLFRM-912 | averageWireLengthAddOn | WireReceptionSpecification +AddOns (WireReceptionAddOn). The inferred attribution is confirmed: it is the only matching addition. | Down | R6 / R4 | - | covered, lossy |
| KBLFRM-913 | Impact protection as RobustnessClass | open literal ImpactProtection | Down | R1 | - | covered |
| KBLFRM-917 | Unspecified accessories permitted | GTPS +UnspecifiedAccessoryPermitted | Down | R6 | - | covered, lossy |
| KBLFRM-921 | Cavity/sealing geometries and sizes | CavitySpecification -CavityDiameter, +CavityDimension (Size), +Geometry; CavityPartSpecification -CavityDiameter (ValueRange), +CavityDimension [0..*], +CompatibleCavityGeometry; CavitySealSpecification +InsideDiameter | Down | CavitySpecificationWrapper with CavityDiameterWrapper: CavityDiameter = Height (otherwise Width) of CavityDimension. CavityPartSpecificationWrapper: CavityDiameter = ValueRange (min = max) from the first CavityDimension. Geometry, CompatibleCavityGeometry and InsideDiameter R6. | w12to11/specification/CavitySpecificationWrapper.java; w12to11/field/CavityDiameterWrapper.java; w12to11/specification/CavityPartSpecificationWrapper.java | covered, lossy (only the first dimension is used and its height is taken as the diameter; geometry enums and seal inside diameter lost; no downgrade test). Robustness: `Optional.of(vecSize.getHeight())` (line 91) throws an NPE for a Size without Height. That can only happen with schema-invalid input, since Size.Height is [1..1]. |
| KBLFRM-922 | ContactSystem -> TerminalPairing | ContactSystem(+Specification) replaced by TerminalPairing(+Specification) | Down | backRef `refContactSystem` on v12x ConductorSpecification and PartVersion; TerminalPairing R4 | M11; T11/Vec12To11PartMasterConversionTest#onlyWhatVec113CannotExpressIsLeftOut | covered by test (lossy: no TerminalPairing -> ContactSystem conversion) |
| KBLFRM-924 | Orientation of coordinate systems | doc only | - | none needed | - | covered |
| KBLFRM-929 | Multiplicity ViewItem2D/3D -> OccurrenceOrUsage | OccurrenceOrUsageViewItem2D/3D.OccurrenceOrUsage IDREF -> IDREFS | Down | 3D: OccurrenceOrUsageViewItem3DWrapper (first element). 2D: generic R2 (first element, warning if more). | w12to11/OccurrenceOrUsageViewItem3DWrapper.java; core/wrapper/ReturnValueAdapter.java | covered, lossy (only the first occurrence is kept) |
| KBLFRM-931 | Minimum content of part master documents | doc only | - | none needed | - | covered |
| KBLFRM-933 | Use of classes in the inheritance structure | doc only | - | none needed | - | covered |
| KBLFRM-934 | TAR/ZIP archive formats | doc only (packaging) | - | none needed | - | covered |
| KBLFRM-935 | SubType for NetworkNode/ComponentNode | ComponentNode/NetworkNode +SubType; NetworkNodeType (open) -Lamp, -Relay, -Fuse (+Ground, which is not in the issue) | Down | R6; NetworkNodeType R1 | - | covered, lossy |
| KBLFRM-937 | Guideline general vs. specific classes | doc only | - | none needed | - | covered |
| KBLFRM-939 | Default and special values | doc only | - | none needed | - | covered |
| KBLFRM-940 | Wildcards for part references | doc only | - | none needed | - | covered |
| KBLFRM-941 | Docs of RequirementsDescription | doc only | - | none needed | - | covered |
| KBLFRM-942 | Pattern restriction for coding names | Coding.Coding string -> CodingName | Down | R1 | - | covered |
| KBLFRM-943 | LevelOfApproval as open enum | Approval.LevelOfApproval string -> open enum; +AdditionalLevelInformation | Down | R1 / R6 | - | covered, lossy |
| KBLFRM-949 | Structuring of multi-cores | doc; model change belongs to KBLFRM-894 | Down | see KBLFRM-894 | - | covered (see KBLFRM-894 for the gap) |
| KBLFRM-956 | Chapter "General Guidelines" | doc only | - | none needed | - | covered |
| KBLFRM-957 | LocalizedTypedString.type open enum | string -> open enum | Down | R1 | - | covered |
| KBLFRM-959 | Typing of AliasIdentifications | AliasIdentification +Type | Down | R6 | - | covered, lossy |
| KBLFRM-962 | ConformanceClasses removed | ConformanceClass removed; VecContent -CompliantConformanceClass | Down | list `compliantConformanceClasses` on v12x Content (empty) | M11 | covered |
| KBLFRM-963 | Cardinality of PartOccurrence.RealizedPartUsage | IDREF -> IDREFS | Down | PartOccurrenceWrapper takes the first element; the setter value is kept in the proxy | w12to11/PartOccurrenceWrapper.java | covered, lossy (only the first usage is kept) |
| KBLFRM-968 | Model outline refactoring | doc only | - | none needed | - | covered |
| KBLFRM-975 | SignalSubType/medium literals, medium on NetType | SignalSubType and SignalTransmissionMediumType literals (open; MOST25/50/150 -> MOST); NetType +SignalTransmissionMediumType | Down | R1 / R6 | - | covered, lossy (NetType medium lost; literals not mapped back) |
| KBLFRM-982 | CableLeadThroughType literals | new open enum on the new CableLeadThroughSpecification | Down | R4 (with the specification) | - | covered, lossy |
| KBLFRM-990 | Open enum DefaultDimension.dimensionType | no XSD change in 1.2 (typed in 2.0.0 by KBLFRM-1015) | - | none needed | - | covered |
| KBLFRM-991 | Hierarchical functional grouping | new FunctionalStructureSpecification/Node | Down | R4 | - | covered, lossy |

## VEC 1.2.2 page (2 issues; KBLFRM-1128 = 1.2.1). Relevant direction: Down 1.2.2 -> 1.1.3

| Issue | Title (short) | Model change (verified) | Relevant direction | Handling | Where | Status |
|---|---|---|---|---|---|---|
| KBLFRM-1128 | TopologyZoneSpecification inherits Specification | base none -> Specification (1.2.0 -> 1.2.1). The v12x binding is 1.2.2. | Down | TopologyZoneSpecification R4 either way | - | covered (no effect; zone loss is the KBLFRM-472 gap) |
| KBLFRM-1146 | Recursive self-reference of ZoneAssignment | mandatory recursive ZoneAssignment element removed (1.2.0 -> 1.2.2; not visible in the 1.1.3 -> 1.2.2 diff) | - | none needed (no finite 1.2.0 document could contain a ZoneAssignment; ZoneAssignment R4 on downgrade) | - | covered |

## VEC 2.0.0 (66 issues). Relevant direction: Up 1.2.2 -> 2.2.0

Rows marked "open enum" are string -> OpenEnumeration changes. Both bindings type the property as String, so R1 passes it through. The typed `...Literal` accessors of 2.x are not intercepted and derive their value from that string (core/WrapperProxyFactory.java; T20/wrapper/vec12to20/OpenEnumerationWrapperTest asserts this for DocumentType).

| Issue | Title (short) | Model change (verified) | Relevant direction | Handling | Where | Status |
|---|---|---|---|---|---|---|
| KBLFRM-999 | ConnectorHousingRole <-> ComponentConnector | ConnectorHousingRole -ComponentNode, +ComponentConnector (IDREFS) | Up | ConnectorHousingRoleWrapper: ComponentConnector = all ComponentConnectors of the referenced ComponentNode; the setter value is kept in the proxy | w12to20/ConnectorHousingRoleWrapper.java; T20/wrapper/vec12to20/ConnectorHousingRoleWrapperTest; T20/Vec12To20ConversionTest#convertedDocumentKeepsAllElementsOfTheSource | covered by test (approximation: if a node has several connectors, each housing role gets all of them; ComponentNode dropped). The other direction is handled by w20to12/ConnectorHousingRoleWrapper. |
| KBLFRM-1054 | Redefined PartVersion.preferredUseCase | string [0..1] -> LocalizedString [0..*] | Up | PartVersionWrapper creates one LocalizedString with language DE | w12to20/PartVersionWrapper.java; T20/Vec12To20PartMasterConversionTest#preferredUseCaseBecomesAGermanLocalizedString; T20/wrapper/vec12to20/PartVersionWrapperTest | covered by test (assumes the 1.2 text is German) |
| KBLFRM-1017 | numberOfSheets as integer | string -> xs:integer | Up | DocumentVersionWrapper parses the text; a non-integer value is left out with a warning | w12to20/DocumentVersionWrapper.java; T20/Vec12To20PartMasterConversionTest#numberOfSheetsBecomesAnInteger; T20/wrapper/vec12to20/DocumentVersionWrapperTest | covered by test (lossy for non-numeric text) |
| KBLFRM-854 | Direction-aware placement of grommets | CableLeadThrough -PlacementPoint, +Outlet (new CableLeadThroughOutlet with PlacementPoint). The inferred change is confirmed by the diff. | Up | CableLeadThroughWrapper creates one outlet for the placement point (generated xs:ID, Identification "CableLeadThroughOutlet_" + placement point identification) | w12to20/CableLeadThroughWrapper.java | covered (no test: neither fixture contains a CableLeadThrough) |
| KBLFRM-1128 | TopologyZoneSpecification inherits Specification | no change between 1.2.2 and 2.2.0 | - | none needed | - | covered |
| KBLFRM-1107 | MatingPoint docs | doc only | - | none needed | - | covered |
| KBLFRM-1078 | FuseType documented as open enum | doc only | - | none needed | - | covered |
| KBLFRM-1071 | Docs Hole-/BoltMountedFixing | doc only | - | none needed | - | covered |
| KBLFRM-1151 | Docs ItemEquivalence | doc only | - | none needed | - | covered |
| KBLFRM-1149 | m:n traceability wiring <-> schematic | IDREF -> IDREFS: WireEnd.ConnectionEnd, WireElementReference.Connection/ConnectionGroup, WireGrouping.ConnectionGroup, TerminalRole.ComponentPort (all terminal roles), CavityReference.ComponentPort, EEComponentRole.ComponentNode, HousingComponentReference.ComponentConnector, MatingPoint/MatingDetail.Connection, BridgeTerminalRole.Connection (grouping confirmed by the diff) | Up | R2 (single value -> one-element list) | core/wrapper/ReturnValueAdapter.java; T20/Vec12To20CardinalityTest (every property listed) | covered by test. Down 2.x -> 1.2: first element (aListReadAsSingleValueYieldsItsFirstElement). |
| KBLFRM-1138 | CavityAddOn docs | doc only | - | none needed | - | covered |
| KBLFRM-1113 | Signal transmission media of wires | WireElementSpecification +TransmissionMediumType | Up | R5 value | M20 | covered |
| KBLFRM-1112 | Semantic versioning | VecVersion literals 2.0.0 ... 2.2.0 | Up | ContentWrapper reports "2.2.0" (VecVersion.VEC20X); the setter value is kept in the proxy | w12to20/ContentWrapper.java; T20/wrapper/vec12to20/ContentWrapperTest; T20/Vec12To20PartMasterConversionTest#settingTheVersionOfTheConvertedContentLeavesTheSourceUntouched | covered by test |
| KBLFRM-1104 | maximumVoltageCompliance | not in the 2.0.0 XSD; delivered in 2.2.0 as GTPS.VoltageLevelCompliance (NominalVoltage [0..*]) | Up | R5 list `voltageLevelCompliances` | M20 | covered |
| KBLFRM-1099 | ComponentNodeType PotentialDistributor | open literal (present in the 2.2 enum) | Up | R1 | - | covered |
| KBLFRM-1097 | Docs TopologyNode.matchingPointId | doc only | - | none needed | - | covered |
| KBLFRM-1095 | Docs positioning of accessories | doc only | - | none needed | - | covered |
| KBLFRM-1073 | Symmetric bolt properties | PotentialDistributorSpecification +BoltNominalSize; BoltType -> TerminalBoltType, BoltNominalSize -> TerminalBoltNominalSize (Bolt-/RingTerminal/PotentialDistributor) | Up | R5 value `boltNominalSize` on PotentialDistributorSpecification. BoltType R1. Untyped 1.2 BoltNominalSize (DOM node) -> String R3. | M20; T20/Vec12To20PartMasterConversionTest#untypedBoltNominalSizeBecomesAString | covered by test |
| KBLFRM-1047 | ProtectionLength redefined, MaterialLength | WireProtectionRole +MaterialLength (ProtectionMaterialLength) | Up | R5 list `materialLengths` (also on TapeRole) | M20, M20, M20 | covered |
| KBLFRM-1044 | Docs Transformation3D/2D | doc only | - | none needed | - | covered |
| KBLFRM-1022 | RobustnessClass AbrasionResistance | open literal | Up | R1 | - | covered |
| KBLFRM-1020 | Same physical realization, several logical connections | covered by the IDREF -> IDREFS changes of KBLFRM-1149 (inferred, consistent with the diff) | Up | R2 | T20/Vec12To20CardinalityTest | covered by test |
| KBLFRM-1018 | Docs isPreferredPart | doc only | - | none needed | - | covered |
| KBLFRM-1009 | Identification of BuildingBlockPositioning | BuildingBlockPositioning2D/3D +Identification | Up | R5 value | M20 | covered |
| KBLFRM-1008 | EffectivityControlKey ranges | ApplicationConstraint and UsageConstraint +From/ToEffectivityControlKey | Up | R5 values on v12x ApplicationConstraint and v12x UsageConstraint (1.2 source classes) | M20, M20 | covered |
| KBLFRM-998 | LocalizedTypedStringType literals | open +MainDescription, +AdditionalDescription | Up | R1 | - | covered |
| KBLFRM-997 | Supplementary parts for a SlotOccurrence | SlotReference +UsedSupplementaryParts | Up | R5 list | M20 | covered |
| KBLFRM-1131 | SheetOrChapter.sheetSize | SheetOrChapter +SheetSize | Up | R5 value | M20 | covered |
| KBLFRM-1127 | SegmentConnectionPoints of E/E components | HousingComponent +SegmentConnectionPoint (SegmentConnectionPointHC); HousingComponent and PinComponent base ExtendableElement -> ConfigurableElement | Up | R5 configurableElementPropertiesPlus plus list `segmentConnectionPoints` (HousingComponent) and the same for PinComponent; backRef `refSegmentConnectionPointHC` on PlacementPoint/SegmentConnectionPoint | M20, M20, M20 | covered |
| KBLFRM-1106 | Variant conditions (ConfigurationConstraint) | new ConfigurationConstraint; ConfigurableElement +ConfigurationConstraint (ApplicationConstraint/ConfigInfo deprecated) | Up | R5 list `configurationConstraints` and backRefs on ConfigurableElement; deprecated properties R1 | M20 | covered |
| KBLFRM-1105 | Improved tube attributes | no XSD change (TubeSpecification unchanged; confirmed) | - | none needed | - | covered |
| KBLFRM-1101 | Detailed terminal dimensions | TerminalSpecification +ConnectionALength, +OverallLength; WireReceptionSpecification +7 lengths; TerminalReceptionSpecification +ContactRangeLength | Up | R5 values | M20, M20, M20 | covered |
| KBLFRM-1098 | SupplementaryParts at PlacementPoint | PlacementPoint +SupplementaryParts; PlacementPointReference +UsedSupplementaryParts | Up | R5 lists | M20, M20 | covered |
| KBLFRM-1081 | GrommetSpecification.MountingType | +MountingType (GrommetMountingType) | Up | R5 value | M20 | covered |
| KBLFRM-1075 | BoltMountedFixingSpecification.boltShape | +BoltShape | Up | R5 value | M20 | covered |
| KBLFRM-1066 | Labeling of components and occurrences | new LabelingSpecification/Role; WireElementReference +LabelValue, +LabelType, +LabelPosition, +LabelingTechnology | Up | R5 values on WireElementReference; new classes need nothing | M20 | covered |
| KBLFRM-1065 | InsulationSpecification labeling technology | +LabelingTechnology, +AllowedLabelingTechnology | Up | R5 value/list | M20 | covered |
| KBLFRM-1055 | Additional geometric cavity parameters | new CavityPositionDetail; CavitySpecification +CavitySealingLength; CavityGeometry (open) +Octagon (confirmed in the 2.2 enum) | Up | R5 value `cavitySealingLength`; Cavity `positionDetail`; literal R1 | M20, M20 | covered |
| KBLFRM-1049 | Twisted-pair untwist | new WireTupleSpecification, WireTupleTermination(+Specification); Signal +WireTupleRequirements | Up | R5 value on Signal; backRef `refWireTupleTermination` on WireEnd | M20, M20 | covered |
| KBLFRM-1041 | Insertion direction/length for cavities | CavityPositionDetail.InsertionPosition/InsertionVector (inferred, confirmed: on the new class CavityPositionDetail) | Up | new class; Cavity `positionDetail` R5 | M20 | covered |
| KBLFRM-1033 | Geometric properties of cavities | Cavity +PositionDetail (CavityPositionDetail: Rotation, CavitySealingOffset, BaseUnit, ...) (inferred, confirmed) | Up | R5 value | M20 | covered |
| KBLFRM-1023 | View definitions for Net-/ConnectionSpecifications | new ConnectionViewSpecification, NetViewSpecification and view items | Up | new classes. The back references they add on existing classes are R5 backRefs (ComponentNode, ComponentPort, NetworkNode, NetworkPort) and parents (CartesianPoint2D, Transformation2D). | M20, M20, M20, M20 | covered |
| KBLFRM-1061 | Open enum PowerConsumption.type | string -> PowerConsumptionType | Up | R1 (open enum) | - | covered |
| KBLFRM-1053 | Open enum PotentialDistributorSpecification.boltType | string -> TerminalBoltType | Up | R1 | - | covered |
| KBLFRM-1052 | Open enum RingTerminalSpecification.boltNominalSize | untyped (xs:anyType) -> TerminalBoltNominalSize | Up | R3 (DOM node -> String) | T20/Vec12To20PartMasterConversionTest#untypedBoltNominalSizeBecomesAString | covered by test |
| KBLFRM-1082 | Open enum RingTerminalSpecification.boltType | string -> TerminalBoltType | Up | R1 | - | covered |
| KBLFRM-1080 | Open enum GrommetSpecification.grommetType | string -> GrommetType | Up | R1 | - | covered |
| KBLFRM-1079 | Open enum FittingSpecification.form | string -> FittingForm | Up | R1 | - | covered |
| KBLFRM-1074 | Open enum BoltMountedFixingSpecification.boltType | string -> BoltMountedFixingBoltType | Up | R1 | - | covered |
| KBLFRM-1067 | Open enum MultiFuseSpecification.fuseType | string -> MultiFuseType | Up | R1 | - | covered |
| KBLFRM-1064 | Open enum InsulationSpecification.labelIdentificationType | string -> LabelType | Up | R1 | - | covered |
| KBLFRM-1063 | Open enum HoleMountedFixingSpecification.holeShape | string -> HoleMountedFixingHoleShape | Up | R1 | - | covered |
| KBLFRM-1062 | Open enum HoleMountedFixingSpecification.holeType | string -> HoleMountedFixingHoleType | Up | R1 | - | covered |
| KBLFRM-1035 | Open enum PartStructureSpecification.Content | string -> PartStructureContentType | Up | R1 | - | covered |
| KBLFRM-1016 | Open enum dataFormat | no XSD change (stays xs:string; confirmed) | - | none needed | - | covered |
| KBLFRM-1015 | Open enum DefaultDimension.dimensionType | string -> DefaultDimensionType | Up | R1 | T20/Vec12To20PartMasterConversionTest#singleValuesBecomeListsWithThatValue (asserts the type "Length") | covered by test |
| KBLFRM-1014 | Open enum ConnectionEnd.gender | string -> ConnectionEndGender | Up | R1 | - | covered |
| KBLFRM-1013 | Open enum BoltTerminalSpecification.boltType | string -> TerminalBoltType | Up | R1 | - | covered |
| KBLFRM-1012 | Open enum BoltTerminalSpecification.boltNominalSize | untyped -> TerminalBoltNominalSize | Up | R3 (DOM node -> String) | T20/Vec12To20PartMasterConversionTest#untypedBoltNominalSizeBecomesAString | covered by test |
| KBLFRM-1006 | Open enum ConductorSpecification.type | string -> ConductorType | Up | R1 | - | covered |
| KBLFRM-1005 | Open enum ConductorSpecification.structure | string -> ConductorStructure | Up | R1 | - | covered |
| KBLFRM-1004 | Open enum WireGroupSpecification.groupType | string -> WireGroupType | Up | R1 | - | covered |
| KBLFRM-1125 | Open enum VariantCode.codeType | string -> VariantCodeType | Up | R1 | - | covered |
| KBLFRM-1124 | Open enum TemperatureInformation.temperatureType | string -> TemperatureType | Up | R1 | - | covered |
| KBLFRM-1123 | Open enum SlotSpecification.layoutType | string -> SlotLayoutType | Up | R1 | - | covered |
| KBLFRM-1122 | Open enum ShieldSpecification.windingType | string -> FoilShieldWindingType | Up | R1 | - | covered |

## VEC 2.0.1 (9 issues). Relevant direction: Up 1.2.2 -> 2.2.0

| Issue | Title (short) | Model change (verified) | Relevant direction | Handling | Where | Status |
|---|---|---|---|---|---|---|
| KBLFRM-1165 | Missing type declarations and min/maxOccurs | RequirementsConformanceStatement.Satisfies untyped -> boolean; DefaultDimensionSpecification.DefaultDimension [1..1] -> [0..*]; PinWireMappingSpecification.PinWireMappingPoint [1..1] -> [0..*]; FerriteRole.NumberOfWindings, TopologySegment.Form [1..1] -> [0..1]; typed Identification/LabelValue/Fixation on properties or classes new since 1.2 (grouping confirmed) | Up | Satisfies: R3. DefaultDimension: renamed(defaultDimensions, defaultDimension). PinWireMappingPoint: renamed(pinWireMappingPoints, pinWireMappingPoint). Loosened cardinalities: R1. | M20, M20; T20/Vec12To20PartMasterConversionTest#untypedSatisfiesBecomesABoolean, #singleValuesBecomeListsWithThatValue; T20/Vec12To20CardinalityTest#renamedSingleValueBecomesTheOnlyElementOfTheList | covered by test. Down 2.x -> 1.2: renamed back (M20), first element only. |
| KBLFRM-1163 | Missing role names in associations | no structural XSD change | - | none needed | - | covered |
| KBLFRM-1162 | Stereotype documentation | doc only | - | none needed | - | covered |
| KBLFRM-1155 | DefaultDimensionType literals | open +SegmentLengths, +FixingDistances, +WireLengths (confirmed) | Up | R1 | - | covered |
| KBLFRM-1166 | Docs of WireTuple fixation | doc only (plus boolean typing on the new WireTuple classes, see KBLFRM-1165) | - | none needed | - | covered |
| KBLFRM-1164 | Automatic UML validations | tooling only | - | none needed | - | covered |
| KBLFRM-1160 | DocumentTypes for 3D catalogue parts | open +GeometryModelSimplified, +GeometryModelOccurrence, +GeometryTemplate, +GeometryParameters (inferred, confirmed in the 2.2 enum) | Up | R1 | - | covered |
| KBLFRM-1157 | Multiple values per RobustnessProperties class | no structural XSD change (confirmed) | - | none needed (R1) | - | covered |
| KBLFRM-988 | Open enum TerminalReception.Gender | string -> TerminalReceptionGender | Up | R1 | - | covered |

## VEC 2.0.2 (5 issues). Relevant direction: Up 1.2.2 -> 2.2.0

| Issue | Title (short) | Model change (verified) | Relevant direction | Handling | Where | Status |
|---|---|---|---|---|---|---|
| KBLFRM-1182 | ComponentNodeViewItem instead of ConnectionNodeViewItem | new ComponentNodeViewItem; ConnectionViewSpecification +PlacedNodeViewItems (all inside the new 2.x view classes) | Up | new classes. backRefs `refComponentNodeViewItem`/`refConnectionNodeViewItem` on ComponentNode; Transformation2D parents R5. | M20, M20 | covered |
| KBLFRM-1143 | Deprecation information in schema | annotations only | - | none needed | - | covered |
| KBLFRM-1167 | External configuration constraints | new ConfigurationConstraintSpecification; ConfigurationConstraint +ConstrainedElements (both new classes relative to 1.2) | Up | backRef `refConfigurationConstraint` on ConfigurableElement, VariantConfiguration, ApplicationConstraint | M20, M20, M20 | covered |
| KBLFRM-1178 | Order/uniqueness meta information | xs:appinfo only | - | none needed | - | covered |
| KBLFRM-1179 | Model meta information as xs:appinfo | xs:appinfo only | - | none needed | - | covered |

## VEC 2.1.0 (41 issues). Relevant direction: Up 1.2.2 -> 2.2.0

| Issue | Title (short) | Model change (verified) | Relevant direction | Handling | Where | Status |
|---|---|---|---|---|---|---|
| KBLFRM-1192 | NumberOfStrands documented as Integer | doc only (reverted by #1071) | - | none needed | - | covered |
| KBLFRM-1205 | X axis in illustration | doc only | - | none needed | - | covered |
| KBLFRM-977 | SignalGraphs | new SignalGraph, -Edge, -Node, -Relation, -Specification | Up | new classes; backRef `refSignalGraphNode` on ConfigurableElement | M20 | covered |
| KBLFRM-1048 | Connected WireProtections | new WireProtectionGroup(+Specification) | Up | backRef `refWireProtectionGroup` on WireProtectionRole | M20 | covered |
| KBLFRM-1084 | ResourceVersion (tools, machines) | new ResourceVersion; VecContent +ResourceVersion; PluggableTerminalSpecification +ExtractionTool; CrimpDetail tool references | Up | R5 lists `resourceVersions`, `extractionTool` | M20, M20 | covered |
| KBLFRM-1085 | CrimpDetails for WireReceptions | new CrimpDetail, CoreCrimpDetail, InsulationCrimpDetail; WireReceptionSpecification +CoreCrimpDetails | Up | R5 list `coreCrimpDetails`; backRefs `refCoreCrimpDetail`/`refInsulationCrimpDetail` | M20, M20, M20 | covered |
| KBLFRM-1152 | DocumentClassification and ConfidentialityLevel | DocumentVersion +DocumentClassification, +ConfidentialityLevel | Up | R5 list/value | M20 | covered |
| KBLFRM-1169 | UNECE codes in units | Unit +UnEceCode | Up | R5 value | M20 | covered |
| KBLFRM-1177 | Crimp width and height | CrimpDetail.Size (inferred, confirmed: Size [0..1] on the new CrimpDetail) | - | none needed (new class) | - | covered |
| KBLFRM-1186 | ConnectorHousingCover | new ConnectorHousingCoverSpecification/Role; PrimaryPartType (closed) +ConnectorHousingCover | Up | none needed | - | covered. Down 2.x -> 1.2 would need something: R7 makes the required PartVersion.PrimaryPartType null (see "Gaps"). |
| KBLFRM-1195 | MaterialComposition | GTPS +MaterialComposition | Up | R5 list | M20 | covered |
| KBLFRM-1199 | WireEnd stripping/cut-back length | WireEnd +StrippingLength, +CutBackLength | Up | R5 values | M20 | covered |
| KBLFRM-1200 | Crimp sizes in WireMountingDetail | +CoreCrimpSize, +InsulationCrimpSize | Up | R5 values | M20 | covered |
| KBLFRM-1202 | Parameters for automatic crimp processes | WireReceptionSpecification +ConductorCrimpShape, +ConductorCrimpLegHeight, +InsulationCrimpShape, +InsulationCrimpLegHeight, +SheetThickness, +WireTipProtrusion; WireMountingDetail +WireTipProtrusion (inferred, confirmed) | Up | R5 values | M20, M20 | covered |
| KBLFRM-1208 | Terminal coordinate system, WireReception.Rotation | WireReception +Rotation | Up | R5 value | M20 | covered |
| KBLFRM-1215 | immutable-global-iri on every complexType | attribute on every complexType | Up | DefaultWrapper registers the value property `immutableGlobalIri` for every 1.2 object (it is the default wrapper and the base of every upgrade wrapper) | w12to20/DefaultWrapper.java; T20/wrapper/vec12to20/DefaultWrapperTest | covered by test |
| KBLFRM-1021 | Hole terminals | new HoleTerminalSpecification/Role | Up | none needed | - | covered |
| KBLFRM-1058 | Minimum distance for terminals | TerminalSpecification +MinimumDistance (TerminalDistanceInformation) | Up | R5 list | M20 | covered |
| KBLFRM-1103 | EEComponentSpecification.voltageRating | +VoltageRating (deprecated again in 2.2.0) | Up | R5 value | M20 | covered |
| KBLFRM-1111 | Role hierarchy consistent with specifications | new role subclasses (FuseRole, RelayRole, ..., TubeRole, CavityPartRole, ...); CavityAccessory/Plug/SealRole base Role -> CavityPartRole | Up | none needed. A 1.2 role keeps its class, which is still concrete in 2.2 (no abstractness change between 1.2.2 and 2.2.0, checked). | - | covered (roles are not specialized, e.g. a fuse stays EEComponentRole). The 2.x -> 1.2 direction would need something: the new subclasses have no 1.2 counterpart, so the whole role is dropped (R4) instead of being mapped to its 1.2 superclass. |
| KBLFRM-1140 | WireLengthType Functional | open literal | Up | R1 | - | covered |
| KBLFRM-1147 | TPA and CPA | ConnectorHousingSpecification +ConnectorPositionAssuranceType, +LockingType; ConnectorHousingRole +CpaState, +LockingState; SlotReference +TpaState; SlotSpecification +SecondaryLockingType (grouping confirmed) | Up | R5 values | M20, M20, M20, M20 | covered |
| KBLFRM-1172 | AliasIdentificationTypes and AliasIds | open +DiagnosticAddress, +SoftwareAddress; ComponentPort, NetworkPort, PinComponentBehavior +AliasId | Up | R5 lists; literals R1 | M20, M20, M20 | covered |
| KBLFRM-1173 | GeneratingSystemName/-Version | doc only | - | none needed | - | covered |
| KBLFRM-1175 | TubeRoles with slits | new TubeRole, CorrugatedPipeRole, ShrinkableTubeRole; CorrugatedPipeSpecification +CutOnWave | Up | CorrugatedPipeSpecificationWrapper registers isCutOnWave/setCutOnWave | w12to20/CorrugatedPipeSpecificationWrapper.java | covered |
| KBLFRM-1176 | pullOffForce for crimps | InsulationCrimpDetail.PullOffForce (new class) | - | none needed | - | covered |
| KBLFRM-1181 | Spot and cross tapes | WindingType (open) +Spot, +Cross (confirmed); TapeRole +NumberOfTurns (inferred, confirmed) | Up | R1; R5 value `numberOfTurns` | M20 | covered |
| KBLFRM-1188 | More classes inherit ExtendableElement | about 40 classes base none -> ExtendableElement | Up | R5 extendableElementPropertiesPlus (referencedExternalDocuments, customProperties, refExternalMapping, refReusage) on every affected 1.2 class. The scan finds no 2.x getter left over. | M20 (e.g. 65, 85, 86, 101, 126, 136-138, 158, 160, 163, 168, 169, 175, 187, 192-200, 209, 214-227) | covered |
| KBLFRM-1193 | Tolerance boundaries | doc only | - | none needed | - | covered |
| KBLFRM-1194 | DocumentType ProcessingInstruction | open literal | Up | R1 | - | covered |
| KBLFRM-1196 | PinComponentType PowerDistributionLimited | open literal | Up | R1 | - | covered |
| KBLFRM-1197 | OpticalTissueDensity -> OpticalCoverage | ShieldSpecification +OpticalCoverage (double); OpticalTissueDensity deprecated | Up | R5 value `opticalCoverage` (null); OpticalTissueDensity R1 | M20 | covered (the old value stays in the deprecated property and is not converted) |
| KBLFRM-1198 | ArcDegree/-Minute/-Second | OtherUnitName (closed enum) +ArcDegree, +ArcMinute, +ArcSecond | Up | none needed | - | covered. The 2.x -> 1.2 direction would need something: R7 makes the required OtherUnit.OtherUnitName null, so the 1.2 output is schema-invalid. |
| KBLFRM-1203 | Docs ISO 6722 | doc only | - | none needed | - | covered |
| KBLFRM-1207 | DocumentType MatingInterface | open literal | Up | R1 | - | covered |
| KBLFRM-1209 | TestingPullOutForce | TerminalReceptionSpecification +TestingPullOutForce | Up | R5 value | M20 | covered |
| KBLFRM-1210 | MaximumInsertionForce | PluggableTerminalSpecification +MaximumInsertionForce | Up | R5 value | M20 | covered |
| KBLFRM-1211 | Ambiguous CavityMapping | Mapping +IdentificationA, +IdentificationB (inferred, confirmed) | Up | R5 values | M20 | covered |
| KBLFRM-1217 | Docs Transformation2D/3D | doc only | - | none needed | - | covered |
| KBLFRM-1221 | Casing of enum literals in docs | doc only | - | none needed | - | covered |
| KBLFRM-1226 | DocumentType SchematicProject | open literal | Up | R1 | - | covered |

## VEC 2.2.0 (38 issues). Relevant direction: Up 1.2.2 -> 2.2.0

| Issue | Title (short) | Model change (verified) | Relevant direction | Handling | Where | Status |
|---|---|---|---|---|---|---|
| #1123 | MultiDimensionalValue, FuseCharacteristics | new MultiDimensionalValue, DataPoint(Value), QuantityKind, FuseCharacteristic, MultiDimensionalValueProperty; FuseSpecification +Characteristic; VecContent +QuantityKind | Up | R5 value `characteristic`, list `quantityKinds`; backRef `refQuantityKind` on Unit | M20, M20, M20 | covered |
| #1113 | Quantities other than one piece | OccurrenceOrUsage +Quantity | Up | R5 value | M20 | covered |
| #1112 | Coordinate systems of fixings | FixingSpecification +MaximumWidthA/B (attribution inferred; it cannot be confirmed from the XSD) | Up | R5 values | M20 | covered |
| #1105 | TerminalProtection | new TerminalProtectionSpecification/Role; PrimaryPartType +TerminalProtection | Up | none needed (ConductorMaterial parent `parentTerminalProtectionSpecification` R5) | M20 | covered. Down 2.x -> 1.2: R7 makes PrimaryPartType null. |
| #1102 | StructuredPrimitive stereotype | visible effect (inferred, present in the diff): Tolerance base ExtendableElement -> none; Unit and SlotMapping base none -> ExtendableElement | Up | Unit/SlotMapping: extendableElementPropertiesPlus. Tolerance: its ExtendableElement properties are dropped (R6). | M20, M20 (other direction M20) | covered, lossy (custom properties and external references on a 1.2 Tolerance are lost) |
| #1087 | CableSeal and CableSealPlug | new CableSeal*/SealingPin*/CavityLayout/CableLeadThroughOutletReference; CableLeadThroughOutlet +SupplementaryParts; CableLeadThroughReference base -> ExtendableElement, +CableLeadThroughOutletReference, UsedPlugs/UsedSeals deprecated; MultiCavitySeal/Plug deprecated; PrimaryPartType +CableSeal, +CableSealPlug | Up | R5 list `cableLeadThroughOutletReferences` (with extendableElementPropertiesPlus); backRefs on Cavity (`refCableSealEntry`, `refSealingPin`) and WireElementReference (`refCableSealEntryReference`); deprecated parts R1 | M20, M20, M20 | covered. Down 2.x -> 1.2: R7 makes PrimaryPartType null. |
| #1081 | Information reuse (Reusage) | new Reusage(+Specification); DocumentType +PartMasterTemplate (confirmed) | Up | backRef `refReusage` on ExtendableElement (and in extendableElementPropertiesPlus); literal R1 | M20 | covered |
| #1066 | Coating of WireEnds | new CoatingSpecification/Role; PrimaryPartType +Coating | Up | none needed | - | covered. Down 2.x -> 1.2: R7 makes PrimaryPartType null, and a CoatingRole is dropped (R4). |
| #937 | ReferenceSurfaceDefinition | ConnectorHousingSpecification +ReferenceSurfaceDefinition | Up | R5 value | M20 | covered |
| #915 | Internal connectivity of E/E components | EEComponentSpecification +ComponentNode; HousingComponent +ComponentConnector; PinComponent +ComponentPort; **SwitchingState +SwitchedLogicalConnections IDREFS [1..1] (mandatory)**; FuseComponent and InternalComponentConnection deprecated | Up | R5 values `componentNode`, `componentConnector`, `componentPort`; list `switchedLogicalConnections`; backRefs on ComponentNode/-Port/-Connector/Connection | M20, M20, M20, M20, M20, M20 | **GAP**: SwitchedLogicalConnections is mandatory in 2.2.0, but the list addition always returns an empty list, so every upgraded SwitchingState is schema-invalid. The 1.2 SwitchedConnections (-> InternalComponentConnection) are kept (R1). Everything else: covered. |
| #1135 | PartSubstitutionSpecification for PartVersions | base Specification -> PartOrUsageRelatedSpecification; PartUsage.PartSubstitution deprecated | Up | R5 `specialPartType`, `describedPart`, backRef `refSpecificRole` | M20 | covered |
| #1131 | PartNumberType from KBL | PartVersion +PartNumberType | Up | R5 value | M20 | covered |
| #1129 | Mating cycles of TerminalPairing | +MatingCycles; ReferencedCoreSpecification [1..1] -> [0..1] | Up | R5 value; R1 | M20 | covered |
| #1128 | Improved TerminalCurrentInformation | +EnvironmentTemperature, +ValidForMatingTerminal; NominalVoltage [1..1] -> [0..1] | Up | R5 value/list; R1 | M20, M20 | covered |
| #1125 | determinationType for pin current/voltage | PinCurrentInformation/PinVoltageInformation +DeterminationType | Up | R5 values | M20, M20 | covered |
| #1122 | pullOffForce for CrimpDetail/WireMountingDetail | Core/InsulationCrimpDetail (new classes) +Core/InsulationPullOffForce; WireMountingDetail +PullOffForce, +CorePullOffForce, +InsulationPullOffForce | Up | R5 values | M20 | covered |
| #1116 | SubWireElements non-unique | mt:relationship meta only | - | none needed | - | covered |
| #1108 | CrimpBarrelType | WireReceptionSpecification +CrimpBarrelType | Up | R5 value | M20 | covered |
| #1104 | Shield crimp WireReceptionTypes | open +UniaxialShieldCrimp, +MultiaxialShieldCrimp | Up | R1 | - | covered |
| #1101 | absoluteSealPosition | WireMountingDetail +AbsoluteSealPosition | Up | R5 value | M20 | covered |
| #1100 | Partial pull-off for stripping | WireEnd +InsulationPullbackLength | Up | R5 value | M20 | covered |
| #1098 | Ordering and layering of multi-core wires | WireElement +Index, +Layer | Up | R5 values | M20 | covered |
| #1097 | TransmissionMedium for Conductor/Terminal | ConductorSpecification/TerminalSpecification +TransmissionMedium | Up | R5 values | M20, M20 | covered |
| #1071 | NumberOfStrands revert | doc only | - | none needed | - | covered |
| #1069 | Logic within ModuleFamilies | ModuleFamily base ExtendableElement -> ConfigurableElement, +Mandatory; ModuleList base -> ConfigurableElement | Up | R5 configurableElementPropertiesPlus; ModuleFamilyWrapper registers isMandatory/setMandatory | M20; w12to20/ModuleFamilyWrapper.java | covered |
| #894 | Logic within ModuleFamilies | same change set as #1069 | Up | see #1069 | M20; w12to20/ModuleFamilyWrapper.java | covered |
| #1018 | Joining methods terminal/wire | WireReceptionSpecification.WireReceptionType [0..1] -> [0..*]; WireMountingDetail +WireReceptionType; open +CrimpAndSoldering | Up | renamed(wireReceptionTypes, wireReceptionType); R5 value on WireMountingDetail | M20 (renamed at line 292), M20; T20/Vec12To20PartMasterConversionTest#singleValuesBecomeListsWithThatValue | covered by test. Down 2.x -> 1.2: renamed back, first element (M20). |
| #969 | voltageComplianceLevel | GTPS +VoltageLevelCompliance | Up | R5 list | M20 | covered |
| #957 | Cavity mapping for modular connectors | new ModularSlotConfiguration, CavityLayout; ModularSlot +Layout; ConnectorHousingSpecification +ModularSlotConfiguration; SlotMapping +LayoutIdentificationA/B | Up | R5 lists/values; Cavity `parentCavityLayout` | M20, M20, M20, M20 | covered |
| #841 | changeRestriction on ItemVersion | ItemVersion +ChangeRestriction | Up | R5 list | M20 | covered |
| #1121 | Consolidated wire part master data | WireSpecification.WireElementSpecification [1..1] -> [0..1] and deprecated; WireElementSpecification.SubWireElementSpecification deprecated | Up | R1 | - | covered |
| #1115 | ApplicationType on GTPS | GTPS +ApplicationType | Up | R5 list | M20 | covered |
| #1089 | Consolidated VoltageRange/VoltageRating | GTPS +VoltageRating; Conductor-/ConnectorHousing-/TerminalSpecification.VoltageRange and EEComponentSpecification.VoltageRating deprecated | Up | R5 value; deprecated properties R1 | M20 | covered |
| #1136 | Docs AbstractSlot/Slot/ModularSlot | doc only | - | none needed | - | covered |
| #1133 | Diagram "Placement and Dimensions" | doc only | - | none needed | - | covered |
| #1103 | Visual definition of terminal dimensions | doc only | - | none needed | - | covered |
| #1096 | Chapter moved to implementation guidelines | doc only | - | none needed | - | covered |
| #1095 | Path format of file names | doc only | - | none needed | - | covered |

---

## Gaps and open points

### Downgrade 1.2.2 -> 1.1.3 (main direction), most important first

1. **WireEndAccessory containment in harness data, no release-note issue (GAP).** In 1.1.3, WireEndAccessoryRole *contains*
   its WireEndAccessorySpecification and WireEndAccessorySpecification *contains* its
   WireReceptionSpecification. In 1.2 both are IDREFs. Nothing handles this, so the downgrade writes the
   referenced 1.2 objects inline. The same xs:ID then appears twice whenever the specification also lives
   in a DocumentVersion of the same file, or is used by several roles. The result is schema-invalid
   (duplicate IDs). Fixed for part master data: a WireReceptionSpecification of a WireEndAccessorySpecification
   of the same document is written inside the latter only. Still open for WireEndAccessoryRoles.
2. **SealingClass/AbrasionResistanceClass depend on a data convention (KBLFRM-811, open question).** The
   wrappers pick the RobustnessProperties whose `ClassKey` is "SealingClass"/"AbrasionResistanceClass". The
   VEC 1.2 documentation defines ClassKey as the *level* (e.g. A, B, C) and `Class` as the influence, and
   RobustnessClass has no "SealingClass" literal. So data following the documentation yields no
   SealingClass/AbrasionResistanceClass in 1.1.3, and where the convention matches, ClassKey gets a literal
   instead of a level. Whether the producers of the converted data follow this convention decides whether
   the mapping has to change. The underlying RobustnessProperties survive on the GTPS.
   (w12to11/specification/AbstractSpecificationReflectionBasedWrapper.java,
   w12to11/specification/wireprotection/WireProtectionSpecificationWrapper.java)
3. **Mandatory 1.1.3 properties that come out null or empty, giving schema-invalid output (GAP).**
   - EEComponentRole.EEComponentSpecification pointing to a Diode- or CapacitorSpecification. R4 drops
     the specification instead of mapping it to its superclass EEComponentSpecification (KBLFRM-818).
   - MatingPoint.FirstTerminalRole/SecondTerminalRole pointing to a Bolt-, Bridge- or
     OpenWireEndTerminalRole, which is dropped by R4 (KBLFRM-840, -897, -630). Rarer: SpecificRole.Specification,
     TerminalRole.TerminalSpecification, ExternalMapping.MappedElement pointing to a dropped class.
   - PartRelation.RelationType = Custom: closed enum, no 1.1.3 constant (KBLFRM-964).
   - Properties that 1.2 made optional: ChangeDescription.ResponsibleDesigner (KBLFRM-810),
     PartWithSubComponentsRole.SubComponent (KBLFRM-842), WireGrouping.WireGroupSpecification (KBLFRM-894).
4. **TopologyZones (KBLFRM-472, GAP).** Zones are lost because TopologySpecification.Zone is always empty,
   but BuildingBlockSpecification3D.Zone still references a zone, leaving a dangling IDREF.
   (w12to11/specification/BuildingBlockSpecification3DWrapper.java, M11)
5. **Covered, lossy: what a downgrade consumer loses.**
   - Specifications and roles without a 1.1.3 class are dropped, although they could fall back to their
     superclass: Bolt-/Bridge-/OpenWireEndTerminal, Diode, Capacitor, Ferrite, TerminalPairing,
     RequirementsConformance, CableLeadThroughSpecification, LocalGeometry, BendingRestriction,
     TopologyMapping, PinWireMapping, AssignmentGroups, Baselines, Variant/FunctionalStructure,
     ApplicationConstraint, ComplexProperty.
   - Tape data: TapeOverlap, TapingDirection (semantics changed), TapeOverlapRelative, WindingType and
     WindingFirmness are dropped; Gradient is kept (KBLFRM-878/-820).
   - NURBS knots and weights (KBLFRM-883).
   - Cavity data: only the first CavityDimension is used and its height becomes the diameter;
     Geometry/CompatibleCavityGeometry/InsideDiameter are dropped (KBLFRM-921).
   - Lists reduced to their first element: RealizedPartUsage and ViewItem2D/3D.OccurrenceOrUsage
     (KBLFRM-963, -929).
   - PartUsages are filtered out of InBillOfMaterial, ReferenceElement and ModuleList.CompletionComponents
     (KBLFRM-643, -900).
   - Nested WireGroupings disappear.
   - All new 1.2 properties are dropped (R6), e.g. FitRate, Signal.DataRate/CurrentType,
     DigitalRepresentationIndex, Unconnected, Connection/ConnectionGroup traceability, SubType, AliasId,
     Abbreviation, CompatibleTypes, ReferencedExternalDocuments.
   - New open-enum literals reach 1.1.3 as plain strings and are not mapped back to 1.1.3 literals
     (PinComponentType PowerSupply/PowerDistribution/NotConnected instead of Power/"Not connected",
     SignalSubType MOST).

### Upgrade 1.2.2 -> 2.2.0 (important direction)

1. **SwitchingState.SwitchedLogicalConnections (#915, GAP).** The property is mandatory in 2.2.0 and always
   comes out as an empty list, so every upgraded SwitchingState is schema-invalid. (M20)
2. **Covered, lossy or based on assumptions.**
   - PreferredUseCase is declared German (KBLFRM-1054).
   - A non-integer NumberOfSheets is dropped (KBLFRM-1017).
   - ConnectorHousingRole gets *all* connectors of its ComponentNode (KBLFRM-999).
   - CableLeadThrough gets a synthetic single outlet; there is no test (KBLFRM-854).
   - A 1.2 Tolerance loses its ExtendableElement content (#1102).
   - OpticalTissueDensity is not converted to OpticalCoverage (KBLFRM-1197).
   - Roles are not specialized to the 2.1 role subclasses (KBLFRM-1111). The output is still valid.
3. Everything else in 2.0.0-2.2.0 is either pass-through (open enums, deprecations, loosened
   cardinalities), R2/R3 conversions covered by Vec12To20CardinalityTest/Vec12To20PartMasterConversionTest,
   or R5 additions on the correct v12x source class. The static scan found no 2.x getter without a handler.

### Other direction, where it needs something

- **2.x -> 1.2.**
  - The closed PrimaryPartType literals new in 2.x (BridgeTerminal, CableSeal, CableSealPlug, Coating,
    ConnectorHousingCover, OpenWireEndTerminal, TerminalProtection) become null in the mandatory
    PartVersion.PrimaryPartType and in PartUsage.PrimaryPartUsageType. w20to12/PartVersionWrapper has no
    replacement map like w12to11/PartVersionWrapper.
  - OtherUnitName ArcDegree/ArcMinute/ArcSecond become null in the mandatory OtherUnit.OtherUnitName.
  - The 2.1 role subclasses (FuseRole, TubeRole, MultiCavityPlugRole, ...) and HoleTerminalRole are
    dropped entirely, so required MatingPoint terminal roles can become null.
  - Several outlets of a CableLeadThrough are reduced to the first one (warning).
- **1.1.3 -> 1.2.**
  - CavityAddOn.Type is mandatory in 1.2 and comes out null (M11, KBLFRM-816).
  - SealingClass/AbrasionResistanceClass are not converted to RobustnessProperties.
  - AllowedInserts PartVersions are filtered out, because 1.2 expects PartRelations.
  - The contained WireEndAccessory/WireReceptionSpecification become IDREFs to objects that are not in any
    DocumentVersion.
  - Two upgrade additions hide real 1.1.3 navigation data (M11 backRef `refPartStructureSpecification`
    on OccurrenceOrUsage, M11 value `parentWireGroupingSpecification` on WireGrouping). This affects
    navigation only and is not serialized.

### Housekeeping (no functional effect)

- M20 registers `testingPullOutForce` on PluggableTerminalSpecification. In 2.x the property is on
  TerminalReceptionSpecification, so this registration is a no-op.
- `contactRangeLength` is registered twice (M20, M20) and `materialLengths` on WireProtectionRole
  twice (M20, M20).
- `configurableElementPropertiesPlus` adds `componentConnector`, which only HousingComponent has.
- w12to11/OccurrenceOrUsageWrapper has no `@Wraps` and acts only through its subclasses.

### Not verified

- **Harness data.** The end-to-end tests convert part master data (synthetic VEC 1.2.2) and one real
  VEC 1.2 harness document. The open gaps above concern harness constructs the samples do not contain.
- **Dangling IDREFs (gap 4) and duplicate IDs (gap 1).** It is certain that they make the output
  schema-invalid. Not checked: whether the JAXB writer already fails while marshalling.
- **Inferred attributions.** #1112 (FixingSpecification.MaximumWidthA/B) and #1102 (base-class changes of
  Tolerance/Unit/SlotMapping) are present in the XSD diff, but which issue they belong to cannot be
  verified from the XSD.
- **1.1.3 WireGrouping.RelatedWireRole.** Its XSD documentation is empty. The library's binding types it
  as WireElementReference, and the downgrade relies on that.

---

## XSD changes without a release-note issue

| Change | Versions | Relevant direction | Handling | Where | Status |
|---|---|---|---|---|---|
| CapacitorSpecification added; PrimaryPartType +Capacitor | 1.1.3 -> 1.2 | Down | PartVersionWrapper maps Capacitor -> EEComponent; specification R4 | w12to11/PartVersionWrapper.java | PartVersion: covered, lossy. **GAP**: EEComponentRole.EEComponentSpecification -> CapacitorSpecification becomes null; mandatory, so the output is schema-invalid. PartUsage.PrimaryPartUsageType: covered. |
| WireEndAccessoryRole.WireEndAccessorySpecification and WireEndAccessorySpecification.WireReceptionSpecification: containment (1.1.3) -> IDREF (1.2) | 1.1.3 -> 1.2 | Down | Part master data: w12to11/DocumentVersionWrapper writes a WireReceptionSpecification of a WireEndAccessorySpecification of the same document inside the latter only. | w12to11/DocumentVersionWrapper.java; T11/Vec12To11PartMasterConversionTest | Part master data: covered by test. **GAP** for harness data: a WireEndAccessoryRole contains its specification in 1.1.3, so the specification is written inside the role and again in its part master document (or in several roles) with the same xs:ID, which is schema-invalid. Up 1.1.3 -> 1.2: IDREFs to objects that are not in any DocumentVersion. |
| ModularSlot/ExtensionSlot.AllowedInserts: PartVersion -> PartRelation (IDREFS in both XSDs; the 1.2.0 overview calls it "refactored modular slot definition") | 1.1.3 -> 1.2 | Down | AllowedInsertsWrapper returns the AccessoryParts of the relations | w12to11/AllowedInsertsWrapper.java | covered, lossy (relation type lost). Up 1.1.3 -> 1.2: the PartVersions are filtered out (lost). |
| ModuleList.CompletionComponents: PartOccurrence -> OccurrenceOrUsage (binding) | 1.1.3 -> 1.2 | Down | R2 element filter drops PartUsages | core/wrapper/ReturnValueAdapter.java | covered, lossy |
| TapingDirection: open (Left/Right) -> closed (FromStart/FromEnd) | 1.1.3 -> 1.2 | Down | see KBLFRM-820 | M11 | covered, lossy |
| NetworkNodeType +Ground (open) | 1.1.3 -> 1.2 | Down | R1 | - | covered |
| VariantConfiguration.ConfigurationType string -> open enum VariantConfigurationType | 1.2 -> 2.0.0 | Up | R1 | - | covered |
| VariantGroupType +OptionalFamily, +LogicalGroup (open) | 1.2 -> 2.0.0 | Up | R1 | - | covered |
| ConductorSpecification/InsulationSpecification +WireType | 2.1.0 | Up | R5 list `wireTypes` | M20, M20 | covered |
| RingTerminalSpecification +NutIntegrated | 2.1.0 | Up | RingTerminalSpecificationWrapper registers isNutIntegrated/setNutIntegrated | w12to20/RingTerminalSpecificationWrapper.java | covered |
| CavityPositionDetail +InsertionDepth | 2.1.0 | Up | new class, none needed | - | covered |
| PrimaryLockingType +Lanceless (open) | 2.1.0 | Up | R1 | - | covered |
| PrimaryPartType (closed) +BridgeTerminal, +OpenWireEndTerminal | 2.1.0 | Up | none needed | - | covered. Down 2.x -> 1.2: R7 makes the value null (see above). |
| DefaultDimensionType +MeasurementChains (open) | 1.2 -> 2.x | Up | R1 | - | covered |
| PartStructureSpecification +DefinedAsEmpty | 2.2.0 | Up | PartStructureSpecificationWrapper registers isDefinedAsEmpty/setDefinedAsEmpty | w12to20/PartStructureSpecificationWrapper.java | covered |
| TopologyNode.InstantiatedNode / TopologySegment.InstantiatedSegment deprecated | 2.2.0 | Up | R1 | - | covered |
