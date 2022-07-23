package com.foursoft.harness.kbl.v25;

import com.foursoft.jaxb.navext.runtime.io.utils.ValidationEventLogger;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class HasPartsTest {

    @Test
    void getPartsTest() throws IOException {
        try (final InputStream is = getClass().getClassLoader().getResourceAsStream("kblxml_2.4sr-1_tab016120_modulare_ltgs._160718.kbl")) {
            final KBLContainer kblContainer = new KblReader(new ValidationEventLogger()).read(is);

            assertThat(kblContainer)
                    .satisfies(c -> assertThat(c.getParts()).hasSize(253));
        }
    }
}