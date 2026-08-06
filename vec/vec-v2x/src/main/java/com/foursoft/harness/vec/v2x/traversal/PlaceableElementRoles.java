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
import com.foursoft.harness.vec.v2x.VecOnPointPlacement;
import com.foursoft.harness.vec.v2x.VecOnWayPlacement;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import com.foursoft.harness.vec.v2x.VecPlacement;

/**
 * Navigations starting at a {@link VecPlaceableElementRole}.
 */
public final class PlaceableElementRoles {

    private PlaceableElementRoles() {
        // hide default constructor
    }

    /**
     * Navigates to all placements of a role.
     *
     * @return A navigation to the placements of a role.
     */
    public static MultiNavigation<VecPlaceableElementRole, VecPlacement> toPlacements() {
        return Navigations.collection(VecPlaceableElementRole::getRefPlacement);
    }

    /**
     * Navigates to the {@link VecOnPointPlacement}s of a role.
     *
     * @return A navigation to the on point placements of a role.
     */
    public static MultiNavigation<VecPlaceableElementRole, VecOnPointPlacement> toOnPointPlacements() {
        return toPlacements().ofType(VecOnPointPlacement.class);
    }

    /**
     * Navigates to the {@link VecOnWayPlacement}s of a role.
     *
     * @return A navigation to the on way placements of a role.
     */
    public static MultiNavigation<VecPlaceableElementRole, VecOnWayPlacement> toOnWayPlacements() {
        return toPlacements().ofType(VecOnWayPlacement.class);
    }

}
