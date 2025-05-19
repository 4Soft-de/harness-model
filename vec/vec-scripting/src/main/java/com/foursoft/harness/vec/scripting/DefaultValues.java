/*-
 * ========================LICENSE_START=================================
 * VEC 2.x Scripting API (Experimental)
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
package com.foursoft.harness.vec.scripting;

public class DefaultValues {

    public static final String VARIANT_CONFIGURATION_SPEC_IDENTIFICATION = "VARIANTS";
    public static final String TOPO_SPEC_IDENTIFICATION = "TOPOLOGY";
    public static final String PLACEMENT_SPECIFICATION_IDENTIFICATION = "PLACEMENTS";
    public static final String ROUTING_SPECIFICATION_IDENTIFICATION = "ROUTINGS";
    public static final String CONFIG_CONSTRAINTS_SPEC_IDENTIFICATION = "CONFIG_CONSTRAINTS";
    private String companyName = "Acme Inc.";
    private String colorReferenceSystem = "Acme Inc.";
    private String materialReferenceSystem = "Acme Inc.";
    public static final String REQUIREMENTS_DESCRIPTION = "RequirementsDescription";

    public static final String COMP_COMPOSITION_SPEC_IDENTIFICATION = "COMPONENTS";
    public static final String MODULES_COMPOSITION_SPEC_IDENTIFICATION = "MODULES";

    public static final String CONNECTION_SPEC_IDENTIFICATION = "SCHEMATIC";

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(final String companyName) {
        this.companyName = companyName;
    }

    public String getColorReferenceSystem() {
        return colorReferenceSystem;
    }

    public void setColorReferenceSystem(final String colorReferenceSystem) {
        this.colorReferenceSystem = colorReferenceSystem;
    }

    public String getMaterialReferenceSystem() {
        return materialReferenceSystem;
    }

    public void setMaterialReferenceSystem(final String materialReferenceSystem) {
        this.materialReferenceSystem = materialReferenceSystem;
    }
}
