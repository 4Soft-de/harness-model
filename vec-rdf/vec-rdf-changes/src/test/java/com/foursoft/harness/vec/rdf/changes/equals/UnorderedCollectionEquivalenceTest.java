package com.foursoft.harness.vec.rdf.changes.equals;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.common.base.Equivalence;

class UnorderedCollectionEquivalenceTest {

    UnorderedCollectionEquivalence<Object, String> unorderedCollectionEquivalence =
            new UnorderedCollectionEquivalence<>(
                    Equivalence.equals());

    @Test
    void should_return_equals_and_same_hashcode() {
        List<String> x = List.of("A", "B", "C");
        List<String> y = List.of("C", "B", "A");

        assertThat(unorderedCollectionEquivalence.hash(x)).isEqualTo(unorderedCollectionEquivalence.hash(y));
        assertThat(unorderedCollectionEquivalence.equivalent(x, y)).isTrue();
    }

    @Test
    void should_not_return_equals_and_same_hashcode() {
        List<String> x = List.of("A", "B", "C");
        List<String> y = List.of("E", "B", "A");

        assertThat(unorderedCollectionEquivalence.hash(x)).isNotEqualTo(unorderedCollectionEquivalence.hash(y));
        assertThat(unorderedCollectionEquivalence.equivalent(x, y)).isFalse();
    }

}