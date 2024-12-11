package com.foursoft.harness.vec.rdf.changes.patch;

import com.foursoft.harness.vec.rdf.common.NamingStrategy;
import org.junit.jupiter.api.Test;

import static com.foursoft.harness.vec.rdf.changes.test.TestUtils.loadResourceFromClasspath;

class VecModelWrapperTest {

    @Test
    void load() {
        VecModelWrapper wrapper = new VecModelWrapper(new NamingStrategy(),
                                                      "https://www.acme.inc/conversion/test/diff-test#");
        wrapper.load(loadResourceFromClasspath("/fixtures/mini-change/VEC-Change-Example I.xml"));
    }

}