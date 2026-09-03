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
package com.foursoft.harness.compatibility.core.util;

import com.foursoft.harness.compatibility.core.exception.WrapperException;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Copies the value returned by a bean's getter into the backing field for every JAXB relevant field
 * ({@link XmlElement} / {@link XmlID}) of that bean.
 * <p>
 * This is what makes a wrapped (proxied) object marshallable: the wrapper computes its values on the fly, while
 * JAXB reads the fields directly. Empty collections are reset to {@code null} so they are not written out.
 * <p>
 * The reflective lookup is done <b>once per class</b> and cached in a {@link ClassValue}, which is lock-free on
 * read and does not keep the (dynamically generated) proxy classes alive. Calling this class concurrently for
 * independent object trees is safe.
 */
public final class XmlFieldInitializer implements InvocationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlFieldInitializer.class);

    private static final String GET_PREFIX = "get";
    private static final String IS_PREFIX = "is";

    private static final XmlFieldInitializer INSTANCE = new XmlFieldInitializer();

    /**
     * Per class list of field / getter pairs to copy. Built lazily, never mutated afterwards.
     */
    private static final ClassValue<Accessor[]> ACCESSORS = new ClassValue<>() {
        @Override
        protected Accessor[] computeValue(final Class<?> type) {
            return buildAccessors(type);
        }
    };

    /**
     * Per visitor interface proxy instance. The invocation handler is stateless, so one instance can be shared.
     */
    private static final ClassValue<Object> VISITOR_PROXIES = new ClassValue<>() {
        @Override
        protected Object computeValue(final Class<?> type) {
            return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, INSTANCE);
        }
    };

    private XmlFieldInitializer() {
    }

    /**
     * Returns a shared visitor proxy which initializes the fields of every visited bean.
     *
     * @param visitorInterface Version specific {@code Visitor} interface to implement.
     * @param <V>              Type of the visitor interface.
     * @return A never-{@code null}, thread-safe visitor proxy.
     */
    public static <V> V visitorProxy(final Class<V> visitorInterface) {
        return visitorInterface.cast(VISITOR_PROXIES.get(visitorInterface));
    }

    /**
     * Initializes the JAXB relevant fields of a single bean.
     *
     * @param bean Bean to initialize, may not be {@code null}.
     */
    public static void initializeFields(final Object bean) {
        for (final Accessor accessor : ACCESSORS.get(bean.getClass())) {
            accessor.copyToField(bean);
        }
    }

    @Override
    public Object invoke(final Object proxy, final Method proxyMethod, final Object[] args) {
        if (args != null && args.length > 0 && args[0] != null) {
            initializeFields(args[0]);
        }
        return null;
    }

    private static Accessor[] buildAccessors(final Class<?> type) {
        final Map<String, Method> getters = collectGetters(type);
        final Map<String, Field> fields = collectFields(type);

        final List<Accessor> accessors = new ArrayList<>(fields.size());
        for (final Map.Entry<String, Field> entry : fields.entrySet()) {
            // Both maps are keyed by the lower case property name, so field `customElement` matches
            // `getCustomElement` / `isCustomElement`.
            final Method getter = getters.get(entry.getKey());
            final Field field = entry.getValue();
            if (getter == null) {
                LOGGER.warn("Cannot find getter for field {} in class {}.", field.getName(), type.getName());
                continue;
            }
            accessors.add(Accessor.create(field, getter));
        }
        return accessors.toArray(new Accessor[0]);
    }

    private static Map<String, Method> collectGetters(final Class<?> type) {
        // `getMethods` already returns the inherited public methods, so there is no need to walk the hierarchy.
        final Map<String, Method> getters = new HashMap<>();
        final Map<String, Method> isGetters = new HashMap<>();
        for (final Method method : type.getMethods()) {
            if (method.getParameterCount() != 0 || method.getReturnType() == void.class
                    || method.isBridge() || method.isSynthetic() || Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            final String name = method.getName();
            if (name.startsWith(GET_PREFIX) && name.length() > GET_PREFIX.length()) {
                getters.putIfAbsent(propertyKey(name, GET_PREFIX), method);
            } else if (name.startsWith(IS_PREFIX) && name.length() > IS_PREFIX.length()) {
                isGetters.putIfAbsent(propertyKey(name, IS_PREFIX), method);
            }
        }
        // `is` wins over `get` for the same property, as it did before.
        getters.putAll(isGetters);
        return getters;
    }

    private static String propertyKey(final String methodName, final String prefix) {
        return methodName.substring(prefix.length()).toLowerCase(Locale.ROOT);
    }

    private static Map<String, Field> collectFields(final Class<?> type) {
        // Most derived class first, so a shadowing field wins over the one it shadows.
        final Map<String, Field> fields = new LinkedHashMap<>();
        for (Class<?> current = type; current != null && current != Object.class;
             current = current.getSuperclass()) {
            for (final Field field : current.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()
                        || !(field.isAnnotationPresent(XmlElement.class)
                        || field.isAnnotationPresent(XmlID.class))) {
                    continue;
                }
                fields.putIfAbsent(field.getName().toLowerCase(Locale.ROOT), field);
            }
        }
        return fields;
    }

    /**
     * A single field / getter pair, prepared once so that the copy itself needs no lookups and no
     * accessibility juggling.
     */
    private record Accessor(Field field, Method getter, boolean required) {

        private static Accessor create(final Field field, final Method getter) {
            // Same policy as ReflectionUtils#setFieldValue - a field is opened up once and the Field
            // object remembers it. Here it can be done up front because the accessors are cached, and it
            // has to be, since the value is read back before it is written.
            field.setAccessible(true);      //NOSONAR
            getter.trySetAccessible();
            final XmlElement xmlElement = field.getAnnotation(XmlElement.class);
            return new Accessor(field, getter, xmlElement != null && xmlElement.required());
        }

        private void copyToField(final Object bean) {
            try {
                Object value = getter.invoke(bean);
                if (value instanceof Collection<?> collection && collection.isEmpty()) {
                    value = null;
                }
                // For an unwrapped bean the getter simply returns the field, so there is nothing to write.
                if (field.get(bean) != value) {
                    field.set(bean, value);
                }
            } catch (final Exception e) {
                final String errorMsg = String.format("Could not set %s %s for class %s.",
                                                      required ? "required field" : "field",
                                                      field.getName(), bean.getClass().getName());
                throw new WrapperException(errorMsg, e);
            }
        }

    }

}
