package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationFragment;
import com.foursoft.harness.vec.v2x.VecPartOrUsageRelatedSpecification;
import com.foursoft.harness.vec.v2x.VecPartVersion;

public final class SpecificationUtils {

    private SpecificationUtils() {
        throw new AssertionError("Can't instantiate utility class");
    }

    public static void withIdentification(final VecPartOrUsageRelatedSpecification specification,
                                          final String partNumber) {
        specification.setIdentification(abbreviatedClassName(specification.getClass()) + "-" + partNumber);
    }

    public static String abbreviatedClassName(final Class<?> clazz) {
        return clazz.getSimpleName().replace("Vec", "").replaceAll("[^A-Z]", "");
    }

    public static <D extends VecPartOrUsageRelatedSpecification> TransformationFragment<D> commonSpecificationAttributes(
            final KblPart source) {
        return (specification, builder) -> {
            specification.setIdentification(
                    abbreviatedClassName(specification.getClass()) + "-" + source.getPartNumber());
            builder.withLinker(Query.of(source), VecPartVersion.class, specification::getDescribedPart);
        };

    }

}
