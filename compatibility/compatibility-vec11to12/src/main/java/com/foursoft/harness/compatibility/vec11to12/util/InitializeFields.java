/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.1.X To VEC 1.2.X
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
package com.foursoft.harness.compatibility.vec11to12.util;

import com.foursoft.harness.compatibility.core.exception.WrapperException;
import com.foursoft.harness.compatibility.core.util.ReflectionUtils;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.reflections.ReflectionUtils.*;

public class InitializeFields {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitializeFields.class);

    private static final Map<Class<?>, Map<String, Method>> CLASS_TO_METHODS_MAP = new HashMap<>();
    private static final Map<Class<?>, Map<String, Field>> CLASS_TO_FIELDS_MAP = new HashMap<>();

    private InitializeFields() {
    }

    /**
     * Initializes the fields of the given VEC 1.2.X Visitable.
     * <p>
     * <b>Notice:</b> Currently, parallel execution is not allowed which is why this method is synchronized.
     */
    public static synchronized void initializeFields(final com.foursoft.harness.vec.v12x.visitor.Visitable content) {
        final com.foursoft.harness.vec.v12x.visitor.Visitor<Void, RuntimeException> proxyInstance =
                (com.foursoft.harness.vec.v12x.visitor.Visitor<Void, RuntimeException>)
                        Proxy.newProxyInstance(
                                InitializeFields.class.getClassLoader(),
                                new Class[]{com.foursoft.harness.vec.v12x.visitor.Visitor.class},
                                new DynamicInvocationHandler());

        content.accept(
                new com.foursoft.harness.vec.v12x.visitor.TraversingVisitor<>(
                        new com.foursoft.harness.vec.v12x.visitor.DepthFirstTraverserImpl<>(),
                        proxyInstance));
    }

    /**
     * Initializes the fields of the given VEC 1.1.X Visitable.
     * <p>
     * <b>Notice:</b> Currently, parallel execution is not allowed which is why this method is synchronized.
     */
    public static synchronized void initializeFields(final com.foursoft.harness.vec.v113.visitor.Visitable content) {
        final com.foursoft.harness.vec.v113.visitor.Visitor<Void, RuntimeException> proxyInstance =
                (com.foursoft.harness.vec.v113.visitor.Visitor<Void, RuntimeException>)
                        Proxy.newProxyInstance(
                                InitializeFields.class.getClassLoader(),
                                new Class[]{com.foursoft.harness.vec.v113.visitor.Visitor.class},
                                new DynamicInvocationHandler());

        content.accept(
                new com.foursoft.harness.vec.v113.visitor.TraversingVisitor<>(
                        new com.foursoft.harness.vec.v113.visitor.DepthFirstTraverserImpl<>(),
                        proxyInstance));
    }

    private static class DynamicInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(final Object proxy, final Method proxyMethod, final Object[] args) {     //NOSONAR
            if (args.length == 0) {
                return null;
            }
            final Object aBean = args[0];
            final Class<?> aClass = aBean.getClass();

            final Map<String, Field> fieldsMap = getOrCreateInnerFieldMap(aClass);
            final Map<String, Method> getterMap = getOrCreateInnerMethodMap(aClass);

            for (final Entry<String, Field> fieldEntry : fieldsMap.entrySet()) {
                final Field field = fieldEntry.getValue();
                final Method method = getterMap.get(fieldEntry.getKey());
                // This will work out since both getterMap and fieldsMap contain lowercase keys.
                // In addition to, the prefix was removed from the getters so the values can match.
                if (method == null) {
                    LOGGER.warn("Cannot find field {} in class {} with proxy {}.",
                                fieldEntry.getKey(), aClass, proxy);
                } else {
                    try {
                        setField(aBean, field, method);
                    } catch (final Exception e) {
                        final XmlElement xmlElementAnno = field.getAnnotation(XmlElement.class);
                        final String fieldRole = xmlElementAnno.required() ? "required field" : "field";
                        final String errorMsg = String.format("Could not set %s %s for class %s.",
                                                              fieldRole, field.getName(), aBean.getClass().getName());
                        throw new WrapperException(errorMsg, e);
                    }
                }
            }
            return null;
        }

        private static synchronized Map<String, Method> getOrCreateInnerMethodMap(final Class<?> clazz) {
            return CLASS_TO_METHODS_MAP.computeIfAbsent(clazz, c -> {
                // This is basically a Map<FieldName, GetterMethod> here.
                // The prefix is removed and made lower case. So `getCustomElement` will be changed to `customelement`.
                // The fields will also be changed to lower case to they will match up in the end.

                final Map<String, Method> getterMap = getAllMethods(c,
                                                                    withModifier(Modifier.PUBLIC), withPrefix("get"),
                                                                    withParametersCount(0))
                        .stream()
                        .collect(Collectors.toMap(p -> p.getName().substring("get".length()).toLowerCase(),
                                                  Function.identity(),
                                                  // Don't update the stored element.
                                                  (m1, m2) -> m1));

                final Map<String, Method> isMethods = getAllMethods(c,
                                                                    withModifier(Modifier.PUBLIC), withPrefix("is"),
                                                                    withParametersCount(0))
                        .stream()
                        .collect(Collectors.toMap(p -> p.getName().substring("is".length()).toLowerCase(),
                                                  Function.identity(),
                                                  // Don't update the stored element.
                                                  (m1, m2) -> m1));

                getterMap.putAll(isMethods);

                return getterMap;
            });
        }

        private static synchronized Map<String, Field> getOrCreateInnerFieldMap(final Class<?> clazz) {
            return CLASS_TO_FIELDS_MAP.computeIfAbsent(clazz, c -> {
                final Set<Field> allFields = getAllFields(c, withAnnotation(XmlElement.class));
                allFields.addAll(getAllFields(c, withAnnotation(XmlID.class)));

                return allFields.stream()
                        .collect(Collectors.toMap(field -> field.getName().toLowerCase(), Function.identity()));
            });
        }

        private static void setField(final Object aBean, final Field field, final Method method)
                throws IllegalAccessException, InvocationTargetException {
            final Object invoke = method.invoke(aBean);
            if (invoke instanceof Collection && ((Collection<?>) invoke).isEmpty()) {
                ReflectionUtils.setFieldValue(aBean, field, null);
            } else {
                ReflectionUtils.setFieldValue(aBean, field, invoke);
            }
        }

    }
}
