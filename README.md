# VEC model
JAXB-Models of the VEC, based on the underlying UML model (not only the XSD).

## Introduction
VEC stands for **V**ehicle **E**lectric **C**ontainer and defines an information model, a data dictionary, and
an XML schema derived from and compliant to the model.

It can be used to define a vehicle with all of its parts, connections, constraints, etc. 

This repository contains two VEC versions, version 1.1.3 and version 1.2.0.
The version 1.2.0 doesn't provide a backwards compatibility to version 1.1.3.
If you have trouble converting from VEC 1.1.3 to VEC 1.2.0, feel free to
contact our colleague [Johannes Becker](mailto:becker@4soft.de?subject=VEC%20Compatibility%20layer).

For an optimized performance, the XML is parsed by our
[enhanced xml navigation library](https://github.com/4Soft-de/jaxb-enhanced-navigation) which builds on top of JAXB.

More information about VEC can be found in the [ECAD wiki](https://ecad-wiki.prostep.org/specifications/vec).

## Code examples

In the codebase, the root of a vec file is the `VecContent` class.

### Reading a VEC file
#### Example VEC file
```xml
<?xml version="1.0" encoding="UTF-8"?>
<?cs checksum="2764192431"?>
<?xsd validated="true"?>
<vec:VecContent id="id_1000_0" xmlns:vec="http://www.prostep.org/ecad-if/2011/vec" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xs="http://www.w3.org/2001/XMLSchema">
    <CustomProperty id="id_1006_2" xsi:type="vec:SimpleValueProperty">
        <PropertyType>ExampleType</PropertyType>
        <Value>CAP</Value>
    </CustomProperty>
    <VecVersion>1.1.3</VecVersion>
    <Contract id="id_2107_0">
        <CompanyName>4Soft GmbH</CompanyName>
        <ContractRole>Oem</ContractRole>
    </Contract>
    <DocumentVersion id="id_1002_0">
        <CompanyName>4Soft GmbH</CompanyName>
        <Approval id="id_2014_0">
            <Status>Approved</Status>
            <Permission id="id_2185_0">
                <Permission>Released</Permission>
                <PermissionDate>2020-05-28T16:07:46.503+02:00</PermissionDate>
                <Permitter id="id_2186_0">
                    <LastName>/NULL</LastName>
                </Permitter>
            </Permission>
        </Approval>
    </DocumentVersion>
</vec:VecContent>
```

#### Java file
```java
public class MyVecReader {
    public void readVecFile(String pathToFile) throws JAXBException, IOException  {
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
```