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

import com.foursoft.harness.vec.common.exception.VecException;
import com.foursoft.harness.vec.common.traversal.MultiNavigation;
import com.foursoft.harness.vec.v12x.VecLocation;
import com.foursoft.harness.vec.v12x.VecOnPointPlacement;
import com.foursoft.harness.vec.v12x.VecOnWayPlacement;
import com.foursoft.harness.vec.v12x.VecPlacement;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * Navigations starting at a {@link VecPlacement}.
 * <p>
 * To restrict the result to a certain kind of location, narrow the navigation at the call site with
 * {@code Placements.toLocations().ofType(VecNodeLocation.class)}.
 */
public final class Placements {

    private Placements() {
        // hide default constructor
    }

    /**
     * Navigates to the locations of a placement, regardless of how the placement expresses them: a
     * {@link VecOnPointPlacement} holds them in a list, a {@link VecOnWayPlacement} as its start and end
     * location. Unset locations are skipped.
     *
     * @return A navigation to the locations of a placement.
     */
    public static MultiNavigation<VecPlacement, VecLocation> toLocations() {
        return placement -> switch (placement) {
            case final VecOnPointPlacement onPointPlacement -> onPointPlacement.getLocations().stream()
                    .filter(Objects::nonNull);
            case final VecOnWayPlacement onWayPlacement -> Stream
                    .of(onWayPlacement.getStartLocation(), onWayPlacement.getEndLocation())
                    .filter(Objects::nonNull);
            default -> throw unhandledSubType(placement);
        };
    }

    private static VecException unhandledSubType(final VecPlacement placement) {
        return new VecException("Unhandled sub type of VecPlacement: " + placement.getClass().getName());
    }

}
