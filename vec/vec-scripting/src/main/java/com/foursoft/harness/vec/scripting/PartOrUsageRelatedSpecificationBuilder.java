/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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
package com.foursoft.harness.vec.scripting;

import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecPartOrUsageRelatedSpecification;

import java.lang.reflect.InvocationTargetException;

public abstract class PartOrUsageRelatedSpecificationBuilder extends AbstractChildBuilder<ComponentMasterDataBuilder> {

    protected final VecDocumentVersion partMasterDocument;

    public PartOrUsageRelatedSpecificationBuilder(final ComponentMasterDataBuilder parent,
                                                  VecDocumentVersion partMasterDocument) {
        super(parent);
        this.partMasterDocument = partMasterDocument;
    }

    protected <T extends VecPartOrUsageRelatedSpecification> T initializeSpecification(Class<T> clazz,
                                                                                       final String partNumber) {
        try {
            T instance = clazz.getConstructor().newInstance();

            instance.getDescribedPart().addAll(partMasterDocument.getReferencedPart());
            instance.setIdentification(abbreviatedClassName(clazz) + "-" + partNumber);

            partMasterDocument.getSpecifications().add(instance);

            return instance;
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private String abbreviatedClassName(Class<?> clazz) {
        return clazz.getSimpleName().replace("Vec", "").replaceAll("[^A-Z]", "");
    }

}
