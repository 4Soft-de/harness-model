package com.foursoft.harness.vec.scripting.eecomponents;

import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.schematic.ComponentNodeLookup;
import com.foursoft.harness.vec.v2x.VecEEComponentRole;
import com.foursoft.harness.vec.v2x.VecEEComponentSpecification;

public class EEComponentRoleBuilder extends AbstractEEComponentRoleBuilder<VecEEComponentRole> {

    public EEComponentRoleBuilder(final VecSession session,
                                  final String identification,
                                  final VecEEComponentSpecification specification,
                                  final ComponentNodeLookup componentNodeLookup) {
        super(session, VecEEComponentRole.class, identification, specification, componentNodeLookup);
    }
}
