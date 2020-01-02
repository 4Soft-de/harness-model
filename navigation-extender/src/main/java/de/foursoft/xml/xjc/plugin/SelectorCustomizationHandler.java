/*-
 * ========================LICENSE_START=================================
 * xjc-plugin
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
package de.foursoft.xml.xjc.plugin;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JPackage;
import com.sun.tools.xjc.generator.bean.BeanGenerator;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.outline.Aspect;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;

public class SelectorCustomizationHandler extends AbstractCustomizationHandler {

    @Override
    protected CustomizationTags handledTag() {
        return CustomizationTags.SELECTOR;
    }

    @Override
    protected void handleGlobal(final Outline outline, final CPluginCustomization customization) {
        final BeanGenerator generator = (BeanGenerator) outline;

        final String selectorName = customization.element.getAttribute(Consts.ATTR_NAME);

        final JDefinedClass selectorInterface = selectorInterface(generator.getUsedPackages(Aspect.IMPLEMENTATION)[0],
                selectorName);

        if (customization.element.hasAttribute(Consts.ATTR_IMPLEMENTS)) {
            selectorInterface._implements(outline.getCodeModel()
                    .ref(customization.element.getAttribute(Consts.ATTR_IMPLEMENTS)));
        }

        customization.markAsAcknowledged();
    }

    @Override
    protected void handleField(final FieldOutline fieldOutline, final CPluginCustomization customization) {
        throw new RuntimeException(CustomizationTags.SELECTOR + " customization is illegal for fields.");
    }

    @Override
    protected void handleClass(final ClassOutline classOutline, final CPluginCustomization customization) {
        final JPackage parentPackage = classOutline._package()
                ._package();

        final String selectorName = customization.element.getAttribute(Consts.ATTR_NAME);

        final JDefinedClass selectorInterface = selectorInterface(parentPackage, selectorName);

        classOutline.implClass._implements(selectorInterface);

        customization.markAsAcknowledged();
    }

    private JDefinedClass selectorInterface(final JPackage parentPackage, final String selectorName) {
        if (parentPackage.isDefined(selectorName)) {
            return parentPackage._getClass(selectorName);
        }
        try {
            final JDefinedClass iface = parentPackage._interface(selectorName);
            iface.javadoc()
                    .append("This is a selector interface for IDREF associations the reference a group of types, that have no shared inheritance hierarchy.");
            return iface;
        } catch (final JClassAlreadyExistsException e) {
            throw new RuntimeException("This should never happen.!");

        }

    }

}
