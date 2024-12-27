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

import com.foursoft.harness.vec.v2x.*;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Queries {

    private Queries() {
        throw new AssertionError();
    }

    public static VecCavity findCavity(VecConnectorHousingSpecification connectorHousingSpecification,
                                       String slotNumber, String cavityNumber) {
        return connectorHousingSpecification.getSlots()
                .stream()
                .filter(s -> slotNumber.equals(s.getSlotNumber()))
                .filter(VecSlot.class::isInstance)
                .map(VecSlot.class::cast)
                .flatMap(s -> s.getCavities().stream())
                .filter(c -> cavityNumber.equals(c.getCavityNumber()))
                .findAny()
                .orElseThrow(
                        notFoundException("Cavity", "SlotNumber: " + slotNumber + " CavityNumber: " + cavityNumber));
    }

    public static Locator<VecTopologyNode> nodeLocator(VecTopologySpecification topologySpecification) {
        return id -> findByValue(topologySpecification.getTopologyNodes(), VecTopologyNode::getIdentification, id,
                                 "TopologyNode");
    }

    public static Locator<VecPartOccurrence> partOccurrenceLocator(
            VecCompositionSpecification compositionSpecification) {
        return id ->
                findByValue(compositionSpecification.getComponents(), VecPartOccurrence::getIdentification, id,
                            "PartOccurrence");

    }

    private static <T, V> T findByValue(List<T> elements, Function<T, V> valueFn, V value, String elementType) {
        return elements.stream()
                .filter(element -> value.equals(valueFn.apply(element)))
                .findAny()
                .orElseThrow(notFoundException(elementType, value));

    }

    private static <T> Supplier<VecScriptingException> notFoundException(String elementType, T identification) {
        return () -> new VecScriptingException(
                "No '" + elementType + "' found with identification '" + identification + "'.");
    }

}
