/*-
 * ========================LICENSE_START=================================
 * vec-v2x
 * %%
 * Copyright (C) 2020 - 2022 4Soft GmbH
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

import com.foursoft.harness.navext.runtime.model.ModifiableIdentifiable;
import com.foursoft.harness.vec.common.util.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

public class XmlIdGenerator {

    private static final String XML_ID_PREFIX = "id_";

    private final AtomicInteger counter = new AtomicInteger(0);

    public void createIdForXmlBean(final ModifiableIdentifiable aBean) {
        createIdForXmlBean(aBean, derivePrefix(aBean));
    }

    private void createIdForXmlBean(final ModifiableIdentifiable aBean, final String prefix) {
        if (StringUtils.isEmpty(aBean.getXmlId())) {
            aBean.setXmlId(generateNewXmlId(prefix));
        }
    }

    private String generateNewXmlId(final String prefix) {
        return String.format("%s%05d", prefix, counter.getAndIncrement());
    }

    private String derivePrefix(final ModifiableIdentifiable aBean) {
        return aBean.getClass()
                .getSimpleName()
                .replace("Vec", "") + "_";
    }

}
