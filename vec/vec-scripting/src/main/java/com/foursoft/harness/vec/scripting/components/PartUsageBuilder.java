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
package com.foursoft.harness.vec.scripting.components;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.Customizer;
import com.foursoft.harness.vec.scripting.VecSession;
import com.foursoft.harness.vec.scripting.components.protection.CorrugatedPipeRoleBuilder;
import com.foursoft.harness.vec.scripting.core.SpecificationLocator;
import com.foursoft.harness.vec.scripting.eecomponents.FuseRoleBuilder;
import com.foursoft.harness.vec.scripting.eecomponents.RelayRoleBuilder;
import com.foursoft.harness.vec.scripting.schematic.ConnectionSpecificationQueries;
import com.foursoft.harness.vec.v2x.*;

public class PartUsageBuilder implements Builder<VecPartUsage> {

    private final VecSession session;
    private final SpecificationLocator specificationLocator;
    private final ConnectionSpecificationQueries connectionSpecificationQueries;
    private final VecPartUsage partUsage = new VecPartUsage();

    public PartUsageBuilder(final VecSession session, final String identification,
                            final SpecificationLocator specificationLocator,
                            final ConnectionSpecificationQueries connectionSpecificationQueries) {
        this.session = session;
        this.specificationLocator = specificationLocator;
        this.connectionSpecificationQueries = connectionSpecificationQueries;
        this.partUsage.setIdentification(identification);
    }

    public PartUsageBuilder addCorrugatedPipeSpecification(final String specificationIdentification,
                                                           final Customizer<CorrugatedPipeRoleBuilder> customizer) {
        final VecCorrugatedPipeSpecification specification = specificationLocator.find(
                        VecCorrugatedPipeSpecification.class,
                        specificationIdentification)
                .orElseThrow();

        partUsage.getPartOrUsageRelatedSpecification().add(specification);

        final CorrugatedPipeRoleBuilder roleBuilder = new CorrugatedPipeRoleBuilder(partUsage.getIdentification(),
                                                                                    specification);
        customizer.customize(roleBuilder);

        partUsage.getRoles().add(roleBuilder.build());

        if (partUsage.getPrimaryPartUsageType() == null) {
            partUsage.setPrimaryPartUsageType(VecPrimaryPartType.CORRUGATED_PIPE);
        }

        return this;
    }

    public PartUsageBuilder addPartSubstitution(final String specificationIdentification) {
        final VecPartSubstitutionSpecification specification = specificationLocator.find(
                        VecPartSubstitutionSpecification.class,
                        specificationIdentification)
                .orElseThrow();

        partUsage.getPartOrUsageRelatedSpecification().add(specification);

        return this;
    }

    public PartUsageBuilder addWireSpecification(final String specificationIdentification,
                                                 final Customizer<WireRoleBuilder> customizer) {
        final VecWireSpecification wireSpecification = specificationLocator.find(VecWireSpecification.class,
                                                                                 specificationIdentification)
                .orElseThrow();

        partUsage.getPartOrUsageRelatedSpecification().add(wireSpecification);

        final WireRoleBuilder wireRoleBuilder = new WireRoleBuilder(session, partUsage.getIdentification(),
                                                                    wireSpecification,
                                                                    connectionSpecificationQueries::findConnection);

        customizer.customize(wireRoleBuilder);

        final VecWireRole wireRole = wireRoleBuilder.build();

        partUsage.getRoles().add(wireRole);

        if (partUsage.getPrimaryPartUsageType() == null) {
            partUsage.setPrimaryPartUsageType(VecPrimaryPartType.WIRE);
        }

        return this;
    }

    public PartUsageBuilder addFuseSpecification(final String specificationIdentification,
                                                 final Customizer<FuseRoleBuilder> customizer) {
        final VecFuseSpecification fuseSpecification = specificationLocator.find(VecFuseSpecification.class,
                                                                                 specificationIdentification)
                .orElseThrow();

        partUsage.getPartOrUsageRelatedSpecification().add(fuseSpecification);

        final FuseRoleBuilder fuseRoleBuilder = new FuseRoleBuilder(session, partUsage.getIdentification(),
                                                                    fuseSpecification,
                                                                    connectionSpecificationQueries::findNode);

        customizer.customize(fuseRoleBuilder);

        final VecFuseRole fuseRole = fuseRoleBuilder.build();

        partUsage.getRoles().add(fuseRole);

        if (partUsage.getPrimaryPartUsageType() == null) {
            partUsage.setPrimaryPartUsageType(VecPrimaryPartType.FUSE);
        }

        return this;
    }

    public PartUsageBuilder addRelaySpecification(final String specificationIdentification,
                                                  final Customizer<RelayRoleBuilder> customizer) {
        final VecRelaySpecification relaySpecification = specificationLocator.find(VecRelaySpecification.class,
                                                                                   specificationIdentification)
                .orElseThrow();

        partUsage.getPartOrUsageRelatedSpecification().add(relaySpecification);

        final RelayRoleBuilder relayRoleBuilder = new RelayRoleBuilder(session, partUsage.getIdentification(),
                                                                       relaySpecification,
                                                                       connectionSpecificationQueries::findNode);
        customizer.customize(relayRoleBuilder);

        final VecRelayRole fuseRole = relayRoleBuilder.build();

        partUsage.getRoles().add(fuseRole);

        if (partUsage.getPrimaryPartUsageType() == null) {
            partUsage.setPrimaryPartUsageType(VecPrimaryPartType.RELAY);
        }

        return this;
    }

    @Override
    public VecPartUsage build() {
        if (partUsage.getPrimaryPartUsageType() == null) {
            partUsage.setPrimaryPartUsageType(VecPrimaryPartType.EE_COMPONENT);
        }

        return partUsage;
    }

}
