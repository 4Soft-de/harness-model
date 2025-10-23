package com.foursoft.harness.kbl2vec.transform.geometry.geo_2d;

import com.foursoft.harness.kbl.common.HasAliasId;
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

import java.util.List;

public class OccurrenceOrUsageViewItem2DTransformer
        implements Transformer<ConnectionOrOccurrence, VecOccurrenceOrUsageViewItem2D> {

    @Override
    public TransformationResult<VecOccurrenceOrUsageViewItem2D> transform(final TransformationContext context,
                                                                          final ConnectionOrOccurrence source) {
        final VecOccurrenceOrUsageViewItem2D destination = new VecOccurrenceOrUsageViewItem2D();
        destination.setIdentification("ViewItem2D_" + source.getXmlId());

        final TransformationResult.Builder<VecOccurrenceOrUsageViewItem2D> builder = TransformationResult.from(
                destination);

        if (source instanceof final HasPlacement<?> hasPlacement) {
            final KblTransformation transformation = (KblTransformation) hasPlacement.getPlacement();
            builder.withDownstream(KblTransformation.class, VecTransformation2D.class, Query.of(transformation),
                                   VecOccurrenceOrUsageViewItem2D::setOrientation);
        }

        if (source instanceof final HasAliasId<?> hasAliasId) {
            final List<KblAliasIdentification> aliasIds = hasAliasId.getAliasIds().stream()
                    .flatMap(StreamUtils.ofClass(KblAliasIdentification.class))
                    .toList();
            builder.withDownstream(KblAliasIdentification.class, VecAliasIdentification.class,
                                   Query.fromLists(aliasIds), VecOccurrenceOrUsageViewItem2D::getAliasIds);
        }

        return builder
                .withLinker(Query.of(source), VecPartOccurrence.class,
                            VecOccurrenceOrUsageViewItem2D::getOccurrenceOrUsage)
                .build();
    }
}
