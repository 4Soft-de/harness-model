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

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;

/**
 * References to the runtime types the generated code depends on. They are resolved by name from the
 * runtime package, so that this plugin does not depend on the model the generated code belongs to.
 *
 * @param literal       The interface every open enumeration literal implements.
 * @param customLiteral The marker interface of the generated {@code Custom} literals.
 * @param registry      The registry holding the literals contributed by service providers.
 */
record OpenEnumRuntime(JClass literal, JClass customLiteral, JClass registry) {

    static OpenEnumRuntime of(final JCodeModel codeModel, final String runtimePackage) {
        return new OpenEnumRuntime(codeModel.ref(runtimePackage + ".OpenEnumLiteral"),
                                   codeModel.ref(runtimePackage + ".CustomOpenEnumLiteral"),
                                   codeModel.ref(runtimePackage + ".OpenEnumLiterals"));
    }

}
