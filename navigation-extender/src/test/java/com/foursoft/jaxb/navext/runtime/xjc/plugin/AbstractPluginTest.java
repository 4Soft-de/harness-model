/*-
 * ========================LICENSE_START=================================
 * xjc-plugin
 * %%
 * Copyright (C) 2019 4Soft GmbH
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
package com.foursoft.jaxb.navext.runtime.xjc.plugin;

import junit.framework.Assert;
import org.jvnet.jaxb2.maven2.test.RunXJC2Mojo;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;

public abstract class AbstractPluginTest extends RunXJC2Mojo {

    private final File DEFAULT_CLASSES_DIR = new File(getBaseDir(), "target/test-classes");

    protected abstract String getTestName();

    @Override
    public File getSchemaDirectory() {
        return new File(getBaseDir(), "src/test/resources/" + getTestName());
    }

    @Override
    protected File getGeneratedDirectory() {
        return new File(getBaseDir(), "target/gen-src/plugin-test/" + getTestName());
    }

    @Override
    public void testExecute() throws Exception {
        super.testExecute();

        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        final StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(null, null, null);

        standardFileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(DEFAULT_CLASSES_DIR));

        final DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        final CompilationTask task = compiler.getTask(null, standardFileManager, diagnostics, null, null,
                standardFileManager.getJavaFileObjects(findSources()));

        final Boolean success = task.call();

        standardFileManager.close();

        if (!success) {
            for (final Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                log.error("Code: " + diagnostic.getCode());
                log.error("Kind: " + diagnostic.getKind());
                log.error("Position: " + diagnostic.getPosition());
                log.error("Start Position: " + diagnostic.getStartPosition());
                log.error("End Position: " + diagnostic.getEndPosition());
                log.error("Source: " + diagnostic.getSource());
                log.error("Message: " + diagnostic.getMessage(Locale.getDefault()));
            }

            Assert.fail("Compilation failed!");
        }
    }

    private File[] findSources() throws IOException {
        return Files.walk(getGeneratedDirectory().toPath())
                .map(Path::toFile)
                .filter(File::isFile)
                .filter(f -> f.getName()
                        .endsWith(".java"))
                .toArray(i -> new File[i]);
    }

}
