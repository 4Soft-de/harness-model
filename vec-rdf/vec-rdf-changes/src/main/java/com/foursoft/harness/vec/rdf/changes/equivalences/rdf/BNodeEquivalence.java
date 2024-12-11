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

import com.foursoft.harness.vec.rdf.changes.equivalences.UnorderedCollectionEquivalence;
import com.google.common.base.Equivalence;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

import java.util.Set;

import static com.foursoft.harness.vec.rdf.changes.equivalences.rdf.PropertyValueEquivalence.propertyNodeEquals;

public class BNodeEquivalence extends Equivalence<Resource> {

    private static final BNodeEquivalence INSTANCE = new BNodeEquivalence();

    private static final UnorderedCollectionEquivalence<Statement, Statement> PROPERTIES_EQUIVALENCE =
            new UnorderedCollectionEquivalence<>(propertyNodeEquals());

    public static BNodeEquivalence bNodeEquals() {
        return INSTANCE;
    }

    @Override protected boolean doEquivalent(final Resource a, final Resource b) {
        if (!(a.isAnon() && b.isAnon())) {
            return false;
        }
        final Set<Statement> propertiesA = a.listProperties().toSet();
        final Set<Statement> propertiesB = b.listProperties().toSet();

        return PROPERTIES_EQUIVALENCE.equivalent(propertiesA, propertiesB);

    }

    @Override protected int doHash(final Resource resource) {
        if (!resource.isAnon()) {
            throw new UnsupportedOperationException("Not an anonymous resource / bnode.");
        }
        return PROPERTIES_EQUIVALENCE.hash(resource.listProperties().toSet());
    }
}
