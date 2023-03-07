package com.foursoft.harness.compatibility.core;

import com.foursoft.harness.compatibility.core.mapping.ClassMapper;
import com.foursoft.harness.compatibility.core.wrapper.ReflectionBasedWrapper;

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

    private Object content;

    private CompatibilityContext(final ClassMapper classMapper, final HasUnsupportedMethods hasUnsupportedMethods) {
        this.classMapper = classMapper;
        this.hasUnsupportedMethods = hasUnsupportedMethods;
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
         * Builds the {@link CompatibilityContext}.
         * If {@link HasUnsupportedMethods} was not defined, all methods will be marked as supported.
         *
         * @return The built {@link CompatibilityContext}.
         */
        public CompatibilityContext build() {
            if (hasUnsupportedMethods == null) {
                hasUnsupportedMethods = method -> false;
            }
            return new CompatibilityContext(classMapper, hasUnsupportedMethods);
        }

    }

}