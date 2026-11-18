/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC Common
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
package com.foursoft.harness.compatibility.vec.common;

import com.foursoft.harness.vec.common.HasVecVersion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class VecVersionTest {

    @Test
    void listsTheKnownReleasesInAscendingOrder() {
        assertThat(VecVersion.VEC11X.getKnownVersions()).containsExactly("1.1.1", "1.1.2", "1.1.3");
        assertThat(VecVersion.VEC12X.getKnownVersions()).containsExactly("1.2.0", "1.2.1", "1.2.2");
        assertThat(VecVersion.VEC2XX.getKnownVersions()).containsExactly("2.0.0", "2.0.1", "2.0.2", "2.1.0",
                                                                         "2.2.0");
    }

    @Test
    void currentVersionIsTheNewestKnownRelease() {
        assertThat(VecVersion.VEC11X.getCurrentVersion()).isEqualTo("1.1.3");
        assertThat(VecVersion.VEC12X.getCurrentVersion()).isEqualTo("1.2.2");
        assertThat(VecVersion.VEC2XX.getCurrentVersion()).isEqualTo("2.2.0");
    }

    @ParameterizedTest
    @CsvSource({"1.1.1, VEC11X", "1.1.3, VEC11X", "1.2.0, VEC12X", "' 1.2.2 ', VEC12X", "2.0.0, VEC2XX",
            "2.2.0, VEC2XX", "2.3.0, VEC2XX", "2.10.1, VEC2XX"})
    void findsTheEntryOfARelease(final String versionNumber, final VecVersion expected) {
        final HasVecVersion content = () -> versionNumber;

        assertThat(VecVersion.findVersionByNumber(versionNumber)).contains(expected);
        assertThat(VecVersion.findVersion(content)).contains(expected);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.0.0", "1.1.4", "1.2.3", "3.0.0", "2.2", "2.2.0-SNAPSHOT", ""})
    void findsNoEntryForANumberThatIsNoRelease(final String versionNumber) {
        assertThat(VecVersion.findVersionByNumber(versionNumber)).isEmpty();
    }

    @Test
    void findsNoEntryWithoutVersionNumber() {
        final HasVecVersion content = () -> null;

        assertThat(VecVersion.findVersionByNumber(null)).isEmpty();
        assertThat(VecVersion.findVersion(content)).isEmpty();
    }

    @Test
    void namesIdentifierAndCurrentVersion() {
        assertThat(VecVersion.VEC2XX)
                .hasToString("VecVersion{identifier='2.X.X', currentVersion='2.2.0'}")
                .extracting(VecVersion::getIdentifier)
                .isEqualTo("2.X.X");
    }

}
