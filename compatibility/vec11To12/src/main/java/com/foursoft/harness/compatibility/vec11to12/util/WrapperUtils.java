package com.foursoft.harness.compatibility.vec11to12.util;

import com.foursoft.harness.compatibility.core.util.IdCreator;
import com.foursoft.harness.vec.v12x.VecNumericalValue;

/**
 * Utility methods for wrapping operations.
 */
public final class WrapperUtils {

    private WrapperUtils() {
        // hide default constructor
    }

    /**
     * Creates a shallow copy of the given {@link VecNumericalValue}.
     *
     * @param vecNumericalValue VecNumericalValue to copy.
     * @return Copied VecNumericalValue.
     */
    public static VecNumericalValue copyVec12xNumericalValue(final VecNumericalValue vecNumericalValue) {
        final VecNumericalValue copy = new VecNumericalValue();
        final String xmlId = IdCreator.generateXmlId(copy);
        copy.setXmlId(xmlId);
        copy.setValueComponent(vecNumericalValue.getValueComponent());
        copy.setTolerance(vecNumericalValue.getTolerance());
        copy.setUnitComponent(vecNumericalValue.getUnitComponent());

        return copy;
    }

    /**
     * Creates a shallow copy of the given {@link com.foursoft.harness.vec.v113.VecNumericalValue}.
     *
     * @param vecNumericalValue VecNumericalValue to copy.
     * @return Copied VecNumericalValue.
     */
    public static com.foursoft.harness.vec.v113.VecNumericalValue copyVec11xNumericalValue(
            final com.foursoft.harness.vec.v113.VecNumericalValue vecNumericalValue) {
        final com.foursoft.harness.vec.v113.VecNumericalValue
                copy = new com.foursoft.harness.vec.v113.VecNumericalValue();
        final String xmlId = IdCreator.generateXmlId(copy);
        copy.setXmlId(xmlId);
        copy.setValueComponent(vecNumericalValue.getValueComponent());
        copy.setTolerance(vecNumericalValue.getTolerance());
        copy.setUnitComponent(vecNumericalValue.getUnitComponent());

        return copy;
    }

}
