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

import com.foursoft.harness.vec.v2x.VecDocumentType;
import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.Customizer;
import com.foursoft.harness.vec.scripting.DefaultValues;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.composite.CompositePartDefinition;
import com.foursoft.harness.vec.scripting.composite.CompositePartDefinitionContext;
import com.foursoft.harness.vec.scripting.core.DocumentVersionBuilder;
import com.foursoft.harness.vec.scripting.schematic.ConnectionSpecificationQueries;
import com.foursoft.harness.vec.scripting.topology.TopologyZonesBuilder;
import com.foursoft.harness.vec.scripting.variants.ConfigManagementBuilder;
import com.foursoft.harness.vec.v2x.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.foursoft.harness.vec.scripting.Queries.configConstraintLocator;

public class HarnessBuilder implements Builder<HarnessBuilder.HarnessResult>, CompositePartDefinition<HarnessBuilder> {

    private final VecSession session;

    private final DocumentVersionBuilder harnessDocumentBuilder;
    private final CompositePartDefinitionContext compositePartDefinition;
    private VecCompositionSpecification modulesCompositionSpecification;

    private VecVariantConfigurationSpecification variantConfigurationSpecification;

    private VecConnectionSpecification schematic;
    private ConnectionSpecificationQueries schematicQuery = new ConnectionSpecificationQueries();

    private final List<VecPartVersion> createdParts = new ArrayList<>();
    private VecConfigurationConstraintSpecification configurationConstraintSpecification;
    private VecTopologyZoneSpecification topologyZonesSpecification;

    public HarnessBuilder(final VecSession session, final String documentNumber, final String version) {
        this.session = session;
        this.harnessDocumentBuilder = initializeDocument(documentNumber, version);
        this.compositePartDefinition = new CompositePartDefinitionContext(
                session, harnessDocumentBuilder, harnessDocumentBuilder::getSpecificationWith, () -> schematicQuery,
                id -> configConstraintLocator(configurationConstraintSpecification).locate(id), documentNumber);
        // A harness description always contains the container of its components.
        this.compositePartDefinition.compositionSpecification();
    }

    @Override
    public CompositePartDefinitionContext compositePartDefinition() {
        return compositePartDefinition;
    }

    @Override
    public HarnessBuilder self() {
        return this;
    }

    @Override
    public HarnessResult build() {
        compositePartDefinition.finish();

        if (modulesCompositionSpecification != null) {
            harnessDocumentBuilder.addSpecification(modulesCompositionSpecification);
        }

        if (variantConfigurationSpecification != null) {
            harnessDocumentBuilder.addSpecification(variantConfigurationSpecification);
        }

        return new HarnessResult(createdParts, harnessDocumentBuilder.build());
    }

    private DocumentVersionBuilder initializeDocument(final String documentNumber, final String version) {
        return new DocumentVersionBuilder(session, documentNumber, version).documentType(
                VecDocumentType.HARNESS_DESCRIPTION);
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

    List<VecPartOccurrence> findOccurrences(final String[] occurrenceIds) {
        return Arrays.stream(occurrenceIds).map(compositePartDefinition::findOccurrence).toList();
    }

    public HarnessBuilder addVariant(final String partNumber, final Customizer<VariantBuilder> customizer) {
        final VariantBuilder builder = new VariantBuilder(this.session, partNumber, this::findOccurrences);

        customizer.customize(builder);

        final VariantBuilder.VariantResult result = builder.build();

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

    public HarnessBuilder withSchematic(final String schematicDocumentNumber) {
        this.schematic = session.findDocument(schematicDocumentNumber).getSpecificationWith(
                VecConnectionSpecification.class, DefaultValues.CONNECTION_SPEC_IDENTIFICATION).orElseThrow();
        this.schematicQuery = new ConnectionSpecificationQueries(this.schematic);
        return this;
    }

    public HarnessBuilder withTopologyZones(final Customizer<TopologyZonesBuilder> customizer) {
        final TopologyZonesBuilder builder = new TopologyZonesBuilder(this.session,
                                                                      compositePartDefinition.topologySpecification());

        customizer.customize(builder);

        topologyZonesSpecification = builder.build();

        harnessDocumentBuilder.addSpecification(topologyZonesSpecification);

        return this;
    }

    public HarnessBuilder withConfigManagement(final Customizer<ConfigManagementBuilder> customizer) {
        final ConfigManagementBuilder builder = new ConfigManagementBuilder();

        customizer.customize(builder);

        builder.build().forEach(s -> {
            if (s instanceof final VecConfigurationConstraintSpecification constraintSpecification) {
                this.configurationConstraintSpecification = constraintSpecification;
            }
            harnessDocumentBuilder.addSpecification(s);
        });

        return this;

    }

    public record HarnessResult(List<VecPartVersion> variants, VecDocumentVersion harnessDocument) {
    }

}
