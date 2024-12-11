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
import com.foursoft.harness.vec.rdf.common.VEC;
import com.foursoft.harness.vec.rdf.common.exception.VecRdfException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Property;

public class PatchUtils {

    public static String[] retrieveInheritanceHierarchy(Class<?> clazz) {
        if (clazz == null || Object.class.equals(clazz)) {
            return new String[]{};
        }

        return ArrayUtils.addFirst(retrieveInheritanceHierarchy(clazz.getSuperclass()), clazz.getSimpleName());
    }

    //TODO: Properties of VecContent are different!
    public static String resolvePropertyName(Class<? extends Identifiable> context, Property property) {
        if (!isVecProperty(property)) {
            throw new VecRdfException(
                    "Mapping is only supported von VEC properties, offending property: " + property.getURI());
        }
        String rdfPropertyName = property.getLocalName();

        for (String className : retrieveInheritanceHierarchy(context)) {
            String contextClassName = StringUtils.uncapitalize(className.replaceFirst("Vec", ""));
            if (rdfPropertyName.startsWith(contextClassName)) {
                return rdfPropertyName.replaceFirst(contextClassName, "");
            }
        }

        throw new VecRdfException(
                "Defined property " + rdfPropertyName + " does not belong to context class " + context.getSimpleName());
    }

    public static boolean isVecProperty(Property property) {
        return VEC.URI.equals(property.getNameSpace());
    }
}
