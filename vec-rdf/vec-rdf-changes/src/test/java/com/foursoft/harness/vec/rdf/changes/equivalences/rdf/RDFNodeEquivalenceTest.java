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
package com.foursoft.harness.vec.rdf.changes.equivalences.rdf;

import com.google.common.base.Equivalence;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.junit.jupiter.api.Test;

import static com.foursoft.harness.vec.rdf.changes.test.TestUtils.findNodeWithXmlId;
import static com.foursoft.harness.vec.rdf.changes.test.TestUtils.loadModel;
import static org.assertj.core.api.Assertions.assertThat;

class RDFNodeEquivalenceTest {

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

    private static final String BNODE2_TTL = """
            [
                rdf:type vec:WireLength ;
                rdfs:label "WireLength[id_00074]" ;
                vec:wireLengthLengthType vec:WireLengthType_DMU ;
                vec:wireLengthLengthValue
                    [
                        rdf:type vec:NumericalValue ;
                        rdfs:label "NumericalValue[id_00075]" ;
                        vec:numericalValueValueComponent
                            "1000.0"^^xsd:double ;
                        vec:valueWithUnitUnitComponent :SIUnit-id_00102 ;
                        vec-dbg:id "id_00075"
                    ] ;
                vec-dbg:id "id_00074"
            ].
            """;

    private static final String BNODE3_TTL = """
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
                        vec:valueWithUnitUnitComponent :SIUnit-id_00103 ;
                        vec-dbg:id "id_00075"
                    ] ;
                vec-dbg:id "id_00074"
            ].
            """;

    @Test
    void should_return_equal_for_identical_bnode() {
        final Model m1 = loadModel(BNODE1_TTL);
        final Model m2 = loadModel(BNODE1_TTL);

        final Resource bNode1 = findNodeWithXmlId(m1, "id_00074");
        final Resource bNode2 = findNodeWithXmlId(m2, "id_00074");

        final Equivalence.Wrapper<Resource> key1 = RDFNodeEquivalence.nodeEquals().wrap(bNode1);
        final Equivalence.Wrapper<Resource> key2 = RDFNodeEquivalence.nodeEquals().wrap(bNode2);

        assertThat(key1).isEqualTo(key2)
                .hasSameHashCodeAs(key2);
    }

    @Test
    void should_return_not_equal_for_literal_change() {
        final Model m1 = loadModel(BNODE1_TTL);
        final Model m2 = loadModel(BNODE2_TTL);

        final Resource bNode1 = findNodeWithXmlId(m1, "id_00074");
        final Resource bNode2 = findNodeWithXmlId(m2, "id_00074");

        final Equivalence.Wrapper<Resource> key1 = RDFNodeEquivalence.nodeEquals().wrap(bNode1);
        final Equivalence.Wrapper<Resource> key2 = RDFNodeEquivalence.nodeEquals().wrap(bNode2);

        assertThat(key1).isNotEqualTo(key2)
                .doesNotHaveSameHashCodeAs(key2);
    }

    @Test
    void should_return_not_equal_for_uri_change() {
        final Model m1 = loadModel(BNODE1_TTL);
        final Model m2 = loadModel(BNODE3_TTL);

        final Resource bNode1 = findNodeWithXmlId(m1, "id_00074");
        final Resource bNode2 = findNodeWithXmlId(m2, "id_00074");

        final Equivalence.Wrapper<Resource> key1 = RDFNodeEquivalence.nodeEquals().wrap(bNode1);
        final Equivalence.Wrapper<Resource> key2 = RDFNodeEquivalence.nodeEquals().wrap(bNode2);

        assertThat(key1).isNotEqualTo(key2)
                .doesNotHaveSameHashCodeAs(key2);
    }

}
