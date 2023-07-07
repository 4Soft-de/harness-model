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

import com.foursoft.harness.vec.v2x.*;

public class HarnessBuilder implements RootBuilder {

    private final VecSession session;
    private final VecDocumentVersion harnessDocument;
    private final VecCompositionSpecification compositionSpecification;
    private VecCompositionSpecification modulesCompositionSpecification;

    private VecContactingSpecification contactingSpecification;

    HarnessBuilder(final VecSession session, final String documentNumber, String version) {
        this.session = session;
        this.harnessDocument = initializeDocument(documentNumber, version);
        this.compositionSpecification = initializeCompositionSpecification(harnessDocument);
    }

    private VecContactingSpecification contactingSpecification() {
        if (contactingSpecification == null) {
            contactingSpecification = new VecContactingSpecification();
            contactingSpecification.setIdentification(harnessDocument.getDocumentNumber());
            harnessDocument.getSpecifications().add(contactingSpecification);
        }
        return contactingSpecification;
    }

    private VecCompositionSpecification initializeCompositionSpecification(final VecDocumentVersion harnessDocument) {
        VecCompositionSpecification compositionSpecification = new VecCompositionSpecification();
        compositionSpecification.setIdentification(DefaultValues.COMP_COMPOSITION_SPEC_IDENTIFICATION);
        harnessDocument.getSpecifications().add(compositionSpecification);
        return compositionSpecification;
    }

    private void ensureModulesCompositionSpecification() {
        if (modulesCompositionSpecification == null) {
            modulesCompositionSpecification = new VecCompositionSpecification();
            modulesCompositionSpecification.setIdentification(DefaultValues.MODULES_COMPOSITION_SPEC_IDENTIFICATION);
            harnessDocument.getSpecifications().add(modulesCompositionSpecification);
        }
    }

    private VecDocumentVersion initializeDocument(final String documentNumber, final String version) {
        final VecDocumentVersion dv = new VecDocumentVersion();
        session.getVecContentRoot().getDocumentVersions().add(dv);

        dv.setDocumentNumber(documentNumber);
        dv.setCompanyName(this.session.getDefaultValues().getCompanyName());
        dv.setDocumentVersion(version);
        dv.setDocumentType(DefaultValues.HARNESS_DESCRIPTION);

        return dv;
    }

    VecPartOccurrence occurrence(final String identification) {
        return this.compositionSpecification.getComponents()
                .stream()
                .filter(o -> o.getIdentification()
                        .equals(identification))
                .findAny()
                .orElseThrow();
    }

    public ConnectivityBuilder addConnection(String wireElementReferenceId) {
        final VecWireElementReference wireElementReference = this.compositionSpecification.getComponents()
                .stream()
                .flatMap(o -> o.getRoleWithType(VecWireRole.class)
                        .stream())
                .flatMap(o -> o.getWireElementReferences()
                        .stream())
                .filter(ref -> ref.getIdentification()
                        .equals(wireElementReferenceId))
                .findAny()
                .orElseThrow();

        return new ConnectivityBuilder(this, this.contactingSpecification(), wireElementReference);
    }

    public PartOccurrenceBuilder addPartOccurrence(
            final String identification, final String partNumber) {
        return new PartOccurrenceBuilder(this, this.compositionSpecification, identification, partNumber);
    }

    public VariantBuilder addVariant(final String partNumber, final String... occurrences) {
        ensureModulesCompositionSpecification();

        return new VariantBuilder(this, this.harnessDocument, partNumber, occurrences);
    }

    @Override public VecSession getSession() {
        return this.session;
    }

    @Override public void end() {

    }
}
