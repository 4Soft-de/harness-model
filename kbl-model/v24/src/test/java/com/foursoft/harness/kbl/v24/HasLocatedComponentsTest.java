package com.foursoft.harness.kbl.v24;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class HasLocatedComponentsTest {

    @Test
    void locatedComponentsTest() throws IOException {
        try (final InputStream is = getClass().getClassLoader().getResourceAsStream("kblxml_2.4sr-1_tab016120_modulare_ltgs._160718.kbl")) {
            final KBLContainer kblContainer = new KblReader().read(is);

            assertThat(kblContainer)
                    .satisfies(c -> assertThat(c.getHarness())
                            .satisfies(h -> assertThat(h.getLocatedComponents()).hasSize(298)));
        }
    }
}
