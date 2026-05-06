/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.2.X To VEC 2.0.X
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
package com.foursoft.harness.compatibility.vec12to20.wrapper.vec12to20;

import com.foursoft.harness.compatibility.vec12to20.wrapper.AbstractBaseWrapperTest;
import com.foursoft.harness.vec.v2x.VecColor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultWrapperTest extends AbstractBaseWrapperTest {

    @Test
    void immutableGlobalIri_should_be_null_initially() {
        final VecColor proxy = createProxy();

        assertThat(proxy.getImmutableGlobalIri()).isNull();
    }

    @Test
    void immutableGlobalIri_should_roundtrip_through_setter_and_getter() {
        final VecColor proxy = createProxy();

        proxy.setImmutableGlobalIri("urn:example:color:red");

        assertThat(proxy.getImmutableGlobalIri()).isEqualTo("urn:example:color:red");
    }

    private VecColor createProxy() {
        return get12To20Context().getWrapperProxyFactory()
                .createProxy(new com.foursoft.harness.vec.v12x.VecColor());
    }

}
