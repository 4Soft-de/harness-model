package de.foursoft.harness.xml;

public class JaxbModel<R, I> {

    private final R rootElement;
    private final IdLookupProvider<I> idLookup;

    public JaxbModel(final R rootElement, final IdLookupProvider<I> idLookupProvider) {
        this.rootElement = rootElement;
        this.idLookup = idLookupProvider;
    }

    /**
     * Copy constructor for use in inheriting classes. This can be used if the
     * {@link JaxbModel} returned by the {@link ExtendedUnmarshaller} shall be
     * extended with its own convenience methods.
     * 
     * @param model
     */
    protected JaxbModel(final JaxbModel<R, I> model) {
        this.rootElement = model.rootElement;
        this.idLookup = model.idLookup;
    }

    public R getRootElement() {
        return rootElement;
    }

    /**
     * A {@link IdLookupProvider} to allow the lookup of identifiable elements
     * in this model.
     * <p/>
     * The {@link IdLookupProvider} must be initialized during the unmarshalling
     * process (see
     * {@link ExtendedUnmarshaller#withIdMapper(Class, java.util.function.Function)}).
     * 
     * @return
     */
    public IdLookupProvider<I> getIdLookup() {
        if (idLookup == null) {
            throw new JaxbModelException("idLookup can not be used. It was not initialized during the unmarshalling.");
        }
        return idLookup;
    }

}
