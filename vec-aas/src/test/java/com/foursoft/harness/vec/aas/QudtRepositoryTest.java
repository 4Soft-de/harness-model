/*-
 * ========================LICENSE_START=================================
 * VEC to AAS Converter
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
package com.foursoft.harness.vec.aas;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QudtRepositoryTest {

    @Test
    void should_create_qudt_model() {
        final QudtRepository qudtRepository = new QudtRepository();

        assertThat(qudtRepository).isNotNull();
    }

    @Test
    void should_query_uneceCode() {
        final QudtRepository qudtRepository = new QudtRepository();

        final QudtRepository.QudtUnit e45 = qudtRepository.findByUnEceCommonCode("E45");
        assertThat(e45).isNotNull();
        assertThat(e45.uri()).isEqualTo("http://qudt.org/vocab/unit/MilliOHM");
        assertThat(e45.label()).isEqualTo("mΩ");
    }

}
