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
package com.foursoft.harness.kbl2vec.convert;

import com.foursoft.harness.vec.v2x.VecCartesianVector2D;

import java.util.List;
import java.util.Optional;

import static com.foursoft.harness.kbl2vec.utils.CoordinateGenerator.getCoordinateOrDefault;

public class DoublesToCartesianVector2DConverter implements Converter<List<Double>, Optional<VecCartesianVector2D>> {

    @Override
    public Optional<VecCartesianVector2D> convert(final List<Double> source) {
        if (source == null || source.isEmpty()) {
            return Optional.empty();
        }
        final VecCartesianVector2D destination = new VecCartesianVector2D();
        destination.setX(getCoordinateOrDefault(source, 0));
        destination.setY(getCoordinateOrDefault(source, 1));
        return Optional.of(destination);
    }
}
