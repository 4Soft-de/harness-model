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
 * Base implementation for a strict {@link Visitor}. Strict means, that the
 * visitor class will throw an {@link UnsupportedOperationException} if it
 * encounters a class that is not handled explicitly, by overriding the
 * corresponding methods.
 *
 * @param <R> Class of the visitor
 */
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
    public R visitKBLContainer(final KBLContainer aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblAbstractSlotOccurrence(final KblAbstractSlotOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblAccessory(final KblAccessory aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblAccessoryOccurrence(final KblAccessoryOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblAliasIdentification(final KblAliasIdentification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblApproval(final KblApproval aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblAssemblyPart(final KblAssemblyPart aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblAssemblyPartOccurrence(final KblAssemblyPartOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblBSplineCurve(final KblBSplineCurve aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCartesianPoint(final KblCartesianPoint aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCavity(final KblCavity aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCavityOccurrence(final KblCavityOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCavityPlug(final KblCavityPlug aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCavityPlugOccurrence(final KblCavityPlugOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCavitySeal(final KblCavitySeal aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCavitySealOccurrence(final KblCavitySealOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblChange(final KblChange aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblChangeDescription(final KblChangeDescription aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCoPackOccurrence(final KblCoPackOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCoPackPart(final KblCoPackPart aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponent(final KblComponent aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentBox(final KblComponentBox aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentBoxConnection(final KblComponentBoxConnection aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentBoxConnector(final KblComponentBoxConnector aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentBoxConnectorOccurrence(final KblComponentBoxConnectorOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentBoxOccurrence(final KblComponentBoxOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentCavity(final KblComponentCavity aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentCavityOccurrence(final KblComponentCavityOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentOccurrence(final KblComponentOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentSlot(final KblComponentSlot aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblComponentSlotOccurrence(final KblComponentSlotOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblConnection(final KblConnection aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblConnectorHousing(final KblConnectorHousing aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblConnectorOccurrence(final KblConnectorOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblConsumers(final KblConsumers aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblContactPoint(final KblContactPoint aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCore(final KblCore aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCoreOccurrence(final KblCoreOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCreation(final KblCreation aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblCrossSectionArea(final KblCrossSectionArea aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblDefaultDimensionSpecification(final KblDefaultDimensionSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblDimensionSpecification(final KblDimensionSpecification aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblExternalReference(final KblExternalReference aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblExtremity(final KblExtremity aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblFixing(final KblFixing aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblFixingAssignment(final KblFixingAssignment aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblFixingOccurrence(final KblFixingOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblFuse(final KblFuse aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblFuseOccurrence(final KblFuseOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblFuseType(final KblFuseType aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblGeneralTerminal(final KblGeneralTerminal aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblGeneralWire(final KblGeneralWire aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblHarness(final KblHarness aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblHarnessConfiguration(final KblHarnessConfiguration aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblInstallationInstruction(final KblInstallationInstruction aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblLocalizedString(final KblLocalizedString aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblMaterial(final KblMaterial aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblModularSlot(final KblModularSlot aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblModularSlotOccurrence(final KblModularSlotOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblModule(final KblModule aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblModuleConfiguration(final KblModuleConfiguration aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblModuleFamily(final KblModuleFamily aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblNode(final KblNode aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblNumericalValue(final KblNumericalValue aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblPartSubstitution(final KblPartSubstitution aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblProcessingInstruction(final KblProcessingInstruction aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblProtectionArea(final KblProtectionArea aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblRouting(final KblRouting aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSchematicConnection(final KblSchematicConnection aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSegment(final KblSegment aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSlot(final KblSlot aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSlotOccurrence(final KblSlotOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecialTerminalOccurrence(final KblSpecialTerminalOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecialWireOccurrence(final KblSpecialWireOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedAccessoryOccurrence(final KblSpecifiedAccessoryOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedCavityPlugOccurrence(final KblSpecifiedCavityPlugOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedCavitySealOccurrence(final KblSpecifiedCavitySealOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedCoPackOccurrence(final KblSpecifiedCoPackOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedComponentBoxOccurrence(final KblSpecifiedComponentBoxOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedComponentOccurrence(final KblSpecifiedComponentOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedConnectorOccurrence(final KblSpecifiedConnectorOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedFixingOccurrence(final KblSpecifiedFixingOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedSpecialTerminalOccurrence(final KblSpecifiedSpecialTerminalOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedSpecialWireOccurrence(final KblSpecifiedSpecialWireOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedTerminalOccurrence(final KblSpecifiedTerminalOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedWireOccurrence(final KblSpecifiedWireOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblSpecifiedWireProtectionOccurrence(final KblSpecifiedWireProtectionOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblTerminalOccurrence(final KblTerminalOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblTolerance(final KblTolerance aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblTransformation(final KblTransformation aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblUnit(final KblUnit aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblValueRange(final KblValueRange aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblWireColour(final KblWireColour aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblWireLength(final KblWireLength aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblWireOccurrence(final KblWireOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblWireProtection(final KblWireProtection aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblWireProtectionOccurrence(final KblWireProtectionOccurrence aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }

    @Override
    public R visitKblWiringGroup(final KblWiringGroup aBean) {
        throw new UnsupportedOperationException(getErrorMessage(aBean));
    }
}
