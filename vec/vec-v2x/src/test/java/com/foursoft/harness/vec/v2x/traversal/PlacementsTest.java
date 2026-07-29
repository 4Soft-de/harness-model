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

class PlacementsTest {

    private VecNodeLocation nodeLocation;
    private VecSegmentLocation segmentLocation;
    private VecOnPointPlacement onPointPlacement;
    private VecOnWayPlacement onWayPlacement;
    private VecPlaceableElementRole role;
    private HasOccurrenceOrUsages viewItem;

    @BeforeEach
    void setUp() {
        nodeLocation = new VecNodeLocation();
        segmentLocation = new VecSegmentLocation();

        onPointPlacement = new VecOnPointPlacement();
        onPointPlacement.getLocations().add(nodeLocation);

        onWayPlacement = new VecOnWayPlacement();
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
    void navigatesToAllPlacementsOfARole() {
        assertThat(Placements.placements().listFrom(role))
                .containsExactlyInAnyOrder(onPointPlacement, onWayPlacement);
    }

    @Test
    void navigatesToThePlacementsOfTheRequestedKind() {
        assertThat(Placements.onPointPlacement().listFrom(role)).containsExactly(onPointPlacement);
        assertThat(Placements.onWayPlacement().listFrom(role)).containsExactly(onWayPlacement);
    }

    @Test
    void navigatesToTheLocationsOfAnOnPointPlacement() {
        assertThat(Placements.onPointLocations().listFrom(onPointPlacement)).containsExactly(nodeLocation);
    }

    @Test
    void navigatesToTheStartAndEndLocationOfAnOnWayPlacement() {
        assertThat(Placements.onWayLocations().listFrom(onWayPlacement))
                .containsExactly(segmentLocation, nodeLocation);
    }

    @Test
    void skipsAnUnsetStartOrEndLocation() {
        onWayPlacement.setStartLocation(null);

        assertThat(Placements.onWayLocations().listFrom(onWayPlacement)).containsExactly(nodeLocation);
    }

    @Test
    void navigatesToTheOnWayLocationsOfTheRequestedType() {
        assertThat(Placements.onWayLocationsWith(VecNodeLocation.class).listFrom(onWayPlacement))
                .containsExactly(nodeLocation);
        assertThat(Placements.onWayLocationsWith(VecSegmentLocation.class).listFrom(onWayPlacement))
                .containsExactly(segmentLocation);
    }

    @Test
    void navigatesToThePlaceableElementRoleOfAViewItem() {
        assertThat(Placements.placeableElementRole().from(viewItem)).contains(role);
    }

    @Test
    void navigatesToNoPlaceableElementRoleForAnEmptyViewItem() {
        assertThat(Placements.placeableElementRole().from(Collections::emptyList)).isEmpty();
    }

    @Test
    void navigatesToTheOnPointLocationsOfAViewItem() {
        final MultiNavigation<VecPlaceableElementRole, VecLocation> locations =
                Placements.onPointPlacement().thenEach(Placements.onPointLocations());

        assertThat(Placements.locationsOf(locations).listFrom(viewItem)).containsExactly(nodeLocation);
    }

    /**
     * The way from the role to the locations is a parameter of
     * {@link Placements#locationsOf(MultiNavigation)}, so on way placements can be used as well. The
     * deprecated {@link PlacementNavs#locationsOf(java.util.function.Function)} documented this, but its
     * parameter type only allowed on point placements.
     */
    @Test
    void navigatesToTheOnWayLocationsOfAViewItem() {
        final MultiNavigation<VecPlaceableElementRole, VecLocation> locations =
                Placements.onWayPlacement().thenEach(Placements.onWayLocations());

        assertThat(Placements.locationsOf(locations).listFrom(viewItem))
                .containsExactly(segmentLocation, nodeLocation);
    }

    @Test
    void navigatesToNoLocationForAnEmptyViewItem() {
        final MultiNavigation<VecPlaceableElementRole, VecLocation> locations =
                Placements.onPointPlacement().thenEach(Placements.onPointLocations());

        assertThat(Placements.locationsOf(locations).listFrom(Collections::emptyList)).isEmpty();
    }

    /**
     * Characterisation test for the deprecated {@link PlacementNavs}, which delegates to {@link Placements}.
     * Can be removed together with {@link PlacementNavs}.
     */
    @Test
    @SuppressWarnings({"deprecation", "removal"})
    void deprecatedNavigationsBehaveLikeTheirReplacement() {
        assertThat(PlacementNavs.onPointPlacement().apply(role))
                .containsExactlyElementsOf(Placements.onPointPlacement().listFrom(role));
        assertThat(PlacementNavs.onWayPlacement().apply(role))
                .containsExactlyElementsOf(Placements.onWayPlacement().listFrom(role));

        assertThat(PlacementNavs.locationsOf(PlacementNavs.onPointPlacement()).apply(viewItem))
                .isEqualTo(Placements.locationsOf(
                                Placements.onPointPlacement().thenEach(Placements.onPointLocations()))
                                   .listFrom(viewItem));
        assertThat(PlacementNavs.locationsOf(PlacementNavs.onPointPlacement()).apply(Collections::emptyList))
                .isEmpty();

        assertThat(PlacementNavs.locationsWith(VecNodeLocation.class).apply(onWayPlacement))
                .isEqualTo(Placements.onWayLocationsWith(VecNodeLocation.class).listFrom(onWayPlacement));
    }

}
