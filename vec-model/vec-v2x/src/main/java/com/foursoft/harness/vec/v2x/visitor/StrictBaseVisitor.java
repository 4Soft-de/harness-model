/*-
 * ========================LICENSE_START=================================
 * vec-v2x
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
package com.foursoft.harness.vec.v2x.visitor;

import com.foursoft.harness.vec.v2x.*;

/**
 * Base implementation for a strict {@link Visitor}. Strict means, that the
 * visitor class will throw an {@link UnsupportedOperationException} if it
 * encounters a class that is not handled explicitly, by overriding the
 * corresponding methods.
 *
 * @param <R> Class of the visitor
 * @author Johannes Becker
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
        return String.format("Encountered unhandled class '%s' in visitor implementation: %s",
                aBean.getClass().getName(), getClass().getName());
    }

    @Override
    public R visitVecAliasIdentification(final VecAliasIdentification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecAntennaSpecification(final VecAntennaSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecApplicationConstraint(final VecApplicationConstraint aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecApplicationConstraintSpecification(final VecApplicationConstraintSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecApproval(final VecApproval aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecAssignmentGroup(final VecAssignmentGroup aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecAssignmentGroupSpecification(final VecAssignmentGroupSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBaselineSpecification(final VecBaselineSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBatterySpecification(final VecBatterySpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBoltMountedFixingSpecification(final VecBoltMountedFixingSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBoltTerminalRole(final VecBoltTerminalRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBoltTerminalSpecification(final VecBoltTerminalSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBooleanValueProperty(final VecBooleanValueProperty aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBoundingBox(final VecBoundingBox aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBridgeTerminalRole(final VecBridgeTerminalRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBridgeTerminalSpecification(final VecBridgeTerminalSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBuildingBlockPositioning2D(final VecBuildingBlockPositioning2D aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBuildingBlockPositioning3D(final VecBuildingBlockPositioning3D aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBuildingBlockSpecification2D(final VecBuildingBlockSpecification2D aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecBuildingBlockSpecification3D(final VecBuildingBlockSpecification3D aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCableDuctOutlet(final VecCableDuctOutlet aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCableDuctRole(final VecCableDuctRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCableDuctSpecification(final VecCableDuctSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCableLeadThrough(final VecCableLeadThrough aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCableLeadThroughOutlet(VecCableLeadThroughOutlet aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCableLeadThroughReference(final VecCableLeadThroughReference aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCableLeadThroughSpecification(final VecCableLeadThroughSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCableTieRole(final VecCableTieRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCableTieSpecification(final VecCableTieSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCapacitorSpecification(final VecCapacitorSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCartesianDimension(final VecCartesianDimension aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCartesianPoint2D(final VecCartesianPoint2D aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCartesianPoint3D(final VecCartesianPoint3D aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCartesianVector2D(final VecCartesianVector2D aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCartesianVector3D(final VecCartesianVector3D aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavity(final VecCavity aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityAccessoryRole(final VecCavityAccessoryRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityAccessorySpecification(final VecCavityAccessorySpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityAddOn(final VecCavityAddOn aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityCoupling(final VecCavityCoupling aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityMapping(final VecCavityMapping aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityMounting(final VecCavityMounting aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityMountingDetail(final VecCavityMountingDetail aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityPlugRole(final VecCavityPlugRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityPlugSpecification(final VecCavityPlugSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityPositionDetail(VecCavityPositionDetail aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavityReference(final VecCavityReference aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavitySealRole(final VecCavitySealRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavitySealSpecification(final VecCavitySealSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCavitySpecification(final VecCavitySpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecChangeDescription(final VecChangeDescription aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCoding(final VecCoding aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecColor(final VecColor aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecComplexProperty(final VecComplexProperty aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecComponentConnector(final VecComponentConnector aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecComponentNode(final VecComponentNode aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecComponentPort(final VecComponentPort aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecComponentPortViewItem(VecComponentPortViewItem aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCompositeUnit(final VecCompositeUnit aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCompositionSpecification(final VecCompositionSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConductorCurrentInformation(final VecConductorCurrentInformation aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConductorMaterial(final VecConductorMaterial aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConfigurationConstraint(VecConfigurationConstraint aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConnection(final VecConnection aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConnectionEnd(final VecConnectionEnd aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConnectionGroup(final VecConnectionGroup aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConnectionNodeViewItem(VecConnectionNodeViewItem aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConnectionSpecification(final VecConnectionSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConnectionViewSpecification(VecConnectionViewSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConnectorHousingCapRole(final VecConnectorHousingCapRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConnectorHousingCapSpecification(final VecConnectorHousingCapSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConnectorHousingRole(final VecConnectorHousingRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecConnectorHousingSpecification(final VecConnectorHousingSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecContactPoint(final VecContactPoint aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecContactingSpecification(final VecContactingSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecContent(final VecContent aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecContract(final VecContract aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCopyrightInformation(final VecCopyrightInformation aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCoreSpecification(final VecCoreSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCorrugatedPipeSpecification(final VecCorrugatedPipeSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCouplingPoint(final VecCouplingPoint aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCouplingSpecification(final VecCouplingSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCreation(final VecCreation aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecCustomUnit(final VecCustomUnit aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecDateValueProperty(final VecDateValueProperty aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecDefaultDimension(final VecDefaultDimension aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecDefaultDimensionSpecification(final VecDefaultDimensionSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecDimension(final VecDimension aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecDiodeSpecification(final VecDiodeSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecDocumentBasedInstruction(final VecDocumentBasedInstruction aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecDocumentRelatedAssignmentGroup(final VecDocumentRelatedAssignmentGroup aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecDocumentVersion(final VecDocumentVersion aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecDoubleValueProperty(final VecDoubleValueProperty aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecEEComponentRole(final VecEEComponentRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecEEComponentSpecification(final VecEEComponentSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecEdgeMountedFixingSpecification(final VecEdgeMountedFixingSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecExtensionSlot(final VecExtensionSlot aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecExtensionSlotReference(final VecExtensionSlotReference aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecExternalMapping(final VecExternalMapping aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecExternalMappingSpecification(final VecExternalMappingSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFerriteRole(final VecFerriteRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFerriteSpecification(final VecFerriteSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFileBasedInstruction(final VecFileBasedInstruction aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFillerSpecification(final VecFillerSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFittingOutlet(final VecFittingOutlet aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFittingSpecification(final VecFittingSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFixingRole(final VecFixingRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFixingSpecification(final VecFixingSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFlatCoreSpecification(final VecFlatCoreSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFunctionalAssignmentGroup(final VecFunctionalAssignmentGroup aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFunctionalRequirement(final VecFunctionalRequirement aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFunctionalStructureNode(final VecFunctionalStructureNode aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFunctionalStructureSpecification(final VecFunctionalStructureSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFuseComponent(final VecFuseComponent aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecFuseSpecification(final VecFuseSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecGeneralTechnicalPartSpecification(final VecGeneralTechnicalPartSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecGeometryNode2D(final VecGeometryNode2D aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecGeometryNode3D(final VecGeometryNode3D aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecGeometrySegment2D(final VecGeometrySegment2D aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecGeometrySegment3D(final VecGeometrySegment3D aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecGrommetRole(final VecGrommetRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecGrommetSpecification(final VecGrommetSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecHarnessDrawingSpecification2D(final VecHarnessDrawingSpecification2D aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecHarnessGeometrySpecification3D(final VecHarnessGeometrySpecification3D aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecHoleMountedFixingSpecification(final VecHoleMountedFixingSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecHousingComponent(final VecHousingComponent aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecHousingComponentReference(final VecHousingComponentReference aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecIECUnit(final VecIECUnit aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecImperialUnit(final VecImperialUnit aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecInsulationSpecification(final VecInsulationSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecIntegerValueProperty(final VecIntegerValueProperty aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecInternalComponentConnection(final VecInternalComponentConnection aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecInternalTerminalConnection(final VecInternalTerminalConnection aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecItemEquivalence(final VecItemEquivalence aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecItemHistoryEntry(final VecItemHistoryEntry aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecLabelingRole(VecLabelingRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecLabelingSpecification(VecLabelingSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecLocalGeometrySpecification(final VecLocalGeometrySpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecLocalizedString(final VecLocalizedString aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecLocalizedStringProperty(final VecLocalizedStringProperty aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecLocalizedTypedString(final VecLocalizedTypedString aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMapping(final VecMapping aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMappingSpecification(final VecMappingSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMassInformation(final VecMassInformation aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMaterial(final VecMaterial aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMatingDetail(final VecMatingDetail aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMatingPoint(final VecMatingPoint aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMeasurePointPosition(final VecMeasurePointPosition aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMeasurementPoint(final VecMeasurementPoint aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMeasurementPointReference(final VecMeasurementPointReference aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecModularSlot(final VecModularSlot aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecModularSlotAddOn(final VecModularSlotAddOn aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecModularSlotReference(final VecModularSlotReference aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecModuleFamily(final VecModuleFamily aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecModuleFamilySpecification(final VecModuleFamilySpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecModuleList(final VecModuleList aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecModuleListSpecification(final VecModuleListSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMultiCavityPlugSpecification(final VecMultiCavityPlugSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMultiCavitySealSpecification(final VecMultiCavitySealSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecMultiFuseSpecification(final VecMultiFuseSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNURBSControlPoint(final VecNURBSControlPoint aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNURBSCurve(final VecNURBSCurve aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNet(final VecNet aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNetGroup(final VecNetGroup aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNetSpecification(final VecNetSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNetType(final VecNetType aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNetViewSpecification(VecNetViewSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNetworkNode(final VecNetworkNode aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNetworkNodeViewItem(VecNetworkNodeViewItem aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNetworkPort(final VecNetworkPort aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNetworkPortViewItem(VecNetworkPortViewItem aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNodeLocation(final VecNodeLocation aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNodeMapping(final VecNodeMapping aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNumericalValue(final VecNumericalValue aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecNumericalValueProperty(final VecNumericalValueProperty aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecOccurrenceOrUsageViewItem2D(final VecOccurrenceOrUsageViewItem2D aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecOccurrenceOrUsageViewItem3D(final VecOccurrenceOrUsageViewItem3D aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecOnPointPlacement(final VecOnPointPlacement aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecOnWayPlacement(final VecOnWayPlacement aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecOpenCavitiesAssignment(final VecOpenCavitiesAssignment aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecOpenWireEndTerminalRole(final VecOpenWireEndTerminalRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecOpenWireEndTerminalSpecification(final VecOpenWireEndTerminalSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecOtherUnit(final VecOtherUnit aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPartOccurrence(final VecPartOccurrence aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPartOrUsageRelatedSpecification(final VecPartOrUsageRelatedSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPartRelation(final VecPartRelation aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPartStructureSpecification(final VecPartStructureSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPartSubstitutionSpecification(final VecPartSubstitutionSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPartUsage(final VecPartUsage aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPartUsageSpecification(final VecPartUsageSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPartVersion(final VecPartVersion aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPartWithSubComponentsRole(final VecPartWithSubComponentsRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPath(final VecPath aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPathSegment(final VecPathSegment aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPermission(final VecPermission aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPerson(final VecPerson aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPinComponent(final VecPinComponent aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPinComponentBehavior(final VecPinComponentBehavior aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPinComponentReference(final VecPinComponentReference aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPinCurrentInformation(final VecPinCurrentInformation aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPinOpticalInformation(final VecPinOpticalInformation aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPinTiming(final VecPinTiming aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPinVoltageInformation(final VecPinVoltageInformation aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPinWireMappingPoint(final VecPinWireMappingPoint aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPinWireMappingSpecification(final VecPinWireMappingSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPlaceableElementRole(final VecPlaceableElementRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPlaceableElementSpecification(final VecPlaceableElementSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPlacementPoint(final VecPlacementPoint aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPlacementPointPosition(final VecPlacementPointPosition aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPlacementPointReference(final VecPlacementPointReference aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPlacementSpecification(final VecPlacementSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPluggableTerminalRole(final VecPluggableTerminalRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPluggableTerminalSpecification(final VecPluggableTerminalSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPotentialDistributorSpecification(final VecPotentialDistributorSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecPowerConsumption(final VecPowerConsumption aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecProject(final VecProject aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecProtectionMaterialLength(VecProtectionMaterialLength aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecRelaySpecification(final VecRelaySpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecRequirementsConformanceSpecification(final VecRequirementsConformanceSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecRequirementsConformanceStatement(final VecRequirementsConformanceStatement aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecRingTerminalRole(final VecRingTerminalRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecRingTerminalSpecification(final VecRingTerminalSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecRobustnessProperties(final VecRobustnessProperties aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecRouting(final VecRouting aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecRoutingSpecification(final VecRoutingSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSIUnit(final VecSIUnit aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSealedCavitiesAssignment(final VecSealedCavitiesAssignment aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSegmentConnectionPoint(final VecSegmentConnectionPoint aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSegmentConnectionPointHC(VecSegmentConnectionPointHC aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSegmentCrossSectionArea(final VecSegmentCrossSectionArea aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSegmentLength(final VecSegmentLength aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSegmentLocation(final VecSegmentLocation aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSegmentMapping(final VecSegmentMapping aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSheetOrChapter(final VecSheetOrChapter aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecShieldSpecification(final VecShieldSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecShrinkableTubeSpecification(final VecShrinkableTubeSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSignal(final VecSignal aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSignalSpecification(final VecSignalSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSimpleValueProperty(final VecSimpleValueProperty aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSize(final VecSize aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSlot(final VecSlot aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSlotCoupling(final VecSlotCoupling aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSlotLayout(final VecSlotLayout aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSlotMapping(final VecSlotMapping aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSlotReference(final VecSlotReference aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSlotSpecification(final VecSlotSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSoundDampingClass(final VecSoundDampingClass aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSpecificRole(final VecSpecificRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSpliceTerminalRole(final VecSpliceTerminalRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSpliceTerminalSpecification(final VecSpliceTerminalSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecStripeSpecification(final VecStripeSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecSwitchingState(final VecSwitchingState aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTapeRole(final VecTapeRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTapeSpecification(final VecTapeSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTemperatureInformation(final VecTemperatureInformation aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTerminalCurrentInformation(final VecTerminalCurrentInformation aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTerminalPairing(final VecTerminalPairing aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTerminalPairingSpecification(final VecTerminalPairingSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTerminalReception(final VecTerminalReception aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTerminalReceptionReference(final VecTerminalReceptionReference aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTerminalReceptionSpecification(final VecTerminalReceptionSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTerminalRole(final VecTerminalRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTerminalSpecification(final VecTerminalSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTerminalType(final VecTerminalType aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTextBasedInstruction(final VecTextBasedInstruction aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTolerance(final VecTolerance aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTopologyBendingRestriction(final VecTopologyBendingRestriction aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTopologyBendingRestrictionSpecification(final VecTopologyBendingRestrictionSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTopologyGroupSpecification(final VecTopologyGroupSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTopologyMappingSpecification(final VecTopologyMappingSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTopologyNode(final VecTopologyNode aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTopologySegment(final VecTopologySegment aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTopologySpecification(final VecTopologySpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTopologyZone(final VecTopologyZone aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTopologyZoneSpecification(final VecTopologyZoneSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTransformation2D(final VecTransformation2D aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTransformation3D(final VecTransformation3D aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecTubeSpecification(final VecTubeSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecUSUnit(final VecUSUnit aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecUsageConstraint(final VecUsageConstraint aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecUsageConstraintSpecification(final VecUsageConstraintSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecUsageNode(final VecUsageNode aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecUsageNodeSpecification(final VecUsageNodeSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecValueRange(final VecValueRange aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecValueRangeProperty(final VecValueRangeProperty aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecVariantCode(final VecVariantCode aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecVariantCodeSpecification(final VecVariantCodeSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecVariantConfiguration(final VecVariantConfiguration aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecVariantConfigurationSpecification(final VecVariantConfigurationSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecVariantGroup(final VecVariantGroup aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecVariantGroupSpecification(final VecVariantGroupSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecVariantStructureNode(final VecVariantStructureNode aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecVariantStructureSpecification(final VecVariantStructureSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireElement(final VecWireElement aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireElementReference(final VecWireElementReference aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireElementSpecification(final VecWireElementSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireEnd(final VecWireEnd aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireEndAccessoryRole(final VecWireEndAccessoryRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireEndAccessorySpecification(final VecWireEndAccessorySpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireGroupSpecification(final VecWireGroupSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireGrouping(final VecWireGrouping aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireGroupingSpecification(final VecWireGroupingSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireLength(final VecWireLength aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireMounting(final VecWireMounting aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireMountingDetail(final VecWireMountingDetail aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireProtectionRole(final VecWireProtectionRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireProtectionSpecification(final VecWireProtectionSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireReception(final VecWireReception aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireReceptionAddOn(final VecWireReceptionAddOn aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireReceptionReference(final VecWireReceptionReference aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireReceptionSpecification(final VecWireReceptionSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireRole(final VecWireRole aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireSpecification(final VecWireSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireTupleSpecification(VecWireTupleSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireTupleTermination(VecWireTupleTermination aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireTupleTerminationSpecification(VecWireTupleTerminationSpecification aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecWireType(final VecWireType aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecZoneAssignment(final VecZoneAssignment aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitVecZoneCoverage(final VecZoneCoverage aBean) throws RuntimeException {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }
}
