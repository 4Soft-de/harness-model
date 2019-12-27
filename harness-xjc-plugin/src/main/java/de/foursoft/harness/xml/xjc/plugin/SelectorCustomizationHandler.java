package de.foursoft.harness.xml.xjc.plugin;

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
