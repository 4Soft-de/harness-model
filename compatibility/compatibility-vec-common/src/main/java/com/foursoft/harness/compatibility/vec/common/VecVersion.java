/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC Common
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
package com.foursoft.harness.compatibility.vec.common;

import com.foursoft.harness.vec.common.HasVecVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Enumeration containing all supported VEC Versions.
 */
public enum VecVersion {

    // The VEC versions should be added in order of their version, canBeConvertedTo depends on it.

    /**
     * Enum value for the VEC 1.1.X.
     */
    VEC11X("1.1.X", "1.1.3"),

    /**
     * Enum value for the VEC 1.2.X.
     */
    VEC12X("1.2.X", "1.2.0");

    private static final Logger LOGGER = LoggerFactory.getLogger(VecVersion.class);

    private static final String VEC_VERSION_XML_FORMAT = "<VecVersion>%s</VecVersion>";

    private final String identifier;
    private final Pattern marker;
    private final String currentVersion;

    VecVersion(final String identifier, final String currentVersion) {
        this.identifier = identifier;
        this.currentVersion = currentVersion;
        marker = createMarkerPattern(identifier);
    }

    /**
     * Returns the actual current version of the enum entry (eg. 1.1.3 for 1.1.X).
     *
     * @return The actual current version of the enum entry.
     */
    public String getCurrentVersion() {
        return currentVersion;
    }

    /**
     * Returns the identifier of the enum entry. Shouldn't be used for writing it to a file.
     *
     * @return The identifier of the enum entry.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Tries to find the version within the given String.
     *
     * @param input String which should contain a version.
     * @return Possibly-empty Optional containing the found version.
     */
    public static Optional<VecVersion> findVersion(final String input) {
        final Optional<VecVersion> vecVersion = Arrays.stream(VecVersion.values())
                .filter(c -> c.marker.matcher(input).find())
                .findFirst();
        if (vecVersion.isEmpty()) {
            LOGGER.debug("No VecVersion could be determined by the given input: {}", input);
        }
        return vecVersion;
    }

    /**
     * Tries to find the version for the given {@code VecContent}.
     *
     * @param value VecContent which should have a version.
     * @return Possibly-empty Optional containing the found version.
     */
    public static Optional<VecVersion> findVersion(final HasVecVersion value) {
        final String vecVersionFormatted = String.format(VEC_VERSION_XML_FORMAT, value.getVecVersion());
        return findVersion(vecVersionFormatted);
    }

    /**
     * Method telling whether this Version can be converted to the given version or not.
     * <p>
     * This will most likely return {@code true} if there is only one minor or major version in between.
     *
     * @param otherVersion Version to check it this version can be converted to.
     * @return {@code true} if this version can be converted to the given version, else {@code false}.
     */
    public boolean canBeConvertedTo(final VecVersion otherVersion) {
        final int thisPosition = ordinal();
        final int otherPosition = otherVersion.ordinal();
        return Math.abs(thisPosition - otherPosition) == 1;
    }

    @Override
    public String toString() {
        return "VecVersion{" +
                "identifier='" + identifier + '\'' +
                ", currentVersion='" + currentVersion + '\'' +
                '}';
    }

    private static Pattern createMarkerPattern(final String version) {
        final String versionAsRegex = version
                .replace("X", "\\d+")  // In theory an X could be replaced with a double-digit value.
                .replace(".", "\\.");  // Dots are special RegEx characters, need to be escaped.

        // Slashes are also special RegEx characters.
        final String fullStringToFind = String.format(VEC_VERSION_XML_FORMAT, versionAsRegex)
                .replace("/", "\\/");  // Slashes are special RegEx characters, need to be escaped.
        return Pattern.compile(fullStringToFind);
    }

}
