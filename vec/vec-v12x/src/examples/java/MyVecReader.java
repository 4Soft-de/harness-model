/*-
 * ========================LICENSE_START=================================
 * VEC 1.2.X
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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

import com.foursoft.harness.navext.runtime.JaxbModel;
import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.v12x.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MyVecReader {

    public static void readVecFile(final String pathToFile) throws IOException {
        try (final InputStream is = MyVecReader.class.getResourceAsStream(pathToFile)) {
            final VecReader vecReader = new VecReader();
            final JaxbModel<VecContent, Identifiable> model = vecReader.readModel(is);

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

    public static void readFullVecFile(final String pathToFile) throws IOException {
        try (final InputStream is = MyVecReader.class.getResourceAsStream(pathToFile)) {
            final VecReader vecReader = new VecReader();

            final VecContent rootElement = vecReader.read(is);

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
            final String standardCopyrightInformationAsString = copyrightNotesAsString.isEmpty()
                    ? "None"
                    : String.format("Copyright Notes: %s", copyrightNotesAsString);

            final List<VecContract> contracts = rootElement.getContracts();
            final String contractsAsString = contracts.stream()
                    .map(contract -> String.format("\tCompany: %s, Role: %s, Item Versions: %s",
                                                   contract.getCompanyName(), contract.getContractRole(),
                                                   contract.getRefItemVersion()))
                    .collect(Collectors.joining(System.lineSeparator()));

            final List<VecCopyrightInformation> copyrightInformation = rootElement.getCopyrightInformations();
            final String copyrightInformationAsString = copyrightInformation.stream()
                    .map(copyright -> String.format("\tCopyright Notes: %s, Item Versions: %s",
                                                    copyright.getCopyrightNotes(), copyright.getRefItemVersion()))
                    .collect(Collectors.joining(System.lineSeparator()));

            final List<VecDocumentVersion> documentVersions = rootElement.getDocumentVersions();
            final String documentVersionsAsString = documentVersions.stream()
                    .map(document -> String.format("\tDocument Number: %s, Document Version: %s, Document Type: %s, " +
                                                           "Document Location: %s, Linked Document: %s, Amount of " +
                                                           "sheets: %s, " +
                                                           "Company Name: %s, Specification Ids: %s",
                                                   document.getDocumentNumber(), document.getDocumentVersion(),
                                                   document.getDocumentType(),
                                                   document.getLocation(), document.getFileName(),
                                                   document.getNumberOfSheets(),
                                                   document.getCompanyName(), document.getSpecifications().stream()
                                                           .map(VecSpecification::getIdentification)
                                                           .reduce((x, y) -> x + ", " + y)
                                                           .orElse("None")))  // allows default value
                    .collect(Collectors.joining(System.lineSeparator()));

            final String specificationsAsString = documentVersions.stream()
                    .map(VecDocumentVersion::getSpecifications)
                    .map(specifications -> specifications.stream()
                            .map(spec -> String.format("\tSpecification Id: %s, Specification class: %s",
                                                       spec.getIdentification(), spec.getClass().getSimpleName()))
                            .toList())
                    .flatMap(Collection::stream) // to get rid of unneeded newlines
                    .collect(Collectors.joining(System.lineSeparator()));

            final List<VecItemHistoryEntry> itemHistoryEntries = rootElement.getItemHistoryEntries();
            final String items = itemHistoryEntries == null
                    ? ""
                    : itemHistoryEntries.stream()
                    .map(item -> String.format("\tItem XML Id: %s, Item Type: %s," +
                                                       "Predecessor ItemVersion: %s, Successor ItemVersion: %s",
                                               item.getXmlId(), item.getType().value(),
                                               item.getPredecessorVersion(), item.getSuccessorVersion()))
                    .collect(Collectors.joining(System.lineSeparator()));

            final List<VecPartVersion> partVersions = rootElement.getPartVersions();
            final String versions = partVersions.stream()
                    .map(partVersion -> String.format("\tPart Version %s, Part Number: %s, " +
                                                              "Preferred Use Case: %s, Primary Part Type: %s",
                                                      partVersion.getPartVersion(), partVersion.getPartNumber(),
                                                      partVersion.getPreferredUseCase(),
                                                      partVersion.getPrimaryPartType().value()))
                    .collect(Collectors.joining(System.lineSeparator()));

            final List<VecProject> projects = rootElement.getProjects();
            final String projectAsString = projects == null
                    ? ""
                    : projects.stream()
                    .map(project -> String.format("\tProject Id: %s, Descriptions: %s, Car classification levels: %s",
                                                  project.getIdentification(), project.getDescriptions().stream()
                                                          .map(VecAbstractLocalizedString::getValue)
                                                          .collect(Collectors.joining(", ")),
                                                  String.format("2: %s, 3: %s, 4: %s",
                                                                project.getCarClassificationLevel2(),
                                                                project.getCarClassificationLevel3(),
                                                                project.getCarClassificationLevel4())))
                    .collect(Collectors.joining(System.lineSeparator()));

            final List<VecUnit> units = rootElement.getUnits();
            final String unitsAsString = units.stream()
                    .map(unit -> String.format("\tUnit XML id: %s, Exponent: %d, Unit class: %s",
                                               unit.getXmlId(), unit.getExponent(), unit.getClass().getSimpleName()))
                    .collect(Collectors.joining(System.lineSeparator()));

            final List<VecCustomProperty> customProperties = rootElement.getCustomProperties();
            final String properties = customProperties.stream()
                    .map(x -> String.format("\tProperty XML id: %s, Property Type: %s, Property class: %s",
                                            x.getXmlId(), x.getPropertyType(), x.getClass().getSimpleName()))
                    .collect(Collectors.joining(System.lineSeparator()));

            final StringBuilder builder = new StringBuilder()
                    .append("VEC Version: ").append(vecVersion).append(System.lineSeparator())
                    .append("System name: ").append(generatingSystemName).append(System.lineSeparator())
                    .append("System version: ").append(generatingSystemVersion).append(System.lineSeparator())
                    .append("Date of creation: ").append(dateOfCreation).append(System.lineSeparator())
                    .append("Standard copyright: ").append(standardCopyrightInformationAsString)
                    .append(System.lineSeparator());

            addDataToBuilder(builder, "Contracts", contractsAsString);
            addDataToBuilder(builder, "Copyright information", copyrightInformationAsString);
            addDataToBuilder(builder, "Document versions", documentVersionsAsString);
            addDataToBuilder(builder, "Specifications", specificationsAsString);
            addDataToBuilder(builder, "Item History", items);
            addDataToBuilder(builder, "Part Versions", versions);
            addDataToBuilder(builder, "Projects", projectAsString);
            addDataToBuilder(builder, "Units", unitsAsString);
            addDataToBuilder(builder, "Custom Properties", properties, false);

            final String output = builder.toString();

            System.out.println(output);
        }
    }

    public static void getBackReferences(final String pathToFile) throws IOException {
        try (final InputStream is = MyVecReader.class.getResourceAsStream(pathToFile)) {
            final VecReader vecReader = new VecReader();
            final JaxbModel<VecContent, Identifiable> model = vecReader.readModel(is);

            final VecContent content = model.getRootElement();

            // VecUnit -> VecValueWithUnit
            final List<VecUnit> units = content.getUnits();
            if (units.isEmpty()) {
                return;
            }
            final VecUnit vecUnit = units.get(0);
            final Set<VecValueWithUnit> refValueWithUnit = vecUnit.getRefValueWithUnit();
            final VecValueWithUnit vecValueWithUnit = refValueWithUnit.stream().findFirst().orElse(null);
            if (vecValueWithUnit == null) {
                return;
            }
            final String xmlId = vecValueWithUnit.getXmlId();

            // VecValueWithUnit -> VecUnit
            final VecValueWithUnit unitWithValue = model.getIdLookup()
                    .findById(VecValueWithUnit.class, xmlId)
                    .orElse(null);
            if (unitWithValue == null) {
                return;
            }
            System.out.println("VecValueWithUnit from VecContent = VecValueWithUnit by id lookup? " +
                                       (vecValueWithUnit.equals(unitWithValue)));
            final VecUnit unitComponent = unitWithValue.getUnitComponent();
            System.out.println(
                    "VecUnit from VecContent = VecUnit by VecUnitByValue? " + (vecUnit.equals(unitComponent)));
        }
    }

    private static void addDataToBuilder(final StringBuilder builder, final String name,
                                         final String data, final boolean newLine) {
        builder.append(name).append(":");
        if (data.isEmpty()) {
            builder.append(" None");
        } else {
            builder
                    .append(System.lineSeparator())
                    .append(data);
        }
        if (newLine) {
            builder.append(System.lineSeparator());
        }
    }

    private static void addDataToBuilder(final StringBuilder builder, final String name, final String data) {
        addDataToBuilder(builder, name, data, true);
    }
}
