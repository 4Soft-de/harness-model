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
package com.foursoft.harness.kbl2vec;

import com.foursoft.harness.kbl.v25.KBLContainer;
import com.foursoft.harness.kbl.v25.visitor.DepthFirstTraverserImpl;
import com.foursoft.harness.kbl.v25.visitor.FunctionVisitor;
import com.foursoft.harness.kbl.v25.visitor.TraversingVisitor;
import com.foursoft.harness.kbl2vec.core.ConversionOrchestrator;
import com.foursoft.harness.kbl2vec.core.ConversionProperties;
import com.foursoft.harness.kbl2vec.core.Logging;
import com.foursoft.harness.kbl2vec.post.XmlIdPostProcessor;
import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.v2x.VecContent;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KblToVecConverter {

    private final ReflectionsBasedTransformerRegistry registry;

    public KblToVecConverter() {
        registry = new ReflectionsBasedTransformerRegistry();

    }

    public ConversionOrchestrator.Result<VecContent> convert(final KBLContainer container) {
        final ConversionOrchestrator<KBLContainer, VecContent> orchestrator = createOrchestrator();
        final ConversionOrchestrator.Result<VecContent> result = orchestrator.orchestrateTransformation(
                container);

        analyzeTransformation(container, result);

        return result;
    }

    private ConversionOrchestrator<KBLContainer, VecContent> createOrchestrator() {
        final ConversionOrchestrator<KBLContainer, VecContent> orchestrator = new ConversionOrchestrator<>(
                KBLContainer.class,
                VecContent.class,
                registry, new ConversionProperties());

        orchestrator.addPostProcessor(new XmlIdPostProcessor());

        return orchestrator;
    }

    private void analyzeTransformation(final KBLContainer source,
                                       final ConversionOrchestrator.Result<VecContent> result) {
        final Logger logger = Logging.TRANSFORM_LOGGER;
        if (logger.isDebugEnabled()) {
            final Set<Identifiable> sourceEntities = collectAllEntities(source);
            final String[] unmappedClassNames = findUnmappedClassNames(result, sourceEntities);
            logger.debug("Instances of the following classes have not been touched during conversion: {}",
                         (Object) unmappedClassNames);

            final ListMultimap<String, String> classMapping = result.entityMapping().entries()
                    .stream()
                    .map(entry -> Map.entry(entry.getKey().getClass().getSimpleName(),
                                            entry.getValue().getClass().getSimpleName()))
                    .distinct()
                    .collect(Multimaps.toMultimap(Map.Entry::getKey, Map.Entry::getValue,
                                                  MultimapBuilder.hashKeys().arrayListValues()::build));

            logger.debug("Mapped the following KBL classes into VEC classes");
            classMapping.keySet().stream().sorted().map(k -> {
                return "%1s --> %2s".formatted(k, classMapping.get(k));
            }).forEach(logger::debug);

            //TODO: Structured Output for created mappings.

        }
    }

    private static String[] findUnmappedClassNames(final ConversionOrchestrator.Result<VecContent> result,
                                                   final Set<Identifiable> sourceEntities) {
        return sourceEntities.stream()
                .filter(e -> !result.entityMapping().containsKey(e))
                .map(e -> e.getClass())
                .distinct()
                .map(Class::getSimpleName)
                .map(s -> s.startsWith("Kbl") ? s.replaceFirst("Kbl", "") : s)
                .sorted()
                .toArray(String[]::new);
    }

    private Set<Identifiable> collectAllEntities(final KBLContainer container) {
        final Set<Identifiable> elements = new HashSet<>();
        final FunctionVisitor<Identifiable, Boolean> visitor = new FunctionVisitor<>(elements::add);
        final TraversingVisitor<Boolean, RuntimeException> traversingVisitor = new TraversingVisitor<>(
                new DepthFirstTraverserImpl<>(), visitor);
        container.accept(traversingVisitor);
        return elements;
    }

}
