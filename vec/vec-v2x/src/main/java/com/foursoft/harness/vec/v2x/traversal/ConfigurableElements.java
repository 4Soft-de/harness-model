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

import com.foursoft.harness.vec.common.traversal.Navigations;
import com.foursoft.harness.vec.common.traversal.SingleNavigation;
import com.foursoft.harness.vec.v2x.VecConfigurableElement;
import com.foursoft.harness.vec.v2x.VecVariantConfiguration;

/**
 * Navigations starting at a {@link VecConfigurableElement}, that is at any element whose presence can be
 * configured.
 */
public final class ConfigurableElements {

    private ConfigurableElements() {
        // hide default constructor
    }

    /**
     * Navigates to the configuration of a configurable element.
     * <p>
     * VEC 2.X deprecates the association this follows without offering a replacement for it, so this
     * navigation is deprecated for removal in the model's sense, not in this API's.
     *
     * @return A navigation to the variant configuration of an element.
     */
    @SuppressWarnings({"deprecation", "removal"})
    public static SingleNavigation<VecConfigurableElement, VecVariantConfiguration> toVariantConfiguration() {
        return Navigations.nullable(VecConfigurableElement::getConfigInfo);
    }

    /**
     * Navigates to the logistic control string of a configurable element.
     *
     * @return A navigation to the logistic control string of an element.
     */
    public static SingleNavigation<VecConfigurableElement, String> toLogisticControlString() {
        return toVariantConfiguration()
                .then(Navigations.nullable(VecVariantConfiguration::getLogisticControlString));
    }

    /**
     * Navigates to the logistic control expression of a configurable element.
     *
     * @return A navigation to the logistic control expression of an element.
     */
    public static SingleNavigation<VecConfigurableElement, String> toLogisticControlExpression() {
        return toVariantConfiguration()
                .then(Navigations.nullable(VecVariantConfiguration::getLogisticControlExpression));
    }

}
