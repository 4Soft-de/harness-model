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
package com.foursoft.harness.vec.v12x.navigations;

import com.foursoft.harness.vec.common.annotations.RequiresBackReferences;
import com.foursoft.harness.vec.common.traversal.MultiNavigation;
import com.foursoft.harness.vec.v12x.VecDocumentVersion;
import com.foursoft.harness.vec.v12x.VecModuleFamily;
import com.foursoft.harness.vec.v12x.VecOccurrenceOrUsage;
import com.foursoft.harness.vec.v12x.VecPartOccurrence;
import com.foursoft.harness.vec.v12x.VecPartOrUsageRelatedSpecification;
import com.foursoft.harness.vec.v12x.VecPartUsage;
import com.foursoft.harness.vec.v12x.VecPrimaryPartType;
import com.foursoft.harness.vec.v12x.VecTopologyNode;
import com.foursoft.harness.vec.v12x.traversal.DocumentVersions;
import com.foursoft.harness.vec.v12x.traversal.OccurrenceOrUsages;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Navigation methods for the {@link VecOccurrenceOrUsage} including {@link VecPartOccurrence} and {@link VecPartUsage}.
 *
 * @deprecated Use {@link OccurrenceOrUsages} instead, which starts at the family wherever both sub types lead
 * to the same target and therefore needs no {@code OfOccurrence} and {@code OfUsage} suffixes.
 */
@Deprecated(forRemoval = true)
public final class PartOccurrenceOrUsageNavs {

    private PartOccurrenceOrUsageNavs() {
        // hide default constructor
    }

    // VecPartUsage

    /**
     * @deprecated Use {@link OccurrenceOrUsages#toParentDocumentNumber()} instead.
     */
    @Deprecated(forRemoval = true)
    @RequiresBackReferences
    public static Function<VecPartUsage, String> parentDocumentNumberOfUsage() {
        return OccurrenceOrUsages.toParentDocumentNumber()::orElseNull;
    }

    /**
     * @deprecated Use {@link OccurrenceOrUsages#toParentDocumentVersion()} instead.
     */
    @Deprecated(forRemoval = true)
    @RequiresBackReferences
    public static Function<VecPartUsage, VecDocumentVersion> parentDocumentVersionOfUsage() {
        return OccurrenceOrUsages.toParentDocumentVersion()::orElseNull;
    }

    // VecPartOccurrence

    /**
     * @deprecated Use {@link OccurrenceOrUsages#toParentDocumentNumber()} instead.
     */
    @Deprecated(forRemoval = true)
    @RequiresBackReferences
    public static Function<VecPartOccurrence, String> parentDocumentNumberOfOccurrence() {
        return OccurrenceOrUsages.toParentDocumentNumber()::orElseNull;
    }

    /**
     * @deprecated Use {@link OccurrenceOrUsages#toPartNumber()} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecPartOccurrence, Optional<String>> partNumber() {
        return OccurrenceOrUsages.toPartNumber();
    }

    /**
     * @deprecated Use {@link OccurrenceOrUsages#toPartVersion()} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecPartOccurrence, Optional<String>> partVersion() {
        return OccurrenceOrUsages.toPartVersion();
    }

    /**
     * @deprecated Use {@link OccurrenceOrUsages#toPrimaryPartType()} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecPartOccurrence, VecPrimaryPartType> primaryPartTypeOfOccurrence() {
        return OccurrenceOrUsages.toPrimaryPartType()::orElseNull;
    }

    /**
     * @deprecated Use {@link OccurrenceOrUsages#toPartOrUsageRelatedSpecifications()} instead.
     */
    @Deprecated(forRemoval = true)
    @RequiresBackReferences
    public static Function<VecPartOccurrence, List<VecPartOrUsageRelatedSpecification>> partOrUsageRelatedSpecificationsOfOccurrence() {
        return occurrence -> new ArrayList<>(
                OccurrenceOrUsages.toPartOrUsageRelatedSpecifications().listFrom(occurrence));
    }

    /**
     * Determines the parent {@link VecDocumentVersion} of a {@link VecPartOccurrence}.
     * <p>
     * <b>Warning: This uses {@link RequiresBackReferences back references}!</b>
     *
     * @return The parent VecDocumentVersion of a VecPartOccurrence.
     * @deprecated Use {@link OccurrenceOrUsages#toParentDocumentVersion()} instead.
     */
    @Deprecated(forRemoval = true)
    @RequiresBackReferences
    public static Function<VecPartOccurrence, VecDocumentVersion> parentDocumentVersionOfOccurrence() {
        return OccurrenceOrUsages.toParentDocumentVersion()::orElseNull;
    }

    /**
     * Function to determine the {@link VecModuleFamily} for a PartOccurrence.
     * Note that the PartOccurrence has to be a module in order to be checked.
     *
     * @return An empty optional if the tested PartOccurrence is not a module or if no family was found.
     * @deprecated Use {@link OccurrenceOrUsages#toModuleFamily()} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecPartOccurrence, Optional<VecModuleFamily>> moduleFamily() {
        return OccurrenceOrUsages.toModuleFamily();
    }

    // VecOccurrenceOrUsage

    /**
     * @deprecated Use {@link OccurrenceOrUsages#toParentDocumentNumber()} instead.
     */
    @Deprecated(forRemoval = true)
    @RequiresBackReferences
    public static Function<VecOccurrenceOrUsage, String> parentDocumentNumber() {
        return OccurrenceOrUsages.toParentDocumentNumber()::orElseNull;
    }

    /**
     * @deprecated Use {@link OccurrenceOrUsages#toParentDocumentVersion()} instead.
     */
    @Deprecated(forRemoval = true)
    @RequiresBackReferences
    public static Function<VecOccurrenceOrUsage, VecDocumentVersion> parentDocumentVersion() {
        return OccurrenceOrUsages.toParentDocumentVersion()::orElseNull;
    }

    /**
     * @deprecated Use {@link DocumentVersions#topologyNodeOf(VecOccurrenceOrUsage)} instead, which is a
     * navigation from the document version rather than a function of two arguments.
     */
    @Deprecated(forRemoval = true)
    public static BiFunction<VecOccurrenceOrUsage, VecDocumentVersion, Optional<VecTopologyNode>> findNodeOfComponent() {
        return (component, documentVersion) -> DocumentVersions.topologyNodeOf(component).from(documentVersion);
    }

    /**
     * @deprecated Use {@link OccurrenceOrUsages#toReferencedTopologyNode()} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecOccurrenceOrUsage, Optional<VecTopologyNode>> topologyNodeByOccurrenceOrUsage() {
        return OccurrenceOrUsages.toReferencedTopologyNode();
    }

    /**
     * @deprecated Use {@link OccurrenceOrUsages#toPrimaryPartType()} instead.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecOccurrenceOrUsage, VecPrimaryPartType> primaryPartType() {
        return OccurrenceOrUsages.toPrimaryPartType()::orElseNull;
    }

    /**
     * @deprecated Use {@link OccurrenceOrUsages#toPartOrUsageRelatedSpecifications()} instead.
     */
    @Deprecated(forRemoval = true)
    @RequiresBackReferences
    public static Function<VecOccurrenceOrUsage, List<VecPartOrUsageRelatedSpecification>> partOrUsageRelatedSpecifications() {
        return occurrenceOrUsage -> new ArrayList<>(
                OccurrenceOrUsages.toPartOrUsageRelatedSpecifications().listFrom(occurrenceOrUsage));
    }

    /**
     * @deprecated Narrow the navigation leading to the occurrences or usages with
     * {@link MultiNavigation#ofType(Class)} instead, for example
     * {@code SpecificationOwners.toOccurrenceOrUsages().ofType(VecPartOccurrence.class)}.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecOccurrenceOrUsage, Optional<VecPartOccurrence>> occurrence() {
        return occurrenceOrUsage -> Optional.of(occurrenceOrUsage)
                .filter(VecPartOccurrence.class::isInstance)
                .map(VecPartOccurrence.class::cast);
    }

    /**
     * @deprecated Narrow the navigation leading to the occurrences or usages with
     * {@link MultiNavigation#ofType(Class)} instead, for example
     * {@code SpecificationOwners.toOccurrenceOrUsages().ofType(VecPartUsage.class)}.
     */
    @Deprecated(forRemoval = true)
    public static Function<VecOccurrenceOrUsage, Optional<VecPartUsage>> usage() {
        return occurrenceOrUsage -> Optional.of(occurrenceOrUsage)
                .filter(VecPartUsage.class::isInstance)
                .map(VecPartUsage.class::cast);
    }

}
