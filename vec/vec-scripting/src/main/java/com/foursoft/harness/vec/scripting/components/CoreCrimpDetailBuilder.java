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

import com.foursoft.harness.vec.scripting.Customizer;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.core.SpecificationLocator;
import com.foursoft.harness.vec.v2x.VecCoreCrimpDetail;
import com.foursoft.harness.vec.v2x.VecCoreSpecification;

import java.util.Optional;

public class CoreCrimpDetailBuilder extends CrimpDetailBuilder<CoreCrimpDetailBuilder, VecCoreCrimpDetail> {

    private final SpecificationLocator specificationLocator;
    private final VecCoreCrimpDetail crimpDetail = new VecCoreCrimpDetail();

    public CoreCrimpDetailBuilder(final VecSession session, final SpecificationLocator specificationLocator,
                                  final String coreIdentification) {
        super(session);
        this.specificationLocator = specificationLocator;
        final Optional<VecCoreSpecification> coreSpecification = specificationLocator.find(
                VecCoreSpecification.class, coreIdentification);
        crimpDetail.setAppliesTo(coreSpecification.orElseThrow());
    }

    public CoreCrimpDetailBuilder addInsulationCrimpDetails(final String insIdentification,
                                                            final Customizer<InsulationCrimpDetailBuilder> crimpDetailsCustomizer) {
        final InsulationCrimpDetailBuilder crimpDetailsBuilder = new InsulationCrimpDetailBuilder(session,
                                                                                                  specificationLocator,
                                                                                                  insIdentification);

        crimpDetailsCustomizer.customize(crimpDetailsBuilder);

        crimpDetail.getInsulationCrimpDetails().add(crimpDetailsBuilder.build());

        return this;
    }

    @Override
    public VecCoreCrimpDetail build() {
        return crimpDetail;
    }

    @Override
    protected VecCoreCrimpDetail getCrimpDetail() {
        return crimpDetail;
    }
}
