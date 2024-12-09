/*-
 * ========================LICENSE_START=================================
 * VEC RDF Common
 * %%
 * Copyright (C) 2024 4Soft GmbH
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
package com.foursoft.harness.vec.rdf.common;

import com.foursoft.harness.vec.rdf.common.exception.VecRdfException;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

public enum VecVersion {
    V113("1.1.3", Packages.V113, true),
    V120("1.2.0", Packages.V12X, false),
    V121("1.2.1", Packages.V12X, false),
    V122("1.2.2", Packages.V12X, true),
    V200("2.0.0", Packages.V2X, false),
    V201("2.0.1", Packages.V2X, false),
    V202("2.0.2", Packages.V2X, false),
    V210("2.1.0", Packages.V2X, true);

    private final static String modelLocationPattern = "/vec/v%1$s/vec-%1$s.mdxml";
    private final String versionString;
    private final String apiPackage;
    private final boolean latest;

    VecVersion(final String versionString, final String apiPackage,
               final boolean latest) {
        this.versionString = versionString;
        this.apiPackage = apiPackage;
        this.latest = latest;
    }

    public String getVersionString() {
        return versionString;
    }

    public String getApiPackage() {
        return apiPackage;
    }

    public boolean isLatest() {
        return latest;
    }

    public InputStream getModelInputStream() {
        final String modelLocation = String.format(modelLocationPattern, versionString);
        final InputStream resourceAsStream = this.getClass().getResourceAsStream(modelLocation);
        return resourceAsStream;
    }

    public static VecVersion findLatestVersionForApiPackage(final String apiPackage) {
        return Arrays.stream(values())
                .filter(literal -> literal.apiPackage.equals(apiPackage))
                .filter(VecVersion::isLatest)
                .findAny()
                .orElseThrow(() -> new VecRdfException("No UML model found for VEC API package: " + apiPackage));
    }

    public static VecVersion fromVersionString(String version) {
        Objects.requireNonNull(version, "version must not be null");
        VecVersion[] enumConstants = VecVersion.class.getEnumConstants();
        return Arrays.stream(enumConstants)
                .filter(v -> version.equals(v.getVersionString()))
                .findFirst()
                .orElseThrow(() -> new VecRdfException("Unsupported VEC Version: " + version));
    }

    private final static class Packages {
        public static final String V113 = "com.foursoft.harness.vec.v113";
        public static final String V12X = "com.foursoft.harness.vec.v12x";
        public static final String V2X = "com.foursoft.harness.vec.v2x";
    }

}
