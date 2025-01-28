package com.foursoft.harness.vec.scripting.factories;

import com.foursoft.harness.vec.v2x.VecMaterial;

public final class MaterialFactory {

    private MaterialFactory() {
        throw new AssertionError();
    }

    public static VecMaterial material(final String referenceSystem, final String key) {
        final VecMaterial material = new VecMaterial();
        material.setReferenceSystem(referenceSystem);
        material.setKey(key);
        return material;
    }

    public static VecMaterial dinEn13602(final String key) {
        final VecMaterial material = new VecMaterial();
        material.setReferenceSystem("DIN EN 13602");
        material.setKey(key);

        return material;
    }
}
