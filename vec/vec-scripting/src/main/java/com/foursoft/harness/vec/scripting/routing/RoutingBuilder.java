package com.foursoft.harness.vec.scripting.routing;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.Locator;
import com.foursoft.harness.vec.scripting.variants.ConfigurableElementBuilder;
import com.foursoft.harness.vec.v2x.*;

import java.util.Arrays;

public class RoutingBuilder extends ConfigurableElementBuilder<RoutingBuilder, VecRouting>
        implements Builder<VecRouting> {

    private final VecRouting routing = new VecRouting();
    private final Locator<VecTopologySegment> segmentLocator;

    public RoutingBuilder(final Locator<VecConfigurationConstraint> configurationConstraintLocator,
                          VecWireElementReference wireElementReference, Locator<VecTopologySegment> segmentLocator) {
        super(configurationConstraintLocator);
        this.segmentLocator = segmentLocator;
        routing.setIdentification(wireElementReference.getIdentification());
        routing.setRoutedElement(wireElementReference);
        VecPath path = new VecPath();
        routing.setPath(path);
    }

    public RoutingBuilder withIdentification(final String identification) {
        routing.setIdentification(identification);
        return this;
    }

    public RoutingBuilder appendPath(String... segmentIds) {
        Arrays.stream(segmentIds)
                .map(segmentLocator::locate)
                .forEach(routing.getPath().getSegment()::add);

        return this;
    }

    public RoutingBuilder addMandatorySegments(String... segmentIds) {
        Arrays.stream(segmentIds)
                .map(segmentLocator::locate)
                .forEach(routing.getMandatorySegment()::add);
        return this;
    }

    @Override
    public VecRouting build() {
        return routing;
    }

    @Override
    protected VecRouting element() {
        return routing;
    }
}
