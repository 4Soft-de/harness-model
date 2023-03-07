package com.foursoft.harness.compatibility.vec11to12;

import com.foursoft.harness.vec.v113.VecNumericalValue;
import com.foursoft.harness.vec.v12x.VecCavityPartSpecification;
import com.foursoft.harness.vec.v12x.VecSize;

/**
 * Class for important constants.
 */
public final class Constants {

    /**
     * Package of the VEC 1.2.X {@code VecContent}.
     */
    public static final String PACKAGE_VEC12X = com.foursoft.harness.vec.v12x.VecContent.class.getPackage().getName();

    /**
     * Package of the VEC 1.1.X {@code VecContent}.
     */
    public static final String PACKAGE_VEC11X = com.foursoft.harness.vec.v113.VecContent.class.getPackage().getName();

    /**
     * The method name for {@link com.foursoft.harness.vec.v113.VecCavitySpecification#getCavityDiameter()}.
     */
    public static final String GET_CAVITY_DIAMETER = "getCavityDiameter";

    /**
     * The method name for
     * {@link com.foursoft.harness.vec.v113.VecCavitySpecification#setCavityDiameter(VecNumericalValue)}.
     */
    public static final String SET_CAVITY_DIAMETER = "setCavityDiameter";

    /**
     * The method name for {@link com.foursoft.harness.vec.v12x.VecCavitySpecification#getCavityDimension()}.
     */
    public static final String GET_CAVITY_DIMENSION = "getCavityDimension";

    /**
     * The method name for
     * {@link com.foursoft.harness.vec.v12x.VecCavitySpecification#setCavityDimension(VecSize)}.
     */
    public static final String SET_CAVITY_DIMENSION = "setCavityDimension";

    /**
     * The method name for {@link VecCavityPartSpecification#getCavityDimensions()}.
     */
    public static final String GET_CAVITY_DIMENSIONS = "getCavityDimensions";

    private Constants() {
        // hide default constructor
    }

}
