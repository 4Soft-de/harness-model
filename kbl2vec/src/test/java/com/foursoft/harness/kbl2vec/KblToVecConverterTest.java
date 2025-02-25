package com.foursoft.harness.kbl2vec;

import com.foursoft.harness.kbl.v25.KBLContainer;
import com.foursoft.harness.kbl.v25.KblReader;
import com.foursoft.harness.navext.runtime.io.utils.ValidationEventLogger;
import com.foursoft.harness.navext.runtime.io.write.XMLWriter;
import com.foursoft.harness.vec.v2x.VecContent;
import jakarta.xml.bind.Marshaller;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class KblToVecConverterTest {

    @Test
    void test_conversion() throws IOException {
        final KblToVecConverter converter = new KblToVecConverter();

        try (final InputStream is = getClass()
                .getResourceAsStream("/vobes_sample_kbl24_mit_sicherungstraeger.kbl")) {

            final KBLContainer kblContainer = new KblReader(new ValidationEventLogger()).read(is);

            System.out.println(writeToString(converter.convert(kblContainer)));
        }
    }

    public String writeToString(final VecContent root) {
        final XMLWriter<VecContent> writer = createWriter();

        return writer.writeToString(root);
    }

    public void writeToStream(final VecContent root, final OutputStream stream) {
        final XMLWriter<VecContent> writer = createWriter();

        writer.write(root, stream);
    }

    private static XMLWriter<VecContent> createWriter() {
        return new XMLWriter<>(VecContent.class, new ValidationEventLogger()) {

            @Override
            protected void configureMarshaller(final Marshaller marshaller) throws Exception {
                super.configureMarshaller(marshaller);
                marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "");
            }
        };
    }

}