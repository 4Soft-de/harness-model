package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl2vec.convert.Converter;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationFragment;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecPartOrUsageRelatedSpecification;
import com.foursoft.harness.vec.v2x.VecPartVersion;

import java.util.Optional;

public final class Fragments {

    private Fragments() {
        throw new AssertionError("Can't instantiate utility class");
    }

    public static String abbreviatedClassName(final Class<?> clazz) {
        return clazz.getSimpleName().replace("Vec", "").replaceAll("[^A-Z]", "");
    }

    public static <D extends VecPartOrUsageRelatedSpecification> TransformationFragment<D,
            TransformationResult.Builder<D>> commonSpecificationAttributes(
            final KblPart source) {
        return (specification, builder) -> {
            specification.setIdentification(
                    abbreviatedClassName(specification.getClass()) + "-" + source.getPartNumber());
            builder.withLinker(Query.of(source), VecPartVersion.class, specification::getDescribedPart);
        };
    }

    public static <D extends VecDocumentVersion> TransformationFragment<D, TransformationResult.Builder<D>> commonDocumentAttributes(
            final KblPart source,
            final TransformationContext context) {
        return (dv, builder) -> {
            final Converter<String, Optional<VecLocalizedString>> stringConverter =
                    context.getConverterRegistry().getStringToLocalizedString();

            dv.setCompanyName(source.getCompanyName());
            dv.setDocumentNumber(source.getPartNumber());
            dv.setDocumentVersion(source.getVersion());
            stringConverter.convert(source.getAbbreviation())
                    .ifPresent(dv.getAbbreviations()::add);
            stringConverter.convert(source.getDescription())
                    .ifPresent(dv.getDescriptions()::add);

            builder.withLinker(source, VecPartVersion.class, dv::getReferencedPart);
        };
    }

}
