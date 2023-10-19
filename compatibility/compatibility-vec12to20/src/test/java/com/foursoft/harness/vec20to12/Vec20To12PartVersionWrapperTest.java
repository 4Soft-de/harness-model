/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.2.X To VEC 2.X.X
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
package com.foursoft.harness.vec20to12;

import com.foursoft.harness.TestFiles;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v12x.VecPartVersion;
import com.foursoft.harness.vec12to20.wrapper.AbstractBaseWrapperTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class Vec20To12PartVersionWrapperTest extends AbstractBaseWrapperTest {

    @Test
    void invokeTest() throws IOException {

        try (final InputStream inputOriginal = TestFiles.getInputStream(TestFiles.OLD_BEETLE_V12X)) {
            final com.foursoft.harness.vec.v12x.VecContent originalContent =
                    new com.foursoft.harness.vec.v12x.VecReader().read(inputOriginal);
            assertThat(originalContent).isNotNull();

            final Optional<VecPartVersion> vecPartVersion = originalContent.getPartVersions().stream()
                    .filter(pv -> pv.getPreferredUseCase() != null && !pv.getPreferredUseCase().isEmpty())
                    .sorted(Comparator.comparing(VecPartVersion::getXmlId))
                    .collect(StreamUtils.findOneOrNone());

            assertThat(vecPartVersion).isPresent();
            assertThat(vecPartVersion.get().getPreferredUseCase()).isEqualTo("Normal Connector");
        }
    }
} 
