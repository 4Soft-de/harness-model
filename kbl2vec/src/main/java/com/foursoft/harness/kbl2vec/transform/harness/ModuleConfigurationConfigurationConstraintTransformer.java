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
package com.foursoft.harness.kbl2vec.transform.harness;

import com.foursoft.harness.kbl.v25.KblModuleConfiguration;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecConfigurationConstraint;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecVariantConfiguration;

public class ModuleConfigurationConfigurationConstraintTransformer
        implements Transformer<KblModuleConfiguration, VecConfigurationConstraint> {

    @Override
    public TransformationResult<VecConfigurationConstraint> transform(final TransformationContext context,
                                                                      final KblModuleConfiguration source) {

        final VecConfigurationConstraint configurationConstraint = new VecConfigurationConstraint();

        final TransformationResult.Builder<VecConfigurationConstraint> builder = TransformationResult.from(
                configurationConstraint);

        if (source.getParentModule() != null) {
            builder.withFragment((v, b) ->
                                         v.setIdentification(
                                                 "ConfConstraint_" + source.getParentModule().getPartNumber())
            );
        } else {
            builder.withComment("This occurrence has no \"Id\" in the KBL data.");
            builder.withFragment((v, b) ->
                                         v.setIdentification("GenericIdentifier-" + context.getNewId())
            );
        }

        return builder

                .withLinker(Query.of(source), VecVariantConfiguration.class, VecConfigurationConstraint::setConfigInfo)
                .withLinker(Query.of(source.getParentModule()), VecPartOccurrence.class,
                            VecConfigurationConstraint::getConstrainedElements)
                .build();
    }
}
