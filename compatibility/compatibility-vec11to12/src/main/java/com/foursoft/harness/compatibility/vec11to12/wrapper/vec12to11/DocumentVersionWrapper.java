/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.1.X To VEC 1.2.X
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
package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.compatibility.core.wrapper.Wraps;
import com.foursoft.harness.vec.v113.VecSpecification;
import com.foursoft.harness.vec.v12x.VecWireEndAccessorySpecification;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecDocumentVersion}
 * to {@link com.foursoft.harness.vec.v113.VecDocumentVersion}.
 */
@Wraps(com.foursoft.harness.vec.v12x.VecDocumentVersion.class)
public class DocumentVersionWrapper extends ReflectionBasedWrapper {

    private List<VecSpecification> specifications;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public DocumentVersionWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getSpecifications".equals(method.getName())) {
            // Created once, so that repeated calls return the same, modifiable list.
            if (specifications == null) {
                specifications = createSpecifications();
            }
            return specifications;
        }
        if ("getSpecificationsWithType".equals(method.getName())) {
            final Class<?> vec11xClass = (Class<?>) allArguments[0];
            final Class<?> vec12xClass = getContext().getClassMapper().map(vec11xClass);
            if (vec12xClass == null) {
                // The requested class does not exist in the wrapped version, so there are no such objects.
                return new ArrayList<>();
            }
            return wrapList("getSpecificationsWithType", vec11xClass, vec12xClass);
        } else if ("getSpecificationWithType".equals(method.getName())) {
            final Class<?> vec11xClass = (Class<?>) allArguments[0];
            final Class<?> vec12xClass = getContext().getClassMapper().map(vec11xClass);
            if (vec12xClass == null) {
                // The requested class does not exist in the wrapped version, so there are no such objects.
                return Optional.empty();
            }
            return wrapOptional("getSpecificationWithType", vec11xClass, vec12xClass);
        }

        return super.wrapObject(obj, method, allArguments);
    }

    /**
     * VEC 1.2 references the wire reception specification of a wire end accessory specification, VEC 1.1
     * contains it there. Listing it in the document as well would write it twice, with the same id. So a wire
     * reception specification of a wire end accessory specification of this document is only written inside
     * the latter.
     */
    private List<VecSpecification> createSpecifications() {
        final List<com.foursoft.harness.vec.v12x.VecSpecification> sourceSpecifications =
                getResultList("getSpecifications", com.foursoft.harness.vec.v12x.VecSpecification.class);
        final Set<Object> containedInAccessories = Collections.newSetFromMap(new IdentityHashMap<>());
        sourceSpecifications.stream()
                .filter(VecWireEndAccessorySpecification.class::isInstance)
                .map(VecWireEndAccessorySpecification.class::cast)
                .map(VecWireEndAccessorySpecification::getWireReceptionSpecification)
                .filter(Objects::nonNull)
                .forEach(containedInAccessories::add);
        return sourceSpecifications.stream()
                .filter(specification -> !containedInAccessories.contains(specification))
                .map(getContext().getWrapperProxyFactory()::createProxy)
                .filter(VecSpecification.class::isInstance)
                .map(VecSpecification.class::cast)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
