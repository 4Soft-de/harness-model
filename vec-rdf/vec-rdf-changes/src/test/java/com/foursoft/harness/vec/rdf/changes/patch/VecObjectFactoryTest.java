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
