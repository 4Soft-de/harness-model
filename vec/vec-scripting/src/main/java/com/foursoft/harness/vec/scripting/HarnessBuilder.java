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

import com.foursoft.harness.vec.scripting.core.DocumentVersionBuilder;
import com.foursoft.harness.vec.v2x.*;

public class HarnessBuilder implements RootBuilder {

    private final VecSession session;
    private final String documentNumber;

    private final DocumentVersionBuilder harnessDocumentBuilder;
    private final VecCompositionSpecification compositionSpecification;
    private VecCompositionSpecification modulesCompositionSpecification;

    private VecContactingSpecification contactingSpecification;

    HarnessBuilder(final VecSession session, final String documentNumber, String version) {
        this.session = session;
        this.documentNumber = documentNumber;
        this.harnessDocumentBuilder = initializeDocument(documentNumber, version);
        this.compositionSpecification = initializeCompositionSpecification();
    }

    private VecContactingSpecification contactingSpecification() {
        if (contactingSpecification == null) {
            contactingSpecification = new VecContactingSpecification();
            contactingSpecification.setIdentification(this.documentNumber);
            harnessDocumentBuilder.addSpecification(contactingSpecification);
        }
        return contactingSpecification;
    }

    private DocumentVersionBuilder initializeDocument(final String documentNumber, final String version) {
        return this.session.document(documentNumber, version).documentType(
                DefaultValues.HARNESS_DESCRIPTION);
    }

    private VecCompositionSpecification initializeCompositionSpecification() {
        VecCompositionSpecification compositionSpecification = new VecCompositionSpecification();
        compositionSpecification.setIdentification(DefaultValues.COMP_COMPOSITION_SPEC_IDENTIFICATION);
        this.harnessDocumentBuilder.addSpecification(compositionSpecification);
        return compositionSpecification;
    }

    private void ensureModulesCompositionSpecification() {
        if (modulesCompositionSpecification == null) {
            modulesCompositionSpecification = new VecCompositionSpecification();
            modulesCompositionSpecification.setIdentification(DefaultValues.MODULES_COMPOSITION_SPEC_IDENTIFICATION);
            harnessDocumentBuilder.addSpecification(modulesCompositionSpecification);
        }
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

        return new VariantBuilder(this, this.harnessDocumentBuilder, partNumber, occurrences);
    }

    @Override public VecSession getSession() {
        return this.session;
    }

    @Override public void end() {

    }
}
