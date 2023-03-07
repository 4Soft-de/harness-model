package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.util.ReflectionUtils;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v113.VecWireSpecification;
import com.foursoft.harness.vec.v12x.VecWireElement;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecWireElement}
 * to {@link com.foursoft.harness.vec.v12x.VecWireElement}.
 */
public class Vec11To12WireElementWrapper extends ReflectionBasedWrapper {

    private VecWireElement parentWireElement;
    private List<VecWireElement> subWireElements;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12WireElementWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    private static boolean hasRefWireSpecification(final com.foursoft.harness.vec.v113.VecWireElement element) {
        final Set<VecWireSpecification> refWireSpecification =
                element.getWireElementSpecification().getRefWireSpecification();
        return refWireSpecification != null && !refWireSpecification.isEmpty();
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        final String methodName = method.getName();
        if ("getSubWireElements".equals(methodName)) {
            if (subWireElements == null) {
                subWireElements = new ArrayList<>();
            }
            return subWireElements;
        } else if ("getParentWireElement".equals(methodName)) {
            if (parentWireElement == null) {
                final com.foursoft.harness.vec.v113.VecWireElement vec113parentWireElement =
                        getResultObject("getParentWireSpecification", VecWireSpecification.class)
                                .map(VecWireSpecification::getWireElements)
                                .orElseGet(Collections::emptyList)
                                .stream()
                                .filter(Vec11To12WireElementWrapper::hasRefWireSpecification)
                                .collect(StreamUtils.findOneOrNone())
                                .orElseThrow(() -> new IllegalStateException(
                                        "VecWireElement couldn't be fetched from the parent VecWireSpecification, " +
                                                "no or more than 1 element was returned by " +
                                                "VecWireSpecification#getWireElements!"));

                parentWireElement = Stream.of(vec113parentWireElement)
                        .map(getContext().getWrapperProxyFactory()::createProxy)
                        .map(VecWireElement.class::cast)
                        .collect(StreamUtils.findOneOrNone())
                        .orElse(null);

                if (parentWireElement != null) {
                    final List<VecWireElement> wireElements = parentWireElement.getSubWireElements();

                    wireElements.forEach(sv -> ReflectionUtils.setParentRelationship(sv,
                                                                                     "parentWireElement",
                                                                                     parentWireElement)
                    );
                }
            }

            return parentWireElement;
        }

        return super.wrapObject(obj, method, allArguments);
    }

}  