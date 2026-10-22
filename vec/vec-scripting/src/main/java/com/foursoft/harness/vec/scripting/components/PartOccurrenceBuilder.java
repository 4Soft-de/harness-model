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

import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.Customizer;
import com.foursoft.harness.vec.scripting.Locator;
import com.foursoft.harness.vec.scripting.VecScriptingException;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.components.protection.CorrugatedPipeRoleBuilder;
import com.foursoft.harness.vec.scripting.eecomponents.EEComponentRoleBuilder;
import com.foursoft.harness.vec.scripting.eecomponents.FuseRoleBuilder;
import com.foursoft.harness.vec.scripting.schematic.ConnectionSpecificationQueries;
import com.foursoft.harness.vec.v2x.*;
import com.foursoft.harness.vec.v2x.visitor.StrictBaseVisitor;

import java.util.List;
import java.util.Objects;

import static java.util.function.Predicate.not;

public class PartOccurrenceBuilder implements Builder<VecPartOccurrence> {

    private final VecSession session;
    private final String partNumber;
    private final ConnectionSpecificationQueries connectionSpecificationQuery;
    private final Locator<? extends VecOccurrenceOrUsage> subComponentLocator;

    private final VecPartOccurrence partOccurrence = new VecPartOccurrence();
    private List<? extends Builder<? extends VecRole>> roleBuilders;

    public PartOccurrenceBuilder(final VecSession session, final String identification,
                                 final String partNumber,
                                 final ConnectionSpecificationQueries connectionSpecificationQuery) {
        this(session, identification, partNumber, connectionSpecificationQuery, id -> {
            throw new VecScriptingException("Subcomponents are not supported in this context.");
        });
    }

    public PartOccurrenceBuilder(final VecSession session, final String identification,
                                 final String partNumber,
                                 final ConnectionSpecificationQueries connectionSpecificationQuery,
                                 final Locator<? extends VecOccurrenceOrUsage> subComponentLocator) {
        this.session = session;
        this.partNumber = partNumber;
        this.connectionSpecificationQuery = connectionSpecificationQuery;
        this.subComponentLocator = subComponentLocator;
        partOccurrence.setIdentification(identification);

        instantiatePartOccurrence();

    }

    @Override
    public VecPartOccurrence build() {
        final List<? extends VecRole> roles = roleBuilders.stream().map(Builder::build).toList();

        partOccurrence.getRoles().addAll(roles);

        return partOccurrence;
    }

    public <T extends Builder<? extends VecRole>> PartOccurrenceBuilder defineRole(final Class<T> clazz,
                                                                                   final Customizer<T> customizer) {
        final T builder = StreamUtils.checkAndCast(roleBuilders, clazz).findAny().orElseThrow();

        customizer.customize(builder);

        return this;
    }

    /**
     * References the {@link VecPartOccurrence} of the part master data of a composite part (e.g. an assembly) that is
     * instantiated by this occurrence.
     */
    public PartOccurrenceBuilder withInstanciatedOccurrence(final String compositePartNumber,
                                                            final String identification) {
        final VecPartVersion compositePart = session.findPartVersionByPartNumber(compositePartNumber);
        final VecPartOccurrence instanciatedOccurrence = session.findPartMasterDocument(compositePart)
                .getSpecificationsWithType(VecCompositionSpecification.class)
                .stream()
                .flatMap(c -> c.getComponents().stream())
                .filter(o -> identification.equals(o.getIdentification()))
                .findFirst()
                .orElseThrow(() -> new VecScriptingException(
                        "No PartOccurrence '" + identification + "' found in the part master data of '" +
                                compositePartNumber + "'."));

        partOccurrence.getInstanciatedOccurrence().add(instanciatedOccurrence);

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
                .filter(not(VecPartSubstitutionSpecification.class::isInstance))
                .map(x -> x.accept(visitor))
                .filter(Objects::nonNull).toList();
    }

    private class InstantiationVisitor extends StrictBaseVisitor<Builder<? extends VecRole>> {

        @Override
        public Builder<? extends VecRole> visitVecConnectorHousingSpecification(
                final VecConnectorHousingSpecification aBean)
                throws RuntimeException {
            return new ConnectorHousingRoleBuilder(partOccurrence.getIdentification(), aBean,
                                                   connectionSpecificationQuery::findConnector);
        }

        @Override
        public Builder<? extends VecRole> visitVecCorrugatedPipeSpecification(
                final VecCorrugatedPipeSpecification aBean) throws RuntimeException {
            return new CorrugatedPipeRoleBuilder(partOccurrence.getIdentification(), aBean);
        }

        @Override
        public Builder<? extends VecRole> visitVecWireSpecification(final VecWireSpecification aBean)
                throws RuntimeException {
            return new WireRoleBuilder(session, partOccurrence.getIdentification(), aBean,
                                       connectionSpecificationQuery::findConnection);
        }

        @Override
        public Builder<? extends VecRole> visitVecPluggableTerminalSpecification(
                final VecPluggableTerminalSpecification aBean) throws RuntimeException {
            return new PluggableTerminalRoleBuilder(partOccurrence.getIdentification(), aBean);
        }

        @Override
        public Builder<? extends VecRole> visitVecEEComponentSpecification(
                final VecEEComponentSpecification aBean) throws RuntimeException {
            return new EEComponentRoleBuilder(session, partOccurrence.getIdentification(),
                                              aBean, connectionSpecificationQuery::findNode);
        }

        @Override
        public Builder<? extends VecRole> visitVecPlaceableElementSpecification(
                final VecPlaceableElementSpecification aBean) throws RuntimeException {
            return new PlaceableElementRoleBuilder(partOccurrence.getIdentification(), aBean);
        }

        @Override
        public Builder<? extends VecRole> visitVecPartStructureSpecification(final VecPartStructureSpecification aBean)
                throws RuntimeException {
            return new PartWithSubComponentsRoleBuilder(partOccurrence.getIdentification(), aBean,
                                                        subComponentLocator);
        }

        @Override
        public Builder<? extends VecRole> visitVecFuseSpecification(final VecFuseSpecification aBean)
                throws RuntimeException {
            return new FuseRoleBuilder(session, partOccurrence.getIdentification(), aBean,
                                       connectionSpecificationQuery::findNode);
        }
    }

}
