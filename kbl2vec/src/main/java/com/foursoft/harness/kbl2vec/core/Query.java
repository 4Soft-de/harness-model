package com.foursoft.harness.kbl2vec.core;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@FunctionalInterface
public interface Query<T> {

    List<T> execute();

    default Stream<T> stream() {
        return execute().stream();
    }

    @SafeVarargs
    static <T> Query<T> fromLists(final List<T>... lists) {
        return () -> Arrays.stream(lists).flatMap(List::stream).toList();
    }

    static <T> Query<T> of(final Supplier<T> supplier) {
        return () -> List.of(supplier.get());
    }

    static <T> Query<T> of(final T element) {
        return () -> List.of(element);
    }

}
