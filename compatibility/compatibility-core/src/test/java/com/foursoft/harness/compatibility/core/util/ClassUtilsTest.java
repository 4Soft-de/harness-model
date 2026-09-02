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

import com.foursoft.harness.compatibility.core.wrapper.fixture.mapping.sourcepackage.MappedBean;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Both methods of {@link ClassUtils} are called for every single method invocation on a proxy and are
 * therefore cached. These tests pin down that the caching does not change what they return.
 */
class ClassUtilsTest {

    private static final String TARGET_PACKAGE =
            "com.foursoft.harness.compatibility.core.wrapper.fixture.mapping.targetpackage";

    @Test
    void getNonProxyClassReturnsTheClassItselfForAPlainClass() {
        assertThat(ClassUtils.getNonProxyClass(MappedBean.class))
                .isSameAs(MappedBean.class);
    }

    @Test
    void getNonProxyClassIsStableAcrossCalls() {
        final Class<?> first = ClassUtils.getNonProxyClass(MappedBean.class);
        final Class<?> second = ClassUtils.getNonProxyClass(MappedBean.class);

        assertThat(first).isSameAs(second);
    }

    @Test
    void getMappedClassResolvesTheSameSimpleNameInTheGivenPackage() throws ClassNotFoundException {
        final Class<?> mapped = ClassUtils.getMappedClass(MappedBean.class, TARGET_PACKAGE);

        assertThat(mapped)
                .isSameAs(com.foursoft.harness.compatibility.core.wrapper.fixture.mapping.targetpackage.MappedBean.class);
    }

    @Test
    void getMappedClassIsStableAcrossCallsAndPackages() throws ClassNotFoundException {
        final Class<?> toTarget = ClassUtils.getMappedClass(MappedBean.class, TARGET_PACKAGE);
        final Class<?> toTargetAgain = ClassUtils.getMappedClass(MappedBean.class, TARGET_PACKAGE);
        final Class<?> toOwnPackage = ClassUtils.getMappedClass(MappedBean.class, MappedBean.class.getPackageName());

        assertThat(toTarget).isSameAs(toTargetAgain);
        assertThat(toOwnPackage).isSameAs(MappedBean.class);
    }

    @Test
    void getMappedClassKeepsFailingForAnUnknownPackage() {
        // A failed lookup must not be remembered as a result, otherwise the second call would answer
        // with null instead of the exception.
        for (int i = 0; i < 2; i++) {
            assertThatThrownBy(() -> ClassUtils.getMappedClass(MappedBean.class, "does.not.exist"))
                    .isInstanceOf(ClassNotFoundException.class);
        }
    }

}
