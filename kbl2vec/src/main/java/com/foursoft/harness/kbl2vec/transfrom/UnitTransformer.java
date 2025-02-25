package com.foursoft.harness.kbl2vec.transfrom;

import com.foursoft.harness.kbl.v25.KblUnit;
import com.foursoft.harness.kbl2vec.core.NoMappingDefinedException;
import com.foursoft.harness.kbl2vec.core.TransformationResult;
import com.foursoft.harness.kbl2vec.core.Transformer;
import com.foursoft.harness.vec.v2x.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

public class UnitTransformer implements Transformer<KblUnit, VecUnit> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnitTransformer.class);

    //private final ModelMapper mapper = new ModelMapper();

    @Override
    public TransformationResult<VecUnit> transform(final KblUnit source) {

        if (source.getSiUnitName() != null) {
            return TransformationResult.of(toSiUnit(source));
        }

        LOGGER.warn("DIALECT: KBL uses non standard unit {}, converted to CustomUnit.", source.getUnitName());
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

    private <D extends Enum<D>> D mapEnum(final Enum<?> source, final Class<D> enumClass) {
        final String name = source.name();
        final D[] literals = enumClass.getEnumConstants();
        for (final D literal : literals) {
            if (name.equalsIgnoreCase(literal.name())) {
                return literal;
            }
        }
        throw new NoMappingDefinedException("No mapping defined for " + enumClass.getSimpleName() + ": " + name);
    }

}

