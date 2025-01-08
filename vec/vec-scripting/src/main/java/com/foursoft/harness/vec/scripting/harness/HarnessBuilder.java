/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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
import com.foursoft.harness.vec.scripting.core.DocumentVersionBuilder;
import com.foursoft.harness.vec.scripting.placement.PlacementSpecificationBuilder;
import com.foursoft.harness.vec.scripting.routing.RoutingSpecificationBuilder;
import com.foursoft.harness.vec.scripting.schematic.SchematicQueries;
import com.foursoft.harness.vec.scripting.topology.TopologyBuilder;
import com.foursoft.harness.vec.scripting.variants.ConfigManagementBuilder;
import com.foursoft.harness.vec.v2x.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.foursoft.harness.vec.scripting.Queries.*;

public class HarnessBuilder implements Builder<HarnessBuilder.HarnessResult> {

    private final VecSession session;
    private final String documentNumber;

    private final DocumentVersionBuilder harnessDocumentBuilder;
    private final VecCompositionSpecification compositionSpecification;
    private VecCompositionSpecification modulesCompositionSpecification;

    private VecVariantConfigurationSpecification variantConfigurationSpecification;

    private VecContactingSpecification contactingSpecification;
    private VecConnectionSpecification schematic;

    private final List<VecPartVersion> createdParts = new ArrayList<>();
    private VecTopologySpecification topologySpecification;
    private VecConfigurationConstraintSpecification configurationConstraintSpecification;

    public HarnessBuilder(final VecSession session, final String documentNumber, String version) {
        this.session = session;
        this.documentNumber = documentNumber;
        this.harnessDocumentBuilder = initializeDocument(documentNumber, version);
        this.compositionSpecification = initializeCompositionSpecification();
    }

    @Override
    public HarnessResult build() {
        if (contactingSpecification != null) {
            harnessDocumentBuilder.addSpecification(contactingSpecification);
        }

        this.harnessDocumentBuilder.addSpecification(compositionSpecification);

        if (modulesCompositionSpecification != null) {
            harnessDocumentBuilder.addSpecification(modulesCompositionSpecification);
        }

        if (variantConfigurationSpecification != null) {
            harnessDocumentBuilder.addSpecification(variantConfigurationSpecification);
        }

        return new HarnessResult(createdParts, harnessDocumentBuilder.build());
    }

    private VecContactingSpecification contactingSpecification() {
        if (contactingSpecification == null) {
            contactingSpecification = new VecContactingSpecification();
            contactingSpecification.setIdentification(this.documentNumber);

        }
        return contactingSpecification;
    }

    private DocumentVersionBuilder initializeDocument(final String documentNumber, final String version) {
        return new DocumentVersionBuilder(session, documentNumber, version).documentType(
                DefaultValues.HARNESS_DESCRIPTION);
    }

    private VecCompositionSpecification initializeCompositionSpecification() {
        VecCompositionSpecification compositionSpecification = new VecCompositionSpecification();
        compositionSpecification.setIdentification(DefaultValues.COMP_COMPOSITION_SPEC_IDENTIFICATION);
        return compositionSpecification;
    }

    private void ensureModulesCompositionSpecification() {
        if (modulesCompositionSpecification == null) {
            modulesCompositionSpecification = new VecCompositionSpecification();
            modulesCompositionSpecification.setIdentification(DefaultValues.MODULES_COMPOSITION_SPEC_IDENTIFICATION);
        }
    }

    private void ensureVariantConfigurationSpecification() {
        if (variantConfigurationSpecification == null) {
            variantConfigurationSpecification = new VecVariantConfigurationSpecification();
            variantConfigurationSpecification.setIdentification(
                    DefaultValues.VARIANT_CONFIGURATION_SPEC_IDENTIFICATION);
        }
    }

    VecPartOccurrence findOccurrence(final String identification) {
        return this.compositionSpecification.getComponents()
                .stream()
                .filter(o -> o.getIdentification()
                        .equals(identification))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("The occurrence '%s' can not be found.", identification)));
    }

    List<VecPartOccurrence> findOccurrences(final String[] occurrenceIds) {
        return Arrays.stream(occurrenceIds).map(this::findOccurrence).toList();
    }

    public HarnessBuilder addConnection(String wireElementReferenceId, Customizer<ConnectivityBuilder> customizer) {
        final VecWireElementReference wireElementReference = wireElementReferenceLocator(
                compositionSpecification).locate(wireElementReferenceId);

        ConnectivityBuilder builder = new ConnectivityBuilder(wireElementReference, this::findOccurrence);

        customizer.customize(builder);

        List<VecContactPoint> result = builder.build();

        contactingSpecification().getContactPoints().addAll(result);

        return this;
    }

    public HarnessBuilder addPartOccurrence(
            final String identification, final String partNumber) {
        return this.addPartOccurrence(identification, partNumber, null);
    }

    public HarnessBuilder addPartOccurrence(
            final String identification, final String partNumber, Customizer<PartOccurrenceBuilder> customizer) {

        PartOccurrenceBuilder builder = new PartOccurrenceBuilder(this.session, identification,
                                                                  partNumber,
                                                                  nodeId -> SchematicQueries.findNode(
                                                                          schematic, nodeId),
                                                                  connectionID -> SchematicQueries.findConnection(
                                                                          schematic, connectionID));

        if (customizer != null) {
            customizer.customize(builder);
        }

        VecPartOccurrence result = builder.build();

        this.compositionSpecification.getComponents().add(result);

        return this;
    }

    public HarnessBuilder addVariant(final String partNumber, Customizer<VariantBuilder> customizer) {
        VariantBuilder builder = new VariantBuilder(this.session, partNumber, this::findOccurrences);

        customizer.customize(builder);

        VariantBuilder.VariantResult result = builder.build();

        ensureModulesCompositionSpecification();

        harnessDocumentBuilder.addSpecification(result.bom());
        modulesCompositionSpecification.getComponents().add(result.variantOccurrence());
        createdParts.add(result.variantPart());

        if (result.variantConfiguration() != null) {
            ensureVariantConfigurationSpecification();
            variantConfigurationSpecification.getVariantConfigurations().add(result.variantConfiguration());
        }

        return this;
    }

    public HarnessBuilder withSchematic(String schematicDocumentNumber) {
        this.schematic = session.findDocument(schematicDocumentNumber).getSpecificationWith(
                VecConnectionSpecification.class, DefaultValues.CONNECTION_SPEC_IDENTIFICATION).orElseThrow();
        return this;
    }

    public HarnessBuilder withTopology(Customizer<TopologyBuilder> customizer) {
        TopologyBuilder builder = new TopologyBuilder(configConstraintLocator(configurationConstraintSpecification));

        customizer.customize(builder);

        topologySpecification = builder.build();

        harnessDocumentBuilder.addSpecification(topologySpecification);

        return this;
    }

    public HarnessBuilder withPlacements(Customizer<PlacementSpecificationBuilder> customizer) {
        PlacementSpecificationBuilder builder = new PlacementSpecificationBuilder(
                partOccurrenceLocator(this.compositionSpecification),
                nodeLocator(this.topologySpecification));
        customizer.customize(builder);

        VecPlacementSpecification placementSpecification = builder.build();

        harnessDocumentBuilder.addSpecification(placementSpecification);

        return this;
    }

    public HarnessBuilder withRoutings(Customizer<RoutingSpecificationBuilder> customizer) {
        RoutingSpecificationBuilder builder = new RoutingSpecificationBuilder(
                wireElementReferenceLocator(compositionSpecification), segmentLocator(topologySpecification),
                configConstraintLocator(configurationConstraintSpecification));

        customizer.customize(builder);

        final VecRoutingSpecification routingSpecification = builder.build();

        harnessDocumentBuilder.addSpecification(routingSpecification);

        return this;
    }

    public HarnessBuilder withConfigManagement(Customizer<ConfigManagementBuilder> customizer) {
        ConfigManagementBuilder builder = new ConfigManagementBuilder();

        customizer.customize(builder);

        builder.build().forEach(s -> {
            if (s instanceof VecConfigurationConstraintSpecification constraintSpecification) {
                this.configurationConstraintSpecification = constraintSpecification;
            }
            harnessDocumentBuilder.addSpecification(s);
        });

        return this;

    }

    public record HarnessResult(List<VecPartVersion> variants, VecDocumentVersion harnessDocument) {
    }

}
