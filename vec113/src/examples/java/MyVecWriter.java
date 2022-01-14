import com.foursoft.vecmodel.vec113.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class MyVecWriter {

    public static void writeExampleVecFile(final String target) throws IOException {
        final VecContent root = new VecContent();
        root.setXmlId("id_1000_0");
        root.setVecVersion("1.1.3");

        final VecPermission permission = new VecPermission();
        permission.setXmlId("id_2185_0");
        permission.setPermission("Released");

        final VecApproval approval = new VecApproval();
        approval.setXmlId("id_2014_0");
        approval.setStatus("Approved");
        approval.getPermissions().add(permission);

        final VecDocumentVersion documentVersion = new VecDocumentVersion();
        documentVersion.setXmlId("id_1002_0");
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

        final VecWriter localWriter = new VecWriter();

        try (final FileOutputStream outputStream = new FileOutputStream(target)) {
            localWriter.write(root, outputStream);
        }
    }
}