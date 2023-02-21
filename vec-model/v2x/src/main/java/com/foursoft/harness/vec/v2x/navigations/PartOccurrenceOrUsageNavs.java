/*-
 * ========================LICENSE_START=================================
 * vec-v2x
 * %%
 * Copyright (C) 2020 - 2022 4Soft GmbH
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
package com.foursoft.harness.vec.v2x.navigations;

import com.foursoft.harness.vec.common.annotations.RequiresBackReferences;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.common.util.StringUtils;
import com.foursoft.harness.vec.v2x.*;
import com.foursoft.harness.vec.v2x.visitor.ReferencedNodeLocationVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Navigation methods for the {@link VecOccurrenceOrUsage} including {@link VecPartOccurrence} and {@link VecPartUsage}.
 */
public final class PartOccurrenceOrUsageNavs {

    private PartOccurrenceOrUsageNavs() {
        // hide default constructor
    }

    // VecPartUsage

    @RequiresBackReferences
    public static Function<VecPartUsage, String> parentDocumentNumberOfUsage() {
        return usage -> parentDocumentVersionOfUsage().apply(usage).getDocumentNumber();
    }

    @RequiresBackReferences
    public static Function<VecPartUsage, VecDocumentVersion> parentDocumentVersionOfUsage() {
        return usage -> {
            final VecPartUsageSpecification partUsageSpec = usage.getParentPartUsageSpecification();
            return SpecificationNavs.parentDocumentVersion().apply(partUsageSpec);
        };
    }

    // VecPartOccurrence

    @RequiresBackReferences
    public static Function<VecPartOccurrence, String> parentDocumentNumberOfOccurrence() {
        return occurrence -> parentDocumentVersionOfOccurrence().apply(occurrence).getDocumentNumber();
    }

    public static Function<VecPartOccurrence, Optional<String>> partNumber() {
        return occurrence -> Optional.of(occurrence)
                .map(VecPartOccurrence::getPart)
                .map(VecPartVersion::getPartNumber)
                .map(StringUtils::collapseMultipleWhitespaces);
    }

    public static Function<VecPartOccurrence, Optional<String>> partVersion() {
        return occurrence -> Optional.of(occurrence)
                .map(VecPartOccurrence::getPart)
                .map(VecPartVersion::getPartVersion)
                .map(StringUtils::collapseMultipleWhitespaces);
    }

    public static Function<VecPartOccurrence, VecPrimaryPartType> primaryPartTypeOfOccurrence() {
        return occurrence -> {
            final VecPartVersion partVersion = occurrence.getPart();
            return partVersion != null
                    ? partVersion.getPrimaryPartType()
                    :
                    occurrence.getRealizedPartUsage()
                            .stream()
                            .collect(StreamUtils.findOneOrNone())
                            .map(VecPartUsage::getPrimaryPartUsageType)
                            .orElse(VecPrimaryPartType.OTHER);
        };
    }

    @RequiresBackReferences
    public static Function<VecPartOccurrence, List<VecPartOrUsageRelatedSpecification>> partOrUsageRelatedSpecificationsOfOccurrence() {
        return occurrence -> new ArrayList<>(occurrence.getPart().getRefPartOrUsageRelatedSpecification());
    }

    /**
     * Determines the parent {@link VecDocumentVersion} of a {@link VecPartOccurrence}.
     * <p>
     * <b>Warning: This uses {@link RequiresBackReferences back references}!</b>
     *
     * @return The parent VecDocumentVersion of a VecPartOccurrence.
     */
    @RequiresBackReferences
    public static Function<VecPartOccurrence, VecDocumentVersion> parentDocumentVersionOfOccurrence() {
        return occurrence -> {

            final VecCompositionSpecification parentCompositionSpecification =
                    occurrence.getParentCompositionSpecification();

            return SpecificationNavs.parentDocumentVersion().apply(parentCompositionSpecification);
        };
    }

    /**
     * Function to determine the {@link VecModuleFamily} for a PartOccurrence.
     * Note that the PartOccurrence has to be a module in order to be checked.
     *
     * @return An empty optional if the tested PartOccurrence is not a module or if no family was found.
     */
    public static Function<VecPartOccurrence, Optional<VecModuleFamily>> moduleFamily() {
        return occurrence -> {
            // Can also be an assembly though!
            final boolean isModule = occurrence.getPart().getPrimaryPartType() == VecPrimaryPartType.PART_STRUCTURE;

            return !isModule
                    ? Optional.empty()
                    :
                    occurrence.getRolesWithType(VecPartWithSubComponentsRole.class).stream()
                            .flatMap(StreamUtils.toStream(VecPartWithSubComponentsRole::getRefModuleFamily))
                            .collect(StreamUtils.findOneOrNone());
        };
    }

    // VecOccurrenceOrUsage

    @RequiresBackReferences
    public static Function<VecOccurrenceOrUsage, String> parentDocumentNumber() {
        return occurrenceOrUsage -> parentDocumentVersion().apply(occurrenceOrUsage).getDocumentNumber();
    }

    @RequiresBackReferences
    public static Function<VecOccurrenceOrUsage, VecDocumentVersion> parentDocumentVersion() {
        return occurrenceOrUsage -> {
            if (occurrenceOrUsage instanceof VecPartOccurrence) {
                return parentDocumentVersionOfOccurrence().apply((VecPartOccurrence) occurrenceOrUsage);
            }
            return parentDocumentVersionOfUsage().apply((VecPartUsage) occurrenceOrUsage);
        };
    }

    public static BiFunction<VecOccurrenceOrUsage, VecDocumentVersion, Optional<VecTopologyNode>> findNodeOfComponent() {
        return (component, documentVersion) -> component.getRoleWithType(VecPlaceableElementRole.class)
                .flatMap(placedElement -> DocumentVersionNavs
                        .nodeLocationsBy(placedElement)
                        .apply(documentVersion)
                        .stream()
                        .collect(StreamUtils.findOneOrNone()))
                .map(VecNodeLocation::getReferencedNode);
    }

    public static Function<VecOccurrenceOrUsage, Optional<VecTopologyNode>> topologyNodeByOccurrenceOrUsage() {
        final ReferencedNodeLocationVisitor visitor = new ReferencedNodeLocationVisitor();
        return occurrence -> occurrence.getRolesWithType(VecPlaceableElementRole.class).stream()
                .map(VecPlaceableElementRole::getRefPlacement)
                .flatMap(Collection::stream)
                .filter(VecOnPointPlacement.class::isInstance)
                .map(VecOnPointPlacement.class::cast)
                .map(VecOnPointPlacement::getLocations)
                .flatMap(Collection::stream)
                .map(location -> location.accept(visitor))
                .collect(StreamUtils.findOneOrNone());
    }

    public static Function<VecOccurrenceOrUsage, VecPrimaryPartType> primaryPartType() {
        return occurrenceOrUsage -> {
            if (occurrenceOrUsage instanceof VecPartOccurrence) {
                return primaryPartTypeOfOccurrence().apply((VecPartOccurrence) occurrenceOrUsage);
            }
            return ((VecPartUsage) occurrenceOrUsage).getPrimaryPartUsageType();
        };
    }

    @RequiresBackReferences
    public static Function<VecOccurrenceOrUsage, List<VecPartOrUsageRelatedSpecification>> partOrUsageRelatedSpecifications() {
        return occurrenceOrUsage -> {
            if (occurrenceOrUsage instanceof VecPartOccurrence) {
                return partOrUsageRelatedSpecificationsOfOccurrence().apply((VecPartOccurrence) occurrenceOrUsage);
            }
            return ((VecPartUsage) occurrenceOrUsage).getPartOrUsageRelatedSpecification();
        };
    }

    public static Function<VecOccurrenceOrUsage, Optional<VecPartOccurrence>> occurrence() {
        return occurrenceOrUsage -> Optional.of(occurrenceOrUsage)
                .filter(VecPartOccurrence.class::isInstance)
                .map(VecPartOccurrence.class::cast);
    }

    public static Function<VecOccurrenceOrUsage, Optional<VecPartUsage>> usage() {
        return occurrenceOrUsage -> Optional.of(occurrenceOrUsage)
                .filter(VecPartUsage.class::isInstance)
                .map(VecPartUsage.class::cast);
    }

}
