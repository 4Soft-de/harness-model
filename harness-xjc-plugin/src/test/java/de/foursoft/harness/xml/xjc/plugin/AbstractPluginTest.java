package de.foursoft.harness.xml.xjc.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.jvnet.jaxb2.maven2.test.RunXJC2Mojo;

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

            fail("Compilation failed!");
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
