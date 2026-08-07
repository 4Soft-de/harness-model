/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.1.X To VEC 1.2.X
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
package com.foursoft.harness.compatibility.vec11to12;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.exception.WrapperException;
import jakarta.xml.bind.annotation.XmlType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.reflections.Reflections;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Regression test ensuring that every v12x {@link XmlType}-annotated class can be proxied to its
 * v2x counterpart and that all public getters and setters on the proxy are callable without error.
 *
 * <p>For list-returning getters the test additionally verifies:
 * <ul>
 *   <li>the returned value is a non-null, empty {@link List}</li>
 *   <li>a subsequent call returns the exact same list instance</li>
 * </ul>
 *
 * <p>The typed getters of an open enumeration are excluded from those two: they are derived,
 * unmodifiable views of the plain list rather than the JAXB list itself.
 */
class Vec11To12ProxyCoverageTest {

    private static final CompatibilityContext CONTEXT =
            new Vec11XTo12XCompatibilityWrapper().getContext();

    static Stream<Class<?>> vec11xXmlTypeClasses() {
        final Reflections reflections = new Reflections(Constants.PACKAGE_VEC11X);
        return reflections.getTypesAnnotatedWith(XmlType.class)
                .stream()
                .filter(c -> !c.isInterface())
                .filter(c -> !Modifier.isAbstract(c.getModifiers()))
                .filter(Vec11To12ProxyCoverageTest::hasNoArgConstructor)
                .filter(Vec11To12ProxyCoverageTest::hasMappedClass)
                .sorted(Comparator.comparing(Class::getSimpleName));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource({"vec11xXmlTypeClasses"})
    @DisplayName("All proxy methods callable without exception for")
    void allProxyMethodsShouldBeCallableWithoutException(final Class<?> vec11xClass) throws Exception {
        final Object source = vec11xClass.getDeclaredConstructor().newInstance();
        final Object proxy = CONTEXT.getWrapperProxyFactory().createProxy(source);

        assertThat(proxy).isNotNull();

        final Class<?> vec2xClass = CONTEXT.getClassMapper().map(vec11xClass);
        final List<Method> publicMethods = Arrays.stream(vec2xClass.getMethods())
                .filter(m -> !m.getDeclaringClass().equals(Object.class))
                .sorted(Comparator.comparing(Method::getName))
                .toList();

        for (final Method method : publicMethods) {
            if (isGetter(method)) {
                assertGetterCallable(proxy, method, vec11xClass, vec2xClass);
            } else if (isSetter(method)) {
                assertSetterCallable(proxy, method, vec11xClass, vec2xClass);
            }
        }
    }

    private static void assertGetterCallable(final Object proxy, final Method method,
                                             final Class<?> vec12xClass, final Class<?> vec2xClass) {
        assertThatCode(() -> method.invoke(proxy))
                .as("Getter %s#%s must not throw when called via proxy of %s",
                    vec2xClass.getSimpleName(), method.getName(), vec12xClass.getSimpleName())
                .doesNotThrowAnyException();

        if (!isListReturnType(method) || isOpenEnumLiteralGetter(method)) {
            // The assertions below describe the JAXB list getters, which hand out the live list so
            // that getX().add(...) works. The typed getters of an open enumeration deliberately do
            // not: they return an unmodifiable snapshot derived from the plain getter, and the list
            // is modified through addXLiteral / setXLiterals instead.
            return;
        }

        try {
            final Object firstResult = method.invoke(proxy);
            assertThat(firstResult)
                    .as("Getter %s#%s on proxy of %s must return an empty list, not null",
                        vec2xClass.getSimpleName(), method.getName(), vec12xClass.getSimpleName())
                    .isNotNull()
                    .isInstanceOf(List.class);
            assertThat((List<?>) firstResult)
                    .as("Getter %s#%s on proxy of %s must return an empty list",
                        vec2xClass.getSimpleName(), method.getName(), vec12xClass.getSimpleName())
                    .isEmpty();

            final Object secondResult = method.invoke(proxy);
            assertThat(secondResult)
                    .as("Getter %s#%s on proxy of %s must return the same list instance on repeated calls",
                        vec2xClass.getSimpleName(), method.getName(), vec12xClass.getSimpleName())
                    .isSameAs(firstResult);
        } catch (final InvocationTargetException | IllegalAccessException e) {
            // already guarded by assertThatCode above — this branch is unreachable in practice
            throw new AssertionError("Unexpected exception after successful doesNotThrowAnyException check", e);
        }
    }

    private static void assertSetterCallable(final Object proxy, final Method method,
                                             final Class<?> vec12xClass, final Class<?> vec2xClass) {
        final Class<?> parameterType = method.getParameterTypes()[0];
        final Object argument = parameterType.isPrimitive() ? primitiveDefaultValue(parameterType) : null;

        assertThatCode(() -> method.invoke(proxy, argument))
                .as("Setter %s#%s must not throw when called via proxy of %s",
                    vec2xClass.getSimpleName(), method.getName(), vec12xClass.getSimpleName())
                .doesNotThrowAnyException();
    }

    private static boolean isGetter(final Method method) {
        return method.getParameterCount() == 0
                && (method.getName().startsWith("get") || method.getName().startsWith("is"))
                && !method.getReturnType().equals(Void.TYPE);
    }

    private static boolean isSetter(final Method method) {
        return method.getParameterCount() == 1
                && method.getName().startsWith("set");
    }

    private static Object primitiveDefaultValue(final Class<?> primitiveType) {
        if (boolean.class.equals(primitiveType)) {
            return false;
        }
        if (char.class.equals(primitiveType)) {
            return '\0';
        }
        if (byte.class.equals(primitiveType)) {
            return (byte) 0;
        }
        if (short.class.equals(primitiveType)) {
            return (short) 0;
        }
        if (int.class.equals(primitiveType)) {
            return 0;
        }
        if (long.class.equals(primitiveType)) {
            return 0L;
        }
        if (float.class.equals(primitiveType)) {
            return 0F;
        }
        if (double.class.equals(primitiveType)) {
            return 0D;
        }
        throw new IllegalArgumentException("Unsupported primitive type: " + primitiveType.getName());
    }

    private static boolean isOpenEnumLiteralGetter(final Method method) {
        return method.getName()
                .endsWith("Literals");
    }

    private static boolean isListReturnType(final Method method) {
        if (!List.class.isAssignableFrom(method.getReturnType())) {
            return false;
        }
        final Type genericReturn = method.getGenericReturnType();
        return genericReturn instanceof ParameterizedType;
    }

    private static boolean hasNoArgConstructor(final Class<?> clazz) {
        try {
            clazz.getDeclaredConstructor();
            return true;
        } catch (final NoSuchMethodException e) {
            return false;
        }
    }

    private static boolean hasMappedClass(final Class<?> clazz) {
        try {
            return CONTEXT.getClassMapper().map(clazz) != null;
        } catch (final WrapperException e) {
            return false;
        }
    }

}
