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
