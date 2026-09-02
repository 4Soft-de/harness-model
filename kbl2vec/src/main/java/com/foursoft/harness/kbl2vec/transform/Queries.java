/*-
 * ========================LICENSE_START=================================
 * KBL to VEC Converter
 * %%
 * Copyright (C) 2025 4Soft GmbH
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
package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.common.HasPlacement;
import com.foursoft.harness.kbl.v25.*;
import com.foursoft.harness.kbl2vec.core.Query;
import com.foursoft.harness.kbl2vec.core.TransformationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Queries {

    private Queries() {
        throw new AssertionError("Should not be instantiated");
    }

    public static Query<KblPart> allParts(final KBLContainer container) {
        return () -> concat(container.getParts(), List.of(container.getHarness()), container.getHarness()
                .getModules());
    }

    private static <T> List<T> concat(final List<? extends T>... lists) {
        return Arrays.stream(lists)
                .flatMap(List::stream)
                .map(e -> (T) e)
                .toList();
    }

    public static Query<ConnectionOrOccurrence> partOccurrences(final List<ConnectionOrOccurrence> components) {
        return () -> components
                .stream()
                .filter(c -> !(c instanceof KblConnection))
                .toList();
    }

    public static Query<ConnectionOrOccurrence> partOccurrences(final KblModuleConfiguration source) {
        return partOccurrences(source.getControlledComponents());
    }

    public static Query<ConnectionOrOccurrence> placeablePartOccurrences(final KblHarness harness) {
        return () -> harness.getConnectionOrOccurrences().stream()
                .filter(HasPlacement.class::isInstance)
                .toList();
    }

    /**
     * Resolves the modules a {@code module list} configuration refers to.
     * <p>
     * KBL has no proper reference for this: {@code Logistic_control_information} is declared as a plain string,
     * but for {@link KblModuleConfigurationType#MODULE_LIST} configurations it is misused as a whitespace
     * separated list of XML IDs of {@link KblModule}s. JAXB therefore does not resolve it, and the lookup has to
     * be done by hand against the modules of the harness. Tokens that do not resolve are skipped with a warning.
     * <p>
     * The result is the module's {@link KblModule#getModuleConfiguration() configuration}, because that is the
     * source element the {@code VecPartWithSubComponentsRole} of a module is created from.
     */
    public static Query<KblModuleConfiguration> modulesInList(final KblModuleConfiguration source,
                                                              final TransformationContext context) {
        return () -> {
            final KblHarness harness = harnessOf(source);
            if (harness == null) {
                context.getLogger().warn("Module list (xml ID: {}) is not contained in a harness, its modules " +
                                                 "cannot be resolved.", source.getXmlId());
                return List.of();
            }
            final Map<String, KblModule> modulesByXmlId = modulesByXmlId(harness, context);
            return referencedXmlIds(source)
                    .map(xmlId -> {
                        final KblModule module = modulesByXmlId.get(xmlId);
                        if (module == null) {
                            context.getLogger().warn(
                                    "Module list (xml ID: {}) references '{}' in its " +
                                            "Logistic_control_information, but no module with that xml ID exists.",
                                    source.getXmlId(), xmlId);
                        }
                        return module;
                    })
                    .filter(Objects::nonNull)
                    .map(KblModule::getModuleConfiguration)
                    .filter(Objects::nonNull)
                    .toList();
        };
    }

    private static Stream<String> referencedXmlIds(final KblModuleConfiguration source) {
        final String logisticControlInformation = source.getLogisticControlInformation();
        if (logisticControlInformation == null || logisticControlInformation.isBlank()) {
            return Stream.empty();
        }
        return Arrays.stream(logisticControlInformation.trim().split("\\s+"))
                .distinct();
    }

    /**
     * Index of the harness' modules by their XML ID, cached for the duration of the conversion because every
     * module list of the harness needs it.
     */
    private static Map<String, KblModule> modulesByXmlId(final KblHarness harness,
                                                         final TransformationContext context) {
        return context.getCached(new ModulesByXmlIdKey(harness),
                                 () -> harness.getModules().stream()
                                         .filter(m -> m.getXmlId() != null)
                                         .collect(Collectors.toMap(KblModule::getXmlId, Function.identity(),
                                                                   (first, second) -> first)));
    }

    private static KblHarness harnessOf(final KblModuleConfiguration source) {
        if (source.getParentHarness() != null) {
            return source.getParentHarness();
        }
        return source.getParentModule() == null ? null : source.getParentModule().getParentHarness();
    }

    /**
     * Cache key, so that the cached index cannot collide with another value cached for the same harness.
     */
    private record ModulesByXmlIdKey(KblHarness harness) {
    }
}
