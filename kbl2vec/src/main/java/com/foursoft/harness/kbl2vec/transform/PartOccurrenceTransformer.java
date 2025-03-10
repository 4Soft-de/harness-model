package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.common.HasDescription;
import com.foursoft.harness.kbl.common.HasIdentification;
import com.foursoft.harness.kbl.common.HasPart;
import com.foursoft.harness.kbl.v25.ConnectionOrOccurrence;
import com.foursoft.harness.kbl2vec.convert.Converter;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class PartOccurrenceTransformer implements Transformer<ConnectionOrOccurrence, VecPartOccurrence> {

    private int idCounter = 0;

    @Override public TransformationResult<VecPartOccurrence> transform(final TransformationContext context,
                                                                       final ConnectionOrOccurrence source) {
        if (source instanceof final HasPart<?> hasPart) {
            final VecPartOccurrence occurrence = new VecPartOccurrence();
            final TransformationResult.Builder<VecPartOccurrence> builder = TransformationResult.from(occurrence);

            builder.withLinker(Query.of(hasPart.getPart()), VecPartVersion.class, occurrence::setPart);

            if (source instanceof final HasIdentification hasIdentification && StringUtils.isNotBlank(
                    hasIdentification.getId())) {
                occurrence.setIdentification(hasIdentification.getId());
            } else {
                builder.withComment("This occurence has no \"Id\" in the KBL data.");
                occurrence.setIdentification("GenericIdentifier-" + idCounter++);
            }
            handleDescription(source, occurrence, context);
            return builder.build();
        }
        return TransformationResult.noResult();
    }

    private static void handleDescription(final ConnectionOrOccurrence source,
                                          final VecPartOccurrence occurrence, final TransformationContext context) {
        if (source instanceof final HasDescription hasDescription) {
            final Converter<String, Optional<VecLocalizedString>> stringConverter =
                    context.getConverterRegistry().getStringToLocalizedString();

            stringConverter.convert(hasDescription.getDescription())
                    .ifPresent(v -> occurrence.getDescriptions()
                            .add(v));

        }
    }
}

