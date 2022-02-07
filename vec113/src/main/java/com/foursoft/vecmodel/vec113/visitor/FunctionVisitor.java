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
package com.foursoft.vecmodel.vec113.visitor;

import com.foursoft.vecmodel.vec113.*;
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
    public O visitVecAbrasionResistanceClass(VecAbrasionResistanceClass aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecAliasIdentification(VecAliasIdentification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecAntennaSpecification(VecAntennaSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecApproval(VecApproval aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecBSplineCurve(VecBSplineCurve aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecBatterySpecification(VecBatterySpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecBoltMountedFixingSpecification(VecBoltMountedFixingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecBooleanValueProperty(VecBooleanValueProperty aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecBoundingBox(VecBoundingBox aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecBuildingBlockPositioning2D(VecBuildingBlockPositioning2D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecBuildingBlockPositioning3D(VecBuildingBlockPositioning3D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecBuildingBlockSpecification2D(VecBuildingBlockSpecification2D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecBuildingBlockSpecification3D(VecBuildingBlockSpecification3D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCableDuctOutlet(VecCableDuctOutlet aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCableDuctRole(VecCableDuctRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCableDuctSpecification(VecCableDuctSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCableLeadThrough(VecCableLeadThrough aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCableTieRole(VecCableTieRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCableTieSpecification(VecCableTieSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCartesianDimension(VecCartesianDimension aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCartesianPoint2D(VecCartesianPoint2D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCartesianPoint3D(VecCartesianPoint3D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCartesianVector2D(VecCartesianVector2D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCartesianVector3D(VecCartesianVector3D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavity(VecCavity aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityAccessoryRole(VecCavityAccessoryRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityAccessorySpecification(VecCavityAccessorySpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityAddOn(VecCavityAddOn aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityCoupling(VecCavityCoupling aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityMapping(VecCavityMapping aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityMounting(VecCavityMounting aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityMountingDetail(VecCavityMountingDetail aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityPlugRole(VecCavityPlugRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityPlugSpecification(VecCavityPlugSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavityReference(VecCavityReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavitySealRole(VecCavitySealRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavitySealSpecification(VecCavitySealSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCavitySpecification(VecCavitySpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecChangeDescription(VecChangeDescription aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCoding(VecCoding aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecColor(VecColor aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCompatibilitySpecification(VecCompatibilitySpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCompatibilityStatement(VecCompatibilityStatement aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecComponentConnector(VecComponentConnector aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecComponentNode(VecComponentNode aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecComponentPort(VecComponentPort aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCompositeUnit(VecCompositeUnit aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCompositionSpecification(VecCompositionSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConductorCurrentInformation(VecConductorCurrentInformation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConductorMaterial(VecConductorMaterial aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConformanceClass(VecConformanceClass aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConnection(VecConnection aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectionEnd(VecConnectionEnd aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectionGroup(VecConnectionGroup aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectionSpecification(VecConnectionSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectorHousingCapRole(VecConnectorHousingCapRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectorHousingCapSpecification(VecConnectorHousingCapSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectorHousingRole(VecConnectorHousingRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecConnectorHousingSpecification(VecConnectorHousingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecContactPoint(VecContactPoint aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecContactSystem(VecContactSystem aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecContactSystemSpecification(VecContactSystemSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecContactingSpecification(VecContactingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecContent(VecContent aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecContract(VecContract aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCopyrightInformation(VecCopyrightInformation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCoreSpecification(VecCoreSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCorrugatedPipeSpecification(VecCorrugatedPipeSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCouplingPoint(VecCouplingPoint aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCouplingSpecification(VecCouplingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCreation(VecCreation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecCustomUnit(VecCustomUnit aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecDateValueProperty(VecDateValueProperty aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecDefaultDimension(VecDefaultDimension aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecDefaultDimensionSpecification(VecDefaultDimensionSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecDimension(VecDimension aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecDocumentBasedInstruction(VecDocumentBasedInstruction aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecDocumentVersion(VecDocumentVersion aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecDoubleValueProperty(VecDoubleValueProperty aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecEEComponentRole(VecEEComponentRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecEEComponentSpecification(VecEEComponentSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecEdgeMountedFixingSpecification(VecEdgeMountedFixingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecExtensionSlot(VecExtensionSlot aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecExtensionSlotReference(VecExtensionSlotReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecExternalMapping(VecExternalMapping aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecExternalMappingSpecification(VecExternalMappingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecFileBasedInstruction(VecFileBasedInstruction aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecFillerSpecification(VecFillerSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecFittingOutlet(VecFittingOutlet aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecFittingSpecification(VecFittingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecFixingRole(VecFixingRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecFixingSpecification(VecFixingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecFlatCoreSpecification(VecFlatCoreSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecFuseComponent(VecFuseComponent aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecFuseSpecification(VecFuseSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecGeneralTechnicalPartSpecification(VecGeneralTechnicalPartSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecGeometryNode2D(VecGeometryNode2D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecGeometryNode3D(VecGeometryNode3D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecGeometrySegment2D(VecGeometrySegment2D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecGeometrySegment3D(VecGeometrySegment3D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecGrommetRole(VecGrommetRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecGrommetSpecification(VecGrommetSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecHarnessDrawingSpecification2D(VecHarnessDrawingSpecification2D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecHarnessGeometrySpecification3D(VecHarnessGeometrySpecification3D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecHoleMountedFixingSpecification(VecHoleMountedFixingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecHousingComponent(VecHousingComponent aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecHousingComponentReference(VecHousingComponentReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecIECUnit(VecIECUnit aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecImperialUnit(VecImperialUnit aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecInsulationSpecification(VecInsulationSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecIntegerValueProperty(VecIntegerValueProperty aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecInternalComponentConnection(VecInternalComponentConnection aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecInternalTerminalConnection(VecInternalTerminalConnection aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecItemEquivalence(VecItemEquivalence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecItemHistoryEntry(VecItemHistoryEntry aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecLocalizedString(VecLocalizedString aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecLocalizedStringProperty(VecLocalizedStringProperty aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecLocalizedTypedString(VecLocalizedTypedString aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMapping(VecMapping aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMappingSpecification(VecMappingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMassInformation(VecMassInformation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMaterial(VecMaterial aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMatingDetail(VecMatingDetail aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMatingPoint(VecMatingPoint aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMeasurementPoint(VecMeasurementPoint aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMeasurementPointReference(VecMeasurementPointReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecModularSlot(VecModularSlot aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecModularSlotAddOn(VecModularSlotAddOn aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecModularSlotReference(VecModularSlotReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecModuleFamily(VecModuleFamily aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecModuleFamilySpecification(VecModuleFamilySpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecModuleList(VecModuleList aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecModuleListSpecification(VecModuleListSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMultiCavityPlugSpecification(VecMultiCavityPlugSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMultiCavitySealSpecification(VecMultiCavitySealSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecMultiFuseSpecification(VecMultiFuseSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecNet(VecNet aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecNetGroup(VecNetGroup aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecNetSpecification(VecNetSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecNetType(VecNetType aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecNetworkNode(VecNetworkNode aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecNetworkPort(VecNetworkPort aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecNodeLocation(VecNodeLocation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecNumericalValue(VecNumericalValue aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecNumericalValueProperty(VecNumericalValueProperty aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecOccurrenceOrUsageViewItem2D(VecOccurrenceOrUsageViewItem2D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecOccurrenceOrUsageViewItem3D(VecOccurrenceOrUsageViewItem3D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecOnPointPlacement(VecOnPointPlacement aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecOnWayPlacement(VecOnWayPlacement aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecOpenCavitiesAssignment(VecOpenCavitiesAssignment aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecOtherUnit(VecOtherUnit aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPartOccurrence(VecPartOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPartOrUsageRelatedSpecification(VecPartOrUsageRelatedSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPartRelation(VecPartRelation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPartStructureSpecification(VecPartStructureSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPartSubstitutionSpecification(VecPartSubstitutionSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPartUsage(VecPartUsage aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPartUsageSpecification(VecPartUsageSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPartVersion(VecPartVersion aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPartWithSubComponentsRole(VecPartWithSubComponentsRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPath(VecPath aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPathSegment(VecPathSegment aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPermission(VecPermission aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPerson(VecPerson aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPinComponent(VecPinComponent aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPinComponentBehavior(VecPinComponentBehavior aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPinComponentReference(VecPinComponentReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPinCurrentInformation(VecPinCurrentInformation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPinOpticalInformation(VecPinOpticalInformation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPinTiming(VecPinTiming aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPinVoltageInformation(VecPinVoltageInformation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPlaceableElementRole(VecPlaceableElementRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPlaceableElementSpecification(VecPlaceableElementSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPlacementPoint(VecPlacementPoint aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPlacementPointReference(VecPlacementPointReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPlacementSpecification(VecPlacementSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPluggableTerminalRole(VecPluggableTerminalRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPluggableTerminalSpecification(VecPluggableTerminalSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPotentialDistributorSpecification(VecPotentialDistributorSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecPowerConsumption(VecPowerConsumption aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecProject(VecProject aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecRelaySpecification(VecRelaySpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecRingTerminalRole(VecRingTerminalRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecRingTerminalSpecification(VecRingTerminalSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecRobustnessProperties(VecRobustnessProperties aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecRouting(VecRouting aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecRoutingSpecification(VecRoutingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSIUnit(VecSIUnit aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSealedCavitiesAssignment(VecSealedCavitiesAssignment aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSealingClass(VecSealingClass aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSegmentConnectionPoint(VecSegmentConnectionPoint aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSegmentCrossSectionArea(VecSegmentCrossSectionArea aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSegmentLength(VecSegmentLength aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSegmentLocation(VecSegmentLocation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSheetOrChapter(VecSheetOrChapter aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecShieldSpecification(VecShieldSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecShrinkableTubeSpecification(VecShrinkableTubeSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSignal(VecSignal aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSignalSpecification(VecSignalSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSimpleValueProperty(VecSimpleValueProperty aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSize(VecSize aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSlot(VecSlot aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSlotCoupling(VecSlotCoupling aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSlotLayout(VecSlotLayout aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSlotMapping(VecSlotMapping aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSlotReference(VecSlotReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSlotSpecification(VecSlotSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSoundDampingClass(VecSoundDampingClass aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSpecificRole(VecSpecificRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSpliceTerminalRole(VecSpliceTerminalRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSpliceTerminalSpecification(VecSpliceTerminalSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecStripeSpecification(VecStripeSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecSwitchingState(VecSwitchingState aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTapeSpecification(VecTapeSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTemperatureInformation(VecTemperatureInformation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalCurrentInformation(VecTerminalCurrentInformation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalReception(VecTerminalReception aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalReceptionReference(VecTerminalReceptionReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalReceptionSpecification(VecTerminalReceptionSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalRole(VecTerminalRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalSpecification(VecTerminalSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTerminalType(VecTerminalType aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTextBasedInstruction(VecTextBasedInstruction aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTolerance(VecTolerance aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTopologyGroupSpecification(VecTopologyGroupSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTopologyNode(VecTopologyNode aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTopologySegment(VecTopologySegment aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTopologySpecification(VecTopologySpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTransformation2D(VecTransformation2D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTransformation3D(VecTransformation3D aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecTubeSpecification(VecTubeSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecUSUnit(VecUSUnit aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecUsageConstraint(VecUsageConstraint aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecUsageConstraintSpecification(VecUsageConstraintSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecUsageNode(VecUsageNode aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecUsageNodeSpecification(VecUsageNodeSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecValueRange(VecValueRange aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecValueRangeProperty(VecValueRangeProperty aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantCode(VecVariantCode aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantCodeSpecification(VecVariantCodeSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantConfiguration(VecVariantConfiguration aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantConfigurationSpecification(VecVariantConfigurationSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantGroup(VecVariantGroup aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecVariantGroupSpecification(VecVariantGroupSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireElement(VecWireElement aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireElementReference(VecWireElementReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireElementSpecification(VecWireElementSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireEnd(VecWireEnd aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireEndAccessoryRole(VecWireEndAccessoryRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireEndAccessorySpecification(VecWireEndAccessorySpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireGroupSpecification(VecWireGroupSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireGrouping(VecWireGrouping aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireGroupingSpecification(VecWireGroupingSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireLength(VecWireLength aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireMounting(VecWireMounting aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireMountingDetail(VecWireMountingDetail aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireProtectionRole(VecWireProtectionRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireProtectionSpecification(VecWireProtectionSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireReception(VecWireReception aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireReceptionReference(VecWireReceptionReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireReceptionSpecification(VecWireReceptionSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireRole(VecWireRole aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireSpecification(VecWireSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecWireType(VecWireType aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitVecZone(VecZone aBean) throws RuntimeException {
        return apply(aBean);
    }

    private <T extends Identifiable> O apply(final T element) {
        return func.apply((I) element);
    }

}
