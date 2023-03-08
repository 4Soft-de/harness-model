/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.1.X To VEC 1.2.X
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
