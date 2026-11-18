/*-
 * ========================LICENSE_START=================================
 * Compatibility Core
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
package com.foursoft.harness.compatibility.core.wrapper;

import com.foursoft.harness.compatibility.core.Context;
import com.foursoft.harness.compatibility.core.MethodCache;
import com.foursoft.harness.compatibility.core.PropertyAddition;
import com.foursoft.harness.compatibility.core.PropertyAdditionProvider;
import com.foursoft.harness.compatibility.core.exception.WrapperException;
import com.foursoft.harness.compatibility.core.mapping.ClassMapper;
import com.foursoft.harness.compatibility.core.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionBasedWrapper.class);

    private static final String GET_PREFIX = "get";
    private static final String IS_PREFIX = "is";
    private static final String SET_PREFIX = "set";

    private static final ReturnValueAdapter RETURN_VALUE_ADAPTER = new ReturnValueAdapter();

    // One wrapper exists per wrapped object, so these collections are created on demand: most wrapped
    // objects have no property additions at all and never touch anything but collectionsByMethod.
    private Map<Object, Object> collectionsByMethod = Map.of();

    private Map<String, Object> valuePropertyValues = Map.of();
    private Set<String> valuePropertyGetters = Set.of();
    private Map<String, String> setterToGetter = Map.of();
    private Set<String> listPropertyGetters = Set.of();
    private Map<String, List<Object>> listPropertyStore = Map.of();
    private Set<String> backRefPropertyGetters = Set.of();
    private Map<String, Set<Object>> backRefPropertyStore = Map.of();
    private Map<String, String> renamedGetters = Map.of();

    private final WrapperHelper wrapperHelper;
    private final Context context;
    private final Object target;
    private final Class<?> nonProxyTargetClass;
    private final boolean targetOfInterest;

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
        nonProxyTargetClass = ClassUtils.getNonProxyClass(target.getClass());
        // Constant for a given target, so it is decided here instead of on every method invocation.
        final ClassMapper mapper = context.getClassMapper();
        targetOfInterest = mapper.isFromSourcePackage(target.getClass())
                || mapper.isFromTargetPackage(target.getClass());
        MethodCache.initClassCache(nonProxyTargetClass);

        if (context.getClassMapper() instanceof final PropertyAdditionProvider provider) {
            for (final PropertyAddition addition : provider.getPropertyAdditions()
                    .getAdditions(nonProxyTargetClass)) {
                if (addition instanceof final PropertyAddition.Value v) {
                    registerValueProperty(v.propertyName());
                } else if (addition instanceof final PropertyAddition.MutableList l) {
                    registerListProperty(l.propertyName());
                } else if (addition instanceof final PropertyAddition.BackRef b) {
                    registerBackRefProperty(b.propertyName());
                } else if (addition instanceof final PropertyAddition.Renamed renamed) {
                    registerRenamedProperty(renamed.propertyName(), renamed.sourcePropertyName());
                }
            }
        }
    }

    @Override
    public final Object invoke(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        final Object returnValue = innerInvoke(obj, method, allArguments);
        if (returnValue == null) {
            // In case the return value is a List. This should prevent NPEs when trying to loop over the list.
            if (List.class.isAssignableFrom(method.getReturnType())) {
                return new ArrayList<>();
            }
            // A primitive cannot be null, the proxy would fail with a NullPointerException on unboxing.
            return RETURN_VALUE_ADAPTER.defaultValue(method.getReturnType());
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

    /**
     * Registers a value property (getter + setter) that is handled in-memory by this wrapper.
     * Getter and setter names are inferred by capitalising the property name and prepending
     * {@code get} / {@code set} (standard JavaBean convention).
     *
     * @param propertyName Property name in camelCase (e.g. {@code "myProperty"}).
     */
    protected void registerValueProperty(final String propertyName) {
        final String capitalised = capitalize(propertyName);
        registerValueProperty("get" + capitalised, "set" + capitalised);
    }

    /**
     * Registers a value property with explicit getter and setter method names.
     *
     * @param getterName Name of the getter method.
     * @param setterName Name of the setter method.
     */
    protected void registerValueProperty(final String getterName, final String setterName) {
        valuePropertyGetters = mutable(valuePropertyGetters);
        setterToGetter = mutable(setterToGetter);
        valuePropertyGetters.add(getterName);
        setterToGetter.put(setterName, getterName);
    }

    /**
     * Registers a list property whose getter returns a stable, lazily-created empty list.
     * The getter name is inferred by capitalising the property name and prepending {@code get}.
     *
     * @param propertyName Property name in camelCase (e.g. {@code "myList"}).
     */
    protected void registerListProperty(final String propertyName) {
        listPropertyGetters = mutable(listPropertyGetters);
        listPropertyGetters.add("get" + capitalize(propertyName));
    }

    /**
     * Registers a back-reference property whose getter returns a stable, lazily-created empty set.
     * The getter name is inferred by capitalising the property name and prepending {@code get}.
     *
     * @param propertyName Property name in camelCase (e.g. {@code "refEEComponentRole"}).
     */
    protected void registerBackRefProperty(final String propertyName) {
        backRefPropertyGetters = mutable(backRefPropertyGetters);
        backRefPropertyGetters.add("get" + capitalize(propertyName));
    }

    /**
     * Registers a property which is read from another property of the target object, see
     * {@link PropertyAddition.Renamed}. Both a {@code get} and an {@code is} getter are redirected.
     *
     * @param propertyName       Property name in camelCase as seen by the caller.
     * @param sourcePropertyName Property name in camelCase on the target object.
     */
    protected void registerRenamedProperty(final String propertyName, final String sourcePropertyName) {
        renamedGetters = mutable(renamedGetters);
        final String capitalised = capitalize(propertyName);
        final String sourceCapitalised = capitalize(sourcePropertyName);
        renamedGetters.put(GET_PREFIX + capitalised, GET_PREFIX + sourceCapitalised);
        renamedGetters.put(IS_PREFIX + capitalised, GET_PREFIX + sourceCapitalised);
    }

    protected Object wrapObject(final Object obj, final Method method, final Object[] allArguments) throws Throwable {
        final String methodName = method.getName();

        if (valuePropertyGetters.contains(methodName)) {
            return valuePropertyValues.get(methodName);
        }
        if (setterToGetter.containsKey(methodName)) {
            valuePropertyValues = mutable(valuePropertyValues);
            valuePropertyValues.put(setterToGetter.get(methodName),
                                    allArguments != null && allArguments.length > 0 ? allArguments[0] : null);
            return null;
        }
        if (listPropertyGetters.contains(methodName)) {
            listPropertyStore = mutable(listPropertyStore);
            return listPropertyStore.computeIfAbsent(methodName, k -> new ArrayList<>());
        }
        if (backRefPropertyGetters.contains(methodName)) {
            backRefPropertyStore = mutable(backRefPropertyStore);
            return backRefPropertyStore.computeIfAbsent(methodName, k -> new HashSet<>());
        }

        final String renamedGetter = renamedGetters.get(methodName);
        if (renamedGetter != null) {
            return getTargetObject(method, renamedGetter, allArguments);
        }
        if (isSetterWithoutTarget(method, allArguments)) {
            keepValueOfSetterWithoutTarget(method, allArguments[0]);
            return null;
        }

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

    protected <T> Optional<T> wrapOptional(final String methodName,
                                           final Class<T> targetClass, final Object... args) {
        return wrapperHelper.wrapOptional(methodName, target, targetClass, args);
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

        return targetOfInterest ? wrapObject(obj, method, allArguments) : null;
    }

    private Object defaultInvoke(final Method method, final Object[] allArguments) {
        return getTargetObject(method, method.getName(), allArguments);
    }

    private boolean isCollection(final Class<?> o) {
        return Collection.class.isAssignableFrom(o) || Array.class.isAssignableFrom(o);
    }

    private Object getTargetObject(final Method method, final String targetMethodName, final Object[] objects) {
        final Method targetMethod = findTargetMethod(targetMethodName);

        final Object targetMethodResult;
        try {
            targetMethodResult = targetMethod.invoke(target, RETURN_VALUE_ADAPTER.toArguments(objects, targetMethod));
        } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            final String args = objects == null ? "[]" : Arrays.stream(objects)
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            final String errorMsg = String.format("Cannot invoke method %s on class %s with args '%s'.",
                                                  targetMethod.getName(), target.getClass(), args);
            throw new WrapperException(errorMsg, e);
        }

        final Class<?> returnType = method.getReturnType();
        if (Collection.class.isAssignableFrom(returnType)) {
            if (objects != null && objects.length > 0) {
                // The result depends on the arguments, so it must not be cached per method.
                return RETURN_VALUE_ADAPTER.toCollection(extractObject(targetMethodResult, targetMethod), method);
            }
            // Cached, so that repeated calls return the same, modifiable collection.
            collectionsByMethod = mutable(collectionsByMethod);
            return collectionsByMethod.computeIfAbsent(
                    method, m -> RETURN_VALUE_ADAPTER.toCollection(extractObject(targetMethodResult, targetMethod),
                                                                   method));
        }
        if (targetMethodResult == null || Map.class.isAssignableFrom(returnType)) {
            return null;
        }
        return RETURN_VALUE_ADAPTER.toSingleValue(extractObject(targetMethodResult, targetMethod), method);
    }

    /**
     * Finds the method to invoke on the target object. A boolean getter is called {@code isX} or
     * {@code getX} depending on whether the property is typed as a boolean, which may differ between the
     * two versions of a model, so the respective other prefix is tried as well.
     */
    private Method findTargetMethod(final String targetMethodName) {
        return MethodCache.find(nonProxyTargetClass, targetMethodName)
                .or(() -> alternativeBooleanGetterName(targetMethodName)
                        .flatMap(name -> MethodCache.find(nonProxyTargetClass, name)))
                .orElseThrow(() -> new WrapperException(
                        String.format("Could not find a target method for source method %s for class %s.",
                                      targetMethodName, nonProxyTargetClass.getName())));
    }

    /**
     * A setter of a property the wrapped object does not have - because the other version of the model does
     * not know the property, or knows it only as a list, which JAXB offers no setter for.
     */
    private boolean isSetterWithoutTarget(final Method method, final Object[] allArguments) {
        final String methodName = method.getName();
        return methodName.startsWith(SET_PREFIX) && methodName.length() > SET_PREFIX.length()
                && allArguments != null && allArguments.length == 1
                && MethodCache.find(nonProxyTargetClass, methodName).isEmpty();
    }

    /**
     * Keeps the value of a setter the wrapped object cannot take, in the same way as a
     * {@link PropertyAddition.Value value addition}: the getter of the property returns it from now on.
     */
    private void keepValueOfSetterWithoutTarget(final Method setter, final Object value) {
        final String property = setter.getName().substring(SET_PREFIX.length());
        final String getterName = hasMethod(setter.getDeclaringClass(), IS_PREFIX + property)
                ? IS_PREFIX + property
                : GET_PREFIX + property;
        registerValueProperty(getterName, setter.getName());
        valuePropertyValues = mutable(valuePropertyValues);
        valuePropertyValues.put(getterName, value);
    }

    private boolean hasMethod(final Class<?> clazz, final String methodName) {
        return Arrays.stream(clazz.getMethods()).anyMatch(m -> m.getName().equals(methodName));
    }

    private Optional<String> alternativeBooleanGetterName(final String methodName) {
        if (methodName.startsWith(IS_PREFIX) && methodName.length() > IS_PREFIX.length()) {
            return Optional.of(GET_PREFIX + methodName.substring(IS_PREFIX.length()));
        }
        if (methodName.startsWith(GET_PREFIX) && methodName.length() > GET_PREFIX.length()) {
            return Optional.of(IS_PREFIX + methodName.substring(GET_PREFIX.length()));
        }
        return Optional.empty();
    }

    private Object extractObject(final Object targetMethodResult, final Method targetMethod) {
        return targetMethodResult == null ? null : extractObject(targetMethodResult, targetMethod.getReturnType());
    }

    private Object extractObject(final Object targetObject, final Class<?> targetType) {
        if (Map.class.isAssignableFrom(targetType)) {
            return null;
        }

        if (isClassOfInterest(targetType)) {
            return context.getWrapperProxyFactory().createProxy(targetObject);
        } else if (isCollection(targetType)) {
            return extractObjectsOfPotentialCollection(targetObject);
        } else if (targetType.isEnum()) {
            final Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) targetType;
            return extractEnum(enumClass.cast(targetObject), enumClass);
        } else if (targetType == Object.class && isClassOfInterest(targetObject.getClass())) {
            // An untyped property (xs:anyType) may still hold a model object.
            return extractObject(targetObject, targetObject.getClass());
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
            LOGGER.error("Could not determine enum class for {}.", enumClassName);
            return null;
        }

        final String enumName = enumObject.name();

        try {
            return Enum.valueOf(mappedClass, enumName);
        } catch (final IllegalArgumentException e) {
            LOGGER.error("Could not find enum for {}#{}.", mappedClass.getName(), enumName, e);
            return null;
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
                targetObject = extractObject(object, object.getClass());
            }
            interestingObjects.add(targetObject);
        }

        return interestingObjects;
    }

    private <K, V> Map<K, V> mutable(final Map<K, V> map) {
        return map instanceof HashMap ? map : new HashMap<>(map);
    }

    private <T> Set<T> mutable(final Set<T> set) {
        return set instanceof HashSet ? set : new HashSet<>(set);
    }

    private static String capitalize(final String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private boolean isClassOfInterest(final Class<?> o) {
        final ClassMapper classMapper = context.getClassMapper();
        final String className = o.getName();
        return !o.isEnum()
                && (className.startsWith(classMapper.getSourcePackageName()) ||
                className.startsWith(classMapper.getTargetPackageName()));
    }

}
