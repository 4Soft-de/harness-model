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

import com.foursoft.harness.kbl.v25.KblCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblComponent;
import com.foursoft.harness.kbl.v25.KblComponentCavityOccurrence;
import com.foursoft.harness.kbl.v25.KblComponentOccurrence;
import com.foursoft.harness.navext.runtime.model.Identifiable;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * KBL describes no cavities of a component, but a component occurrence can be mounted on cavities ({@code
 * Component_cavity_occurrence} of a component box, {@code Cavity_occurrence} of a connector). The cavities of the
 * component are derived from these mountings: the distinct cavity numbers of all mounted cavities of all occurrences of
 * the component become the cavities (and pin components) of its single housing.
 * <p> The records are the sources of the transformation. They are {@link Identifiable} by the component (occurrence)
 * and the cavity number, so that the XML ids of the VEC elements are derived from the KBL ids.
 */
public final class ComponentCavities {

    private static final Pattern LEADING_NUMBER = Pattern.compile("^(\\d+)(.*)$");
    private static final Pattern NO_XML_ID_CHARACTER = Pattern.compile("[^A-Za-z0-9_.-]");

    /**
     * The single slot of the housing of a component, it holds the derived cavities.
     */
    public record ComponentSlot(KblComponent component) implements Identifiable {
        @Override
        public String getXmlId() {
            return component.getXmlId();
        }
    }

    /**
     * A derived cavity (and pin component) of a component.
     */
    public record ComponentCavity(KblComponent component, String cavityNumber) implements Identifiable {
        @Override
        public String getXmlId() {
            return component.getXmlId() + "_" + xmlIdPart(cavityNumber);
        }
    }

    /**
     * The slot of the housing of a component occurrence that is mounted on cavities.
     */
    public record MountedSlot(KblComponentOccurrence occurrence) implements Identifiable {
        @Override
        public String getXmlId() {
            return occurrence.getXmlId();
        }

        public ComponentSlot slot() {
            return new ComponentSlot(occurrence.getPart());
        }
    }

    /**
     * A cavity of a component occurrence, mounted on a cavity of a component box or connector.
     */
    public record MountedCavity(KblComponentOccurrence occurrence, Object mountedOn, String cavityNumber)
            implements Identifiable {
        @Override
        public String getXmlId() {
            return occurrence.getXmlId() + "_" + xmlIdPart(cavityNumber);
        }

        public ComponentCavity cavity() {
            return new ComponentCavity(occurrence.getPart(), cavityNumber);
        }
    }

    private ComponentCavities() {
        // hide default constructor
    }

    /**
     * @return The derived cavities of the component, sorted by their cavity number.
     */
    public static List<ComponentCavity> cavities(final KblComponent component) {
        return component.getRefComponentOccurrence().stream()
                .flatMap(occurrence -> mountedCavities(occurrence).stream())
                .map(MountedCavity::cavityNumber)
                .distinct()
                .sorted(CAVITY_NUMBER_ORDER)
                .map(number -> new ComponentCavity(component, number))
                .toList();
    }

    /**
     * @return The cavities the occurrence is mounted on, in the order of the mounting.
     */
    public static List<MountedCavity> mountedCavities(final KblComponentOccurrence occurrence) {
        return occurrence.getMounting().stream()
                .map(mounted -> {
                    final String cavityNumber = cavityNumber(mounted);
                    return cavityNumber == null ? null : new MountedCavity(occurrence, mounted, cavityNumber);
                })
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private static String cavityNumber(final Object mounted) {
        if (mounted instanceof final KblComponentCavityOccurrence cavity && cavity.getPart() != null) {
            return cavity.getPart().getCavityNumber();
        }
        if (mounted instanceof final KblCavityOccurrence cavity && cavity.getPart() != null) {
            return cavity.getPart().getCavityNumber();
        }
        return null;
    }

    /**
     * Cavity numbers like {@code 2A} and {@code 10A} are sorted by their leading number first.
     */
    private static final Comparator<String> CAVITY_NUMBER_ORDER = Comparator
            .comparing(ComponentCavities::leadingNumber, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(Comparator.naturalOrder());

    private static Long leadingNumber(final String cavityNumber) {
        final Matcher matcher = LEADING_NUMBER.matcher(cavityNumber);
        return matcher.matches() && matcher.group(1).length() < 18 ? Long.valueOf(matcher.group(1)) : null;
    }

    private static String xmlIdPart(final String cavityNumber) {
        return NO_XML_ID_CHARACTER.matcher(cavityNumber).replaceAll("_");
    }
}
