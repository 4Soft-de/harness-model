package com.foursoft.harness.kbl2vec.transform.components.wires.multi_cores;

import com.foursoft.harness.kbl.v25.KblCore;
import com.foursoft.harness.kbl.v25.KblWireColour;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecColor;
import com.foursoft.harness.vec.v2x.VecInsulationSpecification;

import java.util.Optional;

public class InsulationSpecificationTransformer implements Transformer<KblCore, VecInsulationSpecification> {

    @Override
    public TransformationResult<VecInsulationSpecification> transform(final TransformationContext context,
                                                                      final KblCore source) {
        final VecInsulationSpecification destination = new VecInsulationSpecification();
        destination.setIdentification(source.getId());

        if (source.getCoreColours().size() > 3) {
            context.getLogger().warn("'{}' has more than 3 core colours. Only the first 3 will be used.", source);
        }
        for (int i = 0; i < source.getCoreColours().size(); i++) {
            final KblWireColour currentColour = source.getCoreColours().get(i);
            final Optional<VecColor> vecColor = context.getConverterRegistry().getStringToColorConverter().convert(
                    currentColour.getColourValue());
            if (vecColor.isPresent()) {
                switch (i) {
                    case 0 -> destination.getBaseColors().add(vecColor.get());
                    case 1 -> destination.getFirstIdentificationColors().add(vecColor.get());
                    case 2 -> destination.getSecondIdentificationColors().add(vecColor.get());
                    default -> context.getLogger().warn("Ignoring core colour {} for '{}'. Only the first 3 are used.",
                                                        currentColour, source);
                }
            }
        }

        final TransformationResult.Builder<VecInsulationSpecification> builder = TransformationResult.from(destination);
        if (!destination.getBaseColors().isEmpty()) {
            builder.withCommentOnDetail(destination.getBaseColors().get(0),
                                        "Assignment to colortypes based on order in KBL file. Dialect specific color " +
                                                "type mapping required.");
        }
        return builder.build();
    }
}
