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
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Enumeration of the VEC versions that are compile-incompatible with each other. Each entry stands for the
 * releases one model can read.
 * <p>
 * The releases of the 1.x line are listed one by one, as the {@code VecVersion} enumeration of the VEC schema
 * defines them. Since 2.0.0 the VEC follows semantic versioning, so every 2.x release belongs to
 * {@link #VEC2XX}, including releases published after this library.
 */
public enum VecVersion {

    // The VEC versions should be added in order of their version, canBeConvertedTo depends on it.

    /**
     * Enum value for the VEC 1.1.X.
     */
    VEC11X("1.1.X", List.of("1.1.1", "1.1.2", "1.1.3"), Matching.KNOWN_VERSIONS),

    /**
     * Enum value for the VEC 1.2.X.
     */
    VEC12X("1.2.X", List.of("1.2.0", "1.2.1", "1.2.2"), Matching.KNOWN_VERSIONS),

    /**
     * Enum value for the VEC 2.X.X.
     */
    VEC2XX("2.X.X", List.of("2.0.0", "2.0.1", "2.0.2", "2.1.0", "2.2.0"), Matching.SAME_MAJOR_VERSION);

    private static final Logger LOGGER = LoggerFactory.getLogger(VecVersion.class);

    private static final Pattern VEC_VERSION_ELEMENT = Pattern.compile("<VecVersion>\\s*([^<]*?)\\s*</VecVersion>");

    private final String identifier;
    private final List<String> knownVersions;
    private final Matching matching;
    private final Pattern identifierPattern;

    VecVersion(final String identifier, final List<String> knownVersions, final Matching matching) {
        this.identifier = identifier;
        this.knownVersions = knownVersions;
        this.matching = matching;
        identifierPattern = createIdentifierPattern();
    }

    /**
     * Returns the newest release of this entry, which is the version of the schema its model is generated from
     * and the version a document converted to this entry is written with (e.g. 1.2.2 for 1.2.X).
     *
     * @return The newest release of this entry.
     */
    public String getCurrentVersion() {
        return knownVersions.get(knownVersions.size() - 1);
    }

    /**
     * Returns the releases of this entry, in ascending order. For {@link #VEC2XX} these are the releases known
     * to this library; later 2.x releases belong to it as well.
     *
     * @return The releases of this entry, never empty.
     */
    public List<String> getKnownVersions() {
        return knownVersions;
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
     * Tries to find the version within the given String, which contains the {@code VecVersion} element of a VEC
     * document, e.g. its beginning.
     *
     * @param input String which should contain a version.
     * @return Possibly-empty Optional containing the found version.
     */
    public static Optional<VecVersion> findVersion(final String input) {
        final Matcher matcher = VEC_VERSION_ELEMENT.matcher(input);
        if (!matcher.find()) {
            LOGGER.debug("No VecVersion element found in the given input: {}", input);
            return Optional.empty();
        }
        return findVersionByNumber(matcher.group(1));
    }

    /**
     * Tries to find the version for the given {@code VecContent}.
     *
     * @param value VecContent which should have a version.
     * @return Possibly-empty Optional containing the found version.
     */
    public static Optional<VecVersion> findVersion(final HasVecVersion value) {
        return findVersionByNumber(value.getVecVersion());
    }

    /**
     * Returns the entry a version number as written in the {@code VecVersion} element belongs to, e.g.
     * {@link #VEC12X} for {@code 1.2.0}.
     *
     * @param versionNumber Version number, e.g. {@code 1.2.2}. May be {@code null}.
     * @return Possibly-empty Optional containing the entry, empty for an unknown 1.x release.
     */
    public static Optional<VecVersion> findVersionByNumber(final String versionNumber) {
        final Optional<VecVersion> vecVersion = versionNumber == null
                ? Optional.empty()
                : Arrays.stream(values())
                        .filter(c -> c.matches(versionNumber.trim()))
                        .findFirst();
        if (vecVersion.isEmpty()) {
            LOGGER.debug("No VecVersion could be determined by the given version: {}", versionNumber);
        }
        return vecVersion;
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
                ", currentVersion='" + getCurrentVersion() + '\'' +
                '}';
    }

    private boolean matches(final String versionNumber) {
        return switch (matching) {
            case KNOWN_VERSIONS -> knownVersions.contains(versionNumber);
            case SAME_MAJOR_VERSION -> identifierPattern.matcher(versionNumber).matches();
        };
    }

    private Pattern createIdentifierPattern() {
        final String identifierAsRegex = identifier
                .replace(".", "\\.")   // Dots are special RegEx characters, need to be escaped.
                .replace("X", "\\d+"); // In theory an X could be replaced with a multi-digit value.
        return Pattern.compile(identifierAsRegex);
    }

    /**
     * How a version number is assigned to an entry.
     */
    private enum Matching {

        /**
         * Only the releases listed for the entry belong to it. Used for the 1.x line, which is complete.
         */
        KNOWN_VERSIONS,

        /**
         * Every version number of the form of the identifier belongs to the entry. Used since the VEC follows
         * semantic versioning, so that a later minor or patch release is still read.
         */
        SAME_MAJOR_VERSION
    }

}
