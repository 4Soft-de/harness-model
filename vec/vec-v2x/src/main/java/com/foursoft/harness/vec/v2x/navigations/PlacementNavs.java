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

import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v2x.*;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Navigation methods for getting {@link VecPlacement}s.
 */
public final class PlacementNavs {

    private PlacementNavs() {
        // hide default constructor
    }

    public static Function<VecPlaceableElementRole, Stream<VecOnPointPlacement>> onPointPlacement() {
        return role -> role.getRefPlacement().stream()
                .filter(VecOnPointPlacement.class::isInstance)
                .map(VecOnPointPlacement.class::cast);
    }

    public static Function<VecPlaceableElementRole, Stream<VecOnWayPlacement>> onWayPlacement() {
        return role -> role.getRefPlacement().stream()
                .filter(VecOnWayPlacement.class::isInstance)
                .map(VecOnWayPlacement.class::cast);
    }

    /**
     * Returns the locations from a {@link VecOccurrenceOrUsageViewItem3D} or {@link VecOccurrenceOrUsageViewItem2D}.
     *
     * @param placement Placement Navigation method.
     * @return A function to get the locations from a
     * {@link VecOccurrenceOrUsageViewItem3D} or {@link VecOccurrenceOrUsageViewItem2D}.
     * @see #onWayPlacement()
     * @see #onPointPlacement()
     */
    public static Function<HasOccurrenceOrUsages, List<VecLocation>> locationsOf(
            final Function<VecPlaceableElementRole, Stream<VecOnPointPlacement>> placement) {
        return viewItem -> viewItem.getOccurrenceOrUsage().stream()
                .filter(VecPartOccurrence.class::isInstance)
                .map(VecPartOccurrence.class::cast)
                .flatMap(StreamUtils.toStream(c -> c.getRolesWithType(VecPlaceableElementRole.class)))
                .collect(StreamUtils.findOneOrNone())
                .map(role -> placement.apply(role)
                        .map(VecOnPointPlacement::getLocations)
                        .flatMap(List::stream)
                        .toList())
                .orElseGet(Collections::emptyList);
    }

    public static <T extends VecLocation> Function<VecOnWayPlacement, List<T>> locationsWith(
            final Class<T> locationType) {
        return placement ->
                getLocationsByType(Stream.of(placement.getStartLocation(), placement.getEndLocation()), locationType)
                        .toList();
    }

    private static <T extends VecLocation> Stream<T> getLocationsByType(final Stream<VecLocation> locations,
                                                                        final Class<T> locationType) {
        return locations
                .filter(locationType::isInstance)
                .map(locationType::cast);
    }

}
