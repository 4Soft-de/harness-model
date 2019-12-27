package de.foursoft.harness.xml.xjc.plugin;

import java.util.stream.Stream;

import com.sun.tools.xjc.model.CCustomizable;
import com.sun.tools.xjc.model.CPluginCustomization;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;

public abstract class AbstractCustomizationHandler {

    protected abstract CustomizationTags handledTag();

    protected abstract void handleField(final FieldOutline fieldOutline, final CPluginCustomization customization);

    protected abstract void handleClass(final ClassOutline classOutline, final CPluginCustomization customization);

    protected abstract void handleGlobal(final Outline outline, final CPluginCustomization customization);

    public void traverseModel(final Outline outline) {
        findCustomization(outline.getModel(), handledTag()).forEach(c -> {
            handleGlobal(outline, c);
        });
        for (final ClassOutline classOutline : outline.getClasses()) {
            findCustomization(classOutline.getTarget(), handledTag()).forEach(c -> {
                handleClass(classOutline, c);
            });
            for (final FieldOutline fieldOutline : classOutline.getDeclaredFields()) {
                findCustomization(fieldOutline.getPropertyInfo(), handledTag()).forEach(c -> {
                    handleField(fieldOutline, c);
                });
            }
        }
    }

    private Stream<CPluginCustomization> findCustomization(final CCustomizable customizable,
            final CustomizationTags tag) {
        return customizable.getCustomizations()
                .stream()
                .filter(tag::isCustomization);
    }

}
