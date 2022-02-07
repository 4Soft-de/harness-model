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

/**
 * Base implementation for a strict {@link Visitor}. Strict means, that the
 * visitor class will throw an {@link UnsupportedOperationException} if it
 * encounters a class that is not handled explicitly, by overriding the
 * corresponding methods.
 *
 * @author Johannes Becker
 *
 * @param <R> Class of the visitor
 */
public class StrictBaseVisitor<R> implements Visitor<R, RuntimeException> {

    /**
     * Default implementation for creating an error message in case of visiting
     * classes that are not explicitly handled.
     *
     * @param aBean Object to get class name from
     * @return Never null String containing the error message for the given object
     */
    protected String getErrorMessage(final Object aBean) {
        return "Encountered unhandled class '" + aBean.getClass().getName()
                + "' in visitor implementation: "
                + this.getClass().getName();
    }

    @Override
    public R visitVecAbrasionResistanceClass(final VecAbrasionResistanceClass aBean) {

        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecAliasIdentification(final VecAliasIdentification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecAntennaSpecification(final VecAntennaSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecApproval(final VecApproval aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBSplineCurve(final VecBSplineCurve aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBatterySpecification(final VecBatterySpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBoltMountedFixingSpecification(final VecBoltMountedFixingSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBooleanValueProperty(final VecBooleanValueProperty aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBoundingBox(final VecBoundingBox aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBuildingBlockPositioning2D(final VecBuildingBlockPositioning2D aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBuildingBlockPositioning3D(final VecBuildingBlockPositioning3D aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBuildingBlockSpecification2D(final VecBuildingBlockSpecification2D aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBuildingBlockSpecification3D(final VecBuildingBlockSpecification3D aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCableDuctOutlet(final VecCableDuctOutlet aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCableDuctRole(final VecCableDuctRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCableDuctSpecification(final VecCableDuctSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCableLeadThrough(final VecCableLeadThrough aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCableTieRole(final VecCableTieRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCableTieSpecification(final VecCableTieSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCartesianDimension(final VecCartesianDimension aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCartesianPoint2D(final VecCartesianPoint2D aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCartesianPoint3D(final VecCartesianPoint3D aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCartesianVector2D(final VecCartesianVector2D aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCartesianVector3D(final VecCartesianVector3D aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavity(final VecCavity aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityAccessoryRole(final VecCavityAccessoryRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityAccessorySpecification(final VecCavityAccessorySpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityAddOn(final VecCavityAddOn aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityCoupling(final VecCavityCoupling aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityMapping(final VecCavityMapping aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityMounting(final VecCavityMounting aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityMountingDetail(final VecCavityMountingDetail aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityPlugRole(final VecCavityPlugRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityPlugSpecification(final VecCavityPlugSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityReference(final VecCavityReference aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavitySealRole(final VecCavitySealRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavitySealSpecification(final VecCavitySealSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavitySpecification(final VecCavitySpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecChangeDescription(final VecChangeDescription aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCoding(final VecCoding aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecColor(final VecColor aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCompatibilitySpecification(final VecCompatibilitySpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCompatibilityStatement(final VecCompatibilityStatement aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecComponentConnector(final VecComponentConnector aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecComponentNode(final VecComponentNode aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecComponentPort(final VecComponentPort aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCompositeUnit(final VecCompositeUnit aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCompositionSpecification(final VecCompositionSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConductorCurrentInformation(final VecConductorCurrentInformation aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConductorMaterial(final VecConductorMaterial aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConformanceClass(final VecConformanceClass aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConnection(final VecConnection aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConnectionEnd(final VecConnectionEnd aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConnectionGroup(final VecConnectionGroup aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConnectionSpecification(final VecConnectionSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConnectorHousingCapRole(final VecConnectorHousingCapRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConnectorHousingCapSpecification(final VecConnectorHousingCapSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConnectorHousingRole(final VecConnectorHousingRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConnectorHousingSpecification(final VecConnectorHousingSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecContactPoint(final VecContactPoint aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecContactSystem(final VecContactSystem aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecContactSystemSpecification(final VecContactSystemSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecContactingSpecification(final VecContactingSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecContent(final VecContent aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecContract(final VecContract aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCopyrightInformation(final VecCopyrightInformation aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCoreSpecification(final VecCoreSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCorrugatedPipeSpecification(final VecCorrugatedPipeSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCouplingPoint(final VecCouplingPoint aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCouplingSpecification(final VecCouplingSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCreation(final VecCreation aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCustomUnit(final VecCustomUnit aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecDateValueProperty(final VecDateValueProperty aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecDefaultDimension(final VecDefaultDimension aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecDefaultDimensionSpecification(final VecDefaultDimensionSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecDimension(final VecDimension aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecDocumentBasedInstruction(final VecDocumentBasedInstruction aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecDocumentVersion(final VecDocumentVersion aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecDoubleValueProperty(final VecDoubleValueProperty aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecEEComponentRole(final VecEEComponentRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecEEComponentSpecification(final VecEEComponentSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecEdgeMountedFixingSpecification(final VecEdgeMountedFixingSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecExtensionSlot(final VecExtensionSlot aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecExtensionSlotReference(final VecExtensionSlotReference aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecExternalMapping(final VecExternalMapping aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecExternalMappingSpecification(final VecExternalMappingSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFileBasedInstruction(final VecFileBasedInstruction aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFillerSpecification(final VecFillerSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFittingOutlet(final VecFittingOutlet aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFittingSpecification(final VecFittingSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFixingRole(final VecFixingRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFixingSpecification(final VecFixingSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFlatCoreSpecification(final VecFlatCoreSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFuseComponent(final VecFuseComponent aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFuseSpecification(final VecFuseSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecGeneralTechnicalPartSpecification(final VecGeneralTechnicalPartSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecGeometryNode2D(final VecGeometryNode2D aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecGeometryNode3D(final VecGeometryNode3D aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecGeometrySegment2D(final VecGeometrySegment2D aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecGeometrySegment3D(final VecGeometrySegment3D aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecGrommetRole(final VecGrommetRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecGrommetSpecification(final VecGrommetSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecHarnessDrawingSpecification2D(final VecHarnessDrawingSpecification2D aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecHarnessGeometrySpecification3D(final VecHarnessGeometrySpecification3D aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecHoleMountedFixingSpecification(final VecHoleMountedFixingSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecHousingComponent(final VecHousingComponent aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecHousingComponentReference(final VecHousingComponentReference aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecIECUnit(final VecIECUnit aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecImperialUnit(final VecImperialUnit aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecInsulationSpecification(final VecInsulationSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecIntegerValueProperty(final VecIntegerValueProperty aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecInternalComponentConnection(final VecInternalComponentConnection aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecInternalTerminalConnection(final VecInternalTerminalConnection aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecItemEquivalence(final VecItemEquivalence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecItemHistoryEntry(final VecItemHistoryEntry aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecLocalizedString(final VecLocalizedString aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecLocalizedStringProperty(final VecLocalizedStringProperty aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecLocalizedTypedString(final VecLocalizedTypedString aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMapping(final VecMapping aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMappingSpecification(final VecMappingSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMassInformation(final VecMassInformation aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMaterial(final VecMaterial aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMatingDetail(final VecMatingDetail aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMatingPoint(final VecMatingPoint aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMeasurementPoint(final VecMeasurementPoint aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMeasurementPointReference(final VecMeasurementPointReference aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecModularSlot(final VecModularSlot aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecModularSlotAddOn(final VecModularSlotAddOn aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecModularSlotReference(final VecModularSlotReference aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecModuleFamily(final VecModuleFamily aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecModuleFamilySpecification(final VecModuleFamilySpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecModuleList(final VecModuleList aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecModuleListSpecification(final VecModuleListSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMultiCavityPlugSpecification(final VecMultiCavityPlugSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMultiCavitySealSpecification(final VecMultiCavitySealSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMultiFuseSpecification(final VecMultiFuseSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNet(final VecNet aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNetGroup(final VecNetGroup aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNetSpecification(final VecNetSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNetType(final VecNetType aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNetworkNode(final VecNetworkNode aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNetworkPort(final VecNetworkPort aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNodeLocation(final VecNodeLocation aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNumericalValue(final VecNumericalValue aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNumericalValueProperty(final VecNumericalValueProperty aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecOccurrenceOrUsageViewItem2D(final VecOccurrenceOrUsageViewItem2D aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecOccurrenceOrUsageViewItem3D(final VecOccurrenceOrUsageViewItem3D aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecOnPointPlacement(final VecOnPointPlacement aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecOnWayPlacement(final VecOnWayPlacement aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecOpenCavitiesAssignment(final VecOpenCavitiesAssignment aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecOtherUnit(final VecOtherUnit aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPartOccurrence(final VecPartOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPartOrUsageRelatedSpecification(final VecPartOrUsageRelatedSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPartRelation(final VecPartRelation aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPartStructureSpecification(final VecPartStructureSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPartSubstitutionSpecification(final VecPartSubstitutionSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPartUsage(final VecPartUsage aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPartUsageSpecification(final VecPartUsageSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPartVersion(final VecPartVersion aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPartWithSubComponentsRole(final VecPartWithSubComponentsRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPath(final VecPath aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPathSegment(final VecPathSegment aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPermission(final VecPermission aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPerson(final VecPerson aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPinComponent(final VecPinComponent aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPinComponentBehavior(final VecPinComponentBehavior aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPinComponentReference(final VecPinComponentReference aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPinCurrentInformation(final VecPinCurrentInformation aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPinOpticalInformation(final VecPinOpticalInformation aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPinTiming(final VecPinTiming aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPinVoltageInformation(final VecPinVoltageInformation aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPlaceableElementRole(final VecPlaceableElementRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPlaceableElementSpecification(final VecPlaceableElementSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPlacementPoint(final VecPlacementPoint aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPlacementPointReference(final VecPlacementPointReference aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPlacementSpecification(final VecPlacementSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPluggableTerminalRole(final VecPluggableTerminalRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPluggableTerminalSpecification(final VecPluggableTerminalSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPotentialDistributorSpecification(final VecPotentialDistributorSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPowerConsumption(final VecPowerConsumption aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecProject(final VecProject aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecRelaySpecification(final VecRelaySpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecRingTerminalRole(final VecRingTerminalRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecRingTerminalSpecification(final VecRingTerminalSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecRobustnessProperties(final VecRobustnessProperties aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecRouting(final VecRouting aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecRoutingSpecification(final VecRoutingSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSIUnit(final VecSIUnit aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSealedCavitiesAssignment(final VecSealedCavitiesAssignment aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSealingClass(final VecSealingClass aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSegmentConnectionPoint(final VecSegmentConnectionPoint aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSegmentCrossSectionArea(final VecSegmentCrossSectionArea aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSegmentLength(final VecSegmentLength aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSegmentLocation(final VecSegmentLocation aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSheetOrChapter(final VecSheetOrChapter aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecShieldSpecification(final VecShieldSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecShrinkableTubeSpecification(final VecShrinkableTubeSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSignal(final VecSignal aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSignalSpecification(final VecSignalSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSimpleValueProperty(final VecSimpleValueProperty aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSize(final VecSize aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSlot(final VecSlot aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSlotCoupling(final VecSlotCoupling aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSlotLayout(final VecSlotLayout aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSlotMapping(final VecSlotMapping aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSlotReference(final VecSlotReference aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSlotSpecification(final VecSlotSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSoundDampingClass(final VecSoundDampingClass aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSpecificRole(final VecSpecificRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSpliceTerminalRole(final VecSpliceTerminalRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSpliceTerminalSpecification(final VecSpliceTerminalSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecStripeSpecification(final VecStripeSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSwitchingState(final VecSwitchingState aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTapeSpecification(final VecTapeSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTemperatureInformation(final VecTemperatureInformation aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTerminalCurrentInformation(final VecTerminalCurrentInformation aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTerminalReception(final VecTerminalReception aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTerminalReceptionReference(final VecTerminalReceptionReference aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTerminalReceptionSpecification(final VecTerminalReceptionSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTerminalRole(final VecTerminalRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTerminalSpecification(final VecTerminalSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTerminalType(final VecTerminalType aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTextBasedInstruction(final VecTextBasedInstruction aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTolerance(final VecTolerance aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTopologyGroupSpecification(final VecTopologyGroupSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTopologyNode(final VecTopologyNode aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTopologySegment(final VecTopologySegment aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTopologySpecification(final VecTopologySpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTransformation2D(final VecTransformation2D aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTransformation3D(final VecTransformation3D aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTubeSpecification(final VecTubeSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecUSUnit(final VecUSUnit aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecUsageConstraint(final VecUsageConstraint aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecUsageConstraintSpecification(final VecUsageConstraintSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecUsageNode(final VecUsageNode aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecUsageNodeSpecification(final VecUsageNodeSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecValueRange(final VecValueRange aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecValueRangeProperty(final VecValueRangeProperty aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecVariantCode(final VecVariantCode aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecVariantCodeSpecification(final VecVariantCodeSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecVariantConfiguration(final VecVariantConfiguration aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecVariantConfigurationSpecification(final VecVariantConfigurationSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecVariantGroup(final VecVariantGroup aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecVariantGroupSpecification(final VecVariantGroupSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireElement(final VecWireElement aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireElementReference(final VecWireElementReference aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireElementSpecification(final VecWireElementSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireEnd(final VecWireEnd aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireEndAccessoryRole(final VecWireEndAccessoryRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireEndAccessorySpecification(final VecWireEndAccessorySpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireGroupSpecification(final VecWireGroupSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireGrouping(final VecWireGrouping aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireGroupingSpecification(final VecWireGroupingSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireLength(final VecWireLength aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireMounting(final VecWireMounting aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireMountingDetail(final VecWireMountingDetail aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireProtectionRole(final VecWireProtectionRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireProtectionSpecification(final VecWireProtectionSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireReception(final VecWireReception aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireReceptionReference(final VecWireReceptionReference aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireReceptionSpecification(final VecWireReceptionSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireRole(final VecWireRole aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireSpecification(final VecWireSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireType(final VecWireType aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecZone(final VecZone aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

}
