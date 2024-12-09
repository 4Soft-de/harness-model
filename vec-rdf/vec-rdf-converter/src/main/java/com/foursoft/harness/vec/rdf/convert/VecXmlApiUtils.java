/*-
 * ========================LICENSE_START=================================
 * VEC RDF Common
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
import com.foursoft.harness.navext.runtime.io.write.XMLWriter;
import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.rdf.common.VecVersion;
import com.foursoft.harness.vec.rdf.common.exception.VecRdfException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;
import java.util.function.Function;

public final class VecXmlApiUtils {

    private VecXmlApiUtils() {
        throw new AssertionError("Can't instantiate utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(VecXmlApiUtils.class);

    public static Document loadDocument(InputStream file) {
        try {
            LOGGER.info("Parsing DOM Tree.");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            documentBuilderFactory.setXIncludeAware(false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            return builder.parse(file);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new VecRdfException(
                    "Unable to parse XML file with DOM Parser, maybe it is not a well-formed XML file.", e);
        }
    }

    public static void genericVecModelTraversal(Object root, Consumer<Identifiable> consumer) {
        Class<?> rootClass = root.getClass();

        try {
            Class<?> depthFirstTraverserClass = loadVisitorClasses(rootClass, "DepthFirstTraverserImpl");
            Class<?> functionVisitorClass = loadVisitorClasses(rootClass, "FunctionVisitor");
            Class<?> traversingVisitorClass = loadVisitorClasses(rootClass, "TraversingVisitor");
            Class<?> visitorClass = loadVisitorClasses(rootClass, "Visitor");
            Class<?> traverserClass = loadVisitorClasses(rootClass, "Traverser");

            Object depthFirstTraverser = depthFirstTraverserClass.getConstructor()
                    .newInstance();
            Object functionVisitor = functionVisitorClass.getConstructor(Function.class)
                    .newInstance((Function<Identifiable, Void>) (identifiable) -> {
                        consumer.accept(identifiable);
                        return null;
                    });
            Object traverser = traversingVisitorClass.getConstructor(traverserClass, visitorClass)
                    .newInstance(depthFirstTraverser, functionVisitor);

            rootClass.getMethod("accept", visitorClass)
                    .invoke(root, traverser);
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new VecRdfException("Unable to do generic traversal of VEC DOM due to reflection issues.", e);
        }
    }

    private static Class<?> loadVisitorClasses(Class<?> rootClass, String className) throws ClassNotFoundException {
        return Class.forName(rootClass.getPackageName() + ".visitor." + className, true, rootClass.getClassLoader());
    }

    public static com.foursoft.harness.vec.rdf.common.VecVersion guessVersion(Document document) {
        LOGGER.debug("Trying to guess VEC version.");
        NodeList versions = document.getElementsByTagName("VecVersion");
        if (versions.getLength() == 0) {
            throw new VecRdfException("Unable to find 'VecVersion' tag in.");
        }
        String version = versions.item(0)
                .getTextContent();
        LOGGER.info("VEC version detected: {}", version);
        return com.foursoft.harness.vec.rdf.common.VecVersion.fromVersionString(version);
    }

    public static XMLReader<? extends Identifiable, Identifiable> resolveReader(VecVersion version) {
        String readerClassName = version.getApiPackage() + "." + "VecReader";

        try {
            final Class<?> readerClass = Class.forName(readerClassName);

            return (XMLReader<? extends Identifiable, Identifiable>) readerClass.getConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException | ClassCastException e) {
            throw new VecRdfException(
                    "Error occurred while trying to load VEC Reading Api for Version: '" + version + "' (" +
                            readerClassName + "). Added corresponding jar file to classpath.", e);
        }
    }

    public static XMLWriter<? extends Identifiable> resolveWriter(VecVersion version) {
        String writerClassName = version.getApiPackage() + "." + "VecWriter";

        try {
            final Class<?> writerClass = Class.forName(writerClassName);

            return (XMLWriter<? extends Identifiable>) writerClass.getConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException | ClassCastException e) {
            throw new VecRdfException(
                    "Error occurred while trying to load VEC Writing Api for Version: '" + version + "' (" +
                            writerClassName + ").  Added corresponding jar file to classpath.", e);
        }
    }

}
