package com.foursoft.harness.vec.scripting.eecomponents;

import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.schematic.ComponentNodeLookup;
import com.foursoft.harness.vec.v2x.VecEEComponentSpecification;
import com.foursoft.harness.vec.v2x.VecRelayRole;

public class RelayRoleBuilder extends AbstractEEComponentRoleBuilder<VecRelayRole> {
    public RelayRoleBuilder(final VecSession session,
                            final String identification,
                            final VecEEComponentSpecification specification,
                            final ComponentNodeLookup componentNodeLookup) {
        super(session, VecRelayRole.class, identification, specification, componentNodeLookup);
    }
}
