/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
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
package com.foursoft.harness.kbl2vec;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import com.foursoft.harness.kbl.v25.KBLContainer;
import com.foursoft.harness.kbl.v25.KblReader;
import com.foursoft.harness.kbl2vec.core.ConversionOrchestrator;
import com.foursoft.harness.navext.runtime.io.utils.ValidationEventLogger;
import com.foursoft.harness.navext.runtime.io.write.XMLWriter;
import com.foursoft.harness.navext.runtime.io.write.xmlmeta.XMLMeta;
import com.foursoft.harness.navext.runtime.io.write.xmlmeta.comments.Comments;
import com.foursoft.harness.vec.v2x.VecContent;
import jakarta.xml.bind.Marshaller;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@ExtendWith(SnapshotExtension.class)
class KblToVecConverterTest {

    private Expect expect;

    /**
     * Tests the conversion of some reference KBL sample files to VEC and compares the output to a stored snapshot.
     * <ul>
     *     <li>Ensures that no unintended changes to the conversion result are introduced during refactorings.</li>
     *     <li>Highlights differences when new features are added to the converter, allowing inspection of changes in
     *     {@code __snapshots__/KblToVecConverterTest.snap} during PR review without requiring local execution.</li>
     * </ul>
     *
     * @throws IOException if reading or writing files fails
     */
    @ParameterizedTest
    @ValueSource(strings = {"vobes_sample_kbl24_mit_sicherungstraeger", "vobes_sample_kbl24_battery_plus_cable",
            "vobes_sample_kbl24_generator_cable"})
    void should_convertKblToVec(final String kblFileName) throws IOException {
        final KblToVecConverter converter = new KblToVecConverter();
        final String kblFile = "/" + kblFileName + ".kbl";

        try (final InputStream is = getClass()
                .getResourceAsStream(kblFile)) {

            final KBLContainer kblContainer = new KblReader(new ValidationEventLogger()).read(is);

            final ConversionOrchestrator.Result<VecContent> result = converter.convert(kblContainer);

            writeToStream(result, TestUtils.createTestFileStream(kblFileName));

            try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                writeToStream(result, baos);
                expect.scenario(kblFileName).toMatchSnapshot(baos.toString(StandardCharsets.UTF_8));
            }
        }
    }

    public void writeToStream(final ConversionOrchestrator.Result<VecContent> result, final OutputStream stream) {
        final XMLWriter<VecContent> writer = createWriter();
        final XMLMeta xmlMeta = new XMLMeta();
        final Comments comments = new Comments();
        xmlMeta.setComments(comments);

        result.comments().forEach(comments::put);

        writer.write(result.resultValue(), xmlMeta, stream);
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
