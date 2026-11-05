/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 - 2026 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.transform.components.ee_components;

import com.foursoft.harness.kbl.v25.KblComponent;
import com.foursoft.harness.kbl.v25.KblComponentCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentOccurrence;
import com.foursoft.harness.kbl2vec.core.TestConversionOrchestrator;
import com.foursoft.harness.kbl2vec.transform.components.ee_components.ComponentCavities.ComponentCavity;
import com.foursoft.harness.kbl2vec.transform.components.ee_components.ComponentCavities.ComponentSlot;
import com.foursoft.harness.kbl2vec.transform.components.ee_components.ComponentCavities.MountedCavity;
import com.foursoft.harness.kbl2vec.transform.components.ee_components.ComponentCavities.MountedSlot;
import com.foursoft.harness.vec.v2x.VecCavity;
import com.foursoft.harness.vec.v2x.VecCavityReference;
import com.foursoft.harness.vec.v2x.VecConnectorHousingRole;
import com.foursoft.harness.vec.v2x.VecConnectorHousingSpecification;
import com.foursoft.harness.vec.v2x.VecHousingComponent;
import com.foursoft.harness.vec.v2x.VecHousingComponentReference;
import com.foursoft.harness.vec.v2x.VecPinComponent;
import com.foursoft.harness.vec.v2x.VecPinComponentReference;
import com.foursoft.harness.vec.v2x.VecSlot;
import com.foursoft.harness.vec.v2x.VecSlotReference;
import org.junit.jupiter.api.Test;

import static com.foursoft.harness.kbl2vec.transform.components.ee_components.ComponentCavitiesTest.componentCavity;
import static com.foursoft.harness.kbl2vec.transform.components.ee_components.ComponentCavitiesTest.occurrence;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * The part master and instance structure of the cavities derived for a component (see {@link ComponentCavities}).
 */
class ComponentCavityStructureTest {

    private final KblComponent component = component();
    private final KblComponentCavityOccurrence mountedOn = componentCavity("2A");
    private final KblComponentOccurrence occurrence = occurrence(component, mountedOn);
    private final ComponentCavity cavity = new ComponentCavity(component, "2A");
    private final MountedCavity mountedCavity = new MountedCavity(occurrence, mountedOn, "2A");

    @Test
    void should_giveTheHousingSpecificationASlot_withTheDerivedCavities() {
        // Given
        final VecSlot slot = new VecSlot();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(new ComponentSlot(component), slot);

        // When
        final VecConnectorHousingSpecification specification =
                orchestrator.transform(new ComponentHousingSpecificationTransformer(), component);
        final VecSlot vecSlot = new TestConversionOrchestrator().addMockMapping(cavity, new VecCavity())
                .transform(new ComponentSlotTransformer(), new ComponentSlot(component));
        final VecCavity vecCavity = new TestConversionOrchestrator()
                .transform(new ComponentCavityTransformer(), cavity);

        // Then
        assertThat(specification.getSlots()).containsExactly(slot);
        assertThat(vecSlot.getCavities()).hasSize(1);
        assertThat(vecCavity.getCavityNumber()).isEqualTo("2A");
    }

    @Test
    void should_giveTheHousingComponentPinComponents_referencingTheCavities() {
        // Given
        final VecPinComponent pinComponent = new VecPinComponent();
        final VecCavity vecCavity = new VecCavity();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(component, new VecConnectorHousingSpecification())
                .addMockMapping(cavity, pinComponent)
                .addMockMapping(cavity, vecCavity);

        // When
        final VecHousingComponent housingComponent =
                orchestrator.transform(new ComponentHousingTransformer(), component);
        final VecPinComponent vecPinComponent =
                orchestrator.transform(new ComponentPinComponentTransformer(), cavity);

        // Then
        assertThat(housingComponent.getPinComponents()).containsExactly(pinComponent);
        assertThat(vecPinComponent)
                .returns("2A", VecPinComponent::getIdentification)
                .returns(vecCavity, VecPinComponent::getReferencedCavity);
    }

    @Test
    void should_referenceTheMountedCavities_inTheRoleOfTheOccurrence() {
        // Given
        final VecSlot slot = new VecSlot();
        final VecCavity vecCavity = new VecCavity();
        final VecPinComponent pinComponent = new VecPinComponent();
        final VecSlotReference slotReference = new VecSlotReference();
        final VecCavityReference cavityReference = new VecCavityReference();
        final VecPinComponentReference pinComponentReference = new VecPinComponentReference();
        final TestConversionOrchestrator orchestrator = new TestConversionOrchestrator()
                .addMockMapping(component, new VecConnectorHousingSpecification())
                .addMockMapping(component, new VecHousingComponent())
                .addMockMapping(occurrence, new VecConnectorHousingRole())
                .addMockMapping(new MountedSlot(occurrence), slotReference)
                .addMockMapping(new ComponentSlot(component), slot)
                .addMockMapping(mountedCavity, cavityReference)
                .addMockMapping(mountedCavity, pinComponentReference)
                .addMockMapping(cavity, vecCavity)
                .addMockMapping(cavity, pinComponent);

        // When
        final VecConnectorHousingRole role = orchestrator.transform(new ComponentHousingRoleTransformer(), occurrence);
        final VecHousingComponentReference housingReference =
                orchestrator.transform(new ComponentHousingReferenceTransformer(), occurrence);
        final VecSlotReference vecSlotReference =
                orchestrator.transform(new MountedSlotReferenceTransformer(), new MountedSlot(occurrence));
        final VecCavityReference vecCavityReference =
                orchestrator.transform(new MountedCavityReferenceTransformer(), mountedCavity);
        final VecPinComponentReference vecPinComponentReference =
                orchestrator.transform(new MountedPinComponentReferenceTransformer(), mountedCavity);

        // Then
        assertThat(role.getSlotReferences()).containsExactly(slotReference);
        assertThat(housingReference.getPinComponentReves()).containsExactly(pinComponentReference);
        assertThat(vecSlotReference)
                .returns(slot, VecSlotReference::getReferencedSlot)
                .satisfies(v -> assertThat(v.getCavityReferences()).containsExactly(cavityReference));
        assertThat(vecCavityReference)
                .returns("2A", VecCavityReference::getIdentification)
                .returns(vecCavity, VecCavityReference::getReferencedCavity);
        assertThat(vecPinComponentReference)
                .returns("2A", VecPinComponentReference::getIdentification)
                .returns(pinComponent, VecPinComponentReference::getPinComponent);
    }

    private static KblComponent component() {
        final KblComponent component = new KblComponent();
        component.setPartNumber("F-001");
        return component;
    }
}
