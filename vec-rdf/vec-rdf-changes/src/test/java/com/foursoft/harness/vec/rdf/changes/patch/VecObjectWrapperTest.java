package com.foursoft.harness.vec.rdf.changes.patch;

import static com.foursoft.harness.vecrdf.test.TestUtils.findNodeWithXmlId;
import static com.foursoft.harness.vecrdf.test.TestUtils.loadModel;
import static org.assertj.core.api.Assumptions.assumeThat;

import java.io.IOException;
import java.util.HashMap;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.io.ClassPathResource;

import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import com.foursoft.harness.vec.v2x.VecSIUnit;
import com.foursoft.harness.vec.v2x.VecWireLength;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;

@ExtendWith({SnapshotExtension.class})
class VecObjectWrapperTest {

    private Expect expect;

    private static final String PROLOGUE = """
            PREFIX : <https://www.foursoft.com/test#>
            PREFIX rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
            PREFIX rdfs:     <http://www.w3.org/2000/01/rdf-schema#>
            PREFIX vec:     <http://www.prostep.org/ontologies/ecad/2024/03/vec#>
            PREFIX vec-dbg: <http://www.prostep.org/ontologies/ecad/2024/03/vec-debug#>
            PREFIX xsd:     <http://www.w3.org/2001/XMLSchema#>
            
            """;

    private static final String BNODE1_TTL = """
            [
                rdf:type vec:WireLength ;
                rdfs:label "WireLength[id_00074]" ;
                vec:wireLengthLengthType vec:WireLengthType_DMU ;
                vec:wireLengthLengthValue
                    [
                        rdf:type vec:NumericalValue ;
                        rdfs:label "NumericalValue[id_00075]" ;
                        vec:numericalValueValueComponent
                            "1063.0"^^xsd:double ;
                        vec:valueWithUnitUnitComponent :SIUnit-id_00102 ;
                        vec-dbg:id "id_00075"
                    ] ;
                vec-dbg:id "id_00074"
            ].
            """;

    @Test
    void should_load_values() throws IOException {
        Model m1 = loadModel(BNODE1_TTL);
        RDFDataMgr.read(m1, new ClassPathResource("vec/v2.1.0/vec-2.1.0-ontology.ttl").getInputStream(), Lang.TTL);
        Resource bNode1 = findNodeWithXmlId(m1, "id_00074");

        assumeThat(bNode1).isNotNull();

        VecWireLength element = new VecWireLength();
        VecSIUnit unit = new VecSIUnit();
        unit.setXmlId("id_00102 ");

        HashMap<String, Identifiable> uriToIdentifiable = new HashMap<>();
        uriToIdentifiable.put("https://www.foursoft.com/test#SIUnit-id_00102", unit);
        MergeContext context = new MergeContext(element, uriToIdentifiable);

        VecObjectWrapper loader = new VecObjectWrapper(context, element);

        bNode1.listProperties()
                .forEach(s -> loader.loadValue(s.getPredicate(), s.getObject()));

        expect.serializer("json")
                .toMatchSnapshot(element);
    }

    @Test
    void should_unload_values() throws IOException {
        Model m1 = loadModel(BNODE1_TTL);
        RDFDataMgr.read(m1, new ClassPathResource("vec/v2.1.0/vec-2.1.0-ontology.ttl").getInputStream(), Lang.TTL);
        Resource bNode1 = findNodeWithXmlId(m1, "id_00074");

        assumeThat(bNode1).isNotNull();

        VecWireLength element = new VecWireLength();
        element.setXmlId("id_00074");
        element.setLengthType("DMU");

        VecSIUnit unit = new VecSIUnit();
        unit.setXmlId("id_00102 ");

        VecNumericalValue numericalValue = new VecNumericalValue();
        numericalValue.setXmlId("id_00075");
        numericalValue.setValueComponent(1063.0);
        numericalValue.setUnitComponent(unit);

        element.setLengthValue(numericalValue);

        HashMap<String, Identifiable> uriToIdentifiable = new HashMap<>();
        uriToIdentifiable.put("https://www.foursoft.com/test#SIUnit-id_00102", unit);
        MergeContext context = new MergeContext(element, uriToIdentifiable);

        VecObjectWrapper loader = new VecObjectWrapper(context, element);

        bNode1.listProperties()
                .forEach(s -> loader.removeValue(s.getPredicate(), s.getObject()));

        expect.serializer("json")
                .toMatchSnapshot(element);
    }

}