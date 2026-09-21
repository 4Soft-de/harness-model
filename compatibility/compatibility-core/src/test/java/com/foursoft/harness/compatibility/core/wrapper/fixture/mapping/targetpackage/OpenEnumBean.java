/*-
 * ========================LICENSE_START=================================
 * Compatibility Core
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
package com.foursoft.harness.compatibility.core.wrapper.fixture.mapping.targetpackage;

import com.foursoft.harness.compatibility.core.wrapper.fixture.openenum.Kind;

import java.util.Collection;
import java.util.List;

/**
 * Target side of a fixture bean carrying the accessors an open enumeration property is generated
 * with, next to methods which merely look like them by name. The proxies must run the former
 * unintercepted and route the latter to the wrapped object.
 */
public class OpenEnumBean {

    public Kind getKindLiteral() {
        return Kind.ONE;
    }

    public void setKindLiteral(final Kind value) {
        // nothing to do, the proxy must not intercept this
    }

    public List<Kind> getKindLiterals() {
        return List.of(Kind.ONE);
    }

    public void addKindLiteral(final Kind value) {
        // nothing to do, the proxy must not intercept this
    }

    public void setKindLiterals(final Collection<? extends Kind> values) {
        // nothing to do, the proxy must not intercept this
    }

    /** Ends in {@code Literal}, but returns no literal. */
    public String getTextLiteral() {
        return "text";
    }

    /** Ends in {@code Literals}, but returns no literals. */
    public List<String> getTextLiterals() {
        return List.of("text");
    }

    /** Takes a literal, but returns something. */
    public Kind toKindLiteral(final Kind value) {
        return value;
    }

    /** Takes a literal, but takes more. */
    public void setKindLiteral(final Kind value, final String reason) {
        // nothing to do, the proxy must intercept this
    }

}
