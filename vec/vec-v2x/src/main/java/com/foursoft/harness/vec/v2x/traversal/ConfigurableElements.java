/*-
 * ========================LICENSE_START=================================
 * VEC 2.X
 * %%
 * Copyright (C) 2020 - 2026 4Soft GmbH
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
package com.foursoft.harness.vec.v2x.traversal;

import com.foursoft.harness.vec.common.annotations.RequiresBackReferences;
import com.foursoft.harness.vec.common.traversal.MultiNavigation;
import com.foursoft.harness.vec.common.traversal.Navigations;
import com.foursoft.harness.vec.common.traversal.SingleNavigation;
import com.foursoft.harness.vec.v2x.VecConfigurableElement;
import com.foursoft.harness.vec.v2x.VecConfigurationConstraint;
import com.foursoft.harness.vec.v2x.VecVariantConfiguration;

import java.util.Optional;

/**
 * Navigations starting at a {@link VecConfigurableElement}, that is at any element whose presence can be
 * configured.
 */
public final class ConfigurableElements {

    private ConfigurableElements() {
        // hide default constructor
    }

    /**
     * Navigates to the {@link VecConfigurationConstraint}s constraining a configurable element.
     * <p>
     * This is the reverse of {@link VecConfigurationConstraint#getConstrainedElements()} and therefore only
     * yields elements on a model read with back references.
     *
     * @return A navigation to the configuration constraints of an element.
     */
    @RequiresBackReferences
    public static MultiNavigation<VecConfigurableElement, VecConfigurationConstraint> toConfigurationConstraints() {
        return Navigations.collection(VecConfigurableElement::getRefConfigurationConstraint);
    }

    /**
     * Navigates to the configuration of a configurable element.
     * <p>
     * VEC 2.X configures an element through the {@link VecConfigurationConstraint}s pointing at it, so the
     * navigation takes that way first: back to the constraints and forward to their configuration. Only if
     * this leads nowhere does it fall back to the element's own {@code ConfigInfo} association, which the
     * model deprecated for removal. The fallback also covers a model read without back references, where the
     * recommended way cannot be followed at all.
     *
     * @return A navigation to the variant configuration of an element.
     */
    @RequiresBackReferences
    @SuppressWarnings({"deprecation", "removal"})
    public static SingleNavigation<VecConfigurableElement, VecVariantConfiguration> toVariantConfiguration() {
        return element -> toConfigurationConstraints()
                .then(Navigations.nullable(VecConfigurationConstraint::getConfigInfo))
                .atMostOne()
                .from(element)
                .or(() -> Optional.ofNullable(element.getConfigInfo()));
    }

    /**
     * Navigates to the logistic control string of a configurable element.
     *
     * @return A navigation to the logistic control string of an element.
     * @see #toVariantConfiguration()
     */
    @RequiresBackReferences
    public static SingleNavigation<VecConfigurableElement, String> toLogisticControlString() {
        return toVariantConfiguration()
                .then(Navigations.nullable(VecVariantConfiguration::getLogisticControlString));
    }

    /**
     * Navigates to the logistic control expression of a configurable element.
     *
     * @return A navigation to the logistic control expression of an element.
     * @see #toVariantConfiguration()
     */
    @RequiresBackReferences
    public static SingleNavigation<VecConfigurableElement, String> toLogisticControlExpression() {
        return toVariantConfiguration()
                .then(Navigations.nullable(VecVariantConfiguration::getLogisticControlExpression));
    }

}
