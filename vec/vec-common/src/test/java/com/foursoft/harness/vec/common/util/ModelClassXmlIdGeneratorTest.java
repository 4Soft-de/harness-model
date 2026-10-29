/*-
 * ========================LICENSE_START=================================
 * VEC Common
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
package com.foursoft.harness.vec.common.util;

import com.foursoft.harness.navext.runtime.model.ModifiableIdentifiable;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ModelClassXmlIdGeneratorTest {

    // Stand-in for a class of the generated Java API, only its simple name matters.
    private static class VecCartesianVector3D implements ModifiableIdentifiable {

        private String xmlId;

        VecCartesianVector3D(final String xmlId) {
            this.xmlId = xmlId;
        }

        @Override
        public String getXmlId() {
            return xmlId;
        }

        @Override
        public void setXmlId(final String xmlId) {
            this.xmlId = xmlId;
        }
    }

    @Test
    void generatesIdsFromTheModelClassNameAndACounter() {
        final ModelClassXmlIdGenerator generator = new ModelClassXmlIdGenerator();
        final VecCartesianVector3D first = new VecCartesianVector3D(null);
        final VecCartesianVector3D second = new VecCartesianVector3D(null);

        generator.createIdForXmlBean(first);
        generator.createIdForXmlBean(second);

        assertThat(first.getXmlId()).isEqualTo("CartesianVector3D_00000");
        assertThat(second.getXmlId()).isEqualTo("CartesianVector3D_00001");
    }

    @Test
    void putsTheCommonPrefixInFrontOfEveryId() {
        final VecCartesianVector3D bean = new VecCartesianVector3D(null);

        new ModelClassXmlIdGenerator("id_new_").createIdForXmlBean(bean);

        assertThat(bean.getXmlId()).isEqualTo("id_new_CartesianVector3D_00000");
    }

    @Test
    void keepsAnExistingId() {
        final VecCartesianVector3D bean = new VecCartesianVector3D("existing");

        new ModelClassXmlIdGenerator().createIdForXmlBean(bean);

        assertThat(bean.getXmlId()).isEqualTo("existing");
    }

}
