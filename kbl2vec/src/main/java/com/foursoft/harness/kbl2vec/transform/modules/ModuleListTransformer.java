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
package com.foursoft.harness.kbl2vec.transform.modules;

import com.foursoft.harness.kbl.v25.ConnectionOrOccurrence;
import com.foursoft.harness.kbl.v25.KblModuleConfiguration;
import com.foursoft.harness.kbl.v25.KblModuleConfigurationType;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecModuleList;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPartWithSubComponentsRole;

import java.util.List;

import static com.foursoft.harness.kbl2vec.transform.Queries.modulesInList;
import static com.foursoft.harness.kbl2vec.transform.Queries.partOccurrences;

/**
 * Transforms a {@link KblModuleConfiguration} of type {@link KblModuleConfigurationType#MODULE_LIST} into a
 * {@link VecModuleList}. The KBL {@code Controlled_components} become the completion components; the modules of
 * the list are resolved from the misused {@code Logistic_control_information}, see
 * {@link com.foursoft.harness.kbl2vec.transform.Queries#modulesInList}.
 */
public class ModuleListTransformer implements Transformer<KblModuleConfiguration, VecModuleList> {

    @Override
    public TransformationResult<VecModuleList> transform(final TransformationContext context,
                                                         final KblModuleConfiguration source) {
        if (source.getConfigurationType() != KblModuleConfigurationType.MODULE_LIST) {
            return TransformationResult.noResult();
        }

        // Resolved once here instead of in the linkers, so that the lookup and its warnings do not happen twice.
        final List<KblModuleConfiguration> modules = modulesInList(source, context).execute();
        final List<ConnectionOrOccurrence> components = partOccurrences(source).execute();

        // A ModuleList requires at least one ModuleInList and at least one CompletionComponents.
        if (modules.isEmpty() || components.isEmpty()) {
            context.getLogger().warn(
                    "Module list (xml ID: {}) has no modules or no controlled components and is skipped.",
                    source.getXmlId());
            return TransformationResult.noResult();
        }

        final VecModuleList element = new VecModuleList();
        element.setIdentification("ModuleList-" + source.getXmlId());

        return TransformationResult.from(element)
                .withLinker(() -> components, VecPartOccurrence.class, VecModuleList::getCompletionComponents)
                .withLinker(() -> modules, VecPartWithSubComponentsRole.class, VecModuleList::getModuleInList)
                .build();
    }

}
