package com.foursoft.harness.vec.scripting;

import com.foursoft.harness.vec.scripting.core.DocumentVersionBuilder;
import com.foursoft.harness.vec.v2x.VecEEComponentSpecification;
import com.foursoft.harness.vec.v2x.VecPluggableTerminalSpecification;

public class EEComponentSpecificationBuilder extends PartOrUsageRelatedSpecificationBuilder {

    private final VecEEComponentSpecification eeComponentSpecification;
    private final VecPluggableTerminalSpecification pin;

    public EEComponentSpecificationBuilder(final ComponentMasterDataBuilder parent,
                                           final String partNumber, final DocumentVersionBuilder partMasterDocument) {
        super(parent, partMasterDocument);

        eeComponentSpecification = initializeSpecification(VecEEComponentSpecification.class, partNumber);

        pin = new VecPluggableTerminalSpecification();
        pin.setIdentification("PTC-EE-COMP-PIN");
        pin.setTerminalType("Integrated");
        partMasterDocument.addSpecification(pin);
    }

    public VecEEComponentSpecification getElement() {
        return this.eeComponentSpecification;
    }

    public HousingComponentBuilder addHousingComponent(String identification) {
        return new HousingComponentBuilder(this, this.partMasterDocument, pin, identification);
    }
}
