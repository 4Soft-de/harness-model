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
