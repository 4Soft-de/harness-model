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
package com.foursoft.harness.vec.files.guidelines;

import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.enums.DocumentType;
import com.foursoft.harness.vec.scripting.enums.SignalInformationType;
import com.foursoft.harness.vec.scripting.enums.SignalSubType;
import com.foursoft.harness.vec.scripting.enums.SignalType;
import org.junit.jupiter.api.Test;

class ArchitecturalLayerTest {

    /**
     * Sample for ECAD-WIKI Architectural Layer guideline
     * <a href="https://ecad-wiki.prostep.org/specifications/vec/guidelines/elog-layers/architecture/">here</a>.
     *
     */
    @Test
    void test() {
        final VecSession session = new VecSession();

        session.document("1234567", "a", dv -> {
            dv.documentType(DocumentType.NETWORK_ARCHITECTURE);
        });

        session.networkArchitecture("1234567", na -> {
            na.addNetType("12V-Power", nt -> {
                        nt.withSignalType(SignalType.ENERGY);
                    })
                    .addNetType("HAL", nt -> {
                        nt.withSignalType(SignalType.INFORMATION)
                                .withSignalInformationType(SignalInformationType.ANALOG);
                    })
                    .addNetType("CAN", nt -> {
                        nt.withSignalType(SignalType.INFORMATION)
                                .withSignalInformationType(SignalInformationType.DIGITAL)
                                .withSignalSubType(SignalSubType.CAN);
                    });

            na.addNetworkNode("BCM", nn -> {
                nn.addPort("CAN1", "CAN");
            }).addNetworkNode("DMD", nn -> {
                nn.addPort("CAN", "CAN")
                        .addPort("PWED-HAL", "HAL")
                        .addPort("PWED-Engine", "12V-Power");

            }).addNetworkNode("PWED", nn -> {
                nn.addPort("HAL", "HAL")
                        .addPort("Engine", "12V-Power");
            });

            na.addNet("PWED-Engine", nn -> {
                        nn.withNetType("12V-Power")
                                .addPort("PWED", "Engine")
                                .addPort("DMD", "PWED-Engine");
                    })
                    .addNet("HAL", nn -> {
                        nn.withNetType("HAL")
                                .addPort("PWED", "HAL")
                                .addPort("DMD", "PWED-HAL");
                    })
                    .addNet("Body-CAN", nn -> {
                        nn.withNetType("CAN")
                                .addPort("BCM", "CAN1")
                                .addPort("DMD", "CAN");
                    });

        });

        session.writeToStream(System.out);
    }
}
