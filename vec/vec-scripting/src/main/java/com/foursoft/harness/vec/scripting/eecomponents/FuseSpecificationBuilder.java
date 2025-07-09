package com.foursoft.harness.vec.scripting.eecomponents;

import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.core.SpecificationRegistry;
import com.foursoft.harness.vec.scripting.factories.NumericalValueFactory;
import com.foursoft.harness.vec.scripting.schematic.ComponentNodeLookup;
import com.foursoft.harness.vec.v2x.VecFuseSpecification;

public class FuseSpecificationBuilder extends EEComponentSpecificationBuilder<VecFuseSpecification> {
    public FuseSpecificationBuilder(final VecSession session, final String partNumber,
                                    final SpecificationRegistry specificationRegistry,
                                    final ComponentNodeLookup componentNodeLookup) {
        super(session, VecFuseSpecification.class, partNumber, specificationRegistry, componentNodeLookup);
    }

    public FuseSpecificationBuilder withIMax(final double value) {
        eeComponentSpecification.setIMax(NumericalValueFactory.value(value, session.ampere()));

        return this;
    }
}
