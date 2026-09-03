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
import com.foursoft.harness.compatibility.core.exception.WrapperException;
import com.foursoft.harness.compatibility.core.mapping.ClassMapper;
import com.foursoft.harness.compatibility.core.wrapper.fixture.badctor.BadCtorWrapper;
import com.foursoft.harness.compatibility.core.wrapper.fixture.duplicate.DuplicateSource;
import com.foursoft.harness.compatibility.core.wrapper.fixture.duplicate.DuplicateWrapperA;
import com.foursoft.harness.compatibility.core.wrapper.fixture.happy.*;
import com.foursoft.harness.compatibility.core.wrapper.fixture.nothandler.NotHandlerWrapper;
import com.foursoft.harness.compatibility.core.wrapper.fixture.scanlogging.ScanLoggingWrapper;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

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
    void registersAllAnnotatedWrappersForEveryContextWhileScanningOnlyOnce() {
        final CompatibilityContext firstContext = newContext();
        final CompatibilityContext secondContext = newContext();

        WrapperAutoRegistrar.registerAll(firstContext, MultiFixtureWrapper.class);
        WrapperAutoRegistrar.registerAll(secondContext, MultiFixtureWrapper.class);

        assertThat(WrapperAutoRegistrar.scanWrapperClasses(MultiFixtureWrapper.class))
                .isSameAs(WrapperAutoRegistrar.scanWrapperClasses(MultiFixtureWrapper.class));
        assertThat(secondContext.getWrapperRegistry().createInvocationHandler(new FixtureSourceA()))
                .isInstanceOf(SingleFixtureWrapper.class);
        assertThat(secondContext.getWrapperRegistry().createInvocationHandler(new FixtureSourceB()))
                .isInstanceOf(MultiFixtureWrapper.class);
    }

    @Test
    void logsTheClasspathScanOncePerPackageSetNoMatterHowOftenWrappersAreRegistered() {
        final ch.qos.logback.classic.Logger logger =
                ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger(WrapperAutoRegistrar.class);
        final Level originalLevel = logger.getLevel();
        final ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logger.setLevel(Level.DEBUG);
        logger.addAppender(appender);
        try {
            for (int i = 0; i < 25; i++) {
                WrapperAutoRegistrar.registerAll(newContext(), ScanLoggingWrapper.class);
            }
        } finally {
            logger.detachAppender(appender);
            logger.setLevel(originalLevel);
            appender.stop();
        }

        assertThat(appender.list)
                .extracting(ILoggingEvent::getFormattedMessage)
                .containsExactly("Found 1 @Wraps annotated wrapper(s) in packages "
                                         + "'[com.foursoft.harness.compatibility.core.wrapper.fixture.scanlogging]'.");
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

    }

}
