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
package com.foursoft.harness.vec.scripting.components.protection;

import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.v2x.VecCorrugatedPipeSpecification;

import static com.foursoft.harness.vec.scripting.factories.NumericalValueFactory.value;

public class CorrugatedPipeSpecificationBuilder
        extends WireProtectionSpecificationBuilder<VecCorrugatedPipeSpecification> {

    private final VecSession session;

    public CorrugatedPipeSpecificationBuilder(final VecSession session, final String partNumber) {
        super(VecCorrugatedPipeSpecification.class, partNumber);
        this.session = session;
    }

    public CorrugatedPipeSpecificationBuilder withCorrugationHeight(final double height) {
        element().setCorrugationHeight(value(height, session.mm()));

        return this;
    }

    public CorrugatedPipeSpecificationBuilder withNominalSize(final String nominalSize) {
        element().setNominalSize(nominalSize);

        return this;
    }

    public CorrugatedPipeSpecificationBuilder withInnerDiameter(final double innerDiameter) {
        element().setInnerDiameter(value(innerDiameter, session.mm()));

        return this;
    }

    public CorrugatedPipeSpecificationBuilder withOuterDiameter(final double outerDiameter) {
        element().setOuterDiameter(value(outerDiameter, session.mm()));

        return this;
    }

    public CorrugatedPipeSpecificationBuilder withCorrugationWidth(final double width) {
        element().setCorrugationWidth(value(width, session.mm()));

        return this;
    }

}
