package com.foursoft.harness.vec.scripting;

import com.foursoft.harness.vec.scripting.core.SpecificationLocator;
import com.foursoft.harness.vec.v2x.VecInsulationCrimpDetail;
import com.foursoft.harness.vec.v2x.VecInsulationSpecification;

import java.util.Optional;

import static com.foursoft.harness.vec.scripting.factories.NumericalValueFactory.value;

public class InsulationCrimpDetailBuilder
        extends CrimpDetailBuilder<InsulationCrimpDetailBuilder, VecInsulationCrimpDetail> {

    private final SpecificationLocator specificationLocator;
    private final VecInsulationCrimpDetail crimpDetail = new VecInsulationCrimpDetail();

    public InsulationCrimpDetailBuilder(final VecSession session, final SpecificationLocator specificationLocator,
                                        String insulationIdentification) {
        super(session);
        this.specificationLocator = specificationLocator;
        final Optional<VecInsulationSpecification> insulationSpecification = specificationLocator.find(
                VecInsulationSpecification.class, insulationIdentification);
        crimpDetail.setAppliesTo(insulationSpecification.orElseThrow());
    }

    public InsulationCrimpDetailBuilder withPullOffForce(final double pullOffForce) {
        crimpDetail.setPullOffForce(value(pullOffForce, session.newton()));
        return this;
    }

    @Override
    public VecInsulationCrimpDetail build() {
        return crimpDetail;
    }

    @Override
    protected VecInsulationCrimpDetail getCrimpDetail() {
        return crimpDetail;
    }
}
