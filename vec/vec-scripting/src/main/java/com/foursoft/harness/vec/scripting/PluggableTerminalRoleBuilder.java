package com.foursoft.harness.vec.scripting;

import com.foursoft.harness.vec.v2x.*;

public class PluggableTerminalRoleBuilder extends AbstractChildBuilder<PartOccurrenceBuilder> {

    private final VecPluggableTerminalRole pluggableTerminalRole;

    public PluggableTerminalRoleBuilder(final PartOccurrenceBuilder parent, VecPartOccurrence partOccurrence,
                                        VecPluggableTerminalSpecification specification) {
        super(parent);
        this.pluggableTerminalRole = pluggableTerminalRole(partOccurrence, specification);

    }

    private VecPluggableTerminalRole pluggableTerminalRole(VecPartOccurrence partOccurrence,
                                                           VecPluggableTerminalSpecification specification) {

        VecPluggableTerminalRole role = new VecPluggableTerminalRole();
        role.setIdentification(partOccurrence.getIdentification());
        partOccurrence.getRoles().add(role);

        role.setTerminalSpecification(specification);

        role.getWireReceptionReferences()
                .addAll(specification.getWireReceptions()
                                .stream()
                                .map(this::toWireReceptionReference)
                                .toList());

        role.getTerminalReceptionReferences()
                .addAll(specification.getTerminalReceptions()
                                .stream()
                                .map(this::toTerminalReceptionReference)
                                .toList());

        return role;
    }

    private VecWireReceptionReference toWireReceptionReference(final VecWireReception wireReception) {
        VecWireReceptionReference ref = new VecWireReceptionReference();
        ref.setIdentification(wireReception.getIdentification());
        ref.setWireReception(wireReception);
        return ref;
    }

    private VecTerminalReceptionReference toTerminalReceptionReference(final VecTerminalReception terminalReception) {
        VecTerminalReceptionReference ref = new VecTerminalReceptionReference();
        ref.setIdentification(terminalReception.getIdentification());
        ref.setTerminalReception(terminalReception);
        return ref;

    }
}
