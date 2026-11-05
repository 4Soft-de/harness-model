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
import com.foursoft.harness.kbl.v25.KblComponentCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentSlotOccurrence;
import com.foursoft.harness.kbl.v25.KblSlotOccurrence;
import com.foursoft.harness.kbl2vec.transform.components.ee_components.ComponentCavities;
import com.foursoft.harness.kbl2vec.transform.components.ee_components.ComponentCavities.MountedCavity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Resolves what a component occurrence is mounted on ({@code Component_occurrence.Mounting}): the component slot it is
 * mounted on, or the slot of the cavities it is mounted on (a component slot of a component box, or a slot of a
 * connector).
 */
final class Mountings {

    private Mountings() {
        // hide default constructor
    }

    /**
     * @return The mounted slot, a {@link KblComponentSlotOccurrence} or {@link KblSlotOccurrence}.
     */
    static Optional<Object> slot(final KblComponentOccurrence occurrence) {
        final Optional<Object> componentSlot = occurrence.getMounting().stream()
                .filter(KblComponentSlotOccurrence.class::isInstance)
                .map(Object.class::cast)
                .findFirst();
        if (componentSlot.isPresent()) {
            return componentSlot;
        }
        return ComponentCavities.mountedCavities(occurrence).stream()
                .map(MountedCavity::mountedOn)
                .map(Mountings::slotOf)
                .filter(Objects::nonNull)
                .findFirst();
    }

    /**
     * @return The housing with the mounted slot: the component slot itself (it has its own housing role), or the
     * connector of a slot.
     */
    static Optional<Object> housing(final KblComponentOccurrence occurrence) {
        return slot(occurrence).map(slot -> slot instanceof final KblSlotOccurrence slotOccurrence
                ? slotOccurrence.getParentConnectorOccurrence()
                : slot);
    }

    static boolean isMounted(final KblComponentOccurrence occurrence) {
        return housing(occurrence).isPresent();
    }

    static List<MountedCavity> cavities(final KblComponentOccurrence occurrence) {
        return ComponentCavities.mountedCavities(occurrence);
    }

    private static Object slotOf(final Object cavity) {
        if (cavity instanceof final KblComponentCavityOccurrence componentCavity) {
            return componentCavity.getParentComponentSlotOccurrence();
        }
        if (cavity instanceof final KblCavityOccurrence connectorCavity) {
            return connectorCavity.getParentSlotOccurrence();
        }
        return null;
    }
}
