/*-
 * ========================LICENSE_START=================================
 * NavExt XJC Plugin
 * %%
 * Copyright (C) 2019 - 2026 4Soft GmbH
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
package com.foursoft.harness.navext.xjc.plugin.openenum;

import com.sun.codemodel.JPackage;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.model.CEnumLeafInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.EnumOutline;
import com.sun.tools.xjc.outline.Outline;

import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

/**
 * Learns from the already generated classes how this model names and packages the types of a schema.
 *
 * <p>
 * The class name prefix (for example {@code Vec}) comes from a {@code jxb:nameXmlTransform}
 * customization, which is not visible to a plugin. It is therefore derived by comparing the schema
 * type names against the generated class names: whatever the majority of the model does, the open
 * enumerations do as well.
 * </p>
 */
final class ModelNaming {

    private final Map<String, JPackage> packagesByNamespace = new HashMap<>();
    private final Map<String, Integer> prefixCounts = new HashMap<>();
    private JPackage anyPackage;

    private ModelNaming() {
    }

    static ModelNaming of(final Outline outline) {
        final ModelNaming naming = new ModelNaming();
        for (final ClassOutline classOutline : outline.getClasses()) {
            final CClassInfo target = classOutline.target;
            naming.observe(target.getTypeName(), target.shortName, classOutline.implClass._package());
        }
        for (final EnumOutline enumOutline : outline.getEnums()) {
            final CEnumLeafInfo target = enumOutline.target;
            naming.observe(target.getTypeName(), target.shortName, enumOutline.clazz._package());
        }
        return naming;
    }

    private void observe(final QName typeName, final String shortName, final JPackage pkg) {
        if (anyPackage == null) {
            anyPackage = pkg;
        }
        if (typeName == null || shortName == null) {
            return;
        }
        packagesByNamespace.putIfAbsent(typeName.getNamespaceURI(), pkg);

        final String localName = typeName.getLocalPart();
        if (shortName.endsWith(localName)) {
            final String prefix = shortName.substring(0, shortName.length() - localName.length());
            prefixCounts.merge(prefix, 1, Integer::sum);
        }
    }

    /**
     * @param namespaceUri The target namespace of the schema type.
     * @return The package the model of that namespace is generated into.
     */
    JPackage packageOf(final String namespaceUri) {
        return packagesByNamespace.getOrDefault(namespaceUri, anyPackage);
    }

    /**
     * @return The prefix the majority of the generated classes adds to their schema type name.
     * Possibly empty, never {@code null}.
     */
    String classNamePrefix() {
        return prefixCounts.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
    }

}
