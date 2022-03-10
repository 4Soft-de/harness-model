/*-
 * ========================LICENSE_START=================================
 * vec-v12x
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
package com.foursoft.harness.vec.v12x.navigations;

import com.foursoft.harness.vec.common.HasSpecifications;
import com.foursoft.harness.vec.common.annotations.RequiresBackReferences;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v12x.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Navigation methods for the {@link VecSpecification}.
 */
public final class SpecificationNavs {

    private SpecificationNavs() {
        // hide default constructor
    }

    @RequiresBackReferences
    public static Function<VecSpecification, String> parentDocumentNumber() {
        return spec -> parentDocumentVersion().apply(spec).getDocumentNumber();
    }

    @RequiresBackReferences
    public static Function<VecSpecification, VecDocumentVersion> parentDocumentVersion() {
        return specification -> {
            final VecSheetOrChapter sheetOrChapter = specification.getParentSheetOrChapter();
            final VecDocumentVersion documentVersion = specification.getParentDocumentVersion();
            if (documentVersion != null) {
                return documentVersion;
            } else {
                return sheetOrChapter.getParentDocumentVersion();
            }
        };
    }

    public static Function<HasSpecifications<VecSpecification>, List<VecOccurrenceOrUsage>> allOccurrenceOrUsages() {
        return specifications -> Stream.concat(
                        components().apply(specifications).stream(),
                        specifications.getSpecificationsWithType(VecPartUsageSpecification.class)
                                .stream()
                                .map(VecPartUsageSpecification::getPartUsages)
                                .flatMap(Collection::stream))
                .collect(Collectors.toList());
    }

    /**
     * Gets the {@link VecCompositionSpecification}s and gets
     * their {@link VecCompositionSpecification#getComponents() components}.
     *
     * @return A possibly-empty list of Components.
     */
    public static Function<HasSpecifications<VecSpecification>, List<VecPartOccurrence>> components() {
        return specifications -> specifications
                .getSpecificationsWithType(VecCompositionSpecification.class)
                .stream()
                .map(VecCompositionSpecification::getComponents)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * Gets the {@link VecCompositionSpecification} with the given specification value and
     * gets their {@link VecCompositionSpecification#getComponents() components}.
     *
     * @param compositionSpecificationId Id the {@link VecCompositionSpecification} has to have.
     * @return A possibly-empty list of Components.
     */
    public static Function<HasSpecifications<VecSpecification>, List<VecPartOccurrence>> componentsBy(
            final String compositionSpecificationId) {
        return specifications -> specifications
                .getSpecificationWith(VecCompositionSpecification.class, compositionSpecificationId)
                .map(VecCompositionSpecification::getComponents)
                .orElseGet(Collections::emptyList);
    }

    public static Function<VecBuildingBlockSpecification2D, VecGeometrySegment2D> geometrySegment2DBy(
            final String segmentId) {
        return specification -> specification.getGeometrySegments().stream()
                .filter(segment -> segment.getIdentification().equals(segmentId))
                .collect(StreamUtils.findOne());
    }

    public static Function<VecBuildingBlockSpecification3D, VecGeometrySegment3D> geometrySegment3DBy(
            final String segmentId) {
        return specification -> specification.getGeometrySegments().stream()
                .filter(segment -> segment.getIdentification().equals(segmentId))
                .collect(StreamUtils.findOne());
    }

    public static Function<VecBuildingBlockSpecification2D, VecGeometryNode2D> geometryNode2DBy(final String nodeId) {
        return specification -> specification.getGeometryNodes().stream()
                .filter(node -> node.getIdentification().equals(nodeId))
                .collect(StreamUtils.findOne());
    }

    public static Function<VecBuildingBlockSpecification3D, VecGeometryNode3D> geometryNode3DBy(final String nodeId) {
        return specification -> specification.getGeometryNodes().stream()
                .filter(node -> node.getIdentification().equals(nodeId))
                .collect(StreamUtils.findOne());
    }

}
