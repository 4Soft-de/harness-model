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
package com.foursoft.harness.vec.common;

import com.foursoft.harness.vec.common.util.DelegationUtils;
import com.foursoft.harness.vec.common.util.StreamUtils;

import java.util.List;
import java.util.Optional;

@FunctionalInterface
public interface HasRoles<X> {

    List<X> getRoles();

    default <T extends X> List<T> getRolesWithType(final Class<T> type) {
        return DelegationUtils.getFromListWithType(getRoles(), type);
    }

    /**
     * Gets the first role with the given type.
     * <b>Warning: There might be multiple roles with the given type.
     * Only use this method if you are sure there will just be one element!</b>
     *
     * @param type Class of T.
     * @param <T>  Type of role to filter for.
     * @return The first role with the given type if found, else an empty optional.
     */
    default <T extends X> Optional<T> getRoleWithType(final Class<T> type) {
        return DelegationUtils.getFromListWithTypeAsStream(getRoles(), type)
                .collect(StreamUtils.findOneOrNone());
    }

}
