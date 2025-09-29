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
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DescriptiveXmlIdGenerator extends XmlIdGenerator {

    private final TransformationContext context;
    private final Multimap<Object, Object> invertedEntityMapping;

    public DescriptiveXmlIdGenerator(final TransformationContext context) {
        super();
        this.context = context;
        this.invertedEntityMapping = invertEntityMapping();
    }

    private Multimap<Object, Object> invertEntityMapping() {
        final Multimap<Object, Object> invertedEntityMap = ArrayListMultimap.create();
        for (final Map.Entry<Object, Object> entry : context.getEntityMapping().getContent().entries()) {
            invertedEntityMap.put(entry.getValue(), entry.getKey());
        }
        return invertedEntityMap;
    }

    private boolean xmlIdExists(final String xmlId) {
        return invertedEntityMapping.keySet().stream()
                .anyMatch(key -> key instanceof final ModifiableIdentifiable modifiableIdentifiable &&
                        modifiableIdentifiable.getXmlId() != null &&
                        modifiableIdentifiable.getXmlId().equals(xmlId));
    }

    private String createIdWithCounter(final String xmlId) {
        final AtomicInteger counter = new AtomicInteger(0);
        String xmlIdWithCounter = xmlId + "_" + counter.getAndIncrement();
        while (xmlIdExists(xmlIdWithCounter)) {
            xmlIdWithCounter = xmlId + "_" + counter.getAndIncrement();
        }
        return xmlIdWithCounter;
    }

    @Override
    protected void createIdForXmlBean(final ModifiableIdentifiable aBean, final String prefix) {
        if (invertedEntityMapping.get(aBean).isEmpty()) {
            super.createIdForXmlBean(aBean, prefix);
            return;
        }

        final Object kblElement = invertedEntityMapping.get(aBean).stream().toList().get(0);
        if (kblElement instanceof final Identifiable identifiable) {
            final String vecXmlId = prefix + identifiable.getXmlId();
            if (xmlIdExists(vecXmlId)) {
                aBean.setXmlId(createIdWithCounter(vecXmlId));
                return;
            }
            aBean.setXmlId(vecXmlId);
        }
        super.createIdForXmlBean(aBean, prefix);
    }
}
