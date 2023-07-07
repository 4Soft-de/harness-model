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

import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecPrimaryPartType;

public class ComponentMasterDataBuilder implements RootBuilder {
    private final VecSession session;
    private final String partNumber;
    private final VecDocumentVersion partMasterDocument;
    private final VecPartVersion part;

    ComponentMasterDataBuilder(final VecSession session,
                               final String partNumber, final String documentNumber,
                               final VecPrimaryPartType primaryPartType) {
        this.session = session;
        this.partNumber = partNumber;
        this.part = initializePart(partNumber, primaryPartType);
        this.partMasterDocument = initializeDocument(documentNumber, this.part);
    }

    @Override
    public VecSession getSession() {
        return session;
    }

    private VecDocumentVersion initializeDocument(final String documentNumber, VecPartVersion partVersion) {
        final VecDocumentVersion dv = new VecDocumentVersion();
        session.getVecContentRoot().getDocumentVersions().add(dv);

        dv.setDocumentNumber(documentNumber);
        dv.setCompanyName(this.session.getDefaultValues().getCompanyName());
        dv.setDocumentVersion("1");
        dv.setDocumentType(DefaultValues.PART_MASTER);

        dv.getReferencedPart().add(part);
        return dv;
    }

    private VecPartVersion initializePart(final String partNumber, VecPrimaryPartType primaryPartType) {
        final VecPartVersion part = new VecPartVersion();
        session.getVecContentRoot().getPartVersions().add(part);

        part.setCompanyName(this.session.getDefaultValues().getCompanyName());
        part.setPartVersion("1");
        part.setPartNumber(partNumber);
        part.setPrimaryPartType(primaryPartType);

        return part;
    }

    @Override public void end() {

    }

    public ComponentMasterDataBuilder withApplicationSpecification(final String documentNumber,
                                                                   final String documentVersion) {
        VecDocumentVersion dv = new VecDocumentVersion();
        dv.setDocumentNumber(documentNumber);
        dv.setDocumentVersion(documentVersion);
        dv.setCompanyName(getSession().getDefaultValues().getCompanyName());
        dv.setDocumentType("ApplicationSpecification");

        getSession().addXmlComment(dv, " DocumentType to be verified (KBLFRM-1194).");

        getSession().getVecContentRoot().getDocumentVersions().add(dv);
        dv.getReferencedPart().add(part);

        return this;
    }

    public GeneralTechnicalPartSpecificationBuilder addGeneralTechnicalPart() {
        return new GeneralTechnicalPartSpecificationBuilder(this, this.partNumber, this.partMasterDocument);
    }

    public CoreSpecificationBuilder addCoreSpecification(String identification) {
        return new CoreSpecificationBuilder(this, partMasterDocument, identification);
    }

    public WireSpecificationBuilder addWireSpecification() {
        return new WireSpecificationBuilder(this, this.partNumber, partMasterDocument);
    }

    public WireSingleCoreBuilder addSingleCore() {
        return new WireSingleCoreBuilder(this, partNumber, partMasterDocument);
    }

    public ConnectorSpecificationBuilder addConnectorHousing() {
        return new ConnectorSpecificationBuilder(this, this.partNumber, partMasterDocument);
    }

    public PluggableTerminalSpecificationBuilder addPluggableTerminal() {
        return new PluggableTerminalSpecificationBuilder(this, this.partNumber, partMasterDocument);
    }
}
