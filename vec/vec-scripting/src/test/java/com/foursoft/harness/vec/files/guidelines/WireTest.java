package com.foursoft.harness.vec.files.guidelines;

import com.foursoft.harness.vec.files.TestUtils;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.v2x.VecPrimaryPartType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class WireTest {

    @Test
    void create_single_core() {
        final VecSession session = new VecSession();

        session.component("WIRE", "DRAW-WIRE", VecPrimaryPartType.WIRE, comp -> comp
                .addGeneralTechnicalPart(builder -> builder
                        .withMassInformation(4.6, session.gramPerMeter()))
                .addSingleCore(wire -> wire
                        .withDin76722WireType("FLRY-A")
                        .withCSA(0.35)
                        .withOutsideDiameter(1.3, -0.1, 0.0)
                        .withInsulationThickness(0.20)
                        .withNumberOfStrands(7)
                        .withStrandDiameter(0.27)
                )
        );

        assertThatCode(() -> {
            session.writeToStream(TestUtils.createTestFileStream("ecad-wiki-guideline-single-core"));
        }).doesNotThrowAnyException();
    }

}
