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

import com.foursoft.harness.vec.common.annotations.RequiresBackReferences;
import com.foursoft.harness.vec.common.exception.VecException;
import com.foursoft.harness.vec.common.traversal.MultiNavigation;
import com.foursoft.harness.vec.common.traversal.Navigations;
import com.foursoft.harness.vec.common.traversal.SingleNavigation;
import com.foursoft.harness.vec.common.util.StringUtils;
import com.foursoft.harness.vec.v2x.VecCompositionSpecification;
import com.foursoft.harness.vec.v2x.VecDocumentVersion;
import com.foursoft.harness.vec.v2x.VecLocation;
import com.foursoft.harness.vec.v2x.VecModuleFamily;
import com.foursoft.harness.vec.v2x.VecOccurrenceOrUsage;
import com.foursoft.harness.vec.v2x.VecPartOccurrence;
import com.foursoft.harness.vec.v2x.VecPartOrUsageRelatedSpecification;
import com.foursoft.harness.vec.v2x.VecPartUsage;
import com.foursoft.harness.vec.v2x.VecPartUsageSpecification;
import com.foursoft.harness.vec.v2x.VecPartVersion;
import com.foursoft.harness.vec.v2x.VecPartWithSubComponentsRole;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import com.foursoft.harness.vec.v2x.VecPrimaryPartType;
import com.foursoft.harness.vec.v2x.VecRole;
import com.foursoft.harness.vec.v2x.VecTopologyNode;
import com.foursoft.harness.vec.v2x.visitor.ReferencedNodeLocationVisitor;

import java.util.Optional;

/**
 * Navigations starting at a {@link VecOccurrenceOrUsage}, including its {@link VecPartOccurrence} and
 * {@link VecPartUsage} sub types.
 * <p>
 * Where both sub types lead to the same target, the navigation starts at the family and resolves the sub type
 * internally, so the source never shows up in the method name. Navigations which only a
 * {@link VecPartOccurrence} has, such as {@link #toPart()}, start there.
 */
public final class OccurrenceOrUsages {

    private OccurrenceOrUsages() {
        // hide default constructor
    }

    /**
     * Navigates to the roles of an occurrence or usage.
     *
     * @return A navigation to the roles of an occurrence or usage.
     */
    public static MultiNavigation<VecOccurrenceOrUsage, VecRole> toRoles() {
        return Navigations.collection(VecOccurrenceOrUsage::getRoles);
    }

    /**
     * Navigates to the {@link VecPlaceableElementRole} of an occurrence or usage.
     * <p>
     * An occurrence or usage is expected to have at most one such role. If there are several ones, the first
     * is chosen, see {@link MultiNavigation#atMostOne()}.
     *
     * @return A navigation to the placeable element role of an occurrence or usage.
     */
    public static SingleNavigation<VecOccurrenceOrUsage, VecPlaceableElementRole> toPlaceableElementRole() {
        return toRoles()
                .ofType(VecPlaceableElementRole.class)
                .atMostOne();
    }

    /**
     * Navigates to the {@link VecDocumentVersion} an occurrence or usage belongs to, through the
     * {@link VecCompositionSpecification} of an occurrence and the {@link VecPartUsageSpecification} of a
     * usage.
     *
     * @return A navigation to the parent document version of an occurrence or usage.
     */
    @RequiresBackReferences
    public static SingleNavigation<VecOccurrenceOrUsage, VecDocumentVersion> toParentDocumentVersion() {
        return occurrenceOrUsage -> switch (occurrenceOrUsage) {
            case final VecPartOccurrence occurrence -> Navigations
                    .nullable(VecPartOccurrence::getParentCompositionSpecification)
                    .then(Specifications.toParentDocumentVersion())
                    .from(occurrence);
            case final VecPartUsage usage -> Navigations
                    .nullable(VecPartUsage::getParentPartUsageSpecification)
                    .then(Specifications.toParentDocumentVersion())
                    .from(usage);
            default -> throw unhandledSubType(occurrenceOrUsage);
        };
    }

    /**
     * Navigates to the document number of the {@link VecDocumentVersion} an occurrence or usage belongs to.
     *
     * @return A navigation to the parent document number of an occurrence or usage.
     * @see #toParentDocumentVersion()
     */
    @RequiresBackReferences
    public static SingleNavigation<VecOccurrenceOrUsage, String> toParentDocumentNumber() {
        return toParentDocumentVersion().then(Navigations.nullable(VecDocumentVersion::getDocumentNumber));
    }

    /**
     * Navigates to the {@link VecPrimaryPartType} of an occurrence or usage.
     * <p>
     * A usage states its type itself. An occurrence takes it from its {@link VecPartVersion}, falling back to
     * the part usage it realizes and finally to {@link VecPrimaryPartType#OTHER} if it has no part.
     *
     * @return A navigation to the primary part type of an occurrence or usage.
     */
    public static SingleNavigation<VecOccurrenceOrUsage, VecPrimaryPartType> toPrimaryPartType() {
        return occurrenceOrUsage -> switch (occurrenceOrUsage) {
            case final VecPartOccurrence occurrence -> occurrence.getPart() != null
                    ? Optional.ofNullable(occurrence.getPart().getPrimaryPartType())
                    : Optional.of(toRealizedPartUsages()
                                          .then(Navigations.nullable(VecPartUsage::getPrimaryPartUsageType))
                                          .atMostOne()
                                          .orElse(occurrence, VecPrimaryPartType.OTHER));
            case final VecPartUsage usage -> Optional.ofNullable(usage.getPrimaryPartUsageType());
            default -> throw unhandledSubType(occurrenceOrUsage);
        };
    }

    /**
     * Navigates to the {@link VecPartOrUsageRelatedSpecification}s of an occurrence or usage, through the
     * {@link VecPartVersion} of an occurrence.
     *
     * @return A navigation to the part or usage related specifications of an occurrence or usage.
     */
    @RequiresBackReferences
    public static MultiNavigation<VecOccurrenceOrUsage, VecPartOrUsageRelatedSpecification>
    toPartOrUsageRelatedSpecifications() {
        return occurrenceOrUsage -> switch (occurrenceOrUsage) {
            case final VecPartOccurrence occurrence -> toPart()
                    .then(Navigations.collection(VecPartVersion::getRefPartOrUsageRelatedSpecification))
                    .from(occurrence);
            case final VecPartUsage usage -> Navigations
                    .collection(VecPartUsage::getPartOrUsageRelatedSpecification)
                    .from(usage);
            default -> throw unhandledSubType(occurrenceOrUsage);
        };
    }

    /**
     * Navigates to the {@link VecTopologyNode} an occurrence or usage is placed on, by resolving the
     * locations of its on point placements with the {@link ReferencedNodeLocationVisitor}.
     *
     * @return A navigation to the referenced topology node of an occurrence or usage.
     */
    public static SingleNavigation<VecOccurrenceOrUsage, VecTopologyNode> toReferencedTopologyNode() {
        final ReferencedNodeLocationVisitor visitor = new ReferencedNodeLocationVisitor();
        return toRoles()
                .ofType(VecPlaceableElementRole.class)
                .then(PlaceableElementRoles.toOnPointPlacements())
                .then(Placements.toLocations())
                .then(Navigations.<VecLocation, VecTopologyNode>nullable(location -> location.accept(visitor)))
                .atMostOne();
    }

    /**
     * Navigates to the {@link VecPartVersion} an occurrence is an occurrence of.
     *
     * @return A navigation to the part of an occurrence.
     */
    public static SingleNavigation<VecPartOccurrence, VecPartVersion> toPart() {
        return Navigations.nullable(VecPartOccurrence::getPart);
    }

    /**
     * Navigates to the part number of the {@link VecPartVersion} of an occurrence, with multiple whitespaces
     * collapsed into one.
     *
     * @return A navigation to the part number of an occurrence.
     */
    public static SingleNavigation<VecPartOccurrence, String> toPartNumber() {
        return toPart()
                .then(Navigations.nullable(VecPartVersion::getPartNumber))
                .then(Navigations.nullable(StringUtils::collapseMultipleWhitespaces));
    }

    /**
     * Navigates to the part version of the {@link VecPartVersion} of an occurrence, with multiple whitespaces
     * collapsed into one.
     *
     * @return A navigation to the part version of an occurrence.
     */
    public static SingleNavigation<VecPartOccurrence, String> toPartVersion() {
        return toPart()
                .then(Navigations.nullable(VecPartVersion::getPartVersion))
                .then(Navigations.nullable(StringUtils::collapseMultipleWhitespaces));
    }

    /**
     * Navigates to the part usages an occurrence realizes.
     *
     * @return A navigation to the realized part usages of an occurrence.
     */
    public static MultiNavigation<VecPartOccurrence, VecPartUsage> toRealizedPartUsages() {
        return Navigations.collection(VecPartOccurrence::getRealizedPartUsage);
    }

    /**
     * Navigates to the {@link VecModuleFamily} of an occurrence which is a module.
     * <p>
     * Only an occurrence of a {@link VecPrimaryPartType#PART_STRUCTURE} is a module, so any other occurrence
     * leads nowhere. Note that an assembly is a part structure as well.
     *
     * @return A navigation to the module family of an occurrence.
     */
    public static SingleNavigation<VecPartOccurrence, VecModuleFamily> toModuleFamily() {
        final SingleNavigation<VecOccurrenceOrUsage, VecModuleFamily> moduleFamily = toRoles()
                .ofType(VecPartWithSubComponentsRole.class)
                .then(Navigations.collection(VecPartWithSubComponentsRole::getRefModuleFamily))
                .atMostOne();
        return occurrence -> toPart()
                .then(Navigations.nullable(VecPartVersion::getPrimaryPartType))
                .from(occurrence)
                .filter(VecPrimaryPartType.PART_STRUCTURE::equals)
                .flatMap(partType -> moduleFamily.from(occurrence));
    }

    private static VecException unhandledSubType(final VecOccurrenceOrUsage occurrenceOrUsage) {
        return new VecException("Unhandled sub type of VecOccurrenceOrUsage: "
                                        + occurrenceOrUsage.getClass().getName());
    }

}
