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
import com.foursoft.harness.vec.v2x.VecModuleFamily;
import com.foursoft.harness.vec.v2x.VecNodeLocation;
import com.foursoft.harness.vec.v2x.VecOnPointPlacement;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPartOrUsageRelatedSpecification;
import com.foursoft.harness.vec.v2x.VecPartUsage;
import com.foursoft.harness.vec.v2x.VecPartUsageSpecification;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecPartWithSubComponentsRole;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import com.foursoft.harness.vec.v2x.VecPrimaryPartType;
import com.foursoft.harness.vec.v2x.VecTopologyNode;
import com.foursoft.harness.vec.v2x.VecWireRole;
import com.foursoft.harness.vec.v2x.VecWireSpecification;
import com.foursoft.harness.vec.v2x.navigations.PartOccurrenceOrUsageNavs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OccurrenceOrUsagesTest {

    private final VecDocumentVersion harness = new VecDocumentVersion();
    private final VecDocumentVersion partMaster = new VecDocumentVersion();

    private final VecPartOccurrence occurrence = new VecPartOccurrence();
    private final VecPartUsage usage = new VecPartUsage();
    private final VecPartVersion part = new VecPartVersion();
    private final VecPlaceableElementRole placeableElementRole = new VecPlaceableElementRole();
    private final VecWireRole wireRole = new VecWireRole();

    @BeforeEach
    void setUp() {
        harness.setDocumentNumber("HARNESS-1");
        partMaster.setDocumentNumber("PART-MASTER-1");

        part.setPartNumber("  12  34  ");
        part.setPartVersion("  01 ");
        part.setPrimaryPartType(VecPrimaryPartType.WIRE);
        occurrence.setPart(part);
        occurrence.getRoles().add(wireRole);
        occurrence.getRoles().add(placeableElementRole);

        final VecCompositionSpecification composition = new VecCompositionSpecification();
        composition.setParentDocumentVersion(harness);
        occurrence.setParentCompositionSpecification(composition);

        usage.setPrimaryPartUsageType(VecPrimaryPartType.CONNECTOR_HOUSING);
        final VecPartUsageSpecification partUsages = new VecPartUsageSpecification();
        partUsages.setParentDocumentVersion(partMaster);
        usage.setParentPartUsageSpecification(partUsages);
    }

    @Test
    void navigatesToTheRolesOfAnOccurrenceOrUsage() {
        assertThat(OccurrenceOrUsages.toRoles().listFrom(occurrence))
                .containsExactly(wireRole, placeableElementRole);
        assertThat(OccurrenceOrUsages.toRoles().listFrom(usage)).isEmpty();
    }

    @Test
    void navigatesToThePlaceableElementRoleOfAnOccurrenceOrUsage() {
        assertThat(OccurrenceOrUsages.toPlaceableElementRole().from(occurrence)).contains(placeableElementRole);
        assertThat(OccurrenceOrUsages.toPlaceableElementRole().from(usage)).isEmpty();
    }

    @Test
    void navigatesToTheParentDocumentVersionOfAnOccurrenceAndOfAUsage() {
        assertThat(OccurrenceOrUsages.toParentDocumentVersion().from(occurrence)).contains(harness);
        assertThat(OccurrenceOrUsages.toParentDocumentVersion().from(usage)).contains(partMaster);
        assertThat(OccurrenceOrUsages.toParentDocumentNumber().from(occurrence)).contains("HARNESS-1");
        assertThat(OccurrenceOrUsages.toParentDocumentNumber().from(usage)).contains("PART-MASTER-1");
    }

    @Test
    void navigatesToNoParentDocumentVersionForAnUnattachedOccurrence() {
        assertThat(OccurrenceOrUsages.toParentDocumentVersion().from(new VecPartOccurrence())).isEmpty();
    }

    @Test
    void navigatesToThePartOfAnOccurrenceWithItsWhitespacesCollapsed() {
        assertThat(OccurrenceOrUsages.toPart().from(occurrence)).contains(part);
        assertThat(OccurrenceOrUsages.toPartNumber().from(occurrence)).contains("12 34");
        assertThat(OccurrenceOrUsages.toPartVersion().from(occurrence)).contains("01");
    }

    @Test
    void navigatesToNoPartForAnOccurrenceWithoutOne() {
        assertThat(OccurrenceOrUsages.toPart().from(new VecPartOccurrence())).isEmpty();
        assertThat(OccurrenceOrUsages.toPartNumber().from(new VecPartOccurrence())).isEmpty();
    }

    @Test
    void navigatesToThePrimaryPartTypeOfAnOccurrenceAndOfAUsage() {
        assertThat(OccurrenceOrUsages.toPrimaryPartType().from(occurrence)).contains(VecPrimaryPartType.WIRE);
        assertThat(OccurrenceOrUsages.toPrimaryPartType().from(usage))
                .contains(VecPrimaryPartType.CONNECTOR_HOUSING);
    }

    @Test
    void navigatesToThePrimaryPartTypeOfTheRealizedUsageForAnOccurrenceWithoutAPart() {
        final VecPartOccurrence realizing = new VecPartOccurrence();
        realizing.getRealizedPartUsage().add(usage);

        assertThat(OccurrenceOrUsages.toPrimaryPartType().from(realizing))
                .contains(VecPrimaryPartType.CONNECTOR_HOUSING);
        assertThat(OccurrenceOrUsages.toPrimaryPartType().from(new VecPartOccurrence()))
                .contains(VecPrimaryPartType.OTHER);
    }

    @Test
    void navigatesToThePartOrUsageRelatedSpecifications() {
        final VecPartOrUsageRelatedSpecification specification = new VecWireSpecification();
        part.getRefPartOrUsageRelatedSpecification().add(specification);
        usage.getPartOrUsageRelatedSpecification().add(specification);

        assertThat(OccurrenceOrUsages.toPartOrUsageRelatedSpecifications().listFrom(occurrence))
                .containsExactly(specification);
        assertThat(OccurrenceOrUsages.toPartOrUsageRelatedSpecifications().listFrom(usage))
                .containsExactly(specification);
        assertThat(OccurrenceOrUsages.toPartOrUsageRelatedSpecifications().listFrom(new VecPartOccurrence()))
                .isEmpty();
    }

    @Test
    void navigatesToTheTopologyNodeAnOccurrenceIsPlacedOn() {
        final VecTopologyNode topologyNode = new VecTopologyNode();
        final VecNodeLocation location = new VecNodeLocation();
        location.setReferencedNode(topologyNode);
        final VecOnPointPlacement placement = new VecOnPointPlacement();
        placement.getLocations().add(location);
        placeableElementRole.getRefPlacement().add(placement);

        assertThat(OccurrenceOrUsages.toReferencedTopologyNode().from(occurrence)).contains(topologyNode);
        assertThat(OccurrenceOrUsages.toReferencedTopologyNode().from(new VecPartOccurrence())).isEmpty();
    }

    @Test
    void navigatesToTheModuleFamilyOfAModule() {
        final VecModuleFamily moduleFamily = new VecModuleFamily();
        final VecPartWithSubComponentsRole moduleRole = new VecPartWithSubComponentsRole();
        moduleRole.getRefModuleFamily().add(moduleFamily);
        occurrence.getRoles().add(moduleRole);

        assertThat(OccurrenceOrUsages.toModuleFamily().from(occurrence)).isEmpty();

        part.setPrimaryPartType(VecPrimaryPartType.PART_STRUCTURE);

        assertThat(OccurrenceOrUsages.toModuleFamily().from(occurrence)).contains(moduleFamily);
    }

    /**
     * Characterisation test for the deprecated {@link PartOccurrenceOrUsageNavs}, which delegates to
     * {@link OccurrenceOrUsages}. Can be removed together with {@link PartOccurrenceOrUsageNavs}.
     */
    @Test
    @SuppressWarnings({"deprecation", "removal"})
    void deprecatedNavigationsBehaveLikeTheirReplacement() {
        assertThat(PartOccurrenceOrUsageNavs.parentDocumentVersionOfOccurrence().apply(occurrence))
                .isEqualTo(harness);
        assertThat(PartOccurrenceOrUsageNavs.parentDocumentVersionOfUsage().apply(usage)).isEqualTo(partMaster);
        assertThat(PartOccurrenceOrUsageNavs.parentDocumentVersion().apply(occurrence)).isEqualTo(harness);
        assertThat(PartOccurrenceOrUsageNavs.parentDocumentNumberOfOccurrence().apply(occurrence))
                .isEqualTo("HARNESS-1");
        assertThat(PartOccurrenceOrUsageNavs.parentDocumentNumberOfUsage().apply(usage))
                .isEqualTo("PART-MASTER-1");
        assertThat(PartOccurrenceOrUsageNavs.parentDocumentNumber().apply(usage)).isEqualTo("PART-MASTER-1");
        assertThat(PartOccurrenceOrUsageNavs.partNumber().apply(occurrence))
                .isEqualTo(OccurrenceOrUsages.toPartNumber().from(occurrence));
        assertThat(PartOccurrenceOrUsageNavs.partVersion().apply(occurrence))
                .isEqualTo(OccurrenceOrUsages.toPartVersion().from(occurrence));
        assertThat(PartOccurrenceOrUsageNavs.primaryPartTypeOfOccurrence().apply(occurrence))
                .isEqualTo(VecPrimaryPartType.WIRE);
        assertThat(PartOccurrenceOrUsageNavs.primaryPartType().apply(usage))
                .isEqualTo(VecPrimaryPartType.CONNECTOR_HOUSING);
        assertThat(PartOccurrenceOrUsageNavs.moduleFamily().apply(occurrence))
                .isEqualTo(OccurrenceOrUsages.toModuleFamily().from(occurrence));
        assertThat(PartOccurrenceOrUsageNavs.topologyNodeByOccurrenceOrUsage().apply(occurrence))
                .isEqualTo(OccurrenceOrUsages.toReferencedTopologyNode().from(occurrence));
        assertThat(PartOccurrenceOrUsageNavs.occurrence().apply(occurrence)).contains(occurrence);
        assertThat(PartOccurrenceOrUsageNavs.usage().apply(usage)).contains(usage);
    }

}
