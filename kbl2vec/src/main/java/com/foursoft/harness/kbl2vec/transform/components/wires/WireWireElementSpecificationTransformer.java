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
package com.foursoft.harness.kbl2vec.transform.components.wires;

import com.foursoft.harness.kbl.v25.KblGeneralWire;
import com.foursoft.harness.kbl.v25.KblNumericalValue;
import com.foursoft.harness.kbl2vec.convert.StringToWireTypeConverter;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecCoreSpecification;
import com.foursoft.harness.vec.v2x.VecInsulationSpecification;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import com.foursoft.harness.vec.v2x.VecWireElementSpecification;

public class WireWireElementSpecificationTransformer
        implements Transformer<KblGeneralWire, VecWireElementSpecification> {

    @Override
    public TransformationResult<VecWireElementSpecification> transform(final TransformationContext context,
                                                                       final KblGeneralWire source) {
        final VecWireElementSpecification dest = new VecWireElementSpecification();
        dest.setIdentification("WIRE");
        final StringToWireTypeConverter wireTypeConverter =
                context.getConverterRegistry().getStringToWireTypeConverter();
        wireTypeConverter.convert(source.getWireType()).ifPresent(dest.getTypes()::add);

        final TransformationResult.Builder<VecWireElementSpecification> builder =
                TransformationResult.from(dest)
                        .withDownstream(KblNumericalValue.class, VecNumericalValue.class,
                                        Query.of(source::getOutsideDiameter),
                                        VecWireElementSpecification::setOutsideDiameter)
                        .withDownstream(KblNumericalValue.class, VecNumericalValue.class,
                                        Query.of(source::getBendRadius),
                                        VecWireElementSpecification::setMinBendRadiusStatic)
                        .withLinker(Query.of(source), VecInsulationSpecification.class,
                                    VecWireElementSpecification::setInsulationSpecification);
        // Single Core Wire
        if (source.getCores().isEmpty()) {
            builder.withLinker(Query.of(source), VecCoreSpecification.class,
                               VecWireElementSpecification::setConductorSpecification);
        }

        return builder
                .build();
    }
}
