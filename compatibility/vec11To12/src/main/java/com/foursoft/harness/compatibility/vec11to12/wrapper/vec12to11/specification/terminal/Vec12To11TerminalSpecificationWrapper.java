package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.terminal;

import com.foursoft.harness.compatibility.core.Context;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.AbstractSpecificationReflectionBasedWrapper;

import java.lang.reflect.Method;

/**
 * Wrapper to wrap {@link com.foursoft.harness.vec.v12x.VecTerminalSpecification}
 * to {@link com.foursoft.harness.vec.v113.VecTerminalSpecification}.
 */
public class Vec12To11TerminalSpecificationWrapper extends AbstractSpecificationReflectionBasedWrapper {

    /**
     * Creates this wrapper.
     *
     * @param context Context of the wrapper.
     * @param target  Target object of the wrapper.
     */
    public Vec12To11TerminalSpecificationWrapper(final Context context, final Object target) {
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
