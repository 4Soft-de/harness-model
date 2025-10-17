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
package com.foursoft.harness.kbl2vec.transform.components.wires.wire;

import com.foursoft.harness.kbl.v25.KblGeneralWire;
import com.foursoft.harness.kbl.v25.KblWireColour;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.VecColor;
import com.foursoft.harness.vec.v2x.VecInsulationSpecification;

import java.util.Optional;

public class InsulationSpecificationTransformer implements Transformer<KblGeneralWire, VecInsulationSpecification> {
    @Override
    public TransformationResult<VecInsulationSpecification> transform(final TransformationContext context,
                                                                      final KblGeneralWire source) {
        final VecInsulationSpecification dest = new VecInsulationSpecification();
        dest.setIdentification("WIRE-INS");

        if (source.getCoverColours().size() > 3) {
            context.getLogger().warn("'{}' has more than 3 cover colours. Only the first 3 will be used.", source);
        }
        for (int i = 0; i < source.getCoverColours().size(); i++) {
            final KblWireColour currentColour = source.getCoverColours().get(i);
            final Optional<VecColor> vecColor = context.getConverterRegistry().getStringToColorConverter().convert(
                    currentColour.getColourValue());
            if (vecColor.isPresent()) {
                switch (i) {
                    case 0 -> dest.getBaseColors().add(vecColor.get());
                    case 1 -> dest.getFirstIdentificationColors().add(vecColor.get());
                    case 2 -> dest.getSecondIdentificationColors().add(vecColor.get());
                    default -> context.getLogger().warn("Ignoring cover colour {} for '{}'. Only the first 3 are used.",
                                                        currentColour, source);
                }
            }
        }

        final TransformationResult.Builder<VecInsulationSpecification> builder = TransformationResult.from(dest);
        if (!dest.getBaseColors().isEmpty()) {
            builder.withCommentOnDetail(dest.getBaseColors().get(0),
                                        "Assignment to colortypes based on order in KBL file. Dialect specific color " +
                                                "type mapping required.");
        }
        return builder

                .build();
    }
}
