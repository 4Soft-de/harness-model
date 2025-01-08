package com.foursoft.harness.vec.scripting.variants;

import com.foursoft.harness.vec.scripting.Builder;
import com.foursoft.harness.vec.scripting.Locator;
import com.foursoft.harness.vec.v2x.VecConfigurableElement;
import com.foursoft.harness.vec.v2x.VecConfigurationConstraint;

public abstract class ConfigurableElementBuilder<SELF extends ConfigurableElementBuilder<SELF, T>,
        T extends VecConfigurableElement>
        implements Builder<T> {

    private final Locator<VecConfigurationConstraint> configurationConstraintLocator;

    public ConfigurableElementBuilder(Locator<VecConfigurationConstraint> configurationConstraintLocator) {
        this.configurationConstraintLocator = configurationConstraintLocator;
    }

    public SELF withConfigurationConstraint(final String configurationConstraintId) {
        final VecConfigurationConstraint constraint = configurationConstraintLocator.locate(configurationConstraintId);

        constraint.getConstrainedElements().add(element());

        return (SELF) this;
    }

    protected abstract T element();

}
