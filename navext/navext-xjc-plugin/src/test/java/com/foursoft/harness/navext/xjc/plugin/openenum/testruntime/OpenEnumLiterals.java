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
package com.foursoft.harness.navext.xjc.plugin.openenum.testruntime;

import java.util.ArrayList;
import java.util.List;

/**
 * Test stand-in for the runtime type of the same name. Instead of a {@code ServiceLoader} it holds
 * the contributed literals in a list the tests fill directly.
 *
 * @see OpenEnumLiteral
 */
public final class OpenEnumLiterals {

    private static final List<OpenEnumLiteral> CONTRIBUTED = new ArrayList<>();

    private OpenEnumLiterals() {
        throw new AssertionError("OpenEnumLiterals must not be instantiated.");
    }

    public static <T extends OpenEnumLiteral> T resolve(final Class<T> type, final String value) {
        if (type == null || value == null) {
            return null;
        }
        return CONTRIBUTED.stream()
                .filter(type::isInstance)
                .filter(literal -> literal.value()
                        .equals(value))
                .findFirst()
                .map(type::cast)
                .orElse(null);
    }

    public static void contribute(final OpenEnumLiteral literal) {
        CONTRIBUTED.add(literal);
    }

    public static void reload() {
        CONTRIBUTED.clear();
    }

}
