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

import com.foursoft.harness.kbl.v25.KblUnit;
import com.foursoft.harness.kbl2vec.core.NoMappingDefinedException;
import com.foursoft.harness.kbl2vec.core.TransformationContext;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.*;

import java.math.BigInteger;

import static com.foursoft.harness.kbl2vec.utils.EnumMapper.mapEnum;

public class UnitTransformer implements Transformer<KblUnit, VecUnit> {

    @Override
    public TransformationResult<VecUnit> transform(final TransformationContext context, final KblUnit source) {

        if (source.getSiUnitName() != null) {
            return TransformationResult.of(toSiUnit(source));
        }

        context.getLogger().warn("DIALECT: KBL uses non standard unit {}, converted to CustomUnit.",
                                 source.getUnitName());
        final VecCustomUnit vecCustomUnit = new VecCustomUnit();

        vecCustomUnit.setIdentification(source.getUnitName());

        return TransformationResult.of(vecCustomUnit);
    }

    private VecSIUnit toSiUnit(final KblUnit source) {
        final VecSIUnit result = new VecSIUnit();

        result.setSiUnitName(mapEnum(source.getSiUnitName(), VecSiUnitName.class));
        if (source.getSiPrefix() != null) {
            result.setSiPrefix(mapEnum(source.getSiPrefix(), VecSiPrefix.class));
        }
        if (source.getSiDimension() != null) {
            switch (source.getSiDimension()) {
                case SQUARE -> result.setExponent(BigInteger.valueOf(2));
                case CUBIC -> result.setExponent(BigInteger.valueOf(3));
                default -> throw new NoMappingDefinedException(
                        "Unexpected SiDimensionValue: " + source.getSiDimension());
            }
        }
        return result;
    }

}

