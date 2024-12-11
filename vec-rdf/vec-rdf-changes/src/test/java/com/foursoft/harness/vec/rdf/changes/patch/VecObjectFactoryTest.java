package com.foursoft.harness.vec.rdf.changes.patch;

import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.v2x.VecContent;
import com.foursoft.harness.vec.v2x.VecWireLength;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.junit.jupiter.api.Test;

import static com.foursoft.harness.vec.rdf.changes.test.TestUtils.findNodeWithXmlId;
import static com.foursoft.harness.vec.rdf.changes.test.TestUtils.loadModel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

class VecObjectFactoryTest {
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
    void should_create_correct_class() {
        Model m1 = loadModel(BNODE1_TTL);
        Resource bNode1 = findNodeWithXmlId(m1, "id_00074");

        assumeThat(bNode1).isNotNull();

        VecObjectFactory vecObjectFactory = new VecObjectFactory(VecContent.class);

        Identifiable result = vecObjectFactory.create(bNode1);

        assertThat(result).isNotNull()
                .isInstanceOf(VecWireLength.class);
    }

}