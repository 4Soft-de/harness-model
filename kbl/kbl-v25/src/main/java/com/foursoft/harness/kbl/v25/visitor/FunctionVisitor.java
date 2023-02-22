/*-
 * ========================LICENSE_START=================================
 * kbl-v25
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
package com.foursoft.harness.kbl.v25.visitor;

import com.foursoft.harness.kbl.v25.*;
import com.foursoft.jaxb.navext.runtime.model.Identifiable;

import java.util.Objects;
import java.util.function.Function;

public class FunctionVisitor<I extends Identifiable, O> implements Visitor<O, RuntimeException> {

    private final Function<I, O> func;

    public FunctionVisitor(final Function<I, O> func) {
        this.func = Objects.requireNonNull(func, "Given Function may not be null.");
    }

    @Override
    public O visitKBLContainer(final KBLContainer aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblAccessory(final KblAccessory aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblAccessoryOccurrence(final KblAccessoryOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblAliasIdentification(final KblAliasIdentification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblApproval(final KblApproval aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblAssemblyPart(final KblAssemblyPart aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblAssemblyPartOccurrence(final KblAssemblyPartOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblBSplineCurve(final KblBSplineCurve aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblCartesianPoint(final KblCartesianPoint aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblCavity(final KblCavity aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblCavityOccurrence(final KblCavityOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblCavityPlug(final KblCavityPlug aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblCavityPlugOccurrence(final KblCavityPlugOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblCavitySeal(final KblCavitySeal aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblCavitySealOccurrence(final KblCavitySealOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblChange(final KblChange aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblChangeDescription(final KblChangeDescription aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblCoPackOccurrence(final KblCoPackOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblCoPackPart(final KblCoPackPart aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblComponent(final KblComponent aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblComponentBox(final KblComponentBox aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblComponentBoxConnection(final KblComponentBoxConnection aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblComponentBoxConnector(final KblComponentBoxConnector aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblComponentBoxConnectorOccurrence(final KblComponentBoxConnectorOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblComponentBoxOccurrence(final KblComponentBoxOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblComponentCavity(final KblComponentCavity aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblComponentCavityOccurrence(final KblComponentCavityOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblComponentOccurrence(final KblComponentOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblComponentPinMap(final KblComponentPinMap aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblComponentSlot(final KblComponentSlot aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblComponentSlotOccurrence(final KblComponentSlotOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblConnection(final KblConnection aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblConnectorHousing(final KblConnectorHousing aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblConnectorOccurrence(final KblConnectorOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblConsumers(final KblConsumers aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblContactPoint(final KblContactPoint aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblCore(final KblCore aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblCoreOccurrence(final KblCoreOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblCreation(final KblCreation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblCrossSectionArea(final KblCrossSectionArea aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblDefaultDimensionSpecification(final KblDefaultDimensionSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblDimensionSpecification(final KblDimensionSpecification aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblExternalReference(final KblExternalReference aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblExtremity(final KblExtremity aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblFixing(final KblFixing aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblFixingAssignment(final KblFixingAssignment aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblFixingOccurrence(final KblFixingOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblFuse(final KblFuse aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblFuseOccurrence(final KblFuseOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblFuseType(final KblFuseType aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblGeneralTerminal(final KblGeneralTerminal aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblGeneralWire(final KblGeneralWire aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblHarness(final KblHarness aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblHarnessConfiguration(final KblHarnessConfiguration aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblInstallationInstruction(final KblInstallationInstruction aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblLocalizedString(final KblLocalizedString aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblMaterial(final KblMaterial aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblModularSlot(final KblModularSlot aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblModularSlotOccurrence(final KblModularSlotOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblModule(final KblModule aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblModuleConfiguration(final KblModuleConfiguration aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblModuleFamily(final KblModuleFamily aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblNode(final KblNode aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblNumericalValue(final KblNumericalValue aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblPartSubstitution(final KblPartSubstitution aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblProcessingInstruction(final KblProcessingInstruction aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblProtectionArea(final KblProtectionArea aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblRouting(final KblRouting aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSchematicConnection(final KblSchematicConnection aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSegment(final KblSegment aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSlot(final KblSlot aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSlotOccurrence(final KblSlotOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSpecialTerminalOccurrence(final KblSpecialTerminalOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSpecialWireOccurrence(final KblSpecialWireOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSpecifiedAccessoryOccurrence(final KblSpecifiedAccessoryOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSpecifiedCavityPlugOccurrence(final KblSpecifiedCavityPlugOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSpecifiedCavitySealOccurrence(final KblSpecifiedCavitySealOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSpecifiedCoPackOccurrence(final KblSpecifiedCoPackOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSpecifiedComponentBoxOccurrence(final KblSpecifiedComponentBoxOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSpecifiedComponentOccurrence(final KblSpecifiedComponentOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSpecifiedConnectorOccurrence(final KblSpecifiedConnectorOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSpecifiedFixingOccurrence(final KblSpecifiedFixingOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSpecifiedSpecialTerminalOccurrence(final KblSpecifiedSpecialTerminalOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSpecifiedSpecialWireOccurrence(final KblSpecifiedSpecialWireOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSpecifiedTerminalOccurrence(final KblSpecifiedTerminalOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSpecifiedWireOccurrence(final KblSpecifiedWireOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblSpecifiedWireProtectionOccurrence(final KblSpecifiedWireProtectionOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblTerminalOccurrence(final KblTerminalOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblTolerance(final KblTolerance aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblTransformation(final KblTransformation aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblUnit(final KblUnit aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblValueRange(final KblValueRange aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblWireColour(final KblWireColour aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblWireLength(final KblWireLength aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblWireOccurrence(final KblWireOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblWireProtection(final KblWireProtection aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblWireProtectionOccurrence(final KblWireProtectionOccurrence aBean) throws RuntimeException {
        return apply(aBean);
    }

    @Override
    public O visitKblWiringGroup(final KblWiringGroup aBean) throws RuntimeException {
        return apply(aBean);
    }

    private <T extends Identifiable> O apply(final T element) {
        return func.apply((I) element);
    }
}
