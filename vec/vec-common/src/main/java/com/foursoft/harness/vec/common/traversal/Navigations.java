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

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Factory methods to create {@link Navigation}s from plain functions, typically from getters of the model.
 * <p>
 * These adapters are the intended starting point of a navigation chain, for example
 * <pre>
 * {@code
 * MultiNavigation<VecPlaceableElementRole, VecLocation> locations =
 *     Navigations.collection(VecPlaceableElementRole::getRefPlacement)
 *         .ofType(VecOnPointPlacement.class)
 *         .then(Navigations.collection(VecOnPointPlacement::getLocations));
 * }
 * </pre>
 */
public final class Navigations {

    private Navigations() {
        // hide default constructor
    }

    /**
     * Creates a {@link SingleNavigation} from a function already returning an {@link Optional}.
     *
     * @param function Function to adapt.
     * @param <S>      Type of the source object to navigate from.
     * @param <T>      Type of the element navigated to.
     * @return A navigation leading to at most one element.
     */
    public static <S, T> SingleNavigation<S, T> optional(final Function<S, Optional<T>> function) {
        return function::apply;
    }

    /**
     * Creates a {@link SingleNavigation} from a function which may return {@code null}.
     *
     * @param function Function to adapt.
     * @param <S>      Type of the source object to navigate from.
     * @param <T>      Type of the element navigated to.
     * @return A navigation leading to at most one element.
     */
    public static <S, T> SingleNavigation<S, T> nullable(final Function<S, T> function) {
        return source -> Optional.ofNullable(function.apply(source));
    }

    /**
     * Creates a {@link MultiNavigation} from a function already returning a {@link Stream}.
     *
     * @param function Function to adapt.
     * @param <S>      Type of the source object to navigate from.
     * @param <T>      Type of the elements navigated to.
     * @return A navigation leading to an arbitrary number of elements.
     */
    public static <S, T> MultiNavigation<S, T> stream(final Function<S, Stream<T>> function) {
        return function::apply;
    }

    /**
     * Creates a {@link MultiNavigation} from a function returning a {@link Collection}, usually a getter of
     * a to-many association of the model.
     *
     * @param function Function to adapt.
     * @param <S>      Type of the source object to navigate from.
     * @param <T>      Type of the elements navigated to.
     * @return A navigation leading to an arbitrary number of elements.
     */
    public static <S, T> MultiNavigation<S, T> collection(
            final Function<S, ? extends Collection<? extends T>> function) {
        return source -> function.apply(source).stream()
                .map(element -> element);
    }

}
