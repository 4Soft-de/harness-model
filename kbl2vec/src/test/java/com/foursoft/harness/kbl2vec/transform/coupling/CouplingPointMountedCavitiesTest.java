/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 - 2026 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.transform.coupling;

import com.foursoft.harness.kbl.v25.KblCavity;
import com.foursoft.harness.kbl.v25.KblCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblComponent;
import com.foursoft.harness.kbl.v25.KblComponentCavity;
import com.foursoft.harness.kbl.v25.KblComponentCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentSlotOccurrence;
import com.foursoft.harness.kbl.v25.KblConnectorOccurrence;
import com.foursoft.harness.kbl.v25.KblSlotOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.kbl2vec.transform.components.ee_components.ComponentCavities.MountedCavity;
import com.foursoft.harness.kbl2vec.transform.components.ee_components.ComponentCavities.MountedSlot;
import com.foursoft.harness.vec.v2x.VecCavityCoupling;
import com.foursoft.harness.vec.v2x.VecCavityReference;
import com.foursoft.harness.vec.v2x.VecConnectorHousingRole;
import com.foursoft.harness.vec.v2x.VecCouplingPoint;
import com.foursoft.harness.vec.v2x.VecSlotCoupling;
import com.foursoft.harness.vec.v2x.VecSlotReference;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The coupling of a component occurrence that is mounted on cavities.
 */
class CouplingPointMountedCavitiesTest {

    @Test
    void should_coupleSlotAndCavities_ofAComponentMountedOnAComponentCavity() {
        // Given: as in VOBES data, mounted on the component box, the component slot and one of its cavities.
        final KblComponentSlotOccurrence componentSlot = new KblComponentSlotOccurrence();
        final KblComponentCavityOccurrence componentCavity = new KblComponentCavityOccurrence();
        final KblComponentCavity cavityPart = new KblComponentCavity();
        cavityPart.setCavityNumber("2A");
        componentCavity.setPart(cavityPart);
        componentCavity.setParentComponentSlotOccurrence(componentSlot);
        final KblComponentOccurrence source = occurrence();
        source.getMounting().add(componentSlot);
        source.getMounting().add(componentCavity);
        final MountedCavity mountedCavity = new MountedCavity(source, componentCavity, "1A");

        final VecConnectorHousingRole slotRole = new VecConnectorHousingRole();
        final VecConnectorHousingRole componentRole = new VecConnectorHousingRole();
        final VecSlotCoupling slotCoupling = new VecSlotCoupling();
        final VecSlotReference firstSlot = new VecSlotReference();
        final VecSlotReference secondSlot = new VecSlotReference();
        final VecCavityCoupling cavityCoupling = new VecCavityCoupling();
        final VecCavityReference firstCavity = new VecCavityReference();
        final VecCavityReference secondCavity = new VecCavityReference();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(componentSlot, slotRole)
                .addMockMapping(source, componentRole)
                .addMockMapping(new MountedSlot(source), slotCoupling)
                .addMockMapping(componentSlot, firstSlot)
                .addMockMapping(new MountedSlot(source), secondSlot)
                .addMockMapping(mountedCavity, cavityCoupling)
                .addMockMapping(componentCavity, firstCavity)
                .addMockMapping(mountedCavity, secondCavity);

        // When
        final VecCouplingPoint couplingPoint = orchestrator.transform(new CouplingPointTransformer(), source);
        final VecSlotCoupling vecSlotCoupling =
                orchestrator.transform(new MountedSlotCouplingTransformer(), new MountedSlot(source));
        final VecCavityCoupling vecCavityCoupling =
                orchestrator.transform(new MountedCavityCouplingTransformer(), mountedCavity);

        // Then
        assertThat(couplingPoint)
                .returns(slotRole, VecCouplingPoint::getFirstConnector)
                .returns(componentRole, VecCouplingPoint::getSecondConnector)
                .satisfies(v -> assertThat(v.getSlotCouplings()).containsExactly(slotCoupling));
        assertThat(vecSlotCoupling)
                .returns(firstSlot, VecSlotCoupling::getFirstSlot)
                .returns(secondSlot, VecSlotCoupling::getSecondSlot)
                .satisfies(v -> assertThat(v.getCavityCouplings()).containsExactly(cavityCoupling));
        assertThat(vecCavityCoupling)
                .returns(firstCavity, VecCavityCoupling::getFirstCavity)
                .returns(secondCavity, VecCavityCoupling::getSecondCavity);
    }

    @Test
    void should_coupleWithTheConnector_ofAMountedConnectorCavity() {
        // Given: mounted on a cavity of a connector only.
        final KblConnectorOccurrence connector = new KblConnectorOccurrence();
        final KblSlotOccurrence slot = new KblSlotOccurrence();
        slot.setParentConnectorOccurrence(connector);
        final KblCavityOccurrence cavity = new KblCavityOccurrence();
        final KblCavity cavityPart = new KblCavity();
        cavityPart.setCavityNumber("1");
        cavity.setPart(cavityPart);
        cavity.setParentSlotOccurrence(slot);
        final KblComponentOccurrence source = occurrence();
        source.getMounting().add(cavity);

        final VecConnectorHousingRole connectorRole = new VecConnectorHousingRole();
        final VecSlotReference firstSlot = new VecSlotReference();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(connector, connectorRole)
                .addMockMapping(source, new VecConnectorHousingRole())
                .addMockMapping(new MountedSlot(source), new VecSlotCoupling())
                .addMockMapping(slot, firstSlot)
                .addMockMapping(new MountedSlot(source), new VecSlotReference())
                .addMockMapping(new MountedCavity(source, cavity, "1"), new VecCavityCoupling());

        // When
        final VecCouplingPoint couplingPoint = orchestrator.transform(new CouplingPointTransformer(), source);
        final VecSlotCoupling slotCoupling =
                orchestrator.transform(new MountedSlotCouplingTransformer(), new MountedSlot(source));

        // Then
        assertThat(couplingPoint.getFirstConnector()).isSameAs(connectorRole);
        assertThat(slotCoupling.getFirstSlot()).isSameAs(firstSlot);
    }

    private static KblComponentOccurrence occurrence() {
        final KblComponentOccurrence occurrence = new KblComponentOccurrence();
        occurrence.setId("F1");
        occurrence.setPart(new KblComponent());
        return occurrence;
    }
}
