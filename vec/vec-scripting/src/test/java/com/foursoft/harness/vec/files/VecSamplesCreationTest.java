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
package com.foursoft.harness.vec.files;

import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.WireRoleBuilder;
import com.foursoft.harness.vec.v2x.VecPrimaryPartType;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class VecSamplesCreationTest {

    @Test
    void ves_wf_changes_sample_pre() throws IOException {
        VecSession session = new VecSession();

        createCommonBase(session, "SL1", "N_018_886_1", "1");

        session.writeToStream(TestUtils.createTestFileStream("ves-wf-changes-sample-pre"));
    }

    @Test
    void ves_wf_changes_sample_post() throws IOException {
        VecSession session = new VecSession();

        createCommonBase(session, "SL3", "3C0_972_100", "2");

        session.writeToStream(TestUtils.createTestFileStream("ves-wf-changes-sample-post"));
    }

    private static void createCommonBase(final VecSession session, final String specialWireId,
                                         final String specialWirePartNumber, final String harnessVersion) {
        // @formatter:off
        session.component( "CON-A", "DRAW-CON-A",
                                       VecPrimaryPartType.CONNECTOR_HOUSING, comp -> comp
                .addGeneralTechnicalPart()
                .addConnectorHousing(builder -> builder
                    .addCavity("A", "1")
                    .addCavity("A", "2"))
        );

        session.component( "CON-B", "DRAW-CON-B",
                                       VecPrimaryPartType.CONNECTOR_HOUSING, comp -> comp
                .addGeneralTechnicalPart()
                .addConnectorHousing(builder -> builder
                    .addCavity("A", "1"))
        );

        session.component( "CON-C", "DRAW-CON-B",
                                       VecPrimaryPartType.CONNECTOR_HOUSING, comp -> comp
                .addGeneralTechnicalPart()
                .addConnectorHousing(builder -> builder
                    .addCavity("A", "1"))
        );

        session.component( "N_103_361_01", "DRAW-TERM-N_103_361_01", VecPrimaryPartType.PLUGGABLE_TERMINAL,
                comp -> comp.addGeneralTechnicalPart()
                .addPluggableTerminal()
        );

        session.component( "036_911_137", "DRAW-TERM-036_911_137", VecPrimaryPartType.PLUGGABLE_TERMINAL, comp -> comp
                .addGeneralTechnicalPart()
                .addPluggableTerminal()
        );

        session.component( "N_018_886_1", "DRAW-N_018_886_1",VecPrimaryPartType.WIRE, comp -> comp
                .addGeneralTechnicalPart()
                .addCoreSpecification("FL-A-0.5", spec -> spec.withCSA(0.5))
                .addWireSpecification(ws -> ws
                    .withWireElement("ROOT", root -> root
                        .addSubWireElement("1", sub -> sub
                            .withCoreSpecification("FL-A-0.5")
                                .addInsulationSpecification("FLRY-BR", spec -> spec.withColor("BR"))
                        )
                        .addSubWireElement("2", sub -> sub
                            .withCoreSpecification("FL-A-0.5")
                            .addInsulationSpecification("FLRY-SW",  spec -> spec.withColor("SW"))
                        )
                    )
                ));


        session.component("3C0_972_100", "DRAW-WIR-3C0_972_100", VecPrimaryPartType.WIRE, comp -> comp
                .addGeneralTechnicalPart()
                .addCoreSpecification("FL-A-0.3", spec -> spec.withCSA(0.3))
                .addWireSpecification(ws -> ws
                    .withWireElement("ROOT", root -> root
                        .addSubWireElement("1", sub -> sub
                            .withCoreSpecification("FL-A-0.3")
                            .addInsulationSpecification("FLRY-BK", spec -> spec.withColor("B/K"))
                        )
                        .addSubWireElement("2", sub -> sub
                            .withCoreSpecification("FL-A-0.3")
                            .addInsulationSpecification("FLRY-BKII",spec -> spec.withColor("B/K"))
                        )
                    )
                ));

        session.harness("HARNESS-1",harnessVersion, harness -> harness
                .addPartOccurrence("XA.F2.1_1","CON-A")
                .addPartOccurrence("XA.44.1","CON-B")
                .addPartOccurrence("YW001","CON-C")
                .addPartOccurrence(specialWireId, specialWirePartNumber, occ -> occ
                    .defineRole(WireRoleBuilder.class, role -> role
                        .wireElementRef("ROOT", ref -> ref
                            .withIdentification(specialWireId))
                        .wireElementRef("1", ref -> ref
                            .withIdentification("264002")
                            .withLength(1063.0))
                        .wireElementRef("2", ref -> ref
                            .withIdentification("264001")
                            .withLength(503.0))
                    )
                )

                .addConnection("264002", conn -> conn
                    .addEnd("XA.F2.1_1","2")
                    .addEnd("XA.44.1","1"))
                .addConnection("264001", conn-> conn
                    .addEnd("YW001","1")
                    .addEnd("XA.F2.1_1","1"))
                .addVariant("VWK_970_264_A", var -> var
                    .withAbbreviation("V1")
                    .withConfiguration("L0L+1CR+QQ5+9GM")
                    .withOccurrences("XA.F2.1_1","XA.44.1","YW001",  specialWireId)));

        // @formatter:on
    }

    @Test
    void create_arena_sample() throws IOException {
        VecSession session = new VecSession();

        // @formatter:off
        session.component("5-928999-1", "5-928999-1",
                          VecPrimaryPartType.PLUGGABLE_TERMINAL, comp -> comp
                .withApplicationSpecification("114-18021","V")
                .addGeneralTechnicalPart()
                .addPluggableTerminal(builder -> builder
                    .withMissingAttributesComment()
                    .withInsulationDisplacementLength(3.6,-0.15,0.15)
                    .withConnectionBLength(5.6)
                    .withTerminalLengthOverall(13.7)
                    .withRearBellMouth(0.55)
                    .withCrimpDetailsExample()
                    .withSealable(false))
        );

        session.component("WIRE","DRAW-WIRE", VecPrimaryPartType.WIRE, comp -> comp
                .addGeneralTechnicalPart(builder -> builder
                    .withMassInformation(4.6,session.gramPerMeter()))
                .addSingleCore(wire -> wire
                    .withDin76722WireType("FLRY-A")
                    .withCSA(0.35)
                    .withOutsideDiameter(1.3,-0.1,0.0)
                    .withInsulationThickness(0.20)
                    .withNumberOfStrands(7)
                    .withStrandDiameter(0.27)
                )
        );

        session.harness("LEADSET", "1", harness -> harness
                .addPartOccurrence("W1", "WIRE", occ -> occ
                    .defineRole(WireRoleBuilder.class, role -> role
                        .wireElementRef("1", ref -> ref
                            .withIdentification("W1")
                            .withLength(1000.0))
                    )
                )
                .addPartOccurrence("T1", "5-928999-1")
                .addPartOccurrence("T2", "5-928999-1")
                .addConnection("W1", conn -> conn
                    .addEndWithTerminalOnly("T1")
                    .addEndWithTerminalOnly("T2")));
        // @formatter:on

        ;

        session.writeToStream(TestUtils.createTestFileStream("detail-arena2036-example"));
    }

}
