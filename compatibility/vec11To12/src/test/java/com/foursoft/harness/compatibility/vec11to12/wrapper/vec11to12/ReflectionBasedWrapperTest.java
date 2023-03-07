package com.foursoft.harness.compatibility.vec11to12.wrapper.vec11to12;

import com.foursoft.harness.compatibility.vec11to12.Constants;
import com.foursoft.harness.compatibility.vec11to12.TestFiles;
import com.foursoft.harness.compatibility.vec11to12.util.DefaultVecReader;
import com.foursoft.harness.compatibility.vec11to12.wrapper.AbstractBaseWrapperTest;
import com.foursoft.harness.vec.v12x.VecContent;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionBasedWrapperTest extends AbstractBaseWrapperTest {

    @Test
    void invokeEnumTest() throws IOException {
        try (final InputStream inputStream = TestFiles.getInputStream(TestFiles.OLD_BEETLE)) {
            final VecContent vecContent = DefaultVecReader.read(inputStream, "test stream");

            vecContent.getPartVersions().stream()
                    .map(c -> c.getPrimaryPartType().getClass().getName())
                    .forEach(c -> assertThat(c).startsWith(Constants.PACKAGE_VEC12X));
        }
    }

} 