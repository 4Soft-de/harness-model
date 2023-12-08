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

import com.foursoft.harness.vec.scripting.EEComponentRoleBuilder;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.v2x.VecPrimaryPartType;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class NewModellingApproachEeComponentTest {

    @Test
    void create_ee_component_with_schematic() throws IOException {
        VecSession session = new VecSession();

        // @formatter:off
        session.component("EE-COMP", "DRAW-EE-COMP", VecPrimaryPartType.EE_COMPONENT, comp -> comp
            .addGeneralTechnicalPart()
            .addSchematic(schematic -> schematic
                  .addComponentNode("EE-COMP", node -> node
                          .addPort("A","1","Power")
                          .addPort("A","2","F1")
                          .addPort("A","3","Switch-A")
                          .addPort("A","4","Switch-B")
                          .addPort("A","5", "Switched+F2")
                          .addPort("F2","1","F2 - Pluggable")
                          .addPort("F2","2", "F2 - Pluggable")
                          .addChildNode("F1", f1 -> f1
                                  .addPort("A","1")
                                  .addPort("A","2"))

                          .addChildNode("R1", r1 -> r1
                                  .addPort("A","1","Switch-A")
                                  .addPort("A","2", "Switch-B")
                                  .addPort("A","3", "Switched-A")
                                  .addPort("A","4", "Switched-B")))
                  .addConnection("POWER", power -> power
                          .addEnd("EE-COMP","A","1",false)
                          .addEnd("EE-COMP","F2","1", false)
                          .addEnd("F1","A","1"))
                  .addConnection("F1-Limited", f1 -> f1
                          .addEnd("EE-COMP","A","2")
                          .addEnd("F1","A","2"))
                  .addConnection("F2-Limited",f2->f2
                          .addEnd("EE-COMP","F2","2")
                          .addEnd("R1","A","4"))
                  .addConnection("F2-Limited-Switched", f2 -> f2
                          .addEnd("EE-COMP","A","5")
                          .addEnd("R1","A","3"))
                  .addConnection("R1-Switch-A", r1 -> r1
                          .addEnd("EE-COMP","A","3")
                          .addEnd("R1","A","1"))
                  .addConnection("R1-Switch-B", r1 -> r1
                          .addEnd("EE-COMP","A","4")
                          .addEnd("R1","A","2")))
            .addEEComponentSpecification(builder -> builder
                .addHousingComponent("A", hc -> hc
                    .addPinComponents("1","2","3","4","5"))
                .addHousingComponent("F2", hc -> hc
                    .addPinComponents("1","2")))
            .addCoreSpecification("BUSBAR", spec -> spec.withCSA(5.0))
            .addWireSpecificationForPartUsage("BUSBAR", ws -> ws
                    .withWireElement("BUSBAR", we -> we
                        .withCoreSpecification("BUSBAR")))
            .addVirtualPartStructure(bom -> bom
                .addPartUsage("POWER", pu -> pu
                    .addWireSpecification("WS-BUSBAR", wr -> wr
                        .wireElementRef("BUSBAR", ref -> ref.withConnection("POWER"))))
                .addPartUsage("F1-Limited", pu -> pu
                        .addWireSpecification("WS-BUSBAR", wr -> wr
                .wireElementRef("BUSBAR", ref -> ref.withConnection("F1-Limited"))))
                .addPartUsage("F2-Limited", pu -> pu
                        .addWireSpecification("WS-BUSBAR", wr -> wr
                                .wireElementRef("BUSBAR", ref -> ref.withConnection("F2-Limited"))))
                .addPartUsage("F2-Limited-Switched", pu -> pu
                        .addWireSpecification("WS-BUSBAR", wr -> wr
                                .wireElementRef("BUSBAR", ref -> ref.withConnection("F2-Limited-Switched"))))
                .addPartUsage("R1-Switch-A", pu -> pu
                        .addWireSpecification("WS-BUSBAR", wr -> wr
                                .wireElementRef("BUSBAR", ref -> ref.withConnection("R1-Switch-A"))))
                .addPartUsage("R1-Switch-B", pu -> pu
                        .addWireSpecification("WS-BUSBAR", wr -> wr
                                .wireElementRef("BUSBAR", ref -> ref.withConnection("R1-Switch-B"))))
                .addPartUsage("F1", pu -> {})
                .addPartUsage("R1", pu -> {})
            ));



        session.document("SCHEMATIC","1", builder -> {});

        session.schematic("SCHEMATIC", schematic -> schematic
                .addComponentNode("A200", a200 -> a200
                .addPort("A","1","Power")
                .addPort("A","2","F1")
                .addPort("A","3","Switch-A")
                .addPort("A","4","Switch-B")
                .addPort("A","5", "Switched+F2")
                .addPort("F2","1","F2 - Pluggable")
                .addPort("F2","2", "F2 - Pluggable")));

        session.harness("HARNESS-1","1", harness -> harness
                .withSchematic("SCHEMATIC")
                .addPartOccurrence("A200*1","EE-COMP", occ -> occ
                    .defineRole(EEComponentRoleBuilder.class, role -> role
                            .withComponentNode("A200"))));
        // @formatter:on

        System.out.println(session.writeToString());
        session.writeToStream(TestUtils.createTestFileStream("ee-component-with-schematic"));
    }
}
