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
import com.foursoft.harness.vec.v2x.HasOccurrenceOrUsages;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsage;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsageViewItem2D;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsageViewItem3D;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;

/**
 * Navigations starting at a {@link HasOccurrenceOrUsages}, that is a {@link VecOccurrenceOrUsageViewItem2D}
 * or {@link VecOccurrenceOrUsageViewItem3D}.
 * <p>
 * To reach the locations of a view item, continue this navigation at the call site, for example
 * <pre>
 * {@code
 * ViewItems.toPlaceableElementRole()
 *         .then(PlaceableElementRoles.toOnWayPlacements())
 *         .then(Placements.toLocations());
 * }
 * </pre>
 */
public final class ViewItems {

    private ViewItems() {
        // hide default constructor
    }

    /**
     * Navigates to the occurrences or usages a view item shows.
     *
     * @return A navigation to the occurrences or usages of a view item.
     */
    public static MultiNavigation<HasOccurrenceOrUsages, VecOccurrenceOrUsage> toOccurrenceOrUsages() {
        return Navigations.collection(HasOccurrenceOrUsages::getOccurrenceOrUsage);
    }

    /**
     * Navigates to the {@link VecPlaceableElementRole} of a view item.
     * <p>
     * A view item is expected to reference at most one placeable element role. If there are several ones, the
     * first is chosen, see {@link MultiNavigation#atMostOne()}.
     *
     * @return A navigation to the placeable element role of a view item.
     */
    public static SingleNavigation<HasOccurrenceOrUsages, VecPlaceableElementRole> toPlaceableElementRole() {
        return toOccurrenceOrUsages()
                .ofType(VecPartOccurrence.class)
                .then(Navigations.collection(VecOccurrenceOrUsage::getRoles))
                .ofType(VecPlaceableElementRole.class)
                .atMostOne();
    }

}
