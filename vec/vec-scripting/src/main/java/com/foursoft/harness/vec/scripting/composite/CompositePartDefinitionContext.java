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
import com.foursoft.harness.vec.scripting.DefaultValues;
import com.foursoft.harness.vec.scripting.Locator;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.components.PartOccurrenceBuilder;
import com.foursoft.harness.vec.scripting.components.PartUsageBuilder;
import com.foursoft.harness.vec.scripting.core.DocumentVersionBuilder;
import com.foursoft.harness.vec.scripting.core.SpecificationLocator;
import com.foursoft.harness.vec.scripting.harness.ConnectivityBuilder;
import com.foursoft.harness.vec.scripting.placement.PlacementSpecificationBuilder;
import com.foursoft.harness.vec.scripting.routing.RoutingSpecificationBuilder;
import com.foursoft.harness.vec.scripting.schematic.ConnectionSpecificationQueries;
import com.foursoft.harness.vec.scripting.topology.TopologyBuilder;
import com.foursoft.harness.vec.scripting.topology.TopologyReusageBuilder;
import com.foursoft.harness.vec.scripting.topology.TopologySpecificationQueries;
import com.foursoft.harness.vec.v2x.*;

import java.util.function.Supplier;

import static com.foursoft.harness.vec.scripting.Queries.*;

/**
 * Implementation of the {@link CompositePartDefinition} aspects for one {@link VecDocumentVersion}: the instances of
 * the components a composite part (e.g. a harness or an assembly) is defined with, and the aspects that relate them
 * (contacting, topology, placement, routing).
 * <p>
 * The {@link VecTopologySpecification}, {@link VecPlacementSpecification}, {@link VecRoutingSpecification} and
 * {@link VecReusageSpecification} are added to the document when they are defined. The containers of the instances
 * and the {@link VecContactingSpecification} are added by {@link #finish()}.
 */
public class CompositePartDefinitionContext {

    private final VecSession session;
    private final DocumentVersionBuilder document;
    private final SpecificationLocator specificationLocator;
    private final Supplier<ConnectionSpecificationQueries> schematicQuery;
    private final Locator<VecConfigurationConstraint> configurationConstraintLocator;
    private final String contactingSpecificationIdentification;

    private VecCompositionSpecification compositionSpecification;
    private VecPartUsageSpecification partUsageSpecification;
    private VecContactingSpecification contactingSpecification;
    private VecTopologySpecification topologySpecification;

    /**
     * @param document                              the document the specifications are added to.
     * @param specificationLocator                  locates the specifications of {@link VecPartUsage}s, if no
     *                                              other document is given.
     * @param schematicQuery                        the schematic the instances are related to.
     * @param configurationConstraintLocator        locates the configuration constraints of topology segments and
     *                                              routings.
     * @param contactingSpecificationIdentification the identification of the {@link VecContactingSpecification}.
     */
    public CompositePartDefinitionContext(final VecSession session, final DocumentVersionBuilder document,
                                          final SpecificationLocator specificationLocator,
                                          final Supplier<ConnectionSpecificationQueries> schematicQuery,
                                          final Locator<VecConfigurationConstraint> configurationConstraintLocator,
                                          final String contactingSpecificationIdentification) {
        this.session = session;
        this.document = document;
        this.specificationLocator = specificationLocator;
        this.schematicQuery = schematicQuery;
        this.configurationConstraintLocator = configurationConstraintLocator;
        this.contactingSpecificationIdentification = contactingSpecificationIdentification;
    }

    /**
     * Adds the containers of the instances and the {@link VecContactingSpecification} to the document, if they have
     * been defined.
     */
    public void finish() {
        addIfPresent(contactingSpecification);
        addIfPresent(compositionSpecification);
        addIfPresent(partUsageSpecification);
    }

    public void addPartOccurrence(final String identification, final String partNumber,
                                  final Customizer<PartOccurrenceBuilder> customizer) {
        final PartOccurrenceBuilder builder = new PartOccurrenceBuilder(session, identification, partNumber,
                                                                        schematicQuery.get(),
                                                                        this::findOccurrenceOrUsage);
        if (customizer != null) {
            customizer.customize(builder);
        }

        compositionSpecification().getComponents().add(builder.build());
    }

    public void addPartUsage(final String identification, final Customizer<PartUsageBuilder> customizer) {
        addPartUsage(identification, specificationLocator, customizer);
    }

    public void addPartUsage(final String identification, final String specificationsDocumentNumber,
                             final Customizer<PartUsageBuilder> customizer) {
        addPartUsage(identification, session.findDocument(specificationsDocumentNumber)::getSpecificationWith,
                     customizer);
    }

    private void addPartUsage(final String identification, final SpecificationLocator locator,
                              final Customizer<PartUsageBuilder> customizer) {
        final PartUsageBuilder builder = new PartUsageBuilder(session, identification, locator,
                                                              schematicQuery.get());

        customizer.customize(builder);

        partUsageSpecification().getPartUsages().add(builder.build());
    }

    public void addConnection(final String wireElementReferenceId, final Customizer<ConnectivityBuilder> customizer) {
        final VecWireElementReference wireElementReference = wireElementReferenceLocator(
                compositionSpecification, partUsageSpecification).locate(wireElementReferenceId);

        final ConnectivityBuilder builder = new ConnectivityBuilder(session, wireElementReference,
                                                                    this::findOccurrenceOrUsage);

        customizer.customize(builder);

        contactingSpecification().getContactPoints().addAll(builder.build());
    }

    public void withTopology(final Customizer<TopologyBuilder> customizer) {
        final TopologyBuilder builder = new TopologyBuilder(session, configurationConstraintLocator);

        customizer.customize(builder);

        topologySpecification = builder.build();

        document.addSpecification(topologySpecification);
    }

    public void withTopologyReusage(final String templateDocumentNumber,
                                    final Customizer<TopologyReusageBuilder> customizer) {
        if (topologySpecification == null) {
            withTopology(topology -> {
            });
        }

        final TopologyReusageBuilder builder = new TopologyReusageBuilder(
                templateDocumentNumber, TopologySpecificationQueries.forDocument(session, templateDocumentNumber),
                topologySpecification);

        customizer.customize(builder);

        document.addSpecification(builder.build());
    }

    public void withPlacements(final Customizer<PlacementSpecificationBuilder> customizer) {
        final PlacementSpecificationBuilder builder = new PlacementSpecificationBuilder(
                this::findOccurrenceOrUsage, nodeLocator(topologySpecification));

        customizer.customize(builder);

        document.addSpecification(builder.build());
    }

    public void withRoutings(final Customizer<RoutingSpecificationBuilder> customizer) {
        final RoutingSpecificationBuilder builder = new RoutingSpecificationBuilder(
                wireElementReferenceLocator(compositionSpecification, partUsageSpecification),
                segmentLocator(topologySpecification), configurationConstraintLocator);

        customizer.customize(builder);

        document.addSpecification(builder.build());
    }

    public VecOccurrenceOrUsage findOccurrenceOrUsage(final String identification) {
        return occurrenceOrUsageLocator(compositionSpecification, partUsageSpecification).locate(identification);
    }

    public VecPartOccurrence findOccurrence(final String identification) {
        return compositionSpecification().getComponents()
                .stream()
                .filter(o -> o.getIdentification()
                        .equals(identification))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("The occurrence '%s' can not be found.", identification)));
    }

    public VecTopologySpecification topologySpecification() {
        return topologySpecification;
    }

    public VecCompositionSpecification compositionSpecification() {
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
            contactingSpecification.setIdentification(contactingSpecificationIdentification);
        }
        return contactingSpecification;
    }

    private void addIfPresent(final VecSpecification specification) {
        if (specification != null) {
            document.addSpecification(specification);
        }
    }
}
