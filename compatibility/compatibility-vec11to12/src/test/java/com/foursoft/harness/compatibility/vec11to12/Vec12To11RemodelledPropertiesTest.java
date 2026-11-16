/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.1.X To VEC 1.2.X
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
import com.foursoft.harness.vec.v113.VecPartRelationType;
import com.foursoft.harness.vec.v113.VecPrimaryPartType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Properties VEC 1.2 models differently than VEC 1.1, read from VEC 1.2 objects through the VEC 1.1 API.
 */
class Vec12To11RemodelledPropertiesTest {

    private final CompatibilityContext downgrade = new Vec11XTo12XCompatibilityWrapper().getContext();

    @Test
    void wireSpecificationListsTheWireElementsOfEveryLevel() {
        final com.foursoft.harness.vec.v12x.VecWireElement cable = wireElement("cable");
        final com.foursoft.harness.vec.v12x.VecWireElement pair = wireElement("pair");
        pair.getSubWireElements().add(wireElement("core 1"));
        pair.getSubWireElements().add(wireElement("core 2"));
        cable.getSubWireElements().add(pair);
        cable.getSubWireElements().add(wireElement("drain wire"));
        final com.foursoft.harness.vec.v12x.VecWireSpecification source =
                new com.foursoft.harness.vec.v12x.VecWireSpecification();
        source.setWireElement(cable);

        final com.foursoft.harness.vec.v113.VecWireSpecification downgraded = proxy(source);

        assertThat(downgraded.getWireElements())
                .extracting(com.foursoft.harness.vec.v113.VecWireElement::getIdentification)
                .containsExactly("cable", "pair", "core 1", "core 2", "drain wire");
        assertThat(downgraded.getWireElements()).isSameAs(downgraded.getWireElements());
    }

    @Test
    void primaryPartUsageTypeUnknownToVec11BecomesTheGeneralOne() {
        final com.foursoft.harness.vec.v12x.VecPartUsage boltTerminal = new com.foursoft.harness.vec.v12x.VecPartUsage();
        boltTerminal.setPrimaryPartUsageType(com.foursoft.harness.vec.v12x.VecPrimaryPartType.BOLT_TERMINAL);
        final com.foursoft.harness.vec.v12x.VecPartUsage ringTerminal = new com.foursoft.harness.vec.v12x.VecPartUsage();
        ringTerminal.setPrimaryPartUsageType(com.foursoft.harness.vec.v12x.VecPrimaryPartType.RING_TERMINAL);

        final com.foursoft.harness.vec.v113.VecPartUsage downgradedBolt = proxy(boltTerminal);
        final com.foursoft.harness.vec.v113.VecPartUsage downgradedRing = proxy(ringTerminal);

        assertThat(downgradedBolt.getPrimaryPartUsageType()).isEqualTo(VecPrimaryPartType.TERMINAL);
        assertThat(downgradedRing.getPrimaryPartUsageType()).isEqualTo(VecPrimaryPartType.RING_TERMINAL);
    }

    @Test
    void customPartRelationBecomesOptional() {
        final com.foursoft.harness.vec.v12x.VecPartRelation custom = new com.foursoft.harness.vec.v12x.VecPartRelation();
        custom.setRelationType(com.foursoft.harness.vec.v12x.VecPartRelationType.CUSTOM);
        custom.setCustomRelationExpression("two of all");
        final com.foursoft.harness.vec.v12x.VecPartRelation oneOfAll =
                new com.foursoft.harness.vec.v12x.VecPartRelation();
        oneOfAll.setRelationType(com.foursoft.harness.vec.v12x.VecPartRelationType.ONE_OF_ALL);

        final com.foursoft.harness.vec.v113.VecPartRelation downgradedCustom = proxy(custom);
        final com.foursoft.harness.vec.v113.VecPartRelation downgradedOneOfAll = proxy(oneOfAll);

        // VEC 1.1 requires a relation type and has no Custom, so the weakest statement stands in for it.
        assertThat(downgradedCustom.getRelationType()).isEqualTo(VecPartRelationType.OPTIONAL);
        assertThat(downgradedOneOfAll.getRelationType()).isEqualTo(VecPartRelationType.ONE_OF_ALL);
    }

    @Test
    void abrasionResistanceClassesAreTheRobustnessPropertiesOfTheirClass() {
        final com.foursoft.harness.vec.v12x.VecGeneralTechnicalPartSpecification gtps =
                new com.foursoft.harness.vec.v12x.VecGeneralTechnicalPartSpecification();
        gtps.getRobustnessProperties().add(robustness("AbrasionResistance", "B", "VDA"));
        gtps.getRobustnessProperties().add(robustness("LiquidIngressProtection", "7", "ISO 20653"));
        final com.foursoft.harness.vec.v12x.VecWireProtectionSpecification source =
                new com.foursoft.harness.vec.v12x.VecWireProtectionSpecification();
        final com.foursoft.harness.vec.v12x.VecDocumentVersion documentVersion =
                new com.foursoft.harness.vec.v12x.VecDocumentVersion();
        documentVersion.getSpecifications().add(gtps);
        documentVersion.getSpecifications().add(source);
        source.setParentDocumentVersion(documentVersion);

        final com.foursoft.harness.vec.v113.VecWireProtectionSpecification downgraded = proxy(source);

        // The class names the influence; the class key is the level, as the class key of VEC 1.1 is.
        assertThat(downgraded.getAbrasionResistanceClasses())
                .singleElement()
                .returns("B", com.foursoft.harness.vec.v113.VecAbrasionResistanceClass::getClassKey)
                .returns("VDA", com.foursoft.harness.vec.v113.VecAbrasionResistanceClass::getReferenceSystem);
    }

    private com.foursoft.harness.vec.v12x.VecRobustnessProperties robustness(final String clazz,
                                                                            final String classKey,
                                                                            final String referenceSystem) {
        final com.foursoft.harness.vec.v12x.VecRobustnessProperties robustness =
                new com.foursoft.harness.vec.v12x.VecRobustnessProperties();
        robustness.setClazz(clazz);
        robustness.setClassKey(classKey);
        robustness.setClassReferenceSystem(referenceSystem);
        return robustness;
    }

    private <T> T proxy(final Object source) {
        return downgrade.getWrapperProxyFactory().createProxy(source);
    }

    private com.foursoft.harness.vec.v12x.VecWireElement wireElement(final String identification) {
        final com.foursoft.harness.vec.v12x.VecWireElement wireElement = new com.foursoft.harness.vec.v12x.VecWireElement();
        wireElement.setIdentification(identification);
        return wireElement;
    }

}
