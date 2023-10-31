package com.foursoft.harness.vec.scripting;

import com.foursoft.harness.vec.scripting.core.DocumentVersionBuilder;
import com.foursoft.harness.vec.v2x.*;

public class HousingComponentBuilder extends AbstractChildBuilder<EEComponentSpecificationBuilder> {

    private final VecPluggableTerminalSpecification pin;
    private VecHousingComponent housingComponent = new VecHousingComponent();
    private VecConnectorHousingSpecification connectorHousingSpecification = new VecConnectorHousingSpecification();

    public HousingComponentBuilder(final EEComponentSpecificationBuilder parent,
                                   final DocumentVersionBuilder partMasterDocument,
                                   final VecPluggableTerminalSpecification pin, final String identification) {
        super(parent);
        this.pin = pin;

        housingComponent.setIdentification(identification);
        housingComponent.setHousingSpecification(connectorHousingSpecification);
        parent.getElement().getHousingComponents().add(housingComponent);

        connectorHousingSpecification.setIdentification("CHS-" + identification);
        partMasterDocument.addSpecification(connectorHousingSpecification);
    }

    public HousingComponentBuilder addPinComponent(String identification) {
        VecCavity cavity = addCavity(identification);

        VecPinComponent pinComponent = new VecPinComponent();
        pinComponent.setIdentification(identification);
        pinComponent.setReferencedCavity(cavity);
        pinComponent.setPinSpecification(pin);
        housingComponent.getPinComponents().add(pinComponent);

        return this;
    }

    public HousingComponentBuilder addPinComponents(String... identifications) {
        for (String identification : identifications) {
            addPinComponent(identification);
        }

        return this;
    }

    private VecCavity addCavity(final String cavityNumber) {
        final VecSlot vecSlot = ensureSlot();

        final VecCavity cavity = new VecCavity();
        cavity.setCavityNumber(cavityNumber);
        vecSlot.getCavities()
                .add(cavity);

        return cavity;
    }

    private VecSlot ensureSlot() {
        if (connectorHousingSpecification.getSlots().isEmpty()) {
            final VecSlot slot = new VecSlot();
            slot.setSlotNumber("X");
            connectorHousingSpecification
                    .getSlots()
                    .add(slot);
            return slot;
        } else {
            return (VecSlot) connectorHousingSpecification.getSlots().get(0);
        }

    }
}
