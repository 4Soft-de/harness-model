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
import org.apache.jena.rdf.model.Resource;

import java.util.Map;
import java.util.Objects;

public class MergeContext {
    private final Map<String, Identifiable> uriToIdentifiable;
    private final VecObjectFactory objectFactory;
    private final Identifiable root;

    public MergeContext(final Identifiable root, Map<String, Identifiable> uriToIdentifiable) {
        Objects.requireNonNull(root, "root must not be null");
        Objects.requireNonNull(uriToIdentifiable, "uriToIdentifiable must not be null");
        this.root = root;
        this.uriToIdentifiable = uriToIdentifiable;
        this.objectFactory = new VecObjectFactory(root.getClass());
    }

    public Identifiable getRoot() {
        return root;
    }

    public Identifiable getVecObjectForUri(final String uri) {
        Identifiable identifiable = uriToIdentifiable.get(uri);
        if (identifiable == null) {
            throw new MergeConflictException("The object represented by: " + uri + " does not exist in the context.");
        }
        return identifiable;
    }

    public Identifiable create(Resource valueNode) {
        Identifiable result = objectFactory.create(valueNode);
        if (valueNode.isURIResource()) {
            uriToIdentifiable.put(valueNode.getURI(), result);
        }
        return result;
    }
}
