package com.foursoft.harness.vec.scripting;

import com.foursoft.harness.vec.scripting.core.SpecificationLocator;
import com.foursoft.harness.vec.v2x.VecCoreCrimpDetail;
import com.foursoft.harness.vec.v2x.VecCoreSpecification;

import java.util.Optional;

public class CoreCrimpDetailBuilder extends CrimpDetailBuilder<CoreCrimpDetailBuilder, VecCoreCrimpDetail> {

    private final SpecificationLocator specificationLocator;
    private final VecCoreCrimpDetail crimpDetail = new VecCoreCrimpDetail();

    public CoreCrimpDetailBuilder(final VecSession session, final SpecificationLocator specificationLocator,
                                  String coreIdentification) {
        super(session);
        this.specificationLocator = specificationLocator;
        final Optional<VecCoreSpecification> coreSpecification = specificationLocator.find(
                VecCoreSpecification.class, coreIdentification);
        crimpDetail.setAppliesTo(coreSpecification.orElseThrow());
    }

    public CoreCrimpDetailBuilder addInsulationCrimpDetails(String insIdentification,
                                                            Customizer<InsulationCrimpDetailBuilder> crimpDetailsCustomizer) {
        final InsulationCrimpDetailBuilder crimpDetailsBuilder = new InsulationCrimpDetailBuilder(session,
                                                                                                  specificationLocator,
                                                                                                  insIdentification);

        crimpDetailsCustomizer.customize(crimpDetailsBuilder);

        crimpDetail.getInsulationCrimpDetails().add(crimpDetailsBuilder.build());

        return this;
    }

    @Override
    public VecCoreCrimpDetail build() {
        return crimpDetail;
    }

    @Override
    protected VecCoreCrimpDetail getCrimpDetail() {
        return crimpDetail;
    }
}
