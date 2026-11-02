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

import com.foursoft.harness.kbl.v25.KblCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentBoxConnectorOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentBoxOccurrence;
import com.foursoft.harness.kbl.v25.KblConnectorOccurrence;
import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblSlotOccurrence;
import com.foursoft.harness.kbl2vec.convert.ConverterRegistry;
import com.foursoft.harness.kbl2vec.core.ConversionProperties;
import com.foursoft.harness.kbl2vec.core.EntityMapping;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationContextImpl;
import com.foursoft.harness.kbl2vec.transform.coupling.Matings.CavityMating;
import com.foursoft.harness.kbl2vec.transform.coupling.Matings.HousingMating;
import com.foursoft.harness.kbl2vec.transform.coupling.Matings.SlotMating;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MatingsTest {

    private final KblHarness harness = new KblHarness();
    private final KblConnectorOccurrence connector = new KblConnectorOccurrence();
    private final KblComponentBoxConnectorOccurrence boxConnector = new KblComponentBoxConnectorOccurrence();

    MatingsTest() {
        harness.getConnectorOccurrences().add(connector);
        final KblComponentBoxOccurrence componentBox = new KblComponentBoxOccurrence();
        componentBox.getComponentBoxConnectors().add(boxConnector);
        harness.getComponentBoxOccurrences().add(componentBox);
    }

    @Test
    void should_coupleMatedSlots_withTheDeclaringSideFirst() {
        // Given
        final KblSlotOccurrence connectorSlot = slot(connector);
        final KblSlotOccurrence boxSlot = slot(boxConnector);
        boxSlot.getMatedSlots().add(connectorSlot);

        // When
        final List<HousingMating> result = matings();

        // Then
        assertThat(result).singleElement()
                .returns(boxConnector, HousingMating::firstHousing)
                .returns(connector, HousingMating::secondHousing)
                .extracting(HousingMating::slots).asList()
                .containsExactly(new SlotMating(boxSlot, connectorSlot, List.of()));
    }

    @Test
    void should_coupleAMatingDeclaredOnBothSidesOnce() {
        // Given
        final KblSlotOccurrence connectorSlot = slot(connector);
        final KblSlotOccurrence boxSlot = slot(boxConnector);
        boxSlot.getMatedSlots().add(connectorSlot);
        connectorSlot.getMatedSlots().add(boxSlot);

        // When
        final List<HousingMating> result = matings();

        // Then
        assertThat(result).singleElement()
                .extracting(HousingMating::slots).asList()
                .hasSize(1);
    }

    @Test
    void should_groupTheMatedSlotsOfTheSameHousings() {
        // Given
        final KblSlotOccurrence connectorSlot1 = slot(connector);
        final KblSlotOccurrence connectorSlot2 = slot(connector);
        final KblSlotOccurrence boxSlot1 = slot(boxConnector);
        final KblSlotOccurrence boxSlot2 = slot(boxConnector);
        // Connector occurrences are searched first, so this mating defines the orientation of the coupling.
        connectorSlot2.getMatedSlots().add(boxSlot2);
        // Declared on the other side, it is turned around to the orientation of the coupling.
        boxSlot1.getMatedSlots().add(connectorSlot1);

        // When
        final List<HousingMating> result = matings();

        // Then
        assertThat(result).singleElement()
                .extracting(HousingMating::slots).asList()
                .containsExactly(new SlotMating(connectorSlot2, boxSlot2, List.of()),
                                 new SlotMating(connectorSlot1, boxSlot1, List.of()));
    }

    @Test
    void should_deriveTheSlotMating_fromMatedCavities() {
        // Given
        final KblSlotOccurrence connectorSlot = slot(connector);
        final KblSlotOccurrence boxSlot = slot(boxConnector);
        final KblCavityOccurrence connectorCavity = cavity(connectorSlot);
        final KblCavityOccurrence boxCavity = cavity(boxSlot);
        connectorCavity.getMatedCavities().add(boxCavity);

        // When
        final List<HousingMating> result = matings();

        // Then
        assertThat(result).singleElement()
                .returns(connector, HousingMating::firstHousing)
                .extracting(HousingMating::slots).asList()
                .containsExactly(new SlotMating(connectorSlot, boxSlot,
                                                List.of(new CavityMating(connectorCavity, boxCavity))));
    }

    @Test
    void should_orientTheCavityMatings_likeTheirSlots() {
        // Given
        final KblSlotOccurrence connectorSlot = slot(connector);
        final KblSlotOccurrence boxSlot = slot(boxConnector);
        final KblCavityOccurrence connectorCavity = cavity(connectorSlot);
        final KblCavityOccurrence boxCavity = cavity(boxSlot);
        connectorSlot.getMatedSlots().add(boxSlot);
        // Declared on the other side, it is turned around to the orientation of its slots.
        boxCavity.getMatedCavities().add(connectorCavity);

        // When
        final List<HousingMating> result = matings();

        // Then
        assertThat(result).singleElement()
                .extracting(HousingMating::slots).asList()
                .containsExactly(new SlotMating(connectorSlot, boxSlot,
                                                List.of(new CavityMating(connectorCavity, boxCavity))));
    }

    @Test
    void should_ignoreMatingsWithSlotsOutsideOfTheHarness() {
        // Given
        slot(connector).getMatedSlots().add(new KblSlotOccurrence());

        // When
        final List<HousingMating> result = matings();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void should_findNoMatings_withoutMatedSlotsOrCavities() {
        // Given
        cavity(slot(connector));
        cavity(slot(boxConnector));

        // When / Then
        assertThat(matings()).isEmpty();
    }

    private List<HousingMating> matings() {
        final ConversionProperties properties = new ConversionProperties();
        final TransformationContext context = new TransformationContextImpl(properties,
                                                                            new ConverterRegistry(properties),
                                                                            new EntityMapping());
        return Matings.of(context, harness);
    }

    private static KblSlotOccurrence slot(final KblConnectorOccurrence connector) {
        final KblSlotOccurrence slot = new KblSlotOccurrence();
        connector.getSlots().add(slot);
        return slot;
    }

    private static KblSlotOccurrence slot(final KblComponentBoxConnectorOccurrence connector) {
        final KblSlotOccurrence slot = new KblSlotOccurrence();
        connector.getSlots().add(slot);
        return slot;
    }

    private static KblCavityOccurrence cavity(final KblSlotOccurrence slot) {
        final KblCavityOccurrence cavity = new KblCavityOccurrence();
        slot.getCavities().add(cavity);
        return cavity;
    }
}
