import com.foursoft.vecmodel.vec113.*;
import com.foursoft.xml.ExtendedUnmarshaller;
import com.foursoft.xml.JaxbModel;
import com.foursoft.xml.model.Identifiable;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public void readFullVecFile(String pathToFile) throws IOException, JAXBException {
        try (final InputStream is = MyVecReader.class.getResourceAsStream(pathToFile)) {
            final ExtendedUnmarshaller<VecContent, Identifiable> unmarshaller =
                    new ExtendedUnmarshaller<VecContent, Identifiable>(VecContent.class)
                            .withBackReferences()
                            .withIdMapper(Identifiable.class, Identifiable::getXmlId);

            final JaxbModel<VecContent, Identifiable> model = unmarshaller
                    .unmarshall(new BufferedInputStream(is));

            final VecContent rootElement = model.getRootElement();

            final String vecVersion = rootElement.getVecVersion();
            final String generatingSystemName = rootElement.getGeneratingSystemName();
            final XMLGregorianCalendar dateOfCreation = rootElement.getDateOfCreation();
            final String generatingSystemVersion = rootElement.getGeneratingSystemVersion();

            final VecCopyrightInformation standardCopyright = rootElement.getStandardCopyrightInformation();
            final String copyrightNotesAsString = standardCopyright == null
                    ? ""
                    : standardCopyright.getCopyrightNotes().stream()
                    .map(VecAbstractLocalizedString::getValue)
                    .collect(Collectors.joining(", "));
            String standardCopyrightInformationAsString = copyrightNotesAsString.isEmpty()
                    ? "None"
                    : String.format("Copyright Notes: %s", copyrightNotesAsString);

            final List<VecConformanceClass> compliantConformanceClasses = rootElement.getCompliantConformanceClasses();
            final String compliantConformanceClassesAsString = compliantConformanceClasses.stream()
                    .map(conformance -> String.format("\tVecConformance %s: Version: %s, URL: %s",
                            conformance.getIdentification(), conformance.getVersion(), conformance.getUrl()))
                    .collect(Collectors.joining(System.lineSeparator()));

            final List<VecContract> contracts = rootElement.getContracts();
            final String contractsAsString = contracts.stream()
                    .map(contract -> String.format("\tCompany: %s, Role: %s, Item Versions: %s",
                            contract.getCompanyName(), contract.getContractRole(), contract.getRefItemVersion()))
                    .collect(Collectors.joining(System.lineSeparator()));

            final List<VecCopyrightInformation> copyrightInformation = rootElement.getCopyrightInformations();
            final String copyrightInformationAsString = copyrightInformation.stream()
                    .map(copyright -> String.format("\tCopyright Notes: %s, Item Version: %s",
                            copyright.getCopyrightNotes(), copyright.getRefItemVersion()))
                    .collect(Collectors.joining(System.lineSeparator()));

            final List<VecDocumentVersion> documentVersions = rootElement.getDocumentVersions();
            final String documentVersionsAsString = documentVersions.stream()
                    .map(document -> String.format("\tDocument Number: %s, Document Version: %s, Document Type: %s, " +
                                    "Document Location: %s, Linked Document: %s, Amount of sheets: %s",
                            document.getDocumentNumber(), document.getDocumentVersion(), document.getDocumentType(),
                            document.getLocation(), document.getFileName(), document.getNumberOfSheets()))
                    .collect(Collectors.joining(System.lineSeparator()));

            final StringBuilder builder = new StringBuilder()
                    .append("VEC Version: ").append(vecVersion).append(System.lineSeparator())
                    .append("System name: ").append(generatingSystemName).append(System.lineSeparator())
                    .append("System version: ").append(generatingSystemVersion).append(System.lineSeparator())
                    .append("Date of creation: ").append(dateOfCreation).append(System.lineSeparator())
                    .append("Standard copyright: ").append(standardCopyrightInformationAsString)
                    .append(System.lineSeparator());

            builder.append("Conformance classes:");
            if (compliantConformanceClassesAsString.isEmpty())  {
                builder.append(" None");
            }  else {
                builder
                        .append(System.lineSeparator())
                        .append(compliantConformanceClassesAsString);
            }
            builder.append(System.lineSeparator());

            builder.append("Contracts:");
            if (contractsAsString.isEmpty())  {
                builder.append(" None");
            }  else {
                builder
                        .append(System.lineSeparator())
                        .append(contractsAsString);
            }
            builder.append(System.lineSeparator());

            builder.append("Copyright information:");
            if (copyrightInformationAsString.isEmpty())  {
                builder.append(" None");
            }  else {
                builder
                        .append(System.lineSeparator())
                        .append(copyrightInformationAsString);
            }
            builder.append(System.lineSeparator());

            builder.append("Document versions:");
            if (documentVersionsAsString.isEmpty())  {
                builder.append(" None");
            }  else {
                builder
                        .append(System.lineSeparator())
                        .append(documentVersionsAsString);
            }

            final String output = builder.toString();

            System.out.println(output);
        }
    }

    public void getBackReferences(String pathToFile) throws IOException, JAXBException {
        try (final InputStream is = MyVecReader.class.getResourceAsStream(pathToFile)) {
            final ExtendedUnmarshaller<VecContent, Identifiable> unmarshaller =
                    new ExtendedUnmarshaller<VecContent, Identifiable>(VecContent.class)
                            .withBackReferences()
                            .withIdMapper(Identifiable.class, Identifiable::getXmlId);

            final JaxbModel<VecContent, Identifiable> model = unmarshaller
                    .unmarshall(new BufferedInputStream(is));

            final VecContent content = model.getRootElement();

            // VecUnit -> VecValueWithUnit
            final List<VecUnit> units = content.getUnits();
            if (units.isEmpty())  {
                return;
            }
            final VecUnit vecUnit = units.get(0);
            final Set<VecValueWithUnit> refValueWithUnit = vecUnit.getRefValueWithUnit();
            final VecValueWithUnit vecValueWithUnit = refValueWithUnit.stream().findFirst().orElse(null);
            if (vecValueWithUnit == null)  {
                return;
            }
            final String xmlId = vecValueWithUnit.getXmlId();

            // VecValueWithUnit -> VecUnit
            final VecValueWithUnit unitWithValue = model.getIdLookup()
                    .findById(VecValueWithUnit.class, xmlId)
                    .orElse(null);
            if (unitWithValue == null)  {
                return;
            }
            System.out.println("VecValueWithUnit from VecContent = VecValueWithUnit by id lookup? " +
                    (vecValueWithUnit.equals(unitWithValue)));
            final VecUnit unitComponent = unitWithValue.getUnitComponent();
            System.out.println("VecUnit from VecContent = VecUnit by VecUnitByValue? " + (vecUnit.equals(unitComponent)));
        }
    }
}