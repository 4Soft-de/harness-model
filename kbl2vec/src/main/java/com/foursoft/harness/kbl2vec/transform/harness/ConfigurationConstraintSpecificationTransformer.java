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

import com.foursoft.harness.kbl.v25.KblHarness;
import com.foursoft.harness.kbl.v25.KblModule;
import com.foursoft.harness.kbl.v25.KblModuleConfiguration;
import com.foursoft.harness.kbl.v25.KblModuleConfigurationType;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecConfigurationConstraint;
import com.foursoft.harness.vec.v2x.VecConfigurationConstraintSpecification;

import java.util.stream.Stream;

import static com.foursoft.harness.kbl2vec.transform.Fragments.abbreviatedClassName;

public class ConfigurationConstraintSpecificationTransformer
        implements Transformer<KblHarness, VecConfigurationConstraintSpecification> {
    @Override
    public TransformationResult<VecConfigurationConstraintSpecification> transform(final TransformationContext context,
                                                                                   final KblHarness source) {
        final VecConfigurationConstraintSpecification element = new VecConfigurationConstraintSpecification();
        element.setIdentification(
                abbreviatedClassName(element.getClass()) + "-" + source.getPartNumber());
        return TransformationResult.from(element)
                .withDownstream(KblModuleConfiguration.class, VecConfigurationConstraint.class,
                                moduleConfigurationQuery(source),
                                VecConfigurationConstraintSpecification::getConfigurationConstraints)
                .build();
    }

    /**
     * Both the configurations of the modules and the standalone configurations below the harness become a
     * {@link VecConfigurationConstraint}; they only differ in what they constrain, see
     * {@link ModuleConfigurationConfigurationConstraintTransformer}. Module lists are standalone configurations
     * as well, but they are transformed into a {@code VecModuleList} instead.
     */
    Query<KblModuleConfiguration> moduleConfigurationQuery(final KblHarness source) {
        return () -> {
            final Stream<KblModuleConfiguration> moduleConfigurations = source.getModules().stream()
                    .map(KblModule::getModuleConfiguration);
            final Stream<KblModuleConfiguration> standAloneConfigurations = source.getModuleConfigurations().stream()
                    .filter(c -> c.getConfigurationType() != KblModuleConfigurationType.MODULE_LIST);

            return Stream.concat(moduleConfigurations, standAloneConfigurations)
                    .toList();
        };
    }
}
