/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.transform.components.common;

import com.foursoft.harness.kbl.common.HasDescription;
import com.foursoft.harness.kbl.common.HasIdentification;
import com.foursoft.harness.kbl.common.HasPart;
import com.foursoft.harness.kbl.v25.HasRelatedAssembly;
import com.foursoft.harness.kbl.v25.HasRelatedOccurrence;
import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl2vec.convert.Converter;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationFragment;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.vec.v2x.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static com.foursoft.harness.kbl2vec.transform.Fragments.commonPartDocumentAttributes;

public class Fragments {

    public static TransformationFragment<VecDocumentVersion, TransformationResult.Builder<VecDocumentVersion>> commonComponentInformation(
            final KblPart source,
            final TransformationContext context) {
        return (dv, builder) -> {
            dv.setDocumentType("PartMaster");
            commonPartDocumentAttributes(source, context).performFragment(dv, builder);

            builder.withDownstream(KblPart.class, VecGeneralTechnicalPartSpecification.class, Query.of(source),
                                   VecDocumentVersion::getSpecifications);
        };
    }

    public static TransformationFragment<VecPartOccurrence, TransformationResult.Builder<VecPartOccurrence>> commonOccurrenceInformation(
            final HasPart<?> source,
            final TransformationContext context) {
        return (po, builder) -> {

            builder.withLinker(Query.of(source.getPart()), VecPartVersion.class, VecPartOccurrence::setPart);

            if (StringUtils.isBlank(po.getIdentification())) {
                if (source instanceof final HasIdentification hasIdentification && StringUtils.isNotBlank(
                        hasIdentification.getId())) {
                    po.setIdentification(hasIdentification.getId());
                } else {
                    builder.withComment("This occurrence has no \"Id\" in the KBL data.");
                    po.setIdentification("GenericIdentifier-" + context.getNewId());
                }
            }

            if (source instanceof final HasRelatedOccurrence hasRelatedOccurrence) {
                builder.withLinker(hasRelatedOccurrence::getRelatedOccurrence, VecPartOccurrence.class,
                                   VecPartOccurrence::getInstanciatedOccurrence);
            }
            if (source instanceof final HasRelatedAssembly hasRelatedAssembly) {
                builder.withLinker(hasRelatedAssembly::getRelatedAssembly, VecPartWithSubComponentsRole.class,
                                   (occ, assembly) -> assembly.getSubComponent().add(occ));
            }
            handleDescription(source, po, context);
        };
    }

    private static void handleDescription(final HasPart<?> source,
                                          final VecPartOccurrence occurrence, final TransformationContext context) {
        if (source instanceof final HasDescription hasDescription) {
            final Converter<String, Optional<VecLocalizedString>> stringConverter =
                    context.getConverterRegistry().getStringToLocalizedString();

            stringConverter.convert(hasDescription.getDescription())
                    .ifPresent(v -> occurrence.getDescriptions()
                            .add(v));

        }
    }

}
