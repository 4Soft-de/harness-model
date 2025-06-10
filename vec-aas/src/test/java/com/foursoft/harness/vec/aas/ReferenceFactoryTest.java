/*-
 * ========================LICENSE_START=================================
 * VEC to AAS Converter
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
package com.foursoft.harness.vec.aas;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import com.foursoft.harness.navext.runtime.JaxbModel;
import com.foursoft.harness.navext.runtime.io.read.XMLReader;
import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.rdf.common.VecVersion;
import com.foursoft.harness.vec.v2x.VecWireElement;
import org.eclipse.digitaltwin.aas4j.v3.model.Reference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.w3c.dom.Document;

import java.io.InputStream;

import static com.foursoft.harness.vec.rdf.common.util.VecXmlApiUtils.*;

@ExtendWith(SnapshotExtension.class)
class ReferenceFactoryTest {

    Expect expect;

    @Test
    void test() {
        final InputStream inputFile = this.getClass().getResourceAsStream("/coroflex-ti-9-2611-35.vec");
        final Document document = loadDocument(inputFile);

        final VecVersion version = guessVersion(document);

        final XMLReader<?, Identifiable> reader = resolveReader(version);

        final JaxbModel<?, Identifiable> jaxbModel = reader.readModel(document);

        final ReferenceFactory factory = new ReferenceFactory(new AasNamingStrategy(), "https://www.test.com",
                                                              (Identifiable) jaxbModel.getRootElement());

        final VecWireElement wireElement = jaxbModel.getIdLookup().findById(VecWireElement.class,
                                                                            "WireElement_00064").orElseThrow();

        final Reference reference = factory.localReferenceFor(wireElement);

        expect.serializer("json").toMatchSnapshot(reference);

    }

}
