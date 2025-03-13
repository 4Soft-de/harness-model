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

import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl2vec.convert.Converter;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationFragment;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecPartOrUsageRelatedSpecification;
import com.foursoft.harness.vec.v2x.VecPartVersion;

import java.util.Optional;

public final class Fragments {

    private Fragments() {
        throw new AssertionError("Can't instantiate utility class");
    }

    public static String abbreviatedClassName(final Class<?> clazz) {
        return clazz.getSimpleName().replace("Vec", "").replaceAll("[^A-Z]", "");
    }

    public static <D extends VecPartOrUsageRelatedSpecification> TransformationFragment<D,
            TransformationResult.Builder<D>> commonSpecificationAttributes(
            final KblPart source) {
        return (specification, builder) -> {
            specification.setIdentification(
                    abbreviatedClassName(specification.getClass()) + "-" + source.getPartNumber());
            builder.withLinker(Query.of(source), VecPartVersion.class, specification::getDescribedPart);
        };
    }

    public static <D extends VecDocumentVersion> TransformationFragment<D, TransformationResult.Builder<D>> commonDocumentAttributes(
            final KblPart source,
            final TransformationContext context) {
        return (dv, builder) -> {
            final Converter<String, Optional<VecLocalizedString>> stringConverter =
                    context.getConverterRegistry().getStringToLocalizedString();

            dv.setCompanyName(source.getCompanyName());
            dv.setDocumentNumber(source.getPartNumber());
            dv.setDocumentVersion(source.getVersion());
            stringConverter.convert(source.getAbbreviation())
                    .ifPresent(dv.getAbbreviations()::add);
            stringConverter.convert(source.getDescription())
                    .ifPresent(dv.getDescriptions()::add);

            builder.withLinker(source, VecPartVersion.class, dv::getReferencedPart);
        };
    }

}
