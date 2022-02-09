/*-
 * ========================LICENSE_START=================================
 * vec-common
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
package com.foursoft.vecmodel.common;

import com.foursoft.vecmodel.common.util.DelegationUtils;
import com.foursoft.vecmodel.common.util.StreamUtils;

import java.util.List;
import java.util.Optional;

public interface HasSpecifications<X extends HasIdentification> {

    List<X> getSpecifications();

    default <T extends X> List<T> getSpecificationsWithType(final Class<T> type) {
        return DelegationUtils.getFromListWithType(getSpecifications(), type);
    }

    /**
     * Gets the first specification with the given type.
     * <b>Warning: There might be multiple specifications with the given type.
     * Only use this method if you are sure there will just be one element!</b>
     *
     * @param type Class of T.
     * @param <T>  Type of Specification to filter for.
     * @return The first specification with the given type if found, else an empty optional.
     */
    default <T extends X> Optional<T> getSpecificationWithType(final Class<T> type) {
        return DelegationUtils.getFromListWithTypeAsStream(getSpecifications(), type)
                .collect(StreamUtils.findOneOrNone());
    }

    /**
     * Filters the list of Specifications by type and identification.
     *
     * @param type           derived classifiers
     * @param identification specifies a unique identification of the specification.
     * @return the first specification with the given type and identification.
     */
    default <T extends X> Optional<T> getSpecification(final Class<T> type, final String identification) {
        return DelegationUtils.getFromListWithTypeAsStream(getSpecifications(), type)
                .filter(c -> c.getIdentification().equals(identification))
                .collect(StreamUtils.findOneOrNone());
    }

}
