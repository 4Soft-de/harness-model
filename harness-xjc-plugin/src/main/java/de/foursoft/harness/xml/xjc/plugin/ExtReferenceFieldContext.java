package de.foursoft.harness.xml.xjc.plugin;

import java.util.Optional;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;

public class ExtReferenceFieldContext {

    private final Outline outline;
    private final CPluginCustomization customization;
    private final FieldOutline fieldOutline;

    public ExtReferenceFieldContext(final FieldOutline fieldOutline, final CPluginCustomization customization) {
        this.outline = fieldOutline.parent()
                .parent();
        this.customization = customization;
        this.fieldOutline = fieldOutline;
    }

    public JCodeModel getCodeModel() {
        return outline.getCodeModel();
    }

    public JDefinedClass getSourceClass() {
        return fieldOutline.parent().implClass;
    }

    public JFieldVar getSourceField() {
        return getSourceClass().fields()
                .get(fieldOutline.getPropertyInfo()
                        .getName(false));
    }

    public boolean isCollection() {
        return fieldOutline.getPropertyInfo()
                .isCollection();
    }

    public JDefinedClass getTargetClass() {
        final Optional<JDefinedClass> schemaType = getSchemaTypeName()
                .map(name -> CodeModelUtility.findSchemaType(outline, name));
        if (schemaType.isPresent()) {
            return schemaType.get();
        }

        return getSelectorName().map(this::findSelectorType)
                .orElseThrow(() -> new RuntimeException(
                        "Target type can not be determined for: " + fieldOutline.getPropertyInfo()
                                .getSchemaComponent()));
    }

    public Optional<String> getInversePropertyName() {
        if (customization.element.hasAttribute(Consts.ATTR_INVERSE)) {
            return Optional.of(customization.element.getAttribute(Consts.ATTR_INVERSE));
        }
        return Optional.empty();
    }

    public String getGetterName() {
        return "get" + fieldOutline.getPropertyInfo()
                .getName(true);
    }

    public String getSetterName() {
        return "set" + fieldOutline.getPropertyInfo()
                .getName(true);
    }

    private Optional<String> getSelectorName() {
        if (customization.element.hasAttribute(Consts.ATTR_SELECTOR_TYPE)) {
            return Optional.of(customization.element.getAttribute(Consts.ATTR_SELECTOR_TYPE));
        }
        return Optional.empty();
    }

    private Optional<String> getSchemaTypeName() {
        if (customization.element.hasAttribute(Consts.ATTR_SCHEMA_TYPE)) {
            return Optional.of(customization.element.getAttribute(Consts.ATTR_SCHEMA_TYPE));
        }
        return Optional.empty();
    }

    private JDefinedClass findSelectorType(final String selectorName) {
        return getSourceClass().getPackage()
                ._getClass(selectorName);
    }

}
