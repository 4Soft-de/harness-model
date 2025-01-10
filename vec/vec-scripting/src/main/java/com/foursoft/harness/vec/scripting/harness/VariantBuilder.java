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
package com.foursoft.harness.vec.scripting.harness;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.core.PartVersionBuilder;
import com.foursoft.harness.vec.v2x.*;

import java.util.List;
import java.util.function.Function;

import static com.foursoft.harness.vec.scripting.factories.LocalizedStringFactory.de;

public class VariantBuilder implements Builder<VariantBuilder.VariantResult> {

    private final Function<String[], List<VecPartOccurrence>> occurrencesLookup;
    private VecVariantConfiguration variantConfiguration;

    ;

    private final VecPartVersion partVersion;
    private final VecPartStructureSpecification partStructureSpecification;
    private final VecPartOccurrence moduleOccurrence;
    private VecPartWithSubComponentsRole partWithSubComponentsRole;

    public VariantBuilder(VecSession session,
                          String partNumber,
                          Function<String[], List<VecPartOccurrence>> occurrencesLookup
    ) {
        this.occurrencesLookup = occurrencesLookup;
        this.partVersion = new PartVersionBuilder(session, partNumber, VecPrimaryPartType.PART_STRUCTURE).build();

        this.partStructureSpecification = initializePartStructure(this.partVersion);
        this.moduleOccurrence = initializeModuleOccurrence(partNumber);
    }

    @Override
    public VariantResult build() {
        return new VariantResult(partVersion, moduleOccurrence, partStructureSpecification, variantConfiguration);
    }

    public VariantBuilder withAbbreviation(String text) {
        this.partVersion.getAbbreviations().add(de(text));
        return this;
    }

    public VariantBuilder withOccurrences(String... occurrences) {
        List<VecPartOccurrence> result = this.occurrencesLookup.apply(occurrences);
        this.partStructureSpecification.getInBillOfMaterial().addAll(result);
        this.partWithSubComponentsRole.getSubComponent().addAll(result);
        return this;
    }

    public VariantBuilder withConfiguration(final String configuration) {
        variantConfiguration = new VecVariantConfiguration();
        variantConfiguration.setIdentification("Config@" + this.partVersion.getPartNumber());
        variantConfiguration.setLogisticControlString(configuration);

        VecConfigurationConstraint constraint = new VecConfigurationConstraint();
        constraint.setIdentification("Config@" + this.partVersion.getPartNumber());
        constraint.setConfigInfo(variantConfiguration);

        moduleOccurrence.getConfigurationConstraints().add(constraint);

        return this;
    }

    private VecPartOccurrence initializeModuleOccurrence(String partNumber) {
        VecPartOccurrence result = new VecPartOccurrence();
        result.setIdentification(partNumber);
        result.setPart(partVersion);

        partWithSubComponentsRole = new VecPartWithSubComponentsRole();
        partWithSubComponentsRole.setIdentification(result.getIdentification());
        partWithSubComponentsRole.setPartStructureSpecification(this.partStructureSpecification);

        result.getRoles().add(partWithSubComponentsRole);

        return result;
    }

    private VecPartStructureSpecification initializePartStructure(VecPartVersion partVersion) {
        final VecPartStructureSpecification ps = new VecPartStructureSpecification();
        ps.setIdentification("STRUCTURE_" + partVersion.getPartNumber());
        ps.getDescribedPart().add(partVersion);
        ps.setSpecialPartType("Module");

        return ps;
    }

    public record VariantResult(VecPartVersion variantPart, VecPartOccurrence variantOccurrence,
                                VecPartStructureSpecification bom, VecVariantConfiguration variantConfiguration) {
    }

}
