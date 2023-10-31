/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
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
package com.foursoft.harness.vec.scripting;

import com.foursoft.harness.vec.scripting.core.DocumentVersionBuilder;
import com.foursoft.harness.vec.v2x.*;

import java.util.Arrays;
import java.util.List;

import static com.foursoft.harness.vec.scripting.factories.LocalizedStringFactory.de;

public class VariantBuilder extends AbstractChildBuilder<HarnessBuilder> {
    private final DocumentVersionBuilder harnessDocument;
    private final String partNumber;
    private final VecCompositionSpecification components;
    private final VecCompositionSpecification modules;
    private final VecPartVersion partVersion;
    private final VecPartStructureSpecification partStructureSpecification;
    private final List<VecPartOccurrence> occurrences;
    private final VecPartOccurrence moduleOccurrence;

    public VariantBuilder(final HarnessBuilder parent, final DocumentVersionBuilder harnessDocument, String partNumber,
                          String... occurrences
    ) {
        super(parent);

        this.harnessDocument = harnessDocument;
        this.partNumber = partNumber;
        this.components = harnessDocument.getSpecificationWith(VecCompositionSpecification.class,
                                                               DefaultValues.COMP_COMPOSITION_SPEC_IDENTIFICATION)
                .orElseThrow();
        this.modules = harnessDocument.getSpecificationWith(VecCompositionSpecification.class,
                                                            DefaultValues.MODULES_COMPOSITION_SPEC_IDENTIFICATION)
                .orElseThrow();

        this.partVersion = initializePart();
        this.occurrences = initializeOccurrenceList(occurrences);
        this.partStructureSpecification = initializePartStructure();
        this.moduleOccurrence = initializeModuleOccurrence();
    }

    public VariantBuilder withAbbreviation(String text) {
        this.partVersion.getAbbreviations().add(de(text));
        return this;
    }

    public VariantBuilder withConfiguration(final String configuration) {
        VecVariantConfigurationSpecification spec =
                ensureVariantConfigurationSpecification();

        VecVariantConfiguration config = new VecVariantConfiguration();
        config.setIdentification("Config@" + this.partNumber);
        config.setLogisticControlString(configuration);

        spec.getVariantConfigurations().add(config);

        VecConfigurationConstraint constraint = new VecConfigurationConstraint();
        constraint.setIdentification("Config@" + this.partNumber);
        constraint.setConfigInfo(config);

        moduleOccurrence.getConfigurationConstraints().add(constraint);

        return this;
    }

    private VecPartOccurrence initializeModuleOccurrence() {
        VecPartOccurrence result = new VecPartOccurrence();
        result.setIdentification(partNumber);
        result.setPart(partVersion);
        modules.getComponents().add(result);

        VecPartWithSubComponentsRole role = new VecPartWithSubComponentsRole();
        role.setIdentification(result.getIdentification());
        role.setPartStructureSpecification(this.partStructureSpecification);
        role.getSubComponent().addAll(occurrences);

        result.getRoles().add(role);

        return result;
    }

    private VecPartVersion initializePart() {
        final VecPartVersion part = new VecPartVersion();
        getSession().getVecContentRoot()
                .getPartVersions()
                .add(part);

        part.setCompanyName(getSession().getDefaultValues().getCompanyName());
        part.setPartVersion("1");
        part.setPartNumber(this.partNumber);
        part.setPrimaryPartType(VecPrimaryPartType.PART_STRUCTURE);

        return part;
    }

    private VecPartStructureSpecification initializePartStructure() {
        final VecPartStructureSpecification ps = new VecPartStructureSpecification();
        ps.setIdentification("STRUCTURE_" + this.partNumber);
        ps.getDescribedPart().add(this.partVersion);
        ps.setSpecialPartType("Module");
        harnessDocument.addSpecification(ps);

        ps.getInBillOfMaterial()
                .addAll(occurrences);
        return ps;
    }

    private List<VecPartOccurrence> initializeOccurrenceList(final String[] occurrenceIds) {
        final List<VecPartOccurrence> result = components.getComponents()
                .stream()
                .filter(o -> Arrays.stream(occurrenceIds)
                        .anyMatch(x -> x.equals(o.getIdentification())))
                .toList();

        if (result.size() != occurrenceIds.length) {
            throw new IllegalArgumentException(String.format("One of the occurrences in '%s' can not be found.",
                                                             Arrays.toString(occurrenceIds)));
        }
        return result;
    }

    private VecVariantConfigurationSpecification ensureVariantConfigurationSpecification() {
        return harnessDocument.getSpecificationWith(VecVariantConfigurationSpecification.class,
                                                    "VARIANTS").orElseGet(this::initializeVariantConfiguration);
    }

    private VecVariantConfigurationSpecification initializeVariantConfiguration() {
        VecVariantConfigurationSpecification result = new VecVariantConfigurationSpecification();
        result.setIdentification(DefaultValues.MODULES_COMPOSITION_SPEC_IDENTIFICATION);
        harnessDocument.addSpecification(result);
        return result;
    }

}
