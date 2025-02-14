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

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.v2x.VecColor;
import com.foursoft.harness.vec.v2x.VecInsulationSpecification;

import static com.foursoft.harness.vec.scripting.factories.MaterialFactory.material;
import static com.foursoft.harness.vec.scripting.factories.NumericalValueFactory.value;
import static com.foursoft.harness.vec.scripting.factories.WireTypeFactory.din76722;

public class InsulationSpecificationBuilder implements Builder<VecInsulationSpecification> {

    private final VecInsulationSpecification insulationSpecification = new VecInsulationSpecification();
    private final VecSession session;

    InsulationSpecificationBuilder(final VecSession session, final String identification) {
        this.session = session;

        insulationSpecification.setIdentification(identification);
    }

    public InsulationSpecificationBuilder withColor(
            final String baseColor) {
        final VecColor color = new VecColor();
        color.setReferenceSystem(session.getDefaultValues().getColorReferenceSystem());
        color.setKey(baseColor);
        insulationSpecification
                .getBaseColors()
                .add(color);

        return this;
    }

    public InsulationSpecificationBuilder withBaseColor(final VecColor color) {
        insulationSpecification.getBaseColors().add(color);
        return this;
    }

    public InsulationSpecificationBuilder withThickness(final double thickness) {
        insulationSpecification.setThickness(value(thickness, session.mm()));
        return this;
    }

    public InsulationSpecificationBuilder withDin76722WireType(final String wireType) {
        insulationSpecification.getWireTypes().add(din76722(wireType));

        return this;
    }

    public InsulationSpecificationBuilder withInsulationMaterial(final String materialName) {
        insulationSpecification.getMaterials().add(
                material(session.getDefaultValues().getMaterialReferenceSystem(), materialName));

        return this;
    }

    public InsulationSpecificationBuilder withPrintedLabelIdentificationValue(final String labelIdentificationText) {
        insulationSpecification.setLabelingTechnology("Printed");
        insulationSpecification.setLabelIdentificationType("AlphaNumerical");
        insulationSpecification.setLabelIdentificationValue(labelIdentificationText);
        return this;
    }

    public VecInsulationSpecification build() {
        return insulationSpecification;
    }
}

