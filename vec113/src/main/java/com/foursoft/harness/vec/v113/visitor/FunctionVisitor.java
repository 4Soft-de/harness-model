/*-
 * ========================LICENSE_START=================================
 * vec113
 * %%
 * Copyright (C) 2020 - 2022 4Soft GmbH
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
package com.foursoft.harness.vec.v113.visitor;

import com.foursoft.harness.vec.v113.*;
import com.foursoft.xml.model.Identifiable;

import java.util.Objects;
import java.util.function.Function;

/**
 * A Visitor implementation which returns the value returned by the function invocation for each visit method.
 * The function has to be non-null.
 *
 * @param <I> Input Type of the Function.
 * @param <O> Output Type of the Function and Return Type of the visit methods.
 */
public class FunctionVisitor<I extends Identifiable, O> implements Visitor<O, RuntimeException> {

    private final Function<I, O> func;

    /**
     * Instances a new FunctionVisitor with the given function.
     *
     * @param func Function which will be executed and returned for each visit method.
     * @throws NullPointerException If the given function is {@code null}.
     */
    public FunctionVisitor(final Function<I, O> func) {
        this.func = Objects.requireNonNull(func, "Given Function may not be null.");
    }

    @Override
    public O visitVecAbrasionResistanceClass(final VecAbrasionResistanceClass aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecAliasIdentification(final VecAliasIdentification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecAntennaSpecification(final VecAntennaSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecApproval(final VecApproval aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecBSplineCurve(final VecBSplineCurve aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecBatterySpecification(final VecBatterySpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecBoltMountedFixingSpecification(final VecBoltMountedFixingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecBooleanValueProperty(final VecBooleanValueProperty aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecBoundingBox(final VecBoundingBox aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecBuildingBlockPositioning2D(final VecBuildingBlockPositioning2D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecBuildingBlockPositioning3D(final VecBuildingBlockPositioning3D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecBuildingBlockSpecification2D(final VecBuildingBlockSpecification2D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecBuildingBlockSpecification3D(final VecBuildingBlockSpecification3D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCableDuctOutlet(final VecCableDuctOutlet aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCableDuctRole(final VecCableDuctRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCableDuctSpecification(final VecCableDuctSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCableLeadThrough(final VecCableLeadThrough aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCableTieRole(final VecCableTieRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCableTieSpecification(final VecCableTieSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCartesianDimension(final VecCartesianDimension aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCartesianPoint2D(final VecCartesianPoint2D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCartesianPoint3D(final VecCartesianPoint3D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCartesianVector2D(final VecCartesianVector2D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCartesianVector3D(final VecCartesianVector3D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavity(final VecCavity aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityAccessoryRole(final VecCavityAccessoryRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityAccessorySpecification(final VecCavityAccessorySpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityAddOn(final VecCavityAddOn aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityCoupling(final VecCavityCoupling aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityMapping(final VecCavityMapping aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityMounting(final VecCavityMounting aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityMountingDetail(final VecCavityMountingDetail aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityPlugRole(final VecCavityPlugRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityPlugSpecification(final VecCavityPlugSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityReference(final VecCavityReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavitySealRole(final VecCavitySealRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavitySealSpecification(final VecCavitySealSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavitySpecification(final VecCavitySpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecChangeDescription(final VecChangeDescription aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCoding(final VecCoding aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecColor(final VecColor aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCompatibilitySpecification(final VecCompatibilitySpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCompatibilityStatement(final VecCompatibilityStatement aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecComponentConnector(final VecComponentConnector aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecComponentNode(final VecComponentNode aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecComponentPort(final VecComponentPort aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCompositeUnit(final VecCompositeUnit aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCompositionSpecification(final VecCompositionSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConductorCurrentInformation(final VecConductorCurrentInformation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConductorMaterial(final VecConductorMaterial aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConformanceClass(final VecConformanceClass aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConnection(final VecConnection aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectionEnd(final VecConnectionEnd aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectionGroup(final VecConnectionGroup aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectionSpecification(final VecConnectionSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectorHousingCapRole(final VecConnectorHousingCapRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectorHousingCapSpecification(final VecConnectorHousingCapSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectorHousingRole(final VecConnectorHousingRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectorHousingSpecification(final VecConnectorHousingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecContactPoint(final VecContactPoint aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecContactSystem(final VecContactSystem aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecContactSystemSpecification(final VecContactSystemSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecContactingSpecification(final VecContactingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecContent(final VecContent aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecContract(final VecContract aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCopyrightInformation(final VecCopyrightInformation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCoreSpecification(final VecCoreSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCorrugatedPipeSpecification(final VecCorrugatedPipeSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCouplingPoint(final VecCouplingPoint aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCouplingSpecification(final VecCouplingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCreation(final VecCreation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCustomUnit(final VecCustomUnit aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecDateValueProperty(final VecDateValueProperty aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecDefaultDimension(final VecDefaultDimension aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecDefaultDimensionSpecification(final VecDefaultDimensionSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecDimension(final VecDimension aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecDocumentBasedInstruction(final VecDocumentBasedInstruction aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecDocumentVersion(final VecDocumentVersion aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecDoubleValueProperty(final VecDoubleValueProperty aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecEEComponentRole(final VecEEComponentRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecEEComponentSpecification(final VecEEComponentSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecEdgeMountedFixingSpecification(final VecEdgeMountedFixingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecExtensionSlot(final VecExtensionSlot aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecExtensionSlotReference(final VecExtensionSlotReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecExternalMapping(final VecExternalMapping aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecExternalMappingSpecification(final VecExternalMappingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecFileBasedInstruction(final VecFileBasedInstruction aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecFillerSpecification(final VecFillerSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecFittingOutlet(final VecFittingOutlet aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecFittingSpecification(final VecFittingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecFixingRole(final VecFixingRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecFixingSpecification(final VecFixingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecFlatCoreSpecification(final VecFlatCoreSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecFuseComponent(final VecFuseComponent aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecFuseSpecification(final VecFuseSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecGeneralTechnicalPartSpecification(final VecGeneralTechnicalPartSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecGeometryNode2D(final VecGeometryNode2D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecGeometryNode3D(final VecGeometryNode3D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecGeometrySegment2D(final VecGeometrySegment2D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecGeometrySegment3D(final VecGeometrySegment3D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecGrommetRole(final VecGrommetRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecGrommetSpecification(final VecGrommetSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecHarnessDrawingSpecification2D(final VecHarnessDrawingSpecification2D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecHarnessGeometrySpecification3D(final VecHarnessGeometrySpecification3D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecHoleMountedFixingSpecification(final VecHoleMountedFixingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecHousingComponent(final VecHousingComponent aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecHousingComponentReference(final VecHousingComponentReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecIECUnit(final VecIECUnit aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecImperialUnit(final VecImperialUnit aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecInsulationSpecification(final VecInsulationSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecIntegerValueProperty(final VecIntegerValueProperty aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecInternalComponentConnection(final VecInternalComponentConnection aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecInternalTerminalConnection(final VecInternalTerminalConnection aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecItemEquivalence(final VecItemEquivalence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecItemHistoryEntry(final VecItemHistoryEntry aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecLocalizedString(final VecLocalizedString aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecLocalizedStringProperty(final VecLocalizedStringProperty aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecLocalizedTypedString(final VecLocalizedTypedString aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMapping(final VecMapping aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMappingSpecification(final VecMappingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMassInformation(final VecMassInformation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMaterial(final VecMaterial aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMatingDetail(final VecMatingDetail aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMatingPoint(final VecMatingPoint aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMeasurementPoint(final VecMeasurementPoint aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMeasurementPointReference(final VecMeasurementPointReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecModularSlot(final VecModularSlot aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecModularSlotAddOn(final VecModularSlotAddOn aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecModularSlotReference(final VecModularSlotReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecModuleFamily(final VecModuleFamily aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecModuleFamilySpecification(final VecModuleFamilySpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecModuleList(final VecModuleList aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecModuleListSpecification(final VecModuleListSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMultiCavityPlugSpecification(final VecMultiCavityPlugSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMultiCavitySealSpecification(final VecMultiCavitySealSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMultiFuseSpecification(final VecMultiFuseSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecNet(final VecNet aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecNetGroup(final VecNetGroup aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecNetSpecification(final VecNetSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecNetType(final VecNetType aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecNetworkNode(final VecNetworkNode aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecNetworkPort(final VecNetworkPort aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecNodeLocation(final VecNodeLocation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecNumericalValue(final VecNumericalValue aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecNumericalValueProperty(final VecNumericalValueProperty aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecOccurrenceOrUsageViewItem2D(final VecOccurrenceOrUsageViewItem2D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecOccurrenceOrUsageViewItem3D(final VecOccurrenceOrUsageViewItem3D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecOnPointPlacement(final VecOnPointPlacement aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecOnWayPlacement(final VecOnWayPlacement aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecOpenCavitiesAssignment(final VecOpenCavitiesAssignment aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecOtherUnit(final VecOtherUnit aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPartOccurrence(final VecPartOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPartOrUsageRelatedSpecification(final VecPartOrUsageRelatedSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPartRelation(final VecPartRelation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPartStructureSpecification(final VecPartStructureSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPartSubstitutionSpecification(final VecPartSubstitutionSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPartUsage(final VecPartUsage aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPartUsageSpecification(final VecPartUsageSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPartVersion(final VecPartVersion aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPartWithSubComponentsRole(final VecPartWithSubComponentsRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPath(final VecPath aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPathSegment(final VecPathSegment aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPermission(final VecPermission aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPerson(final VecPerson aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPinComponent(final VecPinComponent aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPinComponentBehavior(final VecPinComponentBehavior aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPinComponentReference(final VecPinComponentReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPinCurrentInformation(final VecPinCurrentInformation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPinOpticalInformation(final VecPinOpticalInformation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPinTiming(final VecPinTiming aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPinVoltageInformation(final VecPinVoltageInformation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPlaceableElementRole(final VecPlaceableElementRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPlaceableElementSpecification(final VecPlaceableElementSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPlacementPoint(final VecPlacementPoint aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPlacementPointReference(final VecPlacementPointReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPlacementSpecification(final VecPlacementSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPluggableTerminalRole(final VecPluggableTerminalRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPluggableTerminalSpecification(final VecPluggableTerminalSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPotentialDistributorSpecification(final VecPotentialDistributorSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPowerConsumption(final VecPowerConsumption aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecProject(final VecProject aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecRelaySpecification(final VecRelaySpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecRingTerminalRole(final VecRingTerminalRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecRingTerminalSpecification(final VecRingTerminalSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecRobustnessProperties(final VecRobustnessProperties aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecRouting(final VecRouting aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecRoutingSpecification(final VecRoutingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSIUnit(final VecSIUnit aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSealedCavitiesAssignment(final VecSealedCavitiesAssignment aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSealingClass(final VecSealingClass aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSegmentConnectionPoint(final VecSegmentConnectionPoint aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSegmentCrossSectionArea(final VecSegmentCrossSectionArea aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSegmentLength(final VecSegmentLength aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSegmentLocation(final VecSegmentLocation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSheetOrChapter(final VecSheetOrChapter aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecShieldSpecification(final VecShieldSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecShrinkableTubeSpecification(final VecShrinkableTubeSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSignal(final VecSignal aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSignalSpecification(final VecSignalSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSimpleValueProperty(final VecSimpleValueProperty aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSize(final VecSize aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSlot(final VecSlot aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSlotCoupling(final VecSlotCoupling aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSlotLayout(final VecSlotLayout aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSlotMapping(final VecSlotMapping aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSlotReference(final VecSlotReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSlotSpecification(final VecSlotSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSoundDampingClass(final VecSoundDampingClass aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSpecificRole(final VecSpecificRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSpliceTerminalRole(final VecSpliceTerminalRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSpliceTerminalSpecification(final VecSpliceTerminalSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecStripeSpecification(final VecStripeSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSwitchingState(final VecSwitchingState aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTapeSpecification(final VecTapeSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTemperatureInformation(final VecTemperatureInformation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalCurrentInformation(final VecTerminalCurrentInformation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalReception(final VecTerminalReception aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalReceptionReference(final VecTerminalReceptionReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalReceptionSpecification(final VecTerminalReceptionSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalRole(final VecTerminalRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalSpecification(final VecTerminalSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalType(final VecTerminalType aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTextBasedInstruction(final VecTextBasedInstruction aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTolerance(final VecTolerance aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTopologyGroupSpecification(final VecTopologyGroupSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTopologyNode(final VecTopologyNode aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTopologySegment(final VecTopologySegment aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTopologySpecification(final VecTopologySpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTransformation2D(final VecTransformation2D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTransformation3D(final VecTransformation3D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTubeSpecification(final VecTubeSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecUSUnit(final VecUSUnit aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecUsageConstraint(final VecUsageConstraint aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecUsageConstraintSpecification(final VecUsageConstraintSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecUsageNode(final VecUsageNode aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecUsageNodeSpecification(final VecUsageNodeSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecValueRange(final VecValueRange aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecValueRangeProperty(final VecValueRangeProperty aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantCode(final VecVariantCode aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantCodeSpecification(final VecVariantCodeSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantConfiguration(final VecVariantConfiguration aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantConfigurationSpecification(final VecVariantConfigurationSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantGroup(final VecVariantGroup aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantGroupSpecification(final VecVariantGroupSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireElement(final VecWireElement aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireElementReference(final VecWireElementReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireElementSpecification(final VecWireElementSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireEnd(final VecWireEnd aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireEndAccessoryRole(final VecWireEndAccessoryRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireEndAccessorySpecification(final VecWireEndAccessorySpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireGroupSpecification(final VecWireGroupSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireGrouping(final VecWireGrouping aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireGroupingSpecification(final VecWireGroupingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireLength(final VecWireLength aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireMounting(final VecWireMounting aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireMountingDetail(final VecWireMountingDetail aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireProtectionRole(final VecWireProtectionRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireProtectionSpecification(final VecWireProtectionSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireReception(final VecWireReception aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireReceptionReference(final VecWireReceptionReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireReceptionSpecification(final VecWireReceptionSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireRole(final VecWireRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireSpecification(final VecWireSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireType(final VecWireType aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecZone(final VecZone aBean) throws RuntimeException {
        return apply(aBean);
    }

    private <T extends Identifiable> O apply(final T element) {
        return func.apply((I) element);
    }

}
