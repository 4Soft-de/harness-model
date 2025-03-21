package com.foursoft.harness.vec.aas;

import com.foursoft.harness.navext.runtime.model.Identifiable;
import com.foursoft.harness.vec.rdf.common.NamingStrategy;
import com.foursoft.harness.vec.rdf.common.meta.VecField;

public class AasNamingStrategy extends NamingStrategy {

    @Override
    public String rdfName(final Identifiable identifiable) {
        return rdfName(identifiable.getClass()) + "_" + identifiable.getXmlId();
    }

    public String idShort(final Identifiable identifiable) {
        return identifiable.getXmlId();
    }

    public String idShort(final VecField field) {
        return field.getName();
    }


}
