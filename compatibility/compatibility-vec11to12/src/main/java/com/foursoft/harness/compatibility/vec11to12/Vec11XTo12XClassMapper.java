/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.1.X To VEC 1.2.X
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =========================LICENSE_END==================================
 */
package com.foursoft.harness.compatibility.vec11to12;

import com.foursoft.harness.compatibility.core.PropertyAdditionProvider;
import com.foursoft.harness.compatibility.core.PurePropertyAdditions;
import com.foursoft.harness.compatibility.core.mapping.NameBasedClassMapper;
import com.foursoft.harness.compatibility.core.util.ClassUtils;
import com.foursoft.harness.vec.v113.*;
import com.foursoft.harness.vec.v12x.VecNURBSCurve;
import com.foursoft.harness.vec.v12x.VecTopologyZone;

import java.util.HashMap;
import java.util.Map;

import static com.foursoft.harness.compatibility.core.PropertyAddition.*;

/**
 * Class responsible for mapping VEC 1.1.X classes to VEC 1.2.X <b>and vice versa</b>.
 */
public class Vec11XTo12XClassMapper extends NameBasedClassMapper implements PropertyAdditionProvider {

    private final Map<Class<?>, Class<?>> classMap;
    private final PurePropertyAdditions propertyAdditions;

    /**
     * Creates a VEC 1.1 to VEC 1.2 Class Mapper.
     */
    public Vec11XTo12XClassMapper() {
        super(Constants.PACKAGE_VEC11X, Constants.PACKAGE_VEC12X);

        classMap = new HashMap<>();

        // VEC 1.1.X -> VEC 1.2.X
        classMap.put(VecBSplineCurve.class, VecNURBSCurve.class);
        classMap.put(VecZone.class, VecTopologyZone.class);

        // VEC 1.2.X -> VEC 1.1.X
        classMap.put(VecNURBSCurve.class, VecBSplineCurve.class);
        classMap.put(VecTopologyZone.class, VecZone.class);

        // ── pure property additions — properties new in VEC 1.2.X handled automatically by DefaultWrapper
        propertyAdditions = new PurePropertyAdditions()
                .register(VecAliasIdentification.class, value("type"))
                .register(VecApproval.class, value("additionalLevelInformation"))
                .register(VecBSplineCurve.class, list("knots"))
                .register(VecCableLeadThrough.class, value("cableLeadThroughSpecification"),
                          backRef("refCableLeadThroughReference"))
                .register(VecCavityPlugRole.class, backRef("refCableLeadThroughReference"))
                .register(VecCavitySealRole.class, backRef("refCableLeadThroughReference"))
                .register(VecCavityAddOn.class, value("type"))
                .register(VecCavityPartSpecification.class, list("compatibleCavityGeometries"))
                .register(VecCavityReference.class, value("integratedTerminalRole"))
                .register(VecCavitySpecification.class, value("geometry"))
                .register(VecCartesianPoint3D.class, value("parentLocalGeometrySpecification"),
                          backRef("refLocalPosition"), backRef("refNURBSControlPoint"))
                .register(VecComponentNode.class, value("subType"))
                .register(VecConfigurableElement.class,
                          list("applicationConstraint"), list("associatedAssignmentGroups"))
                .register(VecConnection.class, backRef("refBridgeTerminalRole"), backRef("refMatingDetail"),
                          backRef("refMatingPoint"))
                .register(VecConnectionGroup.class, backRef("refWireElementReference"), backRef("refWireGrouping"))
                .register(VecContactPoint.class, backRef("refPinWireMappingPoint"))
                .register(VecConductorSpecification.class, backRef("refTerminalPairing"))
                .register(VecCustomProperty.class, value("parentComplexProperty"))
                .register(VecDocumentVersion.class, value("digitalRepresentationIndex"),
                          backRef("refBaselineSpecification"), backRef("refDocumentRelatedAssignmentGroup"),
                          backRef("refExtendableElement"), backRef("refRequirementsConformanceStatement"))
                .register(VecExtendableElement.class, list("referencedExternalDocuments"))
                .register(VecGeneralTechnicalPartSpecification.class,
                          value("fitRate"))
                .register(VecGrommetRole.class, list("cableLeadThroughReferences"))
                .register(VecHousingComponent.class, list("compatibleTypes"))
                .register(VecLocation.class, value("parentNodeMapping"), value("parentZoneCoverage"))
                .register(VecMatingDetail.class, value("connection"))
                .register(VecMatingPoint.class, value("connection"))
                .register(VecMeasurementPoint.class, backRef("refMeasurePointPosition"))
                .register(VecNetType.class, value("signalTransmissionMediumType"))
                .register(VecNetworkNode.class, value("subType"))
                .register(VecOccurrenceOrUsage.class, backRef("refOccurrenceOrUsage"),
                          backRef("refPartStructureSpecification"), backRef("refPartUsage"))
                .register(VecPartRelation.class, value("customRelationExpression"), backRef("refExtensionSlot"),
                          backRef("refModularSlot"), backRef("refSlot"))
                .register(VecPartUsage.class, list("instanciatedUsage"), list("referenceElement"),
                          backRef("refModuleList"))
                .register(VecPartVersion.class, backRef("refTerminalPairing"), backRef("refBaselineSpecification"))
                .register(VecPath.class,
                          value("parentSegmentMapping"), value("parentTopologyBendingRestriction"))
                .register(VecPinComponent.class, backRef("refDiodeSpecification"))
                .register(VecPinComponentReference.class, backRef("refPinWireMappingPoint"))
                .register(VecPlacementPoint.class, backRef("refPlacementPointPosition"))
                .register(VecProject.class, backRef("refApplicationConstraint"))
                .register(VecRingTerminalSpecification.class, value("boltNominalSize"))
                .register(VecSheetOrChapter.class, backRef("refDocumentRelatedAssignmentGroup"))
                .register(VecSignal.class, value("currentType"), value("dataRate"))
                .register(VecSlot.class, list("supplementaryParts"))
                .register(VecTerminalRole.class, value("parentCavityReference"))
                .register(VecTopologyGroupSpecification.class, backRef("refTopologyMappingSpecification"))
                .register(VecTopologyNode.class, value("instantiatedNode"), backRef("refNodeMapping"),
                          backRef("refTopologyNode"))
                .register(VecTopologySegment.class, value("instantiatedSegment"), backRef("refSegmentMapping"),
                          backRef("refTopologySegment"), backRef("refZoneAssignment"))
                .register(VecTopologySpecification.class, backRef("refTopologyMappingSpecification"))
                .register(VecTransformation3D.class, value("parentLocalGeometrySpecification"))
                .register(VecUnit.class, backRef("refLocalGeometrySpecification"))
                .register(VecVariantCode.class, value("abbreviation"), list("aliasIds"))
                .register(VecVariantConfiguration.class, value("baseInclusion"), backRef("refVariantConfiguration"))
                .register(VecVariantGroup.class, value("abbreviation"), list("aliasIds"),
                          backRef("refVariantStructureNode"))
                .register(VecWireElement.class, value("parentWireElement"))
                .register(VecWireElementReference.class, value("connectionGroup"))
                .register(VecWireEndAccessorySpecification.class, backRef("refWireEndAccessoryRole"))
                .register(VecWireGrouping.class,
                          list("containedWireGroupings"),
                          list("relatedWireElementReference"),
                          value("connectionGroup"),
                          value("parentWireGrouping"),
                          value("parentWireGroupingSpecification"))
                .register(VecWireReceptionSpecification.class, list("addOns"),
                          backRef("refWireEndAccessorySpecification"))
                .register(VecZone.class,
                          value("parentTopologyZone"),
                          value("parentTopologyZoneSpecification"),
                          value("type"), list("assignments"))

                // Pure Removals:
                .register(com.foursoft.harness.vec.v12x.VecContent.class, list("compliantConformanceClasses"))
                .register(com.foursoft.harness.vec.v12x.VecPath.class, value("configInfo"))
                .register(com.foursoft.harness.vec.v12x.VecTopologySpecification.class, list("zones"))
                .register(com.foursoft.harness.vec.v12x.VecWireProtectionRole.class, value("gradient"))
                .register(com.foursoft.harness.vec.v12x.VecWireProtectionRole.class, value("tapeOverlap"))
                .register(com.foursoft.harness.vec.v12x.VecWireProtectionRole.class, value("tapingDirection"))
        ;
    }

    @Override
    public PurePropertyAdditions getPropertyAdditions() {
        return propertyAdditions;
    }

    @Override
    public Class<?> map(final Class<?> clazz) {
        final Class<?> aClass = classMap.get(clazz);
        return aClass != null
                ? aClass
                : classMap.getOrDefault(ClassUtils.getNonProxyClass(clazz), super.map(clazz));
    }

}
