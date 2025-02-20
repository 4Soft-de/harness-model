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

import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.core.PartOrUsageRelatedSpecificationBuilder;
import com.foursoft.harness.vec.scripting.enums.TemperatureType;
import com.foursoft.harness.vec.v2x.*;

import static com.foursoft.harness.vec.scripting.factories.MaterialFactory.material;
import static com.foursoft.harness.vec.scripting.factories.NumericalValueFactory.value;
import static com.foursoft.harness.vec.scripting.factories.ValueRangeFactory.valueRange;

public class GeneralTechnicalPartSpecificationBuilder
        extends PartOrUsageRelatedSpecificationBuilder<VecGeneralTechnicalPartSpecification> {
    private final VecSession session;
    private final VecGeneralTechnicalPartSpecification element;
    private final String partNumber;

    GeneralTechnicalPartSpecificationBuilder(final VecSession session, final String partNumber) {
        this.session = session;
        this.partNumber = partNumber;

        element = this.initializeSpecification(VecGeneralTechnicalPartSpecification.class, partNumber);
    }

    public GeneralTechnicalPartSpecificationBuilder withMaterialInformation(final String materialName) {
        element.getMaterialInformations().add(
                material(session.getDefaultValues().getMaterialReferenceSystem(), materialName));

        return this;
    }

    public GeneralTechnicalPartSpecificationBuilder withMassInformation(final double value, final VecUnit unit) {

        final VecMassInformation massInformation = new VecMassInformation();
        massInformation.setValue(value(value, unit));
        massInformation.setDeterminationType(VecValueDetermination.MEASURED);
        massInformation.setValueSource("Series");

        element.getMassInformations().add(massInformation);

        return this;
    }

    public GeneralTechnicalPartSpecificationBuilder withTemperatureInformation(final TemperatureType temperatureType,
                                                                               final double lowerLimit,
                                                                               final double upperLimit,
                                                                               final VecUnit unit) {

        final VecTemperatureInformation temperatureInformation = new VecTemperatureInformation();
        temperatureInformation.setTemperatureType(temperatureType.value());
        temperatureInformation.setTemperatureRange(valueRange(lowerLimit, upperLimit, unit));

        element.getTemperatureInformations().add(temperatureInformation);

        return this;
    }

    public GeneralTechnicalPartSpecificationBuilder withBoundingBoxX(final double x) {
        getOrCreateBoundingBox().setX(value(x, session.mm()));
        return this;
    }

    public GeneralTechnicalPartSpecificationBuilder withBoundingBoxY(final double y) {
        getOrCreateBoundingBox().setY(value(y, session.mm()));
        return this;
    }

    public GeneralTechnicalPartSpecificationBuilder withBoundingBoxZ(final double z) {
        getOrCreateBoundingBox().setZ(value(z, session.mm()));
        return this;
    }

    private VecBoundingBox getOrCreateBoundingBox() {
        if (element.getBoundingBox() == null) {
            element.setBoundingBox(new VecBoundingBox());
        }
        return element.getBoundingBox();
    }

    @Override public VecGeneralTechnicalPartSpecification build() {
        return element;
    }
}
