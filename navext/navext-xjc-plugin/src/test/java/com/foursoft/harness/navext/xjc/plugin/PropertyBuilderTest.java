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
package com.foursoft.harness.navext.xjc.plugin;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.writer.SingleStreamCodeWriter;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PropertyBuilderTest {

    private static final String CLASS_NAME = "com.foursoft.harness.navext.xjc.plugin.generated.Target";

    @Test
    void generatesALazyGetterForAPropertyWithALazyInit() throws Exception {
        final JCodeModel codeModel = new JCodeModel();
        final JDefinedClass targetClass = codeModel._class(CLASS_NAME);

        new PropertyBuilder(codeModel)
                .withName("refSources")
                .withBaseType(codeModel.ref(Set.class)
                                      .narrow(String.class))
                .withLazyInit(JExpr._new(codeModel.ref(HashSet.class)
                                                 .narrow(String.class)))
                .build(targetClass);

        assertThat(generate(codeModel))
                .as("the field must not be initialized eagerly, the getter has to create the set on first access")
                .contains("private Set<String> refSources;")
                .contains("if (refSources == null) {")
                .contains("refSources = new HashSet<>();")
                .contains("return refSources;");
    }

    @Test
    void generatesAPlainGetterForAPropertyWithoutALazyInit() throws Exception {
        final JCodeModel codeModel = new JCodeModel();
        final JDefinedClass targetClass = codeModel._class(CLASS_NAME);

        new PropertyBuilder(codeModel)
                .withName("parent")
                .withBaseType(codeModel.ref(String.class))
                .withSetter()
                .build(targetClass);

        assertThat(generate(codeModel))
                .contains("private String parent;")
                .contains("return parent;")
                .doesNotContain("if (parent == null)");
    }

    private String generate(final JCodeModel codeModel) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        codeModel.build(new SingleStreamCodeWriter(out));
        return out.toString(StandardCharsets.UTF_8);
    }

}
