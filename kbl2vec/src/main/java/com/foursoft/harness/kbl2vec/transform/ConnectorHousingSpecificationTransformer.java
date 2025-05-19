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
package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.KblConnectorHousing;
import com.foursoft.harness.kbl.v25.KblPart;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;

import static com.foursoft.harness.kbl2vec.transform.Fragments.commonSpecificationAttributes;

public class ConnectorHousingSpecificationTransformer
        implements Transformer<KblPart, VecConnectorHousingSpecification> {
    @Override
    public TransformationResult<VecConnectorHousingSpecification> transform(final TransformationContext context,
                                                                            final KblPart source) {
        if (source instanceof final KblConnectorHousing connector) {
            final VecConnectorHousingSpecification specification = new VecConnectorHousingSpecification();

            specification.setSpecialPartType(connector.getHousingType());

            return TransformationResult.from(specification)
                    .withFragment(commonSpecificationAttributes(source))
                    .build();
        }
        return TransformationResult.noResult();
    }
}
