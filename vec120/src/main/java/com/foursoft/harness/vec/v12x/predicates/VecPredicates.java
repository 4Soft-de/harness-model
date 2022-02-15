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
package com.foursoft.harness.vec.v12x.predicates;

import com.foursoft.harness.vec.v12x.*;
import com.foursoft.vecmodel.common.annotations.RequiresBackReferences;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Uncategorized {@link Predicate} methods.
 */
public final class VecPredicates {

    private VecPredicates() {
        // hide default constructor
    }

    public static Predicate<VecPlaceableElementRole> isOnWayPlacement() {
        return c -> c.getPlaceableElementSpecification().getValidPlacementTypes()
                .stream()
                .anyMatch(VecPlacementType.ON_WAY::equals);
    }

    public static Predicate<VecPlaceableElementRole> isOnPointPlacement() {
        return c -> c.getPlaceableElementSpecification().getValidPlacementTypes()
                .stream()
                .anyMatch(VecPlacementType.ON_POINT::equals);
    }

    public static Predicate<VecAbstractLocalizedString> isGermanLanguageCode() {
        return isLanguageCode(VecLanguageCode.DE);
    }

    public static Predicate<VecAbstractLocalizedString> isEnglishLanguageCode() {
        return isLanguageCode(VecLanguageCode.EN);
    }

    public static Predicate<VecAbstractLocalizedString> isLanguageCode(final VecLanguageCode code) {
        return localizedString -> code == localizedString.getLanguageCode();
    }

    /**
     * Checks if an {@link VecEEComponentRole} is of specific subtype (which not
     * expressed by inheritance of {@link VecEEComponentRole}, but by
     * inheritance of the corresponding {@link VecEEComponentSpecification}.
     *
     * @param specificClass the class to check against.
     */
    public static Predicate<VecEEComponentRole> isEEComponentOfSpecificType(
            final Class<? extends VecEEComponentSpecification> specificClass) {
        return role -> specificClass.isInstance(role.getEEComponentSpecification());
    }

    /**
     * Checks if an {@link VecPartOccurrence} is of specific subtype (which not
     * expressed by inheritance of {@link VecEEComponentRole}, but by
     * inheritance of the corresponding {@link VecEEComponentSpecification}.
     */
    public static Predicate<VecOccurrenceOrUsage> isFuse() {
        return occ -> occ.getRolesWithType(VecEEComponentRole.class).stream()
                .anyMatch(isEEComponentOfSpecificType(VecFuseSpecification.class)
                        .or(isEEComponentOfSpecificType(VecMultiFuseSpecification.class)));
    }

    /**
     * Checks if an {@link VecPartOccurrence} is of specific subtype (which not
     * expressed by inheritance of {@link VecEEComponentRole}, but by
     * inheritance of the corresponding {@link VecEEComponentSpecification}.
     */
    public static Predicate<VecOccurrenceOrUsage> isRelay() {
        return occ -> occ.getRolesWithType(VecEEComponentRole.class).stream()
                .anyMatch(isEEComponentOfSpecificType(VecRelaySpecification.class));
    }

    @RequiresBackReferences
    public static Predicate<VecWireElementReference> isRootWireElementReference() {
        return reference -> {
            final VecWireElement referencedWireElement = reference.getReferencedWireElement();
            final VecWireSpecification wireSpecification = referencedWireElement.getParentWireSpecification();
            return wireSpecification != null &&
                    referencedWireElement.getWireElementSpecification()
                            .equals(wireSpecification.getWireElementSpecification());
        };
    }

    /**
     * Checks if a {@link VecWireElementReference} is a single wire element reference.
     * <p>
     * Notice: Master data context is not checked!
     *
     * @return Predicate to check if the tested WireElementReference is single.
     */
    @RequiresBackReferences
    public static Predicate<VecWireElementReference> isSingleWireElementReference() {
        return reference -> reference.getParentWireRole().getWireElementReferences().size() == 1;
    }

    /**
     * Checks if a PartOccurrence is an assembly.
     * <p>
     * This check depends on the context, for a module this will also be {@code true}.
     *
     * @return Check to test if a PartOccurrence is an assembly.
     */
    public static Predicate<VecPartOccurrence> isAssemblyOccurrence() {
        return occ -> !occ.getRolesWithType(VecPartWithSubComponentsRole.class).isEmpty();
    }

    public static Predicate<VecOccurrenceOrUsage> isPartUsage() {
        return VecPartUsage.class::isInstance;
    }

    public static Predicate<VecOccurrenceOrUsage> isPartOccurrence() {
        return VecPartOccurrence.class::isInstance;
    }

    public static Predicate<VecPartOccurrence> isWire() {
        return occ -> !occ.getRolesWithType(VecWireRole.class).isEmpty();
    }

    public static Predicate<VecPartOccurrence> isPlug() {
        return occ -> !occ.getRolesWithType(VecCavityPlugRole.class).isEmpty();
    }

    public static Predicate<VecPartVersion> isEqualPartVersion(final VecPartVersion comparePartVersion) {
        return partVersion -> partVersion.getPartNumber().equals(comparePartVersion.getPartNumber())
                && partVersion.getPartVersion().equals(comparePartVersion.getPartVersion())
                && partVersion.getCompanyName().equals(comparePartVersion.getCompanyName())
                && partVersion.getPrimaryPartType() == comparePartVersion.getPrimaryPartType();
    }

    public static Predicate<VecSIUnit> isEqualSiUnit(final VecSIUnit compareUnit) {
        return unit -> unit.getSiUnitName() == compareUnit.getSiUnitName()
                && unit.getSiPrefix() == compareUnit.getSiPrefix()
                && Objects.equals(unit.getExponent(), compareUnit.getExponent());
    }

    public static Predicate<VecUSUnit> isEqualUsUnit(final VecUSUnit compareUnit) {
        return unit -> unit.getUsUnitName() == compareUnit.getUsUnitName()
                && Objects.equals(unit.getExponent(), compareUnit.getExponent());
    }

    public static Predicate<VecCompositeUnit> isEqualCompositeUnit(final VecCompositeUnit compareUnit) {
        return unit -> unit.getFactors().stream()
                .allMatch(u -> compareUnit.getFactors().stream().anyMatch(isEqualUnit(u)))
                && Objects.equals(unit.getExponent(), compareUnit.getExponent());
    }

    public static Predicate<VecUnit> isEqualUnit(final VecUnit compareUnit) {
        return unit -> {
            if (unit instanceof VecSIUnit && compareUnit instanceof VecSIUnit) {
                return isEqualSiUnit((VecSIUnit) compareUnit).test((VecSIUnit) unit);
            }

            if (unit instanceof VecUSUnit && compareUnit instanceof VecUSUnit) {
                return isEqualUsUnit((VecUSUnit) compareUnit).test((VecUSUnit) unit);
            }

            if (unit instanceof VecCompositeUnit && compareUnit instanceof VecCompositeUnit) {
                return isEqualCompositeUnit((VecCompositeUnit) compareUnit).test((VecCompositeUnit) unit);
            }
            return false;
        };
    }

    public static Predicate<VecContract> isEqualContract(final VecContract compareContract) {
        return contract -> contract.getContractRole().equals(compareContract.getContractRole()) &&
                contract.getCompanyName().equals(compareContract.getCompanyName());
    }

    public static Predicate<VecOnPointPlacement> isOnPointPlacementOf(final VecTopologyNode topologyNode) {
        return vecPlacement -> vecPlacement.getLocations().stream()
                .filter(VecNodeLocation.class::isInstance)
                .map(VecNodeLocation.class::cast)
                .map(VecNodeLocation::getReferencedNode)
                .anyMatch(n -> n.equals(topologyNode));
    }

}
