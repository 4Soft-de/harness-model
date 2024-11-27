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

import com.foursoft.harness.compatibility.core.mapping.ClassMapper;
import com.foursoft.harness.compatibility.core.wrapper.CompatibilityWrapper;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;
import com.foursoft.harness.compatibility.core.wrapper.WrapperHelper;

import java.util.function.Function;

/**
 * Default implementation of a {@link Context}.
 * <p>
 * Use the {@link CompatibilityContextBuilder} to construct one.
 */
public final class CompatibilityContext implements Context {

    private final ClassMapper classMapper;
    private final WrapperRegistry wrapperRegistry;
    private final WrapperProxyFactory wrapperProxyFactory;
    private final HasUnsupportedMethods hasUnsupportedMethods;
    private final Function<CompatibilityWrapper, WrapperHelper> wrapperHelperCreationFunction;

    private Object content;

    private CompatibilityContext(final ClassMapper classMapper, final HasUnsupportedMethods hasUnsupportedMethods,
                                 final Function<CompatibilityWrapper, WrapperHelper> wrapperHelperCreationFunction) {
        this.classMapper = classMapper;
        this.hasUnsupportedMethods = hasUnsupportedMethods;
        this.wrapperHelperCreationFunction = wrapperHelperCreationFunction;

        wrapperRegistry = new WrapperRegistry(c -> new ReflectionBasedWrapper(this, c));
        wrapperProxyFactory = new WrapperProxyFactory.WrapperProxyFactoryBuilder()
                .withClassMapper(classMapper)
                .withWrapperRegistry(wrapperRegistry)
                .build();
    }

    @Override
    public ClassMapper getClassMapper() {
        return classMapper;
    }

    @Override
    public WrapperRegistry getWrapperRegistry() {
        return wrapperRegistry;
    }

    @Override
    public WrapperProxyFactory getWrapperProxyFactory() {
        return wrapperProxyFactory;
    }

    @Override
    public Function<CompatibilityWrapper, WrapperHelper> getWrapperHelperCreationFunction() {
        return wrapperHelperCreationFunction;
    }

    @Override
    public HasUnsupportedMethods checkUnsupportedMethods() {
        return hasUnsupportedMethods;
    }

    @Override
    public Object getContent() {
        return content;
    }

    @Override
    public void setContent(final Object content) {
        this.content = content;
    }

    /**
     * Builder for a {@link CompatibilityContext}.
     */
    public static final class CompatibilityContextBuilder {
        private ClassMapper classMapper;
        private HasUnsupportedMethods hasUnsupportedMethods;
        private Function<CompatibilityWrapper, WrapperHelper> wrapperHelperCreationFunction;

        /**
         * Defines the {@link ClassMapper} to use.
         *
         * @param classMapper {@link ClassMapper} to use.
         * @return The builder, useful for chaining.
         */
        public CompatibilityContextBuilder withClassMapper(final ClassMapper classMapper) {
            this.classMapper = classMapper;
            return this;
        }

        /**
         * Defines the {@link HasUnsupportedMethods unsupported methods}.
         *
         * @param hasUnsupportedMethods {@link HasUnsupportedMethods unsupported methods}.
         * @return The builder, useful for chaining.
         */
        public CompatibilityContextBuilder withUnsupportedMethodCheck(
                final HasUnsupportedMethods hasUnsupportedMethods) {
            this.hasUnsupportedMethods = hasUnsupportedMethods;
            return this;
        }

        /**
         * Defines the Function to create the {@link WrapperHelper}.
         *
         * @param wrapperHelperFunction Function to create the {@link WrapperHelper}.
         * @return The builder, useful for chaining.
         */
        public CompatibilityContextBuilder withWrapperHelperCreationFunction(
                final Function<CompatibilityWrapper, WrapperHelper> wrapperHelperFunction) {
            this.wrapperHelperCreationFunction = wrapperHelperFunction;
            return this;
        }

        /**
         * Builds the {@link CompatibilityContext}.
         * If {@link HasUnsupportedMethods} was not defined, all methods will be marked as supported.
         *
         * @return The built {@link CompatibilityContext}.
         */
        public CompatibilityContext build() {
            if (hasUnsupportedMethods == null) {
                hasUnsupportedMethods = method -> false;
            }
            if (wrapperHelperCreationFunction == null) {
                wrapperHelperCreationFunction = WrapperHelper::new;
            }
            return new CompatibilityContext(classMapper, hasUnsupportedMethods, wrapperHelperCreationFunction);
        }

    }

}
