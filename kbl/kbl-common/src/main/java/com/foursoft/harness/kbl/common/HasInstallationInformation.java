/*-
 * ========================LICENSE_START=================================
 * KBL Common
 * %%
 * Copyright (C) 2020 - 2023 4Soft GmbH
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
package com.foursoft.harness.kbl.common;

import com.foursoft.harness.kbl.common.util.StreamUtils;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface HasInstallationInformation<X extends HasInstruction> {

    List<X> getInstallationInformations();

    /**
     * Filters the list of {@link HasInstruction} key.
     * At most one element of the specified type is expected here. If more are found, the first one will be returned
     * and a warning will be logged.
     * If multiple values of the specified type are valid, then please use the other
     * {@link #installationInstructionValues(Predicate)} or {@link #installationInstructionValues(String)} method.
     *
     * @param instructionType defines the meaning of the value
     * @return the first value with the given type.
     */
    default Optional<String> getInstallationInstructionValue(final String instructionType) {
        return getInstallationInstructionValue(c -> c.getInstructionType().equals(instructionType));
    }

    /**
     * Filters the list of {@link HasInstruction} key.
     * At most one element of the specified type is expected here. If more are found, the first one will be returned
     * and a warning will be logged.
     * If multiple values of the specified type are valid, then please use the other
     * {@link #installationInstructionValues(Predicate)} or {@link #installationInstructionValues(String)} method.
     *
     * @param matches defines the meaning of the value
     * @return the first value with the given type.
     */
    default Optional<String> getInstallationInstructionValue(final Predicate<HasInstruction> matches) {
        return installationInstructionValues(matches)
                .collect(StreamUtils.findOneOrNone());
    }

    /**
     * Filters the list of {@link HasInstruction} key.
     *
     * @param instructionType defines the meaning of the value
     * @return a stream with all instruction values for the given type.
     */
    default Stream<String> installationInstructionValues(final String instructionType) {
        return installationInstructionValues(c -> c.getInstructionType().equalsIgnoreCase(instructionType));
    }

    /**
     * Filters the list of {@link HasInstruction} key.
     *
     * @param matches defines the meaning of the value
     * @return a stream with all instruction values for the given type.
     */
    default Stream<String> installationInstructionValues(final Predicate<HasInstruction> matches) {
        return getInstallationInformations()
                .stream()
                .filter(matches)
                .map(HasInstruction::getInstructionValue);
    }
}
