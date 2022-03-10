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
 * All methods are implemented as default method. Only methods which inherit from LocatedComponent are not overwritten.
 */
public interface KblLocatedComponentVisitor<R, E extends Throwable> extends Visitor<R, E> {

    private String getErrorMessage(final Object aBean) {
        return String.format("Encountered unhandled class '%s' in visitor implementation: %s",
                aBean.getClass().getName(), getClass().getName());
    }

    @Override
    default R visitKBLContainer(final KBLContainer aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblAbstractSlotOccurrence(final KblAbstractSlotOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblAccessory(final KblAccessory aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblAliasIdentification(final KblAliasIdentification aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblApproval(final KblApproval aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblAssemblyPart(final KblAssemblyPart aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblBSplineCurve(final KblBSplineCurve aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCartesianPoint(final KblCartesianPoint aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCavity(final KblCavity aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCavityOccurrence(final KblCavityOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCavityPlug(final KblCavityPlug aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCavityPlugOccurrence(final KblCavityPlugOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCavitySeal(final KblCavitySeal aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCavitySealOccurrence(final KblCavitySealOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblChange(final KblChange aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblChangeDescription(final KblChangeDescription aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCoPackOccurrence(final KblCoPackOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCoPackPart(final KblCoPackPart aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponent(final KblComponent aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponentBox(final KblComponentBox aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponentBoxConnection(final KblComponentBoxConnection aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponentBoxConnector(final KblComponentBoxConnector aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponentBoxConnectorOccurrence(final KblComponentBoxConnectorOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponentCavity(final KblComponentCavity aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponentCavityOccurrence(final KblComponentCavityOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponentOccurrence(final KblComponentOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponentSlot(final KblComponentSlot aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblComponentSlotOccurrence(final KblComponentSlotOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblConnection(final KblConnection aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblConnectorHousing(final KblConnectorHousing aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblConsumers(final KblConsumers aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblContactPoint(final KblContactPoint aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCore(final KblCore aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCoreOccurrence(final KblCoreOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCreation(final KblCreation aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblCrossSectionArea(final KblCrossSectionArea aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblDefaultDimensionSpecification(final KblDefaultDimensionSpecification aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblDimensionSpecification(final KblDimensionSpecification aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblExternalReference(final KblExternalReference aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblExtremity(final KblExtremity aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblFixing(final KblFixing aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblFixingAssignment(final KblFixingAssignment aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblFuse(final KblFuse aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblFuseOccurrence(final KblFuseOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblFuseType(final KblFuseType aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblGeneralTerminal(final KblGeneralTerminal aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblGeneralWire(final KblGeneralWire aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblHarness(final KblHarness aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblHarnessConfiguration(final KblHarnessConfiguration aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblInstallationInstruction(final KblInstallationInstruction aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblLocalizedString(final KblLocalizedString aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblMaterial(final KblMaterial aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblModularSlot(final KblModularSlot aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblModularSlotOccurrence(final KblModularSlotOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblModule(final KblModule aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblModuleConfiguration(final KblModuleConfiguration aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblModuleFamily(final KblModuleFamily aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblNode(final KblNode aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblNumericalValue(final KblNumericalValue aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblPartSubstitution(final KblPartSubstitution aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblProcessingInstruction(final KblProcessingInstruction aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblProtectionArea(final KblProtectionArea aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblRouting(final KblRouting aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSchematicConnection(final KblSchematicConnection aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSegment(final KblSegment aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSlot(final KblSlot aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSlotOccurrence(final KblSlotOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecialWireOccurrence(final KblSpecialWireOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedCavityPlugOccurrence(final KblSpecifiedCavityPlugOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedCavitySealOccurrence(final KblSpecifiedCavitySealOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedCoPackOccurrence(final KblSpecifiedCoPackOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedComponentBoxOccurrence(final KblSpecifiedComponentBoxOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedComponentOccurrence(final KblSpecifiedComponentOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedSpecialWireOccurrence(final KblSpecifiedSpecialWireOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedTerminalOccurrence(final KblSpecifiedTerminalOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblSpecifiedWireOccurrence(final KblSpecifiedWireOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblTerminalOccurrence(final KblTerminalOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblTolerance(final KblTolerance aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblTransformation(final KblTransformation aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblUnit(final KblUnit aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblValueRange(final KblValueRange aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblWireColour(final KblWireColour aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblWireLength(final KblWireLength aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblWireOccurrence(final KblWireOccurrence aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblWireProtection(final KblWireProtection aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    default R visitKblWiringGroup(final KblWiringGroup aBean) throws E {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }
}
