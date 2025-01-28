package com.foursoft.harness.vec.scripting.components;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.v2x.VecLocalizedString;
import com.foursoft.harness.vec.v2x.VecMaterial;
import com.foursoft.harness.vec.v2x.VecShieldSpecification;

import static com.foursoft.harness.vec.scripting.factories.NumericalValueFactory.value;

public class ShieldSpecificationBuilder implements Builder<VecShieldSpecification> {

    private final VecShieldSpecification shieldSpecification = new VecShieldSpecification();
    private final VecSession session;

    ShieldSpecificationBuilder(final VecSession session, final String identification) {
        this.session = session;
        shieldSpecification.setIdentification(identification);
    }

    public ShieldSpecificationBuilder braided() {
        shieldSpecification.setType("Braided");
        return this;
    }

    public ShieldSpecificationBuilder foil() {
        shieldSpecification.setType("Foil");
        return this;
    }

    public ShieldSpecificationBuilder withMaterial(final VecMaterial material) {
        shieldSpecification.getMaterials().add(material);

        return this;
    }

    public ShieldSpecificationBuilder withPlatingMaterial(final VecMaterial material) {
        shieldSpecification.getPlatingMaterials().add(material);

        return this;
    }

    public ShieldSpecificationBuilder withOpticalCoverage(final double opticalCoverage) {
        shieldSpecification.setOpticalCoverage(opticalCoverage);

        return this;
    }

    public ShieldSpecificationBuilder withResistance(final double valueInmOhmPerMetre) {
        shieldSpecification.setResistance(value(valueInmOhmPerMetre, session.mOhmPerMeter()));

        return this;
    }

    public ShieldSpecificationBuilder withDescription(final VecLocalizedString description) {
        shieldSpecification.getDescriptions().add(description);
        return this;
    }

    @Override public VecShieldSpecification build() {
        return shieldSpecification;
    }
}
