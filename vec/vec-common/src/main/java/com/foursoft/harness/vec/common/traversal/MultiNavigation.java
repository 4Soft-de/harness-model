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

import com.foursoft.harness.vec.common.util.StreamUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * A {@link Navigation} leading to an arbitrary number of elements.
 * <p>
 * The result is always a {@link Stream}, so the navigation stays lazy and can be composed without creating
 * intermediate collections. A fresh stream is returned on every application.
 *
 * @param <S> Type of the source object to navigate from.
 * @param <T> Type of the elements navigated to.
 */
@FunctionalInterface
public interface MultiNavigation<S, T> extends Navigation<S, Stream<T>> {

    /**
     * Continues this navigation with a single valued navigation, dropping the elements without a target.
     *
     * @param next Navigation to apply to each element of this navigation.
     * @param <R>  Type of the elements the returned navigation leads to.
     * @return A navigation from {@code S} to an arbitrary number of {@code R}.
     */
    default <R> MultiNavigation<S, R> then(final SingleNavigation<? super T, R> next) {
        return source -> from(source)
                .map(next)
                .flatMap(Optional::stream);
    }

    /**
     * Continues this navigation with another multi valued navigation, flattening the results.
     *
     * @param next Navigation to apply to each element of this navigation.
     * @param <R>  Type of the elements the returned navigation leads to.
     * @return A navigation from {@code S} to an arbitrary number of {@code R}.
     */
    default <R> MultiNavigation<S, R> then(final MultiNavigation<? super T, R> next) {
        return source -> from(source).flatMap(next);
    }

    /**
     * Returns a navigation which only leads to the elements matching the given predicate.
     *
     * @param predicate Predicate the navigated elements have to match.
     * @return A navigation leading to the filtered elements.
     */
    default MultiNavigation<S, T> filter(final Predicate<? super T> predicate) {
        return source -> from(source).filter(predicate);
    }

    /**
     * Returns a navigation which only leads to the elements which are an instance of the given type.
     *
     * @param type Type the navigated elements have to have.
     * @param <R>  Type to narrow the navigation to.
     * @return A navigation leading to the narrowed elements.
     */
    default <R extends T> MultiNavigation<S, R> ofType(final Class<R> type) {
        return source -> from(source)
                .filter(type::isInstance)
                .map(type::cast);
    }

    /**
     * Reduces this navigation to a single valued one with the given reduction.
     * <p>
     * This is the general way from many elements to at most one. Use it whenever choosing the result needs
     * rules of its own rather than a filter or a mapping of the single elements; author such reductions with
     * {@link StreamUtils#reducing(java.util.function.Function)}.
     *
     * @param reduction Reduction of the navigated elements to at most one result.
     * @param <R>       Type of the element the returned navigation leads to.
     * @return A navigation from {@code S} to at most one {@code R}.
     * @see #atMostOne()
     */
    default <R> SingleNavigation<S, R> collect(final Collector<? super T, ?, Optional<R>> reduction) {
        return source -> from(source).collect(reduction);
    }

    /**
     * Returns a navigation leading to the only element of this navigation, the most common
     * {@linkplain #collect(Collector) reduction}.
     * <p>
     * If this navigation leads to more than one element, the first one is chosen and a debug log entry is
     * fired, see {@link StreamUtils#findOneOrNone()}.
     *
     * @return A single valued view on this navigation.
     */
    default SingleNavigation<S, T> atMostOne() {
        return collect(StreamUtils.findOneOrNone());
    }

    /**
     * Applies this navigation to the given source object and collects the result into a {@link List}.
     *
     * @param source Source object to navigate from.
     * @return The navigated elements, an empty list if there are none.
     */
    default List<T> listFrom(final S source) {
        return from(source).toList();
    }

    /**
     * Returns this navigation as a navigation to a {@link List} instead of a {@link Stream}.
     *
     * @return A navigation leading to the navigated elements as a list.
     */
    default Navigation<S, List<T>> asList() {
        return this::listFrom;
    }

}
