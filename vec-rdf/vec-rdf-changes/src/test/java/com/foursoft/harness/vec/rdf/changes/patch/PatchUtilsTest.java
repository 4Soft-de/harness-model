package com.foursoft.harness.vec.rdf.changes.patch;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.foursoft.harness.vec.v2x.VecConnection;

class PatchUtilsTest {

    @Test
    void retrieveInheritanceHierarchy_should_return_all_classes() {
        String[] classes = PatchUtils.retrieveInheritanceHierarchy(VecConnection.class);

        assertThat(classes).containsExactly("VecConnection", "VecRoutableElement", "VecConfigurableElement",
                                            "VecExtendableElement");
    }

}