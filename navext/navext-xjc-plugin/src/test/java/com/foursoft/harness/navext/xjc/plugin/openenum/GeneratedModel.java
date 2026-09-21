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

import com.foursoft.harness.navext.xjc.plugin.openenum.testruntime.OpenEnumLiteral;
import com.sun.tools.xjc.Driver;
import jakarta.xml.bind.annotation.XmlElement;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Runs XJC over the fixture schema with the open enumerations plugin enabled, compiles what it
 * generated and gives reflective access to the result.
 *
 * <p>
 * Compiling is not an afterthought: it is what proves that the emitted constant names, the nested
 * {@code Custom} class and the added accessors are valid Java at all.
 * </p>
 */
final class GeneratedModel {

    static final String PACKAGE = "com.foursoft.harness.navext.xjc.plugin.openenum.generated";
    static final String RUNTIME_OPTION =
            "-Xopen-enums-runtime:com.foursoft.harness.navext.xjc.plugin.openenum.testruntime";
    static final String NAMES_OPTION = "-Xopen-enums-names:sample-open-enum-names.xml";

    private final ClassLoader classLoader;

    private GeneratedModel(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * @param name       Distinguishes the output directories of several generation runs.
     * @param extraArgs  Additional XJC arguments.
     * @return The generated and compiled model.
     * @throws IllegalStateException If XJC or the compiler rejects the result. The message contains
     *                               their output.
     */
    static GeneratedModel generate(final String name, final String... extraArgs) throws Exception {
        final Path baseDir = baseDir();
        final Path sources = recreate(baseDir.resolve("target/generated-test-sources/openenum-" + name));
        final Path classes = recreate(baseDir.resolve("target/openenum-classes-" + name));

        runXjc(baseDir.resolve("src/test/resources/openenum/sample.xsd"), sources, extraArgs);
        compile(sources, classes);

        return new GeneratedModel(new URLClassLoader(new URL[]{classes.toUri()
                                                                       .toURL()},
                                                     GeneratedModel.class.getClassLoader()));
    }

    Class<?> load(final String className) throws ClassNotFoundException {
        return classLoader.loadClass(className);
    }

    boolean exists(final String className) {
        try {
            classLoader.loadClass(className);
            return true;
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * @return The result of {@code <simpleName>Literal.of(value)}.
     */
    OpenEnumLiteral literalOf(final String simpleName, final String value) throws Exception {
        return (OpenEnumLiteral) load(PACKAGE + "." + simpleName + "Literal").getMethod("of", String.class)
                .invoke(null, value);
    }

    /**
     * @return The constant {@code <simpleName>.<constantName>}.
     */
    Object constant(final String simpleName, final String constantName) throws Exception {
        return Stream.of(load(PACKAGE + "." + simpleName).getEnumConstants())
                .filter(constant -> ((Enum<?>) constant).name()
                        .equals(constantName))
                .findFirst()
                .orElseThrow(() -> new AssertionError(simpleName + " has no constant " + constantName));
    }

    /**
     * Stands in for a literal an API consumer contributes with an enum of its own.
     */
    OpenEnumLiteral contributedLiteral(final Class<?> literalInterface, final String value) {
        final InvocationHandler handler = (proxy, method, args) -> switch (method.getName()) {
            case "value" -> value;
            case "isCustom" -> false;
            case "toString" -> "contributed(" + value + ")";
            case "hashCode" -> value.hashCode();
            case "equals" -> proxy == args[0];
            default -> throw new UnsupportedOperationException(method.getName());
        };
        return (OpenEnumLiteral) Proxy.newProxyInstance(literalInterface.getClassLoader(),
                                                        new Class<?>[]{literalInterface}, handler);
    }

    private static void runXjc(final Path schema, final Path sources, final String... extraArgs) throws Exception {
        final List<String> args = new ArrayList<>(List.of(
                "-d", sources.toString(),
                "-p", PACKAGE,
                "-extension",
                "-Xopen-enums",
                RUNTIME_OPTION));
        args.addAll(List.of(extraArgs));
        args.add(schema.toString());

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final int result;
        try (final PrintStream out = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            result = Driver.run(args.toArray(new String[0]), out, out);
        }
        if (result != 0) {
            throw new IllegalStateException("XJC failed:" + System.lineSeparator()
                                                    + output.toString(StandardCharsets.UTF_8));
        }
    }

    private static void compile(final Path sources, final Path classes) throws Exception {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        final boolean compiled;
        try (final StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
             final PrintStream out = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, List.of(classes.toFile()));
            fileManager.setLocation(StandardLocation.CLASS_PATH, compileClasspath());

            compiled = compiler.getTask(new java.io.PrintWriter(out, true), fileManager, null, null, null,
                                        fileManager.getJavaFileObjectsFromFiles(javaFiles(sources)))
                    .call();
        }
        if (!compiled) {
            throw new IllegalStateException("The generated sources do not compile:" + System.lineSeparator()
                                                    + output.toString(StandardCharsets.UTF_8));
        }
    }

    /**
     * The generated code needs the JAXB annotations and the runtime stand-ins. Both are located
     * through their code source rather than through {@code java.class.path}, which under Surefire is
     * only the booter jar.
     */
    private static List<File> compileClasspath() throws Exception {
        return List.of(codeSourceOf(XmlElement.class), codeSourceOf(OpenEnumLiteral.class));
    }

    private static File codeSourceOf(final Class<?> type) throws Exception {
        return new File(type.getProtectionDomain()
                                .getCodeSource()
                                .getLocation()
                                .toURI());
    }

    private static List<File> javaFiles(final Path sources) throws Exception {
        try (final Stream<Path> paths = Files.walk(sources)) {
            return paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString()
                            .endsWith(".java"))
                    .map(Path::toFile)
                    .toList();
        }
    }

    private static Path recreate(final Path directory) throws Exception {
        if (Files.exists(directory)) {
            try (final Stream<Path> paths = Files.walk(directory)) {
                for (final Path path : paths.sorted(Comparator.reverseOrder())
                        .toList()) {
                    Files.delete(path);
                }
            }
        }
        return Files.createDirectories(directory);
    }

    /**
     * The module directory, derived from the location of the test classes.
     */
    private static Path baseDir() throws Exception {
        return codeSourceOf(GeneratedModel.class).toPath()
                .getParent()
                .getParent();
    }

}
