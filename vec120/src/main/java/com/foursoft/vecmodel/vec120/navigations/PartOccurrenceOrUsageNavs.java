/*-
 * ========================LICENSE_START=================================
 * vec120
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
package com.foursoft.vecmodel.vec120.navigations;

import com.foursoft.vecmodel.common.annotations.RequiresBackReferences;
import com.foursoft.vecmodel.vec120.*;
import com.foursoft.vecmodel.vec120.utils.StreamUtils;
import com.foursoft.vecmodel.vec120.utils.StringUtils;
import com.foursoft.vecmodel.vec120.visitor.ReferencedNodeLocationVisitor;

import java.util.Collection;
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

    // VecPartOccurrence

    @RequiresBackReferences
    public static Function<VecPartOccurrence, String> parentDocumentNumber() {
        return occurrence -> {
            final VecCompositionSpecification compSpec = occurrence.getParentCompositionSpecification();
            return SpecificationNavs.parentDocumentNumber().apply(compSpec);
        };
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

    public static Function<VecPartOccurrence, VecPrimaryPartType> primaryPartType() {
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

    /**
     * Determines the parent {@link VecDocumentVersion} of a {@link VecPartOccurrence}.
     * <p>
     * <b>Warning: This uses {@link RequiresBackReferences back references}!</b>
     *
     * @return The parent VecDocumentVersion of a VecPartOccurrence.
     */
    @RequiresBackReferences
    public static Function<VecPartOccurrence, VecDocumentVersion> parentDocumentVersion() {
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

    public static BiFunction<VecOccurrenceOrUsage, VecDocumentVersion, Optional<VecTopologyNode>> findNodeOfComponent() {
        return (component, documentVersion) -> component.getRolesWithType(VecPlaceableElementRole.class).stream()
                .findFirst()  // there should be at most one such role
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
