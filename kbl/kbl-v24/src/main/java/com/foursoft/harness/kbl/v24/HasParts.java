/*-
 * ========================LICENSE_START=================================
 * KBL 2.4
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
package com.foursoft.harness.kbl.v24;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface HasParts {

    List<KblAccessory> getAccessories();

    List<KblAssemblyPart> getAssemblyParts();

    List<KblCavityPlug> getCavityPlugs();

    List<KblCavitySeal> getCavitySeals();

    List<KblCoPackPart> getCoPackParts();

    List<KblComponent> getComponents();

    List<KblComponentBox> getComponentBoxes();

    List<KblConnectorHousing> getConnectorHousings();

    List<KblFixing> getFixings();

    List<KblGeneralTerminal> getGeneralTerminals();

    List<KblGeneralWire> getGeneralWires();

    List<KblWireProtection> getWireProtections();

    default List<KblPart> getParts() {
        return Stream.of(getAccessories(),
                         getAssemblyParts(),
                         getCavityPlugs(),
                         getCavitySeals(),
                         getCoPackParts(),
                         getComponents(),
                         getComponentBoxes(),
                         getConnectorHousings(),
                         getFixings(),
                         getGeneralTerminals(),
                         getGeneralWires(),
                         getWireProtections())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
