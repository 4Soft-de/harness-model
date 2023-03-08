/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.1.X To VEC 1.2.X
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
package com.foursoft.harness.compatibility.vec11to12;

import com.foursoft.harness.compatibility.core.CompatibilityContext;
import com.foursoft.harness.compatibility.core.CompatibilityContext.CompatibilityContextBuilder;
import com.foursoft.harness.compatibility.core.WrapperRegistry;
import com.foursoft.harness.compatibility.core.mapping.ClassMapper;
import com.foursoft.harness.compatibility.core.wrapper.CompatibilityWrapper;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.*;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification.*;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.*;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.*;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.terminal.Vec12To11PluggableTerminalSpecificationWrapper;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.terminal.Vec12To11RingTerminalSpecificationWrapper;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.terminal.Vec12To11SpliceTerminalSpecificationWrapper;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.terminal.Vec12To11TerminalSpecificationWrapper;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.wireprotection.Vec12To11FittingSpecificationWrapper;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.wireprotection.Vec12To11StripeSpecificationWrapper;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.wireprotection.Vec12To11TapeSpecificationWrapper;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.wireprotection.Vec12To11WireProtectionSpecificationWrapper;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.wireprotection.tube.Vec12To11CorrugatedPipeSpecificationWrapper;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.wireprotection.tube.Vec12To11ShrinkableTubeSpecificationWrapper;
import com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11.specification.wireprotection.tube.Vec12To11TubeSpecificationWrapper;

/**
 * Compatibility Wrapper for VEC 1.1.X to VEC 1.2.X and vice versa.
 */
public final class Vec11XTo12XCompatibilityWrapper implements CompatibilityWrapper {

    private final CompatibilityContext compatibilityContext;

    /**
     * Creates a new VEC 1.1 to VEC 1.2 wrapper.
     */
    public Vec11XTo12XCompatibilityWrapper() {
        final ClassMapper classMapper = new Vec11XTo12XClassMapper();
        final CompatibilityContext context =
                new CompatibilityContextBuilder()
                        .withClassMapper(classMapper)
                        .withUnsupportedMethodCheck(classMapper.checkUnsupportedMethods())
                        .build();

        initializeRegistry(context);

        compatibilityContext = context;
    }

    @Override
    public CompatibilityContext getContext() {
        return compatibilityContext;
    }

    private static void initializeRegistry(final CompatibilityContext context) {
        final WrapperRegistry registry = context.getWrapperRegistry();

        // VEC 1.1.X -> VEC 1.2.X
        registry.register(com.foursoft.harness.vec.v113.VecContent.class,
                          c -> new Vec11To12ContentWrapper(context, c))
                .register(com.foursoft.harness.vec.v113.VecDocumentVersion.class,
                          c -> new Vec11To12DocumentVersionWrapper(context, c))
                .register(com.foursoft.harness.vec.v113.VecPartUsage.class,
                          c -> new Vec11To12PartUsageWrapper(context, c))
                .register(com.foursoft.harness.vec.v113.VecCavitySpecification.class,
                          c -> new Vec11To12CavitySpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v113.VecCavitySealSpecification.class,
                          c -> new Vec11To12CavitySealSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v113.VecCavityPlugSpecification.class,
                          c -> new Vec11To12CavityPlugSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v113.VecCavityPartSpecification.class,
                          c -> new Vec11To12CavityPartSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v113.VecGeometrySegment3D.class,
                          c -> new Vec11To12GeometrySegment3DWrapper(context, c))
                .register(com.foursoft.harness.vec.v113.VecBSplineCurve.class,
                          c -> new Vec11To12NURBSCurveWrapper(context, c))
                .register(com.foursoft.harness.vec.v113.VecBuildingBlockSpecification3D.class,
                          c -> new Vec11To12BuildingBlockSpecification3DWrapper(context, c))
                .register(com.foursoft.harness.vec.v113.VecWireSpecification.class,
                          c -> new Vec11To12WireSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v113.VecWireElement.class,
                          c -> new Vec11To12WireElementWrapper(context, c))
                .register(com.foursoft.harness.vec.v113.VecTapeSpecification.class,
                          c -> new Vec11To12TapeSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v113.VecOccurrenceOrUsageViewItem3D.class,
                          c -> new Vec11To12OccurrenceOrUsageViewItem3DWrapper(context, c))
                .register(com.foursoft.harness.vec.v113.VecPartOccurrence.class,
                          c -> new Vec11To12PartOccurrenceWrapper(context, c))
                .register(com.foursoft.harness.vec.v113.VecWireReceptionSpecification.class,
                          c -> new Vec11To12WireReceptionSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v113.VecGrommetRole.class,
                          c -> new Vec11To12GrommetRoleWrapper(context, c));

        // VEC 1.2.X -> VEC 1.1.X
        registry.register(com.foursoft.harness.vec.v12x.VecContent.class,
                          c -> new Vec12To11ContentWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecDocumentVersion.class,
                          c -> new Vec12To11DocumentVersionWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecPartUsage.class,
                          c -> new Vec12To11PartUsageWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecCavitySpecification.class,
                          c -> new Vec12To11CavitySpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecCavitySealSpecification.class,
                          c -> new Vec12To11CavitySealSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecCavityPlugSpecification.class,
                          c -> new Vec12To11CavityPlugSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecCavityPartSpecification.class,
                          c -> new Vec12To11CavityPartSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecGeometrySegment3D.class,
                          c -> new Vec12To11GeometrySegment3DWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecNURBSCurve.class,
                          c -> new Vec12To11BSplineCurveWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecBuildingBlockSpecification3D.class,
                          c -> new Vec12To11BuildingBlockSpecification3DWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecWireSpecification.class,
                          c -> new Vec12To11WireSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecWireElement.class,
                          c -> new Vec12To11WireElementWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecOccurrenceOrUsageViewItem3D.class,
                          c -> new Vec12To11OccurrenceOrUsageViewItem3DWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecPartOccurrence.class,
                          c -> new Vec12To11PartOccurrenceWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecConnectorHousingSpecification.class,
                          c -> new Vec12To11ConnectorHousingSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecGrommetSpecification.class,
                          c -> new Vec12To11GrommetSpecificationWrapper(context, c));
        // WireProtection Wrapper
        registry.register(com.foursoft.harness.vec.v12x.VecWireProtectionSpecification.class,
                          c -> new Vec12To11WireProtectionSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecTapeSpecification.class,
                          c -> new Vec12To11TapeSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecStripeSpecification.class,
                          c -> new Vec12To11StripeSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecFittingSpecification.class,
                          c -> new Vec12To11FittingSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecTubeSpecification.class,
                          c -> new Vec12To11TubeSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecShrinkableTubeSpecification.class,
                          c -> new Vec12To11ShrinkableTubeSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecCorrugatedPipeSpecification.class,
                          c -> new Vec12To11CorrugatedPipeSpecificationWrapper(context, c));
        // Terminal Wrapper
        registry.register(com.foursoft.harness.vec.v12x.VecTerminalSpecification.class,
                          c -> new Vec12To11TerminalSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecPluggableTerminalSpecification.class,
                          c -> new Vec12To11PluggableTerminalSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecRingTerminalSpecification.class,
                          c -> new Vec12To11RingTerminalSpecificationWrapper(context, c))
                .register(com.foursoft.harness.vec.v12x.VecSpliceTerminalSpecification.class,
                          c -> new Vec12To11SpliceTerminalSpecificationWrapper(context, c));
    }

}
