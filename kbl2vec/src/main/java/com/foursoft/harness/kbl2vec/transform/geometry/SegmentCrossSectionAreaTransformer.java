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
package com.foursoft.harness.kbl2vec.transform.geometry;

import com.foursoft.harness.kbl.v25.KblCrossSectionArea;
import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl.v25.KblValueDetermination;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import com.foursoft.harness.vec.v2x.VecSegmentCrossSectionArea;
import com.foursoft.harness.vec.v2x.VecValueDetermination;

public class SegmentCrossSectionAreaTransformer
        implements Transformer<KblCrossSectionArea, VecSegmentCrossSectionArea> {

    @Override
    public TransformationResult<VecSegmentCrossSectionArea> transform(final TransformationContext context,
                                                                      final KblCrossSectionArea source) {
        final VecSegmentCrossSectionArea destination = new VecSegmentCrossSectionArea();

        final KblValueDetermination valueDetermination = source.getValueDetermination();
        if (valueDetermination == KblValueDetermination.CALCULATED ||
                valueDetermination == KblValueDetermination.RESERVED) {
            destination.setValueDetermination(VecValueDetermination.CALCULATED);
            destination.setCrossSectionAreaType("Reserved");
        } else if (valueDetermination == KblValueDetermination.MEASURED) {
            destination.setValueDetermination(VecValueDetermination.MEASURED);
            destination.setCrossSectionAreaType("Real");
        }

        return TransformationResult.from(destination)
                .withDownstream(KblNumericalValue.class, VecNumericalValue.class, Query.of(source.getArea()),
                                VecSegmentCrossSectionArea::setArea)
                .build();
    }
}
