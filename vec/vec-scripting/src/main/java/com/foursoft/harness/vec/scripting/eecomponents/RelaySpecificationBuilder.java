package com.foursoft.harness.vec.scripting.eecomponents;

import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.core.SpecificationRegistry;
import com.foursoft.harness.vec.scripting.schematic.ComponentNodeLookup;
import com.foursoft.harness.vec.v2x.VecRelaySpecification;

public class RelaySpecificationBuilder extends EEComponentSpecificationBuilder<VecRelaySpecification> {
    public RelaySpecificationBuilder(final VecSession session, final String partNumber,
                                     final SpecificationRegistry specificationRegistry,
                                     final ComponentNodeLookup componentNodeLookup) {
        super(session, VecRelaySpecification.class, partNumber, specificationRegistry, componentNodeLookup);
    }

    public RelaySpecificationBuilder withLowNoise(final boolean lowNoise) {
        eeComponentSpecification.setLowNoise(lowNoise);

        return this;
    }
}
