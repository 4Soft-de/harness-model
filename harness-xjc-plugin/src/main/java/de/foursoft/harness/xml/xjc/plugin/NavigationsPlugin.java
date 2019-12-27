package de.foursoft.harness.xml.xjc.plugin;

import java.util.Collections;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.Outline;

public class NavigationsPlugin extends Plugin {

    private final AbstractCustomizationHandler[] handlers = new AbstractCustomizationHandler[] {
            new SelectorCustomizationHandler(), new ExtReferenceCustomizationHandler(),
            new ParentCustomizationHandler() };

    @Override
    public List<String> getCustomizationURIs() {
        return Collections.singletonList(CustomizationTags.NS);
    }

    @Override
    public boolean isCustomizationTagName(final String nsUri, final String localName) {
        return CustomizationTags.of(nsUri, localName)
                .isPresent();
    }

    @Override
    public String getOptionName() {
        return "Xext-navs";
    }

    @Override
    public String getUsage() {
        return "Usage!";
    }

    @Override
    public boolean run(final Outline outline, final Options opt, final ErrorHandler errorHandler) throws SAXException {
        for (final AbstractCustomizationHandler h : handlers) {
            h.traverseModel(outline);
        }

        return true;
    }
}
