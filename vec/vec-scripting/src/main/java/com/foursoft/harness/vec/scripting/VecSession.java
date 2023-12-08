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
package com.foursoft.harness.vec.scripting;

import com.foursoft.harness.navext.runtime.io.utils.ValidationEventLogger;
import com.foursoft.harness.navext.runtime.io.write.XMLWriter;
import com.foursoft.harness.navext.runtime.io.write.xmlmeta.XMLMeta;
import com.foursoft.harness.navext.runtime.io.write.xmlmeta.comments.Comments;
import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.scripting.core.DocumentVersionBuilder;
import com.foursoft.harness.vec.scripting.factories.SiUnitFactory;
import com.foursoft.harness.vec.scripting.factories.VecContentFactory;
import com.foursoft.harness.vec.scripting.schematic.SchematicBuilder;
import com.foursoft.harness.vec.scripting.utils.XmlIdGeneratingTraverser;
import com.foursoft.harness.vec.scripting.utils.XmlIdGenerator;
import com.foursoft.harness.vec.v2x.*;
import jakarta.xml.bind.Marshaller;

import java.io.OutputStream;

//TODO: Provision of Units & Provision of comments should be extracted to interfaces. (e.g. GlobalUnitProvider)

public class VecSession {

    private final XMLMeta xmlMeta = new XMLMeta();
    private final Comments comments = new Comments();

    private final XmlIdGenerator xmlIdGenerator = new XmlIdGenerator();

    private final VecContent vecContentRoot = VecContentFactory.create();

    private final DefaultValues defaultValues = new DefaultValues();

    private VecSIUnit squareMM;
    private VecSIUnit mm;
    private VecCompositeUnit gramPerMeter;
    private VecOtherUnit piece;
    private VecSIUnit newton;

    public VecSession() {
        xmlMeta.setComments(comments);
    }

    public DefaultValues getDefaultValues() {
        return defaultValues;
    }

    public VecContent getVecContentRoot() {
        return vecContentRoot;
    }

    public void document(final String documentNumber, String version, Customizer<DocumentVersionBuilder> customizer) {
        DocumentVersionBuilder builder = new DocumentVersionBuilder(this, documentNumber, version);

        customizer.customize(builder);

        VecDocumentVersion build = builder.build();

        vecContentRoot.getDocumentVersions().add(build);
    }

    public void component(final String partNumber, final String documentNumber,
                          final VecPrimaryPartType primaryPartType, Customizer<ComponentMasterDataBuilder> customizer) {
        ComponentMasterDataBuilder builder = new ComponentMasterDataBuilder(this, partNumber, documentNumber,
                                                                            primaryPartType);
        customizer.customize(builder);

        ComponentMasterDataBuilder.PartDocumentsPair result = builder.build();

        vecContentRoot.getDocumentVersions().addAll(result.documentVersions());
        vecContentRoot.getPartVersions().add(result.partVersion());
    }

    public void schematic(String containerDocumentNumber, Customizer<SchematicBuilder> customizer) {
        VecDocumentVersion containerDocument = findDocument(containerDocumentNumber);

        SchematicBuilder builder = new SchematicBuilder();

        customizer.customize(builder);

        VecConnectionSpecification result = builder.build();

        containerDocument.getSpecifications().add(result);
    }

    public void harness(final String documentNumber, String version, Customizer<HarnessBuilder> customizer) {
        HarnessBuilder harnessBuilder = new HarnessBuilder(this, documentNumber, version);

        customizer.customize(harnessBuilder);

        HarnessBuilder.HarnessResult result = harnessBuilder.build();

        this.vecContentRoot.getDocumentVersions().add(result.harnessDocument());
        this.vecContentRoot.getPartVersions().addAll(result.variants());
    }

    public void addXmlComment(Identifiable identifiable, String comment) {
        comments.put(identifiable, comment);
    }

    public String writeToString() {
        ensureXmlIds();
        final XMLWriter<VecContent> writer = createWriter();

        return writer.writeToString(vecContentRoot, xmlMeta);
    }

    public void writeToStream(OutputStream stream) {
        ensureXmlIds();
        final XMLWriter<VecContent> writer = createWriter();

        writer.write(vecContentRoot, xmlMeta, stream);
    }

    private static XMLWriter<VecContent> createWriter() {
        return new XMLWriter<>(VecContent.class, new ValidationEventLogger()) {

            @Override
            protected void configureMarshaller(final Marshaller marshaller) throws Exception {
                super.configureMarshaller(marshaller);
                marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "");
            }
        };
    }

    private void ensureXmlIds() {
        vecContentRoot.accept(new XmlIdGeneratingTraverser(xmlIdGenerator));
    }

    public VecSIUnit mm() {
        if (this.mm == null) {
            this.mm = SiUnitFactory.mm();
            this.vecContentRoot.getUnits()
                    .add(this.mm);
        }
        return this.mm;
    }

    public VecUnit piece() {
        if (this.piece == null) {
            this.piece = new VecOtherUnit();
            this.piece.setOtherUnitName(VecOtherUnitName.PIECE);
            this.vecContentRoot.getUnits()
                    .add(this.piece);
        }
        return this.piece;
    }

    public VecSIUnit squareMM() {
        if (this.squareMM == null) {
            this.squareMM = SiUnitFactory.squareMM();

            this.vecContentRoot.getUnits()
                    .add(this.squareMM);
        }
        return this.squareMM;
    }

    public VecSIUnit newton() {
        if (this.newton == null) {
            this.newton = SiUnitFactory.newton();

            this.vecContentRoot.getUnits()
                    .add(this.newton);
        }
        return this.newton;
    }

    public VecUnit gramPerMeter() {
        if (this.gramPerMeter == null) {
            VecSIUnit gram = SiUnitFactory.gram();
            VecSIUnit perMeter = SiUnitFactory.perMeter();
            this.gramPerMeter = new VecCompositeUnit();
            this.gramPerMeter.getFactors().add(gram);
            this.gramPerMeter.getFactors().add(perMeter);

            this.vecContentRoot.getUnits().add(gram);
            this.vecContentRoot.getUnits().add(perMeter);
            this.vecContentRoot.getUnits().add(this.gramPerMeter);
        }
        return this.gramPerMeter;
    }

    VecPartVersion findPartVersionByPartNumber(final String partNumber) {
        return this.vecContentRoot.getPartVersions()
                .stream()
                .filter(pv -> pv.getPartNumber()
                        .equals(partNumber))
                .findFirst()
                .orElseThrow();
    }

    VecDocumentVersion findPartMasterDocument(final VecPartVersion partVersion) {
        return this.vecContentRoot.getDocumentVersions()
                .stream()
                .filter(dv -> dv.getDocumentType()
                        .equals(DefaultValues.PART_MASTER) && dv.getReferencedPart()
                        .contains(partVersion))
                .findFirst()
                .orElseThrow();
    }

    VecDocumentVersion findDocument(final String documentNumber) {
        return this.vecContentRoot.getDocumentVersions().stream().filter(
                dv -> documentNumber.equals(dv.getDocumentNumber())).findFirst().orElseThrow();
    }
}
