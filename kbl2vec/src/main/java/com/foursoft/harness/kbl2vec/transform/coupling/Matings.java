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
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.navext.runtime.model.Identifiable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Collects the mated slots ({@code Slot_occurrence.Mated_slots}) and cavities
 * ({@code Cavity_occurrence.Mated_cavities}) of the housings of a harness: its connector occurrences and the connectors
 * of its component box occurrences.
 * <p>
 * A mating declared on both sides is collected once. The side found first becomes the first one, connector occurrences
 * are searched before component boxes. Mated cavities whose
 * slots are not declared as mated imply the mating of their slots. The matings are grouped by the pair of mated
 * housings, each group becomes one {@code CouplingPoint}.
 * <p>
 * The records are the sources of the transformation. They are {@link Identifiable} by their first slot / cavity, so
 * that the XML ids of the VEC elements are derived from the KBL ids.
 */
final class Matings {

    record HousingMating(Object firstHousing, Object secondHousing, List<SlotMating> slots) implements Identifiable {
        @Override
        public String getXmlId() {
            return slots.get(0).getXmlId();
        }
    }

    record SlotMating(KblSlotOccurrence first, KblSlotOccurrence second, List<CavityMating> cavities)
            implements Identifiable {
        @Override
        public String getXmlId() {
            return first.getXmlId();
        }
    }

    record CavityMating(KblCavityOccurrence first, KblCavityOccurrence second) implements Identifiable {
        @Override
        public String getXmlId() {
            return first.getXmlId();
        }
    }

    private final TransformationContext context;
    private final Map<KblSlotOccurrence, Object> housingOfSlot = new LinkedHashMap<>();
    private final Map<KblCavityOccurrence, KblSlotOccurrence> slotOfCavity = new LinkedHashMap<>();
    private final Map<Set<KblSlotOccurrence>, SlotMatingBuilder> slotMatings = new LinkedHashMap<>();

    private Matings(final TransformationContext context, final KblHarness harness) {
        this.context = context;
        for (final KblConnectorOccurrence connector : harness.getConnectorOccurrences()) {
            connector.getSlots().forEach(slot -> addSlot(connector, slot));
        }
        for (final KblComponentBoxOccurrence componentBox : harness.getComponentBoxOccurrences()) {
            for (final KblComponentBoxConnectorOccurrence connector : componentBox.getComponentBoxConnectors()) {
                connector.getSlots().stream()
                        .filter(KblSlotOccurrence.class::isInstance)
                        .map(KblSlotOccurrence.class::cast)
                        .forEach(slot -> addSlot(connector, slot));
            }
        }
        housingOfSlot.keySet().forEach(this::collectMatings);
    }

    static List<HousingMating> of(final TransformationContext context, final KblHarness harness) {
        return new Matings(context, harness).housingMatings();
    }

    private void addSlot(final Object housing, final KblSlotOccurrence slot) {
        housingOfSlot.put(slot, housing);
        slot.getCavities().forEach(cavity -> slotOfCavity.put(cavity, slot));
    }

    private void collectMatings(final KblSlotOccurrence slot) {
        for (final KblSlotOccurrence matedSlot : slot.getMatedSlots()) {
            slotMating(slot, matedSlot);
        }
        for (final KblCavityOccurrence cavity : slot.getCavities()) {
            for (final KblCavityOccurrence matedCavity : cavity.getMatedCavities()) {
                final KblSlotOccurrence matedSlot = slotOfCavity.get(matedCavity);
                if (matedSlot == null) {
                    context.getLogger().warn("'{}' is mated with '{}', which is no cavity of the harness. The mating "
                                                     + "is ignored.", cavity, matedCavity);
                    continue;
                }
                final SlotMatingBuilder builder = slotMating(slot, matedSlot);
                if (builder != null) {
                    builder.addCavityMating(cavity, matedCavity);
                }
            }
        }
    }

    private SlotMatingBuilder slotMating(final KblSlotOccurrence slot, final KblSlotOccurrence matedSlot) {
        if (!housingOfSlot.containsKey(matedSlot)) {
            context.getLogger().warn("'{}' is mated with '{}', which is no slot of the harness. The mating is "
                                             + "ignored.", slot, matedSlot);
            return null;
        }
        return slotMatings.computeIfAbsent(Set.of(slot, matedSlot), k -> new SlotMatingBuilder(slot, matedSlot));
    }

    private List<HousingMating> housingMatings() {
        final Map<Set<Object>, List<SlotMatingBuilder>> byHousings = new LinkedHashMap<>();
        for (final SlotMatingBuilder builder : slotMatings.values()) {
            final Set<Object> housings = Set.of(housingOfSlot.get(builder.first), housingOfSlot.get(builder.second));
            byHousings.computeIfAbsent(housings, k -> new ArrayList<>()).add(builder);
        }
        return byHousings.values().stream()
                .map(builders -> {
                    final Object firstHousing = housingOfSlot.get(builders.get(0).first);
                    final Object secondHousing = housingOfSlot.get(builders.get(0).second);
                    final List<SlotMating> slots = builders.stream()
                            .map(builder -> builder.build(housingOfSlot.get(builder.first) == firstHousing))
                            .toList();
                    return new HousingMating(firstHousing, secondHousing, slots);
                })
                .toList();
    }

    private static final class SlotMatingBuilder {
        private final KblSlotOccurrence first;
        private final KblSlotOccurrence second;
        private final Map<Set<KblCavityOccurrence>, CavityMating> cavities = new LinkedHashMap<>();

        private SlotMatingBuilder(final KblSlotOccurrence first, final KblSlotOccurrence second) {
            this.first = first;
            this.second = second;
        }

        private void addCavityMating(final KblCavityOccurrence cavity, final KblCavityOccurrence matedCavity) {
            cavities.putIfAbsent(Set.of(cavity, matedCavity), new CavityMating(cavity, matedCavity));
        }

        /**
         * @param sameOrientation {@code false} if the first slot belongs to the second housing of the coupling, then
         *                        the slots and cavities are swapped.
         */
        private SlotMating build(final boolean sameOrientation) {
            final KblSlotOccurrence firstSlot = sameOrientation ? first : second;
            final KblSlotOccurrence secondSlot = sameOrientation ? second : first;
            final List<CavityMating> cavityMatings = cavities.values().stream()
                    .map(c -> firstSlot.getCavities().contains(c.first()) ? c : new CavityMating(c.second(), c.first()))
                    .toList();
            return new SlotMating(firstSlot, secondSlot, cavityMatings);
        }
    }
}
