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
import com.foursoft.harness.vec.common.traversal.Navigations;
import com.foursoft.harness.vec.common.traversal.SingleNavigation;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v2x.HasOccurrenceOrUsages;
import com.foursoft.harness.vec.v2x.VecLocation;
import com.foursoft.harness.vec.v2x.VecOnPointPlacement;
import com.foursoft.harness.vec.v2x.VecOnWayPlacement;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsageViewItem2D;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsageViewItem3D;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import com.foursoft.harness.vec.v2x.VecPlacement;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Navigations to {@link VecPlacement}s and the {@link VecLocation}s they refer to.
 */
public final class Placements {

    private Placements() {
        // hide default constructor
    }

    /**
     * Navigates to all placements of a {@link VecPlaceableElementRole}.
     *
     * @return A navigation to the placements of a role.
     */
    public static MultiNavigation<VecPlaceableElementRole, VecPlacement> placements() {
        return Navigations.collection(VecPlaceableElementRole::getRefPlacement);
    }

    /**
     * Navigates to the {@link VecOnPointPlacement}s of a {@link VecPlaceableElementRole}.
     *
     * @return A navigation to the on point placements of a role.
     */
    public static MultiNavigation<VecPlaceableElementRole, VecOnPointPlacement> onPointPlacement() {
        return placements().ofType(VecOnPointPlacement.class);
    }

    /**
     * Navigates to the {@link VecOnWayPlacement}s of a {@link VecPlaceableElementRole}.
     *
     * @return A navigation to the on way placements of a role.
     */
    public static MultiNavigation<VecPlaceableElementRole, VecOnWayPlacement> onWayPlacement() {
        return placements().ofType(VecOnWayPlacement.class);
    }

    /**
     * Navigates to the locations of a {@link VecOnPointPlacement}.
     *
     * @return A navigation to the locations of an on point placement.
     */
    public static MultiNavigation<VecOnPointPlacement, VecLocation> onPointLocations() {
        return Navigations.collection(VecOnPointPlacement::getLocations);
    }

    /**
     * Navigates to the start and end location of a {@link VecOnWayPlacement}.
     *
     * @return A navigation to the locations of an on way placement.
     */
    public static MultiNavigation<VecOnWayPlacement, VecLocation> onWayLocations() {
        return placement -> Stream.of(placement.getStartLocation(), placement.getEndLocation())
                .filter(Objects::nonNull);
    }

    /**
     * Navigates to the start and end location of a {@link VecOnWayPlacement} which have the given type.
     *
     * @param locationType Type the locations have to have.
     * @param <T>          Type to narrow the navigation to.
     * @return A navigation to the locations of the given type.
     */
    public static <T extends VecLocation> MultiNavigation<VecOnWayPlacement, T> onWayLocationsWith(
            final Class<T> locationType) {
        return onWayLocations().ofType(locationType);
    }

    /**
     * Navigates to the {@link VecPlaceableElementRole} of a {@link VecOccurrenceOrUsageViewItem3D} or
     * {@link VecOccurrenceOrUsageViewItem2D}.
     * <p>
     * A view item is expected to reference at most one placeable element role. If there are several ones, the
     * first is chosen, see {@link StreamUtils#findOneOrNone()}.
     *
     * @return A navigation to the placeable element role of a view item.
     */
    public static SingleNavigation<HasOccurrenceOrUsages, VecPlaceableElementRole> placeableElementRole() {
        return viewItem -> viewItem.getOccurrenceOrUsage().stream()
                .flatMap(StreamUtils.ofClass(VecPartOccurrence.class))
                .flatMap(StreamUtils.toStream(
                        occurrence -> occurrence.getRolesWithType(VecPlaceableElementRole.class)))
                .collect(StreamUtils.findOneOrNone());
    }

    /**
     * Navigates to the locations of a {@link VecOccurrenceOrUsageViewItem3D} or
     * {@link VecOccurrenceOrUsageViewItem2D}, using the given navigation to get from the placeable element
     * role of the view item to its locations.
     * <p>
     * Both kinds of placement can be used, as the way from the role to the locations is passed in:
     * <pre>
     * {@code
     * Placements.locationsOf(Placements.onPointPlacement().thenEach(Placements.onPointLocations()));
     * Placements.locationsOf(Placements.onWayPlacement().thenEach(Placements.onWayLocations()));
     * }
     * </pre>
     *
     * @param locations Navigation from the placeable element role to its locations.
     * @return A navigation to the locations of a view item.
     * @see #placeableElementRole()
     */
    public static MultiNavigation<HasOccurrenceOrUsages, VecLocation> locationsOf(
            final MultiNavigation<VecPlaceableElementRole, VecLocation> locations) {
        return placeableElementRole().thenEach(locations);
    }

}
