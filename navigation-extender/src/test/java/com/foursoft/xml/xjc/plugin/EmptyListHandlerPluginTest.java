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
package com.foursoft.xml.xjc.plugin;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.jvnet.jaxb2.maven2.AbstractXJC2Mojo;

public class EmptyListHandlerPluginTest extends AbstractPluginTest {

    @Override
    protected String getTestName() {
        return "emptylist";
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void configureMojo(final AbstractXJC2Mojo mojo) {
        super.configureMojo(mojo);

        mojo.setForceRegenerate(true);

        mojo.setBindingIncludes(new String[] { "basic.xjb" });

        mojo.setExtension(true);

    }

    @Override
    public List<String> getArgs() {

        final List<String> args = new ArrayList<>(super.getArgs());

        args.add("-Xext-navs");
        args.add("-Xnull-empty-lists");

        return args;

    }

    @Override
    public void testExecute() throws Exception {
        super.testExecute();

        assertTypeSafeIDREF();

    }

    private void assertTypeSafeIDREF() throws Exception {
        final Class<?> forName = Class.forName("de.foursoft.xml.xjc.test.NavsChildA");

        final Method m = forName.getMethod("getRefBs");

        Assertions.assertThat(m.getGenericReturnType()
                .getTypeName()).isEqualTo("java.util.List<de.foursoft.xml.xjc.test.NavsChildB>");

    }

}
