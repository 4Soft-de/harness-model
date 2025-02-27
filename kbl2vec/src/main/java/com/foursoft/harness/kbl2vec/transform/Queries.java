package com.foursoft.harness.kbl2vec.transform;

import com.foursoft.harness.kbl.v25.KBLContainer;
import com.foursoft.harness.kbl.v25.KblPart;

import java.util.Arrays;
import java.util.List;

public final class Queries {

    private Queries() {
        throw new AssertionError("Should not be instantiated");
    }

    public static com.foursoft.harness.kbl2vec.core.Query<KblPart> allParts(final KBLContainer container) {
        return () -> concat(container.getParts(), List.of(container.getHarness()), container.getHarness()
                .getModules());
    }

    private static <T> List<T> concat(final List<? extends T>... lists) {
        return Arrays.stream(lists)
                .flatMap(List::stream)
                .map(e -> (T) e)
                .toList();
    }

}
