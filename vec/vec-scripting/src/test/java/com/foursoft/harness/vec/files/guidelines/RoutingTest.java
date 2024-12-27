package com.foursoft.harness.vec.files.guidelines;

import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.components.ComponentMasterDataBuilder;
import com.foursoft.harness.vec.scripting.components.WireRoleBuilder;
import com.foursoft.harness.vec.scripting.harness.HarnessBuilder;
import com.foursoft.harness.vec.scripting.topology.TopologyBuilder;
import com.foursoft.harness.vec.v2x.VecPlacementType;
import com.foursoft.harness.vec.v2x.VecPrimaryPartType;
import org.junit.jupiter.api.Test;

public class RoutingTest {

    @Test
    public void test() {
        VecSession session = new VecSession();

        session.component("CON-A", "DRAW-CON-A", VecPrimaryPartType.CONNECTOR_HOUSING, this::customizeConA);
        session.component("WIRE-A", "DRAW-WIRE-A", VecPrimaryPartType.WIRE, this::customizeWire);

        session.harness("HARNESS-1", "a", this::customizeHarness);

        session.writeToStream(System.out);
    }

    private void customizeWire(ComponentMasterDataBuilder cmp) {
        cmp.addGeneralTechnicalPart()
                .addSingleCore(wire -> {
                    wire.withCSA(0.5).withColor("BL");
                });
    }

    public void customizeHarness(HarnessBuilder harness) {
        harness.withConfigManagement(config -> {
                    config.addConfigVariant("RL", "RL")
                            .addConfigVariant("LL", "LL");
                })
                .withTopology(this::customizeTopology)
                .addPartOccurrence("A1", "CON-A")
                .addPartOccurrence("A2", "CON-A")
                .addPartOccurrence("A3", "CON-A")
                .addPartOccurrence("W1", "WIRE-A", wire -> {
                    wire.defineRole(WireRoleBuilder.class, role -> role
                            .wireElementRef("1", ref -> ref
                                    .withIdentification("W1"))
                    );
                })
                .addPartOccurrence("W2", "WIRE-A", wire -> {
                    wire.defineRole(WireRoleBuilder.class, role -> role
                            .wireElementRef("1", ref -> ref
                                    .withIdentification("W2"))
                    );
                })
                .addPartOccurrence("W3", "WIRE-A", wire -> {
                    wire.defineRole(WireRoleBuilder.class, role -> role
                            .wireElementRef("1", ref -> ref
                                    .withIdentification("W3"))
                    );
                })
                .addConnection("W1", cnn -> {
                    cnn.addEnd("A1", "1")
                            .addEnd("A2", "1");
                })
                .addConnection("W2", cnn -> {
                    cnn.addEnd("A1", "1")
                            .addEnd("A3", "1");
                })
                .addConnection("W3", cnn -> {
                    cnn.addEnd("A1", "1")
                            .addEnd("A2", "1");
                })
                .withPlacements(placements -> {
                    placements.addPlacement("A1", "ND-1")
                            .addPlacement("A2", "ND-3")
                            .addPlacement("A3", "ND-7");
                })
                .withRoutings(routings -> {
                    routings
                            .addRouting("W1", "SEG-1", "SEG-2")
                            .addRouting("W3", routing -> {
                                routing.appendPath("SEG-1", "SEG-3", "SEG-3", "SEG-2")
                                        .addMandatorySegments("SEG-3");
                            })
                            .addRouting("W2", routing -> {
                                routing.appendPath("SEG-2", "SEG-3", "SEG-4", "SEG-5", "SEG-6")
                                        .withIdentification("W2.1")
                                        .withConfigurationConstraint("RL")
                                        .addMandatorySegments("SEG-4", "SEG-5");
                            })
                            .addRouting("W2", routing -> {
                                routing.appendPath("SEG-2", "SEG-3", "SEG-7", "SEG-8", "SEG-6")
                                        .withIdentification("W2.1")
                                        .withConfigurationConstraint("LL")
                                        .addMandatorySegments("SEG-7", "SEG-8");
                            });
                });

    }

    private void customizeTopology(TopologyBuilder topology) {
        topology.addSegment("ND-1", "ND-2", "SEG-2")
                .addSegment("ND-3", "ND-2", "SEG-1")
                .addSegment("ND-2", "ND-4", "SEG-3")
                .addSegment("ND-4", "ND-5", "SEG-4", seg -> seg.withConfigurationConstraint("RL"))
                .addSegment("ND-5", "ND-6", "SEG-5", seg -> seg.withConfigurationConstraint("RL"))
                .addSegment("ND-6", "ND-7", "SEG-6")
                .addSegment("ND-4", "ND-8", "SEG-7", seg -> seg.withConfigurationConstraint("LL"))
                .addSegment("ND-8", "ND-6", "SEG-8", seg -> seg.withConfigurationConstraint("LL"));
    }

    private void customizeConA(ComponentMasterDataBuilder comp) {
        comp.addGeneralTechnicalPart()
                .addPlaceability(VecPlacementType.ON_POINT)
                .addConnectorHousing(builder -> builder
                        .addCavity("A", "1"));
    }
}
