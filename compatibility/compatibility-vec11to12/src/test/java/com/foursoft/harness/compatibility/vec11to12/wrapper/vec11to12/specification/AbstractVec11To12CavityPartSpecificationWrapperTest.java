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
package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12.specification;

import com.foursoft.harness.compatibility.vec11to12.TestFiles;
import com.foursoft.harness.compatibility.vec11to12.wrapper.AbstractBaseWrapperTest;
import com.foursoft.harness.vec.common.util.StreamUtils;
import com.foursoft.harness.vec.v113.*;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

abstract class AbstractVec11To12CavityPartSpecificationWrapperTest<
        V1 extends com.foursoft.harness.vec.v113.VecCavityPartSpecification,
        V2 extends com.foursoft.harness.vec.v12x.VecCavityPartSpecification> extends AbstractBaseWrapperTest {

    private final Type[] genericTypes = ((ParameterizedType) getClass().getGenericSuperclass())
            .getActualTypeArguments();

    private V1 create11XPartSpecification()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Class<V1> v11xType = (Class<V1>) genericTypes[0];
        return v11xType.getConstructor().newInstance();
    }

    private Class<V2> get12XPartSpecificationClass() {
        return (Class<V2>) genericTypes[1];
    }

    @Test
    void invokeTest() throws Exception {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            final VecContent vecContent113 = new VecReader().read(inputStream);
            final VecDocumentVersion doc113 = vecContent113.getDocumentVersions().stream()
                    .min(Comparator.comparing(VecExtendableElement::getXmlId))
                    .orElse(null);
            assertThat(doc113).isNotNull();

            final VecUnit unit113 = vecContent113.getUnits().stream()
                    .min(Comparator.comparing(VecUnit::getXmlId))
                    .orElse(null);
            assertThat(unit113).isNotNull();

            final V1 partSpec113 = create11XPartSpecification();
            partSpec113.setXmlId("42");
            final VecValueRange value113 = new VecValueRange();
            value113.setMaximum(1.1);
            value113.setMinimum(0.1);
            value113.setXmlId("43");
            value113.setUnitComponent(unit113);
            partSpec113.setCavityDiameter(value113);
            doc113.getSpecifications().add(partSpec113);

            final com.foursoft.harness.vec.v12x.VecContent vecContent120 =
                    get11To12Context().getWrapperProxyFactory().createProxy(vecContent113);

            final com.foursoft.harness.vec.v12x.VecDocumentVersion doc120 =
                    vecContent120.getDocumentVersions().stream()
                            .filter(documentVersion -> documentVersion.getXmlId().equalsIgnoreCase(doc113.getXmlId()))
                            .findFirst().orElse(null);
            assertThat(doc120).isNotNull();

            final V2 partSpec120 = doc120.getSpecificationsWithType(get12XPartSpecificationClass()).stream()
                    .filter(specification -> specification.getXmlId().equalsIgnoreCase("42"))
                    .collect(StreamUtils.findOneOrNone()).orElse(null);
            assertThat(partSpec120).isNotNull();

            final List<com.foursoft.harness.vec.v12x.VecSize> cavityDimensions = partSpec120.getCavityDimensions();
            assertThat(cavityDimensions)
                    .isNotNull()
                    .hasSize(2);
            assertThat(cavityDimensions.get(0).getHeight())
                    .returns(0.1,
                             com.foursoft.harness.vec.v12x.VecNumericalValue::getValueComponent);
        }
    }

}
