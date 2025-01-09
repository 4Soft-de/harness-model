/*-
 * ========================LICENSE_START=================================
 * NavExt XJC Plugin
 * %%
 * Copyright (C) 2019 - 2025 4Soft GmbH
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
package com.foursoft.harness.navext.xjc.plugin;

import org.junit.jupiter.api.Test;
import org.jvnet.higherjaxb.mojo.AbstractHigherjaxbParmMojo;
import org.jvnet.higherjaxb.mojo.HigherjaxbMojo;
import org.jvnet.higherjaxb.mojo.testing.AbstractMojoTest;
import org.jvnet.higherjaxb.mojo.testing.SLF4JLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.List;

public abstract class RunHigherJaxbMojoTest extends AbstractMojoTest {
    protected Logger logger = LoggerFactory.getLogger(RunHigherJaxbMojoTest.class);

    @Test
    public void testExecute() throws Exception {
        initMojo().execute();
    }

    protected AbstractHigherjaxbParmMojo<?> initMojo() {
        final AbstractHigherjaxbParmMojo<?> mojo = createMojo();
        configureMojo(mojo);
        return mojo;
    }

    protected AbstractHigherjaxbParmMojo<?> createMojo() {
        return new HigherjaxbMojo();
    }

    protected void configureMojo(final AbstractHigherjaxbParmMojo<?> mojo) {
        mojo.setLog(new SLF4JLogger(logger));
        mojo.setProject(createMavenProject());
        mojo.setSchemaDirectory(getSchemaDirectory());
        mojo.setGenerateDirectory(getGenerateDirectory());
        mojo.setGeneratePackage(getGeneratePackage());
        mojo.setArgs(getArgs());
        mojo.setVerbose(isVerbose());
        mojo.setDebug(isDebug());
        mojo.setWriteCode(isWriteCode());

        mojo.setRemoveOldOutput(true);
        mojo.setForceRegenerate(false);
        mojo.setExtension(true);
    }

    public void check() throws Exception {
    }

    public List<String> getArgs() {
        return Collections.emptyList();
    }

    public String getGeneratePackage() {
        return null;
    }

    public boolean isDebug() {
        return true;
    }

    public boolean isVerbose() {
        return true;
    }

    public boolean isWriteCode() {
        return true;
    }

    public File getSchemaDirectory() {
        return getDirectory("src/main/resources");
    }

    public File getGenerateDirectory() {
        return getDirectory("target/generated-test-sources/xjc");
    }

    protected File getDirectory(String path) {
        return new File(getBaseDir(), path);
    }

    protected File getBaseDir() {
        try {
            return (new File(getClass().getProtectionDomain().getCodeSource()
                                     .getLocation().toURI())).getParentFile().getParentFile()
                    .getAbsoluteFile();
        } catch (Exception ex) {
            throw new AssertionError(ex);
        }
    }
}
