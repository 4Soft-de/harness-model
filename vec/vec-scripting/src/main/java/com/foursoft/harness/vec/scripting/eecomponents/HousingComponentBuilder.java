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

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.Queries;
import com.foursoft.harness.vec.scripting.components.ConnectorSpecificationBuilder;
import com.foursoft.harness.vec.scripting.core.SpecificationRegistry;
import com.foursoft.harness.vec.v2x.*;

public class HousingComponentBuilder implements Builder<VecHousingComponent> {

    public static final String DEFAULT_SLOT_NUMBER = "X";
    private final VecPluggableTerminalSpecification pin;
    private final SpecificationRegistry specificationRegistry;
    private final VecHousingComponent housingComponent = new VecHousingComponent();
    private final ConnectorSpecificationBuilder connectorSpecificationBuilder;

    public HousingComponentBuilder(final String identification,
                                   final VecPluggableTerminalSpecification pin,
                                   SpecificationRegistry specificationRegistry) {
        this.connectorSpecificationBuilder = new ConnectorSpecificationBuilder(identification);
        this.specificationRegistry = specificationRegistry;
        this.pin = pin;

        housingComponent.setIdentification(identification);
    }

    public HousingComponentBuilder addPinComponent(String identification) {
        connectorSpecificationBuilder.addCavity(DEFAULT_SLOT_NUMBER, identification);

        VecPinComponent pinComponent = new VecPinComponent();
        pinComponent.setIdentification(identification);
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

    @Override public VecHousingComponent build() {
        VecConnectorHousingSpecification connectorHousingSpecification = connectorSpecificationBuilder.build();
        specificationRegistry.register(connectorHousingSpecification);

        housingComponent.setHousingSpecification(connectorHousingSpecification);

        for (final VecPinComponent pinComponent : housingComponent.getPinComponents()) {
            VecCavity cavity = Queries.findCavity(connectorHousingSpecification, DEFAULT_SLOT_NUMBER,
                                                  pinComponent.getIdentification());
            pinComponent.setReferencedCavity(cavity);
        }

        return housingComponent;
    }

}
