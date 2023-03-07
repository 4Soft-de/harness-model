package com.foursoft.harness.compatibility.core.mapping;

import com.foursoft.harness.compatibility.core.HasUnsupportedMethods;

/**
 * Interface for mapping classes between two packages.
 * It also allows defining which package counts as the source and which is the target.
 */
public interface ClassMapper {

    /**
     * Maps the given {@code clazz} to the same class in the opposite package.
     *
     * @param clazz Class from the {@link #getSourcePackageName() source package} which should be mapped.
     * @return The corresponding class in the target.
     */
    Class<?> map(Class<?> clazz);

    /**
     * Name of the package which should be treated as the source.
     *
     * @return The Name of the package which should be treated as the source.
     */
    String getSourcePackageName();

    /**
     * Name of the package which should be treated as the target.
     *
     * @return The Name of the package which should be treated as the target.
     */
    String getTargetPackageName();

    /**
     * Checks whether the given class is from the source package or not.
     *
     * @param clazz Clazz to check.
     * @return {@code true} if the class is from the source package, else {@code false}.
     */
    default boolean isFromSourcePackage(final Class<?> clazz) {
        final String packageName = clazz.getPackage().getName();
        return packageName.startsWith(getSourcePackageName());
    }

    /**
     * Checks whether the given class is from the target package or not.
     *
     * @param clazz Clazz to check.
     * @return {@code true} if the class is from the target package, else {@code false}.
     */
    default boolean isFromTargetPackage(final Class<?> clazz) {
        final String packageName = clazz.getPackage().getName();
        return packageName.startsWith(getTargetPackageName());
    }

    /**
     * Grants access to check which methods of which classes are not supported.
     *
     * @return An instance of {@link HasUnsupportedMethods} to check whether a method is supported or not.
     */
    HasUnsupportedMethods checkUnsupportedMethods();

}