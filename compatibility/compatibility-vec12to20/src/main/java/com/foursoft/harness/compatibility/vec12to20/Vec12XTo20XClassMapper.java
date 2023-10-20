/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.2.X To VEC 2.X.X
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
package com.foursoft.harness.compatibility.vec12to20;

import com.foursoft.harness.compatibility.core.HasUnsupportedMethods;
import com.foursoft.harness.compatibility.core.MethodIdentifier;
import com.foursoft.harness.compatibility.core.mapping.NameBasedClassMapper;
import com.foursoft.harness.compatibility.core.util.ClassUtils;
import com.foursoft.harness.vec.v2x.VecCableLeadThrough;

import java.io.Serial;
import java.util.*;

/**
 * Class responsible for mapping VEC 1.2.X classes to VEC 2.X.X <b>and vice versa</b>.
 */
public class Vec12XTo20XClassMapper extends NameBasedClassMapper {

    private final Map<Class<?>, Class<?>> explicitClassMappings;
    private final UnsupportedVec12XToVec20XMethods ignored;

    /**
     * Creates a VEC 1.1 to VEC 1.2 Class Mapper.
     */
    public Vec12XTo20XClassMapper() {
        super(Constants.PACKAGE_VEC12X, Constants.PACKAGE_VEC20X);
        explicitClassMappings = new HashMap<>();
        ignored = new UnsupportedVec12XToVec20XMethods();

        // Ignored VEC 1.2.X -> VEC 2.X.X methods.
        // methods added in VEC 2.X.X
        ignored.add(VecCableLeadThrough.class, "getOutlets");

        // Ignored VEC 2.X.X -> VEC 1.2.X methods.
        // methods removed in VEC 2.X.X
        ignored.add(com.foursoft.harness.vec.v12x.VecCableLeadThrough.class, "getPlacementPoint");
    }

    @Override
    public Class<?> map(final Class<?> clazz) {
        return resolveExplicitClassMappings(clazz).orElseGet(() -> super.map(clazz));
    }

    private Optional<Class<?>> resolveExplicitClassMappings(final Class<?> clazz) {
        return Optional.<Class<?>>ofNullable(explicitClassMappings.get(clazz))
                .or(() -> Optional.ofNullable(explicitClassMappings.get(ClassUtils.getNonProxyClass(clazz))));
    }

    @Override
    public HasUnsupportedMethods checkUnsupportedMethods() {
        return ignored;
    }

    private static class UnsupportedVec12XToVec20XMethods extends HashSet<MethodIdentifier>
            implements HasUnsupportedMethods {

        @Serial
        private static final long serialVersionUID = 6377405392358586968L;

        @Override
        public boolean isNotSupported(final MethodIdentifier method) {
            Objects.requireNonNull(method);
            return contains(method);
        }

        public void add(final Class<?> vecClass, final String methodName) {
            final String className = vecClass.getSimpleName();
            add(new MethodIdentifier(className, methodName));
        }

    }

}
