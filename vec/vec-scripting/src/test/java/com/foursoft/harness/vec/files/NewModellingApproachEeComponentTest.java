package com.foursoft.harness.vec.files;

import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.core.DocumentVersionBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class NewModellingApproachEeComponentTest {

    @Test
    void create_ee_component_with_schematic() throws IOException {
        VecSession session = new VecSession();

        DocumentVersionBuilder dvb = session.document("DRAW-EE-COMP", "1");
        // @formatter:off
        session.schematic(dvb)
                .addComponentNode("EECOMP")
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
                    .addEnd("EECOMP","A","1",false)
                    .addEnd("EECOMP","F2","1", false)
                    .addEnd("F1","A","1")
                .end()
                .addConnection("F1-Limited")
                    .addEnd("EECOMP","A","2")
                    .addEnd("F1","A","2")
                .end()
                .addConnection("F2-Limited")
                    .addEnd("EECOMP","F2","2")
                    .addEnd("R1","A","4")
                .end()
                .addConnection("F2-Limited-Switched")
                    .addEnd("EECOMP","A","5")
                    .addEnd("R1","A","3")
                .end()
                .addConnection("R1-Switch-A")
                    .addEnd("EECOMP","A","3")
                    .addEnd("R1","A","1")
                .end()
                .addConnection("R1-Switch-B")
                    .addEnd("EECOMP","A","4")
                    .addEnd("R1","A","2")
                .end()
            .end();
        // @formatter:on
        System.out.println(session.writeToString());
        session.writeToStream(TestUtils.createTestFileStream("ee-component-with-schematic"));
    }
}
