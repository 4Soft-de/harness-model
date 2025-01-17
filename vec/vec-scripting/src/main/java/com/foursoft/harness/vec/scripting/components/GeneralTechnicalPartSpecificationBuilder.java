/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
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
package com.foursoft.harness.vec.scripting.components;

import com.foursoft.harness.vec.scripting.core.PartOrUsageRelatedSpecificationBuilder;
import com.foursoft.harness.vec.v2x.VecGeneralTechnicalPartSpecification;
import com.foursoft.harness.vec.v2x.VecMassInformation;
import com.foursoft.harness.vec.v2x.VecUnit;
import com.foursoft.harness.vec.v2x.VecValueDetermination;

import static com.foursoft.harness.vec.scripting.factories.NumericalValueFactory.value;

public class GeneralTechnicalPartSpecificationBuilder
        extends PartOrUsageRelatedSpecificationBuilder<VecGeneralTechnicalPartSpecification> {

    private final VecGeneralTechnicalPartSpecification element;
    private final String partNumber;

    GeneralTechnicalPartSpecificationBuilder(final String partNumber) {
        this.partNumber = partNumber;

        element = this.initializeSpecification(VecGeneralTechnicalPartSpecification.class, partNumber);
    }

    public GeneralTechnicalPartSpecificationBuilder withMassInformation(double value, VecUnit unit) {

        VecMassInformation massInformation = new VecMassInformation();
        massInformation.setValue(value(value, unit));
        massInformation.setDeterminationType(VecValueDetermination.MEASURED);
        massInformation.setValueSource("Series");

        element.getMassInformations().add(massInformation);

        return this;
    }

    @Override public VecGeneralTechnicalPartSpecification build() {
        return element;
    }
}
