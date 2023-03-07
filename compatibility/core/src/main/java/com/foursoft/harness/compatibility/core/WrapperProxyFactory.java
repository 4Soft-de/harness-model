package com.foursoft.harness.compatibility.core;

import com.foursoft.harness.compatibility.core.exception.WrapperException;
import com.foursoft.harness.compatibility.core.mapping.ClassMapper;
import com.foursoft.harness.compatibility.core.util.ClassUtils;
import com.foursoft.harness.compatibility.core.util.ReflectionUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.TypeCache;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.implementation.InvocationHandlerAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
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

    private final Map<Object, Object> objectByObject = new HashMap<>();

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

        final InvocationHandler callback = registry.findOrCreate(target);
        final Class<?> proxyType = TYPE_CACHE.findOrInsert(target.getClass().getClassLoader(), target.getClass(), () ->
                new ByteBuddy().subclass(mappedClass)
                        .defineField(CALLBACK, InvocationHandler.class, Visibility.PUBLIC)
                        .method(not(isDeclaredBy(Object.class)).and(not(named("accept"))))
                        .intercept(InvocationHandlerAdapter.toField(CALLBACK).withoutMethodCache())
                        .make()
                        .load(target.getClass().getClassLoader())
                        .getLoaded());

        final T proxy = (T) proxyType.newInstance();
        final Field field = proxy.getClass().getDeclaredField(CALLBACK);
        ReflectionUtils.setFieldValue(proxy, field, callback);
        return proxy;
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
