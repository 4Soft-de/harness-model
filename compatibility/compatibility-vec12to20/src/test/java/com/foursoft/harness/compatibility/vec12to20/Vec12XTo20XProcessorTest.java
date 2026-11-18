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
package com.foursoft.harness.compatibility.vec12to20;

import com.foursoft.harness.compatibility.vec.common.VecCreator;
import com.foursoft.harness.compatibility.vec.common.VecVersion;
import com.foursoft.harness.compatibility.vec12to20.util.InitializeFields;
import com.foursoft.harness.compatibility.vec12to20.util.Vec20XReader;
import com.foursoft.harness.vec.common.HasVecVersion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Every combination of source and target version the processor accepts, read from a stream and from content.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Vec12XTo20XProcessorTest {

    private String vec2xXml;

    @BeforeAll
    void writeVec2x() {
        final com.foursoft.harness.vec.v2x.VecContent upgraded =
                Vec20XReader.createVec20x(vec12xStream(), "part master");
        // A converted model is a proxy; its fields have to be filled before JAXB can write it.
        InitializeFields.initializeFields(upgraded);
        vec2xXml = new com.foursoft.harness.vec.v2x.VecWriter().writeToString(upgraded);
    }

    @Test
    void upgradesAVec12xStream() {
        assertVersion(Vec20XReader.createVec20x(vec12xStream(), "1.2 to 2.x"), VecVersion.VEC2XX);
    }

    @Test
    void readsAVec12xStreamAsItIs() {
        assertVersion(Vec20XReader.createVec12x(vec12xStream(), "1.2 to 1.2"), VecVersion.VEC12X);
    }

    @Test
    void downgradesAVec2xStream() {
        assertVersion(Vec20XReader.createVec12x(vec2xStream(), "2.x to 1.2"), VecVersion.VEC12X);
    }

    @Test
    void readsAVec2xStreamAsItIs() {
        assertVersion(Vec20XReader.createVec20x(vec2xStream(), "2.x to 2.x"), VecVersion.VEC2XX);
    }

    @Test
    void upgradesVec12xContent() {
        final com.foursoft.harness.vec.v12x.VecContent source =
                new com.foursoft.harness.vec.v12x.VecReader().read(vec12xStream());

        assertVersion(Vec20XReader.createVec20x(source, "1.2 to 2.x"), VecVersion.VEC2XX);
    }

    @Test
    void handsOverVec12xContentAsItIs() {
        final com.foursoft.harness.vec.v12x.VecContent source =
                new com.foursoft.harness.vec.v12x.VecReader().read(vec12xStream());

        assertThat(creator().createVec(source, "1.2 to 1.2", com.foursoft.harness.vec.v12x.VecContent.class))
                .isSameAs(source);
    }

    @Test
    void downgradesVec2xContent() {
        final com.foursoft.harness.vec.v2x.VecContent source =
                new com.foursoft.harness.vec.v2x.VecReader().read(vec2xStream());

        assertVersion(Vec20XReader.createVec12x(source, "2.x to 1.2"), VecVersion.VEC12X);
    }

    @Test
    void handsOverVec2xContentAsItIs() {
        final com.foursoft.harness.vec.v2x.VecContent source =
                new com.foursoft.harness.vec.v2x.VecReader().read(vec2xStream());

        assertThat(creator().createVec(source, "2.x to 2.x", com.foursoft.harness.vec.v2x.VecContent.class))
                .isSameAs(source);
    }

    private VecCreator creator() {
        return new VecCreator(new Vec12XTo20XProcessor());
    }

    private void assertVersion(final HasVecVersion content, final VecVersion expected) {
        assertThat(VecVersion.findVersion(content)).contains(expected);
    }

    private InputStream vec12xStream() {
        return TestFiles.getInputStream(TestFiles.PART_MASTER_V122);
    }

    private InputStream vec2xStream() {
        return new ByteArrayInputStream(vec2xXml.getBytes(StandardCharsets.UTF_8));
    }

}
