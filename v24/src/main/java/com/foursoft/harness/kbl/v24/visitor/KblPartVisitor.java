/*-
 * ========================LICENSE_START=================================
 * kbl-v24
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
package com.foursoft.harness.kbl.v24.visitor;

import com.foursoft.harness.kbl.v24.*;

/**
 * All methods are implemented as default method. Only methods which inherit from KblPart (except KblPartWithTitleBlock) are not overwritten.
 */
public interface KblPartVisitor<R, E extends Throwable> extends Visitor<R, E> {

    private String getErrorMessage(final Object aBean) {
        return String.format("Encountered unhandled class '%s' in visitor implementation: %s",
                aBean.getClass().getName(), getClass().getName());
    }

    @Override
    default R visitKBLContainer(final KBLContainer aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblAbstractSlotOccurrence(final KblAbstractSlotOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblAccessoryOccurrence(final KblAccessoryOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblAliasIdentification(final KblAliasIdentification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblApproval(final KblApproval aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblAssemblyPartOccurrence(final KblAssemblyPartOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblBSplineCurve(final KblBSplineCurve aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCartesianPoint(final KblCartesianPoint aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCavityOccurrence(final KblCavityOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCavity(final KblCavity aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCavityPlugOccurrence(final KblCavityPlugOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCavitySealOccurrence(final KblCavitySealOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblChange(final KblChange aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblChangeDescription(final KblChangeDescription aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCoPackOccurrence(final KblCoPackOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponentBoxConnection(final KblComponentBoxConnection aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponentBoxConnector(final KblComponentBoxConnector aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponentBoxConnectorOccurrence(final KblComponentBoxConnectorOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponentBoxOccurrence(final KblComponentBoxOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponentCavity(final KblComponentCavity aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponentCavityOccurrence(final KblComponentCavityOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponentOccurrence(final KblComponentOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponentSlot(final KblComponentSlot aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponentSlotOccurrence(final KblComponentSlotOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblConnection(final KblConnection aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblConnectorOccurrence(final KblConnectorOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblConsumers(final KblConsumers aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblContactPoint(final KblContactPoint aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCore(final KblCore aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCoreOccurrence(final KblCoreOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCreation(final KblCreation aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCrossSectionArea(final KblCrossSectionArea aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblDefaultDimensionSpecification(final KblDefaultDimensionSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblDimensionSpecification(final KblDimensionSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblExternalReference(final KblExternalReference aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblExtremity(final KblExtremity aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblFixingAssignment(final KblFixingAssignment aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblFixingOccurrence(final KblFixingOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblFuseOccurrence(final KblFuseOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblFuseType(final KblFuseType aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblHarness(final KblHarness aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblHarnessConfiguration(final KblHarnessConfiguration aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblInstallationInstruction(final KblInstallationInstruction aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblLocalizedString(final KblLocalizedString aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblMaterial(final KblMaterial aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblModularSlot(final KblModularSlot aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblModularSlotOccurrence(final KblModularSlotOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblModule(final KblModule aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblModuleConfiguration(final KblModuleConfiguration aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblModuleFamily(final KblModuleFamily aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblNode(final KblNode aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblNumericalValue(final KblNumericalValue aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblPartSubstitution(final KblPartSubstitution aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblProcessingInstruction(final KblProcessingInstruction aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblProtectionArea(final KblProtectionArea aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblRouting(final KblRouting aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSchematicConnection(final KblSchematicConnection aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSegment(final KblSegment aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSlot(final KblSlot aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSlotOccurrence(final KblSlotOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecialTerminalOccurrence(final KblSpecialTerminalOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecialWireOccurrence(final KblSpecialWireOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedAccessoryOccurrence(final KblSpecifiedAccessoryOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedCavityPlugOccurrence(final KblSpecifiedCavityPlugOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedCavitySealOccurrence(final KblSpecifiedCavitySealOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedCoPackOccurrence(final KblSpecifiedCoPackOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedComponentBoxOccurrence(final KblSpecifiedComponentBoxOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedComponentOccurrence(final KblSpecifiedComponentOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedConnectorOccurrence(final KblSpecifiedConnectorOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedFixingOccurrence(final KblSpecifiedFixingOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedSpecialTerminalOccurrence(final KblSpecifiedSpecialTerminalOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedSpecialWireOccurrence(final KblSpecifiedSpecialWireOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedTerminalOccurrence(final KblSpecifiedTerminalOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedWireOccurrence(final KblSpecifiedWireOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedWireProtectionOccurrence(final KblSpecifiedWireProtectionOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblTerminalOccurrence(final KblTerminalOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblTolerance(final KblTolerance aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblTransformation(final KblTransformation aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblUnit(final KblUnit aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblValueRange(final KblValueRange aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblWireColour(final KblWireColour aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblWireLength(final KblWireLength aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblWireOccurrence(final KblWireOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblWireProtectionOccurrence(final KblWireProtectionOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblWiringGroup(final KblWiringGroup aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }
}
