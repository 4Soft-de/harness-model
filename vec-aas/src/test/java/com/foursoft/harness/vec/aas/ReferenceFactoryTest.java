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