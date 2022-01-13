/*-
 * ========================LICENSE_START=================================
 * vec120
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
package com.foursoft.vecmodel.vec120.visitor;

import com.foursoft.vecmodel.vec120.*;
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
     * @param func Function which will be executed and returned for each visit method.
     * @throws NullPointerException If the given function is {@code null}.
     */
    public FunctionVisitor(final Function<I, O> func) {
        this.func = Objects.requireNonNull(func, "Given Function may not be null.");
    }

    @Override
    public O visitVecAliasIdentification(final VecAliasIdentification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecAntennaSpecification(final VecAntennaSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecApplicationConstraint(final VecApplicationConstraint aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecApplicationConstraintSpecification(final VecApplicationConstraintSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecApproval(final VecApproval aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecAssignmentGroup(final VecAssignmentGroup aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecAssignmentGroupSpecification(final VecAssignmentGroupSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecBaselineSpecification(final VecBaselineSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecBatterySpecification(final VecBatterySpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecBoltMountedFixingSpecification(final VecBoltMountedFixingSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecBoltTerminalRole(final VecBoltTerminalRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecBoltTerminalSpecification(final VecBoltTerminalSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecBooleanValueProperty(final VecBooleanValueProperty aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecBoundingBox(final VecBoundingBox aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecBridgeTerminalRole(final VecBridgeTerminalRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecBridgeTerminalSpecification(final VecBridgeTerminalSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecBuildingBlockPositioning2D(final VecBuildingBlockPositioning2D aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecBuildingBlockPositioning3D(final VecBuildingBlockPositioning3D aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecBuildingBlockSpecification2D(final VecBuildingBlockSpecification2D aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecBuildingBlockSpecification3D(final VecBuildingBlockSpecification3D aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCableDuctOutlet(final VecCableDuctOutlet aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCableDuctRole(final VecCableDuctRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCableDuctSpecification(final VecCableDuctSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCableLeadThrough(final VecCableLeadThrough aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCableLeadThroughReference(final VecCableLeadThroughReference aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCableLeadThroughSpecification(final VecCableLeadThroughSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCableTieRole(final VecCableTieRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCableTieSpecification(final VecCableTieSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCapacitorSpecification(final VecCapacitorSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCartesianDimension(final VecCartesianDimension aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCartesianPoint2D(final VecCartesianPoint2D aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCartesianPoint3D(final VecCartesianPoint3D aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCartesianVector2D(final VecCartesianVector2D aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCartesianVector3D(final VecCartesianVector3D aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCavity(final VecCavity aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityAccessoryRole(final VecCavityAccessoryRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityAccessorySpecification(final VecCavityAccessorySpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityAddOn(final VecCavityAddOn aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityCoupling(final VecCavityCoupling aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityMapping(final VecCavityMapping aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityMounting(final VecCavityMounting aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityMountingDetail(final VecCavityMountingDetail aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityPlugRole(final VecCavityPlugRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityPlugSpecification(final VecCavityPlugSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityReference(final VecCavityReference aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCavitySealRole(final VecCavitySealRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCavitySealSpecification(final VecCavitySealSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCavitySpecification(final VecCavitySpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecChangeDescription(final VecChangeDescription aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCoding(final VecCoding aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecColor(final VecColor aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecComplexProperty(final VecComplexProperty aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecComponentConnector(final VecComponentConnector aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecComponentNode(final VecComponentNode aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecComponentPort(final VecComponentPort aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCompositeUnit(final VecCompositeUnit aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCompositionSpecification(final VecCompositionSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecConductorCurrentInformation(final VecConductorCurrentInformation aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecConductorMaterial(final VecConductorMaterial aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecConnection(final VecConnection aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectionEnd(final VecConnectionEnd aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectionGroup(final VecConnectionGroup aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectionSpecification(final VecConnectionSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectorHousingCapRole(final VecConnectorHousingCapRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectorHousingCapSpecification(final VecConnectorHousingCapSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectorHousingRole(final VecConnectorHousingRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectorHousingSpecification(final VecConnectorHousingSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecContactPoint(final VecContactPoint aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecContactingSpecification(final VecContactingSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecContent(final VecContent aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecContract(final VecContract aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCopyrightInformation(final VecCopyrightInformation aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCoreSpecification(final VecCoreSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCorrugatedPipeSpecification(final VecCorrugatedPipeSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCouplingPoint(final VecCouplingPoint aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCouplingSpecification(final VecCouplingSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCreation(final VecCreation aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecCustomUnit(final VecCustomUnit aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecDateValueProperty(final VecDateValueProperty aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecDefaultDimension(final VecDefaultDimension aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecDefaultDimensionSpecification(final VecDefaultDimensionSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecDimension(final VecDimension aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecDiodeSpecification(final VecDiodeSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecDocumentBasedInstruction(final VecDocumentBasedInstruction aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecDocumentRelatedAssignmentGroup(final VecDocumentRelatedAssignmentGroup aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecDocumentVersion(final VecDocumentVersion aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecDoubleValueProperty(final VecDoubleValueProperty aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecEEComponentRole(final VecEEComponentRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecEEComponentSpecification(final VecEEComponentSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecEdgeMountedFixingSpecification(final VecEdgeMountedFixingSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecExtensionSlot(final VecExtensionSlot aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecExtensionSlotReference(final VecExtensionSlotReference aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecExternalMapping(final VecExternalMapping aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecExternalMappingSpecification(final VecExternalMappingSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecFerriteRole(final VecFerriteRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecFerriteSpecification(final VecFerriteSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecFileBasedInstruction(final VecFileBasedInstruction aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecFillerSpecification(final VecFillerSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecFittingOutlet(final VecFittingOutlet aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecFittingSpecification(final VecFittingSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecFixingRole(final VecFixingRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecFixingSpecification(final VecFixingSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecFlatCoreSpecification(final VecFlatCoreSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecFunctionalAssignmentGroup(final VecFunctionalAssignmentGroup aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecFunctionalRequirement(final VecFunctionalRequirement aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecFuseComponent(final VecFuseComponent aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecFuseSpecification(final VecFuseSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecGeneralTechnicalPartSpecification(final VecGeneralTechnicalPartSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecGeometryNode2D(final VecGeometryNode2D aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecGeometryNode3D(final VecGeometryNode3D aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecGeometrySegment2D(final VecGeometrySegment2D aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecGeometrySegment3D(final VecGeometrySegment3D aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecGrommetRole(final VecGrommetRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecGrommetSpecification(final VecGrommetSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecHarnessDrawingSpecification2D(final VecHarnessDrawingSpecification2D aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecHarnessGeometrySpecification3D(final VecHarnessGeometrySpecification3D aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecHoleMountedFixingSpecification(final VecHoleMountedFixingSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecHousingComponent(final VecHousingComponent aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecHousingComponentReference(final VecHousingComponentReference aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecIECUnit(final VecIECUnit aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecImperialUnit(final VecImperialUnit aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecInsulationSpecification(final VecInsulationSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecIntegerValueProperty(final VecIntegerValueProperty aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecInternalComponentConnection(final VecInternalComponentConnection aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecInternalTerminalConnection(final VecInternalTerminalConnection aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecItemEquivalence(final VecItemEquivalence aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecItemHistoryEntry(final VecItemHistoryEntry aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecLocalGeometrySpecification(final VecLocalGeometrySpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecLocalizedString(final VecLocalizedString aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecLocalizedStringProperty(final VecLocalizedStringProperty aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecLocalizedTypedString(final VecLocalizedTypedString aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecMapping(final VecMapping aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecMappingSpecification(final VecMappingSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecMassInformation(final VecMassInformation aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecMaterial(final VecMaterial aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecMatingDetail(final VecMatingDetail aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecMatingPoint(final VecMatingPoint aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecMeasurePointPosition(final VecMeasurePointPosition aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecMeasurementPoint(final VecMeasurementPoint aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecMeasurementPointReference(final VecMeasurementPointReference aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecModularSlot(final VecModularSlot aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecModularSlotAddOn(final VecModularSlotAddOn aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecModularSlotReference(final VecModularSlotReference aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecModuleFamily(final VecModuleFamily aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecModuleFamilySpecification(final VecModuleFamilySpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecModuleList(final VecModuleList aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecModuleListSpecification(final VecModuleListSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecMultiCavityPlugSpecification(final VecMultiCavityPlugSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecMultiCavitySealSpecification(final VecMultiCavitySealSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecMultiFuseSpecification(final VecMultiFuseSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecNURBSControlPoint(final VecNURBSControlPoint aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecNURBSCurve(final VecNURBSCurve aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecNet(final VecNet aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecNetGroup(final VecNetGroup aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecNetSpecification(final VecNetSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecNetType(final VecNetType aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecNetworkNode(final VecNetworkNode aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecNetworkPort(final VecNetworkPort aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecNodeLocation(final VecNodeLocation aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecNodeMapping(final VecNodeMapping aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecNumericalValue(final VecNumericalValue aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecNumericalValueProperty(final VecNumericalValueProperty aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecOccurrenceOrUsageViewItem2D(final VecOccurrenceOrUsageViewItem2D aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecOccurrenceOrUsageViewItem3D(final VecOccurrenceOrUsageViewItem3D aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecOnPointPlacement(final VecOnPointPlacement aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecOnWayPlacement(final VecOnWayPlacement aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecOpenCavitiesAssignment(final VecOpenCavitiesAssignment aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecOpenWireEndTerminalRole(final VecOpenWireEndTerminalRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecOpenWireEndTerminalSpecification(final VecOpenWireEndTerminalSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecOtherUnit(final VecOtherUnit aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPartOccurrence(final VecPartOccurrence aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPartOrUsageRelatedSpecification(final VecPartOrUsageRelatedSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPartRelation(final VecPartRelation aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPartStructureSpecification(final VecPartStructureSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPartSubstitutionSpecification(final VecPartSubstitutionSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPartUsage(final VecPartUsage aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPartUsageSpecification(final VecPartUsageSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPartVersion(final VecPartVersion aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPartWithSubComponentsRole(final VecPartWithSubComponentsRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPath(final VecPath aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPathSegment(final VecPathSegment aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPermission(final VecPermission aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPerson(final VecPerson aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPinComponent(final VecPinComponent aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPinComponentBehavior(final VecPinComponentBehavior aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPinComponentReference(final VecPinComponentReference aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPinCurrentInformation(final VecPinCurrentInformation aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPinOpticalInformation(final VecPinOpticalInformation aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPinTiming(final VecPinTiming aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPinVoltageInformation(final VecPinVoltageInformation aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPinWireMappingPoint(final VecPinWireMappingPoint aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPinWireMappingSpecification(final VecPinWireMappingSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPlaceableElementRole(final VecPlaceableElementRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPlaceableElementSpecification(final VecPlaceableElementSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPlacementPoint(final VecPlacementPoint aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPlacementPointPosition(final VecPlacementPointPosition aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPlacementPointReference(final VecPlacementPointReference aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPlacementSpecification(final VecPlacementSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPluggableTerminalRole(final VecPluggableTerminalRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPluggableTerminalSpecification(final VecPluggableTerminalSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPotentialDistributorSpecification(final VecPotentialDistributorSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecPowerConsumption(final VecPowerConsumption aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecProject(final VecProject aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecRelaySpecification(final VecRelaySpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecRequirementsConformanceSpecification(final VecRequirementsConformanceSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecRequirementsConformanceStatement(final VecRequirementsConformanceStatement aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecRingTerminalRole(final VecRingTerminalRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecRingTerminalSpecification(final VecRingTerminalSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecRobustnessProperties(final VecRobustnessProperties aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecRouting(final VecRouting aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecRoutingSpecification(final VecRoutingSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSIUnit(final VecSIUnit aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSealedCavitiesAssignment(final VecSealedCavitiesAssignment aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSegmentConnectionPoint(final VecSegmentConnectionPoint aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSegmentCrossSectionArea(final VecSegmentCrossSectionArea aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSegmentLength(final VecSegmentLength aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSegmentLocation(final VecSegmentLocation aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSegmentMapping(final VecSegmentMapping aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSheetOrChapter(final VecSheetOrChapter aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecShieldSpecification(final VecShieldSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecShrinkableTubeSpecification(final VecShrinkableTubeSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSignal(final VecSignal aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSignalSpecification(final VecSignalSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSimpleValueProperty(final VecSimpleValueProperty aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSize(final VecSize aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSlot(final VecSlot aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSlotCoupling(final VecSlotCoupling aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSlotLayout(final VecSlotLayout aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSlotMapping(final VecSlotMapping aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSlotReference(final VecSlotReference aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSlotSpecification(final VecSlotSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSoundDampingClass(final VecSoundDampingClass aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSpecificRole(final VecSpecificRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSpliceTerminalRole(final VecSpliceTerminalRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSpliceTerminalSpecification(final VecSpliceTerminalSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecStripeSpecification(final VecStripeSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecSwitchingState(final VecSwitchingState aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTapeRole(final VecTapeRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTapeSpecification(final VecTapeSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTemperatureInformation(final VecTemperatureInformation aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalCurrentInformation(final VecTerminalCurrentInformation aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalPairing(final VecTerminalPairing aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalPairingSpecification(final VecTerminalPairingSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalReception(final VecTerminalReception aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalReceptionReference(final VecTerminalReceptionReference aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalReceptionSpecification(final VecTerminalReceptionSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalRole(final VecTerminalRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalSpecification(final VecTerminalSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalType(final VecTerminalType aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTextBasedInstruction(final VecTextBasedInstruction aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTolerance(final VecTolerance aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTopologyBendingRestriction(final VecTopologyBendingRestriction aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTopologyBendingRestrictionSpecification(final VecTopologyBendingRestrictionSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTopologyGroupSpecification(final VecTopologyGroupSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTopologyMappingSpecification(final VecTopologyMappingSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTopologyNode(final VecTopologyNode aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTopologySegment(final VecTopologySegment aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTopologySpecification(final VecTopologySpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTopologyZone(final VecTopologyZone aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTopologyZoneSpecification(final VecTopologyZoneSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTransformation2D(final VecTransformation2D aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTransformation3D(final VecTransformation3D aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecTubeSpecification(final VecTubeSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecUSUnit(final VecUSUnit aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecUsageConstraint(final VecUsageConstraint aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecUsageConstraintSpecification(final VecUsageConstraintSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecUsageNode(final VecUsageNode aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecUsageNodeSpecification(final VecUsageNodeSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecValueRange(final VecValueRange aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecValueRangeProperty(final VecValueRangeProperty aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantCode(final VecVariantCode aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantCodeSpecification(final VecVariantCodeSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantConfiguration(final VecVariantConfiguration aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantConfigurationSpecification(final VecVariantConfigurationSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantGroup(final VecVariantGroup aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantGroupSpecification(final VecVariantGroupSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantStructureNode(final VecVariantStructureNode aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantStructureSpecification(final VecVariantStructureSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireElement(final VecWireElement aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireElementReference(final VecWireElementReference aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireElementSpecification(final VecWireElementSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireEnd(final VecWireEnd aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireEndAccessoryRole(final VecWireEndAccessoryRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireEndAccessorySpecification(final VecWireEndAccessorySpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireGroupSpecification(final VecWireGroupSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireGrouping(final VecWireGrouping aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireGroupingSpecification(final VecWireGroupingSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireLength(final VecWireLength aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireMounting(final VecWireMounting aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireMountingDetail(final VecWireMountingDetail aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireProtectionRole(final VecWireProtectionRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireProtectionSpecification(final VecWireProtectionSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireReception(final VecWireReception aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireReceptionAddOn(final VecWireReceptionAddOn aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireReceptionReference(final VecWireReceptionReference aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireReceptionSpecification(final VecWireReceptionSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireRole(final VecWireRole aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireSpecification(final VecWireSpecification aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecWireType(final VecWireType aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecZoneAssignment(final VecZoneAssignment aBean) {
        return apply(aBean);
    }

    @Override
    public O visitVecZoneCoverage(final VecZoneCoverage aBean) {
        return apply(aBean);
    }

    private <T extends Identifiable> O apply(final T element) {
        return func.apply((I) element);
    }

}
