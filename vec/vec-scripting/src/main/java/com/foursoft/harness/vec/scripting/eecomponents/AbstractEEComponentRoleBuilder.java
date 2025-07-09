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
import com.foursoft.harness.vec.scripting.VecScriptingException;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.schematic.ComponentNodeLookup;
import com.foursoft.harness.vec.v2x.*;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractEEComponentRoleBuilder<T extends VecEEComponentRole> implements Builder<T> {

    private final T eeComponentRole;
    private final VecSession session;
    private final ComponentNodeLookup componentNodeLookup;

    public AbstractEEComponentRoleBuilder(final VecSession session, final Class<T> eeComponentClass,
                                          final String identification,
                                          final VecEEComponentSpecification specification,
                                          final ComponentNodeLookup componentNodeLookup) {
        this.session = session;
        this.componentNodeLookup = componentNodeLookup;
        eeComponentRole = eeComponentRole(identification, eeComponentClass, specification);

    }

    @Override public T build() {
        return eeComponentRole;
    }

    private T eeComponentRole(final String identification,
                              final Class<T> eeComponentClass,
                              final VecEEComponentSpecification specification) {

        try {
            final T eeComponentRole = eeComponentClass.getConstructor().newInstance();
            eeComponentRole.setIdentification(identification);
            eeComponentRole.setEEComponentSpecification(specification);
            eeComponentRole.getHousingComponentReves().addAll(
                    specification.getHousingComponents()
                            .stream()
                            .map(this::toHousingComponentReference)
                            .toList());

            return eeComponentRole;
        } catch (final InstantiationException | NoSuchMethodException | IllegalAccessException |
                       InvocationTargetException e) {
            throw new VecScriptingException("Error creating EEComponentRole", e);
        }
    }

    public AbstractEEComponentRoleBuilder<T> withComponentNode(final String componentNode) {
        final VecComponentNode node = componentNodeLookup.find(componentNode);
        eeComponentRole.getComponentNode().add(node);

        for (final VecHousingComponentReference housing : eeComponentRole.getHousingComponentReves()) {
            final VecComponentConnector connector = node.getComponentConnectors()
                    .stream()
                    .filter(c -> housing.getIdentification().equals(c.getIdentification()))
                    .findFirst()
                    .orElseThrow();

            housing.getComponentConnector().add(connector);

            for (final VecPinComponentReference pin : housing.getPinComponentReves()) {
                final VecComponentPort port = connector.getComponentPorts()
                        .stream()
                        .filter(p -> pin.getIdentification().equals(p.getIdentification()))
                        .findFirst()
                        .orElseThrow();

                pin.getTerminalRole().getComponentPort().add(port);
            }
        }

        return this;
    }

    private VecHousingComponentReference toHousingComponentReference(final VecHousingComponent housingComponent) {
        final VecHousingComponentReference housingComponentReference = new VecHousingComponentReference();
        housingComponentReference.setIdentification(housingComponent.getIdentification());
        housingComponentReference.setHousingComponent(housingComponent);

        session.addXmlComment(housingComponentReference, "Embedded ConnectorHousingRole omitted.");
        housingComponentReference.getPinComponentReves()
                .addAll(housingComponent.getPinComponents()
                                .stream()
                                .map(this::toPinComponentReference)
                                .toList());

        return housingComponentReference;
    }

    private VecPinComponentReference toPinComponentReference(final VecPinComponent pinComponent) {
        final VecPinComponentReference pinComponentReference = new VecPinComponentReference();
        pinComponentReference.setIdentification(pinComponent.getIdentification());
        pinComponentReference.setPinComponent(pinComponent);

        final VecTerminalRole terminalRole = new VecBoltTerminalRole();
        terminalRole.setIdentification(pinComponentReference.getIdentification());
        terminalRole.setTerminalSpecification(pinComponent.getPinSpecification());
        pinComponentReference.setTerminalRole(terminalRole);

        return pinComponentReference;
    }

}
