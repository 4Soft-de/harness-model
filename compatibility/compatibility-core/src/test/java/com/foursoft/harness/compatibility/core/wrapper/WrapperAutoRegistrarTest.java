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

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.CompatibilityContext.CompatibilityContextBuilder;
import com.foursoft.harness.compatibility.core.HasUnsupportedMethods;
import com.foursoft.harness.compatibility.core.exception.WrapperException;
import com.foursoft.harness.compatibility.core.mapping.ClassMapper;
import com.foursoft.harness.compatibility.core.wrapper.fixture.badctor.BadCtorWrapper;
import com.foursoft.harness.compatibility.core.wrapper.fixture.duplicate.DuplicateSource;
import com.foursoft.harness.compatibility.core.wrapper.fixture.duplicate.DuplicateWrapperA;
import com.foursoft.harness.compatibility.core.wrapper.fixture.happy.*;
import com.foursoft.harness.compatibility.core.wrapper.fixture.nothandler.NotHandlerWrapper;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WrapperAutoRegistrarTest {

    @Test
    void registersAllAnnotatedWrappersInPackage() {
        final CompatibilityContext context = newContext();

        WrapperAutoRegistrar.registerAll(context, MultiFixtureWrapper.class);

        final InvocationHandler singleHandler = context.getWrapperRegistry().createInvocationHandler(
                new FixtureSourceA());
        final InvocationHandler multiBHandler = context.getWrapperRegistry().createInvocationHandler(
                new FixtureSourceB());
        final InvocationHandler multiCHandler = context.getWrapperRegistry().createInvocationHandler(
                new FixtureSourceC());

        assertThat(singleHandler).isInstanceOf(SingleFixtureWrapper.class);
        assertThat(multiBHandler).isInstanceOf(MultiFixtureWrapper.class);
        assertThat(multiCHandler).isInstanceOf(MultiFixtureWrapper.class);
    }

    @Test
    void rejectsWrapperWithoutContextObjectConstructor() {
        final CompatibilityContext context = newContext();

        assertThatThrownBy(() -> WrapperAutoRegistrar.registerAll(context, BadCtorWrapper.class))
                .isInstanceOf(WrapperException.class)
                .hasMessageContaining(BadCtorWrapper.class.getName())
                .hasMessageContaining("(Context, Object)");
    }

    @Test
    void rejectsWrapperThatDoesNotImplementInvocationHandler() {
        final CompatibilityContext context = newContext();

        assertThatThrownBy(() -> WrapperAutoRegistrar.registerAll(context, NotHandlerWrapper.class))
                .isInstanceOf(WrapperException.class)
                .hasMessageContaining(NotHandlerWrapper.class.getName())
                .hasMessageContaining(InvocationHandler.class.getName());
    }

    @Test
    void rejectsDuplicateSourceClassRegistration() {
        final CompatibilityContext context = newContext();

        assertThatThrownBy(() -> WrapperAutoRegistrar.registerAll(context, DuplicateWrapperA.class))
                .isInstanceOf(WrapperException.class)
                .hasMessageContaining(DuplicateSource.class.getName())
                .hasMessageContaining("more than one wrapper");
    }

    private static CompatibilityContext newContext() {
        return new CompatibilityContextBuilder()
                .withClassMapper(new NoopClassMapper())
                .build();
    }

    private static final class NoopClassMapper implements ClassMapper {
        @Override
        public Class<?> map(final Class<?> clazz) {
            return clazz;
        }

        @Override
        public String getSourcePackageName() {
            return "";
        }

        @Override
        public String getTargetPackageName() {
            return "";
        }

        @Override
        public HasUnsupportedMethods checkUnsupportedMethods() {
            return method -> false;
        }
    }

}
