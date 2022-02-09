/*-
 * ========================LICENSE_START=================================
 * vec120
 * %%
 * Copyright (C) 2020 - 2022 4Soft GmbH
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
package com.foursoft.vecmodel.vec120.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Utility class that contains helping methods for Streams.
 */
public final class StreamUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtils.class);

    private StreamUtils() {
        // hide default constructor
    }

    /**
     * Removes empty {@link Optional}s from a Stream and unwraps them, when used as a "flatMapper".
     *
     * @return Unwrapped optional.
     * @see Stream#flatMap(Function)
     */
    public static <S> Function<Optional<S>, Stream<S>> unwrapOptional() {
        return o -> o.map(Stream::of)
                .orElseGet(Stream::empty);
    }

    /**
     * Returns a function which returns a stream from the given function.
     * <p>
     * Can be used together with {@link Stream#flatMap(Function)} to cast the stream.
     * <pre>
     * {@code
     * List&lt;Person&gt; people = streamOfHouses
     *   .flatMap(StreamUtils.toStream(House::getPeople))
     *   .collect(Collectors.toList());
     * }
     * </pre>
     *
     * @param inputFunction Function to create a Stream.
     * @param <T>           Input Type of the Function.
     * @param <R>           Type of the Collection within the given function. Will also determine the type of the Stream.
     * @return A stream of the applied Function.
     */
    public static <T, R> Function<T, Stream<R>> toStream(final Function<T, Collection<R>> inputFunction) {
        return t -> inputFunction.apply(t)
                .stream();
    }

    /**
     * Returns an optional with the first element of a list if available, else an empty optional.
     * <p>
     * If the list contains more than one element, a debug log entry is fired.
     * It also contains the calling stacktrace attached if {@link Logger#isDebugEnabled()} is {@code true}.
     */
    public static <T> Collector<T, List<T>, Optional<T>> findOneOrNone() {
        return Collector.of(ArrayList::new, List::add, (left, right) -> {
            left.addAll(right);
            return left;
        }, list -> {
            if (list.isEmpty()) {
                return Optional.empty();
            }
            if (list.size() > 1) {
                final String message =
                        "Did find more than one element in list; will choose first one. All elements: {}";
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(message, list, new IllegalArgumentException("Too many elements found in list."));
                } else {
                    LOGGER.debug(message, list);
                }
            }
            return Optional.ofNullable(list.get(0));
        });
    }

    /**
     * Returns the only element in the list. If zero or more than one element is in the list, an exception is thrown.
     */
    public static <T> Collector<T, List<T>, T> findOne() {
        return Collector.of(ArrayList::new, List::add, (left, right) -> {
            left.addAll(right);
            return left;
        }, list -> {
            if (list.size() != 1) {
                throw new IllegalStateException("Did find zero or more than one element in list: " + list);
            }
            return list.get(0);
        });
    }

    /**
     * Returns a Function which returns a Stream of elements of the given class.
     * <p>
     * Can be used together with {@link Stream#flatMap(Function)} to filter the stream.
     * <pre>
     * {@code
     * List&lt;Developer&gt; developers = streamOfPeople
     *   .flatMap(StreamUtils.ofClass(Developer.class))
     *   .collect(Collectors.toList());
     * }
     * </pre>
     *
     * @param classOfT Class an element must have to stay in the stream.
     * @param <E>      Input type of the function.
     * @param <T>      Type of the class which the stream should have.
     * @return A stream with the input cast to the given class if applicable.
     * If this is not the case, an empty stream will be returned.
     */
    public static <E, T> Function<E, Stream<T>> ofClass(final Class<T> classOfT) {
        return t -> {
            if (t == null || !classOfT.isAssignableFrom(t.getClass())) {
                return Stream.empty();
            }
            return Stream.of(classOfT.cast(t));
        };
    }

    /**
     * Filters a given stream checking if an element is an instance of the given class and casts to it.
     *
     * @param stream Stream to check and cast to the given class.
     * @param clazz  Class which should be checked and cast to.
     * @param <I>    Input type.
     * @param <O>    Output type.
     * @return Stream of the type of the given class.
     */
    public static <I, O> Stream<O> checkAndCast(final Stream<I> stream, final Class<O> clazz) {
        return stream
                .flatMap(ofClass(clazz));
    }

    /**
     * Streams and filters a given collection checking if an element is an instance of the given class and casts to it.
     *
     * @param collection Collection to stream, check and cast to the given class.
     * @param clazz      Class which should be checked and cast to.
     * @param <I>        Input type.
     * @param <O>        Output type.
     * @return Stream of the type of the given class.
     */
    public static <I, O> Stream<O> checkAndCast(final Collection<I> collection, final Class<O> clazz) {
        return checkAndCast(collection.stream(), clazz);
    }

    /**
     * Returns a stream of the given {@link Collection} and removes {@code null} elements from it.
     *
     * @param collection Collection to stream.
     * @param <T>        Type of the Collection / Stream.
     * @return A Stream of the given Collection without {@code null} elements.
     */
    public static <T> Stream<T> nonNullStream(final Collection<T> collection) {
        return collection.stream().filter(Objects::nonNull);
    }

}
