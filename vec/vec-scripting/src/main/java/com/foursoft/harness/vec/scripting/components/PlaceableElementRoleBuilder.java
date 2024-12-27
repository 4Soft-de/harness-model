package com.foursoft.harness.vec.scripting.components;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.v2x.VecPlaceableElementRole;
import com.foursoft.harness.vec.v2x.VecPlaceableElementSpecification;

public class PlaceableElementRoleBuilder implements Builder<VecPlaceableElementRole> {

    private final VecPlaceableElementRole role = new VecPlaceableElementRole();

    public PlaceableElementRoleBuilder(String identification, VecPlaceableElementSpecification specification) {
        this.role.setIdentification(identification);
        this.role.setPlaceableElementSpecification(specification);
    }

    @Override public VecPlaceableElementRole build() {
        return role;
    }
}
