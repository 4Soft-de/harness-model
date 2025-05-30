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
package com.foursoft.harness.compatibility.vec11to12;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Objects;

public final class TestFiles {

    public static final String OLD_BEETLE = "/vec11x/oldbeetle_vec113.vec";
    public static final String WIRE_PROTECTION_11X = "/vec11x/wire_protection+111_222_333.vec";
    public static final String WIRE_PROTECTION_12X = "/vec12x/wire_protection+111_222_333.vec";

    private TestFiles() {
        // hide constructor
    }

    public static InputStream getInputStream(final String path) {
        final InputStream resourceAsStream = TestFiles.class.getResourceAsStream(path);
        Objects.requireNonNull(resourceAsStream, "Couldn't get resource " + path);
        return new BufferedInputStream(resourceAsStream);
    }

}
