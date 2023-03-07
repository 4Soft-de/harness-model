package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification;

import com.foursoft.harness.compatibility.core.Context;

import java.lang.reflect.Method;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecGrommetSpecification}
 * to {@link com.foursoft.harness.vec.v113.VecGrommetSpecification}.
 */
public class Vec12To11GrommetSpecificationWrapper extends AbstractSpecificationReflectionBasedWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To11GrommetSpecificationWrapper(final Context context, final Object target) {
        super(context, target);
    }

    @Override
    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        if (GET_SEALING_CLASSES.equals(method.getName())) {
            return getSealingClasses();
        }

        return super.wrapObject(obj, method, allArguments);
    }

}
