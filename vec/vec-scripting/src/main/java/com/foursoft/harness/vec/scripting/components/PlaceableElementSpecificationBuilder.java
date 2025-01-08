package com.foursoft.harness.vec.scripting.components;

import com.foursoft.harness.vec.scripting.core.PartOrUsageRelatedSpecificationBuilder;
import com.foursoft.harness.vec.v2x.VecPlaceableElementSpecification;
import com.foursoft.harness.vec.v2x.VecPlacementType;

public class PlaceableElementSpecificationBuilder
        extends PartOrUsageRelatedSpecificationBuilder<VecPlaceableElementSpecification> {

    private final VecPlaceableElementSpecification placeableElementSpecification;

    public PlaceableElementSpecificationBuilder(final String partNumber, VecPlacementType placementType) {
        placeableElementSpecification = initializeSpecification(VecPlaceableElementSpecification.class, partNumber);
        placeableElementSpecification.getValidPlacementTypes().add(placementType);
    }

    @Override public VecPlaceableElementSpecification build() {
        return placeableElementSpecification;
    }
}
