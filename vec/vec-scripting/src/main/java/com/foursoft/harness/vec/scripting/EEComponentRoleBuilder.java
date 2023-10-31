package com.foursoft.harness.vec.scripting;

import com.foursoft.harness.vec.v2x.*;

public class EEComponentRoleBuilder extends AbstractChildBuilder<PartOccurrenceBuilder> {

    private final VecEEComponentRole eeComponentRole;

    public EEComponentRoleBuilder(final PartOccurrenceBuilder parent, VecPartOccurrence partOccurrence,
                                  VecEEComponentSpecification specification) {
        super(parent);
        eeComponentRole = eeComponentRole(partOccurrence, specification);

    }

    private VecEEComponentRole eeComponentRole(final VecPartOccurrence partOccurrence,
                                               final VecEEComponentSpecification specification) {
        final VecEEComponentRole eeComponentRole;
        eeComponentRole = new VecEEComponentRole();
        eeComponentRole.setIdentification(partOccurrence.getIdentification());
        partOccurrence.getRoles().add(eeComponentRole);
        eeComponentRole.setEEComponentSpecification(specification);
        eeComponentRole.getHousingComponentReves().addAll(
                specification.getHousingComponents()
                        .stream()
                        .map(this::toHousingComponentReference)
                        .toList());

        return eeComponentRole;
    }

    public EEComponentRoleBuilder withComponentNode(String componentNode) {
        VecComponentNode node = parent.parent.getAssociatedSchematic().node(componentNode);
        eeComponentRole.getComponentNode().add(node);

        for (VecHousingComponentReference housing : eeComponentRole.getHousingComponentReves()) {
            VecComponentConnector connector = node.getComponentConnectors()
                    .stream()
                    .filter(c -> housing.getIdentification().equals(c.getIdentification()))
                    .findFirst()
                    .orElseThrow();

            housing.getComponentConnector().add(connector);

            for (VecPinComponentReference pin : housing.getPinComponentReves()) {
                VecComponentPort port = connector.getComponentPorts()
                        .stream()
                        .filter(p -> pin.getIdentification().equals(p.getIdentification()))
                        .findFirst()
                        .orElseThrow();

                pin.getTerminalRole().getComponentPort().add(port);
            }
        }

        return this;
    }

    private VecHousingComponentReference toHousingComponentReference(final VecHousingComponent housingComponent) {
        VecHousingComponentReference housingComponentReference = new VecHousingComponentReference();
        housingComponentReference.setIdentification(housingComponent.getIdentification());
        housingComponentReference.setHousingComponent(housingComponent);

        getSession().addXmlComment(housingComponentReference, "Embedded ConnectorHousingRole omitted.");
        housingComponentReference.getPinComponentReves()
                .addAll(housingComponent.getPinComponents()
                                .stream()
                                .map(this::toPinComponentReference)
                                .toList());

        return housingComponentReference;
    }

    private VecPinComponentReference toPinComponentReference(VecPinComponent pinComponent) {
        VecPinComponentReference pinComponentReference = new VecPinComponentReference();
        pinComponentReference.setIdentification(pinComponent.getIdentification());
        pinComponentReference.setPinComponent(pinComponent);

        VecTerminalRole terminalRole = new VecBoltTerminalRole();
        terminalRole.setIdentification(pinComponentReference.getIdentification());
        terminalRole.setTerminalSpecification(pinComponent.getPinSpecification());
        pinComponentReference.setTerminalRole(terminalRole);

        return pinComponentReference;
    }

}
