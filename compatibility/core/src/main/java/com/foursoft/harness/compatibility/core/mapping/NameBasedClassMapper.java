package com.foursoft.harness.compatibility.core.mapping;

import com.foursoft.harness.compatibility.core.exception.WrapperException;
import com.foursoft.harness.compatibility.core.util.ClassUtils;

/**
 * A {@link ClassMapper} implementation which uses a source and target package for class mapping.
 */
public abstract class NameBasedClassMapper implements ClassMapper {

    private final String sourcePackage;
    private final String targetPackage;

    /**
     * Creates a new class mapper which helps in converting classes
     * from the {@code sourcePackage} to the {@code targetPackage} and vice versa.
     *
     * @param sourcePackage Name of the source package where the classes should generally be converted from.
     * @param targetPackage Name of the target package where the classes should generally be converted to.
     */
    protected NameBasedClassMapper(final String sourcePackage, final String targetPackage) {
        this.sourcePackage = sourcePackage;
        this.targetPackage = targetPackage;
    }

    @Override
    public Class<?> map(final Class<?> clazz) {
        final boolean isSource = isFromSourcePackage(clazz);
        final boolean isTarget = isFromTargetPackage(clazz);
        if (!isSource && !isTarget) {
            final String errorMsg =
                    String.format("Given class %s is neither from the source package nor the target package.",
                                  clazz.getName());
            throw new WrapperException(errorMsg);
        }
        final String oppositePackageName = isSource ? targetPackage : sourcePackage;

        try {
            return ClassUtils.getMappedClass(clazz, oppositePackageName);
        } catch (final ClassNotFoundException e) {
            final String errorMsg = String.format("Could not map class %s.", clazz.getName());
            throw new WrapperException(errorMsg, e);
        }
    }

    @Override
    public String getSourcePackageName() {
        return sourcePackage;
    }

    @Override
    public String getTargetPackageName() {
        return targetPackage;
    }

}
