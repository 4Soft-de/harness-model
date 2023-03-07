/*-
 * ========================LICENSE_START=================================
 * compatibility-vec11to12
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
