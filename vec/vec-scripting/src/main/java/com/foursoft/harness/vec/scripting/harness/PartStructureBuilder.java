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
package com.foursoft.harness.vec.scripting.harness;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.Customizer;
import com.foursoft.harness.vec.scripting.DefaultValues;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.components.PartOccurrenceBuilder;
import com.foursoft.harness.vec.scripting.components.PartUsageBuilder;
import com.foursoft.harness.vec.scripting.core.SpecificationLocator;
import com.foursoft.harness.vec.scripting.placement.PlacementSpecificationBuilder;
import com.foursoft.harness.vec.scripting.routing.RoutingSpecificationBuilder;
import com.foursoft.harness.vec.scripting.schematic.ConnectionSpecificationQueries;
import com.foursoft.harness.vec.scripting.topology.TopologyBuilder;
import com.foursoft.harness.vec.v2x.*;

import java.util.ArrayList;
import java.util.List;

import static com.foursoft.harness.vec.scripting.Queries.*;

/**
 * Builder for the bill of material of a composite part (e.g. an assembly) in its part master data. The contents are
 * defined as {@link VecPartOccurrence}s (contents with a part number of their own) or {@link VecPartUsage}s (contents
 * without a part number of their own) and are referenced by the {@link VecPartStructureSpecification}. Like a
 * harness, the composite part can define the contacting, topology, placement and routing of its contents.
 */
public class PartStructureBuilder implements Builder<PartStructureBuilder.PartStructureResult> {

    private final VecSession session;
    private final SpecificationLocator specificationLocator;
    private final ConnectionSpecificationQueries connectionSpecificationQueries = new ConnectionSpecificationQueries();

    private final VecPartStructureSpecification partStructureSpecification;
    private VecCompositionSpecification compositionSpecification;
    private VecPartUsageSpecification partUsageSpecification;
    private VecContactingSpecification contactingSpecification;
    private VecTopologySpecification topologySpecification;
    private VecPlacementSpecification placementSpecification;
    private VecRoutingSpecification routingSpecification;

    public PartStructureBuilder(final VecSession session, final String partNumber,
                                final SpecificationLocator specificationLocator) {
        this.session = session;
        this.specificationLocator = specificationLocator;
        this.partStructureSpecification = initializePartStructureSpecification(partNumber);
    }

    @Override
    public PartStructureResult build() {
        final List<VecSpecification> specifications = new ArrayList<>();
        addIfPresent(specifications, compositionSpecification);
        addIfPresent(specifications, partUsageSpecification);
        addIfPresent(specifications, contactingSpecification);
        addIfPresent(specifications, topologySpecification);
        addIfPresent(specifications, placementSpecification);
        addIfPresent(specifications, routingSpecification);
        return new PartStructureResult(partStructureSpecification, specifications);
    }

    public PartStructureBuilder withContent(final VecPartStructureContentTypeLiteral content) {
        partStructureSpecification.setContentLiteral(content);
        return this;
    }

    /**
     * Adds a content with a part number of its own to the bill of material.
     */
    public PartStructureBuilder addPartOccurrence(final String identification, final String partNumber,
                                                  final Customizer<PartOccurrenceBuilder> customizer) {
        final PartOccurrenceBuilder builder = new PartOccurrenceBuilder(session, identification, partNumber,
                                                                        connectionSpecificationQueries);
        if (customizer != null) {
            customizer.customize(builder);
        }

        final VecPartOccurrence result = builder.build();

        compositionSpecification().getComponents().add(result);
        partStructureSpecification.getInBillOfMaterial().add(result);

        return this;
    }

    public PartStructureBuilder addPartOccurrence(final String identification, final String partNumber) {
        return addPartOccurrence(identification, partNumber, null);
    }

    /**
     * Adds a content without a part number of its own to the bill of material. The specifications of the usage are
     * located in the part master document of the composite part.
     */
    public PartStructureBuilder addPartUsage(final String identification,
                                             final Customizer<PartUsageBuilder> customizer) {
        final PartUsageBuilder builder = new PartUsageBuilder(session, identification, specificationLocator,
                                                              connectionSpecificationQueries);
        customizer.customize(builder);

        final VecPartUsage result = builder.build();

        partUsageSpecification().getPartUsages().add(result);
        partStructureSpecification.getInBillOfMaterial().add(result);

        return this;
    }

    public PartStructureBuilder addConnection(final String wireElementReferenceId,
                                              final Customizer<ConnectivityBuilder> customizer) {
        final VecWireElementReference wireElementReference = wireElementReferenceLocator(
                compositionSpecification, partUsageSpecification).locate(wireElementReferenceId);

        final ConnectivityBuilder builder = new ConnectivityBuilder(session, wireElementReference,
                                                                    this::findOccurrenceOrUsage);

        customizer.customize(builder);

        contactingSpecification().getContactPoints().addAll(builder.build());

        return this;
    }

    public PartStructureBuilder withTopology(final Customizer<TopologyBuilder> customizer) {
        final TopologyBuilder builder = new TopologyBuilder(session, configConstraintLocator(null));

        customizer.customize(builder);

        topologySpecification = builder.build();

        return this;
    }

    public PartStructureBuilder withPlacements(final Customizer<PlacementSpecificationBuilder> customizer) {
        final PlacementSpecificationBuilder builder = new PlacementSpecificationBuilder(
                this::findOccurrenceOrUsage, nodeLocator(topologySpecification));

        customizer.customize(builder);

        placementSpecification = builder.build();

        return this;
    }

    public PartStructureBuilder withRoutings(final Customizer<RoutingSpecificationBuilder> customizer) {
        final RoutingSpecificationBuilder builder = new RoutingSpecificationBuilder(
                wireElementReferenceLocator(compositionSpecification, partUsageSpecification),
                segmentLocator(topologySpecification), configConstraintLocator(null));

        customizer.customize(builder);

        routingSpecification = builder.build();

        return this;
    }

    private VecOccurrenceOrUsage findOccurrenceOrUsage(final String identification) {
        return occurrenceOrUsageLocator(compositionSpecification, partUsageSpecification).locate(identification);
    }

    private VecCompositionSpecification compositionSpecification() {
        if (compositionSpecification == null) {
            compositionSpecification = new VecCompositionSpecification();
            compositionSpecification.setIdentification(DefaultValues.COMP_COMPOSITION_SPEC_IDENTIFICATION);
        }
        return compositionSpecification;
    }

    private VecPartUsageSpecification partUsageSpecification() {
        if (partUsageSpecification == null) {
            partUsageSpecification = new VecPartUsageSpecification();
            partUsageSpecification.setIdentification(DefaultValues.COMP_USAGE_SPEC_IDENTIFICATION);
        }
        return partUsageSpecification;
    }

    private VecContactingSpecification contactingSpecification() {
        if (contactingSpecification == null) {
            contactingSpecification = new VecContactingSpecification();
            contactingSpecification.setIdentification(DefaultValues.CONTACTING_SPEC_IDENTIFICATION);
        }
        return contactingSpecification;
    }

    private static VecPartStructureSpecification initializePartStructureSpecification(final String partNumber) {
        final VecPartStructureSpecification result = new VecPartStructureSpecification();
        result.setIdentification("STRUCTURE-" + partNumber);
        return result;
    }

    private static void addIfPresent(final List<VecSpecification> specifications,
                                     final VecSpecification specification) {
        if (specification != null) {
            specifications.add(specification);
        }
    }

    public record PartStructureResult(VecPartStructureSpecification bom, List<VecSpecification> specifications) {
    }
}
