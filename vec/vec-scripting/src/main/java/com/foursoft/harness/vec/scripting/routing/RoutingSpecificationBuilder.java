package com.foursoft.harness.vec.scripting.routing;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.Customizer;
import com.foursoft.harness.vec.scripting.DefaultValues;
import com.foursoft.harness.vec.scripting.Locator;
import com.foursoft.harness.vec.v2x.VecConfigurationConstraint;
import com.foursoft.harness.vec.v2x.VecRoutingSpecification;
import com.foursoft.harness.vec.v2x.VecTopologySegment;
import com.foursoft.harness.vec.v2x.VecWireElementReference;

public class RoutingSpecificationBuilder implements Builder<VecRoutingSpecification> {

    private final VecRoutingSpecification routingSpecification;
    private final Locator<VecWireElementReference> wireElementReferenceLocator;
    private final Locator<VecTopologySegment> segmentLocator;
    private final Locator<VecConfigurationConstraint> configurationConstraintLocator;

    public RoutingSpecificationBuilder(Locator<VecWireElementReference> wireElementReferenceLocator,
                                       Locator<VecTopologySegment> segmentLocator,
                                       Locator<VecConfigurationConstraint> configurationConstraintLocator) {
        this.wireElementReferenceLocator = wireElementReferenceLocator;
        this.segmentLocator = segmentLocator;
        this.configurationConstraintLocator = configurationConstraintLocator;
        routingSpecification = initializeSpecification();
    }

    @Override
    public VecRoutingSpecification build() {
        return routingSpecification;
    }

    public RoutingSpecificationBuilder addRouting(String wireElementRefId, String... segmentIds) {
        addRouting(wireElementRefId, routing -> {
            routing.appendPath(segmentIds);
        });
        return this;
    }

    public RoutingSpecificationBuilder addRouting(String wireElementRefId, Customizer<RoutingBuilder> customizer) {
        final RoutingBuilder routingBuilder = new RoutingBuilder(configurationConstraintLocator,
                                                                 wireElementReferenceLocator.locate(wireElementRefId),
                                                                 segmentLocator);
        if (customizer != null) {
            customizer.customize(routingBuilder);
        }

        routingSpecification.getRoutings().add(routingBuilder.build());

        return this;
    }

    private static VecRoutingSpecification initializeSpecification() {
        final VecRoutingSpecification specification = new VecRoutingSpecification();
        specification.setIdentification(DefaultValues.ROUTING_SPECIFICATION_IDENTIFICATION);
        return specification;
    }
}
