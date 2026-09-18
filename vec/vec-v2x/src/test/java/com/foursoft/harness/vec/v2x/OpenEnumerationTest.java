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
package com.foursoft.harness.vec.v2x;

import com.foursoft.harness.vec.common.openenum.CustomOpenEnumLiteral;
import com.foursoft.harness.vec.common.openenum.OpenEnumLiteral;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The behaviour of the open enumeration literals on the model itself: what a document containing an
 * unrecognized literal reads back as, and that such a document can be written in the first place.
 */
class OpenEnumerationTest {

    @Test
    void literalsAgreeWithTheStringsOfTheSampleDocument() {
        final VecContent content = new VecReader().read(TestFiles.getInputStream(TestFiles.SAMPLE_VEC));

        final List<VecDocumentVersion> documentVersions = content.getDocumentVersions();
        assertThat(documentVersions).isNotEmpty();

        for (final VecDocumentVersion documentVersion : documentVersions) {
            final VecDocumentTypeLiteral literal = documentVersion.getDocumentTypeLiteral();
            if (documentVersion.getDocumentType() == null) {
                assertThat(literal).isNull();
            } else {
                assertThat(literal).isNotNull();
                assertThat(literal.value()).isEqualTo(documentVersion.getDocumentType());
            }
        }
    }

    @Test
    void aDefinedLiteralIsTheEnumConstantItself() {
        final VecDocumentVersion documentVersion = new VecDocumentVersion();
        documentVersion.setDocumentTypeLiteral(VecDocumentType.PART_MASTER);

        assertThat(documentVersion.getDocumentType()).isEqualTo("PartMaster");
        assertThat(documentVersion.getDocumentTypeLiteral()).isSameAs(VecDocumentType.PART_MASTER);
        assertThat(documentVersion.getDocumentTypeLiteral()
                           .isCustom()).isFalse();
    }

    @Test
    void nullMeansUnsetAndNothingElse() {
        final VecDocumentVersion documentVersion = new VecDocumentVersion();
        assertThat(documentVersion.getDocumentTypeLiteral()).isNull();

        documentVersion.setDocumentTypeLiteral(VecDocumentType.PART_MASTER);
        documentVersion.setDocumentTypeLiteral(null);

        assertThat(documentVersion.getDocumentType()).isNull();
        assertThat(documentVersion.getDocumentTypeLiteral()).isNull();
    }

    @Test
    void aCustomLiteralIsReadBackAsACustomLiteralRatherThanNull() {
        final VecDocumentVersion documentVersion = new VecDocumentVersion();
        documentVersion.setDocumentType("AcmeInternalDocumentType");

        final VecDocumentTypeLiteral literal = documentVersion.getDocumentTypeLiteral();

        assertThat(literal).isInstanceOf(CustomOpenEnumLiteral.class);
        assertThat(literal.isCustom()).isTrue();
        assertThat(literal.value()).isEqualTo("AcmeInternalDocumentType");
        assertThat(literal).isEqualTo(new VecDocumentTypeLiteral.Custom("AcmeInternalDocumentType"));
    }

    @Test
    void aCustomLiteralSurvivesAWriteAndReadRoundTrip() {
        final VecContent written = new VecReader().read(TestFiles.getInputStream(TestFiles.SAMPLE_VEC));
        final VecDocumentVersion documentVersion = written.getDocumentVersions()
                .get(0);
        documentVersion.setDocumentTypeLiteral(new VecDocumentTypeLiteral.Custom("AcmeInternalDocumentType"));

        final String xml = new VecWriter().writeToString(written);
        assertThat(xml).contains("AcmeInternalDocumentType");

        // Reading must not fail on the unrecognized literal: the mapped property is a plain string.
        final VecContent read = new VecReader().read(
                new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        final VecDocumentVersion reread = read.getDocumentVersions()
                .get(0);

        assertThat(reread.getDocumentType()).isEqualTo("AcmeInternalDocumentType");
        assertThat(reread.getDocumentTypeLiteral()
                           .isCustom()).isTrue();
    }

    @Test
    void repeatingPropertiesHaveTypedAccessorsToo() {
        final VecItemVersion itemVersion = new VecPartVersion();
        itemVersion.addChangeRestrictionLiteral(VecChangeRestrictionType.SAFETY);
        itemVersion.addChangeRestrictionLiteral(new VecChangeRestrictionTypeLiteral.Custom("AcmeRestriction"));

        assertThat(itemVersion.getChangeRestrictions()).containsExactly("Safety", "AcmeRestriction");
        assertThat(itemVersion.getChangeRestrictionLiterals())
                .extracting(OpenEnumLiteral::value)
                .containsExactly("Safety", "AcmeRestriction");

        itemVersion.setChangeRestrictionLiterals(List.of(VecChangeRestrictionType.SAFETY));
        assertThat(itemVersion.getChangeRestrictions()).containsExactly("Safety");
    }

    @Test
    void aLiteralContributedByAProviderIsPreferredOverACustomOne() {
        // TestOpenEnumLiterals is registered through META-INF/services.
        final VecDocumentVersion documentVersion = new VecDocumentVersion();
        documentVersion.setDocumentType(TestDocumentType.INTEGRATION_TEST_DOCUMENT.value());

        assertThat(documentVersion.getDocumentTypeLiteral())
                .isSameAs(TestDocumentType.INTEGRATION_TEST_DOCUMENT);
        assertThat(documentVersion.getDocumentTypeLiteral()
                           .isCustom()).isFalse();
    }

    @Test
    void aLiteralOfTheStandardIsPreferredOverAContributedOne() {
        // TestDocumentType also contributes "PartMaster", which the standard defines.
        final VecDocumentVersion documentVersion = new VecDocumentVersion();
        documentVersion.setDocumentType("PartMaster");

        assertThat(documentVersion.getDocumentTypeLiteral()).isSameAs(VecDocumentType.PART_MASTER);
    }

    @Test
    void unsetStandardContributedAndCustomAreAllDistinguishableInOneSwitch() {
        final VecDocumentVersion documentVersion = new VecDocumentVersion();

        assertThat(classify(documentVersion)).isEqualTo("unset");

        documentVersion.setDocumentTypeLiteral(VecDocumentType.PART_MASTER);
        assertThat(classify(documentVersion)).isEqualTo("part master");

        documentVersion.setDocumentTypeLiteral(TestDocumentType.INTEGRATION_TEST_DOCUMENT);
        assertThat(classify(documentVersion)).isEqualTo("contributed");

        documentVersion.setDocumentType("AcmeInternalDocumentType");
        assertThat(classify(documentVersion)).isEqualTo("custom: AcmeInternalDocumentType");
    }

    /**
     * The reason the literals are modelled as an interface with an enum implementing it: a single
     * switch tells apart an unset property, a literal of the standard, a literal an API consumer
     * contributed, and one nothing recognizes. A {@code default} is required because the interface is
     * not sealed, which is what makes the enumeration open in the first place.
     */
    private static String classify(final VecDocumentVersion documentVersion) {
        return switch (documentVersion.getDocumentTypeLiteral()) {
            case VecDocumentType.PART_MASTER -> "part master";
            case TestDocumentType.INTEGRATION_TEST_DOCUMENT -> "contributed";
            case VecDocumentTypeLiteral.Custom custom -> "custom: " + custom.value();
            case null -> "unset";
            default -> "other";
        };
    }

    @Test
    void aContributedLiteralCanBeWrittenLikeAnyOther() {
        final VecDocumentVersion documentVersion = new VecDocumentVersion();
        documentVersion.setDocumentTypeLiteral(TestDocumentType.INTEGRATION_TEST_DOCUMENT);

        assertThat(documentVersion.getDocumentType()).isEqualTo("IntegrationTestDocument");
    }

}
