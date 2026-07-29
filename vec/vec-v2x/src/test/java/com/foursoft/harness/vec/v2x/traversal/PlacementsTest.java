/*-
 * ========================LICENSE_START=================================
 * VEC 2.X
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
package com.foursoft.harness.vec.v2x.traversal;

import com.foursoft.harness.vec.v2x.VecNodeLocation;
import com.foursoft.harness.vec.v2x.VecOnPointPlacement;
import com.foursoft.harness.vec.v2x.VecOnWayPlacement;
import com.foursoft.harness.vec.v2x.VecSegmentLocation;
import com.foursoft.harness.vec.v2x.navigations.PlacementNavs;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlacementsTest {

    private final VecNodeLocation nodeLocation = new VecNodeLocation();
    private final VecSegmentLocation segmentLocation = new VecSegmentLocation();

    private VecOnPointPlacement onPointPlacement(final VecNodeLocation... locations) {
        final VecOnPointPlacement placement = new VecOnPointPlacement();
        for (final VecNodeLocation location : locations) {
            placement.getLocations().add(location);
        }
        return placement;
    }

    private VecOnWayPlacement onWayPlacement() {
        final VecOnWayPlacement placement = new VecOnWayPlacement();
        placement.setStartLocation(segmentLocation);
        placement.setEndLocation(nodeLocation);
        return placement;
    }

    @Test
    void navigatesToTheLocationsOfAnOnPointPlacement() {
        assertThat(Placements.locations().listFrom(onPointPlacement(nodeLocation)))
                .containsExactly(nodeLocation);
    }

    @Test
    void navigatesToTheStartAndEndLocationOfAnOnWayPlacement() {
        assertThat(Placements.locations().listFrom(onWayPlacement()))
                .containsExactly(segmentLocation, nodeLocation);
    }

    @Test
    void navigatesToNoLocationForAPlacementWithoutLocations() {
        assertThat(Placements.locations().listFrom(onPointPlacement())).isEmpty();
        assertThat(Placements.locations().listFrom(new VecOnWayPlacement())).isEmpty();
    }

    @Test
    void skipsAnUnsetStartOrEndLocation() {
        final VecOnWayPlacement placement = onWayPlacement();
        placement.setStartLocation(null);

        assertThat(Placements.locations().listFrom(placement)).containsExactly(nodeLocation);
    }

    @Test
    void navigatesToTheLocationsOfTheRequestedType() {
        final VecOnWayPlacement placement = onWayPlacement();

        assertThat(Placements.locationsWith(VecNodeLocation.class).listFrom(placement))
                .containsExactly(nodeLocation);
        assertThat(Placements.locationsWith(VecSegmentLocation.class).listFrom(placement))
                .containsExactly(segmentLocation);
    }

    /**
     * Characterisation test for the deprecated {@link PlacementNavs}, which delegates to {@link Placements}.
     * Can be removed together with {@link PlacementNavs}.
     */
    @Test
    @SuppressWarnings({"deprecation", "removal"})
    void deprecatedNavigationsBehaveLikeTheirReplacement() {
        final VecOnWayPlacement placement = onWayPlacement();

        assertThat(PlacementNavs.locationsWith(VecNodeLocation.class).apply(placement))
                .isEqualTo(Placements.locationsWith(VecNodeLocation.class).listFrom(placement));
        assertThat(PlacementNavs.locationsWith(VecSegmentLocation.class).apply(placement))
                .isEqualTo(Placements.locationsWith(VecSegmentLocation.class).listFrom(placement));
    }

}
