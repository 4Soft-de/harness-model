package com.foursoft.harness.vec.scripting.schematic;

import com.foursoft.harness.vec.v2x.VecConnection;

@FunctionalInterface
public interface ConnectionLookup {

    VecConnection find(String connectionID);
}
