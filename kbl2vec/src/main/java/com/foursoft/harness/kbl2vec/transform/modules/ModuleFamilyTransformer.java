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
package com.foursoft.harness.kbl2vec.transform.modules;

import com.foursoft.harness.kbl.v25.KblLocalizedString;
import com.foursoft.harness.kbl.v25.KblModule;
import com.foursoft.harness.kbl.v25.KblModuleConfiguration;
import com.foursoft.harness.kbl.v25.KblModuleFamily;
import com.foursoft.harness.kbl2vec.convert.Converter;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecModuleFamily;
import com.foursoft.harness.vec.v2x.VecPartWithSubComponentsRole;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

public class ModuleFamilyTransformer implements Transformer<KblModuleFamily, VecModuleFamily> {

    @Override
    public TransformationResult<VecModuleFamily> transform(final TransformationContext context,
                                                           final KblModuleFamily source) {
        final VecModuleFamily element = new VecModuleFamily();
        element.setIdentification(source.getId());

        final Converter<String, Optional<VecLocalizedString>> stringConverter =
                context.getConverterRegistry().getStringToLocalizedString();
        stringConverter.convert(source.getDescription())
                .ifPresent(v -> element.getDescriptions().add(v));

        final Converter<KblLocalizedString, Optional<VecLocalizedString>> localizedStringConverter =
                context.getConverterRegistry().getLocalizedString();
        source.getLocalizedDescriptions().stream()
                .map(localizedStringConverter::convert)
                .flatMap(Optional::stream)
                .forEach(element.getDescriptions()::add);

        return TransformationResult.from(element)
                .withLinker(moduleConfigurationQuery(source), VecPartWithSubComponentsRole.class,
                            VecModuleFamily::getModuleInFamily)
                .build();
    }

    /**
     * The {@link VecPartWithSubComponentsRole} of a module is created from its
     * {@link KblModuleConfiguration}, therefore the family members have to be resolved to their configurations.
     */
    Query<KblModuleConfiguration> moduleConfigurationQuery(final KblModuleFamily source) {
        return () -> source.getRefModule().stream()
                .sorted(Comparator.comparing(KblModule::getPartNumber,
                                             Comparator.nullsLast(Comparator.naturalOrder())))
                .map(KblModule::getModuleConfiguration)
                .filter(Objects::nonNull)
                .toList();
    }

}
