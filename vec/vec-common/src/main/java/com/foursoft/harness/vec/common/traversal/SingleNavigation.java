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

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A {@link Navigation} leading to at most one element.
 * <p>
 * The result is always an {@link Optional}, so an absent target is part of the contract instead of being
 * expressed by a {@code null} return value.
 *
 * @param <S> Type of the source object to navigate from.
 * @param <T> Type of the element navigated to.
 */
@FunctionalInterface
public interface SingleNavigation<S, T> extends Navigation<S, Optional<T>> {

    /**
     * Continues this navigation with another single valued navigation.
     *
     * @param next Navigation to apply to the result of this navigation.
     * @param <R>  Type of the element the returned navigation leads to.
     * @return A navigation from {@code S} to at most one {@code R}.
     */
    default <R> SingleNavigation<S, R> then(final SingleNavigation<? super T, R> next) {
        return source -> from(source).flatMap(next);
    }

    /**
     * Continues this navigation with a multi valued navigation.
     *
     * @param next Navigation to apply to the result of this navigation.
     * @param <R>  Type of the elements the returned navigation leads to.
     * @return A navigation from {@code S} to an arbitrary number of {@code R}.
     */
    default <R> MultiNavigation<S, R> thenEach(final MultiNavigation<? super T, R> next) {
        return source -> from(source).stream()
                .flatMap(next);
    }

    /**
     * Returns a navigation which only leads to the element if it matches the given predicate.
     *
     * @param predicate Predicate the navigated element has to match.
     * @return A navigation leading to the filtered element.
     */
    default SingleNavigation<S, T> filter(final Predicate<? super T> predicate) {
        return source -> from(source).filter(predicate);
    }

    /**
     * Returns a navigation which only leads to the element if it is an instance of the given type.
     *
     * @param type Type the navigated element has to have.
     * @param <R>  Type to narrow the navigation to.
     * @return A navigation leading to the narrowed element.
     */
    default <R extends T> SingleNavigation<S, R> ofType(final Class<R> type) {
        return source -> from(source)
                .filter(type::isInstance)
                .map(type::cast);
    }

    /**
     * Returns this navigation as a multi valued navigation leading to zero or one element.
     *
     * @return A multi valued view on this navigation.
     */
    default MultiNavigation<S, T> asMulti() {
        return source -> from(source).stream();
    }

    /**
     * Applies this navigation to the given source object and returns {@code null} if there is no target.
     *
     * @param source Source object to navigate from.
     * @return The navigated element or {@code null} if there is none.
     */
    default T orElseNull(final S source) {
        return from(source).orElse(null);
    }

    /**
     * Applies this navigation to the given source object and returns the given fallback if there is no target.
     *
     * @param source   Source object to navigate from.
     * @param fallback Value to return if the navigation does not lead to an element.
     * @return The navigated element or the given fallback if there is none.
     */
    default T orElse(final S source, final T fallback) {
        return from(source).orElse(fallback);
    }

    /**
     * Applies this navigation to the given source object and returns the result as a {@link Stream}.
     *
     * @param source Source object to navigate from.
     * @return A stream containing the navigated element, or an empty stream if there is none.
     */
    default Stream<T> streamFrom(final S source) {
        return from(source).stream();
    }

}
