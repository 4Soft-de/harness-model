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

import com.foursoft.harness.vec.v2x.VecContent;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecPlacementSpecification;
import com.foursoft.harness.vec.v2x.VecSpecification;
import com.foursoft.harness.vec.v2x.VecTopologySpecification;
import com.foursoft.harness.vec.v2x.navigations.ContentNavs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ContentsTest {

    private final VecContent content = new VecContent();
    private final VecDocumentVersion harness = documentVersion("DOC-1");
    private final VecDocumentVersion topology = documentVersion("DOC-2");

    private final VecPlacementSpecification placements = new VecPlacementSpecification();
    private final VecTopologySpecification topologySpecification = new VecTopologySpecification();

    private static VecDocumentVersion documentVersion(final String documentNumber) {
        final VecDocumentVersion documentVersion = new VecDocumentVersion();
        documentVersion.setDocumentNumber(documentNumber);
        return documentVersion;
    }

    @BeforeEach
    void setUp() {
        harness.getSpecifications().add(placements);
        topology.getSpecifications().add(topologySpecification);
        content.getDocumentVersions().add(harness);
        content.getDocumentVersions().add(topology);
    }

    @Test
    void navigatesToTheDocumentVersionsOfAContent() {
        assertThat(Contents.toDocumentVersions().listFrom(content)).containsExactly(harness, topology);
        assertThat(Contents.toDocumentVersions().listFrom(new VecContent())).isEmpty();
    }

    @Test
    void navigatesToTheDocumentVersionWithTheGivenDocumentNumber() {
        assertThat(Contents.documentVersionBy("DOC-1").from(content)).contains(harness);
        assertThat(Contents.documentVersionBy("DOC-3").from(content)).isEmpty();
    }

    @Test
    void navigatesToTheSpecificationsOfAllDocumentVersions() {
        assertThat(Contents.toDocumentVersions()
                           .then(SpecificationOwners.<VecDocumentVersion>toSpecifications())
                           .listFrom(content))
                .containsExactly(placements, topologySpecification);
    }

    /**
     * Characterisation test for the deprecated {@link ContentNavs}, which delegates to {@link Contents}.
     * Can be removed together with {@link ContentNavs}.
     */
    @Test
    @SuppressWarnings({"deprecation", "removal"})
    void deprecatedNavigationsBehaveLikeTheirReplacement() {
        assertThat(ContentNavs.documentVersionBy("DOC-1").apply(content))
                .isEqualTo(Contents.documentVersionBy("DOC-1").from(content));
        assertThat(ContentNavs.documentVersionBy("DOC-3").apply(content))
                .isEqualTo(Contents.documentVersionBy("DOC-3").from(content));
        assertThat(ContentNavs.allSpecificationsOf(VecSpecification.class).apply(content))
                .containsExactly(placements, topologySpecification);
        assertThat(ContentNavs.allSpecificationsOf(VecPlacementSpecification.class).apply(content))
                .containsExactly(placements);
    }

}
