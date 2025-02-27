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

