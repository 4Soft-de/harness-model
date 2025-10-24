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
package com.foursoft.harness.kbl2vec.transform.geometry.geo_2d;

import com.foursoft.harness.kbl.common.HasAliasId;
import com.foursoft.harness.kbl.common.HasIdentification;
import com.foursoft.harness.kbl.common.HasPlacement;
import com.foursoft.harness.kbl.v25.ConnectionOrOccurrence;
import com.foursoft.harness.kbl.v25.KblAliasIdentification;
import com.foursoft.harness.kbl.v25.KblTransformation;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v2x.VecAliasIdentification;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsageViewItem2D;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecTransformation2D;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class OccurrenceOrUsageViewItem2DTransformer
        implements Transformer<ConnectionOrOccurrence, VecOccurrenceOrUsageViewItem2D> {

    @Override
    public TransformationResult<VecOccurrenceOrUsageViewItem2D> transform(final TransformationContext context,
                                                                          final ConnectionOrOccurrence source) {
        if (source instanceof final HasPlacement<?> hasPlacement) {
            final VecOccurrenceOrUsageViewItem2D destination = new VecOccurrenceOrUsageViewItem2D();

            if (source instanceof final HasIdentification hasIdentification && StringUtils.isNotBlank(
                    hasIdentification.getId())) {
                destination.setIdentification(hasIdentification.getId());
            } else {
                context.getLogger().warn("Occurrence (xml ID: {}) has no ID.", source.getXmlId());
                destination.setIdentification("ViewItem2D_" + source.getXmlId());
            }

            final TransformationResult.Builder<VecOccurrenceOrUsageViewItem2D> builder = TransformationResult.from(
                    destination);

            if (source instanceof final HasAliasId<?> hasAliasId) {
                final List<KblAliasIdentification> aliasIds = hasAliasId.getAliasIds().stream()
                        .flatMap(StreamUtils.ofClass(KblAliasIdentification.class))
                        .toList();
                builder.withDownstream(KblAliasIdentification.class, VecAliasIdentification.class,
                                       Query.fromLists(aliasIds), VecOccurrenceOrUsageViewItem2D::getAliasIds);
            }

            final KblTransformation transformation = (KblTransformation) hasPlacement.getPlacement();
            return builder
                    .withDownstream(KblTransformation.class, VecTransformation2D.class, Query.of(transformation),
                                    VecOccurrenceOrUsageViewItem2D::setOrientation)
                    .withLinker(Query.of(source), VecPartOccurrence.class,
                                VecOccurrenceOrUsageViewItem2D::getOccurrenceOrUsage)
                    .build();
        }
        return TransformationResult.noResult();
    }
}
