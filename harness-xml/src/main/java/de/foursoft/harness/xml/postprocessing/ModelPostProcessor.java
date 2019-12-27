package de.foursoft.harness.xml.postprocessing;

import javax.xml.bind.Unmarshaller;

public interface ModelPostProcessor {

    Class<?> getClassToHandle();

    /**
     * Called during the unmarshalling the JAXB unmarshalling process, after the
     * unmarshalling of <tt>target</tt> has been completed. IDREF relations are
     * not yet initialized at this point of the unmarshalling process. <a>
     * 
     * @see Unmarshaller.Listener#afterUnmarshal(Object, Object)
     * 
     * @param target
     *            non-null instance of JAXB mapped class prior to unmarshalling
     *            into it.
     * @param parent
     *            instance of JAXB mapped class that will reference
     *            <tt>target</tt>. <tt>null</tt> when <tt>target</tt> is root
     *            element.
     */
    void afterUnmarshalling(Object target, Object parent);

    /**
     * Called after the unmarshalling process of the whole model has been
     * completed.
     */
    void afterUnmarshallingCompleted(Object target);

    /**
     * Clear the state of the {@link ModelPostProcessor} to allow garbage
     * collection and reuse during multiple unmarshallings.
     */
    void clearState();

}
