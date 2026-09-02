/*-
 * ========================LICENSE_START=================================
 * VEC 1.2.X
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
package com.foursoft.harness.vec.v12x.traversal;

import com.foursoft.harness.vec.v12x.VecOnPointPlacement;
import com.foursoft.harness.vec.v12x.VecOnWayPlacement;
import com.foursoft.harness.vec.v12x.VecPlaceableElementRole;
import com.foursoft.harness.vec.v12x.navigations.PlacementNavs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceableElementRolesTest {

    private final VecOnPointPlacement onPointPlacement = new VecOnPointPlacement();
    private final VecOnWayPlacement onWayPlacement = new VecOnWayPlacement();

    private VecPlaceableElementRole role;

    @BeforeEach
    void setUp() {
        role = new VecPlaceableElementRole();
        role.getRefPlacement().add(onPointPlacement);
        role.getRefPlacement().add(onWayPlacement);
    }

    @Test
    void navigatesToAllPlacementsOfARole() {
        assertThat(PlaceableElementRoles.toPlacements().listFrom(role))
                .containsExactlyInAnyOrder(onPointPlacement, onWayPlacement);
    }

    @Test
    void navigatesToThePlacementsOfTheRequestedKind() {
        assertThat(PlaceableElementRoles.toOnPointPlacements().listFrom(role))
                .containsExactly(onPointPlacement);
        assertThat(PlaceableElementRoles.toOnWayPlacements().listFrom(role))
                .containsExactly(onWayPlacement);
    }

    @Test
    void navigatesToNoPlacementForARoleWithoutPlacements() {
        assertThat(PlaceableElementRoles.toPlacements().listFrom(new VecPlaceableElementRole())).isEmpty();
    }

    /**
     * Characterisation test for the deprecated {@link PlacementNavs}, which delegates to
     * {@link PlaceableElementRoles}. Can be removed together with {@link PlacementNavs}.
     */
    @Test
    @SuppressWarnings({"deprecation", "removal"})
    void deprecatedNavigationsBehaveLikeTheirReplacement() {
        assertThat(PlacementNavs.onPointPlacement().apply(role))
                .containsExactlyElementsOf(PlaceableElementRoles.toOnPointPlacements().listFrom(role));
        assertThat(PlacementNavs.onWayPlacement().apply(role))
                .containsExactlyElementsOf(PlaceableElementRoles.toOnWayPlacements().listFrom(role));
    }

}
