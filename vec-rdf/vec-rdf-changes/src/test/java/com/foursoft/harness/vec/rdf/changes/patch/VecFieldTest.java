package com.foursoft.harness.vec.rdf.changes.patch;

import com.foursoft.harness.vec.v2x.VecExtendableElement;
import com.foursoft.harness.vec.v2x.VecPartOrUsageRelatedSpecification;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class VecFieldTest {

    @Test
    void should_return_true_on_xmlid() throws NoSuchFieldException {
        VecField vecField = new VecField(VecExtendableElement.class.getDeclaredField("xmlId"));

        assertThat(vecField.isXmlId()).isTrue();
    }

    @Test
    void should_return_false_on_xmlid() throws NoSuchFieldException {
        VecField vecField = new VecField(VecExtendableElement.class.getDeclaredField("customProperties"));

        assertThat(vecField.isXmlId()).isFalse();
    }

    @Test
    void should_return_true_on_transient() throws NoSuchFieldException {
        VecField vecField = new VecField(VecExtendableElement.class.getDeclaredField("refExternalMapping"));

        assertThat(vecField.isTransient()).isTrue();
    }

    @Test
    void should_return_false_on_transient() throws NoSuchFieldException {
        VecField vecField = new VecField(VecExtendableElement.class.getDeclaredField("customProperties"));

        assertThat(vecField.isTransient()).isFalse();
    }

    @Test
    void should_return_true_on_reference() throws NoSuchFieldException {
        VecField vecField = new VecField(VecPartOrUsageRelatedSpecification.class.getDeclaredField("describedPart"));

        assertThat(vecField.isReference()).isTrue();
    }

    @Test
    void should_return_false_on_reference() throws NoSuchFieldException {
        VecField vecField = new VecField(VecExtendableElement.class.getDeclaredField("customProperties"));

        assertThat(vecField.isReference()).isFalse();
    }

    @Test
    void should_return_true_on_collection() throws NoSuchFieldException {
        VecField vecField = new VecField(VecPartOrUsageRelatedSpecification.class.getDeclaredField("describedPart"));

        assertThat(vecField.isCollection()).isTrue();
    }

    @Test
    void should_return_false_on_collection() throws NoSuchFieldException {
        VecField vecField = new VecField(VecPartOrUsageRelatedSpecification.class.getDeclaredField("specialPartType"));

        assertThat(vecField.isCollection()).isFalse();
    }

}