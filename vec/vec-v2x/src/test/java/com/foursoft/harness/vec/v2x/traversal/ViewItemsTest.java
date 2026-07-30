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

import com.foursoft.harness.vec.common.traversal.MultiNavigation;
import com.foursoft.harness.vec.v2x.HasOccurrenceOrUsages;
import com.foursoft.harness.vec.v2x.VecLocation;
import com.foursoft.harness.vec.v2x.VecNodeLocation;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsage;
import com.foursoft.harness.vec.v2x.VecOnPointPlacement;
import com.foursoft.harness.vec.v2x.VecOnWayPlacement;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import com.foursoft.harness.vec.v2x.VecSegmentLocation;
import com.foursoft.harness.vec.v2x.navigations.PlacementNavs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ViewItemsTest {

    private static final MultiNavigation<HasOccurrenceOrUsages, VecLocation> ON_POINT_LOCATIONS =
            ViewItems.placeableElementRole()
                    .thenEach(PlaceableElementRoles.onPointPlacements())
                    .thenEach(Placements.locations());
    private static final MultiNavigation<HasOccurrenceOrUsages, VecLocation> ON_WAY_LOCATIONS =
            ViewItems.placeableElementRole()
                    .thenEach(PlaceableElementRoles.onWayPlacements())
                    .thenEach(Placements.locations());

    private final VecNodeLocation nodeLocation = new VecNodeLocation();
    private final VecSegmentLocation segmentLocation = new VecSegmentLocation();

    private VecPlaceableElementRole role;
    private HasOccurrenceOrUsages viewItem;

    @BeforeEach
    void setUp() {
        final VecOnPointPlacement onPointPlacement = new VecOnPointPlacement();
        onPointPlacement.getLocations().add(nodeLocation);

        final VecOnWayPlacement onWayPlacement = new VecOnWayPlacement();
        onWayPlacement.setStartLocation(segmentLocation);
        onWayPlacement.setEndLocation(nodeLocation);

        role = new VecPlaceableElementRole();
        role.getRefPlacement().add(onPointPlacement);
        role.getRefPlacement().add(onWayPlacement);

        final VecPartOccurrence partOccurrence = new VecPartOccurrence();
        partOccurrence.getRoles().add(role);

        final List<VecOccurrenceOrUsage> occurrences = List.of(partOccurrence);
        viewItem = () -> occurrences;
    }

    @Test
    void navigatesToThePlaceableElementRoleOfAViewItem() {
        assertThat(ViewItems.placeableElementRole().from(viewItem)).contains(role);
    }

    @Test
    void navigatesToNoPlaceableElementRoleForAnEmptyViewItem() {
        assertThat(ViewItems.placeableElementRole().from(Collections::emptyList)).isEmpty();
    }

    @Test
    void navigatesToTheOnPointLocationsOfAViewItem() {
        assertThat(ON_POINT_LOCATIONS.listFrom(viewItem)).containsExactly(nodeLocation);
    }

    /**
     * Since the way to the locations is composed at the call site, on way placements work just as well. The
     * deprecated {@link PlacementNavs#locationsOf(java.util.function.Function)} documented this, but its
     * parameter type only allowed on point placements.
     */
    @Test
    void navigatesToTheOnWayLocationsOfAViewItem() {
        assertThat(ON_WAY_LOCATIONS.listFrom(viewItem)).containsExactly(segmentLocation, nodeLocation);
    }

    @Test
    void navigatesToTheLocationsOfEveryPlacementKindAtOnce() {
        final MultiNavigation<HasOccurrenceOrUsages, VecLocation> allLocations =
                ViewItems.placeableElementRole()
                        .thenEach(PlaceableElementRoles.placements())
                        .thenEach(Placements.locations());

        assertThat(allLocations.listFrom(viewItem))
                .containsExactlyInAnyOrder(nodeLocation, segmentLocation, nodeLocation);
    }

    @Test
    void navigatesToNoLocationForAnEmptyViewItem() {
        assertThat(ON_POINT_LOCATIONS.listFrom(Collections::emptyList)).isEmpty();
    }

    /**
     * Characterisation test for the deprecated {@link PlacementNavs}, which delegates to {@link ViewItems}.
     * Can be removed together with {@link PlacementNavs}.
     */
    @Test
    @SuppressWarnings({"deprecation", "removal"})
    void deprecatedNavigationsBehaveLikeTheirReplacement() {
        assertThat(PlacementNavs.locationsOf(PlacementNavs.onPointPlacement()).apply(viewItem))
                .isEqualTo(ON_POINT_LOCATIONS.listFrom(viewItem));
        assertThat(PlacementNavs.locationsOf(PlacementNavs.onPointPlacement()).apply(Collections::emptyList))
                .isEmpty();
    }

}
