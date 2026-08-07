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

import com.sun.codemodel.JJavaName;
import org.glassfish.jaxb.core.api.impl.NameConverter;
import org.xml.sax.SAXException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Derives the Java constant name of an open enumeration literal.
 *
 * <p>
 * The literal is mangled and converted exactly like XJC converts the literals of a closed
 * enumeration, so that both kinds of enumeration are named consistently. Unlike XJC, which rejects a
 * schema whose literals cannot be named, this namer accepts anything the VEC declares: a literal that
 * does not start with a Java identifier character is prefixed with {@code _}, and a genuine collision
 * is resolved by a hand-maintained {@link ConstantNameOverrides override} rather than by an
 * automatically generated suffix, which would silently renumber published constants whenever a
 * literal is inserted upstream.
 * </p>
 */
final class ConstantNamer {

    private final NameConverter nameConverter;
    private final ConstantNameOverrides overrides;

    ConstantNamer(final NameConverter nameConverter, final ConstantNameOverrides overrides) {
        this.nameConverter = nameConverter;
        this.overrides = overrides;
    }

    /**
     * @param definition The open enumeration to name the literals of.
     * @return The literals keyed by their constant name, in schema order.
     * @throws SAXException If two literals of the definition resolve to the same constant name.
     */
    Map<String, OpenEnumDefinition.Literal> nameLiterals(final OpenEnumDefinition definition) throws SAXException {
        final Map<String, OpenEnumDefinition.Literal> named = new LinkedHashMap<>();
        for (final OpenEnumDefinition.Literal literal : definition.literals()) {
            final String constantName = constantNameFor(definition, literal);
            final OpenEnumDefinition.Literal clashing = named.putIfAbsent(constantName, literal);
            if (clashing != null) {
                throw new SAXException(collisionMessage(definition, literal, clashing, constantName));
            }
        }
        return named;
    }

    private String constantNameFor(final OpenEnumDefinition definition, final OpenEnumDefinition.Literal literal) {
        final String override = overrides.constantNameFor(definition.typeName(), literal.value());
        if (override != null && !override.isBlank()) {
            return override;
        }

        final String constantName = nameConverter.toConstantName(mangle(literal.value()));
        return JJavaName.isJavaIdentifier(constantName) ? constantName : "_" + constantName;
    }

    /**
     * Replaces everything that cannot appear in a Java identifier, the way XJC does it for closed
     * enumerations.
     */
    private static String mangle(final String value) {
        final StringBuilder mangled = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            final char c = value.charAt(i);
            mangled.append(Character.isJavaIdentifierPart(c) ? c : '_');
        }
        return mangled.toString();
    }

    private static String collisionMessage(final OpenEnumDefinition definition,
                                           final OpenEnumDefinition.Literal literal,
                                           final OpenEnumDefinition.Literal clashing,
                                           final String constantName) {
        return String.format(
                "The literals '%s' and '%s' of the open enumeration %s both resolve to the constant name %s. "
                        + "Add an entry to the constant name overrides (-Xopen-enums-names) to resolve this, "
                        + "for example: <type name=\"%s\"><literal value=\"%s\" name=\"...\"/></type>",
                clashing.value(), literal.value(), definition.typeName(), constantName,
                definition.typeName()
                        .getLocalPart(), literal.value());
    }

}
