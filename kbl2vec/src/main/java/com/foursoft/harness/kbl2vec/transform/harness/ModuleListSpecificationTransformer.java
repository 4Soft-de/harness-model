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
package com.foursoft.harness.kbl2vec.transform.harness;

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblModuleConfiguration;
import com.foursoft.harness.kbl.v25.KblModuleConfigurationType;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecModuleList;
import com.foursoft.harness.vec.v2x.VecModuleListSpecification;

public class ModuleListSpecificationTransformer implements Transformer<KblHarness, VecModuleListSpecification> {

    static final String IDENTIFICATION = "ModuleLists";

    @Override
    public TransformationResult<VecModuleListSpecification> transform(final TransformationContext context,
                                                                      final KblHarness source) {
        if (moduleListQuery(source).stream().findAny().isEmpty()) {
            // A ModuleListSpecification requires at least one ModuleList.
            return TransformationResult.noResult();
        }

        final VecModuleListSpecification element = new VecModuleListSpecification();
        element.setIdentification(IDENTIFICATION);

        return TransformationResult.from(element)
                .withDownstream(KblModuleConfiguration.class, VecModuleList.class, moduleListQuery(source),
                                VecModuleListSpecification::getModuleListConfigurations)
                .build();
    }

    /**
     * In the KBL a module list is a {@link KblModuleConfiguration} of type
     * {@link KblModuleConfigurationType#MODULE_LIST}. Those are defined below the harness, whereas the
     * configurations below a module describe the module itself.
     */
    static Query<KblModuleConfiguration> moduleListQuery(final KblHarness source) {
        return () -> source.getModuleConfigurations().stream()
                .filter(c -> c.getConfigurationType() == KblModuleConfigurationType.MODULE_LIST)
                .toList();
    }

}
