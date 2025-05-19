/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2025 4Soft GmbH
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
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecMaterial;
import com.foursoft.harness.vec.v2x.VecShieldSpecification;

import static com.foursoft.harness.vec.scripting.factories.NumericalValueFactory.value;

public class ShieldSpecificationBuilder implements Builder<VecShieldSpecification> {

    private final VecShieldSpecification shieldSpecification = new VecShieldSpecification();
    private final VecSession session;

    ShieldSpecificationBuilder(final VecSession session, final String identification) {
        this.session = session;
        shieldSpecification.setIdentification(identification);
    }

    public ShieldSpecificationBuilder braided() {
        shieldSpecification.setType("Braided");
        return this;
    }

    public ShieldSpecificationBuilder foil() {
        shieldSpecification.setType("Foil");
        return this;
    }

    public ShieldSpecificationBuilder withMaterial(final VecMaterial material) {
        shieldSpecification.getMaterials().add(material);

        return this;
    }

    public ShieldSpecificationBuilder withPlatingMaterial(final VecMaterial material) {
        shieldSpecification.getPlatingMaterials().add(material);

        return this;
    }

    public ShieldSpecificationBuilder withOpticalCoverage(final double opticalCoverage) {
        shieldSpecification.setOpticalCoverage(opticalCoverage);

        return this;
    }

    public ShieldSpecificationBuilder withResistance(final double valueInmOhmPerMetre) {
        shieldSpecification.setResistance(value(valueInmOhmPerMetre, session.mOhmPerMeter()));

        return this;
    }

    public ShieldSpecificationBuilder withDescription(final VecLocalizedString description) {
        shieldSpecification.getDescriptions().add(description);
        return this;
    }

    @Override public VecShieldSpecification build() {
        return shieldSpecification;
    }
}
