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
package com.foursoft.harness.vec.scripting.components;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.Customizer;
import com.foursoft.harness.vec.scripting.DefaultValues;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.core.DocumentVersionBuilder;
import com.foursoft.harness.vec.scripting.core.PartOrUsageRelatedSpecificationBuilder;
import com.foursoft.harness.vec.scripting.core.PartVersionBuilder;
import com.foursoft.harness.vec.scripting.eecomponents.EEComponentSpecificationBuilder;
import com.foursoft.harness.vec.scripting.enums.DocumentType;
import com.foursoft.harness.vec.scripting.harness.VirtualPartStructureBuilder;
import com.foursoft.harness.vec.scripting.schematic.SchematicBuilder;
import com.foursoft.harness.vec.scripting.schematic.SchematicQueries;
import com.foursoft.harness.vec.v2x.*;

import java.util.ArrayList;
import java.util.List;

public class ComponentMasterDataBuilder implements Builder<ComponentMasterDataBuilder.PartDocumentsPair> {

    private final VecSession session;
    private final String partNumber;
    private final DocumentVersionBuilder partMasterDocument;

    private final List<VecDocumentVersion> additionalDocuments = new ArrayList<>();
    private final VecPartVersion part;
    private VecConnectionSpecification interalSchematic;

    public ComponentMasterDataBuilder(final VecSession session,
                                      final String partNumber, final String documentNumber,
                                      final VecPrimaryPartType primaryPartType) {
        this.session = session;
        this.partNumber = partNumber;
        this.part = new PartVersionBuilder(session, partNumber, primaryPartType).build();
        this.partMasterDocument = initializeDocument(documentNumber);
    }

    @Override
    public PartDocumentsPair build() {
        final List<VecDocumentVersion> documents = new ArrayList<>();
        documents.add(partMasterDocument.build());
        documents.addAll(additionalDocuments);
        return new PartDocumentsPair(part, documents);
    }

    private DocumentVersionBuilder initializeDocument(final String documentNumber) {
        return new DocumentVersionBuilder(session, documentNumber, "1").documentType(DocumentType.PART_MASTER)
                .addReferencedPart(
                        this.part);
    }

    public ComponentMasterDataBuilder withPartAbbreviation(final VecLocalizedString abbreviation) {
        this.part.getAbbreviations().add(abbreviation);
        return this;
    }

    public ComponentMasterDataBuilder withPartDescription(final VecAbstractLocalizedString description) {
        this.part.getDescriptions().add(description);
        return this;
    }

    public ComponentMasterDataBuilder withApplicationSpecification(final String documentNumber,
                                                                   final String documentVersion) {
        final VecDocumentVersion dv = new DocumentVersionBuilder(session, documentNumber, documentVersion)
                .documentType(DocumentType.PROCESSING_INSTRUCTION)
                .build();

        dv.getReferencedPart().add(part);

        additionalDocuments.add(dv);

        return this;
    }

    private void addPartOrUsageRelatedSpecification(final VecPartOrUsageRelatedSpecification specification,
                                                    final boolean associatedToPart) {
        if (associatedToPart) {
            specification.getDescribedPart().add(part);
        }
        partMasterDocument.addSpecification(specification);
    }

    private <X extends VecPartOrUsageRelatedSpecification, T extends PartOrUsageRelatedSpecificationBuilder<X>> ComponentMasterDataBuilder addPartOrUsageRelatedSpecification(
            final T builder, final Customizer<T> customizer, final boolean associatedToPart) {
        if (customizer != null) {
            customizer.customize(builder);
        }

        final X spec = builder.build();

        this.addPartOrUsageRelatedSpecification(spec, associatedToPart);

        return this;
    }

    public ComponentMasterDataBuilder addGeneralTechnicalPart() {
        return this.addGeneralTechnicalPart(null);
    }

    public ComponentMasterDataBuilder addGeneralTechnicalPart(
            final Customizer<GeneralTechnicalPartSpecificationBuilder> customizer) {
        final GeneralTechnicalPartSpecificationBuilder builder = new GeneralTechnicalPartSpecificationBuilder(session,
                                                                                                              this.partNumber);

        return addPartOrUsageRelatedSpecification(builder, customizer, true);
    }

    public ComponentMasterDataBuilder addRequirementsConformance(
            final Customizer<RequirementsConformanceBuilder> customizer) {
        final RequirementsConformanceBuilder builder = new RequirementsConformanceBuilder(
                session, this.partNumber);

        return addPartOrUsageRelatedSpecification(builder, customizer, true);
    }

    public ComponentMasterDataBuilder addVirtualPartStructure(
            final Customizer<VirtualPartStructureBuilder> customizer) {
        final VirtualPartStructureBuilder builder = new VirtualPartStructureBuilder(session,
                                                                                    partMasterDocument::getSpecificationWith,
                                                                                    connectionID -> SchematicQueries.findConnection(
                                                                                            interalSchematic,
                                                                                            connectionID));

        customizer.customize(builder);

        final VirtualPartStructureBuilder.VirtualPartStructureResult build = builder.build();

        addPartOrUsageRelatedSpecification(build.bom(), true);

        partMasterDocument.addSpecification(build.usages());

        return this;
    }

    public ComponentMasterDataBuilder addSchematic(final Customizer<SchematicBuilder> customizer) {
        final SchematicBuilder builder = new SchematicBuilder();

        customizer.customize(builder);

        interalSchematic = builder.build();

        partMasterDocument.addSpecification(interalSchematic);

        return this;
    }

    public ComponentMasterDataBuilder addConductorSpecification(final String identification,
                                                                final Customizer<CoreSpecificationBuilder> customizer) {
        final CoreSpecificationBuilder builder = new CoreSpecificationBuilder(this.session, identification);

        customizer.customize(builder);

        return addConductorSpecification(builder.build());
    }

    public ComponentMasterDataBuilder addShieldSpecification(final String identification,
                                                             final Customizer<ShieldSpecificationBuilder> customizer) {
        final ShieldSpecificationBuilder builder = new ShieldSpecificationBuilder(this.session, identification);

        customizer.customize(builder);

        return addConductorSpecification(builder.build());
    }

    public ComponentMasterDataBuilder addInsulationSpecification(final String identification,
                                                                 final Customizer<InsulationSpecificationBuilder> customizer) {
        final InsulationSpecificationBuilder builder = new InsulationSpecificationBuilder(this.session, identification);

        customizer.customize(builder);

        addSpecification(builder.build());

        return this;
    }

    private ComponentMasterDataBuilder addConductorSpecification(final VecConductorSpecification specification) {
        this.addSpecification(specification);

        return this;
    }

    private void addSpecification(final VecSpecification specification) {
        partMasterDocument.addSpecification(specification);
    }

    public ComponentMasterDataBuilder addWireSpecification(final Customizer<WireSpecificationBuilder> customizer) {
        final WireSpecificationBuilder builder = new WireSpecificationBuilder(this.session, this.partNumber,
                                                                              this::addSpecification,
                                                                              this.partMasterDocument::getSpecificationWith);
        return addPartOrUsageRelatedSpecification(builder, customizer, true);
    }

    public ComponentMasterDataBuilder addWireSpecificationForPartUsage(final String identification,
                                                                       final Customizer<WireSpecificationBuilder> customizer) {
        final WireSpecificationBuilder builder = new WireSpecificationBuilder(this.session, identification,
                                                                              this::addSpecification,
                                                                              this.partMasterDocument::getSpecificationWith);
        return addPartOrUsageRelatedSpecification(builder, customizer, false);
    }

    public ComponentMasterDataBuilder addEEComponentSpecification(
            final Customizer<EEComponentSpecificationBuilder> customizer) {
        final EEComponentSpecificationBuilder builder = new EEComponentSpecificationBuilder(
                this.partNumber, this::addSpecification);
        return addPartOrUsageRelatedSpecification(builder, customizer, true);
    }

    public ComponentMasterDataBuilder addSingleCore(final Customizer<WireSingleCoreBuilder> customizer) {
        final WireSingleCoreBuilder builder = new WireSingleCoreBuilder(this.session, this.partNumber,
                                                                        this::addSpecification,
                                                                        this.partMasterDocument::getSpecificationWith);
        return addPartOrUsageRelatedSpecification(builder, customizer, true);
    }

    public ComponentMasterDataBuilder addConnectorHousing(final Customizer<ConnectorSpecificationBuilder> customizer) {
        final ConnectorSpecificationBuilder builder = new ConnectorSpecificationBuilder(this.partNumber);

        return addPartOrUsageRelatedSpecification(builder, customizer, true);
    }

    public ComponentMasterDataBuilder addPlaceability(final VecPlacementType placementType) {
        final PlaceableElementSpecificationBuilder builder = new PlaceableElementSpecificationBuilder(this.partNumber,
                                                                                                      placementType);
        return addPartOrUsageRelatedSpecification(builder, null, true);
    }

    public ComponentMasterDataBuilder addPluggableTerminal() {
        return this.addPluggableTerminal(null);
    }

    public ComponentMasterDataBuilder addPluggableTerminal(
            final Customizer<PluggableTerminalSpecificationBuilder> customizer) {
        final PluggableTerminalSpecificationBuilder builder =
                new PluggableTerminalSpecificationBuilder(this.session, this::addSpecification, this.partNumber,
                                                          this.partMasterDocument::getSpecificationWith);

        return addPartOrUsageRelatedSpecification(builder, customizer, true);

    }

    public ComponentMasterDataBuilder addTerminalPairing(final String otherSidePartNumber,
                                                         final Customizer<TerminalPairingBuilder> customizer) {
        final TerminalPairingBuilder terminalPairingBuilder = new TerminalPairingBuilder(this.session, this.part,
                                                                                         this.session.findPartVersionByPartNumber(
                                                                                                 otherSidePartNumber));
        customizer.customize(terminalPairingBuilder);

        final VecTerminalPairingSpecification build = terminalPairingBuilder.build();
        this.addSpecification(build);
        return this;
    }

    public record PartDocumentsPair(VecPartVersion partVersion, List<VecDocumentVersion> documentVersions) {
    }

}
