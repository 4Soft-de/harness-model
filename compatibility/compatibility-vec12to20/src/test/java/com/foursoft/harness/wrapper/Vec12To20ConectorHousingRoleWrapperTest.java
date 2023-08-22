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
package com.foursoft.harness.wrapper;

import com.foursoft.harness.TestFiles;
import com.foursoft.harness.compatibility.vec12to20.util.DefaultVecReader;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v2x.VecCompositionSpecification;
import com.foursoft.harness.vec.v2x.VecConnectorHousingRole;
import com.foursoft.harness.vec.v2x.VecContent;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class Vec12To20ConectorHousingRoleWrapperTest extends AbstractBaseWrapperTest {

    @Test
    void invokeTest() throws IOException {
        final InputStream inputOriginal = TestFiles.getInputStream(TestFiles.OLD_BEETLE_V12X);
        final VecContent originalContent = DefaultVecReader.read(inputOriginal, "test");
        assertThat(originalContent).isNotNull();

        final Optional<VecPartOccurrence> connector = originalContent.getDocumentVersions().stream()
                .filter(d -> d.getDocumentType().equals("HarnessDescription"))
                .flatMap(d -> d.getSpecificationsWithType(VecCompositionSpecification.class).stream())
                .flatMap(StreamUtils.toStream(VecCompositionSpecification::getComponents))
                .filter(c -> c.getIdentification().equals("XA.MX1.1"))
                .findAny();

        assertThat(connector).isPresent();

        final Optional<VecConnectorHousingRole> housingRole = connector.get()
                .getRolesWithType(VecConnectorHousingRole.class)
                .stream().findAny();

        assertThat(housingRole).isPresent();
        assertThat(housingRole.get().getComponentConnector()).isNotEmpty();
    }

} 
