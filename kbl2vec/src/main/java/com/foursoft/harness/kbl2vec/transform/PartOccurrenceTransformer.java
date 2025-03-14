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
package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.common.HasDescription;
import com.foursoft.harness.kbl.common.HasIdentification;
import com.foursoft.harness.kbl.common.HasPart;
import com.foursoft.harness.kbl.v25.ConnectionOrOccurrence;
import com.foursoft.harness.kbl2vec.convert.Converter;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class PartOccurrenceTransformer implements Transformer<ConnectionOrOccurrence, VecPartOccurrence> {

    private int idCounter = 0;

    @Override public TransformationResult<VecPartOccurrence> transform(final TransformationContext context,
                                                                       final ConnectionOrOccurrence source) {
        if (source instanceof final HasPart<?> hasPart) {
            final VecPartOccurrence occurrence = new VecPartOccurrence();
            final TransformationResult.Builder<VecPartOccurrence> builder = TransformationResult.from(occurrence);

            builder.withLinker(Query.of(hasPart.getPart()), VecPartVersion.class, VecPartOccurrence::setPart);

            if (source instanceof final HasIdentification hasIdentification && StringUtils.isNotBlank(
                    hasIdentification.getId())) {
                occurrence.setIdentification(hasIdentification.getId());
            } else {
                builder.withComment("This occurence has no \"Id\" in the KBL data.");
                occurrence.setIdentification("GenericIdentifier-" + idCounter++);
            }
            handleDescription(source, occurrence, context);
            return builder.build();
        }
        return TransformationResult.noResult();
    }

    private static void handleDescription(final ConnectionOrOccurrence source,
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

