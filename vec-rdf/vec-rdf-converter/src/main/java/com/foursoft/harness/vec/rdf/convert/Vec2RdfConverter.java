/*-
 * ========================LICENSE_START=================================
 * VEC RDF Converter
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
package com.foursoft.harness.vec.rdf.convert;

import com.foursoft.harness.navext.runtime.io.read.XMLReader;
import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.rdf.common.NamingStrategy;
import com.foursoft.harness.vec.rdf.common.VEC;
import com.foursoft.harness.vec.rdf.common.VecVersion;
import com.foursoft.harness.vec.rdf.common.meta.xmi.VersionLookupModelProvider;
import com.foursoft.harness.vec.rdf.common.util.VecXmlApiUtils;
import com.foursoft.harness.vec.rdf.convert.exception.VecRdfConversionException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shared.PrefixMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.foursoft.harness.vec.rdf.common.util.VecXmlApiUtils.*;

public class Vec2RdfConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Vec2RdfConverter.class);

    private final NamingStrategy namingStrategy;
    private final VersionLookupModelProvider vecModelProvider;

    public Vec2RdfConverter() {
        vecModelProvider = new VersionLookupModelProvider();
        this.namingStrategy = new NamingStrategy();
    }

    /**
     * Kept during migration, should moved to execution layer.
     *
     * @param inputFiles
     * @param targetNamespace
     * @deprecated Is only support during migration of RDF Api to public repository.
     */
    @Deprecated(forRemoval = true)
    public void convert(final File[] inputFiles, final String targetNamespace) {
        for (final File inputFile : inputFiles) {
            final String fileName = inputFile.getName();
            final String uriFilename = fileName
                    .replace(".vec", "");
            final String ns = targetNamespace + "/" + URLEncoder.encode(uriFilename, StandardCharsets.UTF_8) + "#";

            try {
                final Model model = convert(new FileInputStream(inputFile), ns);

                final String outputfileName = uriFilename + ".ttl";
                LOGGER.info("Writing output file: {}", outputfileName);
                model.write(new FileOutputStream(outputfileName), "TURTLE");
            } catch (final IOException e) {
                throw new VecRdfConversionException("Error during conversion", e);
            }
        }
    }

    public Model convert(final InputStream vecXmlFile, final String targetNamespace) {
        final Document document = loadDocument(vecXmlFile);

        final VecVersion version = guessVersion(document);
        final Model model = ModelFactory.createDefaultModel();

        model.withDefaultMappings(PrefixMapping.Standard);
        model.setNsPrefix(VEC.PREFIX, VEC.URI);
        model.setNsPrefix("vec-dbg", VEC.DEBUG_NS);
        model.setNsPrefix("", targetNamespace);

        final VecSerializer vecSerializer = new VecSerializer(model, vecModelProvider, namingStrategy,
                                                              targetNamespace);

        final XMLReader<?, Identifiable> reader = resolveReader(version);

        final Object root = reader.read(document);

        VecXmlApiUtils.genericVecModelTraversal(root, vecSerializer::handle);

        return model;
    }

}
