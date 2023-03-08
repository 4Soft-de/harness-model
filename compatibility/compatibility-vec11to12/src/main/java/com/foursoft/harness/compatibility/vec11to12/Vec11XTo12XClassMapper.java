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

import com.foursoft.harness.compatibility.core.HasUnsupportedMethods;
import com.foursoft.harness.compatibility.core.MethodIdentifier;
import com.foursoft.harness.compatibility.core.mapping.NameBasedClassMapper;
import com.foursoft.harness.compatibility.core.util.ClassUtils;
import com.foursoft.harness.vec.v113.VecBSplineCurve;
import com.foursoft.harness.vec.v113.VecZone;
import com.foursoft.harness.vec.v12x.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

/**
 * Class responsible for mapping VEC 1.1.X classes to VEC 1.2.X <b>and vice versa</b>.
 */
public class Vec11XTo12XClassMapper extends NameBasedClassMapper {

    private final Map<Class<?>, Class<?>> classMap;
    private final UnsupportedVec11XToVec12XMethods ignored;

    /**
     * Creates a VEC 1.1 to VEC 1.2 Class Mapper.
     */
    public Vec11XTo12XClassMapper() {
        super(Constants.PACKAGE_VEC11X, Constants.PACKAGE_VEC12X);

        classMap = new HashMap<>();

        // VEC 1.1.X -> VEC 1.2.X
        classMap.put(VecBSplineCurve.class, VecNURBSCurve.class);
        classMap.put(VecZone.class, VecTopologyZone.class);

        // VEC 1.2.X -> VEC 1.1.X
        classMap.put(VecNURBSCurve.class, VecBSplineCurve.class);
        classMap.put(VecTopologyZone.class, VecZone.class);

        ignored = new UnsupportedVec11XToVec12XMethods();

        // Ignored VEC 1.2.X -> VEC 1.1.X methods.
        ignored.add(VecApproval.class, "getAdditionalLevelInformation");
        ignored.add(VecAliasIdentification.class, "getType");
        ignored.add(VecCableLeadThrough.class, "getCableLeadThroughSpecification");
        ignored.add(VecCavityReference.class, "getIntegratedTerminalRole");
        ignored.add(VecCavitySpecification.class, "getGeometry");
        ignored.add(VecComponentNode.class, "getSubType");
        ignored.add(VecConfigurableElement.class, "getApplicationConstraint");
        ignored.add(VecConfigurableElement.class, "getAssociatedAssignmentGroups");
        ignored.add(VecDocumentVersion.class, "getDigitalRepresentationIndex");
        ignored.add(VecExtendableElement.class, "getReferencedExternalDocuments");
        ignored.add(VecGeneralTechnicalPartSpecification.class, "getFitRate");
        ignored.add(VecGeneralTechnicalPartSpecification.class, "isUnspecifiedAccessoryPermitted");
        ignored.add(VecHousingComponent.class, "getCompatibleTypes");
        ignored.add(VecMatingPoint.class, "getConnection");
        ignored.add(VecNURBSCurve.class, "getKnots");
        ignored.add(VecPartUsage.class, "getInstanciatedUsage");
        ignored.add(VecPartUsage.class, "getReferenceElement");
        ignored.add(VecPartVersion.class, "getRefBaselineSpecification");
        ignored.add(VecPartVersion.class, "getRefContactSystem");
        ignored.add(VecPartVersion.class, "getRefExtensionSlot");
        ignored.add(VecPartVersion.class, "getRefModularSlot");
        ignored.add(VecPartVersion.class, "getRefTerminalPairing");
        ignored.add(VecPinComponent.class, "getRefDiodeSpecification");
        ignored.add(VecPinComponentReference.class, "getRefPinWireMappingPoint");
        ignored.add(VecRingTerminalSpecification.class, "getBoltNominalSize");
        ignored.add(VecSignal.class, "getCurrentType");
        ignored.add(VecSignal.class, "getDataRate");
        ignored.add(VecSlot.class, "getSupplementaryParts");
        ignored.add(VecStripeSpecification.class, "getThickness");
        ignored.add(VecTopologyNode.class, "getInstantiatedNode");
        ignored.add(VecTopologySegment.class, "getInstantiatedSegment");
        ignored.add(VecUnit.class, "getRefLocalGeometrySpecification");
        ignored.add(VecVariantConfiguration.class, "getBaseInclusion");
        ignored.add(VecWireElementReference.class, "getConnectionGroup");
        ignored.add(VecWireElementReference.class, "isUnconnected");

        // Ignored VEC 1.1.X -> VEC 1.2.X methods.
        ignored.add(com.foursoft.harness.vec.v113.VecContent.class, "getCompliantConformanceClasses");
        ignored.add(com.foursoft.harness.vec.v113.VecPath.class, "getConfigInfo");
        ignored.add(com.foursoft.harness.vec.v113.VecTopologySpecification.class, "getZones");
        ignored.add(com.foursoft.harness.vec.v113.VecWireProtectionRole.class, "getGradient");
        ignored.add(com.foursoft.harness.vec.v113.VecWireProtectionRole.class, "getTapeOverlap");
        ignored.add(com.foursoft.harness.vec.v113.VecWireProtectionRole.class, "getTapingDirection");
    }

    @Override
    public Class<?> map(final Class<?> clazz) {
        final Class<?> aClass = classMap.get(clazz);
        return aClass != null
                ? aClass
                : classMap.getOrDefault(ClassUtils.getNonProxyClass(clazz), super.map(clazz));
    }

    @Override
    public HasUnsupportedMethods checkUnsupportedMethods() {
        return ignored;
    }

    private static class UnsupportedVec11XToVec12XMethods extends HashSet<MethodIdentifier>
            implements HasUnsupportedMethods {

        private static final long serialVersionUID = 6377405392358586968L;

        @Override
        public boolean isNotSupported(final MethodIdentifier method) {
            Objects.requireNonNull(method);
            return contains(method);
        }

        public void add(final Class<?> vecClass, final String methodName) {
            final String className = vecClass.getSimpleName();
            add(new MethodIdentifier(className, methodName));
        }

    }

}
