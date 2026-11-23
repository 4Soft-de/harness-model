/*-
 * ========================LICENSE_START=================================
 * Compatibility VEC 1.1.X To VEC 1.2.X
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
package com.foursoft.harness.compatibility.vec11to12.wrapper.vec12to11;

import com.foursoft.harness.vec.v113.VecPrimaryPartType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Maps the primary part types of VEC 1.2 to VEC 1.1.
 * <p>
 * VEC 1.2 added primary part types which VEC 1.1 does not know. As the primary part type of a part version and
 * the primary part usage type of a part usage are mandatory in VEC 1.1, such a type becomes the closest, more
 * general type VEC 1.1 offers instead of none.
 */
final class PrimaryPartTypeMapping {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrimaryPartTypeMapping.class);

    private static final Map<com.foursoft.harness.vec.v12x.VecPrimaryPartType, VecPrimaryPartType> REPLACEMENTS =
            Map.of(com.foursoft.harness.vec.v12x.VecPrimaryPartType.BOLT_TERMINAL, VecPrimaryPartType.TERMINAL,
                   com.foursoft.harness.vec.v12x.VecPrimaryPartType.OPEN_WIRE_END, VecPrimaryPartType.TERMINAL,
                   com.foursoft.harness.vec.v12x.VecPrimaryPartType.CAPACITOR, VecPrimaryPartType.EE_COMPONENT,
                   com.foursoft.harness.vec.v12x.VecPrimaryPartType.DIODE, VecPrimaryPartType.EE_COMPONENT,
                   com.foursoft.harness.vec.v12x.VecPrimaryPartType.FERRITE, VecPrimaryPartType.OTHER);

    /**
     * @param type Primary part type of VEC 1.2, not {@code null}.
     * @return The same type in VEC 1.1, or the closest more general one if VEC 1.1 does not have it.
     */
    VecPrimaryPartType toVec11(final com.foursoft.harness.vec.v12x.VecPrimaryPartType type) {
        final VecPrimaryPartType replacement = REPLACEMENTS.get(type);
        if (replacement != null) {
            LOGGER.debug("Primary part type {} does not exist in VEC 1.1, it is converted to {}.", type,
                         replacement);
            return replacement;
        }
        return VecPrimaryPartType.valueOf(type.name());
    }

}
