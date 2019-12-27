package de.foursoft.harness.xml.xjc.plugin;

import java.util.Optional;
import java.util.stream.Stream;

import com.sun.tools.xjc.model.CPluginCustomization;

public enum CustomizationTags {

    EXT_REFERENCE("ext-reference"), PARENT("parent"), SELECTOR("selector");

    public static final String NS = "http://www.4soft.de/xjc-plugins/navigations";

    private final String tagName;

    CustomizationTags(final String tagName) {
        this.tagName = tagName;
    }

    public boolean isCustomization(final CPluginCustomization customization) {
        return NS.equals(customization.element.getNamespaceURI())
                && tagName.equals(customization.element.getLocalName());
    }

    public static Optional<CustomizationTags> of(final String nsUri, final String localName) {
        if (!NS.equals(nsUri)) {
            return Optional.empty();
        }
        return Stream.of(values())
                .filter(p -> p.tagName.equals(localName))
                .findAny();
    }

}
