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

import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v12x.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Navigation methods for the {@link VecDocumentVersion}.
 */
public final class DocumentVersionNavs {

    private DocumentVersionNavs() {
        // hide default constructor
    }

    public static Function<VecDocumentVersion, List<VecGeometryNode3D>> geometryNodes3DBy(
            final VecTopologyNode topologyNode) {
        return documentVersion -> documentVersion.getSpecificationsWithType(VecBuildingBlockSpecification3D.class)
                .stream()
                .map(VecBuildingBlockSpecification3D::getGeometryNodes)
                .flatMap(Collection::stream)
                .filter(node3D -> node3D.getReferenceNode().equals(topologyNode))
                .collect(Collectors.toList());
    }

    public static Function<VecDocumentVersion, List<VecGeometrySegment3D>> geometrySegments3DBy(
            final VecTopologySegment topologySegment) {
        return documentVersion -> documentVersion.getSpecificationsWithType(VecBuildingBlockSpecification3D.class)
                .stream()
                .map(VecBuildingBlockSpecification3D::getGeometrySegments)
                .flatMap(Collection::stream)
                .filter(segment3D -> segment3D.getReferenceSegment().equals(topologySegment))
                .collect(Collectors.toList());
    }

    public static Function<VecDocumentVersion, List<VecOccurrenceOrUsageViewItem3D>> viewItems3DBy(
            final VecOccurrenceOrUsage occurrence) {
        return documentVersion -> documentVersion.getSpecificationsWithType(VecBuildingBlockSpecification3D.class)
                .stream()
                .map(specification -> specification
                        .getPlacedElementViewItem3Ds().stream()
                        .filter(viewItem -> viewItem.getOccurrenceOrUsage().contains(occurrence))
                        .collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static Function<VecDocumentVersion, Optional<VecTopologyNode>> topologyNodeBy(
            final String occurrenceIdentification) {
        return documentVersion -> SpecificationNavs.components().apply(documentVersion)
                .stream()
                .filter(occurrence -> occurrence.getIdentification().equals(occurrenceIdentification))
                .map(PartOccurrenceOrUsageNavs.topologyNodeByOccurrenceOrUsage())
                .flatMap(StreamUtils.unwrapOptional())
                .collect(StreamUtils.findOneOrNone());
    }

    /**
     * Navigation method to get the {@link VecNodeLocation}s from a given {@link VecPlaceableElementRole}.
     *
     * @param placedElement Placed Element the {@link VecOnPointPlacement} needs to contain.
     *                      May be {@code null} to not filter for this.
     * @return A possibly-empty list of VecNodeLocations.
     */
    public static Function<VecDocumentVersion, List<VecNodeLocation>> nodeLocationsBy(
            final VecPlaceableElementRole placedElement) {
        return documentVersion -> documentVersion
                .getSpecificationsWithType(VecPlacementSpecification.class).stream()
                .map(VecPlacementSpecification::getPlacements)
                .flatMap(Collection::stream)
                .filter(VecOnPointPlacement.class::isInstance)
                .map(VecOnPointPlacement.class::cast)
                .filter(c -> placedElement == null || c.getPlacedElement().contains(placedElement))
                .map(VecOnPointPlacement::getLocations)
                .flatMap(Collection::stream)
                .filter(VecNodeLocation.class::isInstance)
                .map(VecNodeLocation.class::cast)
                .collect(Collectors.toList());
    }

    public static Function<VecDocumentVersion, VecPlaceableElementRole> placeableElementRoleBy(
            final String specificationValue,
            final String occurrenceOrUsageId) {
        return dv -> SpecificationNavs.componentsBy(specificationValue).apply(dv)
                .stream()
                .filter(c -> c.getIdentification().equals(occurrenceOrUsageId))
                .map(VecPartOccurrence::getRoles)
                .flatMap(Collection::stream)
                .filter(VecPlaceableElementRole.class::isInstance)
                .map(VecPlaceableElementRole.class::cast)
                .collect(StreamUtils.findOne());
    }

    public static Function<VecDocumentVersion, VecPlacement> placementBy(final String specificationValue,
                                                                         final String occurrenceOrUsageId) {
        return dv -> {
            final VecPlaceableElementRole role =
                    placeableElementRoleBy(specificationValue, occurrenceOrUsageId).apply(dv);

            return dv.getSpecificationsWithType(VecPlacementSpecification.class).stream()
                    .map(VecPlacementSpecification::getPlacements)
                    .flatMap(Collection::stream)
                    .filter(p -> p.getPlacedElement().stream().anyMatch(r -> r.equals(role)))
                    .collect(StreamUtils.findOne());
        };
    }

    public static Function<VecDocumentVersion, VecGeometryNode2D> geometryNode2DBy(final VecNodeLocation location) {
        return dv -> {
            final List<VecGeometryNode2D> nodes = dv.getSpecificationWithType(VecBuildingBlockSpecification2D.class)
                    .map(VecBuildingBlockSpecification2D::getGeometryNodes)
                    .orElseGet(Collections::emptyList);
            return getGeometryNode(nodes, location);
        };
    }

    public static Function<VecDocumentVersion, VecGeometryNode3D> geometryNode3DBy(final VecNodeLocation location) {
        return dv -> {
            final List<VecGeometryNode3D> nodes = dv.getSpecificationWithType(VecBuildingBlockSpecification3D.class)
                    .map(VecBuildingBlockSpecification3D::getGeometryNodes)
                    .orElseGet(Collections::emptyList);
            return getGeometryNode(nodes, location);
        };
    }

    public static Function<VecDocumentVersion, Optional<VecOccurrenceOrUsageViewItem2D>> viewItem2DBy(
            final String occurrenceOrUsageId) {
        return dv -> {
            final Stream<VecOccurrenceOrUsageViewItem2D> stream = dv
                    .getSpecificationsWithType(VecBuildingBlockSpecification2D.class)
                    .stream()
                    .map(VecBuildingBlockSpecification2D::getPlacedElementViewItems)
                    .flatMap(Collection::stream);
            return getViewItem(stream, occurrenceOrUsageId);
        };
    }

    public static Function<VecDocumentVersion, Optional<VecOccurrenceOrUsageViewItem3D>> viewItem3DBy(
            final String occurrenceOrUsageId) {
        return dv -> {
            final Stream<VecOccurrenceOrUsageViewItem3D> stream = dv
                    .getSpecificationsWithType(VecBuildingBlockSpecification3D.class)
                    .stream()
                    .map(VecBuildingBlockSpecification3D::getPlacedElementViewItem3Ds)
                    .flatMap(Collection::stream);
            return getViewItem(stream, occurrenceOrUsageId);
        };
    }

    public static Function<VecDocumentVersion, Optional<VecBuildingBlockSpecification2D>> buildingBlockSpecification2DBy(
            final String specificationId) {
        return dv -> dv.getSpecificationWith(VecBuildingBlockSpecification2D.class, specificationId);
    }

    public static Function<VecDocumentVersion, Optional<VecBuildingBlockSpecification3D>> buildingBlockSpecification3DBy(
            final String specificationId) {
        return dv -> dv.getSpecificationWith(VecBuildingBlockSpecification3D.class, specificationId);
    }

    public static Function<VecDocumentVersion, Optional<VecBuildingBlockPositioning2D>> positioning2DWith(
            final VecBuildingBlockSpecification2D buildingBlock) {
        return dv -> dv.getSpecificationsWithType(VecHarnessDrawingSpecification2D.class).stream()
                .map(VecHarnessDrawingSpecification2D::getBuildingBlockPositionings)
                .flatMap(Collection::stream)
                .filter(positioning -> buildingBlock.equals(positioning.getReferenced2DBuildingBlock()))
                .collect(StreamUtils.findOneOrNone());
    }

    public static Function<VecDocumentVersion, Optional<VecBuildingBlockPositioning3D>> positioning3DWith(
            final VecBuildingBlockSpecification3D buildingBlock) {
        return dv -> dv.getSpecificationsWithType(VecHarnessGeometrySpecification3D.class).stream()
                .map(VecHarnessGeometrySpecification3D::getBuildingBlockPositionings)
                .flatMap(Collection::stream)
                .filter(positioning -> buildingBlock.equals(positioning.getReferenced3DBuildingBlock()))
                .collect(StreamUtils.findOneOrNone());
    }

    private static <T extends VecGeometryNode> T getGeometryNode(final List<T> nodes,
                                                                 final VecNodeLocation location) {
        return nodes.stream()
                .filter(node -> node.getReferenceNode().equals(location.getReferencedNode()))
                .collect(StreamUtils.findOne());
    }

    private static <T extends HasOccurrenceOrUsages> Optional<T> getViewItem(final Stream<T> stream,
                                                                             final String occurrenceOrUsageId) {
        return stream
                .filter(item -> item.getOccurrenceOrUsage().stream()
                        .collect(StreamUtils.findOne())
                        .getIdentification()
                        .equals(occurrenceOrUsageId))
                .collect(StreamUtils.findOneOrNone());
    }

}
