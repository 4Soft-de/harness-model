package com.foursoft.harness.kbl2vec.core;

import com.foursoft.harness.vec.v2x.VecLanguageCode;
import com.foursoft.harness.vec.v2x.VecValueDetermination;

public class ConversionProperties {
    private VecLanguageCode defaultLanguageCode = VecLanguageCode.DE;
    private String defaultMassInformationSource = "Series";
    private VecValueDetermination defaultValueDetermination = VecValueDetermination.MEASURED;
    private String defaultColorReferenceSystem = "ACME Inc.";

    public VecLanguageCode getDefaultLanguageCode() {
        return defaultLanguageCode;
    }

    public void setDefaultLanguageCode(final VecLanguageCode defaultLanguageCode) {
        this.defaultLanguageCode = defaultLanguageCode;
    }

    public String getDefaultMassInformationSource() {
        return defaultMassInformationSource;
    }

    public void setDefaultMassInformationSource(final String defaultMassInformationSource) {
        this.defaultMassInformationSource = defaultMassInformationSource;
    }

    public VecValueDetermination getDefaultValueDetermination() {
        return defaultValueDetermination;
    }

    public void setDefaultValueDetermination(final VecValueDetermination defaultValueDetermination) {
        this.defaultValueDetermination = defaultValueDetermination;
    }

    public String getDefaultColorReferenceSystem() {
        return defaultColorReferenceSystem;
    }

    public void setDefaultColorReferenceSystem(final String defaultColorReferenceSystem) {
        this.defaultColorReferenceSystem = defaultColorReferenceSystem;
    }
}
