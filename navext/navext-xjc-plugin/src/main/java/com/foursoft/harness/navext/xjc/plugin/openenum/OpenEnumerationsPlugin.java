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

import com.sun.codemodel.JDefinedClass;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import com.sun.xml.xsom.XSComponent;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSFacet;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSRestrictionSimpleType;
import com.sun.xml.xsom.XSSimpleType;
import com.sun.xml.xsom.XSType;
import org.glassfish.jaxb.core.api.impl.NameConverter;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.WeakHashMap;

/**
 * Generates compile-safe literals for the <i>open enumerations</i> of a schema.
 *
 * <p>
 * An open enumeration defines recommended, not exhaustive, literals: a document may legitimately use
 * one of its own. Schemas express this by declaring the literals only in a second schema used for
 * strict validation, while the schema used for code generation declares the same simple type as a
 * plain {@code xs:string} restriction. XJC therefore maps open enumerations to {@link String}, which
 * loses every literal the standard does define.
 * </p>
 *
 * <p>
 * This plugin reads the literals from the second schema and generates, per open enumeration, an
 * interface, a nested {@code Custom} literal and an enum of the defined literals, plus typed
 * accessors on the model classes. The mapped {@code String} properties are left untouched, so
 * unmarshalling still cannot fail on an unrecognized literal and no published signature changes.
 * </p>
 *
 * @see OpenEnumOptions
 */
public class OpenEnumerationsPlugin extends Plugin {

    /**
     * XJC reuses a plugin instance across invocations within one JVM, and a Maven build compiles
     * every module in the same JVM. The options are therefore kept per invocation rather than per
     * plugin, so that the options of one module cannot leak into the next one.
     */
    private final Map<Options, OpenEnumOptions> optionsByInvocation =
            Collections.synchronizedMap(new WeakHashMap<>());

    @Override
    public String getOptionName() {
        return "Xopen-enums";
    }

    @Override
    public String getUsage() {
        return "  -Xopen-enums          :  generate typed literals for open enumerations, reading the\n"
                + "                          literals from the sibling '-strict' schema. See "
                + OpenEnumOptions.class.getName() + " for the additional options.";
    }

    @Override
    public int parseArgument(final Options opt, final String[] args, final int i) {
        // XJC offers every unrecognized argument to every registered plugin, whether or not this
        // plugin was activated, so the sub-options must be consumed unconditionally.
        return optionsOf(opt).parse(args[i]) ? 1 : 0;
    }

    @Override
    public boolean run(final Outline outline, final Options opt, final ErrorHandler errorHandler) {
        try {
            return generate(outline, opt);
        } catch (final SAXException e) {
            // XJC discards the exception assuming the plugin has reported it, so report it here.
            outline.getErrorReceiver()
                    .error(null, e.getMessage());
            return false;
        }
    }

    private boolean generate(final Outline outline, final Options opt) throws SAXException {
        final Map<QName, XSSimpleType> openEnumerations = findOpenEnumerations(outline);
        if (openEnumerations.isEmpty()) {
            outline.getErrorReceiver()
                    .debug("No open enumeration candidates found.");
            return true;
        }

        final OpenEnumOptions options = optionsOf(opt);
        final Map<QName, OpenEnumDefinition> definitions = readDefinitions(options, openEnumerations, opt);
        final Map<QName, JDefinedClass> literalInterfaces = generateLiteralTypes(outline, options, opt,
                                                                                openEnumerations, definitions);
        final int properties = generateAccessors(outline, literalInterfaces);

        outline.getErrorReceiver()
                .debug(String.format("Generated %d open enumerations and the accessors of %d properties.",
                                     literalInterfaces.size(), properties));
        return true;
    }

    private OpenEnumOptions optionsOf(final Options opt) {
        return optionsByInvocation.computeIfAbsent(opt, key -> new OpenEnumOptions());
    }

    /**
     * Finds the global simple types that could be open enumerations: those the compiled schema
     * declares without a single enumeration facet, which is why XJC mapped them to {@link String}.
     * A type that also has no literals in the literal schema is dropped later.
     */
    private static Map<QName, XSSimpleType> findOpenEnumerations(final Outline outline) {
        final Map<QName, XSSimpleType> candidates = new TreeMap<>(Comparator.comparing(QName::toString));

        final Iterator<XSSimpleType> simpleTypes = outline.getModel().schemaComponent.iterateSimpleTypes();
        while (simpleTypes.hasNext()) {
            final XSSimpleType simpleType = simpleTypes.next();
            final XSRestrictionSimpleType restriction = simpleType.asRestriction();
            if (!simpleType.isGlobal() || restriction == null
                    || !restriction.getDeclaredFacets(XSFacet.FACET_ENUMERATION)
                    .isEmpty()) {
                continue;
            }
            candidates.put(new QName(simpleType.getTargetNamespace(), simpleType.getName()), simpleType);
        }
        return candidates;
    }

    private static Map<QName, OpenEnumDefinition> readDefinitions(final OpenEnumOptions options,
                                                                  final Map<QName, XSSimpleType> candidates,
                                                                  final Options opt) throws SAXException {
        final Map<QName, OpenEnumDefinition> definitions = new HashMap<>();
        for (final String literalSchemaUri : literalSchemaUris(options, candidates, opt)) {
            definitions.putAll(LiteralSchemaReader.read(literalSchemaUri));
        }
        return definitions;
    }

    /**
     * Derives the literal schemas from the schema documents that declare the candidates, so that a
     * schema split over several documents keeps working.
     */
    private static Set<String> literalSchemaUris(final OpenEnumOptions options,
                                                 final Map<QName, XSSimpleType> candidates, final Options opt) {
        final Set<String> compiledSchemaUris = new LinkedHashSet<>();
        for (final XSSimpleType candidate : candidates.values()) {
            if (candidate.getLocator() != null && candidate.getLocator()
                    .getSystemId() != null) {
                compiledSchemaUris.add(candidate.getLocator()
                                               .getSystemId());
            }
        }
        if (compiledSchemaUris.isEmpty()) {
            for (final InputSource grammar : opt.getGrammars()) {
                if (grammar.getSystemId() != null) {
                    compiledSchemaUris.add(grammar.getSystemId());
                }
            }
        }

        final Set<String> literalSchemaUris = new LinkedHashSet<>();
        for (final String compiledSchemaUri : compiledSchemaUris) {
            literalSchemaUris.add(options.literalSchemaUriFor(compiledSchemaUri));
        }
        return literalSchemaUris;
    }

    private static Map<QName, JDefinedClass> generateLiteralTypes(final Outline outline,
                                                                  final OpenEnumOptions options, final Options opt,
                                                                  final Map<QName, XSSimpleType> candidates,
                                                                  final Map<QName, OpenEnumDefinition> definitions)
            throws SAXException {
        final ModelNaming naming = ModelNaming.of(outline);
        final String classPrefix = options.classPrefix() == null ? naming.classNamePrefix() : options.classPrefix();
        final NameConverter nameConverter = outline.getModel()
                .getNameConverter();

        final LiteralTypeGenerator generator = new LiteralTypeGenerator(
                outline, OpenEnumRuntime.of(outline.getCodeModel(), options.runtimePackage()),
                new ConstantNamer(nameConverter, readOverrides(options, candidates, opt)));

        final Map<QName, JDefinedClass> literalInterfaces = new LinkedHashMap<>();
        for (final QName typeName : candidates.keySet()) {
            final OpenEnumDefinition definition = definitions.get(typeName);
            if (definition == null) {
                outline.getErrorReceiver()
                        .debug(String.format("The simple type %s declares no literals in the literal schema. "
                                                     + "Not generating an open enumeration for it.", typeName));
                continue;
            }
            final String className = classPrefix + nameConverter.toClassName(typeName.getLocalPart());
            literalInterfaces.put(typeName,
                                  generator.generate(naming.packageOf(typeName.getNamespaceURI()), className,
                                                     definition));
        }
        return literalInterfaces;
    }

    private static ConstantNameOverrides readOverrides(final OpenEnumOptions options,
                                                       final Map<QName, XSSimpleType> candidates, final Options opt)
            throws SAXException {
        for (final String literalSchemaUri : literalSchemaUris(options, candidates, opt)) {
            final String namesUri = options.namesUriFor(literalSchemaUri);
            if (namesUri != null) {
                return ConstantNameOverrides.read(namesUri);
            }
        }
        return ConstantNameOverrides.none();
    }

    private static int generateAccessors(final Outline outline, final Map<QName, JDefinedClass> literalInterfaces) {
        final LiteralAccessorGenerator generator = new LiteralAccessorGenerator(outline.getCodeModel(),
                                                                               outline.getErrorReceiver());

        int generated = 0;
        for (final ClassOutline classOutline : outline.getClasses()) {
            for (final FieldOutline field : classOutline.getDeclaredFields()) {
                final XSElementDecl element = elementOf(field.getPropertyInfo());
                if (element == null) {
                    continue;
                }
                final JDefinedClass literalInterface = literalInterfaces.get(typeNameOf(element));
                if (literalInterface != null
                        && generator.addAccessors(classOutline, field, element.getName(), literalInterface)) {
                    generated++;
                }
            }
        }
        return generated;
    }

    /**
     * Resolves the element declaration a property is mapped from. The type name XJC keeps on the
     * property itself is of no use here: it is resolved up the derivation chain until it reaches a
     * name in the XML Schema namespace, which for an open enumeration is always {@code xsd:string}.
     */
    private static XSElementDecl elementOf(final CPropertyInfo property) {
        final XSComponent component = property.getSchemaComponent();
        if (component instanceof final XSElementDecl element) {
            return element;
        }
        if (component instanceof final XSParticle particle) {
            return particle.getTerm() == null ? null : particle.getTerm()
                    .asElementDecl();
        }
        return null;
    }

    private static QName typeNameOf(final XSElementDecl element) {
        final XSType type = element.getType();
        return type == null || type.getName() == null
                ? null
                : new QName(type.getTargetNamespace(), type.getName());
    }

}
