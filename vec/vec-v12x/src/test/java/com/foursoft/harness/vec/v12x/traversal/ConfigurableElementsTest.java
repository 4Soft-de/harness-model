/*-
 * ========================LICENSE_START=================================
 * VEC 1.2.X
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
package com.foursoft.harness.vec.v12x.traversal;

import com.foursoft.harness.vec.v12x.VecConfigurableElement;
import com.foursoft.harness.vec.v12x.VecPartOccurrence;
import com.foursoft.harness.vec.v12x.VecVariantConfiguration;
import com.foursoft.harness.vec.v12x.navigations.ConfigurableElementNavs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"deprecation", "removal"})
class ConfigurableElementsTest {

    private final VecConfigurableElement unconfigured = new VecPartOccurrence();

    private VecVariantConfiguration configuration;
    private VecConfigurableElement configured;

    @BeforeEach
    void setUp() {
        configuration = new VecVariantConfiguration();
        configuration.setLogisticControlString("A + B");
        configuration.setLogisticControlExpression("A OR B");

        final VecPartOccurrence occurrence = new VecPartOccurrence();
        occurrence.setConfigInfo(configuration);
        configured = occurrence;
    }

    @Test
    void navigatesToTheVariantConfigurationOfAnElement() {
        assertThat(ConfigurableElements.toVariantConfiguration().from(configured)).contains(configuration);
    }

    @Test
    void navigatesToTheLogisticControlStringAndExpression() {
        assertThat(ConfigurableElements.toLogisticControlString().from(configured)).contains("A + B");
        assertThat(ConfigurableElements.toLogisticControlExpression().from(configured)).contains("A OR B");
    }

    @Test
    void navigatesNowhereForAnUnconfiguredElement() {
        assertThat(ConfigurableElements.toVariantConfiguration().from(unconfigured)).isEmpty();
        assertThat(ConfigurableElements.toLogisticControlString().from(unconfigured)).isEmpty();
        assertThat(ConfigurableElements.toLogisticControlExpression().from(unconfigured)).isEmpty();
    }

    @Test
    void navigatesNowhereForAConfigurationWithoutControlInformation() {
        configuration.setLogisticControlString(null);
        configuration.setLogisticControlExpression(null);

        assertThat(ConfigurableElements.toLogisticControlString().from(configured)).isEmpty();
        assertThat(ConfigurableElements.toLogisticControlExpression().from(configured)).isEmpty();
    }

    /**
     * Characterisation test for the deprecated {@link ConfigurableElementNavs}, which delegates to
     * {@link ConfigurableElements}. Can be removed together with {@link ConfigurableElementNavs}.
     */
    @Test
    void deprecatedNavigationsBehaveLikeTheirReplacement() {
        for (final VecConfigurableElement element : List.of(configured, unconfigured)) {
            assertThat(ConfigurableElementNavs.variantConfiguration().apply(element))
                    .isEqualTo(ConfigurableElements.toVariantConfiguration().from(element));
            assertThat(ConfigurableElementNavs.controlInformation().apply(element))
                    .isEqualTo(ConfigurableElements.toLogisticControlString().from(element));
            assertThat(ConfigurableElementNavs.controlExpression().apply(element))
                    .isEqualTo(ConfigurableElements.toLogisticControlExpression().from(element));
        }
    }

    /**
     * The deprecated navigations accept a {@code null} element, which the replacement does not; a navigation
     * starts at an element.
     */
    @Test
    void deprecatedNavigationsTolerateANullElement() {
        assertThat(ConfigurableElementNavs.variantConfiguration().apply(null)).isEmpty();
        assertThat(ConfigurableElementNavs.controlInformation().apply(null)).isEmpty();
        assertThat(ConfigurableElementNavs.controlExpression().apply(null)).isEmpty();
    }

}
