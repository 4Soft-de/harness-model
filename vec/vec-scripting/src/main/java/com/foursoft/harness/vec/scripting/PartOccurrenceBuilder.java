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

import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.scripting.schematic.ComponentNodeLookup;
import com.foursoft.harness.vec.v2x.*;
import com.foursoft.harness.vec.v2x.visitor.StrictBaseVisitor;

import java.util.List;
import java.util.Objects;

import static java.util.function.Predicate.not;

public class PartOccurrenceBuilder implements Builder<VecPartOccurrence> {

    private final VecSession session;
    private final String partNumber;

    private final VecPartOccurrence partOccurrence = new VecPartOccurrence();
    private final ComponentNodeLookup componentNodeLookup;
    private List<? extends Builder<? extends VecRole>> roleBuilders;

    PartOccurrenceBuilder(VecSession session, String identification,
                          final String partNumber, ComponentNodeLookup componentNodeLookup) {
        this.session = session;
        this.partNumber = partNumber;
        this.componentNodeLookup = componentNodeLookup;
        partOccurrence.setIdentification(identification);

        instantiatePartOccurrence();

    }

    @Override
    public VecPartOccurrence build() {
        List<? extends VecRole> roles = roleBuilders.stream().map(Builder::build).toList();

        partOccurrence.getRoles().addAll(roles);

        return partOccurrence;
    }

    public <T extends Builder<? extends VecRole>> PartOccurrenceBuilder defineRole(Class<T> clazz,
                                                                                   Customizer<T> customizer) {
        T builder = StreamUtils.checkAndCast(roleBuilders, clazz).findAny().orElseThrow();

        customizer.customize(builder);

        return this;
    }

    private void instantiatePartOccurrence() {
        final VecPartVersion partVersion = session.findPartVersionByPartNumber(this.partNumber);
        final VecDocumentVersion partMasterDocument = session.findPartMasterDocument(partVersion);

        partOccurrence.setPart(partVersion);

        this.roleBuilders = createRoleBuilders(partVersion, partMasterDocument);

    }

    private List<? extends Builder<? extends VecRole>> createRoleBuilders(final VecPartVersion partVersion,
                                                                          final VecDocumentVersion partMasterDocument) {
        final InstantiationVisitor visitor = new InstantiationVisitor();

        return StreamUtils.checkAndCast(partMasterDocument.getSpecifications()
                                                .stream(), VecPartOrUsageRelatedSpecification.class)
                .filter(s -> s.getDescribedPart().contains(partVersion))
                .filter(not(VecGeneralTechnicalPartSpecification.class::isInstance))
                .map(x -> x.accept(visitor))
                .filter(Objects::nonNull).toList();
    }

    private class InstantiationVisitor extends StrictBaseVisitor<Builder<? extends VecRole>> {

        @Override
        public Builder<? extends VecRole> visitVecConnectorHousingSpecification(
                final VecConnectorHousingSpecification aBean)
                throws RuntimeException {
            return new ConnectorHousingRoleBuilder(partOccurrence.getIdentification(), aBean);
        }

        @Override
        public Builder<? extends VecRole> visitVecWireSpecification(final VecWireSpecification aBean)
                throws RuntimeException {
            return new WireRoleBuilder(session, partOccurrence.getIdentification(), aBean);
        }

        @Override
        public Builder<? extends VecRole> visitVecPluggableTerminalSpecification(
                final VecPluggableTerminalSpecification aBean) throws RuntimeException {
            return new PluggableTerminalRoleBuilder(partOccurrence.getIdentification(), aBean);
        }

        @Override
        public Builder<? extends VecRole> visitVecEEComponentSpecification(
                final VecEEComponentSpecification aBean) throws RuntimeException {
            return new EEComponentRoleBuilder(session, partOccurrence.getIdentification(), aBean, componentNodeLookup);
        }
    }

}
