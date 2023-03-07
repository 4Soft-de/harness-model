/*-
 * ========================LICENSE_START=================================
 * compatibility-vec11tovec12
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
package com.foursoft.harness.compatibility.vec11to12.util;

import com.foursoft.harness.compatibility.core.util.ClassUtils;
import com.foursoft.harness.compatibility.vec11to12.TestFiles;
import com.foursoft.harness.compatibility.vec11to12.Vec11XTo12XProcessor;
import com.foursoft.harness.compatibility.vec11to12.common.VecCreator;
import com.foursoft.harness.compatibility.vec11to12.wrapper.AbstractBaseWrapperTest;
import com.foursoft.harness.vec.v113.VecContent;
import com.foursoft.harness.vec.v113.VecReader;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class ClassUtilsTest extends AbstractBaseWrapperTest {

    @Test
    void getMappedClass() throws ClassNotFoundException {
        final VecCreator vecCreator = new VecCreator(new Vec11XTo12XProcessor());
        final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE);
        final com.foursoft.harness.vec.v12x.VecContent contentByteBuddy =
                vecCreator.createVec(inputStream, "getMappedClass", com.foursoft.harness.vec.v12x.VecContent.class);

        final Class<?> mappedClass = ClassUtils.getNonProxyClass(contentByteBuddy.getClass());
        assertThat(mappedClass.getSimpleName()).isEqualTo(VecContent.class.getSimpleName());

        final VecContent vecContent = new VecReader()
                .read(TestFiles.getInputStream(TestFiles.OLD_BEETLE));
        final Class<?> contentByteBuddyClass = ClassUtils.getNonProxyClass(vecContent.getClass());
        assertThat(contentByteBuddyClass.getSimpleName()).isEqualTo(VecContent.class.getSimpleName());

        final Class<?> mappedClassByPackage = ClassUtils.getMappedClass(vecContent.getClass(),
                                                                        VecContent.class.getPackageName());
        assertThat(mappedClassByPackage.getSimpleName()).isEqualTo(VecContent.class.getSimpleName());
    }

}
