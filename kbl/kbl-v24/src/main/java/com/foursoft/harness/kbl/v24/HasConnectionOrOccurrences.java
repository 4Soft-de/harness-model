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

import com.foursoft.harness.kbl.common.util.StreamUtils;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface HasConnectionOrOccurrences {

    List<KblAccessoryOccurrence> getAccessoryOccurrences();

    List<KblAssemblyPartOccurrence> getAssemblyPartOccurrences();

    List<KblCavityPlugOccurrence> getCavityPlugOccurrences();

    List<KblCavitySealOccurrence> getCavitySealOccurrences();

    List<KblCoPackOccurrence> getCoPackOccurrences();

    List<KblComponentBoxOccurrence> getComponentBoxOccurrences();

    List<KblComponentOccurrence> getComponentOccurrences();

    List<KblConnection> getConnections();

    List<KblConnectorOccurrence> getConnectorOccurrences();

    List<KblFixingOccurrence> getFixingOccurrences();

    List<KblGeneralWireOccurrence> getGeneralWireOccurrences();

    List<KblSpecialTerminalOccurrence> getSpecialTerminalOccurrences();

    List<KblTerminalOccurrence> getTerminalOccurrences();

    List<KblWireProtectionOccurrence> getWireProtectionOccurrences();

    List<KblWiringGroup> getWiringGroups();

    default List<ConnectionOrOccurrence> getConnectionOrOccurrences() {
        return Stream.of(getOccurrences(),
                         getAssemblyPartOccurrences().stream(),
                         getConnections().stream(),
                         getWiringGroups().stream())
                .flatMap(Function.identity())
                .flatMap(StreamUtils.ofClass(ConnectionOrOccurrence.class))
                .toList();
    }

    default List<HasRelatedAssembly> getHasRelatedAssemblyOccurrences() {
        return getOccurrences()
                .flatMap(StreamUtils.ofClass(HasRelatedAssembly.class))
                .toList();
    }

    private Stream<?> getOccurrences() {
        return Stream.of(getAccessoryOccurrences(),
                         getCavityPlugOccurrences(),
                         getCavitySealOccurrences(),
                         getCoPackOccurrences(),
                         getComponentBoxOccurrences(),
                         getComponentOccurrences(),
                         getConnectorOccurrences(),
                         getFixingOccurrences(),
                         getGeneralWireOccurrences(),
                         getSpecialTerminalOccurrences(),
                         getTerminalOccurrences(),
                         getWireProtectionOccurrences())
                .flatMap(Collection::stream);
    }

}
