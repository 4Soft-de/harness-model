import com.foursoft.vecmodel.vec113.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.TransformerFactoryConfigurationError;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyVecWriter {

    public void writeVecFile(final String target) throws JAXBException, TransformerFactoryConfigurationError, IOException {
        final JAXBContext jc = JAXBContext.newInstance(VecContent.class);

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

        final Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        final StringWriter stringWriter = new StringWriter();
        marshaller.marshal(root, stringWriter);
        final String result = stringWriter.toString();

        final Path outPath = Paths.get(target).toAbsolutePath();
        if (Files.notExists(outPath))  {
            final Path parentFolder = outPath.getParent();
            if (parentFolder != null && Files.notExists(parentFolder)) {
                Files.createDirectory(parentFolder);
            }
            Files.createFile(outPath);
        }

        Files.write(outPath, result.getBytes(StandardCharsets.UTF_8));
    }
}