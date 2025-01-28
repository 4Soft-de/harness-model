package com.foursoft.harness.vec.files;

import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.enums.TemperatureType;
import com.foursoft.harness.vec.v2x.VecPrimaryPartType;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.foursoft.harness.vec.scripting.factories.ColorFactory.ral;
import static com.foursoft.harness.vec.scripting.factories.LocalizedStringFactory.de;
import static com.foursoft.harness.vec.scripting.factories.LocalizedStringFactory.en;
import static com.foursoft.harness.vec.scripting.factories.MaterialFactory.dinEn13602;
import static com.foursoft.harness.vec.scripting.factories.MaterialFactory.material;

class Vws4LsCoroflexWireExampleTest {

    @Test
    void create9_2611() throws IOException {
        final VecSession session = new VecSession();
        session.getDefaultValues().setCompanyName("Coroflex");
        session.getDefaultValues().setColorReferenceSystem("Coroflex");
        session.getDefaultValues().setMaterialReferenceSystem("Coroflex");

        session.component("9-2611 / 35mm²", "TI_9-2611_FHLR2GCB2G_35", VecPrimaryPartType.WIRE, comp -> comp
                .withPartAbbreviation(en("FHLR2GCB2G 35 mm² / 0,21 T180 0,6/1,0 kV"))
                .withPartAbbreviation(de("FHLR2GCB2G 35 mm² / 0,21 T180 0,6/1,0 kV"))
                .withPartDescription(
                        en("Shielded Cable for automotive electric powertrain",
                           "ProductFamilyTitle"))
                .withPartDescription(
                        de("Automotive Leitung geschirmt für elektrische Fahrzeugantriebe",
                           "ProductFamilyTitle"))
                .addRequirementsConformance(req -> {
                    req.addConformanceWith("LV216-2", "VDA")
                            .addConformanceWith("GS 95007-6-2", "BMW Group")
                            .addConformanceWith("C51 / 12.14", "Mercedes")
                            .addConformanceWith("A0025460501", "Daimler Truck")
                            .addConformanceWith("N 107 777", "Volkswagen");
                })
                .addGeneralTechnicalPart(builder -> builder
                        .withMassInformation(485, session.gramPerMeter())
                        .withTemperatureInformation(TemperatureType.OPERATING_TEMPERATURE, -40, 180,
                                                    session.degreeCelsius())
                        .withTemperatureInformation(TemperatureType.SHORT_TERM_AGING_TEMPERATURE,
                                                    Double.NaN, 205,
                                                    session.degreeCelsius())
                )
                .addConductorSpecification("Core Conductor", core ->
                        core.withCSA(35.0)
                                .withOutsideDiameter(8.5)
                                .withResistance(0.527)
                                .strands(1070, 0.05, 0.21)
                                .withMaterial(dinEn13602("Cu-ETP1"))

                )
                .addShieldSpecification("Foiled Shielding Conductor", foil ->
                        foil.foil()
                                .withDescription(
                                        de("ALU-kaschierte PET-Folie, Metallseite innen, Überlappung min. 20%"))
                                .withDescription(en("ALU-PET foil, Metal-side in contact to screen, overlap min. 20%"))
                                .withMaterial(material("Coroflex", "ALU-PET foil"))
                )
                .addShieldSpecification("Screening Braid Conductor", braid ->
                        braid.braided()
                                .withResistance(3.8)
                                .withDescription(de("Cu.-verzinnt max. 0,21mm optische Bedeckung min. 85%"))
                                .withDescription(en("Tinned copper max. 0.21mm optical covering min. 85%"))
                                .withOpticalCoverage(0.85)
                                .withMaterial(material("Coroflex", "Cu"))
                                .withPlatingMaterial(material("Coroflex", "Tin"))
                )

                .addWireSpecification(ws -> ws
                        .withWireElement("Cable", cable -> cable
                                .withOutsideDiameter(14.4, -0.6, +0.0)
                                .withMinBendRadiusDynamic(14.4 * 6)
                                .withMinBendRadiusStatic(14.4 * 3)
                                .withImpedance(10)
                                .withCapacitance(600)
                                .withInductance(100)
                                .addInsulationSpecification("Outer Sheath Insulation", ins ->
                                        ins.withColor("orange")
                                                .withBaseColor(ral("2003"))
                                                .withThickness(0.80)
                                                .withInsulationMaterial("mod. Silicon Rubber SiR")
                                                .withPrintedLabelIdentificationValue(
                                                        "COROFLEX [nnn] 9-2611 FHLR2GCB2G 35 mm²/T180 ATTENTION HIGH " +
                                                                "VOLTAGE MAX 600 V AC / 1000 V DC [xx…xx]")
                                )
                                .addSubWireElement("Screening Braid", braid -> braid
                                        .withCoreSpecification("Screening Braid Conductor")
                                        .addSubWireElement("Foiled Shielding", foil -> foil
                                                .withCoreSpecification("Foiled Shielding Conductor")
                                                .addSubWireElement("Core", core -> core
                                                        .withOutsideDiameter(10.5, -0.7, 0)
                                                        .withCoreSpecification("Core Conductor")
                                                        .addInsulationSpecification("Core Insulation", ins ->
                                                                ins.withColor("orange")
                                                                        .withBaseColor(
                                                                                ral("2003"))
                                                                        .withThickness(0.64)
                                                                        .withInsulationMaterial(
                                                                                "mod. Silicon " +
                                                                                        "Rubber SiR")
                                                        )
                                                ))))
                )
        );

        session.writeToStream(TestUtils.createTestFileStream("coroflex-ti-9-2611-35"));
    }
}
