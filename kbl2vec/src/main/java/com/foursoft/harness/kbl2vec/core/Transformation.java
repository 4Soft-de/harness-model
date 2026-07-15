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

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A single, queued transformation step of the conversion pipeline: it produces destination objects of type
 * {@code D} from source objects of type {@code S} (obtained via {@link #sourceQuery()}) and attaches each result
 * to its parent via the {@link #accumulator()}.
 * <p>
 * Optionally a transformation can deduplicate the destination objects it produces: if {@link #deduplicationKey()}
 * is non-{@code null}, the orchestrator keeps only the first destination object per key. Every source object whose
 * destination object is a duplicate is re-pointed in the entity mapping to the retained ("canonical") destination
 * object, so second-phase reference linking resolves to the canonical instance. If a {@link #merger()} is present,
 * it is invoked as {@code merger.accept(canonical, duplicate)} to fold information of the dropped duplicate into
 * the retained instance; for pure "throw away the duplicate" cases the merger may be {@code null}.
 *
 * @param sourceClass      the source type, part of the transformer registry lookup key
 * @param destinationClass the destination type, part of the transformer registry lookup key
 * @param sourceQuery      produces the source objects to transform
 * @param accumulator      attaches a produced destination object to its parent
 * @param deduplicationKey extracts the deduplication key from a destination object, or {@code null} to disable
 *                         deduplication for this transformation
 * @param merger           folds a dropped duplicate into the retained canonical instance, or {@code null} if no
 *                         merging is required (only meaningful together with {@code deduplicationKey})
 */
public record Transformation<S, D>(Class<S> sourceClass, Class<D> destinationClass, Query<S> sourceQuery,
                                   Consumer<D> accumulator, Function<D, Object> deduplicationKey,
                                   BiConsumer<D, D> merger) {

    public Transformation(final Class<S> sourceClass, final Class<D> destinationClass, final Query<S> sourceQuery,
                          final Consumer<D> accumulator) {
        this(sourceClass, destinationClass, sourceQuery, accumulator, null, null);
    }

}
