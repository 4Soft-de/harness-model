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
package com.foursoft.harness.vec.scripting;

import com.foursoft.harness.vec.scripting.schematic.ConnectionLookup;
import com.foursoft.harness.vec.v2x.VecWireElement;
import com.foursoft.harness.vec.v2x.VecWireElementReference;
import com.foursoft.harness.vec.v2x.VecWireRole;
import com.foursoft.harness.vec.v2x.VecWireSpecification;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WireRoleBuilder implements Builder<VecWireRole> {

    private final VecWireRole wireRole;
    private final VecSession session;
    private final ConnectionLookup connectionLookup;
    private Map<String, WireElementRefBuilder> wireElementRefBuilders;

    public WireRoleBuilder(VecSession session, String identification,
                           VecWireSpecification specification, ConnectionLookup connectionLookup) {
        this.session = session;
        this.connectionLookup = connectionLookup;
        this.wireRole = wireRole(identification, specification);

    }

    public WireRoleBuilder wireElementRef(String partMasterIdentification,
                                          Customizer<WireElementRefBuilder> customizer) {
        WireElementRefBuilder builder = wireElementRefBuilders.get(partMasterIdentification);

        if (customizer != null) {
            customizer.customize(builder);
        }

        return this;
    }

    @Override
    public VecWireRole build() {
        List<VecWireElementReference> list = wireElementRefBuilders.values().stream().map(WireElementRefBuilder::build)
                .toList();

        wireRole.getWireElementReferences().addAll(list);

        return wireRole;
    }

    private VecWireRole wireRole(String identification,
                                 VecWireSpecification specification) {

        VecWireRole role = new VecWireRole();
        role.setIdentification(identification);

        role.setWireSpecification(specification);

        wireElementRefBuilders = flatTree(specification.getWireElement()).collect(
                Collectors.toMap(VecWireElement::getIdentification,
                                 e -> new WireElementRefBuilder(session, e, connectionLookup)));

        return role;
    }

    private Stream<VecWireElement> flatTree(VecWireElement element) {
        return Stream.concat(Stream.of(element), element.getSubWireElements().stream().flatMap(this::flatTree));
    }

}
