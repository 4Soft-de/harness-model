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
import com.foursoft.harness.vec.v2x.*;
import com.foursoft.harness.vec.v2x.visitor.StrictBaseVisitor;

import java.util.List;
import java.util.Objects;

import static java.util.function.Predicate.not;

public class PartOccurrenceBuilder extends AbstractChildBuilder<HarnessBuilder> {

    private final VecCompositionSpecification compositionSpecification;
    private final String partNumber;

    private final VecPartOccurrence element = new VecPartOccurrence();
    private List<AbstractChildBuilder<PartOccurrenceBuilder>> roleBuilders;

    PartOccurrenceBuilder(final HarnessBuilder parent, VecCompositionSpecification compositionSpecification,
                          String identification,
                          final String partNumber) {
        super(parent);
        this.compositionSpecification = compositionSpecification;
        this.partNumber = partNumber;

        compositionSpecification.getComponents().add(element);
        element.setIdentification(identification);

        instantiatePartOccurrence();

    }

    public <T extends AbstractChildBuilder<PartOccurrenceBuilder>> T roleBuilder(Class<T> clazz) {
        return StreamUtils.checkAndCast(roleBuilders, clazz).findAny().orElseThrow();
    }

    private void instantiatePartOccurrence() {
        final VecPartVersion partVersion = this.getSession()
                .findPartVersionByPartNumber(this.partNumber);
        final VecDocumentVersion partMasterDocument = this.getSession()
                .findPartMasterDocument(partVersion);

        element.setPart(partVersion);

        this.roleBuilders = createRoleBuilders(partVersion, partMasterDocument);

    }

    private List<AbstractChildBuilder<PartOccurrenceBuilder>> createRoleBuilders(final VecPartVersion partVersion,
                                                                                 final VecDocumentVersion partMasterDocument) {
        final InstantiationVisitor visitor = new InstantiationVisitor();

        return StreamUtils.checkAndCast(partMasterDocument.getSpecifications()
                                                .stream(), VecPartOrUsageRelatedSpecification.class)
                .filter(s -> s.getDescribedPart().contains(partVersion))
                .filter(not(VecGeneralTechnicalPartSpecification.class::isInstance))
                .map(x -> x.accept(visitor))
                .filter(Objects::nonNull).toList();
    }

    private class InstantiationVisitor extends StrictBaseVisitor<AbstractChildBuilder<PartOccurrenceBuilder>> {

        @Override
        public AbstractChildBuilder<PartOccurrenceBuilder> visitVecConnectorHousingSpecification(
                final VecConnectorHousingSpecification aBean)
                throws RuntimeException {
            return new ConnectorHousingRoleBuilder(PartOccurrenceBuilder.this, element, aBean);
        }

        @Override
        public AbstractChildBuilder<PartOccurrenceBuilder> visitVecWireSpecification(final VecWireSpecification aBean)
                throws RuntimeException {
            return new WireRoleBuilder(PartOccurrenceBuilder.this, element, aBean);
        }

        @Override
        public AbstractChildBuilder<PartOccurrenceBuilder> visitVecPluggableTerminalSpecification(
                final VecPluggableTerminalSpecification aBean) throws RuntimeException {
            return new PluggableTerminalRoleBuilder(PartOccurrenceBuilder.this, element, aBean);
        }
    }

}
