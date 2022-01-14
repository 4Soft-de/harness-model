import com.foursoft.vecmodel.vec120.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class MyVecWriter {

    public static void writeVecFile(final String target) throws IOException {

        final VecContent root = new VecContent();
        root.setXmlId("id_1000_0");
        root.setVecVersion("1.1.3");

        final VecPermission permission = new VecPermission();
        permission.setPermission("Released");

        final VecApproval approval = new VecApproval();
        approval.setStatus("Approved");
        approval.getPermissions().add(permission);

        final VecDocumentVersion documentVersion = new VecDocumentVersion();
        documentVersion.getApprovals().add(approval);
        documentVersion.setDocumentNumber("123_456_789");

        final VecSpecification specification = new VecConnectorHousingCapSpecification();
        specification.setXmlId("id_2000_0");
        specification.setIdentification("Ccs-123_456_789-1");

        documentVersion.getSpecifications().add(specification);

        final VecPartVersion partVersion = new VecPartVersion();
        partVersion.setXmlId("id_1001_0");
        partVersion.setPartNumber("123_456_789");

        root.getDocumentVersions().add(documentVersion);
        root.getPartVersions().add(partVersion);

        final VecWriter vecWriter = new VecWriter();

        try (final FileOutputStream outputStream = new FileOutputStream(target)) {
            vecWriter.write(root, outputStream);
        }
    }
}