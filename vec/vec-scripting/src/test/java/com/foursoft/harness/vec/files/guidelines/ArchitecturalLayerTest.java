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

import com.foursoft.harness.vec.files.TestUtils;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.enums.DocumentType;
import com.foursoft.harness.vec.scripting.enums.SignalInformationType;
import com.foursoft.harness.vec.scripting.enums.SignalSubType;
import com.foursoft.harness.vec.scripting.enums.SignalType;
import com.foursoft.harness.vec.v2x.validation.VecValidation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

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
                    .addNetType("HALL", nt -> {
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
                        .addPort("PWED-HALL", "HALL")
                        .addPort("PWED-Engine", "12V-Power");

            }).addNetworkNode("PWED", nn -> {
                nn.addPort("HALL", "HALL")
                        .addPort("Engine", "12V-Power");
            });

            na.addNet("PWED-Engine", nn -> {
                        nn.withNetType("12V-Power")
                                .addPort("PWED", "Engine")
                                .addPort("DMD", "PWED-Engine");
                    })
                    .addNet("HALL", nn -> {
                        nn.withNetType("HALL")
                                .addPort("PWED", "HALL")
                                .addPort("DMD", "PWED-HALL");
                    })
                    .addNet("Body-CAN", nn -> {
                        nn.withNetType("CAN")
                                .addPort("BCM", "CAN1")
                                .addPort("DMD", "CAN");
                    });

        });

        session.signals("1234567", signals -> {
            signals.withNetworkLayer("1234567")
                    .addSignal("PWED_HALL_GROUND", s -> {
                        s.withNetType("HALL")
                                .withSignalType(SignalType.GROUND);
                    })
                    .addSignal("PWED_HALL_IN", s -> {
                        s.withNetType("HALL")
                                .withSignalType(SignalType.ENERGY);
                    })
                    .addSignal("PWED_HALL_OUT", s -> {
                        s.withNetType("HALL")
                                .withSignalType(SignalType.INFORMATION)
                                .withSignalInformationType(SignalInformationType.ANALOG);
                    });
        });

        session.schematic("1234567", schematic -> {
            schematic.withNetworkLayer("1234567")
                    .withSignals("1234567")
                    .addComponentNode("DMD", cn -> {
                        cn.withNetworkNode("DMD")
                                .addPort("A", "1", p -> {
                                    p.withNetworkPort("PWED-HALL");
                                })
                                .addPort("A", "2", p -> {
                                    p.withNetworkPort("PWED-HALL");
                                })
                                .addPort("A", "3", p -> {
                                    p.withNetworkPort("PWED-HALL");
                                });
                    })
                    .addComponentNode("PWED", cn -> {
                        cn.withNetworkNode("PWED")
                                .addPort("A", "1", p -> {
                                    p.withSignal("PWED_HALL_GROUND")
                                            .withNetworkPort("HALL");
                                })
                                .addPort("A", "2", p -> {
                                    p.withSignal("PWED_HALL_IN")
                                            .withNetworkPort("HALL");
                                })
                                .addPort("A", "3", p -> {
                                    p.withSignal("PWED_HALL_OUT")
                                            .withNetworkPort("HALL");
                                });
                    }).addConnection("PWED_HALL_GROUND", cn -> {
                        cn.withNet("HALL")
                                .withSignal("PWED_HALL_GROUND")
                                .addEnd("PWED", "A", "1")
                                .addEnd("DMD", "A", "1");
                    }).addConnection("PWED_HALL_IN", cn -> {
                        cn.withNet("HALL")
                                .withSignal("PWED_HALL_IN")
                                .addEnd("PWED", "A", "2")
                                .addEnd("DMD", "A", "2");
                    }).addConnection("PWED_HALL_OUT", cn -> {
                        cn.withNet("HALL")
                                .withSignal("PWED_HALL_OUT")
                                .addEnd("PWED", "A", "3")
                                .addEnd("DMD", "A", "3");
                    });
        });

        assertThatCode(() -> {
            session.writeToStream(TestUtils.createTestFileStream("ecad-wiki-guideline-architectural-layer"));
            VecValidation.validateXML(session.writeToString(), System.out::println, true);
        }).doesNotThrowAnyException();

    }
}
