/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
import com.foursoft.harness.vec.scripting.components.PartWithSubComponentsRoleBuilder;
import com.foursoft.harness.vec.v2x.*;
import com.foursoft.harness.vec.v2x.validation.VecValidation;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Example for the implementation guideline
 * <a href="https://ecad-wiki.prostep.org/specifications/vec/guidelines/ee-components/pigtail/">E/E Components with
 * Pigtail</a>: a sensor delivered with a pigtail ending in a harness side connector is an assembly, the sensor is one
 * of its contents.
 */
class EeComponentWithPigtailTest {

    private static final String SENSOR_DOCUMENT = "DRAW-SEN-2000";

    @Test
    void create_ee_component_with_pigtail() {
        final VecSession session = new VecSession();

        // @formatter:off
        // standard parts with a part number of their own: harness side connector and its contacts
        session.component("CON-0042", "DRAW-CON-0042", VecPrimaryPartType.CONNECTOR_HOUSING, comp -> comp
                .addGeneralTechnicalPart()
                .addPlaceability(VecPlacementType.ON_POINT)
                .addConnectorHousing(connector -> connector
                        .addNumericalCavities("A", 2)));

        session.component("TRM-0815", "DRAW-TRM-0815", VecPrimaryPartType.PLUGGABLE_TERMINAL, comp -> comp
                .addGeneralTechnicalPart()
                .addPluggableTerminal(terminal -> terminal
                        .withCoreCrossSectionArea(0.35, 0.5)));

        // the sensor with pigtail: an assembly, the sensor is one of its contents
        session.component("SEN-2000", SENSOR_DOCUMENT, VecPrimaryPartType.PART_STRUCTURE, comp -> comp
                .addGeneralTechnicalPart()
                // specifications of the contents without a part number of their own
                .addEEComponentSpecificationForPartUsage("SENSOR", sensor -> sensor
                        .addHousingComponent("X1", housing -> housing
                                .addPinComponents("1", "2")))
                .addPlaceabilityForPartUsage("SENSOR", VecPlacementType.ON_POINT)
                .addPluggableTerminalForPartUsage("UNI-CONT", terminal -> terminal
                        .withCoreCrossSectionArea(0.35, 0.5))
                .addSingleCoreForPartUsage("PIGTAIL", wire -> wire
                        .withCSA(0.35)
                        .withColor("BK"))
                // bill of material
                .addPartStructure(bom -> bom
                        .withContent(VecPartStructureContentType.ASSEMBLY)
                        .addPartUsage("SENSOR", sensor -> sensor
                                .addEEComponentSpecification("EECS-SENSOR", role -> role
                                        .withConnectorHousingRole("X1"))
                                .addPlaceableElementSpecification("PES-SENSOR"))
                        .addPartUsage("W1", wire -> wire
                                .addWireSpecification("WS-PIGTAIL", role -> role
                                        .wireElementRef("1", ref -> ref.withIdentification("W1"))))
                        .addPartUsage("W2", wire -> wire
                                .addWireSpecification("WS-PIGTAIL", role -> role
                                        .wireElementRef("1", ref -> ref.withIdentification("W2"))))
                        .addPartUsage("CONTACT-S1", contact -> contact
                                .addPluggableTerminalSpecification("PTS-UNI-CONT"))
                        .addPartUsage("CONTACT-S2", contact -> contact
                                .addPluggableTerminalSpecification("PTS-UNI-CONT"))
                        .addPartOccurrence("CONNECTOR", "CON-0042")
                        .addPartOccurrence("CONTACT-C1", "TRM-0815")
                        .addPartOccurrence("CONTACT-C2", "TRM-0815")
                        // contacting on both sides of the pigtail
                        .addConnection("W1", cnn -> cnn
                                .addEndAtHousingComponent("SENSOR", "X1", "1", "CONTACT-S1")
                                .addEnd("CONNECTOR", "1", "CONTACT-C1"))
                        .addConnection("W2", cnn -> cnn
                                .addEndAtHousingComponent("SENSOR", "X1", "2", "CONTACT-S2")
                                .addEnd("CONNECTOR", "2", "CONTACT-C2"))
                        // topology of the pigtail
                        .withTopology(topology -> topology
                                .addSegment("N-SENSOR", "N-CONNECTOR", "S-PIGTAIL", segment -> segment
                                        .withSegmentLength(500, VecLengthClassification.DESIGNED)))
                        .withPlacements(placements -> placements
                                .addPlacement("SENSOR", "N-SENSOR")
                                .addPlacement("CONNECTOR", "N-CONNECTOR"))
                        .withRoutings(routings -> routings
                                .addRouting("W1", "S-PIGTAIL")
                                .addRouting("W2", "S-PIGTAIL"))));

        // usage of the sensor with pigtail in a harness
        session.harness("H-100", "1", harness -> harness
                .withTopology(topology -> topology
                        .addNode("N-B12", node -> node
                                .withInstantiatedNode(SENSOR_DOCUMENT, "N-SENSOR"))
                        .addNode("N-X12", node -> node
                                .withInstantiatedNode(SENSOR_DOCUMENT, "N-CONNECTOR"))
                        .addSegment("N-B12", "N-X12", "S-B12", segment -> segment
                                .withInstantiatedSegment(SENSOR_DOCUMENT, "S-PIGTAIL")))
                // instantiated subcomponents: the sensor itself and the harness side connector
                .addPartUsage("B12", SENSOR_DOCUMENT, sensor -> sensor
                        .withInstanciatedUsage("SENSOR")
                        .addEEComponentSpecification("EECS-SENSOR", role -> {})
                        .addPlaceableElementSpecification("PES-SENSOR"))
                .addPartOccurrence("X12", "CON-0042", connector -> connector
                        .withInstanciatedOccurrence("SEN-2000", "CONNECTOR"))
                // the occurrence of the assembly
                .addPartOccurrence("B12-ASSY", "SEN-2000", assembly -> assembly
                        .defineRole(PartWithSubComponentsRoleBuilder.class, role -> role
                                .withSubComponents("B12", "X12")))
                .withPlacements(placements -> placements
                        .addPlacement("B12", "N-B12")
                        .addPlacement("X12", "N-X12")));
        // @formatter:on

        assertThatCode(() -> {
            session.writeToStream(TestUtils.createTestFileStream("ecad-wiki-guideline-ee-component-with-pigtail"));
            VecValidation.validateXML(session.writeToString(), System.out::println, true);
        }).doesNotThrowAnyException();

        final VecContent content = session.toVecContent();

        final VecPartStructureSpecification bom = content.getDocumentVersions()
                .stream()
                .filter(dv -> SENSOR_DOCUMENT.equals(dv.getDocumentNumber()))
                .findFirst()
                .orElseThrow()
                .getSpecificationWithType(VecPartStructureSpecification.class)
                .orElseThrow();

        assertThat(bom.getContent()).isEqualTo("Assembly");
        assertThat(bom.getInBillOfMaterial()).extracting(VecOccurrenceOrUsage::getIdentification)
                .containsExactlyInAnyOrder("SENSOR", "W1", "W2", "CONTACT-S1", "CONTACT-S2", "CONNECTOR",
                                           "CONTACT-C1", "CONTACT-C2");

        final VecPartOccurrence assembly = content.getDocumentVersions()
                .stream()
                .flatMap(dv -> dv.getSpecificationsWithType(VecCompositionSpecification.class).stream())
                .flatMap(c -> c.getComponents().stream())
                .filter(o -> "B12-ASSY".equals(o.getIdentification()))
                .findFirst()
                .orElseThrow();

        assertThat(assembly.getRoleWithType(VecPartWithSubComponentsRole.class)).hasValueSatisfying(role -> {
            assertThat(role.getPartStructureSpecification().getIdentification()).isEqualTo(bom.getIdentification());
            assertThat(role.getSubComponent()).extracting(VecOccurrenceOrUsage::getIdentification)
                    .containsExactly("B12", "X12");
        });
    }

}
