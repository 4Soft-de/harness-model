/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2025 4Soft GmbH
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
package com.foursoft.harness.vec.scripting.placement;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.DefaultValues;
import com.foursoft.harness.vec.scripting.Locator;
import com.foursoft.harness.vec.scripting.VecScriptingException;
import com.foursoft.harness.vec.v2x.*;

public class PlacementSpecificationBuilder implements Builder<VecPlacementSpecification> {

    private final VecPlacementSpecification placementSpecification;
    private final Locator<VecPartOccurrence> occurrenceLocator;
    private final Locator<VecTopologyNode> nodeLocator;

    public PlacementSpecificationBuilder(Locator<VecPartOccurrence> occurrenceLocator,
                                         Locator<VecTopologyNode> nodeLocator) {
        this.occurrenceLocator = occurrenceLocator;
        this.nodeLocator = nodeLocator;
        this.placementSpecification = initializeSpecification();
    }

    @Override public VecPlacementSpecification build() {
        return placementSpecification;
    }

    private VecPlacementSpecification initializeSpecification() {
        VecPlacementSpecification placementSpecification = new VecPlacementSpecification();
        placementSpecification.setIdentification(DefaultValues.PLACEMENT_SPECIFICATION_IDENTIFICATION);
        return placementSpecification;
    }

    public PlacementSpecificationBuilder addPlacement(String occurrenceId, String nodeId) {
        final VecPartOccurrence vecPartOccurrence = occurrenceLocator.locate(occurrenceId);
        final VecTopologyNode vecTopologyNode = nodeLocator.locate(nodeId);

        final VecPlaceableElementRole placeableElementRole = vecPartOccurrence
                .getRoleWithType(VecPlaceableElementRole.class)
                .orElseThrow(() -> new VecScriptingException("No PlaceableElementRole found for " + occurrenceId));

        VecOnPointPlacement placement = new VecOnPointPlacement();
        placement.setIdentification(occurrenceId);
        placement.getPlacedElement().add(placeableElementRole);

        VecNodeLocation vecNodeLocation = new VecNodeLocation();
        vecNodeLocation.setIdentification(occurrenceId);
        vecNodeLocation.setReferencedNode(vecTopologyNode);

        placement.getLocations().add(vecNodeLocation);

        placementSpecification.getPlacements().add(placement);

        return this;
    }

}
