package com.foursoft.harness.vec.rdf.changes;

import com.foursoft.harness.vec.rdf.common.VEC;
import org.apache.jena.shared.PrefixMapping;

public class VecPrefixMapping {
    public static final PrefixMapping VecStandard = PrefixMapping.Factory.create()
            .withDefaultMappings(PrefixMapping.Standard)
            .setNsPrefix(VEC.PREFIX, VEC.URI)
            .setNsPrefix("vec-dbg", VEC.DEBUG_NS)
            .setNsPrefix("cs", VECCS.URI)
            .setNsPrefix("", "https://www.acme.inc/conversion/test/diff-test#")
            .lock();
}
