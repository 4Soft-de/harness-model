/*-
 * ========================LICENSE_START=================================
 * vec-v12x
 * %%
 * Copyright (C) 2020 - 2022 4Soft GmbH
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
open module com.foursoft.harness.vec.v12x {
    requires transitive com.foursoft.harness.vec.common;
    requires com.foursoft.jaxb.navext.runtime;

    requires java.xml;
    requires java.xml.bind;
    requires java.annotation;
    requires org.slf4j;

    exports com.foursoft.harness.vec.v12x;
    exports com.foursoft.harness.vec.v12x.visitor;
    exports com.foursoft.harness.vec.v12x.navigations;
    exports com.foursoft.harness.vec.v12x.predicates;
    exports com.foursoft.harness.vec.v12x.validation;
}
