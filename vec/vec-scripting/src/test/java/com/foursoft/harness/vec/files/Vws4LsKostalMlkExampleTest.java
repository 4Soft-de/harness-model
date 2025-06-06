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
package com.foursoft.harness.vec.files;

import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.enums.TemperatureType;
import com.foursoft.harness.vec.scripting.enums.WireReceptionType;
import com.foursoft.harness.vec.v2x.VecPrimaryPartType;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.foursoft.harness.vec.scripting.factories.LocalizedStringFactory.de;

class Vws4LsKostalMlkExampleTest {

    @Test
    void createMlk12Sm() throws IOException {
        final VecSession session = new VecSession();
        session.getDefaultValues().setCompanyName("Kostal");
        session.getDefaultValues().setColorReferenceSystem("Kostal");
        session.getDefaultValues().setMaterialReferenceSystem("Kostal");

        session.part("10322345", VecPrimaryPartType.PLUGGABLE_TERMINAL, part -> {
        });

        session.component("32124733993", "DOC00079128-04", VecPrimaryPartType.PLUGGABLE_TERMINAL, comp -> comp
                .withPartAbbreviation(de("MLK 1,2 Sm - Standard"))
                .withApplicationSpecification("DOC00061540", "22")
                .addConductorSpecification("CORE-FL-A", core -> core
                        .withDin76722WireType("FL-A")
                        .withCSA(0.14)
                        .withStructure("Symmetric"))
                .addInsulationSpecification("INS-FLR", ins -> ins
                        .withThickness(0.20)
                        .withDin76722WireType("FLR")
                        .withInsulationMaterial("PVC")
                )

                .addGeneralTechnicalPart(gen -> gen
                        .withMaterialInformation("CuSn")
                        .withTemperatureInformation(TemperatureType.OPERATING_TEMPERATURE, Double.NaN, 150,
                                                    session.degreeCelsius())
                )
                .addPluggableTerminal(plug -> plug
                        .withTerminalCurrentInformation(1.5, 12, 0, 23, 20)
                        .withTerminalCurrentInformation(1.5, 12, 0, 17, 80)
                        .withPullOutForce(60.0)
                        .withTerminalType("MLK", "1.2x0.6")
                        .withPlatingMaterialTerminalReception("Ag3")
                        .withPlatingMaterialWireReception("frSn0,8-2")
                        .withCoreCrossSectionArea(0.12, 0.14)
                        .withWireElementOutsideDiameter(0.85, 1.2)
                        .withWireReceptionType(WireReceptionType.CRIMP)
                        .withTerminalLengthOverall(15, 0.3, 0.3)
                        .withInsulationCrimpLength(2.0, 0.2)
                        .withInsulationCrimpLegHeight(1.85, 0.2)
                        .withConductorCrimpLength(2.6, 0.2)
                        .withConductorCrimpLegHeight(1.55, 0.2)
                        .withCrimpConnectionLength(5.8, 0.2)
                        .withContactRangeLength(7.3, -0.05, 0.1)
                        .withInsulationDisplacementLength(3.1, -0.5, 0.5)
                        .withSealable(false)
                        .addCoreCrimpDetails("CORE-FL-A", core -> core
                                .withHeight(0.74, -0.03, 0.03)
                                .withWidth(1.06, -0.03, 0.03)
                                .addInsulationCrimpDetails("INS-FLR", ins -> ins
                                        .withHeight(1.30, -0.1, 0.1)
                                        .withWidth(1.61, -0.1, 0.1)
                                )
                        )

                )
                .addTerminalPairing("10322345", pairing -> pairing
                        .withMatingForce(3.4, 1.0, 1.0)
                        .withUnMatingForce(3.4, 1.0, 1.0)
                        .withNumberOfMatingCycles(20)
                )
        );

        session.writeToStream(TestUtils.createTestFileStream("kostal-32124733993"));

    }

}
