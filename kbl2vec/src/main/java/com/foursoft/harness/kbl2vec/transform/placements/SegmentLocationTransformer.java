/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.transform.placements;

import com.foursoft.harness.kbl.v25.KblFixingAssignment;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.v2x.VecSegmentLocation;

import java.util.List;

import static java.util.Comparator.comparing;

public class SegmentLocationTransformer extends AbstractSegmentLocationTransformer<KblFixingAssignment>
        implements Transformer<KblFixingAssignment, VecSegmentLocation> {

    @Override
    protected LocationData extractLocationData(final KblFixingAssignment source) {
        final List<KblFixingAssignment> assignments = source.getFixing()
                .getRefFixingAssignment()
                .stream()
                .sorted(comparing(Identifiable::getXmlId))
                .toList();

        final String PATTERN = "%1$s-%2$s";

        return new LocationData(source.getLocation(), source.getAbsoluteLocation(),
                                PATTERN.formatted(Constants.FIXING_LOCATION_ID, assignments.indexOf(source)),
                                source.getParentSegment());
    }
}
