import com.foursoft.kblmodel.kbl24.KBLContainer;
import com.foursoft.kblmodel.kbl24.KblConnectorHousing;
import com.foursoft.kblmodel.kbl24.KblConnectorOccurrence;
import com.foursoft.kblmodel.kbl24.KblHarness;
import com.foursoft.xml.ExtendedUnmarshaller;
import com.foursoft.xml.JaxbModel;
import com.foursoft.xml.model.Identifiable;

import javax.xml.bind.JAXBException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Set;

public class MyKblWriter {
    public void readKblFile(String pathToFile) throws JAXBException, IOException {
        try (final InputStream is = MyKblWriter.class.getResourceAsStream(pathToFile)) {
            final ExtendedUnmarshaller<KBLContainer, Identifiable> unmarshaller =
                    new ExtendedUnmarshaller<KBLContainer, Identifiable>(
                        KBLContainer.class).withBackReferences()
                        .withIdMapper(Identifiable.class, Identifiable::getXmlId);

            final JaxbModel<KBLContainer, Identifiable> model = unmarshaller
                    .unmarshall(new BufferedInputStream(is));

            final KblConnectorOccurrence occurrence = model.getIdLookup()
                    .findById(KblConnectorOccurrence.class, "I1616")
                    .orElse(null);

            final KblConnectorHousing part = model.getIdLookup()
                    .findById(KblConnectorHousing.class, "ch_part")
                    .orElse(null);

            if (occurrence == null || part == null)  {
                return;
            }

            final boolean equalsPart = occurrence.getPart().equals(part);
            System.out.println("Part via. lookup identical to getter call? " + (equalsPart));

            final KblHarness parentHarness = occurrence.getParentHarness();
            final KblHarness harnessOfRootElement = model.getRootElement().getHarness();

            final boolean equalsHarness = harnessOfRootElement.equals(parentHarness);
            System.out.println("Harness of root element identical to parent harness of occurrence? " + (equalsHarness));

            final Set<KblConnectorOccurrence> singleton = Collections.singleton(occurrence);
            final Set<KblConnectorOccurrence> refConnectorOccurrence = part.getRefConnectorOccurrence();

            final boolean equalsOccurrence = refConnectorOccurrence.equals(singleton);
            System.out.println("Occurrence being equals to the referenced connector of the part? " + (equalsOccurrence));
        }
    }
}