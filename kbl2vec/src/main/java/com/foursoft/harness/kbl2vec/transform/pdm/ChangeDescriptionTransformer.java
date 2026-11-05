/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 - 2026 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.transform.pdm;

import com.foursoft.harness.kbl.v25.KblChange;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecChangeDescription;

import static com.foursoft.harness.kbl2vec.transform.pdm.PdmValues.date;
import static com.foursoft.harness.kbl2vec.transform.pdm.PdmValues.person;
import static com.foursoft.harness.kbl2vec.utils.UndefinedValues.definedOrNull;
import static com.foursoft.harness.kbl2vec.utils.UndefinedValues.isUndefined;

public class ChangeDescriptionTransformer implements Transformer<KblChange, VecChangeDescription> {

    @Override
    public TransformationResult<VecChangeDescription> transform(final TransformationContext context,
                                                                final KblChange source) {
        final VecChangeDescription destination = new VecChangeDescription();
        destination.setIdentification(definedOrNull(source.getId()));
        context.getConverterRegistry().getStringToLocalizedString().convert(source.getDescription())
                .ifPresent(destination.getDescriptions()::add);
        source.getLocalizedDescriptions().forEach(d -> context.getConverterRegistry().getLocalizedString()
                .convert(d).ifPresent(destination.getDescriptions()::add));
        if (!isUndefined(source.getChangeRequest())) {
            destination.getRelatedChangeRequests().add(source.getChangeRequest());
        }
        date(context, source.getChangeDate(), source).ifPresent(destination::setChangeDate);
        person(source.getResponsibleDesigner(), source.getDesignerDepartment())
                .ifPresent(destination::setResponsibleDesigner);
        person(source.getApproverName(), source.getApproverDepartment()).ifPresent(destination::setApprover);
        if (isEmpty(destination)) {
            // E.g. a change with an unknown designer and a date in an unknown format.
            return TransformationResult.noResult();
        }
        return TransformationResult.of(destination);
    }

    private static boolean isEmpty(final VecChangeDescription description) {
        return description.getIdentification() == null && description.getDescriptions().isEmpty()
                && description.getRelatedChangeRequests().isEmpty() && description.getChangeDate() == null
                && description.getResponsibleDesigner() == null && description.getApprover() == null;
    }
}
