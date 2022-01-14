import com.foursoft.kblmodel.kbl24.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyKblWriter {
    public void writeKblFile(final String target)
            throws JAXBException, TransformerFactoryConfigurationError, IOException {
        final JAXBContext jc = JAXBContext.newInstance(KBLContainer.class);
        final KBLContainer root = new KBLContainer();
        final KblHarness harness = new KblHarness();

        root.setHarness(harness);

        final KblConnectorOccurrence connectorOccurrence = new KblConnectorOccurrence();
        final KblTerminalOccurrence terminalOccurrence = new KblTerminalOccurrence();
        terminalOccurrence.setXmlId("id_4711");
        final KblTerminalOccurrence terminalOccurrence2 = new KblTerminalOccurrence();
        terminalOccurrence2.setXmlId("id_4711");

        harness.getConnectorOccurrences()
                .add(connectorOccurrence);
        harness.getTerminalOccurrences()
                .add(terminalOccurrence);
        harness.getTerminalOccurrences()
                .add(terminalOccurrence2);

        final KblContactPoint contactPoint = new KblContactPoint();
        connectorOccurrence.getContactPoints()
                .add(contactPoint);

        contactPoint.setId("SCHNUPSI");
        contactPoint.setXmlId("id_1234");
        contactPoint.getAssociatedParts()
                .add(terminalOccurrence);

        final KblContactPoint contactPoint2 = new KblContactPoint();
        connectorOccurrence.getContactPoints()
                .add(contactPoint2);

        contactPoint2.setId("SCHNUPSI");
        contactPoint2.setXmlId("id_1235");

        // Access to getter = Lazy Init to of List = EmptyList =
        // <AssociatedParts></AssociatedParts>
        contactPoint2.getAssociatedParts();

        final KblContactPoint contactPoint3 = new KblContactPoint();
        connectorOccurrence.getContactPoints()
                .add(contactPoint3);

        contactPoint3.setId("SCHNUPSI");
        contactPoint3.setXmlId("id_1236");

        final KblWriter kblWriter = new KblWriter();

        try (final FileOutputStream outputStream = new FileOutputStream(target)) {
            kblWriter.write(root, outputStream);
        }
    }
}