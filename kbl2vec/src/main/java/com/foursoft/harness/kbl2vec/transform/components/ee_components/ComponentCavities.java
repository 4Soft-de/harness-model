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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * KBL describes no cavities of a component, but a component occurrence can be mounted on cavities
 * ({@code Component_cavity_occurrence} of a component box, {@code Cavity_occurrence} of a connector). The cavities of
 * the component are derived from these mountings:
 * <ul>
 *     <li>The single housing of the component gets as many cavities (and pin components) as the occurrence with the
 *     most mounted cavities is mounted on.</li>
 *     <li>They are numbered from {@code 1}. If all mounted cavity numbers of the component's occurrences have the same
 *     suffix (e.g. {@code 1A}, {@code 2A}), the derived numbers get it as well ({@code 1A}, …).</li>
 *     <li>The mounted cavities of an occurrence are coupled with the derived cavities in numerical order.</li>
 * </ul>
 * The records are the sources of the transformation. They are {@link Identifiable} by the component (occurrence) and
 * the derived cavity number, so that the XML ids of the VEC elements are derived from the KBL ids.
 */
public final class ComponentCavities {

    private static final Pattern LEADING_NUMBER = Pattern.compile("^(\\d+)(\\D*)$");
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
     *
     * @param cavityNumber The derived number of the component's cavity, not the number of the mounted cavity.
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
     * @return The derived cavities of the component, in the order of their numbers.
     */
    public static List<ComponentCavity> cavities(final KblComponent component) {
        final int count = component.getRefComponentOccurrence().stream()
                .mapToInt(occurrence -> mountedCavityOccurrences(occurrence).size())
                .max()
                .orElse(0);
        final String suffix = suffix(component, null);
        return IntStream.rangeClosed(1, count)
                .mapToObj(number -> new ComponentCavity(component, number + suffix))
                .toList();
    }

    /**
     * @return The cavities the occurrence is mounted on, in the numerical order of the mounted cavities, each one
     * assigned to the derived cavity of the same position.
     */
    public static List<MountedCavity> mountedCavities(final KblComponentOccurrence occurrence) {
        final List<NumberedCavity> mounted = mountedCavityOccurrences(occurrence);
        if (mounted.isEmpty()) {
            return List.of();
        }
        final String suffix = suffix(occurrence.getPart(), occurrence);
        return IntStream.range(0, mounted.size())
                .mapToObj(i -> new MountedCavity(occurrence, mounted.get(i).cavity(), (i + 1) + suffix))
                .toList();
    }

    /**
     * A mounted cavity occurrence with its cavity number.
     */
    private record NumberedCavity(Object cavity, String cavityNumber) {
    }

    private static List<NumberedCavity> mountedCavityOccurrences(final KblComponentOccurrence occurrence) {
        return occurrence.getMounting().stream()
                .distinct()
                .map(mounted -> cavityNumber(mounted).map(number -> new NumberedCavity(mounted, number)))
                .flatMap(Optional::stream)
                .sorted(Comparator.comparing(NumberedCavity::cavityNumber, CAVITY_NUMBER_ORDER))
                .toList();
    }

    /**
     * @param additional An occurrence to consider in addition to the ones known by the component, may be {@code null}.
     * @return The common suffix of all mounted cavity numbers, empty if they have none or different ones.
     */
    private static String suffix(final KblComponent component, final KblComponentOccurrence additional) {
        final Set<KblComponentOccurrence> occurrences = component == null
                ? new HashSet<>()
                : new HashSet<>(component.getRefComponentOccurrence());
        if (additional != null) {
            occurrences.add(additional);
        }
        final Set<String> suffixes = occurrences.stream()
                .flatMap(occurrence -> mountedCavityOccurrences(occurrence).stream())
                .map(NumberedCavity::cavityNumber)
                .map(number -> {
                    final Matcher matcher = LEADING_NUMBER.matcher(number);
                    return matcher.matches() ? matcher.group(2) : null;
                })
                .collect(Collectors.toSet());
        if (suffixes.size() == 1 && !suffixes.contains(null)) {
            return suffixes.iterator().next();
        }
        return "";
    }

    private static Optional<String> cavityNumber(final Object mounted) {
        if (mounted instanceof final KblComponentCavityOccurrence cavity && cavity.getPart() != null) {
            return Optional.ofNullable(cavity.getPart().getCavityNumber());
        }
        if (mounted instanceof final KblCavityOccurrence cavity && cavity.getPart() != null) {
            return Optional.ofNullable(cavity.getPart().getCavityNumber());
        }
        return Optional.empty();
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
