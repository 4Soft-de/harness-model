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
package com.foursoft.harness.compatibility.common;

import com.foursoft.harness.compatibility.vec.common.VecVersion;
import com.foursoft.harness.vec.v12x.VecContent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VecVersionTest {

    @Test
    void testCanBeConvertedTo() {
        assertThat(VecVersion.VEC11X.canBeConvertedTo(VecVersion.VEC12X)).isTrue();
        assertThat(VecVersion.VEC12X.canBeConvertedTo(VecVersion.VEC11X)).isTrue();
    }

    @Test
    void testFindVersion() {
        assertThat(VecVersion.findVersion("1"))
                .isNotPresent();
        assertThat(VecVersion.findVersion("<VecVersion>1.0.0</VecVersion>"))
                .isNotPresent();
        assertThat(VecVersion.findVersion("1.2.3"))
                .isNotPresent();
        assertThat(VecVersion.findVersion("<VecVersion>1.2.3</VecVersion>"))
                .isPresent()
                .get()
                .isEqualTo(VecVersion.VEC12X);

        final VecContent vec12x = new VecContent();
        vec12x.setVecVersion(VecVersion.VEC12X.getCurrentVersion());
        assertThat(VecVersion.findVersion(vec12x))
                .isPresent()
                .get()
                .isEqualTo(VecVersion.VEC12X);
        vec12x.setVecVersion("1.2.3");
        assertThat(VecVersion.findVersion(vec12x))
                .isPresent()
                .get()
                .isEqualTo(VecVersion.VEC12X);

        final com.foursoft.harness.vec.v113.VecContent vec11x = new com.foursoft.harness.vec.v113.VecContent();
        vec11x.setVecVersion(VecVersion.VEC11X.getCurrentVersion());
        assertThat(VecVersion.findVersion(vec11x))
                .isPresent()
                .get()
                .isEqualTo(VecVersion.VEC11X);
        vec11x.setVecVersion("1.1.1");
        assertThat(VecVersion.findVersion(vec11x))
                .isPresent()
                .get()
                .isEqualTo(VecVersion.VEC11X);

        assertThat(VecVersion.findVersion(new VecContent()))
                .isNotPresent();
    }

}
