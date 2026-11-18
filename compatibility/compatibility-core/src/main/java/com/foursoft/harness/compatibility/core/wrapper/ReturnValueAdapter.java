/*-
 * ========================LICENSE_START=================================
 * Compatibility Core
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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

import com.foursoft.harness.compatibility.core.exception.WrapperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Adapts a value read from the wrapped object to the return type of the method the caller invoked on the
 * proxy.
 * <p>
 * Two versions of a model regularly declare the same property with a different type, without the property
 * changing its meaning:
 * <ul>
 *     <li>the cardinality changes, e.g. a single reference ({@code IDREF}) becomes a list ({@code IDREFS}) or a
 *     list becomes a single value,</li>
 *     <li>an element without a type in the schema ({@code xs:anyType}, unmarshalled as a DOM node) gets one,</li>
 *     <li>a string becomes a number or a boolean, or vice versa.</li>
 * </ul>
 * Handing the value over unchanged would fail with a {@link ClassCastException} in the proxy, and treating the
 * property as a new one would silently lose its value. This adapter converts it instead.
 */
final class ReturnValueAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReturnValueAdapter.class);

    /**
     * Element type of a collection returned by a method, {@code Object} if it cannot be determined.
     */
    private final Map<Method, Class<?>> elementTypeByMethod = new ConcurrentHashMap<>();

    /**
     * Returns the value to hand out for a property without a value.
     *
     * @param returnType Return type of the invoked method.
     * @return The default value of a primitive type (a primitive cannot be {@code null}), else {@code null}.
     */
    Object defaultValue(final Class<?> returnType) {
        if (!returnType.isPrimitive() || returnType == void.class) {
            return null;
        }
        if (returnType == boolean.class) {
            return false;
        }
        if (returnType == char.class) {
            return '\0';
        }
        return convertNumber(0, returnType);
    }

    /**
     * Adapts a value to a method returning a collection.
     *
     * @param value  Value read from the wrapped object, already mapped to the other version of the model. May be
     *               {@code null}, a single value or a collection.
     * @param method Invoked method, which returns a collection.
     * @return A new, mutable collection. Elements which do not fit the element type of the method are left out
     * (e.g. an element without a counterpart in the other version, or a specialization the other version
     * does not allow at this place).
     */
    Collection<Object> toCollection(final Object value, final Method method) {
        final Collection<Object> result = Set.class.isAssignableFrom(method.getReturnType())
                ? new HashSet<>()
                : new ArrayList<>();
        if (value == null) {
            return result;
        }
        final Class<?> elementType = elementTypeByMethod.computeIfAbsent(method, this::resolveElementType);
        final Collection<?> values = value instanceof final Collection<?> collection ? collection : List.of(value);
        for (final Object element : values) {
            if (element == null) {
                continue;
            }
            final Object adapted = adaptElement(element, elementType);
            if (adapted == null) {
                LOGGER.debug("{} is not a {} and is left out of {}#{}.", element.getClass().getName(),
                             elementType.getName(), method.getDeclaringClass().getName(), method.getName());
            } else {
                result.add(adapted);
            }
        }
        return result;
    }

    /**
     * Adapts a value to a method returning a single value.
     *
     * @param value  Value read from the wrapped object, already mapped to the other version of the model.
     * @param method Invoked method, which does not return a collection.
     * @return The value in the return type of the method.
     * @throws WrapperException If the value cannot be converted to the return type of the method.
     */
    Object toSingleValue(final Object value, final Method method) {
        final Class<?> returnType = method.getReturnType();
        if (value == null) {
            return defaultValue(returnType);
        }
        if (value instanceof final Collection<?> collection) {
            if (collection.size() > 1) {
                LOGGER.warn("{}#{} takes a single value, but {} values are given. Only the first one is used.",
                            method.getDeclaringClass().getName(), method.getName(), collection.size());
            }
            return collection.stream()
                    .filter(Objects::nonNull)
                    .findFirst()
                    .map(element -> toSingleValue(element, method))
                    .orElseGet(() -> defaultValue(returnType));
        }
        final Object converted = convert(value, returnType);
        if (converted == null) {
            final String errorMsg = String.format("Cannot convert %s to %s, the return type of %s#%s.",
                                                  value.getClass().getName(), returnType.getName(),
                                                  method.getDeclaringClass().getName(), method.getName());
            throw new WrapperException(errorMsg);
        }
        return converted;
    }

    /**
     * Adapts the arguments of a call to the parameter types of the method of the wrapped object, which may
     * differ in the same way as return types do.
     *
     * @param arguments Arguments the caller passed, may be {@code null} for a method without parameters.
     * @param method    Method of the wrapped object which will be invoked.
     * @return The arguments to invoke the method with.
     */
    Object[] toArguments(final Object[] arguments, final Method method) {
        if (arguments == null || arguments.length != method.getParameterCount()) {
            return arguments;
        }
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Object[] adapted = arguments.clone();
        for (int i = 0; i < adapted.length; i++) {
            if (adapted[i] == null) {
                adapted[i] = defaultValue(parameterTypes[i]);
            } else if (!box(parameterTypes[i]).isInstance(adapted[i])) {
                final Object converted = convert(adapted[i], parameterTypes[i]);
                // Left as it is if it cannot be converted, the invocation reports the mismatch.
                adapted[i] = converted == null ? adapted[i] : converted;
            }
        }
        return adapted;
    }

    private Object adaptElement(final Object element, final Class<?> elementType) {
        if (elementType.isInstance(element)) {
            return element;
        }
        if (isModelObject(element)) {
            // A model object of the wrong type cannot be converted, only left out.
            return null;
        }
        return convert(element, elementType);
    }

    /**
     * @return The converted value, {@code null} if it cannot be converted.
     */
    private Object convert(final Object value, final Class<?> type) {
        final Class<?> boxedType = box(type);
        if (boxedType.isInstance(value)) {
            return value;
        }
        if (value instanceof final Node node) {
            return convert(node.getTextContent(), type);
        }
        if (boxedType == String.class && isScalar(value)) {
            return value instanceof final BigDecimal decimal ? decimal.toPlainString() : value.toString();
        }
        if (value instanceof final String text) {
            return parse(text.trim(), boxedType);
        }
        if (value instanceof final Number number && Number.class.isAssignableFrom(boxedType)) {
            return convertNumber(number, boxedType);
        }
        return null;
    }

    private Object parse(final String text, final Class<?> boxedType) {
        if (boxedType == Boolean.class) {
            if ("true".equals(text) || "1".equals(text)) {
                return Boolean.TRUE;
            }
            if ("false".equals(text) || "0".equals(text)) {
                return Boolean.FALSE;
            }
            return null;
        }
        if (Number.class.isAssignableFrom(boxedType)) {
            try {
                return convertNumber(new BigDecimal(text), boxedType);
            } catch (final NumberFormatException | ArithmeticException e) {
                LOGGER.warn("Cannot convert '{}' to {}.", text, boxedType.getSimpleName());
                return null;
            }
        }
        return null;
    }

    private Object convertNumber(final Number number, final Class<?> type) {
        final Class<?> boxedType = box(type);
        final BigDecimal decimal = number instanceof final BigDecimal d ? d : new BigDecimal(number.toString());
        if (boxedType == BigDecimal.class || boxedType == Number.class) {
            return decimal;
        }
        if (boxedType == BigInteger.class) {
            return decimal.toBigIntegerExact();
        }
        if (boxedType == Long.class) {
            return decimal.longValueExact();
        }
        if (boxedType == Integer.class) {
            return decimal.intValueExact();
        }
        if (boxedType == Short.class) {
            return decimal.shortValueExact();
        }
        if (boxedType == Byte.class) {
            return decimal.byteValueExact();
        }
        if (boxedType == Double.class) {
            return decimal.doubleValue();
        }
        if (boxedType == Float.class) {
            return decimal.floatValue();
        }
        return null;
    }

    private boolean isScalar(final Object value) {
        return value instanceof Number || value instanceof Boolean || value instanceof CharSequence
                || value instanceof Character;
    }

    private boolean isModelObject(final Object value) {
        return !isScalar(value) && !(value instanceof Node) && !(value instanceof Enum<?>);
    }

    private Class<?> box(final Class<?> type) {
        if (!type.isPrimitive()) {
            return type;
        }
        if (type == boolean.class) {
            return Boolean.class;
        }
        if (type == int.class) {
            return Integer.class;
        }
        if (type == long.class) {
            return Long.class;
        }
        if (type == double.class) {
            return Double.class;
        }
        if (type == float.class) {
            return Float.class;
        }
        if (type == short.class) {
            return Short.class;
        }
        if (type == byte.class) {
            return Byte.class;
        }
        if (type == char.class) {
            return Character.class;
        }
        return type;
    }

    private Class<?> resolveElementType(final Method method) {
        final Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof final ParameterizedType parameterizedType
                && parameterizedType.getActualTypeArguments().length == 1) {
            Type argument = parameterizedType.getActualTypeArguments()[0];
            if (argument instanceof final WildcardType wildcard) {
                argument = wildcard.getUpperBounds()[0];
            }
            if (argument instanceof final ParameterizedType parameterizedArgument) {
                argument = parameterizedArgument.getRawType();
            }
            if (argument instanceof final Class<?> elementClass) {
                return elementClass;
            }
        }
        return Object.class;
    }

}
