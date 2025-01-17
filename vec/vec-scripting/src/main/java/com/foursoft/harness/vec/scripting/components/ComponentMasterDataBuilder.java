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
        List<VecDocumentVersion> documents = new ArrayList<>();
        documents.add(partMasterDocument.build());
        documents.addAll(additionalDocuments);
        return new PartDocumentsPair(part, documents);
    }

    private DocumentVersionBuilder initializeDocument(final String documentNumber) {
        return new DocumentVersionBuilder(session, documentNumber, "1").documentType(DefaultValues.PART_MASTER)
                .addReferencedPart(
                        this.part);
    }

    public ComponentMasterDataBuilder withApplicationSpecification(final String documentNumber,
                                                                   final String documentVersion) {
        VecDocumentVersion dv = new DocumentVersionBuilder(session, documentNumber, documentVersion)
                .documentType("ApplicationSpecification")
                .build();

        session.addXmlComment(dv, " DocumentType to be verified (KBLFRM-1194).");

        dv.getReferencedPart().add(part);

        additionalDocuments.add(dv);

        return this;
    }

    private void addPartOrUsageRelatedSpecification(VecPartOrUsageRelatedSpecification specification,
                                                    boolean associatedToPart) {
        if (associatedToPart) {
            specification.getDescribedPart().add(part);
        }
        partMasterDocument.addSpecification(specification);
    }

    private <X extends VecPartOrUsageRelatedSpecification, T extends PartOrUsageRelatedSpecificationBuilder<X>> ComponentMasterDataBuilder addPartOrUsageRelatedSpecification(
            T builder, Customizer<T> customizer, boolean associatedToPart) {
        if (customizer != null) {
            customizer.customize(builder);
        }

        X spec = builder.build();

        this.addPartOrUsageRelatedSpecification(spec, associatedToPart);

        return this;
    }

    public ComponentMasterDataBuilder addGeneralTechnicalPart() {
        return this.addGeneralTechnicalPart(null);
    }

    public ComponentMasterDataBuilder addGeneralTechnicalPart(
            Customizer<GeneralTechnicalPartSpecificationBuilder> customizer) {
        GeneralTechnicalPartSpecificationBuilder builder = new GeneralTechnicalPartSpecificationBuilder(
                this.partNumber);

        return addPartOrUsageRelatedSpecification(builder, customizer, true);
    }

    public ComponentMasterDataBuilder addVirtualPartStructure(Customizer<VirtualPartStructureBuilder> customizer) {
        VirtualPartStructureBuilder builder = new VirtualPartStructureBuilder(session,
                                                                              partMasterDocument::getSpecificationWith,
                                                                              connectionID -> SchematicQueries.findConnection(
                                                                                      interalSchematic, connectionID));

        customizer.customize(builder);

        VirtualPartStructureBuilder.VirtualPartStructureResult build = builder.build();

        addPartOrUsageRelatedSpecification(build.bom(), true);

        partMasterDocument.addSpecification(build.usages());

        return this;
    }

    public ComponentMasterDataBuilder addSchematic(Customizer<SchematicBuilder> customizer) {
        SchematicBuilder builder = new SchematicBuilder();

        customizer.customize(builder);

        interalSchematic = builder.build();

        partMasterDocument.addSpecification(interalSchematic);

        return this;
    }

    public ComponentMasterDataBuilder addCoreSpecification(String identification,
                                                           Customizer<CoreSpecificationBuilder> customizer) {
        CoreSpecificationBuilder builder = new CoreSpecificationBuilder(this.session, identification);

        customizer.customize(builder);

        return addCoreSpecification(builder.build());
    }

    public ComponentMasterDataBuilder addCoreSpecification(VecCoreSpecification specification) {
        this.addSpecification(specification);

        return this;
    }

    private void addSpecification(VecSpecification specification) {
        partMasterDocument.addSpecification(specification);
    }

    public ComponentMasterDataBuilder addWireSpecification(Customizer<WireSpecificationBuilder> customizer) {
        WireSpecificationBuilder builder = new WireSpecificationBuilder(this.session, this.partNumber,
                                                                        this::addSpecification,
                                                                        this.partMasterDocument::getSpecificationWith);
        return addPartOrUsageRelatedSpecification(builder, customizer, true);
    }

    public ComponentMasterDataBuilder addWireSpecificationForPartUsage(String identification,
                                                                       Customizer<WireSpecificationBuilder> customizer) {
        WireSpecificationBuilder builder = new WireSpecificationBuilder(this.session, identification,
                                                                        this::addSpecification,
                                                                        this.partMasterDocument::getSpecificationWith);
        return addPartOrUsageRelatedSpecification(builder, customizer, false);
    }

    public ComponentMasterDataBuilder addEEComponentSpecification(
            Customizer<EEComponentSpecificationBuilder> customizer) {
        EEComponentSpecificationBuilder builder = new EEComponentSpecificationBuilder(
                this.partNumber, this::addSpecification);
        return addPartOrUsageRelatedSpecification(builder, customizer, true);
    }

    public ComponentMasterDataBuilder addSingleCore(Customizer<WireSingleCoreBuilder> customizer) {
        WireSingleCoreBuilder builder = new WireSingleCoreBuilder(this.session, this.partNumber,
                                                                  this::addSpecification,
                                                                  this.partMasterDocument::getSpecificationWith);
        return addPartOrUsageRelatedSpecification(builder, customizer, true);
    }

    public ComponentMasterDataBuilder addConnectorHousing(Customizer<ConnectorSpecificationBuilder> customizer) {
        ConnectorSpecificationBuilder builder = new ConnectorSpecificationBuilder(this.partNumber);

        return addPartOrUsageRelatedSpecification(builder, customizer, true);
    }

    public ComponentMasterDataBuilder addPlaceability(VecPlacementType placementType) {
        PlaceableElementSpecificationBuilder builder = new PlaceableElementSpecificationBuilder(this.partNumber,
                                                                                                placementType);
        return addPartOrUsageRelatedSpecification(builder, null, true);
    }

    public ComponentMasterDataBuilder addPluggableTerminal() {
        return this.addPluggableTerminal(null);
    }

    public ComponentMasterDataBuilder addPluggableTerminal(
            Customizer<PluggableTerminalSpecificationBuilder> customizer) {
        PluggableTerminalSpecificationBuilder builder =
                new PluggableTerminalSpecificationBuilder(this.session, this::addSpecification, this.partNumber);

        return addPartOrUsageRelatedSpecification(builder, customizer, true);

    }

    public record PartDocumentsPair(VecPartVersion partVersion, List<VecDocumentVersion> documentVersions) {
    }

}
