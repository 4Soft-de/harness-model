package com.foursoft.jaxb.navext.runtime.io.write.id;

import com.foursoft.jaxb.navext.runtime.io.TestData;
import com.foursoft.jaxb.navext.runtime.model.Root;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdGeneratorTest {

    @Test
    void testSetXmlId() {
        final IdGenerator idGenerator = new SimpleIdGenerator.Builder()
                .withDelimiter("_")
                .withRemovePrefix(0)
                .build();

        final Root root = TestData.readBasicTest();
        idGenerator.setXmlId(root);

        assertThat(root).satisfies(c -> assertThat(c.getXmlId()).isEqualTo("root_1"));
    }

}