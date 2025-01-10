/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2025 4Soft GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =========================LICENSE_END==================================
 */
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
