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
