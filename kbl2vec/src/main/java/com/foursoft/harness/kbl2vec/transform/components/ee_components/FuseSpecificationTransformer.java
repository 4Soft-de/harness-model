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
package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblFuse;
import com.foursoft.harness.kbl.v25.KblFuseType;
import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecFuseSpecification;
import com.foursoft.harness.vec.v2x.VecNumericalValue;

import static com.foursoft.harness.kbl2vec.transform.Fragments.commonSpecificationAttributes;
import static com.foursoft.harness.kbl2vec.utils.UndefinedValues.definedOrNull;

/**
 * Transforms a KBL {@link KblFuse}. The key of the fuse type is copied as it is, the predefined values of the VEC
 * {@code FuseType} (e.g. {@code Type_SF30}) are not interpreted. VEC has no counterpart for the nominal current, it
 * is mapped to {@code IMax}. KBL describes no internal structure of a fuse, therefore the specification has no
 * housing or pin components.
 */
public class FuseSpecificationTransformer implements Transformer<KblFuse, VecFuseSpecification> {

    @Override
    public TransformationResult<VecFuseSpecification> transform(final TransformationContext context,
                                                                final KblFuse source) {
        final VecFuseSpecification destination = new VecFuseSpecification();
        final KblFuseType type = source.getType();
        if (type != null) {
            destination.setFuseType(definedOrNull(type.getKey()));
        }
        return TransformationResult.from(destination)
                .withFragment(commonSpecificationAttributes(source))
                .withDownstream(KblNumericalValue.class, VecNumericalValue.class,
                                Query.of(source::getNominalCurrent), VecFuseSpecification::setIMax)
                .build();
    }
}
