/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
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
package com.foursoft.harness.vec.scripting.composite;

import com.foursoft.harness.vec.scripting.Customizer;
import com.foursoft.harness.vec.scripting.components.PartOccurrenceBuilder;
import com.foursoft.harness.vec.scripting.components.PartUsageBuilder;
import com.foursoft.harness.vec.scripting.harness.ConnectivityBuilder;
import com.foursoft.harness.vec.scripting.placement.PlacementSpecificationBuilder;
import com.foursoft.harness.vec.scripting.routing.RoutingSpecificationBuilder;
import com.foursoft.harness.vec.scripting.topology.TopologyBuilder;
import com.foursoft.harness.vec.scripting.topology.TopologyReusageBuilder;

/**
 * The aspects for the definition of a composite part (e.g. a harness or an assembly): the instances of its
 * components and the aspects that relate them (contacting, topology, placement, routing). Implemented by the
 * builders of documents that define composite parts, which provide the {@link CompositePartDefinitionContext} of
 * their document.
 *
 * @param <SELF> the implementing builder, returned by the fluent methods.
 */
public interface CompositePartDefinition<SELF extends CompositePartDefinition<SELF>> {

    CompositePartDefinitionContext compositePartDefinition();

    SELF self();

    default SELF addPartOccurrence(final String identification, final String partNumber) {
        return addPartOccurrence(identification, partNumber, null);
    }

    default SELF addPartOccurrence(final String identification, final String partNumber,
                                   final Customizer<PartOccurrenceBuilder> customizer) {
        compositePartDefinition().addPartOccurrence(identification, partNumber, customizer);
        return self();
    }

    /**
     * Adds a {@link com.foursoft.harness.vec.v2x.VecPartUsage} whose specifications are contained in the document
     * of this builder.
     */
    default SELF addPartUsage(final String identification, final Customizer<PartUsageBuilder> customizer) {
        compositePartDefinition().addPartUsage(identification, customizer);
        return self();
    }

    /**
     * Adds a {@link com.foursoft.harness.vec.v2x.VecPartUsage} whose specifications are contained in the given
     * document.
     */
    default SELF addPartUsage(final String identification, final String specificationsDocumentNumber,
                              final Customizer<PartUsageBuilder> customizer) {
        compositePartDefinition().addPartUsage(identification, specificationsDocumentNumber, customizer);
        return self();
    }

    default SELF addConnection(final String wireElementReferenceId,
                               final Customizer<ConnectivityBuilder> customizer) {
        compositePartDefinition().addConnection(wireElementReferenceId, customizer);
        return self();
    }

    default SELF withTopology(final Customizer<TopologyBuilder> customizer) {
        compositePartDefinition().withTopology(customizer);
        return self();
    }

    /**
     * Reuses elements of the topology of another document (e.g. the topology of an assembly in its part master data)
     * in the topology of this document. Each call creates its own
     * {@link com.foursoft.harness.vec.v2x.VecReusageSpecification}.
     */
    default SELF withTopologyReusage(final String templateDocumentNumber,
                                     final Customizer<TopologyReusageBuilder> customizer) {
        compositePartDefinition().withTopologyReusage(templateDocumentNumber, customizer);
        return self();
    }

    default SELF withPlacements(final Customizer<PlacementSpecificationBuilder> customizer) {
        compositePartDefinition().withPlacements(customizer);
        return self();
    }

    default SELF withRoutings(final Customizer<RoutingSpecificationBuilder> customizer) {
        compositePartDefinition().withRoutings(customizer);
        return self();
    }
}
