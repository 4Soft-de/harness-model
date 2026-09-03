/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.1.X To VEC 1.2.X
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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
package com.foursoft.harness.compatibility.vec11to12.util;

import com.foursoft.harness.compatibility.core.util.XmlFieldInitializer;

public class InitializeFields {

    private InitializeFields() {
    }

    /**
     * Initializes the fields of the given VEC 1.2.X Visitable.
     * <p>
     * <b>Notice:</b> Independent object trees may now be initialized in parallel. Trees sharing the same
     * {@link com.foursoft.harness.compatibility.core.CompatibilityContext} may not.
     */
    public static void initializeFields(final com.foursoft.harness.vec.v12x.visitor.Visitable content) {
        final com.foursoft.harness.vec.v12x.visitor.Visitor<Void, RuntimeException> proxyInstance =
                XmlFieldInitializer.visitorProxy(com.foursoft.harness.vec.v12x.visitor.Visitor.class);

        content.accept(
                new com.foursoft.harness.vec.v12x.visitor.TraversingVisitor<>(
                        new com.foursoft.harness.vec.v12x.visitor.DepthFirstTraverserImpl<>(),
                        proxyInstance));
    }

    /**
     * Initializes the fields of the given VEC 1.1.X Visitable.
     * <p>
     * <b>Notice:</b> Independent object trees may now be initialized in parallel. Trees sharing the same
     * {@link com.foursoft.harness.compatibility.core.CompatibilityContext} may not.
     */
    public static void initializeFields(final com.foursoft.harness.vec.v113.visitor.Visitable content) {
        final com.foursoft.harness.vec.v113.visitor.Visitor<Void, RuntimeException> proxyInstance =
                XmlFieldInitializer.visitorProxy(com.foursoft.harness.vec.v113.visitor.Visitor.class);

        content.accept(
                new com.foursoft.harness.vec.v113.visitor.TraversingVisitor<>(
                        new com.foursoft.harness.vec.v113.visitor.DepthFirstTraverserImpl<>(),
                        proxyInstance));
    }

}
