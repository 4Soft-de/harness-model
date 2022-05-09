package com.foursoft.harness.kbl.v24;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HasProcessingInformationTest {

    @Test
    void processingInformationTest() throws IOException {
        try (final InputStream is = getClass().getClassLoader().getResourceAsStream("sample.kbl")) {
            final KBLContainer kblContainer = new KblReader().read(is);

            final KblSlotOccurrence occurrence = kblContainer.getHarness().getConnectorOccurrences()
                    .stream()
                    .filter(c -> c.getId().equals("Id184"))
                    .flatMap(c -> c.getSlots().stream())
                    .findFirst().orElse(null);

            assertThat(occurrence)
                    .returns(Optional.of("Instruction_value396"), c -> c.getProcessingInstructionValue("Instruction_type396"))
                    .returns(List.of("Instruction_value396"), c -> c.getProcessingInstructionValues("Instruction_type396"));
        }

    }

}