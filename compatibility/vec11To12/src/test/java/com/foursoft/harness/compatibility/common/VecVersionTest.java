package com.foursoft.harness.compatibility.common;

import com.foursoft.harness.compatibility.vec11to12.common.VecVersion;
import com.foursoft.harness.vec.v12x.VecContent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VecVersionTest {

    @Test
    void testCanBeConvertedTo() {
        assertThat(VecVersion.VEC11X.canBeConvertedTo(VecVersion.VEC12X)).isTrue();
        assertThat(VecVersion.VEC12X.canBeConvertedTo(VecVersion.VEC11X)).isTrue();
    }

    @Test
    void testFindVersion() {
        assertThat(VecVersion.findVersion("1"))
                .isNotPresent();
        assertThat(VecVersion.findVersion("<VecVersion>1.0.0</VecVersion>"))
                .isNotPresent();
        assertThat(VecVersion.findVersion("1.2.3"))
                .isNotPresent();
        assertThat(VecVersion.findVersion("<VecVersion>1.2.3</VecVersion>"))
                .isPresent()
                .get()
                .isEqualTo(VecVersion.VEC12X);

        final VecContent vec12x = new VecContent();
        vec12x.setVecVersion(VecVersion.VEC12X.getCurrentVersion());
        assertThat(VecVersion.findVersion(vec12x))
                .isPresent()
                .get()
                .isEqualTo(VecVersion.VEC12X);
        vec12x.setVecVersion("1.2.3");
        assertThat(VecVersion.findVersion(vec12x))
                .isPresent()
                .get()
                .isEqualTo(VecVersion.VEC12X);

        final com.foursoft.harness.vec.v113.VecContent vec11x = new com.foursoft.harness.vec.v113.VecContent();
        vec11x.setVecVersion(VecVersion.VEC11X.getCurrentVersion());
        assertThat(VecVersion.findVersion(vec11x))
                .isPresent()
                .get()
                .isEqualTo(VecVersion.VEC11X);
        vec11x.setVecVersion("1.1.1");
        assertThat(VecVersion.findVersion(vec11x))
                .isPresent()
                .get()
                .isEqualTo(VecVersion.VEC11X);

        assertThat(VecVersion.findVersion(new VecContent()))
                .isNotPresent();
    }

}