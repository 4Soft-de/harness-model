/*-
 * ========================LICENSE_START=================================
 * compatibility-core
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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
