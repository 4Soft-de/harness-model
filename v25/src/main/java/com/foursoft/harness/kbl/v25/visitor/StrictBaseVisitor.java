package com.foursoft.harness.kbl.v25.visitor;

import com.foursoft.harness.kbl.v25.*;

public class StrictBaseVisitor<R, E extends Throwable> implements Visitor<R, E> {

    /**
     * Default implementation for creating an error message in case of visiting
     * classes that are not explicitly handled.
     *
     * @param aBean Object to get class name from
     * @return Never null String containing the error message for the given object
     */
    private String getErrorMessage(final Object aBean) {
        return String.format("Encountered unhandled class '%s' in visitor implementation: %s",
                aBean.getClass().getName(), getClass().getName());
    }

    @Override
    public R visitKBLContainer(final KBLContainer aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblAccessory(final KblAccessory aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblAccessoryOccurrence(final KblAccessoryOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblAliasIdentification(final KblAliasIdentification aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblApproval(final KblApproval aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblAssemblyPart(final KblAssemblyPart aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblAssemblyPartOccurrence(final KblAssemblyPartOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblBSplineCurve(final KblBSplineCurve aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCartesianPoint(final KblCartesianPoint aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCavity(final KblCavity aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCavityOccurrence(final KblCavityOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCavityPlug(final KblCavityPlug aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCavityPlugOccurrence(final KblCavityPlugOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCavitySeal(final KblCavitySeal aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCavitySealOccurrence(final KblCavitySealOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblChange(final KblChange aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblChangeDescription(final KblChangeDescription aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCoPackOccurrence(final KblCoPackOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCoPackPart(final KblCoPackPart aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponent(final KblComponent aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentBox(final KblComponentBox aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentBoxConnection(final KblComponentBoxConnection aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentBoxConnector(final KblComponentBoxConnector aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentBoxConnectorOccurrence(final KblComponentBoxConnectorOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentBoxOccurrence(final KblComponentBoxOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentCavity(final KblComponentCavity aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentCavityOccurrence(final KblComponentCavityOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentOccurrence(final KblComponentOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentPinMap(final KblComponentPinMap aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentSlot(final KblComponentSlot aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentSlotOccurrence(final KblComponentSlotOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblConnection(final KblConnection aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblConnectorHousing(final KblConnectorHousing aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblConnectorOccurrence(final KblConnectorOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblConsumers(final KblConsumers aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblContactPoint(final KblContactPoint aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCore(final KblCore aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCoreOccurrence(final KblCoreOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCreation(final KblCreation aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCrossSectionArea(final KblCrossSectionArea aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblDefaultDimensionSpecification(final KblDefaultDimensionSpecification aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblDimensionSpecification(final KblDimensionSpecification aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblExternalReference(final KblExternalReference aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblExtremity(final KblExtremity aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblFixing(final KblFixing aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblFixingAssignment(final KblFixingAssignment aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblFixingOccurrence(final KblFixingOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblFuse(final KblFuse aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblFuseOccurrence(final KblFuseOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblFuseType(final KblFuseType aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblGeneralTerminal(final KblGeneralTerminal aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblGeneralWire(final KblGeneralWire aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblHarness(final KblHarness aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblHarnessConfiguration(final KblHarnessConfiguration aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblInstallationInstruction(final KblInstallationInstruction aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblLocalizedString(final KblLocalizedString aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblMaterial(final KblMaterial aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblModularSlot(final KblModularSlot aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblModularSlotOccurrence(final KblModularSlotOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblModule(final KblModule aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblModuleConfiguration(final KblModuleConfiguration aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblModuleFamily(final KblModuleFamily aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblNode(final KblNode aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblNumericalValue(final KblNumericalValue aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblPartSubstitution(final KblPartSubstitution aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblProcessingInstruction(final KblProcessingInstruction aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblProtectionArea(final KblProtectionArea aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblRouting(final KblRouting aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSchematicConnection(final KblSchematicConnection aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSegment(final KblSegment aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSlot(final KblSlot aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSlotOccurrence(final KblSlotOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecialTerminalOccurrence(final KblSpecialTerminalOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecialWireOccurrence(final KblSpecialWireOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedAccessoryOccurrence(final KblSpecifiedAccessoryOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedCavityPlugOccurrence(final KblSpecifiedCavityPlugOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedCavitySealOccurrence(final KblSpecifiedCavitySealOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedCoPackOccurrence(final KblSpecifiedCoPackOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedComponentBoxOccurrence(final KblSpecifiedComponentBoxOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedComponentOccurrence(final KblSpecifiedComponentOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedConnectorOccurrence(final KblSpecifiedConnectorOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedFixingOccurrence(final KblSpecifiedFixingOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedSpecialTerminalOccurrence(final KblSpecifiedSpecialTerminalOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedSpecialWireOccurrence(final KblSpecifiedSpecialWireOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedTerminalOccurrence(final KblSpecifiedTerminalOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedWireOccurrence(final KblSpecifiedWireOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedWireProtectionOccurrence(final KblSpecifiedWireProtectionOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblTerminalOccurrence(final KblTerminalOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblTolerance(final KblTolerance aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblTransformation(final KblTransformation aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblUnit(final KblUnit aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblValueRange(final KblValueRange aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblWireColour(final KblWireColour aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblWireLength(final KblWireLength aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblWireOccurrence(final KblWireOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblWireProtection(final KblWireProtection aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblWireProtectionOccurrence(final KblWireProtectionOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblWiringGroup(final KblWiringGroup aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }
}
