/*-
 * ========================LICENSE_START=================================
 * VEC to AAS Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
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
package com.foursoft.harness.vec.aas;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.InputStream;

public class QudtRepository {

    public static final String QUDT = "/qudt-vocab-units-3.1.0.ttl";
    private final Model qudtModel;

    private static final String QUERY_TEMPLATE = """
            PREFIX qudt: <http://qudt.org/schema/qudt/>
            PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
            SELECT ?unit ?name ?symbol
            WHERE {
                ?unit qudt:uneceCommonCode ?code;
                      qudt:symbol ?symbol;
                      rdfs:label ?name
                FILTER(LANG(?name) = "" || LANGMATCHES(LANG(?name), "en"))
            }
            """;

    public QudtRepository() {
        qudtModel = ModelFactory.createDefaultModel();
        final InputStream ontologyInputStream = this.getClass().getResourceAsStream(QUDT);
        if (ontologyInputStream == null) {
            throw new AasConversionException("Cannot find QUDT ontology: " + QUDT);
        }
        RDFDataMgr.read(qudtModel, ontologyInputStream, Lang.TTL);
    }

    public QudtUnit findByUnEceCommonCode(final String code) {
        final ParameterizedSparqlString pss = new ParameterizedSparqlString(QUERY_TEMPLATE);
        pss.setLiteral("code", code);

        final Query query = pss.asQuery();
        try (final QueryExecution qexec = QueryExecutionFactory.create(query, qudtModel)) {
            final ResultSet results = qexec.execSelect();
            if (results.hasNext()) {
                final QuerySolution solution = results.next();
                return new QudtUnit(solution.getResource("unit").getURI(),
                                    solution.getLiteral("name").getLexicalForm(),
                                    solution.getLiteral("symbol").getLexicalForm());
            }
            return null;
        }
    }

    public record QudtUnit(String uri, String name, String symbol) {
    }

}
