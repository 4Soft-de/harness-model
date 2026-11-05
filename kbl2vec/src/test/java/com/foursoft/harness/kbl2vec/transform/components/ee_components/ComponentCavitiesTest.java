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

import com.foursoft.harness.kbl.v25.KblCavity;
import com.foursoft.harness.kbl.v25.KblCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblComponent;
import com.foursoft.harness.kbl.v25.KblComponentBoxOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentCavity;
import com.foursoft.harness.kbl.v25.KblComponentCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentSlotOccurrence;
import com.foursoft.harness.kbl.v25.MountingElement;
import com.foursoft.harness.kbl2vec.transform.components.ee_components.ComponentCavities.ComponentCavity;
import com.foursoft.harness.kbl2vec.transform.components.ee_components.ComponentCavities.MountedCavity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class ComponentCavitiesTest {

    @Test
    void should_deriveAsManyCavitiesAsTheOccurrenceWithTheMostMountedCavities() {
        // Given
        final KblComponent component = new KblComponent();
        for (final String number : List.of("1A", "2A", "3A", "4A", "5A", "6A", "7A", "8A", "9A")) {
            occurrence(component, componentCavity(number));
        }
        final KblComponentCavityOccurrence second = componentCavity("2A");
        final KblComponentCavityOccurrence tenth = componentCavity("10A");
        final KblComponentOccurrence occurrence = occurrence(component, tenth, second);
        occurrence.getMounting().add(new KblComponentBoxOccurrence());
        occurrence.getMounting().add(new KblComponentSlotOccurrence());

        // When
        final List<ComponentCavity> result = ComponentCavities.cavities(component);

        // Then: numbered from 1 with the common suffix, coupled in numerical order.
        assertThat(result).extracting(ComponentCavity::cavityNumber).containsExactly("1A", "2A");
        assertThat(ComponentCavities.mountedCavities(occurrence))
                .extracting(MountedCavity::mountedOn, MountedCavity::cavityNumber)
                .containsExactly(tuple(second, "1A"), tuple(tenth, "2A"));
    }

    @Test
    void should_numberTheCavitiesWithoutSuffix_whenTheMountedCavitiesHaveNoCommonSuffix() {
        // Given
        final KblComponent component = new KblComponent();
        occurrence(component, componentCavity("122"));
        occurrence(component, componentCavity("4B"), connectorCavity("1"));

        // When / Then
        assertThat(ComponentCavities.cavities(component)).extracting(ComponentCavity::cavityNumber)
                .containsExactly("1", "2");
    }

    @Test
    void should_deriveNoCavities_withoutMountedCavities() {
        // Given
        final KblComponent component = new KblComponent();
        final KblComponentOccurrence occurrence = occurrence(component);
        occurrence.getMounting().add(new KblComponentSlotOccurrence());

        // When / Then
        assertThat(ComponentCavities.cavities(component)).isEmpty();
        assertThat(ComponentCavities.mountedCavities(occurrence)).isEmpty();
    }

    @Test
    void should_deriveValidXmlIds() {
        // Given
        final KblComponent component = new KblComponent();
        component.setXmlId("id_302_2");

        // When / Then
        assertThat(new ComponentCavity(component, "1 A/2").getXmlId()).isEqualTo("id_302_2_1_A_2");
    }

    static KblComponentOccurrence occurrence(final KblComponent component, final MountingElement... mounting) {
        final KblComponentOccurrence occurrence = new KblComponentOccurrence();
        occurrence.setPart(component);
        occurrence.getMounting().addAll(List.of(mounting));
        component.getRefComponentOccurrence().add(occurrence);
        return occurrence;
    }

    static KblComponentCavityOccurrence componentCavity(final String cavityNumber) {
        final KblComponentCavity cavity = new KblComponentCavity();
        cavity.setCavityNumber(cavityNumber);
        final KblComponentCavityOccurrence occurrence = new KblComponentCavityOccurrence();
        occurrence.setPart(cavity);
        return occurrence;
    }

    static KblCavityOccurrence connectorCavity(final String cavityNumber) {
        final KblCavity cavity = new KblCavity();
        cavity.setCavityNumber(cavityNumber);
        final KblCavityOccurrence occurrence = new KblCavityOccurrence();
        occurrence.setPart(cavity);
        return occurrence;
    }
}
