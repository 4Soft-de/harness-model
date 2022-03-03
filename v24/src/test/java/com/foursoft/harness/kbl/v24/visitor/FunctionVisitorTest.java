package com.foursoft.harness.kbl.v24.visitor;


import com.foursoft.harness.kbl.v24.KBLContainer;
import com.foursoft.harness.kbl.v24.KblReader;
import com.foursoft.jaxb.navext.runtime.model.Identifiable;
import com.foursoft.jaxb.navext.runtime.model.ModifiableIdentifiable;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class FunctionVisitorTest {

    public static final String PREFIX = "TestPrefix";

    @Test
    void updateXmlIdsTest() throws IOException {
        try (final InputStream is = getClass().getClassLoader().getResourceAsStream("sample.kbl")) {
            final KBLContainer kblContainer = new KblReader().read(is);
            final FunctionVisitor<Identifiable, Void> visitor = new FunctionVisitor<>(this::changeXmlId);
            final TraversingVisitor<Void, RuntimeException> traversingVisitor =
                    new TraversingVisitor<>(new DepthFirstTraverserImpl<>(), visitor);
            kblContainer.accept(traversingVisitor);

            assertThat(kblContainer)
                    .satisfies(c -> assertThat(c.getXmlId()).startsWith(PREFIX));
        }
    }

    private Void changeXmlId(final Identifiable element) {
        if (element instanceof ModifiableIdentifiable) {
            final ModifiableIdentifiable xmlObject = (ModifiableIdentifiable) element;
            xmlObject.setXmlId(PREFIX + element.getXmlId());
        }
        return null;
    }

}