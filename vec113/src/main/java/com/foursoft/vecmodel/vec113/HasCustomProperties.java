/*-
 * ========================LICENSE_START=================================
 * vec113
 * %%
 * Copyright (C) 2020 - 2022 4Soft GmbH
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
package com.foursoft.vecmodel.vec113;

import com.foursoft.vecmodel.common.util.DelegationUtils;
import com.foursoft.vecmodel.common.util.StreamUtils;

import java.util.List;
import java.util.Optional;

public interface HasCustomProperties {

    List<VecCustomProperty> getCustomProperties();

    default <T extends VecCustomProperty> List<T> getCustomProperties(final Class<T> type) {
        return DelegationUtils.getFromListWithType(getCustomProperties(), type);
    }

    /**
     * Filters the list of CustomProperties by type and key.
     *
     * @param type         derived classifiers
     * @param propertyType defines the meaning of the value.
     * @return the first property with the given type and key.
     */
    default <T extends VecCustomProperty> Optional<T> getCustomProperty(final Class<T> type, final String propertyType) {
        return DelegationUtils.getFromListWithTypeAsStream(getCustomProperties(), type)
                .filter(c -> c.getPropertyType().equals(propertyType))
                .collect(StreamUtils.findOneOrNone());
    }
}
