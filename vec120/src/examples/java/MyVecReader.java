import com.foursoft.vecmodel.vec120.VecApproval;
import com.foursoft.vecmodel.vec120.VecContent;
import com.foursoft.vecmodel.vec120.VecPermission;
import com.foursoft.xml.ExtendedUnmarshaller;
import com.foursoft.xml.JaxbModel;
import com.foursoft.xml.model.Identifiable;

import javax.xml.bind.JAXBException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MyVecReader {

    public void readVecFile(String pathToFile) throws JAXBException, IOException {
        try (final InputStream is = MyVecReader.class.getResourceAsStream(pathToFile)) {
            final ExtendedUnmarshaller<VecContent, Identifiable> unmarshaller =
                    new ExtendedUnmarshaller<VecContent, Identifiable>(VecContent.class)
                            .withBackReferences()
                            .withIdMapper(Identifiable.class, Identifiable::getXmlId);

            final JaxbModel<VecContent, Identifiable> model = unmarshaller
                    .unmarshall(new BufferedInputStream(is));

            final VecApproval approval = model.getIdLookup()
                    .findById(VecApproval.class, "id_2014_0")
                    .orElse(null);

            // get a specific permission
            final VecPermission vecPermission = model.getIdLookup()
                    .findById(VecPermission.class, "id_2185_0")
                    .orElse(null);

            // get all permissions of an approval (in oop style)
            final List<VecPermission> permissions = approval.getPermissions();
        }
    }
}