/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.core;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies the deduplication feature of the downstream transformation API on the real {@link ConversionOrchestrator}
 * (as opposed to the mock {@link TestConversionOrchestrator}, which does not execute the orchestration loop).
 */
class ConversionOrchestratorDeduplicationTest {

    @Test
    void should_collapseDuplicatesAndRepointEntityMapping_when_keyFunctionIsGiven() {
        // Given: three leaves, two of which share the name "A".
        final Leaf a1 = new Leaf("A", 1);
        final Leaf a2 = new Leaf("A", 2);
        final Leaf b1 = new Leaf("B", 5);
        final Root root = new Root(List.of(a1, a2, b1));

        final ConversionOrchestrator<Root, Container> orchestrator = orchestrator(
                deduplicatingRootTransformer(null));

        // When
        final ConversionOrchestrator.Result<Container> result = orchestrator.orchestrateTransformation(root);

        // Then: only the first item per name survives.
        assertThat(result.resultValue().items)
                .extracting(item -> item.name)
                .containsExactly("A", "B");

        // And the duplicate leaf is re-pointed to the retained ("canonical") item, so linking resolves to it.
        final Item canonicalA = result.resultValue().items.get(0);
        assertThat(result.entityMapping().get(a1)).containsExactly(canonicalA);
        assertThat(result.entityMapping().get(a2)).containsExactly(canonicalA);
    }

    @Test
    void should_invokeMergerOnCanonicalWithDuplicate_when_mergerIsGiven() {
        // Given: two leaves sharing the name "A" with values 1 and 2.
        final Root root = new Root(List.of(new Leaf("A", 1), new Leaf("A", 2)));

        final BiConsumer<Item, Item> sumValues = (canonical, duplicate) -> canonical.value += duplicate.value;
        final ConversionOrchestrator<Root, Container> orchestrator = orchestrator(
                deduplicatingRootTransformer(sumValues));

        // When
        final ConversionOrchestrator.Result<Container> result = orchestrator.orchestrateTransformation(root);

        // Then: the duplicate is folded into the retained item.
        assertThat(result.resultValue().items).singleElement()
                .satisfies(item -> {
                    assertThat(item.name).isEqualTo("A");
                    assertThat(item.value).isEqualTo(3);
                });
    }

    @Test
    void should_keepAllElements_when_noKeyFunctionIsGiven() {
        // Given: two leaves sharing the name "A" but no deduplication configured.
        final Root root = new Root(List.of(new Leaf("A", 1), new Leaf("A", 2)));

        final Transformer<Root, Container> rootTransformer = (context, source) ->
                TransformationResult.from(new Container())
                        .withDownstream(Leaf.class, Item.class, () -> source.leaves, Container::getItems)
                        .build();

        // When
        final ConversionOrchestrator.Result<Container> result = orchestrator(rootTransformer)
                .orchestrateTransformation(root);

        // Then: both items are retained (default behaviour is unchanged).
        assertThat(result.resultValue().items).hasSize(2);
    }

    private static ConversionOrchestrator<Root, Container> orchestrator(final Transformer<Root, Container> root) {
        final MapTransformerRegistry registry = new MapTransformerRegistry();
        registry.register(Root.class, Container.class, root);
        registry.register(Leaf.class, Item.class,
                          (context, source) -> TransformationResult.of(new Item(source.name, source.value)));
        return new ConversionOrchestrator<>(Root.class, Container.class, registry, new ConversionProperties());
    }

    private static Transformer<Root, Container> deduplicatingRootTransformer(final BiConsumer<Item, Item> merger) {
        return (context, source) -> TransformationResult.from(new Container())
                .withDownstream(Leaf.class, Item.class, () -> source.leaves, Container::getItems,
                                item -> item.name, merger)
                .build();
    }

    private static final class Root {
        private final List<Leaf> leaves;

        private Root(final List<Leaf> leaves) {
            this.leaves = leaves;
        }
    }

    private static final class Leaf {
        private final String name;
        private final int value;

        private Leaf(final String name, final int value) {
            this.name = name;
            this.value = value;
        }
    }

    private static final class Container {
        private final List<Item> items = new ArrayList<>();

        private List<? super Item> getItems() {
            return items;
        }
    }

    private static final class Item {
        private final String name;
        private int value;

        private Item(final String name, final int value) {
            this.name = name;
            this.value = value;
        }
    }

    private static final class MapTransformerRegistry implements TransformerRegistry {
        private final Map<Map.Entry<Class<?>, Class<?>>, Transformer<?, ?>> transformers = new HashMap<>();

        private <S, D> void register(final Class<S> source, final Class<D> destination,
                                     final Transformer<S, D> transformer) {
            transformers.put(Map.entry(source, destination), transformer);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <S, D> Collection<Transformer<S, D>> getTransformer(final Class<S> source, final Class<D> destination) {
            final Transformer<?, ?> transformer = transformers.get(Map.entry(source, destination));
            return transformer == null ? List.of() : List.of((Transformer<S, D>) transformer);
        }
    }
}
