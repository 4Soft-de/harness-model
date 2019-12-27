/*-
 * ========================LICENSE_START=================================
 * xml-runtime
 * %%
 * Copyright (C) 2019 4Soft GmbH
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * =========================LICENSE_END==================================
 */
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
