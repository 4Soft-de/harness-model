/*-
 * ========================LICENSE_START=================================
 * VEC Common
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
package com.foursoft.harness.vec.common.traversal;

import java.util.function.Function;

/**
 * A reusable, typed navigation from a source object of type {@code S} to a result of type {@code T}.
 * <p>
 * A navigation encapsulates the way from one element of the model to another one, so that this way can be
 * named, reused and composed instead of being spelled out at every call site.
 * <p>
 * This interface is the common base type of the navigation hierarchy and mainly serves as an escape hatch for
 * results which are neither {@link java.util.Optional} nor {@link java.util.stream.Stream}. Prefer the more
 * specific sub types, as only those offer composition:
 * <ul>
 *     <li>{@link SingleNavigation} for navigations leading to at most one element.</li>
 *     <li>{@link MultiNavigation} for navigations leading to an arbitrary number of elements.</li>
 * </ul>
 * A navigation is a {@link Function} and can therefore be used wherever a function is expected.
 * <p>
 * Steps are composed with {@code then}, which is overloaded on the kind of the following step, so the
 * chain never has to name the kinds itself. The result stays single valued only if both steps are:
 * <pre>
 * single.then(single) -&gt; single      multi.then(single) -&gt; multi
 * single.then(multi)  -&gt; multi       multi.then(multi)  -&gt; multi
 * </pre>
 * Since both kinds are functional interfaces, an <em>implicitly</em> typed lambda is ambiguous as an
 * argument to {@code then}. Lift plain getters with {@link Navigations} instead, which is the intended
 * way to start a chain; an explicitly typed lambda or an exact method reference also resolves.
 *
 * @param <S> Type of the source object to navigate from.
 * @param <T> Type of the navigation result.
 */
@FunctionalInterface
public interface Navigation<S, T> extends Function<S, T> {

    /**
     * Applies this navigation to the given source object.
     * <p>
     * Equivalent to {@link #apply(Object)}, but reads better at the call site.
     *
     * @param source Source object to navigate from.
     * @return The result of the navigation.
     */
    default T from(final S source) {
        return apply(source);
    }

}
