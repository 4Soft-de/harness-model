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
package com.foursoft.harness.compatibility.core;

import com.foursoft.harness.compatibility.core.exception.WrapperException;
import com.foursoft.harness.compatibility.core.mapping.ClassMapper;
import com.foursoft.harness.compatibility.core.util.ClassUtils;
import com.foursoft.harness.compatibility.core.util.ReflectionUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.TypeCache;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatcher;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * Factory class to create proxy classes.
 * <p>
 * Use the {@link WrapperProxyFactoryBuilder} to construct one.
 */
public final class WrapperProxyFactory {

    private static final String CALLBACK = "callback";
    private static final TypeCache<Object> TYPE_CACHE = new TypeCache<>(TypeCache.Sort.SOFT);

    /**
     * Simple name of the interface every literal of an open enumeration implements. It is matched by
     * name because this module knows nothing about the models it proxies: the interface lives in the
     * runtime package of the respective model, which is an option of the XJC plugin generating the
     * accessors, in the same way the plugin itself resolves it.
     */
    private static final String LITERAL_INTERFACE = "OpenEnumLiteral";

    /**
     * Wrappers are looked up by object identity: a source object and its wrapper belong together
     * one to one, and hashing the source objects instead would walk their (potentially large) graphs.
     */
    private final Map<Object, Object> objectByObject = new IdentityHashMap<>();

    private final ClassMapper classMapper;
    private final WrapperRegistry registry;

    private WrapperProxyFactory(final ClassMapper classMapper, final WrapperRegistry registry) {
        this.classMapper = classMapper;
        this.registry = registry;
    }

    /**
     * Creates a proxy for the given object.
     *
     * @param target Object to create the proxy for.
     * @param <T>    Dynamic return type, independent of the given object's class.
     * @return Proxy for the given class.
     * @throws WrapperException In case the proxy couldn't be created.
     */
    public <T> T createProxy(final Object target) throws WrapperException {
        Objects.requireNonNull(target, "Can not create wrapper/interceptor. The target object is null");

        final Object wrapper = objectByObject.computeIfAbsent(target, t -> {
            try {
                return create(t);
            } catch (final InstantiationException | IllegalAccessException | NoSuchFieldException e) {
                throw new WrapperException("Can not create wrapper / interceptor.", e);
            }
        });
        return (T) wrapper;
    }

    private <T> T create(final Object target)
            throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        final Class<?> classToMap = ClassUtils.getNonProxyClass(target.getClass());
        final Class<?> mappedClass = classMapper.map(classToMap);
        if (mappedClass == null) {
            return null;
        }

        final InvocationHandler callback = registry.createInvocationHandler(target);
        final Class<?> proxyType = TYPE_CACHE.findOrInsert(target.getClass().getClassLoader(), target.getClass(), () ->
                new ByteBuddy().subclass(mappedClass)
                        .defineField(CALLBACK, InvocationHandler.class, Visibility.PUBLIC)
                        .method(not(isDeclaredBy(Object.class)).and(not(named("accept")))
                                        .and(not(isOpenEnumLiteralAccessor())))
                        .intercept(InvocationHandlerAdapter.toField(CALLBACK))
                        .make()
                        .load(target.getClass().getClassLoader())
                        .getLoaded());

        final T proxy = (T) proxyType.newInstance();
        final Field field = proxy.getClass().getDeclaredField(CALLBACK);
        ReflectionUtils.setFieldValue(proxy, field, callback);
        return proxy;
    }

    /**
     * Matches the typed accessors of an open enumeration, which must keep their own implementation
     * instead of being routed to the wrapped object.
     *
     * <p>
     * Whether a property is an open enumeration is decided by the standard, and standards evolve, so
     * these accessors regularly have no counterpart on the wrapped object - the property may be a
     * plain string there, or may not exist at all. They need none: their implementation derives the
     * literals from the plain accessor of the same property, and that one is proxied as usual. So
     * letting them run unintercepted yields the right value wherever the plain accessor takes it
     * from, and interprets it with the vocabulary of the version the caller is using.
     * </p>
     * <p>
     * The accessors are recognized by their shape, not by their name alone, so that a model method
     * which merely happens to end in {@code Literal} is still proxied: a getter returns a literal or
     * a collection of literals, a setter or adder takes one and returns nothing.
     * </p>
     */
    private static ElementMatcher.Junction<MethodDescription> isOpenEnumLiteralAccessor() {
        final ElementMatcher.Junction<TypeDescription.Generic> literals = isLiteral().or(isLiteralCollection());
        final ElementMatcher.Junction<MethodDescription> getter = takesArguments(0).and(returnsGeneric(literals));
        final ElementMatcher.Junction<MethodDescription> setter = takesArguments(1).and(returns(void.class))
                .and(takesGenericArgument(0, literals));
        return nameEndsWith("Literal").or(nameEndsWith("Literals"))
                .and(getter.or(setter));
    }

    private static ElementMatcher.Junction<TypeDescription.Generic> isLiteral() {
        return erasure(hasSuperType(type -> LITERAL_INTERFACE.equals(type.getSimpleName())));
    }

    private static ElementMatcher.Junction<TypeDescription.Generic> isLiteralCollection() {
        return erasure(isSubTypeOf(Collection.class)).and(type -> type.getSort().isParameterized()
                && type.getTypeArguments().size() == 1
                && isLiteral().matches(upperBound(type.getTypeArguments().getOnly())));
    }

    /**
     * @return The type itself, or for a wildcard such as {@code ? extends X} its upper bound.
     */
    private static TypeDescription.Generic upperBound(final TypeDescription.Generic type) {
        return type.getSort().isWildcard() ? type.getUpperBounds().getOnly() : type;
    }

    /**
     * Builder for a {@link WrapperProxyFactory}.
     */
    public static class WrapperProxyFactoryBuilder {

        private ClassMapper classMapper;
        private WrapperRegistry registry;

        /**
         * Defines the {@link ClassMapper} to use.
         *
         * @param classMapper {@link ClassMapper} to use.
         * @return The builder, useful for chaining.
         */
        public WrapperProxyFactoryBuilder withClassMapper(final ClassMapper classMapper) {
            this.classMapper = classMapper;
            return this;
        }

        /**
         * Defines the {@link WrapperRegistry} to use.
         *
         * @param registry {@link WrapperRegistry} to use.
         * @return The builder, useful for chaining.
         */
        public WrapperProxyFactoryBuilder withWrapperRegistry(final WrapperRegistry registry) {
            this.registry = registry;
            return this;
        }

        /**
         * Builds the {@link WrapperProxyFactory}.
         *
         * @return The built {@link WrapperProxyFactory}.
         */
        public WrapperProxyFactory build() {
            return new WrapperProxyFactory(classMapper, registry);
        }

    }

}
