package com.foursoft.harness.vec.rdf.changes.patch;

import com.foursoft.harness.vec.rdf.common.NamingStrategy;
import org.junit.jupiter.api.Test;

class VecModelWrapperTest {

    @Test
    void load() {
        VecModelWrapper wrapper = new VecModelWrapper(new NamingStrategy(),
                                                      "https://www.acme.inc/conversion/test/diff-test#");
        wrapper.load(VecModelWrapperTest.class.getResourceAsStream("/fixtures/mini-change/VEC-Change-Example I.xml"));
    }

}