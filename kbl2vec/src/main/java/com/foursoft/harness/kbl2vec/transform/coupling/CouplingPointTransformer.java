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

import com.foursoft.harness.kbl.v25.KblComponentOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentSlotOccurrence;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.kbl2vec.transform.components.ee_components.ComponentCavities;
import com.foursoft.harness.kbl2vec.transform.components.ee_components.ComponentCavities.MountedSlot;
import com.foursoft.harness.vec.v2x.VecConnectorHousingRole;
import com.foursoft.harness.vec.v2x.VecCouplingPoint;
import com.foursoft.harness.vec.v2x.VecSlotCoupling;

import java.util.List;
import java.util.Optional;

/**
 * Couples the housing of a mounted component with the housing it is mounted on: a component slot, or the component
 * slot / connector of the cavities it is mounted on. If it is mounted on cavities, the slots and cavities are coupled
 * as well, with the cavities derived for the component's housing (see {@link ComponentCavities}). A mounted component
 * box is not evaluated, it is the component box of the mounted slot.
 */
public class CouplingPointTransformer implements Transformer<KblComponentOccurrence, VecCouplingPoint> {

    @Override
    public TransformationResult<VecCouplingPoint> transform(final TransformationContext context,
                                                            final KblComponentOccurrence source) {
        final Optional<Object> housing = Mountings.housing(source);
        if (housing.isEmpty()) {
            return TransformationResult.noResult();
        }
        final long componentSlots = source.getMounting().stream()
                .filter(KblComponentSlotOccurrence.class::isInstance)
                .count();
        if (componentSlots > 1) {
            context.getLogger().warn("'{}' is mounted on {} component slots, only the first one is coupled.", source,
                                     componentSlots);
        }
        final List<MountedSlot> slots = Mountings.cavities(source).isEmpty()
                ? List.of()
                : List.of(new MountedSlot(source));
        final VecCouplingPoint destination = new VecCouplingPoint();
        destination.setIdentification(source.getId());
        return TransformationResult.from(destination)
                .withLinker(Query.of(housing.get()), VecConnectorHousingRole.class, VecCouplingPoint::setFirstConnector)
                .withLinker(Query.of(source), VecConnectorHousingRole.class, VecCouplingPoint::setSecondConnector)
                .withDownstream(MountedSlot.class, VecSlotCoupling.class, Query.fromLists(slots),
                                VecCouplingPoint::getSlotCouplings)
                .build();
    }
}
