/*-
 * ========================LICENSE_START=================================
 * VEC RDF Common
 * %%
 * Copyright (C) 2024 4Soft GmbH
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
package com.foursoft.harness.vec.rdf.common.meta.xmi;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;

public interface VecModelProvider {

    Optional<UmlField> findField(Field field);

    /**
     * This is planned to come from the UML in the future via stereotypes.
     *
     * @return
     */
    default Set<String> getAnonymousClasses() {
        return Set.of("AbstractLocalizedString",
                      "AliasIdentification",
                      "CartesianVector2D",
                      "CartesianVector3D",
                      "Color",
                      "DocumentClassification",
                      "FunctionalRequirement",
                      "Instruction",
                      "LocalizedString",
                      "LocalizedTypedString",
                      "MassInformation",
                      "Material",
                      "MaterialComposition",
                      "NumericalValue",
                      "Person",
                      "PowerConsumption",
                      "RobustnessProperties",
                      "Size",
                      "SoundDampingClass",
                      "TemperatureInformation",
                      "TerminalType",
                      "ValueRange",
                      "ValueWithUnit",
                      "WireLength",
                      "WireType",
                      "BooleanValueProperty",
                      "ComplexProperty",
                      "DateValueProperty",
                      "DoubleValueProperty",
                      "IntegerValueProperty",
                      "LocalizedStringProperty",
                      "NumericalValueProperty",
                      "SimpleValueProperty",
                      "ValueRangeProperty");
    }
}
