package de.foursoft.harness.xml.cache;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

public class SimpleCacheTest {

    @Test
    public void testCacheIsWorking() {
        final SimpleCache<String, Object> sut = new SimpleCache<>((final String s) -> new Object());

        final Object a1 = sut.get("A");
        final Object b1 = sut.get("B");
        final Object a2 = sut.get("A");

        assertThat(a1).isSameAs(a2)
                .isNotSameAs(b1)
                .isNotNull();
    }
}
