/*-
 * ========================LICENSE_START=================================
 * VEC 2.X
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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
package com.foursoft.harness.vec.v2x.navigations;

import com.foursoft.harness.vec.common.traversal.MultiNavigation;
import com.foursoft.harness.vec.common.traversal.Navigations;
import com.foursoft.harness.vec.v2x.*;
import com.foursoft.harness.vec.v2x.traversal.PlaceableElementRoles;
import com.foursoft.harness.vec.v2x.traversal.Placements;
import com.foursoft.harness.vec.v2x.traversal.ViewItems;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Navigation methods for getting {@link VecPlacement}s.
 *
 * @deprecated These navigations start at three different source types and are therefore spread over the
 * catalogs of those types: {@link PlaceableElementRoles}, {@link Placements} and {@link ViewItems}.
 */
@Deprecated(forRemoval = true)
public final class PlacementNavs {

    private PlacementNavs() {
        // hide default constructor
    }

    /**
     * @deprecated Use {@link PlaceableElementRoles#toOnPointPlacements()} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecPlaceableElementRole, Stream<VecOnPointPlacement>> onPointPlacement() {
        return PlaceableElementRoles.toOnPointPlacements();
    }

    /**
     * @deprecated Use {@link PlaceableElementRoles#toOnWayPlacements()} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecPlaceableElementRole, Stream<VecOnWayPlacement>> onWayPlacement() {
        return PlaceableElementRoles.toOnWayPlacements();
    }

    /**
     * Returns the locations from a {@link VecOccurrenceOrUsageViewItem3D} or {@link VecOccurrenceOrUsageViewItem2D}.
     *
     * @param placement Placement Navigation method.
     * @return A function to get the locations from a
     * {@link VecOccurrenceOrUsageViewItem3D} or {@link VecOccurrenceOrUsageViewItem2D}.
     * @see #onPointPlacement()
     * @deprecated Compose the navigation at the call site instead, which also works for
     * {@link PlaceableElementRoles#toOnWayPlacements()}:
     * {@code ViewItems.toPlaceableElementRole().then(PlaceableElementRoles.toOnPointPlacements())
     * .then(Placements.toLocations())}.
     */
    @Deprecated(forRemoval = true)
    public static Function<HasOccurrenceOrUsages, List<VecLocation>> locationsOf(
            final Function<VecPlaceableElementRole, Stream<VecOnPointPlacement>> placement) {
        final MultiNavigation<HasOccurrenceOrUsages, VecLocation> locations = ViewItems.toPlaceableElementRole()
                .then(Navigations.stream(placement))
                .then(Placements.toLocations());
        return locations::listFrom;
    }

    /**
     * @deprecated Use {@code Placements.toLocations().ofType(locationType)} instead.
     */
    @Deprecated(forRemoval = true)
    public static <T extends VecLocation> Function<VecOnWayPlacement, List<T>> locationsWith(
            final Class<T> locationType) {
        final MultiNavigation<VecPlacement, T> locations = Placements.toLocations().ofType(locationType);
        return locations::listFrom;
    }

}
