/*-
 * ========================LICENSE_START=================================
 * VEC 2.X
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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
package com.foursoft.harness.vec.v2x.predicates;

import com.foursoft.harness.vec.common.annotations.RequiresBackReferences;
import com.foursoft.harness.vec.v2x.*;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Uncategorized {@link Predicate} methods.
 */
public final class VecPredicates {

    private VecPredicates() {
        // hide default constructor
    }

    public static Predicate<VecPlaceableElementRole> onWayPlacement() {
        return c -> c.getPlaceableElementSpecification().getValidPlacementTypes()
                .stream()
                .anyMatch(VecPlacementType.ON_WAY::equals);
    }

    public static Predicate<VecPlaceableElementRole> onPointPlacement() {
        return c -> c.getPlaceableElementSpecification().getValidPlacementTypes()
                .stream()
                .anyMatch(VecPlacementType.ON_POINT::equals);
    }

    public static Predicate<VecAbstractLocalizedString> germanLanguageCode() {
        return languageCode(VecLanguageCode.DE);
    }

    public static Predicate<VecAbstractLocalizedString> englishLanguageCode() {
        return languageCode(VecLanguageCode.EN);
    }

    public static Predicate<VecAbstractLocalizedString> languageCode(final VecLanguageCode code) {
        return localizedString -> code == localizedString.getLanguageCode();
    }

    /**
     * Checks if an {@link VecEEComponentRole} is of specific subtype (which not
     * expressed by inheritance of {@link VecEEComponentRole}, but by
     * inheritance of the corresponding {@link VecEEComponentSpecification}.
     *
     * @param specificClass the class to check against.
     */
    public static Predicate<VecEEComponentRole> eEComponentOfSpecificType(
            final Class<? extends VecEEComponentSpecification> specificClass) {
        return role -> specificClass.isInstance(role.getEEComponentSpecification());
    }

    /**
     * Checks if an {@link VecPartOccurrence} is of specific subtype (which not
     * expressed by inheritance of {@link VecEEComponentRole}, but by
     * inheritance of the corresponding {@link VecEEComponentSpecification}.
     */
    public static Predicate<VecOccurrenceOrUsage> fuse() {
        return occ -> occ.getRolesWithType(VecEEComponentRole.class).stream()
                .anyMatch(eEComponentOfSpecificType(VecFuseSpecification.class)
                                  .or(eEComponentOfSpecificType(VecMultiFuseSpecification.class)));
    }

    /**
     * Checks if an {@link VecPartOccurrence} is of specific subtype (which not
     * expressed by inheritance of {@link VecEEComponentRole}, but by
     * inheritance of the corresponding {@link VecEEComponentSpecification}.
     */
    public static Predicate<VecOccurrenceOrUsage> relay() {
        return occ -> occ.getRolesWithType(VecEEComponentRole.class).stream()
                .anyMatch(eEComponentOfSpecificType(VecRelaySpecification.class));
    }

    @RequiresBackReferences
    public static Predicate<VecWireElementReference> rootWireElementReference() {
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
    public static Predicate<VecWireElementReference> singleWireElementReference() {
        return reference -> reference.getParentWireRole().getWireElementReferences().size() == 1;
    }

    /**
     * Checks if a PartOccurrence is an assembly.
     * <p>
     * This check depends on the context, for a module this will also be {@code true}.
     *
     * @return Check to test if a PartOccurrence is an assembly.
     */
    public static Predicate<VecPartOccurrence> assemblyOccurrence() {
        return occ -> !occ.getRolesWithType(VecPartWithSubComponentsRole.class).isEmpty();
    }

    public static Predicate<VecOccurrenceOrUsage> partUsage() {
        return VecPartUsage.class::isInstance;
    }

    public static Predicate<VecOccurrenceOrUsage> partOccurrence() {
        return VecPartOccurrence.class::isInstance;
    }

    public static Predicate<VecPartOccurrence> wire() {
        return occ -> !occ.getRolesWithType(VecWireRole.class).isEmpty();
    }

    public static Predicate<VecPartOccurrence> plug() {
        return occ -> !occ.getRolesWithType(VecCavityPlugRole.class).isEmpty();
    }

    public static Predicate<VecPartOccurrence> seal() {
        return occ -> !occ.getRolesWithType(VecCavitySealRole.class).isEmpty();
    }

    public static Predicate<VecPartVersion> equalPartVersion(final VecPartVersion comparePartVersion) {
        return partVersion -> partVersion.getPartNumber().equals(comparePartVersion.getPartNumber())
                && partVersion.getPartVersion().equals(comparePartVersion.getPartVersion())
                && partVersion.getCompanyName().equals(comparePartVersion.getCompanyName())
                && partVersion.getPrimaryPartType() == comparePartVersion.getPrimaryPartType();
    }

    public static Predicate<VecSIUnit> equalSiUnit(final VecSIUnit compareUnit) {
        return unit -> unit.getSiUnitName() == compareUnit.getSiUnitName()
                && unit.getSiPrefix() == compareUnit.getSiPrefix()
                && Objects.equals(unit.getExponent(), compareUnit.getExponent());
    }

    public static Predicate<VecUSUnit> equalUsUnit(final VecUSUnit compareUnit) {
        return unit -> unit.getUsUnitName() == compareUnit.getUsUnitName()
                && Objects.equals(unit.getExponent(), compareUnit.getExponent());
    }

    public static Predicate<VecCompositeUnit> equalCompositeUnit(final VecCompositeUnit compareUnit) {
        return unit -> unit.getFactors().stream()
                .allMatch(u -> compareUnit.getFactors().stream().anyMatch(equalUnit(u)))
                && Objects.equals(unit.getExponent(), compareUnit.getExponent());
    }

    public static Predicate<VecUnit> equalUnit(final VecUnit compareUnit) {
        return unit -> {
            if (unit instanceof VecSIUnit && compareUnit instanceof VecSIUnit) {
                return equalSiUnit((VecSIUnit) compareUnit).test((VecSIUnit) unit);
            }

            if (unit instanceof VecUSUnit && compareUnit instanceof VecUSUnit) {
                return equalUsUnit((VecUSUnit) compareUnit).test((VecUSUnit) unit);
            }

            if (unit instanceof VecCompositeUnit && compareUnit instanceof VecCompositeUnit) {
                return equalCompositeUnit((VecCompositeUnit) compareUnit).test((VecCompositeUnit) unit);
            }
            return false;
        };
    }

    public static Predicate<VecContract> equalContract(final VecContract compareContract) {
        return contract -> contract.getContractRole().equals(compareContract.getContractRole()) &&
                contract.getCompanyName().equals(compareContract.getCompanyName());
    }

    public static Predicate<VecOnPointPlacement> onPointPlacementOf(final VecTopologyNode topologyNode) {
        return vecPlacement -> vecPlacement.getLocations().stream()
                .filter(VecNodeLocation.class::isInstance)
                .map(VecNodeLocation.class::cast)
                .map(VecNodeLocation::getReferencedNode)
                .anyMatch(n -> n.equals(topologyNode));
    }

}
