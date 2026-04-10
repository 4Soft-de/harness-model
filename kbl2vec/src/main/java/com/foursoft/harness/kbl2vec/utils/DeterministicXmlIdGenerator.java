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
package com.foursoft.harness.kbl2vec.utils;

import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.navext.runtime.model.ModifiableIdentifiable;
import com.foursoft.harness.vec.v2x.visitor.DefaultXmlIdGenerator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DeterministicXmlIdGenerator extends DefaultXmlIdGenerator {

    private final TransformationContext context;
    private final Map<Object, Object> invertedEntityMapping;
    private final Set<String> generatedIds;

    public DeterministicXmlIdGenerator(final TransformationContext context) {
        super();
        this.context = context;
        this.invertedEntityMapping = invertEntityMapping();
        this.generatedIds = new HashSet<>();
    }

    private Map<Object, Object> invertEntityMapping() {
        return context.getEntityMapping().getContent().entries().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    private String formatId(final String baseId, final int suffix) {
        if (suffix == 0) {
            return baseId;
        }
        return baseId + "_" + suffix;
    }

    private String createIdWithCounter(final String baseId, final int suffix) {
        final String candidateId = formatId(baseId, suffix);
        if (this.generatedIds.contains(candidateId)) {
            return createIdWithCounter(baseId, suffix + 1);
        }
        return candidateId;
    }

    @Override
    protected void createIdForXmlBean(final ModifiableIdentifiable aBean, final String prefix) {
        final Object source = invertedEntityMapping.get(aBean);
        if (!(source instanceof final Identifiable identifiable)) {
            super.createIdForXmlBean(aBean, prefix);
            return;
        }
        final String vecXmlId = prefix + identifiable.getXmlId();
        final String generatedId = createIdWithCounter(vecXmlId, 0);
        aBean.setXmlId(generatedId);
        this.generatedIds.add(generatedId);
    }
}
