package com.foursoft.jaxb.navext.runtime.io.write.id;

import com.foursoft.jaxb.navext.runtime.model.ModifiableIdentifiable;

public interface IdGenerator {

    /**
     * creates unique id for every category!
     *
     * @param object category
     * @return unique id
     */
    String getNextId(final Object object);

    /**
     * creates and set unique id for every category!
     *
     * @param object category
     */
    default void setXmlId(final ModifiableIdentifiable object) {
        final String xmlId = getNextId(object);
        object.setXmlId(xmlId);
    }

}
