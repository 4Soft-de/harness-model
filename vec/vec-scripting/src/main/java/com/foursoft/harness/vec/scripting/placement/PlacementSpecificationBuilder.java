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
