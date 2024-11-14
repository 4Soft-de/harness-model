package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.vec.v12x.VecCustomProperty;
import com.foursoft.harness.vec.v12x.VecNumericalValue;
import com.foursoft.harness.vec.v12x.VecNumericalValueProperty;

import java.lang.reflect.Method;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v113.VecStripeSpecification}
 * to {@link com.foursoft.harness.vec.v12x.VecStripeSpecification}.
 */
public class Vec11To12StripeSpecificationWrapper extends ReflectionBasedWrapper {

    private VecNumericalValue thickness;

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec11To12StripeSpecificationWrapper(final CompatibilityContext context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if ("getThickness".equals(method.getName())) {
            return getThickness();
        }
        if ("setThickness".equals(method.getName())) {
            return setThickness((VecNumericalValue) allArguments[0]);
        }

        return super.wrapObject(obj, method, allArguments);
    }

    private Object setThickness(final VecNumericalValue arg) {
        thickness = arg;
        return null;
    }

    private VecNumericalValue getThickness() {
        thickness = getResultObject("getCustomProperties", VecCustomProperty.class)
                .filter(c -> c.getPropertyType().equalsIgnoreCase("Thickness"))
                .filter(VecNumericalValueProperty.class::isInstance)
                .map(VecNumericalValueProperty.class::cast)
                .map(VecNumericalValueProperty::getValue)
                .orElse(null);

        return thickness;
    }

}
