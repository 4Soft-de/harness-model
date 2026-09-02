/*-
 * ========================LICENSE_START=================================
 * VEC 2.X
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
package com.foursoft.harness.vec.v2x.traversal;

import com.foursoft.harness.vec.v2x.VecCompositionSpecification;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import com.foursoft.harness.vec.v2x.VecRole;
import com.foursoft.harness.vec.v2x.navigations.VecNavs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RolesTest {

    private final VecDocumentVersion documentVersion = new VecDocumentVersion();
    private final VecPartOccurrence occurrence = new VecPartOccurrence();
    private final VecRole role = new VecPlaceableElementRole();

    @BeforeEach
    void setUp() {
        final VecCompositionSpecification composition = new VecCompositionSpecification();
        composition.setParentDocumentVersion(documentVersion);
        occurrence.setParentCompositionSpecification(composition);
        role.setParentOccurrenceOrUsage(occurrence);
    }

    @Test
    void navigatesToTheOccurrenceOrUsageOfARole() {
        assertThat(Roles.toParentOccurrenceOrUsage().from(role)).contains(occurrence);
    }

    @Test
    void navigatesToTheParentDocumentVersionOfARole() {
        assertThat(Roles.toParentDocumentVersion().from(role)).contains(documentVersion);
    }

    @Test
    void navigatesNowhereForAnUnattachedRole() {
        assertThat(Roles.toParentOccurrenceOrUsage().from(new VecPlaceableElementRole())).isEmpty();
        assertThat(Roles.toParentDocumentVersion().from(new VecPlaceableElementRole())).isEmpty();
    }

    /**
     * Characterisation test for the deprecated {@link VecNavs}, which delegates to {@link Roles}.
     * Can be removed together with {@link VecNavs}.
     */
    @Test
    @SuppressWarnings({"deprecation", "removal"})
    void deprecatedNavigationsBehaveLikeTheirReplacement() {
        assertThat(VecNavs.parentDocumentVersion().apply(role))
                .isEqualTo(Roles.toParentDocumentVersion().orElseNull(role));
    }

}
