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
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

class VecSamplesCreationTest {

    private OutputStream createTestFileStream(String testcase) throws IOException {
        Path dir = FileSystems.getDefault().getPath(".", "target", "samples");

        Files.createDirectories(dir);

        return Files.newOutputStream(dir.resolve(testcase + ".vec"), StandardOpenOption.CREATE);
    }

    @Test
    void ves_wf_changes_sample_pre() throws IOException {
        VecSession session = new VecSession();

        createCommonBase(session, "SL1", "N_018_886_1", "1");

        session.writeToStream(createTestFileStream("ves-wf-changes-sample-pre"));
    }

    @Test
    void ves_wf_changes_sample_post() throws IOException {
        VecSession session = new VecSession();

        createCommonBase(session, "SL3", "3C0_972_100", "2");

//        session.writeToStream(createTestFileStream("ves-wf-changes-sample-post"));

    }

    private static void createCommonBase(final VecSession session, final String specialWireId,
                                         final String specialWirePartNumber, final String harnessVersion) {
        // @formatter:off
        session.component( "CON-A", "DRAW-CON-A",
                                       VecPrimaryPartType.CONNECTOR_HOUSING)
                .addGeneralTechnicalPart()
                    .end()
                .addConnectorHousing()
                    .addCavity("A", "1")
                    .addCavity("A", "2")
                    .end()
                .end();

        session.component( "CON-B", "DRAW-CON-B",
                                       VecPrimaryPartType.CONNECTOR_HOUSING)
                .addGeneralTechnicalPart()
                    .end()
                .addConnectorHousing()
                    .addCavity("A", "1")
                    .end()
                .end();

        session.component( "CON-C", "DRAW-CON-B",
                                       VecPrimaryPartType.CONNECTOR_HOUSING)
                .addGeneralTechnicalPart()
                    .end()
                .addConnectorHousing()
                    .addCavity("A", "1")
                    .end()
                .end();

        session.component( "N_103_361_01", "DRAW-TERM-N_103_361_01",
                                                                                  VecPrimaryPartType.PLUGGABLE_TERMINAL)
                .addGeneralTechnicalPart()
                    .end()
                .addPluggableTerminal()
                    .end()
                .end();

        session.component( "036_911_137", "DRAW-TERM-036_911_137",
                                                                                  VecPrimaryPartType.PLUGGABLE_TERMINAL)
                .addGeneralTechnicalPart()
                    .end()
                .addPluggableTerminal()
                    .end()
                .end();

        session.component( "N_018_886_1", "DRAW-N_018_886_1",VecPrimaryPartType.WIRE)
                .addGeneralTechnicalPart()
                    .end()
                .addCoreSpecification("FL-A-0.5")
                    .withCSA(0.5)
                    .end()
                .addWireSpecification()
                    .withWireElement("ROOT")
                        .addSubWireElement("1")
                            .withCoreSpecification("FL-A-0.5")
                                .addInsulationSpecification("FLRY-BR")
                                .withColor("BR")
                            .end()
                        .end()
                        .addSubWireElement("2")
                            .withCoreSpecification("FL-A-0.5")
                            .addInsulationSpecification("FLRY-SW")
                                .withColor("SW")
                                .end()
                            .end()
                        .end()
                    .end()
                .end();

        session.component("3C0_972_100", "DRAW-WIR-3C0_972_100", VecPrimaryPartType.WIRE)
                .addGeneralTechnicalPart()
                    .end()
                .addCoreSpecification("FL-A-0.3")
                    .withCSA(0.3)
                    .end()
                .addWireSpecification()
                    .withWireElement("ROOT")
                        .addSubWireElement("1")
                            .withCoreSpecification("FL-A-0.3")
                            .addInsulationSpecification("FLRY-BK")
                                .withColor("B/K")
                                .end()
                            .end()
                        .addSubWireElement("2")
                            .withCoreSpecification("FL-A-0.3")
                            .addInsulationSpecification("FLRY-BKII")
                                .withColor("B/K")
                                .end()
                            .end()
                        .end()
                    .end()
                .end();

        session.harness("HARNESS-1",harnessVersion)
                .addPartOccurrence("XA.F2.1_1","CON-A")
                    .end()
                .addPartOccurrence("XA.44.1","CON-B")
                    .end()
                .addPartOccurrence("YW001","CON-C")
                    .end()
                .addPartOccurrence(specialWireId, specialWirePartNumber)
                    .roleBuilder(WireRoleBuilder.class)
                        .wireElementRef("ROOT")
                            .withIdentification(specialWireId)
                            .end()
                        .wireElementRef("1")
                            .withIdentification("264002")
                            .withLength(1063.0)
                            .end()
                        .wireElementRef("2")
                            .withIdentification("264001")
                            .withLength(503.0)
                            .end()
                        .end()
                    .end()
                .addConnection("264002")
                    .addEnd("XA.F2.1_1","2")
                    .addEnd("XA.44.1","1")
                    .end()
                .addConnection("264001")
                    .addEnd("YW001","1")
                    .addEnd("XA.F2.1_1","1")
                    .end()
                .addVariant("VWK_970_264_A", "XA.F2.1_1","XA.44.1","YW001",  specialWireId)
                    .withAbbreviation("V1")
                    .withConfiguration("L0L+1CR+QQ5+9GM")
                    .end()
                .end();

        // @formatter:on

    }

}
