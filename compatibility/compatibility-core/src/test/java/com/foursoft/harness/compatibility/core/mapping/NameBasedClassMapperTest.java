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
package com.foursoft.harness.compatibility.core.mapping;

import com.foursoft.harness.compatibility.core.exception.WrapperException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NameBasedClassMapperTest {

    private static final String SOURCE_PACKAGE =
            "com.foursoft.harness.compatibility.core.wrapper.fixture.mapping.sourcepackage";
    private static final String TARGET_PACKAGE =
            "com.foursoft.harness.compatibility.core.wrapper.fixture.mapping.targetpackage";

    @Test
    void mapsFromTheSourceToTheTargetPackage() {
        assertThat(new TestClassMapper().map(
                com.foursoft.harness.compatibility.core.wrapper.fixture.mapping.sourcepackage.MappedBean.class))
                .isSameAs(com.foursoft.harness.compatibility.core.wrapper.fixture.mapping.targetpackage.MappedBean.class);
    }

    @Test
    void mapsFromTheTargetBackToTheSourcePackage() {
        assertThat(new TestClassMapper().map(
                com.foursoft.harness.compatibility.core.wrapper.fixture.mapping.targetpackage.MappedBean.class))
                .isSameAs(com.foursoft.harness.compatibility.core.wrapper.fixture.mapping.sourcepackage.MappedBean.class);
    }

    @Test
    void answersRepeatedRequestsWithTheSameResult() {
        final TestClassMapper mapper = new TestClassMapper();

        assertThat(mapper.map(
                com.foursoft.harness.compatibility.core.wrapper.fixture.mapping.sourcepackage.MappedBean.class))
                .isSameAs(mapper.map(
                        com.foursoft.harness.compatibility.core.wrapper.fixture.mapping.sourcepackage.MappedBean.class));
    }

    @Test
    void keepsRejectingAClassFromNeitherPackage() {
        // A rejected class must not end up in the cache, otherwise the second call would answer with
        // null instead of the exception.
        final TestClassMapper mapper = new TestClassMapper();

        for (int i = 0; i < 2; i++) {
            assertThatThrownBy(() -> mapper.map(String.class))
                    .isInstanceOf(WrapperException.class)
                    .hasMessageContaining("neither from the source package nor the target package");
        }
    }

    private static class TestClassMapper extends NameBasedClassMapper {

        TestClassMapper() {
            super(SOURCE_PACKAGE, TARGET_PACKAGE);
        }

    }

}
