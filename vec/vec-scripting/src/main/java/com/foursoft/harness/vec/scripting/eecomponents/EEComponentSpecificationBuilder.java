/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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
package com.foursoft.harness.vec.scripting.eecomponents;

import com.foursoft.harness.vec.scripting.Customizer;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.core.PartOrUsageRelatedSpecificationBuilder;
import com.foursoft.harness.vec.scripting.core.SpecificationLocator;
import com.foursoft.harness.vec.scripting.core.SpecificationRegistry;
import com.foursoft.harness.vec.scripting.schematic.ComponentNodeLookup;
import com.foursoft.harness.vec.v2x.*;

public class EEComponentSpecificationBuilder<T extends VecEEComponentSpecification>
        extends PartOrUsageRelatedSpecificationBuilder<T> {

    protected final T eeComponentSpecification;
    private final SpecificationRegistry specificationRegistry;
    private final ComponentNodeLookup componentNodeLookup;
    private final VecPluggableTerminalSpecification pinSpecification;
    protected final VecSession session;

    public EEComponentSpecificationBuilder(final VecSession session, final Class<T> eeComponentClass,
                                           final String partNumber,
                                           final SpecificationLocator specificationLocator,
                                           final SpecificationRegistry specificationRegistry,
                                           final ComponentNodeLookup componentNodeLookup) {
        this.session = session;

        eeComponentSpecification = initializeSpecification(eeComponentClass, partNumber);
        this.specificationRegistry = specificationRegistry;
        this.componentNodeLookup = componentNodeLookup;

        pinSpecification = specificationLocator.find(VecPluggableTerminalSpecification.class, "PTC-EE-COMP-PIN")
                .orElseGet(() -> {
                    final VecPluggableTerminalSpecification result = new VecPluggableTerminalSpecification();
                    result.setIdentification("PTC-EE-COMP-PIN");
                    result.setTerminalType("Integrated");
                    specificationRegistry.register(result);
                    return result;
                });

    }

    public EEComponentSpecificationBuilder<T> addHousingComponent(final String identification,
                                                                  final Customizer<HousingComponentBuilder> customizer) {
        final HousingComponentBuilder builder = new HousingComponentBuilder(identification, pinSpecification,
                                                                            specificationRegistry);

        customizer.customize(builder);

        eeComponentSpecification.getHousingComponents().add(builder.build());

        return this;
    }

    public EEComponentSpecificationBuilder<T> withComponentNode(final String componentNode) {
        final VecComponentNode node = componentNodeLookup.find(componentNode);
        eeComponentSpecification.setComponentNode(node);

        for (final VecHousingComponent housing : eeComponentSpecification.getHousingComponents()) {
            final VecComponentConnector connector = node.getComponentConnectors()
                    .stream()
                    .filter(c -> housing.getIdentification().equals(c.getIdentification()))
                    .findFirst()
                    .orElseThrow();

            housing.setComponentConnector(connector);

            for (final VecPinComponent pin : housing.getPinComponents()) {
                final VecComponentPort port = connector.getComponentPorts()
                        .stream()
                        .filter(p -> pin.getIdentification().equals(p.getIdentification()))
                        .findFirst()
                        .orElseThrow();

                pin.setComponentPort(port);
            }
        }

        return this;
    }

    @Override
    public T build() {
        return this.eeComponentSpecification;
    }
}
