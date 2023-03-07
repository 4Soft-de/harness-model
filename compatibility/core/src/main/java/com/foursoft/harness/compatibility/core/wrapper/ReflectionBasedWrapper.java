package com.foursoft.harness.compatibility.core.wrapper;

import com.foursoft.harness.compatibility.core.Context;
import com.foursoft.harness.compatibility.core.HasUnsupportedMethods;
import com.foursoft.harness.compatibility.core.MethodCache;
import com.foursoft.harness.compatibility.core.MethodIdentifier;
import com.foursoft.harness.compatibility.core.exception.WrapperException;
import com.foursoft.harness.compatibility.core.mapping.ClassMapper;
import com.foursoft.harness.compatibility.core.util.ClassUtils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A {@link CompatibilityWrapper} implementation which uses reflection.
 */
public class ReflectionBasedWrapper implements InvocationHandler, CompatibilityWrapper {

    private final Map<Object, Object> collectionsByMethod = new HashMap<>();

    private final WrapperHelper wrapperHelper;
    private final Context context;
    private final Object target;

    /**
     * Creates a wrapper for the given {@link Context} and target object.
     *
     * @param context Context for the wrapper.
     * @param target  Target object to adjust.
     */
    public ReflectionBasedWrapper(final Context context, final Object target) {
        this.context = context;
        this.target = target;

        wrapperHelper = new WrapperHelper(this);
        MethodCache.initClassCache(ClassUtils.getNonProxyClass(target.getClass()));
    }

    @Override
    public final Object invoke(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        final Object returnValue = innerInvoke(obj, method, allArguments);
        // In case the return value is a List. This should prevent NPEs when trying to loop over the list.
        if (returnValue == null && method.getReturnType().isAssignableFrom(List.class)) {
            return new ArrayList<>();
        }
        return returnValue;
    }

    @Override
    public Context getContext() {
        return context;
    }

    /**
     * Returns the target object which should be wrapped.
     *
     * @return The target object which should be wrapped.
     */
    public Object getTarget() {
        return target;
    }

    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        return defaultInvoke(method, allArguments);
    }

    protected <T> Optional<T> getResultObject(final String methodName,
                                              final Class<T> targetClass, final Object... args) {
        return wrapperHelper.getResultObject(methodName, target, targetClass, args);
    }

    protected <T> List<T> getResultList(final String methodName,
                                        final Class<T> targetClass, final Object... args) {
        return wrapperHelper.getResultList(methodName, target, targetClass, args);
    }

    protected <T> List<T> wrapList(final String methodName,
                                   final Class<T> targetClass, final Object... args) {
        return wrapperHelper.wrapList(methodName, target, targetClass, args);
    }

    protected <T> Optional<T> wrapListToSingleElement(final String methodName,
                                                      final Class<T> targetClass, final Object... args) {
        return wrapperHelper.wrapListToSingleElement(methodName, target, targetClass, args);
    }

    protected <T> T wrapListToSingleElementIfNull(final T object,
                                                  final String methodName,
                                                  final Object... args) {
        return wrapperHelper.wrapListToSingleElementIfNull(object, methodName, target, args);
    }

    private Object innerInvoke(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        // Note: hashCode, equals and toString should NOT be handled, the Sonar / IJ warning can be ignored.
        // See WrapperProxyFactory (`.method(not(isDeclaredBy(Object.class)))`).
        if (obj == null || target == null) {
            return null;
        }

        final HasUnsupportedMethods hasUnsupportedMethods = context.checkUnsupportedMethods();
        if (hasUnsupportedMethods.isNotSupported(MethodIdentifier.of(method))) {
            return null;
        }

        // The given method can be from a super class which can exist in both source and target package.
        // However, this doesn't need to be true for the method of the given object (e.g. changed inheritance).
        // Thus, the unsupported check has to be for the method of the given object too.
        final String className = ClassUtils.getNonProxyClass(obj.getClass()).getSimpleName();
        final String methodName = method.getName();
        if (hasUnsupportedMethods.isNotSupported(new MethodIdentifier(className, methodName))) {
            return null;
        }

        final ClassMapper classMapper = context.getClassMapper();
        final Class<?> targetClass = target.getClass();
        if (classMapper.isFromSourcePackage(targetClass) || classMapper.isFromTargetPackage(targetClass)) {
            return wrapObject(obj, method, allArguments);
        } else {
            return null;
        }
    }

    private Object defaultInvoke(final Method method, final Object[] allArguments) {
        return getTargetObject(method, allArguments);
    }

    private boolean isCollection(final Class<?> o) {
        return Collection.class.isAssignableFrom(o) || Array.class.isAssignableFrom(o);
    }

    private Object getTargetObject(final Method method, final Object[] objects) {
        final Class<?> nonProxyTargetClass = ClassUtils.getNonProxyClass(target.getClass());
        final Optional<Method> targetMethodOpt = MethodCache.get(nonProxyTargetClass, method.getName());
        if (targetMethodOpt.isEmpty()) {
            final String errorMsg = String.format("Could not find a target method for source method %s for class %s.",
                                                  method.getName(), nonProxyTargetClass.getName());
            throw new WrapperException(errorMsg);
        }
        final Method targetMethod = targetMethodOpt.get();

        final Object targetMethodResult;
        try {
            targetMethodResult = targetMethod.invoke(target, objects);
        } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            final String args = Arrays.stream(objects)
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            final String errorMsg = String.format("Cannot invoke method %s on class %s with args '%s'.",
                                                  targetMethod.getName(), target.getClass(), args);
            throw new WrapperException(errorMsg, e);
        }

        if (targetMethodResult == null) {
            return null;
        }

        final Class<?> returnType = targetMethod.getReturnType();
        return extractObject(targetMethodResult, returnType, method);
    }

    private Object extractObject(final Object targetObject, final Class<?> targetType, final Object method) {
        if (Map.class.isAssignableFrom(targetType)) {
            return null;
        }

        if (isClassOfInterest(targetType)) {
            return context.getWrapperProxyFactory().createProxy(targetObject);
        } else if (isCollection(targetType)) {
            if (method == null) {
                return extractObjectsOfPotentialCollection(targetObject);
            }
            return collectionsByMethod.computeIfAbsent(method, c -> extractObjectsOfPotentialCollection(targetObject));
        } else if (targetType.isEnum()) {
            final Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) targetType;
            return extractEnum(enumClass.cast(targetObject), enumClass);
        } else {
            return targetObject;
        }
    }

    private <T extends Enum<T>> T extractEnum(final Enum<?> enumObject, final Class<? extends Enum<?>> enumClass) {
        final String enumClassName = enumClass.getName();

        if (enumObject == null) {
            final String errorMsg = String.format("Given enum of class %s is null.", enumClassName);
            throw new WrapperException(errorMsg);
        }

        final Class<T> mappedClass = (Class<T>) context.getClassMapper().map(enumClass);
        if (mappedClass == null) {
            final String errorMsg = String.format("Could not determine target enum class for %s.", enumClassName);
            throw new WrapperException(errorMsg);
        }

        final String enumName = enumObject.name();

        try {
            return Enum.valueOf(mappedClass, enumName);
        } catch (final IllegalArgumentException e) {
            final String errorMsg = String.format("Could not find enum for %s#%s.",
                                                  mappedClass.getName(), enumName);
            throw new WrapperException(errorMsg);
        }
    }

    private Object extractObjectsOfPotentialCollection(final Object o) {
        final Object[] containedValues;
        if (o instanceof Collection) {
            containedValues = ((Collection<?>) o).toArray();
        } else if (o instanceof Object[]) {
            containedValues = (Object[]) o;
        } else {
            return o;
        }

        final Collection<Object> interestingObjects = o instanceof Set ? new HashSet<>() : new ArrayList<>();
        for (final Object object : containedValues) {
            final Object targetObject;
            if (object == null) {
                targetObject = null;
            } else {
                targetObject = extractObject(object, object.getClass(), null);
            }
            interestingObjects.add(targetObject);
        }

        return interestingObjects;
    }

    private boolean isClassOfInterest(final Class<?> o) {
        final ClassMapper classMapper = context.getClassMapper();
        final String className = o.getName();
        return !o.isEnum()
                && (className.startsWith(classMapper.getSourcePackageName()) ||
                className.startsWith(classMapper.getTargetPackageName()));
    }

}