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
package com.foursoft.harness.compatibility.core;

import com.foursoft.harness.compatibility.core.wrapper.fixture.additions.AdditionsBean;
import com.foursoft.harness.compatibility.core.wrapper.fixture.additions.ParentAdditionsBean;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.foursoft.harness.compatibility.core.PropertyAddition.backRef;
import static com.foursoft.harness.compatibility.core.PropertyAddition.list;
import static com.foursoft.harness.compatibility.core.PropertyAddition.value;
import static org.assertj.core.api.Assertions.assertThat;

class PurePropertyAdditionsTest {

    @Test
    void returnsAdditionsRegisteredDirectlyForClass() {
        final PurePropertyAdditions additions = new PurePropertyAdditions()
                .register(AdditionsBean.class, value("foo"), list("barList"));

        final List<PropertyAddition> result = additions.getAdditions(AdditionsBean.class);

        assertThat(result).containsExactlyInAnyOrder(value("foo"), list("barList"));
    }

    @Test
    void includesAdditionsFromParentClass() {
        final PurePropertyAdditions additions = new PurePropertyAdditions()
                .register(ParentAdditionsBean.class, value("parentProp"))
                .register(AdditionsBean.class, value("foo"));

        final List<PropertyAddition> result = additions.getAdditions(AdditionsBean.class);

        assertThat(result).containsExactlyInAnyOrder(value("parentProp"), value("foo"));
    }

    @Test
    void doesNotIncludeAdditionsFromUnrelatedClass() {
        final PurePropertyAdditions additions = new PurePropertyAdditions()
                .register(AdditionsBean.class, value("foo"))
                .register(String.class, value("unrelated"));

        final List<PropertyAddition> result = additions.getAdditions(AdditionsBean.class);

        assertThat(result).containsExactly(value("foo"));
    }

    @Test
    void returnsEmptyListForClassWithNoAdditions() {
        final PurePropertyAdditions additions = new PurePropertyAdditions();

        assertThat(additions.getAdditions(AdditionsBean.class)).isEmpty();
    }

    @Test
    void cachesResultAcrossMultipleCalls() {
        final PurePropertyAdditions additions = new PurePropertyAdditions()
                .register(AdditionsBean.class, value("foo"));

        final List<PropertyAddition> first = additions.getAdditions(AdditionsBean.class);
        final List<PropertyAddition> second = additions.getAdditions(AdditionsBean.class);

        assertThat(first).isSameAs(second);
    }

    @Test
    void emptyStaticInstanceHasNoAdditions() {
        assertThat(PurePropertyAdditions.empty().getAdditions(AdditionsBean.class)).isEmpty();
    }

    @Test
    void allThreeAdditionTypesAreSupported() {
        final PurePropertyAdditions additions = new PurePropertyAdditions()
                .register(AdditionsBean.class,
                          value("foo"),
                          list("barList"),
                          backRef("bazSet"));

        final List<PropertyAddition> result = additions.getAdditions(AdditionsBean.class);

        assertThat(result).hasSize(3);
        assertThat(result).filteredOn(a -> a instanceof PropertyAddition.Value).hasSize(1);
        assertThat(result).filteredOn(a -> a instanceof PropertyAddition.MutableList).hasSize(1);
        assertThat(result).filteredOn(a -> a instanceof PropertyAddition.BackRef).hasSize(1);
    }

    @Test
    void registerIsFluentAndAccumulatesAdditions() {
        final PurePropertyAdditions additions = new PurePropertyAdditions()
                .register(AdditionsBean.class, value("foo"))
                .register(AdditionsBean.class, value("bar"));

        assertThat(additions.getAdditions(AdditionsBean.class))
                .containsExactlyInAnyOrder(value("foo"), value("bar"));
    }
}
