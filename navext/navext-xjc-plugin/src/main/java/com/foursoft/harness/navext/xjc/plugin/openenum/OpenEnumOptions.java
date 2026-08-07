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

import java.net.URI;
import java.net.URISyntaxException;

/**
 * The options of the {@code -Xopen-enums} plugin.
 *
 * <table>
 *     <caption>Options</caption>
 *     <tr><th>Option</th><th>Default</th><th>Meaning</th></tr>
 *     <tr>
 *         <td>{@code -Xopen-enums-literal-suffix:<suffix>}</td><td>{@code -strict}</td>
 *         <td>Inserted before the extension of the compiled schema to locate the literal schema.</td>
 *     </tr>
 *     <tr>
 *         <td>{@code -Xopen-enums-literal-schema:<uri>}</td><td>&mdash;</td>
 *         <td>The literal schema, overriding the suffix rule. Relative to the compiled schema.</td>
 *     </tr>
 *     <tr>
 *         <td>{@code -Xopen-enums-names:<uri>}</td><td>&mdash;</td>
 *         <td>Constant name overrides. Relative to the literal schema.</td>
 *     </tr>
 *     <tr>
 *         <td>{@code -Xopen-enums-class-prefix:<prefix>}</td><td>derived from the model</td>
 *         <td>The prefix added to the schema type name to form the class name.</td>
 *     </tr>
 *     <tr>
 *         <td>{@code -Xopen-enums-runtime:<package>}</td>
 *         <td>{@code com.foursoft.harness.vec.common.openenum}</td>
 *         <td>The package holding {@code OpenEnumLiteral}, {@code CustomOpenEnumLiteral} and
 *         {@code OpenEnumLiterals}.</td>
 *     </tr>
 * </table>
 */
final class OpenEnumOptions {

    static final String LITERAL_SUFFIX = "-Xopen-enums-literal-suffix:";
    static final String LITERAL_SCHEMA = "-Xopen-enums-literal-schema:";
    static final String NAMES = "-Xopen-enums-names:";
    static final String CLASS_PREFIX = "-Xopen-enums-class-prefix:";
    static final String RUNTIME = "-Xopen-enums-runtime:";

    private static final String DEFAULT_RUNTIME_PACKAGE = "com.foursoft.harness.vec.common.openenum";
    private static final String DEFAULT_LITERAL_SUFFIX = "-strict";

    private String literalSuffix = DEFAULT_LITERAL_SUFFIX;
    private String literalSchema;
    private String names;
    private String classPrefix;
    private String runtimePackage = DEFAULT_RUNTIME_PACKAGE;

    /**
     * @param argument The argument to consume.
     * @return {@code true} if the argument belongs to this plugin.
     */
    boolean parse(final String argument) {
        if (argument.startsWith(LITERAL_SUFFIX)) {
            literalSuffix = valueOf(argument, LITERAL_SUFFIX);
        } else if (argument.startsWith(LITERAL_SCHEMA)) {
            literalSchema = valueOf(argument, LITERAL_SCHEMA);
        } else if (argument.startsWith(NAMES)) {
            names = valueOf(argument, NAMES);
        } else if (argument.startsWith(CLASS_PREFIX)) {
            classPrefix = valueOf(argument, CLASS_PREFIX);
        } else if (argument.startsWith(RUNTIME)) {
            runtimePackage = valueOf(argument, RUNTIME);
        } else {
            return false;
        }
        return true;
    }

    /**
     * @param compiledSchemaUri The system id of the schema XJC compiled.
     * @return The system id of the schema declaring the literals of the open enumerations.
     */
    String literalSchemaUriFor(final String compiledSchemaUri) {
        if (literalSchema != null) {
            return resolve(compiledSchemaUri, literalSchema);
        }
        final int extension = compiledSchemaUri.lastIndexOf('.');
        return extension < 0
                ? compiledSchemaUri + literalSuffix
                : compiledSchemaUri.substring(0, extension) + literalSuffix + compiledSchemaUri.substring(extension);
    }

    /**
     * @param literalSchemaUri The system id of the literal schema.
     * @return The system id of the constant name overrides, or {@code null} if there are none.
     */
    String namesUriFor(final String literalSchemaUri) {
        return names == null ? null : resolve(literalSchemaUri, names);
    }

    String classPrefix() {
        return classPrefix;
    }

    String runtimePackage() {
        return runtimePackage;
    }

    private static String resolve(final String base, final String reference) {
        try {
            return new URI(base).resolve(reference)
                    .toString();
        } catch (final URISyntaxException | IllegalArgumentException e) {
            // Not a URI - most likely a plain path, which needs no resolution.
            return reference;
        }
    }

    private static String valueOf(final String argument, final String option) {
        return argument.substring(option.length());
    }

}
