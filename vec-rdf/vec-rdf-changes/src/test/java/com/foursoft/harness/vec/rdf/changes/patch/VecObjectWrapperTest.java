/*-
 * ========================LICENSE_START=================================
 * VEC RDF Changesets (Experimental)
 * %%
 * Copyright (C) 2024 4Soft GmbH
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
package com.foursoft.harness.vec.rdf.changes.patch;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.v2x.VecNumericalValue;
import com.foursoft.harness.vec.v2x.VecSIUnit;
import com.foursoft.harness.vec.v2x.VecWireLength;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;

import static com.foursoft.harness.vec.rdf.changes.test.TestUtils.*;
import static org.assertj.core.api.Assumptions.assumeThat;

@ExtendWith({SnapshotExtension.class})
class VecObjectWrapperTest {

    private Expect expect;

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
    void should_load_values() {
        Model m1 = loadModel(BNODE1_TTL);
        RDFDataMgr.read(m1, loadResourceFromClasspath("/vec/v2.1.0/vec-2.1.0-ontology.ttl"), Lang.TTL);
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
    void should_unload_values() {
        Model m1 = loadModel(BNODE1_TTL);
        RDFDataMgr.read(m1, loadResourceFromClasspath("/vec/v2.1.0/vec-2.1.0-ontology.ttl"), Lang.TTL);
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
