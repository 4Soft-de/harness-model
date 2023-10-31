package com.foursoft.harness.vec.files;

import com.foursoft.harness.vec.scripting.ComponentMasterDataBuilder;
import com.foursoft.harness.vec.scripting.EEComponentRoleBuilder;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.core.DocumentVersionBuilder;
import com.foursoft.harness.vec.scripting.schematic.SchematicBuilder;
import com.foursoft.harness.vec.v2x.VecPrimaryPartType;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class NewModellingApproachEeComponentTest {

    @Test
    void create_ee_component_with_schematic() throws IOException {
        VecSession session = new VecSession();

        // @formatter:off
        ComponentMasterDataBuilder component =
                session.component("EE-COMP", "DRAW-EE-COMP", VecPrimaryPartType.EE_COMPONENT)
                        .addGeneralTechnicalPart().end()
                        .addEEComponentSpecification()
                            .addHousingComponent("A")
                                .addPinComponents("1","2","3","4","5")
                            .end()
                            .addHousingComponent("F2")
                                .addPinComponents("1","2")
                            .end()
                        .end();


        session.schematic(component.partMasterDocument())
                .addComponentNode("EE-COMP")
                    .addPort("A","1","Power")
                    .addPort("A","2","F1")
                    .addPort("A","3","Switch-A")
                    .addPort("A","4","Switch-B")
                    .addPort("A","5", "Switched+F2")
                    .addPort("F2","1","F2 - Pluggable")
                    .addPort("F2","2", "F2 - Pluggable")
                    .addChildNode("F1")
                        .addPort("A","1")
                        .addPort("A","2")
                    .end()
                    .addChildNode("R1")
                        .addPort("A","1","Switch-A")
                        .addPort("A","2", "Switch-B")
                        .addPort("A","3", "Switched-A")
                        .addPort("A","4", "Switched-B")
                    .end()
                .end()
                .addConnection("POWER")
                    .addEnd("EE-COMP","A","1",false)
                    .addEnd("EE-COMP","F2","1", false)
                    .addEnd("F1","A","1")
                .end()
                .addConnection("F1-Limited")
                    .addEnd("EE-COMP","A","2")
                    .addEnd("F1","A","2")
                .end()
                .addConnection("F2-Limited")
                    .addEnd("EE-COMP","F2","2")
                    .addEnd("R1","A","4")
                .end()
                .addConnection("F2-Limited-Switched")
                    .addEnd("EE-COMP","A","5")
                    .addEnd("R1","A","3")
                .end()
                .addConnection("R1-Switch-A")
                    .addEnd("EE-COMP","A","3")
                    .addEnd("R1","A","1")
                .end()
                .addConnection("R1-Switch-B")
                    .addEnd("EE-COMP","A","4")
                    .addEnd("R1","A","2")
                .end()
            .end();

        DocumentVersionBuilder dvb = session.document("SCHEMATIC","1");

        SchematicBuilder schematic = session.schematic(dvb).addComponentNode("A200")
                .addPort("A","1","Power")
                .addPort("A","2","F1")
                .addPort("A","3","Switch-A")
                .addPort("A","4","Switch-B")
                .addPort("A","5", "Switched+F2")
                .addPort("F2","1","F2 - Pluggable")
                .addPort("F2","2", "F2 - Pluggable")
                .end();

        session.harness("HARNESS-1","1")
                .withSchematic(schematic)
                .addPartOccurrence("A200*1","EE-COMP")
                .roleBuilder(EEComponentRoleBuilder.class)
                    .withComponentNode("A200");
        // @formatter:on

        System.out.println(session.writeToString());
        session.writeToStream(TestUtils.createTestFileStream("ee-component-with-schematic"));
    }
}
