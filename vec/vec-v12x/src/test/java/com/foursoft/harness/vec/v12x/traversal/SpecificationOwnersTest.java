/*-
 * ========================LICENSE_START=================================
 * VEC 1.2.X
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
package com.foursoft.harness.vec.v12x.traversal;

import com.foursoft.harness.vec.common.HasSpecifications;
import com.foursoft.harness.vec.v12x.VecCompositionSpecification;
import com.foursoft.harness.vec.v12x.VecPartOccurrence;
import com.foursoft.harness.vec.v12x.VecPartUsage;
import com.foursoft.harness.vec.v12x.VecPartUsageSpecification;
import com.foursoft.harness.vec.v12x.VecSpecification;
import com.foursoft.harness.vec.v12x.navigations.SpecificationNavs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SpecificationOwnersTest {

    private final List<VecSpecification> specifications = new ArrayList<>();
    private final HasSpecifications<VecSpecification> owner = () -> specifications;

    private final VecPartOccurrence firstComponent = new VecPartOccurrence();
    private final VecPartOccurrence secondComponent = new VecPartOccurrence();
    private final VecPartUsage partUsage = new VecPartUsage();

    private final VecCompositionSpecification firstComposition = new VecCompositionSpecification();
    private final VecCompositionSpecification secondComposition = new VecCompositionSpecification();
    private final VecPartUsageSpecification partUsages = new VecPartUsageSpecification();

    @BeforeEach
    void setUp() {
        firstComposition.setIdentification("COMP-1");
        firstComposition.getComponents().add(firstComponent);
        secondComposition.setIdentification("COMP-2");
        secondComposition.getComponents().add(secondComponent);
        partUsages.getPartUsages().add(partUsage);

        specifications.add(firstComposition);
        specifications.add(secondComposition);
        specifications.add(partUsages);
    }

    @Test
    void navigatesToTheSpecificationsOfTheirHolder() {
        assertThat(SpecificationOwners.toSpecifications().listFrom(owner))
                .containsExactly(firstComposition, secondComposition, partUsages);
        assertThat(SpecificationOwners.toSpecifications().listFrom(List::of)).isEmpty();
    }

    @Test
    void navigatesToTheComponentsOfAllCompositionSpecifications() {
        assertThat(SpecificationOwners.toComponents().listFrom(owner))
                .containsExactly(firstComponent, secondComponent);
    }

    @Test
    void navigatesToThePartUsagesOfAllPartUsageSpecifications() {
        assertThat(SpecificationOwners.toPartUsages().listFrom(owner)).containsExactly(partUsage);
    }

    @Test
    void navigatesToTheComponentsFollowedByThePartUsages() {
        assertThat(SpecificationOwners.toOccurrenceOrUsages().listFrom(owner))
                .containsExactly(firstComponent, secondComponent, partUsage);
    }

    @Test
    void navigatesToTheComponentsOfTheIdentifiedCompositionSpecification() {
        assertThat(SpecificationOwners.componentsBy("COMP-2").listFrom(owner)).containsExactly(secondComponent);
        assertThat(SpecificationOwners.componentsBy("COMP-3").listFrom(owner)).isEmpty();
    }

    /**
     * Characterisation test for the deprecated {@link SpecificationNavs}, which delegates to
     * {@link SpecificationOwners}. Can be removed together with {@link SpecificationNavs}.
     */
    @Test
    @SuppressWarnings({"deprecation", "removal"})
    void deprecatedNavigationsBehaveLikeTheirReplacement() {
        assertThat(SpecificationNavs.components().apply(owner))
                .isEqualTo(SpecificationOwners.toComponents().listFrom(owner));
        assertThat(SpecificationNavs.componentsBy("COMP-2").apply(owner))
                .isEqualTo(SpecificationOwners.componentsBy("COMP-2").listFrom(owner));
        assertThat(SpecificationNavs.allOccurrenceOrUsages().apply(owner))
                .isEqualTo(SpecificationOwners.toOccurrenceOrUsages().listFrom(owner));
    }

}
