/*-
 * ========================LICENSE_START=================================
 * kbl-v24
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
package com.foursoft.harness.kbl.v24.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * Utility class that contains aggregation functions that are reused throughout
 * the API.
 *
 * @author becker
 */
public final class StreamUtils {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StreamUtils.class);

    private StreamUtils() {
        // Shadowing default constructor.
    }

    /**
     * Returns an optional if the list was empty or contained one element.
     * <p>
     * If the list contains more than one element, a log-error statement is
     * created with the calling stacktrace attached. In this case the first
     * element is taken.
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
                    LOGGER.debug(message, list, new IllegalArgumentException("To many elements"));
                } else {
                    LOGGER.debug(message, list);
                }
                return Optional.ofNullable(list.get(0));
            }
            return Optional.ofNullable(list.get(0));
        });
    }

    /**
     * Returns the only element in the list. If zero or more than one element are in the list, an exception is thrown.
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

}
