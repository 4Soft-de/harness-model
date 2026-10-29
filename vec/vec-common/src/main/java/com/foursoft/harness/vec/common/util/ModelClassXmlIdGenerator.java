/*-
 * ========================LICENSE_START=================================
 * VEC Common
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
package com.foursoft.harness.vec.common.util;

import com.foursoft.harness.navext.runtime.model.ModifiableIdentifiable;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generates XML ids made of the {@linkplain ModelClassNames#modelClassName(Class) model class name} of a bean and a
 * counter, e.g. {@code ConnectorHousingRole_00012}.
 * <p>
 * The generator does not depend on a specific VEC version, it works on any bean of the generated Java APIs.
 */
public class ModelClassXmlIdGenerator {

    private final AtomicInteger counter = new AtomicInteger(0);
    private final String commonPrefix;

    public ModelClassXmlIdGenerator() {
        this("");
    }

    /**
     * @param commonPrefix Prefix put in front of every generated id, e.g. to tell generated ids apart from
     *                     existing ones.
     */
    public ModelClassXmlIdGenerator(final String commonPrefix) {
        this.commonPrefix = commonPrefix;
    }

    /**
     * Assigns a new XML id to the given bean, unless it has one already.
     *
     * @param aBean Bean to assign the XML id to.
     */
    public void createIdForXmlBean(final ModifiableIdentifiable aBean) {
        createIdForXmlBean(aBean, derivePrefix(aBean));
    }

    protected void createIdForXmlBean(final ModifiableIdentifiable aBean, final String prefix) {
        if (StringUtils.isEmpty(aBean.getXmlId())) {
            aBean.setXmlId(generateNewXmlId(prefix));
        }
    }

    protected String generateNewXmlId(final String prefix) {
        return String.format("%s%05d", prefix, counter.getAndIncrement());
    }

    protected String derivePrefix(final ModifiableIdentifiable aBean) {
        return commonPrefix + ModelClassNames.modelClassName(aBean.getClass()) + "_";
    }

}
