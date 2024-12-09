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
package com.foursoft.harness.vec.rdf.common.util;

import com.foursoft.harness.navext.runtime.io.read.XMLReader;
import com.foursoft.harness.navext.runtime.io.write.XMLWriter;
import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.rdf.common.exception.VecRdfException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.Arrays;
import java.util.Objects;

public enum VecVersion {

    V113("1.1.3"), V120("1.2.0"), V121("1.2.1"), V122("1.2.2"), V200("2.0.0"), V201("2.0.1"), V202("2.0.2"), V210(
            "2.1.0");

    private static final Logger LOGGER = LoggerFactory.getLogger(VecVersion.class);

    private final String version;

    VecVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public XMLReader<? extends Identifiable, Identifiable> resolveReader() {
        if ("1.1.3".equals(version)) {
            return new com.foursoft.harness.vec.v113.VecReader();
        }
        if (version.startsWith("1.2.")) {
            return new com.foursoft.harness.vec.v12x.VecReader();
        }
        if (version.startsWith("2.")) {
            return new com.foursoft.harness.vec.v2x.VecReader();
        }
        throw new VecRdfException("No VEC Api for Version: '" + version + "' found.");
    }

    public XMLWriter<? extends Identifiable> resolveWriter() {
        if ("1.1.3".equals(version)) {
            return new com.foursoft.harness.vec.v113.VecWriter();
        }
        if (version.startsWith("1.2.")) {
            return new com.foursoft.harness.vec.v12x.VecWriter();
        }
        if (version.startsWith("2.")) {
            return new com.foursoft.harness.vec.v2x.VecWriter();
        }
        throw new VecRdfException("No VEC Api for Version: '" + version + "' found.");
    }

    public static VecVersion guessVersion(Document document) {
        LOGGER.debug("Trying to guess VEC version.");
        NodeList versions = document.getElementsByTagName("VecVersion");
        if (versions.getLength() == 0) {
            throw new VecRdfException("Unable to find 'VecVersion' tag in.");
        }
        String version = versions.item(0)
                .getTextContent();
        LOGGER.info("VEC version detected: {}", version);
        return VecVersion.fromVersion(version);
    }

    public static VecVersion fromVersion(String version) {
        Objects.requireNonNull(version, "version must not be null");
        VecVersion[] enumConstants = VecVersion.class.getEnumConstants();
        return Arrays.stream(enumConstants)
                .filter(v -> version.equals(v.getVersion()))
                .findFirst()
                .orElseThrow(() -> new VecRdfException("Unsupported VEC Version: " + version));
    }
}
