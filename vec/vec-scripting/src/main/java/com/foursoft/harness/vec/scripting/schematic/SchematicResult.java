package com.foursoft.harness.vec.scripting.schematic;

import com.foursoft.harness.vec.v2x.VecConnectionSpecification;
import com.foursoft.harness.vec.v2x.VecReusageSpecification;
import jakarta.annotation.Nonnull;

import java.util.List;

public record SchematicResult(@Nonnull VecConnectionSpecification connectionSpecification,
                              List<VecReusageSpecification> reusageSpecification) {
}
